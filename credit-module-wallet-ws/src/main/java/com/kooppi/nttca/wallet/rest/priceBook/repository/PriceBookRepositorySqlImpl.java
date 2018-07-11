package com.kooppi.nttca.wallet.rest.priceBook.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.CategoryStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.PriceBookStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ProductStatus;
import com.kooppi.nttca.portal.wallet.domain.PriceBookEnum.ServiceStatus;
import com.kooppi.nttca.wallet.common.persistence.domain.PriceBook;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class PriceBookRepositorySqlImpl extends BasicRepository implements PriceBookRepository {

	private EntityManager em;
	
	PriceBookRepositorySqlImpl() {}
	
	@Inject
	public PriceBookRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public ResultList<PriceBook> searchPriceBooksByOrFilter(String globalFilter, String orderBy, String orderSorting,
			Integer offset, Integer maxRows) {
		StringBuffer sb = new StringBuffer(" from PriceBook pb where 1=1 and pb.serviceStatus = :serviceStatus "
				+ "and pb.categoryStatus = :categoryStatus and pb.productStatus = :productStatus and pb.priceBookStatus = :priceBookStatus "
				+ "and pb.currencyCode = :currencyCode ");
		StringBuffer countSb = new StringBuffer(" select count(pb.uid) from PriceBook pb where 1=1 and pb.serviceStatus = :serviceStatus " + 
				"and pb.categoryStatus = :categoryStatus and pb.productStatus = :productStatus and pb.priceBookStatus = :priceBookStatus " + 
				"and pb.currencyCode = :currencyCode ");
		
		if(!Strings.isNullOrEmpty(globalFilter)) {
			sb.append(" and (");
			countSb.append(" and (");
			
			sb.append(" pb.serviceName like :serviceName");
			countSb.append(" pb.serviceName like :serviceName");
			
			sb.append(" or pb.shortName like :shortName");
			countSb.append("  or pb.shortName like :shortName");
			
			sb.append(" or pb.productName like :productName");
			countSb.append("  or pb.productName like :productName");
			
			sb.append(" or pb.partNo like :partNo");
			countSb.append("  or pb.partNo like :partNo");
			
			sb.append(" )");
			countSb.append(" )");
		}
		
		if (orderBy != null) {
			sb.append(" order by pb." + orderBy);
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by pb.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<PriceBook> tq = em.createQuery(sql, PriceBook.class);
		TypedQuery<Long> cq = em.createQuery(countSql, Long.class);
		
		if(!Strings.isNullOrEmpty(globalFilter)) {
			tq.setParameter("serviceName", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("serviceName", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("shortName", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("shortName", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("productName", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("productName", StringUtils.enclosePercentWildcard(globalFilter));
			
			tq.setParameter("partNo", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("partNo", StringUtils.enclosePercentWildcard(globalFilter));
		}
		
		tq.setParameter("serviceStatus", ServiceStatus.Active);
		cq.setParameter("serviceStatus", ServiceStatus.Active);
		tq.setParameter("categoryStatus", CategoryStatus.Active);
		cq.setParameter("categoryStatus", CategoryStatus.Active);
		tq.setParameter("productStatus", ProductStatus.Active);
		cq.setParameter("productStatus", ProductStatus.Active);
		tq.setParameter("priceBookStatus", PriceBookStatus.Active);
		cq.setParameter("priceBookStatus", PriceBookStatus.Active);
		tq.setParameter("currencyCode", "NTT$");
		cq.setParameter("currencyCode", "NTT$");
		
		if (offset != null && offset > 0) {
			tq.setFirstResult(offset);
		}
		if (maxRows != null && maxRows > 0) {
			tq.setMaxResults(maxRows);
		}
		
		List<PriceBook> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		return new ResultList<PriceBook>(result, totalCount.intValue());
	}

	@Override
	public Optional<PriceBook> findByUid(Long uid) {
		String query = " from PriceBook pb where pb.uid = :uid ";
		TypedQuery<PriceBook> tq = em.createQuery(query, PriceBook.class);
		tq.setParameter("uid", uid);
		return getSingleResult(tq);
	}

	@Override
	public Optional<PriceBook> findByPartNumber(String partNumber) {
		String query = " from PriceBook pb where pb.partNo = :partNumber ";
		TypedQuery<PriceBook> tq = em.createQuery(query, PriceBook.class);
		tq.setParameter("partNumber", partNumber);
		return getSingleResult(tq);
	}

	@Override
	public List<PriceBook> findPriceBooksByPartNumber(List<String> partNumbers) {
		if (!partNumbers.isEmpty()) {
			StringBuilder sb = new StringBuilder("SELECT pb FROM PriceBook pb where pb.partNo in :partNumbers");
			TypedQuery<PriceBook> tq = em.createQuery(sb.toString(), PriceBook.class).setParameter("partNumbers", partNumbers);
			return tq.getResultList();
		} else
			return Lists.newArrayList();
	}

	@Override
	public Optional<List<PriceBook>> findPriceBooksByBuName(String buName) {
		StringBuilder sb = new StringBuilder(
				"SELECT pb FROM PriceBook pb where pb.buMaster.name = :buName");
		TypedQuery<PriceBook> rq = em.createQuery(sb.toString(), PriceBook.class).setParameter("buName", buName);
		return Optional.ofNullable(rq.getResultList());
	}

	@Override
	public void create(PriceBook pb) {
        em.persist(pb);
        em.flush();
	}

	@Override
	public void update(PriceBook pb) {
        em.merge(pb);
	}

	@Override
	public List<PriceBook> getAllPriceBooks() {
		StringBuilder sb = new StringBuilder("SELECT pb FROM PriceBook pb");
		TypedQuery<PriceBook> tq = em.createQuery(sb.toString(), PriceBook.class);
		try {
			return tq.getResultList();
		} catch (NoResultException e) {
			return Lists.newArrayList();
		}
	}

	@Override
	public List<PriceBook> likeSearchPriceBooksByPartNumberAndProductName(String partNo, String productName) {
		StringBuffer sb = new StringBuffer(" from PriceBook pb where 1=1 ");
		
		if(!Strings.isNullOrEmpty(partNo)) {
			sb.append(" and pb.partNo like :partNo");
		}
		if(!Strings.isNullOrEmpty(productName)) {
			sb.append(" and pb.productName like :productName");
		}
		
		String sql = sb.toString();
		
		TypedQuery<PriceBook> tq = em.createQuery(sql, PriceBook.class);
		
		if(!Strings.isNullOrEmpty(partNo)) {
			tq.setParameter("partNo", StringUtils.enclosePercentWildcard(partNo));
		}
		
		if(!Strings.isNullOrEmpty(productName)) {
			tq.setParameter("productName", StringUtils.enclosePercentWildcard(productName));
		}
		try {
			return tq.getResultList();
		} catch (NoResultException e) {
			return Lists.newArrayList();
		}
	}

}
