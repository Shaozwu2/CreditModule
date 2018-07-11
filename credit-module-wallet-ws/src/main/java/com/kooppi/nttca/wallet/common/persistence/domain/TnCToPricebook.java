package com.kooppi.nttca.wallet.common.persistence.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCToPricebookDto;

import jersey.repackaged.com.google.common.collect.Sets;

@Entity
@Table(name = "tnc_to_price_book")
public class TnCToPricebook extends Modifiable {

	private static final long serialVersionUID = 4560943205199533651L;

	@Column(name = "tnc_id")
	private Integer tncId;
	
	@Column(name = "bu_name")
	private String buName;
	
	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "part_no")
	private String partNo;

	@Column(name = "is_default_bu")
	private Boolean isDefaultBu;
	
	@Column(name = "is_all_bu")
	private Boolean isAllBu;

	@Column(name = "is_all_product")
	private Boolean isAllProduct;
	
	@Column(name = "is_all_customer")
	private Boolean isAllCustomer;
	
	@Column(name = "priority")
	private Integer priority;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="part_no",referencedColumnName = "part_no",insertable=false,updatable = false)
	private PriceBook priceBook;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tnc_id",referencedColumnName = "uid",insertable=false,updatable = false)
	private TnC tnc;
	
	public static TnCToPricebook create(Integer tncId, String buName, String partNo, String organizationId, boolean isDefaultBu, boolean isAllCustomer, boolean isAllBu, boolean isAllProduct) {
		TnCToPricebook tnCToPricebook = new TnCToPricebook();
		tnCToPricebook.tncId = tncId;
		tnCToPricebook.buName = buName;
		tnCToPricebook.partNo = partNo;
		tnCToPricebook.organizationId = organizationId;
		tnCToPricebook.isDefaultBu = isDefaultBu;
		tnCToPricebook.isAllCustomer = isAllCustomer;
		tnCToPricebook.isAllBu = isAllBu;
		tnCToPricebook.isAllProduct = isAllProduct;
		tnCToPricebook.priority = isDefaultBu == true ? 0 : null;
		return tnCToPricebook;
	}
	
	public void update(Integer tncId, String buName, String partNo, String organizationId, boolean isDefaultBu, boolean isAllCustomer, boolean isAllBu, boolean isAllProduct) {
		this.tncId = tncId;
		this.buName = buName;
		this.partNo = partNo;
		this.organizationId = organizationId;
		this.isDefaultBu = isDefaultBu;
		this.isAllCustomer = isAllCustomer;
		this.isAllBu = isAllBu;
		this.isAllProduct = isAllProduct;
	}
	
	public TnCToPricebookDto toTnCToPricebookDto() {
		TnCToPricebookDto dto = new TnCToPricebookDto();
		dto.setUid(getUid());
		dto.setBuName(getBuName());
		dto.setOrganizationId(getOrganizationId());
		dto.setIsAllCustomer(getIsAllCustomer());
		dto.setIsDefaultBu(getIsDefaultBu());
		dto.setIsAllBu(getIsAllBU());
		dto.setIsAllProduct(getIsAllProduct());
		dto.setPartNo(getPartNo());
		dto.setProductName(getPriceBook() == null ? "All" : getPriceBook().getProductName());
		dto.setTncId(getTncId());
		dto.setTnc(getTnc().toTnCDto());
		return dto;
	}

	public Integer getTncId() {
		return tncId;
	}

	public String getBuName() {
		return buName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public String getPartNo() {
		return partNo;
	}

	public Boolean getIsDefaultBu() {
		return isDefaultBu;
	}

	public Boolean getIsAllCustomer() {
		return isAllCustomer;
	}

	public Boolean getIsAllBU() {
		return isAllBu;
	}

	public Boolean getIsAllProduct() {
		return isAllProduct;
	}

	public Integer getPriority() {
		return priority;
	}

	public PriceBook getPriceBook() {
		return priceBook;
	}

	public TnC getTnc() {
		return tnc;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TnCToPricebook other = (TnCToPricebook) obj;
		if (buName == null) {
			if (other.buName != null)
				return false;
		} else if (!buName.equals(other.buName))
			return false;
		if (organizationId == null) {
			if (other.organizationId != null)
				return false;
		} else if (!organizationId.equals(other.organizationId))
			return false;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		return true;
	}
	public static void main(String[] args) {
		Set<TnCToPricebook> setList = Sets.newConcurrentHashSet();
		TnCToPricebook tnc = TnCToPricebook.create(null, "Hosting", "p1001", "ACompany", false, false, false, false);
		TnCToPricebook tnc2 = TnCToPricebook.create(null, "Hosting", "p1001", "BCompany", false, false, false, false);
		TnCToPricebook tnc3 = TnCToPricebook.create(null, "Hosting", "p1001", "BCompany", false, false, false, false);
		setList.add(tnc);
		setList.add(tnc2);
		setList.add(tnc3);
		System.out.println(setList.size());
	}
	
}
