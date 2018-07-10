package com.kooppi.nttca.wallet.common.persistence.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.ce.domain.ChargeType;
import com.kooppi.nttca.portal.ce.dto.payment.ChargingItemDto;
import com.kooppi.nttca.portal.common.domain.BasicEntity;
import com.kooppi.nttca.portal.wallet.dto.wallet.ChargeItemDto;

@Entity
@Table(name = "charge_item")
public class ChargeItem extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -261322277682752849L;
	
	@Column(name = "charge_item_id")
	private String chargeItemId;
	
	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "total_amount")
	private Integer amount;
	
	@Column(name = "part_no")
	private String partNo;

	@Column(name = "product_name")
	private String productName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "charge_type")
	private ChargeType chargeType;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "charge_item_id",referencedColumnName = "charge_item_id", updatable = true, insertable = false)
	private List<MapChargeItemToContract> mapChargeItemToContract = Lists.newArrayList();

	public static ChargeItem create(String paymentId,ChargeItemDto dto){
		ChargeItem item = new ChargeItem();
		item.paymentId = paymentId;
		item.chargeItemId = UUID.randomUUID().toString();
		item.quantity = dto.getQuantity();
		item.amount = dto.getAmount();
		return item;
	}
	
	public static ChargeItem create(String paymentId, ChargingItemDto dto){
		ChargeItem item = new ChargeItem();
		item.paymentId = paymentId;
		item.chargeItemId = UUID.randomUUID().toString();
		item.quantity = dto.getQuantity();
		item.amount = dto.getTotalAmount();
		return item;
	}

	public String getPaymentId() {
		return paymentId;
	}
	
	public String getProductName() {
		return productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Integer getAmount() {
		return amount;
	}
	
	public String getPartNo() {
		return partNo;
	}

	public void updatePaymentId(String paymentId){
		this.paymentId = paymentId;
	}
	
	public String getChargeItemId() {
		return chargeItemId;
	}

	public List<MapChargeItemToContract> getMapChargeItemToContract() {
		return mapChargeItemToContract;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public ChargeItemDto toChargeItemDto(){
		ChargeItemDto dto = new ChargeItemDto();
		dto.setQuantity(this.getQuantity());
		dto.setAmount(this.getAmount());
		return dto;
	}
	
}
