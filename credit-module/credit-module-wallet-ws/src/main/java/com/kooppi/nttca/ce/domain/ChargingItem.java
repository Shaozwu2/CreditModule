package com.kooppi.nttca.ce.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;
import com.kooppi.nttca.portal.wallet.dto.wallet.ChargeItemDto;
import com.kooppi.nttca.wallet.common.persistence.domain.MapChargeItemToContract;

@Entity
@Table(name = "charge_item")
public class ChargingItem extends BasicEntity {

	private static final long serialVersionUID = 7377226792138672406L;
	
	@Column(name = "charge_item_id")
	private String chargeItemId;
	
	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "total_amount")
	private Integer totalAmount;
	
	@Column(name = "part_no")
	private String partNo;
	
	@Column(name = "product_name")
	private String productName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "charge_type")
	private ChargeType chargeType;
	
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "charge_item_id",referencedColumnName = "charge_item_id")
	private List<MapChargeItemToContract> mapChargeItemToContract = Lists.newArrayList();
	
	public void updatePaymentId(String paymentId){
		this.paymentId = paymentId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}
	
	public String getChargeItemId() {
		return chargeItemId;
	}

	public void setChargeItemId(String chargeItemId) {
		this.chargeItemId = chargeItemId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public List<MapChargeItemToContract> getMapChargeItemToContract() {
		return mapChargeItemToContract;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public ChargingItemDto toChargingItemDtoWithUnitPrice() {
		ChargingItemDto dto = new ChargingItemDto();
		dto.setQuantity(getQuantity());
		dto.setTotalAmount(getTotalAmount());
		if (getTotalAmount() != null && getQuantity() != null) {
			dto.setUnitPrice(new BigDecimal(getTotalAmount()).divide(new BigDecimal(getQuantity()), 5, RoundingMode.HALF_UP));
		}
		dto.setPartNo(getPartNo());
		dto.setProductName(getProductName());
		dto.setChargeType(getChargeType());
		return dto;
	}
	
	public ChargingItemDto toChargingItemDto() {
		ChargingItemDto dto = new ChargingItemDto();
		dto.setQuantity(getQuantity());
		dto.setTotalAmount(getTotalAmount());
		dto.setPartNo(getPartNo());
		dto.setProductName(getProductName());
		dto.setChargeType(getChargeType());
		return dto;
	}
	
	public static ChargingItem create(ChargingItemDto dto) {
		PortalExceptionUtils.throwIfFalse(dto.isItemReady(), PortalErrorCode.PAYMENT_NOT_READY);
		ChargingItem item = new ChargingItem();
		item.quantity = dto.getQuantity();
		item.totalAmount = dto.getTotalAmount();
		item.chargeType = dto.getChargeType();
		item.partNo = dto.getPartNo();
		item.productName = dto.getProductName();
		item.chargeItemId = UUID.randomUUID().toString();
		return item;
	}

	public static ChargingItem createForfeitItem(Integer amount, String transactionId) {
		ChargingItem item = new ChargingItem();
		item.totalAmount = amount;
		item.transactionId = transactionId;
		item.productName = "forfeit credit";
		item.chargeItemId = UUID.randomUUID().toString();
		return item;
	}
	
	public ChargeItemDto toChargeItemDto() {
		ChargeItemDto dto = new ChargeItemDto();
		dto.setQuantity(quantity);
		dto.setAmount(totalAmount);
		return dto;
	}
}
