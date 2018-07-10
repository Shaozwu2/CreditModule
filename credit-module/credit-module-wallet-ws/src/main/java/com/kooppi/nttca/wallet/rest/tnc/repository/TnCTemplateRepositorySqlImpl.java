package com.kooppi.nttca.wallet.rest.tnc.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.wallet.common.persistence.domain.TnC;
import com.kooppi.nttca.wallet.common.persistence.domain.TnCToPricebook;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class TnCTemplateRepositorySqlImpl extends BasicRepository implements TnCTemplateRepository {

	private EntityManager em;
	
	TnCTemplateRepositorySqlImpl() {}
	
	@Inject
	public TnCTemplateRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}

	@Override
	public Optional<TnC> findById(Long uid) {
		String query = " from TnC tc where tc.uid = :uid ";
		TypedQuery<TnC> tq = em.createQuery(query, TnC.class);
		tq.setParameter("uid", uid);
		return getSingleResult(tq);
	}

	@Override
	public ResultList<TnC> searchTnCsByOrFilter(String globalFilter, String orderBy,
			String orderSorting, Integer offset, Integer maxRows) {
		StringBuffer sb = new StringBuffer(" from TnC tc where 1=1 ");
		StringBuffer countSb = new StringBuffer(" select count(tc.uid) from TnC tc where 1=1 ");
		
		if(!Strings.isNullOrEmpty(globalFilter)) {
			sb.append(" and (");
			countSb.append(" and (");
			
			sb.append(" tc.templateName like :templateName");
			countSb.append(" tc.templateName like :templateName");
			
			sb.append(" or tc.description like :description");
			countSb.append("  or tc.description like :description");
			
			sb.append(" )");
			countSb.append(" )");
		}
		
		if (orderBy != null) {
			sb.append(" order by tc." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by tc.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<TnC> tq = em.createQuery(sql, TnC.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		if(!Strings.isNullOrEmpty(globalFilter)) {
			tq.setParameter("templateName", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("templateName", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("description", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("description", StringUtils.enclosePercentWildcard(globalFilter));
			
		}
		
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
		
		List<TnC> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		return new ResultList<TnC>(result, totalCount.intValue());
	}

	@Override
	public TnC saveAndRefresh(TnC tnc) {
		em.persist(tnc);
		em.flush();
		em.refresh(tnc);
		return tnc;
	}

	@Override
	public void deleteTnC(TnC tnc) {
		em.remove(tnc);
		
		// also delete mappings
		String sql = "DELETE FROM TnCToPricebook ttp WHERE ttp.tncId = :uid";
		Query query = em.createQuery(sql);
		query.setParameter("uid", tnc.getUid());
		query.executeUpdate();
	}
	
	@Override
	public TnCToPricebook saveAndRefresh(TnCToPricebook tnCToPricebook) {
		// check duplicate
		boolean isDuplicate = checkDuplicateMapping(tnCToPricebook);
		PortalExceptionUtils.throwIfTrue(isDuplicate && tnCToPricebook.getIsDefaultBu(), PortalErrorCode.TNC_TO_PRICEBOOK_DEFAULT_DUPLICATE);
		PortalExceptionUtils.throwIfTrue(isDuplicate, PortalErrorCode.TNC_TO_PRICEBOOK_DUPLICATE);
		
		// generate priority
		generatePriority(tnCToPricebook);
		
		// insert record
		em.persist(tnCToPricebook);
		em.flush();
		em.refresh(tnCToPricebook);
		return tnCToPricebook;
	}
	
	private void generatePriority(TnCToPricebook ttp) {
		/* 1. if isDefaultBu		priority = 0
		 * 2. if isAllBu			priority = 1
		 * 3. if can be drag		priority = each BU start from 1001
		 */
		if (ttp.getIsDefaultBu()) {
			ttp.setPriority(0);
		}
		else if (ttp.getIsAllBU()) {
			ttp.setPriority(1);
		}
		else {
			String sql = "SELECT FUNC('IFNULL', MAX(ttp.priority), 1000) FROM TnCToPricebook ttp WHERE"
					+ " ttp.buName = :buName AND ttp.priority >= 1001";
			TypedQuery<Long> query = em.createQuery(sql, Long.class);
			query.setParameter("buName", ttp.getBuName());
			ttp.setPriority(query.getSingleResult().intValue() + 1);
		}
	}
	
	private boolean checkDuplicateMapping(TnCToPricebook ttp) {
		String sql = "SELECT COUNT(1) FROM TnCToPricebook ttp WHERE";
		
		if (ttp.getBuName() == null)
			sql += " ttp.buName IS NULL";
		else
			sql += " ttp.buName = :buName";
		
		if (ttp.getOrganizationId() == null)
			sql += " AND ttp.organizationId IS NULL";
		else
			sql += " AND ttp.organizationId = :orgId";
		
		if (ttp.getPartNo() == null)
			sql += " AND ttp.partNo IS NULL";
		else
			sql += " AND ttp.partNo = :partNo";
		
		TypedQuery<Long> query = em.createQuery(sql, Long.class);
		
		if (ttp.getBuName() != null)
			query.setParameter("buName", ttp.getBuName());
		if (ttp.getOrganizationId() != null)
			query.setParameter("orgId", ttp.getOrganizationId());
		if (ttp.getPartNo() != null)
			query.setParameter("partNo", ttp.getPartNo());
		
		Long count = query.getSingleResult();
		if (count > 0L)
			return true;
		else
			return false;
	}
	
	/* 
	 * 5 filters:
	 * buFilter			--> ttp.buName
	 * partNoFilter		--> ttp.priceBook.partNo
	 * productFilter		--> ttp.priceBook.productName
	 * customerFilter	--> ttp.organizationId
	 * documentFilter	--> ttp.tnc.templateName
	 * default sorted by ttp.buName desc, ttp.priority desc
	 */
	@Override
	public ResultList<TnCToPricebook> searchTnCToPricebooksByAndFilter(String buFilter, String partNoFilter, String productFilter, String customerFilter, String documentFilter,
			String orderBy, String orderSorting, Integer offset, Integer maxRows) {
		
		StringBuffer sb = new StringBuffer(" from TnCToPricebook ttp where 1=1 ");
		StringBuffer countSb = new StringBuffer(" select count(ttp.uid) from TnCToPricebook ttp where 1=1 ");
		
		if (!Strings.isNullOrEmpty(buFilter)) {
			sb.append(" and ttp.buName = :buName");
			countSb.append(" and ttp.buName = :buName");
		}
		if (!Strings.isNullOrEmpty(partNoFilter)) {
			sb.append(" and ttp.priceBook.partNo = :partNo");
			countSb.append(" and ttp.priceBook.partNo = :partNo");
		}
		if (!Strings.isNullOrEmpty(productFilter)) {
			sb.append(" and ttp.priceBook.productName like :productName");
			countSb.append(" and ttp.priceBook.productName like :productName");
		}
		if (!Strings.isNullOrEmpty(customerFilter)) {
			sb.append(" and ttp.organizationId = :organizationId");
			countSb.append(" and ttp.organizationId = :organizationId");
		}
		if (!Strings.isNullOrEmpty(documentFilter)) {
			sb.append(" and ttp.tnc.templateName like :documentName");
			countSb.append(" and ttp.tnc.templateName like :documentName");
		}
		
		if (orderBy != null) {
			sb.append(" order by ttp." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by ttp.buName desc, ttp.priority desc");
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<TnCToPricebook> tq = em.createQuery(sql, TnCToPricebook.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);

		if (!Strings.isNullOrEmpty(buFilter)) {
			tq.setParameter("buName", buFilter);
			cq.setParameter("buName", buFilter);
		}
		if (!Strings.isNullOrEmpty(partNoFilter)) {
			tq.setParameter("partNo", partNoFilter);
			cq.setParameter("partNo", partNoFilter);
		}
		if (!Strings.isNullOrEmpty(productFilter)) {
			tq.setParameter("productName", StringUtils.enclosePercentWildcard(productFilter));
			cq.setParameter("productName", StringUtils.enclosePercentWildcard(productFilter));
		}
		if (!Strings.isNullOrEmpty(customerFilter)) {
			tq.setParameter("organizationId", customerFilter);
			cq.setParameter("organizationId", customerFilter);
		}
		if (!Strings.isNullOrEmpty(documentFilter)) {
			tq.setParameter("documentName", StringUtils.enclosePercentWildcard(documentFilter));
			cq.setParameter("documentName", StringUtils.enclosePercentWildcard(documentFilter));
		}
		
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
		
		List<TnCToPricebook> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		return new ResultList<TnCToPricebook>(result, totalCount.intValue());
	}
	
	@Override
	public Optional<TnCToPricebook> findTncToPricebookByUid(Long uid) {
		String query = " from TnCToPricebook ttp where ttp.uid = :uid ";
		TypedQuery<TnCToPricebook> tq = em.createQuery(query, TnCToPricebook.class);
		tq.setParameter("uid", uid);
		return getSingleResult(tq);
	}
	
	@Override
	public void deleteTnCToPricebook(TnCToPricebook tnCToPricebook) {
		em.remove(tnCToPricebook);
	}
	
	@Override
	public TnC findDefaultTnC() {
		StringBuffer sb = new StringBuffer("select tnc from TnC tnc where tnc.isDefault = 1");
		TypedQuery<TnC> query = em.createQuery(sb.toString(), TnC.class);
		List<TnC> tncList = query.getResultList();
		if (tncList != null && !tncList.isEmpty())
			return tncList.get(0);
		else
			return null;
	}

	//TODO:TBC
	@Override
	public List<TnCToPricebook> searchTnCToPricebooksByCustomerIdAndPartNo(String buName, String organizationId, String partNo) {
		StringBuffer sb = new StringBuffer(" from TnCToPricebook ttp where 1=1");
		
		sb.append(" and (");
		 
		sb.append("	ttp.priceBook.partNo = :partNo and ttp.isAllCustomer=1");
		
		sb.append("	or (ttp.priceBook.partNo = :partNo and ttp.organizationId = :organizationId)");

		sb.append(" or (ttp.isAllProduct = 1 and ttp.organizationId = :organizationId and (ttp.buName = :buName or ttp.isAllBu=1))");

		sb.append(" or ttp.isDefaultBu = 1");
		
		sb.append(" )");
		
		sb.append(" order by ttp.buName desc, ttp.priority desc");
		
		String sql = sb.toString();
		
		TypedQuery<TnCToPricebook> tq = em.createQuery(sql, TnCToPricebook.class);

		tq.setParameter("buName", buName);
		tq.setParameter("partNo", partNo);
		tq.setParameter("organizationId", organizationId);
		
		try {
			return tq.getResultList();
		} catch (NoResultException e) {
			return Lists.newArrayList();
		}
	}
	
	// swap priority of TnC1(uid = uid1) and TnC2(uid = uid2)
	@Override
	public void swapTnCMappingPriority(Long uid1, Long uid2) {
		String nativeSql = "UPDATE tnc_to_price_book t1 "
				+ "INNER JOIN tnc_to_price_book t2 "
				+ "ON (t1.uid, t2.uid) IN ((?1, ?2), (?2, ?1)) "
				+ "SET t1.priority = t2.priority";
		Query query = em.createNativeQuery(nativeSql);
		query.setParameter(1, uid1);
		query.setParameter(2, uid2);
		query.executeUpdate();
	}
}
