package com.kooppi.nttca.ce.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.ce.domain.PaymentStatus;
import com.kooppi.nttca.portal.ce.dto.payment.ChargingItemDto;
import com.kooppi.nttca.portal.ce.dto.payment.CreatePaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.PaymentDto;
import com.kooppi.nttca.portal.ce.dto.payment.ReservationAndTransactionDto;
import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;
import com.kooppi.nttca.portal.wallet.dto.wallet.TransactionReservationDto;
import com.kooppi.nttca.wallet.common.persistence.domain.Wallet;

@Entity
@Table(name = "payment")
public class Payment extends Modifiable {

	private static final long serialVersionUID = -5884961098664633497L;
	
	public static final String PAYMENT_ID_PREFIX = "PM-";

	@Column(name = "payment_id")
	private String paymentId;

	@Column(name = "wallet_id")
	private String walletId;
	
	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "total_amount")
	private Integer totalAmount;

	@Column(name = "description")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PaymentStatus status;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "expired_date")
	private LocalDateTime expiredDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
	private List<ChargingItem> chargingItems = Lists.newArrayList();
	
	@Transient
	private Integer compensationTotalAmount;
	
	public void updatePaymentId(){
		paymentId = Payment.PAYMENT_ID_PREFIX  + Strings.padStart(getUid() + "", 10, '0');
		getChargingItems().forEach(c->{
			c.updatePaymentId(paymentId);
		});
	}

	public String getPaymentId() {
		return paymentId;
	}

	public String getWalletId() {
		return walletId;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public List<ChargingItem> getChargingItems() {
		return chargingItems;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public PaymentStatus getStatus() {
		return status;
	}
	
	public boolean isReserved() {
		return status == PaymentStatus.RESERVED;
	}

	public boolean isConfirm() {
		return status == PaymentStatus.CONFIRMED;
	}

	public Integer getCompensationTotalAmount() {
		return compensationTotalAmount;
	}

	public void setCompensationTotalAmount(Integer compensationTotalAmount) {
		this.compensationTotalAmount = compensationTotalAmount;
	}

	public void confirmPayment(RequestContext rc, String description) {
		this.description = description;
		this.status = PaymentStatus.CONFIRMED;
		this.totalAmount =  getTotalAmount();
	}

	public void cancelReservedPayment(RequestContext rc, String description) {
		this.description = description;
		this.status = PaymentStatus.CANCELLED;
		this.totalAmount = 0;
	}

	public void refundPayment(RequestContext rc, String description) {
		this.description = description;
		this.status = PaymentStatus.REVERSED;
		this.totalAmount = getTotalAmount() * -1;
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiredDate);
	}

	private Integer getWalletTotalAmount() {
		return this.totalAmount - this.compensationTotalAmount;
	}

	public PaymentDto toPaymentDto() {
		PaymentDto dto = new PaymentDto();
		dto.setPaymentId(getPaymentId());
		dto.setWalletId(getWalletId());
		dto.setWalletTotalAmount(getWalletTotalAmount());
		dto.setCompensationTotalAmount(getCompensationTotalAmount());
		dto.setDescription(getDescription());
		dto.setExpiredDate(getExpiredDate());
		dto.setPaymentStatus(getStatus());
		dto.getChargingItems()
				.addAll(getChargingItems().stream().map(c -> c.toChargingItemDto()).collect(Collectors.toList()));
		return dto;
	}

	public PaymentDto toPaymentDtoWithUnitPrice() {
		PaymentDto dto = new PaymentDto();
		dto.setPaymentId(getPaymentId());
		dto.setWalletId(getWalletId());
		dto.setWalletTotalAmount(getTotalAmount());
		dto.setCompensationTotalAmount(getCompensationTotalAmount());
		dto.setDescription(getDescription());
		dto.setExpiredDate(getExpiredDate());
		dto.setTransactionDate(getCreatedDate());
		dto.setPaymentStatus(getStatus());
		dto.getChargingItems().addAll(getChargingItems().stream().map(c -> c.toChargingItemDtoWithUnitPrice())
				.collect(Collectors.toList()));
		return dto;
	}

	public PaymentDto toPaymentDtoWithoutChargingItemsAndExpiredDate() {
		PaymentDto dto = new PaymentDto();
		dto.setPaymentId(getPaymentId());
		dto.setWalletId(getWalletId());
		dto.setWalletTotalAmount(getTotalAmount());
		dto.setCompensationTotalAmount(getCompensationTotalAmount());
		dto.setDescription(getDescription());
		dto.setChargingItems(null);
		dto.setPaymentStatus(getStatus());
		return dto;
	}

	public static Payment create(Wallet wallet, CreatePaymentDto dto, LocalDateTime expiredDate, RequestContext rc) {
		Payment payment = new Payment();
		payment.paymentId = UUID.randomUUID().toString();
		payment.walletId = wallet.getWalletId();
		payment.organizationId = wallet.getOrganizationId();
		
		payment.description = dto.getDescription();
		payment.expiredDate = expiredDate;
		payment.status = PaymentStatus.RESERVED;

		Integer totalAmount = 0;
		for (ChargingItemDto itemDto : dto.getChargingItems()) {
			if (itemDto.getTotalAmount() != null)
				totalAmount += itemDto.getTotalAmount();
			payment.chargingItems.add(ChargingItem.create(itemDto));
		}
		payment.totalAmount = totalAmount;
		return payment;
	}

	public static Payment createRealTime(Wallet wallet,RequestContext rc, Integer totalAmount, String description) {
		Payment payment = new Payment();
		payment.paymentId = UUID.randomUUID().toString();
		payment.walletId = wallet.getWalletId();
		payment.organizationId = wallet.getOrganizationId();
		payment.description = description;

//		Integer totalAmount = 0;
//		for (ChargingItemDto itemDto : dto.getChargingItems()) {
//			if (itemDto.getTotalAmount() != null)
//				totalAmount += itemDto.getTotalAmount();
//			payment.chargingItems.add(ChargingItem.create(itemDto));
//		}
		payment.totalAmount = totalAmount;
		payment.status = PaymentStatus.CONFIRMED;
		return payment;
	}

	public TransactionReservationDto toTransactionReservation() {
		TransactionReservationDto dto = new TransactionReservationDto();
		dto.setPaymentId(paymentId);
		dto.setAmount(totalAmount);
		dto.setExpiredDate(expiredDate);
		dto.setDescription(description);
		for (ChargingItem chargingItem : chargingItems) {
			dto.getChargingItems().add(chargingItem.toChargeItemDto());
		}
		return dto;
	}

	public ReservationAndTransactionDto toReservationAndTransactionDto() {
		ReservationAndTransactionDto dto = new ReservationAndTransactionDto();
		dto.setUid(getUid());
		dto.setOrganizationId(getOrganizationId());
		dto.setPaymentId(getPaymentId());
		dto.setPaymentStatus(getStatus());
		dto.setTransactionDate(getCreatedDate());
		dto.setLastUpdateDate(getModifiedDate());
		return dto;
	}
	
	public static Payment generatePaymentFromObject(Object[] obj) {
		Payment payment = new Payment();
		
		payment.uid = Long.parseLong(String.valueOf(obj[0]));
		payment.paymentId = String.valueOf(obj[1]);
		payment.walletId = String.valueOf(obj[2]);
		payment.organizationId = String.valueOf(obj[3]);
		payment.totalAmount = Integer.parseInt(String.valueOf(obj[4]));
		payment.description = String.valueOf(obj[5]);
		payment.status = PaymentStatus.valueOf(String.valueOf(obj[6]));
		payment.compensationTotalAmount = Integer.parseInt(String.valueOf(obj[20]));
		
		return payment;
	}
}