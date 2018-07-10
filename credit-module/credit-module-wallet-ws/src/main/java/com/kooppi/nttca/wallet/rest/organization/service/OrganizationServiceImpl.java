package com.kooppi.nttca.wallet.rest.organization.service;

//@ApplicationScoped
//public class OrganizationServiceImpl implements OrganizationService {
//
//	@Inject
//	private OrganizationRepository organizationRepository;
//	
//	@Override
//	public List<String> findAllCustomerCdsCompanyId() {
//		List<String> result = Lists.newArrayList();
//		List<Organization> orgList = organizationRepository.findAllOrganization();
//		for (Organization org : orgList) {
//			String cdsCompanyId = org.getCdsCompanyId();
//			if (!result.contains(cdsCompanyId)) {
//				OrganizationType orgType = org.getOrganizationType();
//				if (orgType.equals(OrganizationType.CUSTOMER))
//					result.add(cdsCompanyId);
//			}
//		}
//		return result;
//	}
//
//}
