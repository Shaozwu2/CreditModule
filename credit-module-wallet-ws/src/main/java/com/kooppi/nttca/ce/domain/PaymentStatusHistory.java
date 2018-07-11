package com.kooppi.nttca.ce.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kooppi.nttca.portal.ce.domain.PaymentStatus;
import com.kooppi.nttca.portal.ce.dto.payment.PaymentStatusHistoryDto;
import com.kooppi.nttca.portal.common.domain.BasicEntity;
import com.kooppi.nttca.portal.common.filter.request.RequestContext;
import com.kooppi.nttca.portal.exception.domain.PortalErrorCode;
import com.kooppi.nttca.portal.exception.domain.PortalExceptionUtils;

@Entity
@Table(name = "cm_payment_status_history")
public class PaymentStatusHistory extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2551329775851757580L;

	@Column(name = "payment_id", updatable = false, insertable = false)
	private String paymentId;

	@Column(name = "previous_status_uid")
	private Long previousStatusUid;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
	private Payment payment;

	@Column(name = "request_id")
	private String requestId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PaymentStatus status;

	@Column(name = "amount")
	private Integer amount;

	@Column(name = "remark")
	private String remark;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "user_id")
	private String userId;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", referencedColumnName = "payment_status_uid", updatable = false, insertable = false)
	private WalletTransactionReservationHistory transactionReservationHistory;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", referencedColumnName = "payment_status_uid", updatable = false, insertable = false)
	private WalletTransactionHistory transactionhistory;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "previous_status_uid", referencedColumnName = "uid", updatable = false, insertable = false)
	private PaymentStatusHistory previousStatusHistory;

	public static PaymentStatusHistory createReserveStatus(Payment payment, RequestContext rc) {

		PaymentStatusHistory status = new PaymentStatusHistory();
		status.payment = payment;
		status.requestId = rc.getRequestId();
		status.status = PaymentStatus.RESERVED;
		status.amount = 0;
//		status.userId = rc.getRequestUserId();
		return status;
	}

	public static PaymentStatusHistory createComfirmStatus(Payment payment, RequestContext rc) {
		PaymentStatusHistory status = new PaymentStatusHistory();
		status.payment = payment;
		status.requestId = rc.getRequestId();
		status.status = PaymentStatus.CONFIRMED;
		status.amount = payment.getTotalAmount();
//		status.userId = rc.getRequestUserId();
//		status.previousStatusHistory = !payment.getCurrentStatus().isPresent() ? null
//				: payment.getCurrentStatus().get();
//		status.previousStatusUid = !payment.getCurrentStatus().isPresent() ? null
//				: payment.getCurrentStatus().get().getUid();
		return status;
	}

	public static PaymentStatusHistory createCancelStatus(Payment payment, RequestContext rc) {
		PaymentStatusHistory status = new PaymentStatusHistory();
		status.payment = payment;
		status.requestId = rc.getRequestId();
		status.status = PaymentStatus.CANCELLED;
		status.amount = 0;
//		status.userId = rc.getRequestUserId();
//		status.previousStatusHistory = payment.getCurrentStatus().get();
//		status.previousStatusUid = payment.getCurrentStatus().get().getUid();
		return status;
	}

	public static PaymentStatusHistory createRefundStatus(Payment payment, RequestContext rc) {
		PaymentStatusHistory status = new PaymentStatusHistory();
		status.payment = payment;
		status.requestId = rc.getRequestId();
		status.status = PaymentStatus.REVERSED;
		status.amount = payment.getTotalAmount() * -1;
//		status.userId = rc.getRequestUserId();
//		status.previousStatusHistory = payment.getCurrentStatus().get();
//		status.previousStatusUid = payment.getCurrentStatus().get().getUid();
		return status;
	}
	
	public void updatePaymentId(String paymentId){
		this.paymentId = paymentId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public String getRequestId() {
		return requestId;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public Integer getAmount() {
		return amount;
	}

	public String getRemark() {
		return remark;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public PaymentStatusHistory getPreviousStatusHistory() {
		return previousStatusHistory;
	}

	public PaymentStatusHistoryDto toPaymentStausHistoryDto() {
		return PaymentStatusHistoryDto.create(getPaymentId(), getRequestId(), getStatus(), getAmount(), getRemark());
	}

	public void updateReservationResult(WalletTransactionReservationHistory tr) {
		PortalExceptionUtils.throwIfFalse(tr.isReserved(), PortalErrorCode.UNKNOW_ERROR_ON_WALLET_API);
		this.transactionReservationHistory = tr;
		this.transactionId = tr.getTransactionId();
		tr.updateParentStatusHistory(this);
	}

	public void updateTransactionResult(WalletTransactionHistory tran) {
		PortalExceptionUtils.throwIfFalse(tran.isCommitAndSuccess(), PortalErrorCode.UNKNOW_ERROR_ON_WALLET_API);
		this.transactionhistory = tran;
		this.transactionId = tran.getTransactionId();
		tran.updateParentStatusHistory(this);
	}

	public void updateRollbackTransactionResult(WalletTransactionHistory tran) {
		PortalExceptionUtils.throwIfFalse(tran.isRollbackAndSuccess(), PortalErrorCode.UNKNOW_ERROR_ON_WALLET_API);
		this.transactionhistory = tran;
		this.transactionId = tran.getTransactionId();
		tran.updateParentStatusHistory(this);
	}

	public boolean isReserved() {
		return this.status == PaymentStatus.RESERVED;
	}

	public boolean isConfirm() {
		return this.status == PaymentStatus.CONFIRMED;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}