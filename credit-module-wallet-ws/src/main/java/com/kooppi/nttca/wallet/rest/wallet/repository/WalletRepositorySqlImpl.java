package com.kooppi.nttca.wallet.rest.wallet.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.common.base.Strings;
import com.kooppi.nttca.ce.payment.repository.ResultList;
import com.kooppi.nttca.portal.common.filter.context.RequestContextImpl;
import com.kooppi.nttca.portal.common.repository.BasicRepository;
import com.kooppi.nttca.portal.common.utils.StringUtils;
import com.kooppi.nttca.portal.wallet.domain.WalletStatus;
import com.kooppi.nttca.portal.wallet.domain.WalletType;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;
import com.kooppi.nttca.wallet.persistence.producer.WalletDataSource;

@ApplicationScoped
public class WalletRepositorySqlImpl extends BasicRepository implements WalletRepository{

	private EntityManager em;
	
	WalletRepositorySqlImpl(){}
	
	@Inject
	private WalletRepositorySqlImpl(@WalletDataSource EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Optional<Wallet> findWalletById(String walletId) {
		String query = "from Wallet w where w.walletId = :walletId";
		TypedQuery<Wallet> tq = em.createQuery(query,Wallet.class);
		tq.setParameter("walletId", walletId);
		return getSingleResult(tq);
	}
	
	@Override
	public Wallet add(Wallet cm) {
		em.persist(cm);
		em.flush();
		em.refresh(cm);
		cm.generateWalletId();
		return cm;
	}

	@Override
	public List<Wallet> findWalletByOrganizationId(String organizationId) {
		String query = "from Wallet w where w.organizationId = :organizationId";
		TypedQuery<Wallet> tq = em.createQuery(query,Wallet.class);
		tq.setParameter("organizationId", organizationId);
		return tq.getResultList();
	}
	
	@Override
	public Optional<Wallet> findMainWalletByOrganizationId(String organizationId) {
		StringBuffer sb = new StringBuffer("from Wallet w where w.organizationId = :organizationId");
		sb.append(" and w.walletType = :walletType");
		
		String sql = sb.toString();		
		TypedQuery<Wallet> tq = em.createQuery(sql,Wallet.class);
		tq.setParameter("organizationId", organizationId);
		tq.setParameter("walletType", WalletType.MAIN_WALLET);
		
		System.out.println(WalletType.MAIN_WALLET.toString());
		return getSingleResult(tq);
	}
	
//	@Override
//	public void adjustBalance(Wallet wallet, Integer adjustment) {
//		String query = "update CreditPool p set p.balance = p.balance + :adjustment, p.available = p.available + :adjustment where p.creditPoolId = :creditPoolId";
//		Query q = em.createQuery(query);
//		q.setParameter("adjustment", adjustment);
//		q.setParameter("creditPoolId", wallet.getMainCreditPool().getCreditPoolId());
//		q.executeUpdate();
//	}

	@Override
	public List<Wallet> findAllWallets() {
		return em.createQuery("SELECT u FROM Wallet u", Wallet.class).getResultList();
	}

	@Override
	public ResultList<Wallet> searchWalletByCustomer(String organizationId) {
		StringBuffer sb = new StringBuffer(" from Wallet w where 1=1");
		if (!Strings.isNullOrEmpty(organizationId)) {
			sb.append(" and w.organizationId = :organizationId");
		}
		
		String sql = sb.toString();
		TypedQuery<Wallet> tq = em.createQuery(sql,Wallet.class);
		
		if (!Strings.isNullOrEmpty(organizationId)) {
			tq.setParameter("organizationId", organizationId);
		}
		
		List<Wallet> result = tq.getResultList();
		
		return new ResultList<Wallet>(result, result.size());
	}
	
	@Override
	public ResultList<Wallet> searchWalletsByAndFilters(RequestContextImpl rc, String organizationId, String organizationName, String userId, String parentOrganizationId,
			String parentOrganizationName, String walletId, String walletName, WalletType walletType, WalletStatus status, Integer minBalance, 
			Integer maxBalance, Integer minAvailable, Integer maxAvailable, Integer minCreditBuffer, Integer maxCreditBuffer, 
			Integer minAlertThreshold, Integer maxAlertThreshold, String orderBy, String orderSorting, Integer offset, Integer maxRows) {
		
		StringBuffer sb = new StringBuffer(" from Wallet w where 1=1");
		StringBuffer countSb = new StringBuffer(" select count(w.walletId) from Wallet w where 1=1");
		
//		if (!rc.isSystemRequest()) {
//			User user = rc.getUser().get();
//			if (!(user.getPortalRole().equals(PortalRole.SUPER_ADMIN) || user.getPortalRole().equals(PortalRole.ENTERPRISE_ADMIN))) {
				if (!Strings.isNullOrEmpty(organizationId)) {
					sb.append(" and w.organizationId = :organizationId");
					countSb.append(" and w.organizationId = :organizationId");
				}
//			} 
//		}	
		
//		if (!Strings.isNullOrEmpty(parentOrganizationId)) {
//			sb.append(" and w.parentOrganizationId like :parentOrganizationId");
//			countSb.append(" and w.parentOrganizationId like :parentOrganizationId");
//		}
		
		if (!Strings.isNullOrEmpty(organizationName)) {
			sb.append(" and w.organizationName like :organizationName");
			countSb.append(" and w.organizationName like :organizationName");
		}

		if (!Strings.isNullOrEmpty(userId)) {
			sb.append(" and p.userId like :userId");
			countSb.append(" and p.userId like :userId");
		}

//		if (!Strings.isNullOrEmpty(parentOrganizationName)) {
//			sb.append(" and w.parentOrganizationName like :parentOrganizationName");
//			countSb.append(" and w.parentOrganizationName like :parentOrganizationName");
//		}

		if (!Strings.isNullOrEmpty(walletId)) {
			sb.append(" and w.walletId like :walletId");
			countSb.append(" and w.walletId like :walletId");
		}

		if (!Strings.isNullOrEmpty(walletName)) {
			sb.append(" and w.name like :walletName");
			countSb.append(" and w.name like :walletName");
		}
		
		if (walletType !=null) {
			sb.append(" and w.walletType = :walletType");
			countSb.append(" and w.walletType = :walletType");
		}

		if (status != null ) {
			sb.append(" and w.walletStatus = :status");
			countSb.append(" and w.walletStatus = :status");
		}

		if (minBalance != null) {
			sb.append(" and w.balance >= :minBalance");
			countSb.append(" and w.balance >= :minBalance");
		}

		if (maxBalance != null) {
			sb.append(" and w.balance <= :maxBalance");
			countSb.append(" and w.balance <= :maxBalance");
		}

		if (minAvailable != null) {
			sb.append(" and w.available >= :minAvailable");
			countSb.append(" and w.available >= :minAvailable");
		}

		if (maxAvailable != null) {
			sb.append(" and w.available <= :maxAvailable");
			countSb.append(" and w.available <= :maxAvailable");
		}

		if (minCreditBuffer != null) {
			sb.append(" and w.creditBuffer >= :minCreditBuffer");
			countSb.append(" and w.creditBuffer >= :minCreditBuffer");
		}

		if (maxCreditBuffer != null) {
			sb.append(" and w.creditBuffer <= :maxCreditBuffer");
			countSb.append(" and w.creditBuffer <= :maxCreditBuffer");
		}

		if (minAlertThreshold != null) {
			sb.append(" and w.alertThreshold >= :minAlertThreshold");
			countSb.append(" and w.alertThreshold >= :minAlertThreshold");
		}

		if (maxAlertThreshold != null) {
			sb.append(" and w.alertThreshold <= :maxAlertThreshold");
			countSb.append(" and w.alertThreshold <= :maxAlertThreshold");
		}
		
		if (orderBy != null) {
			
//			if(orderBy.equals("balance") || orderBy.equals("available")){
//				sb.append(" order by c." + orderBy);
//			}else{
				sb.append(" order by w." + orderBy);
//			}
			
			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by w.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<Wallet> tq = em.createQuery(sql,Wallet.class);
		TypedQuery<Long> cq = em.createQuery(countSql,Long.class);
		
//		if (!rc.isSystemRequest()) {
//			User user = rc.getUser().get();
//			if (!(user.getPortalRole().equals(PortalRole.SUPER_ADMIN) || user.getPortalRole().equals(PortalRole.ENTERPRISE_ADMIN))) {
				if (!Strings.isNullOrEmpty(organizationId)) {
					tq.setParameter("organizationId", organizationId);
					cq.setParameter("organizationId", organizationId);
				}
//			} 
//		}
	
		if (!Strings.isNullOrEmpty(organizationName)) {
			tq.setParameter("organizationName", StringUtils.enclosePercentWildcard(organizationName));
			cq.setParameter("organizationName", StringUtils.enclosePercentWildcard(organizationName));
		}

		if (!Strings.isNullOrEmpty(userId)) {
			tq.setParameter("userId", StringUtils.enclosePercentWildcard(userId));
			cq.setParameter("userId", StringUtils.enclosePercentWildcard(userId));
		}

//		if (!Strings.isNullOrEmpty(parentOrganizationId)) {
//			tq.setParameter("parentOrganizationId", StringUtils.enclosePercentWildcard(parentOrganizationId));
//			cq.setParameter("parentOrganizationId", StringUtils.enclosePercentWildcard(parentOrganizationId));
//		}

//		if (!Strings.isNullOrEmpty(parentOrganizationName)) {
//			tq.setParameter("parentOrganizationName", StringUtils.enclosePercentWildcard(parentOrganizationName));
//			cq.setParameter("parentOrganizationName", StringUtils.enclosePercentWildcard(parentOrganizationName));
//		}

		if (!Strings.isNullOrEmpty(walletId)) {
			tq.setParameter("walletId", StringUtils.enclosePercentWildcard(walletId));
			cq.setParameter("walletId", StringUtils.enclosePercentWildcard(walletId));
		}

		if (!Strings.isNullOrEmpty(walletName)) {
			tq.setParameter("walletName", StringUtils.enclosePercentWildcard(walletName));
			cq.setParameter("walletName", StringUtils.enclosePercentWildcard(walletName));
		}
		
		if (walletType != null) {
			tq.setParameter("walletType", walletType);
			cq.setParameter("walletType", walletType);
		}

		if (status != null) {
			tq.setParameter("status", status);
			cq.setParameter("status", status);
		}

		if (minBalance != null) {
			tq.setParameter("minBalance", minBalance);
			cq.setParameter("minBalance", minBalance);
		}

		if (maxBalance != null) {
			tq.setParameter("maxBalance", maxBalance);
			cq.setParameter("maxBalance", maxBalance);
		}

		if (minAvailable != null) {
			tq.setParameter("minAvailable", minAvailable);
			cq.setParameter("minAvailable", minAvailable);
		}

		if (maxAvailable != null) {
			tq.setParameter("maxAvailable", maxAvailable);
			cq.setParameter("maxAvailable", maxAvailable);
		}

		if (minCreditBuffer != null) {
			tq.setParameter("minCreditBuffer", minCreditBuffer);
			cq.setParameter("minCreditBuffer", minCreditBuffer);
		}

		if (maxCreditBuffer != null) {
			tq.setParameter("maxCreditBuffer", maxCreditBuffer);
			cq.setParameter("maxCreditBuffer", maxCreditBuffer);
		}

		if (minAlertThreshold != null) {
			tq.setParameter("minAlertThreshold", minAlertThreshold);
			cq.setParameter("minAlertThreshold", minAlertThreshold);
		}

		if (maxAlertThreshold != null) {
			tq.setParameter("maxAlertThreshold", maxAlertThreshold);
			cq.setParameter("maxAlertThreshold", maxAlertThreshold);
		}
		
		if (offset !=null && offset > 0) {
	    		tq.setFirstResult(offset);
	    	}
		
	    	if (maxRows !=null && maxRows > 0) {
	    		tq.setMaxResults(maxRows);
	    	}
	    	
	    	List<Wallet> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		
		return new ResultList<Wallet>(result, totalCount.intValue());
	}

	@Override
	public ResultList<Wallet> searchWalletsByOrFilter(String organizationId, String globalFilter, String orderBy, String orderSorting, Integer offset, Integer maxRows) {
		
		StringBuffer sb = new StringBuffer(" from Wallet w where 1=1");
		StringBuffer countSb = new StringBuffer(" select count(w.walletId) from Wallet w where 1=1");
		
		if(!Strings.isNullOrEmpty(organizationId)){
			sb.append(" and w.organizationId = :organizationId");
			countSb.append(" and w.organizationId = :organizationId");
		}
		
		if(!Strings.isNullOrEmpty(globalFilter)){
			sb.append(" and (");
			countSb.append(" and (");
			
			sb.append(" w.organizationName like :organizationName");
			countSb.append(" w.organizationName like :organizationName");
	
//			sb.append(" or w.parentOrganizationName like :parentOrganizationName");
//			countSb.append(" or w.parentOrganizationName like :parentOrganizationName");
			
//			sb.append(" or w.organizationId like :parentOrganizationId");
//			countSb.append(" or w.organizationId like :parentOrganizationId");
	
//			sb.append(" or p.userId like :userId");
//			countSb.append(" or p.userId like :userId");
	
			sb.append(" or w.name like :walletName");
			countSb.append(" or w.name like :walletName");
			
			if ("MAIN_WALLET".contains(globalFilter.toUpperCase())) {
				sb.append(" or w.walletType = :walletType");
				countSb.append(" or w.walletType = :walletType");
			}
	
			if ("ACTIVE".contains(globalFilter.toUpperCase())) {
				sb.append(" or w.walletStatus = :active");
				countSb.append(" or w.walletStatus = :active");
			}
			
			if ("INACTIVE".contains(globalFilter.toUpperCase())) {
				sb.append(" or w.walletStatus = :inactive");
				countSb.append(" or w.walletStatus = :inactive");
			}
			
			if ("DELETED".contains(globalFilter.toUpperCase())) {
				sb.append(" or w.walletStatus = :deleted");
				countSb.append(" or w.walletStatus = :deleted");
			}
			
			if (isInteger(globalFilter)) {
				sb.append(" or w.balance like :balance");
				countSb.append(" or w.balance like :balance");
				
				sb.append(" or w.available like :available");
				countSb.append(" or w.available like :available");
				
				sb.append(" or w.alertThreshold like :alertThreshold");
				countSb.append(" or w.alertThreshold like :alertThreshold");
			}
			sb.append(" )");
			countSb.append(" )");
		}
		if (orderBy != null) {
			
//			if(orderBy.equals("balance") || orderBy.equals("available")){
//				sb.append(" order by c." + orderBy);
//			}else{
				sb.append(" order by w." + orderBy);
//			}

			if (orderSorting != null) {
				sb.append(" " + orderSorting);
			}
		} else {
			sb.append(" order by w.uid");
			if (orderSorting == null) {
				sb.append(" asc");
			}
		}
		
		String sql = sb.toString();
		String countSql = countSb.toString();
		
		TypedQuery<Wallet> tq = em.createQuery(sql,Wallet.class);
		TypedQuery<Long> cq = em.createQuery(countSql,Long.class);
		
		if(!Strings.isNullOrEmpty(organizationId)){
			tq.setParameter("organizationId", organizationId);
			cq.setParameter("organizationId", organizationId);
		}
		
		if(!Strings.isNullOrEmpty(globalFilter)){
			tq.setParameter("organizationName", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("organizationName", StringUtils.enclosePercentWildcard(globalFilter));
	
//			tq.setParameter("userId", StringUtils.enclosePercentWildcard(globalFilter));
//			cq.setParameter("userId", StringUtils.enclosePercentWildcard(globalFilter));
//	
//			tq.setParameter("parentOrganizationId", StringUtils.enclosePercentWildcard(globalFilter));
//			cq.setParameter("parentOrganizationId", StringUtils.enclosePercentWildcard(globalFilter));
	
//			tq.setParameter("parentOrganizationName", StringUtils.enclosePercentWildcard(globalFilter));
//			cq.setParameter("parentOrganizationName", StringUtils.enclosePercentWildcard(globalFilter));
	
			tq.setParameter("walletName", StringUtils.enclosePercentWildcard(globalFilter));
			cq.setParameter("walletName", StringUtils.enclosePercentWildcard(globalFilter));
			
			if ("MAIN_WALLET".contains(globalFilter.toUpperCase())) {
				tq.setParameter("walletType", WalletType.MAIN_WALLET);
				cq.setParameter("walletType", WalletType.MAIN_WALLET);
			}
			
			if ("ACTIVE".contains(globalFilter.toUpperCase())) {
				tq.setParameter("active", WalletStatus.ACTIVE);
				cq.setParameter("active", WalletStatus.ACTIVE);
			}
			
			if ("INACTIVE".contains(globalFilter.toUpperCase())) {
				tq.setParameter("inactive", WalletStatus.INACTIVE);
				cq.setParameter("inactive", WalletStatus.INACTIVE);
			}

			if (isInteger(globalFilter)) {
				tq.setParameter("balance", Integer.parseInt(globalFilter));
				cq.setParameter("balance", Integer.parseInt(globalFilter));
				
				tq.setParameter("available", Integer.parseInt(globalFilter));
				cq.setParameter("available", Integer.parseInt(globalFilter));
				
				tq.setParameter("alertThreshold", Integer.parseInt(globalFilter));
				cq.setParameter("alertThreshold", Integer.parseInt(globalFilter));
			}
		}
		
		if (offset !=null && offset > 0) {
	    		tq.setFirstResult(offset);
	    	}
			
	    	if (maxRows !=null && maxRows > 0) {
	    		tq.setMaxResults(maxRows);
	    	}
	    	
	    	List<Wallet> result = tq.getResultList();
		Long totalCount = cq.getSingleResult();
		
		return new ResultList<Wallet>(result, totalCount.intValue());
	}

	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}
