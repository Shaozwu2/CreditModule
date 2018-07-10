package com.kooppi.nttca.portal.wallet.dto.adjustment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateAdapter;
import com.kooppi.nttca.portal.common.utils.PortalXmlDateTimeAdapter;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentStatus;
import com.kooppi.nttca.portal.wallet.domain.AdjustmentType;
import com.kooppi.nttca.portal.wallet.dto.priceBook.PriceBookDto;

@XmlAccessorType(XmlAccessType.FIELD)
public class AdjustmentDto extends ResponseResult{

	@XmlElement(name = "walletId")
	private String walletId;
	
	@XmlElement(name = "transactionId")
	private String id;
	
	@XmlElement(name = "refNumber")
	private String refNumber;
	
	@XmlElement(name = "companyCode")
	private String companyCode;
	
	@XmlElement(name = "adjustmentType")
	private AdjustmentType adjustmentType;

	@XmlElement(name = "currency")
	private String currency;
	
	@XmlElement(name = "exchangeRate")
	private BigDecimal exchangeRate;
	
	@XmlElement(name = "nttDollarSO")
	private Integer nttDollarSO;
	
	@XmlElement(name = "currencyAmountSO")
	private BigDecimal currencyAmountSO;
	
	@XmlElement(name = "nttDollarMonthly")
	private Integer nttDollarMonthly;
	
	@XmlElement(name = "currencyAmountMonthly")
	private BigDecimal currencyAmountMonthly;
	
	@XmlElement(name = "nttDollarBalance")
	private Integer nttDollarBalance;
	
	@XmlElement(name = "currencyAmountBalance")
	private BigDecimal currencyAmountBalance;
	
	@XmlElement(name = "description")
	private String description;

	@XmlElement(name = "status")
	private AdjustmentStatus status;
	
	@XmlElement(name = "bus")
	private List<String> buLists;

	@XmlElement(name = "priceBooks")
	private List<PriceBookDto> priceBookList;
	
	@XmlElement(name = "products")
	private List<String> productList;
	
	@XmlElement(name = "isAllBu")
	private Boolean isAllBu;

	@XmlElement(name = "isAllProduct")
	private Boolean isAllProduct;
	
	@XmlElement(name = "contractEffectiveDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate contractEffectiveDate;
	
	@XmlElement(name = "contractTerminationDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate contractTerminationDate;
	
	@XmlElement(name = "creditExpiryDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateAdapter.class)
	private LocalDate creditExpiryDate;
	
	@XmlElement(name = "nextRechargeDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime nextRechargeDate;
	
	@XmlElement(name = "terminatedReason")
	private String terminatedReason;
	
	@XmlElement(name = "transactionDate")
	@XmlJavaTypeAdapter(value = PortalXmlDateTimeAdapter.class)
	private LocalDateTime transactionDate;
	
	@XmlElement(name = "lastModifidedBy")
	private String userId;
	
	@XmlElement(name = "organization_id")
	private String organizationId;
	
	@XmlElement(name = "isForfeited")
	private Boolean isForfeited;
	
	@XmlElement(name = "nttDollarForfeit")
	private Integer nttDollarForfeit;
	
	@XmlElement(name = "currencyAmountForfeit")
	private BigDecimal currencyAmountForfeit;
	
	public String getWalletId() {
		return walletId;
	}

	public String getId() {
		return id;
	}

	public String getRefNumber() {
		return refNumber;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	public AdjustmentType getAdjustmentType() {
		return adjustmentType;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public Integer getNttDollarSO() {
		return nttDollarSO;
	}

	public BigDecimal getCurrencyAmountSO() {
		return currencyAmountSO;
	}

	public Integer getNttDollarMonthly() {
		return nttDollarMonthly;
	}

	public BigDecimal getCurrencyAmountMonthly() {
		return currencyAmountMonthly;
	}

	public Integer getNttDollarBalance() {
		return nttDollarBalance;
	}

	public BigDecimal getCurrencyAmountBalance() {
		return currencyAmountBalance;
	}

	public AdjustmentStatus getStatus() {
		return status;
	}

	public LocalDate getCreditExpiryDate() {
		return creditExpiryDate;
	}

	public LocalDateTime getNextRechargeDate() {
		return nextRechargeDate;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getContractEffectiveDate() {
		return contractEffectiveDate;
	}

	public LocalDate getContractTerminationDate() {
		return contractTerminationDate;
	}

	public String getTerminatedReason() {
		return terminatedReason;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public void setAdjustmentType(AdjustmentType adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStatus(AdjustmentStatus status) {
		this.status = status;
	}

	public void setContractEffectiveDate(LocalDate contractEffectiveDate) {
		this.contractEffectiveDate = contractEffectiveDate;
	}

	public void setContractTerminationDate(LocalDate contractTerminationDate) {
		this.contractTerminationDate = contractTerminationDate;
	}

	public void setTerminatedReason(String terminatedReason) {
		this.terminatedReason = terminatedReason;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public void setNttDollarSO(Integer nttDollarSO) {
		this.nttDollarSO = nttDollarSO;
	}

	public void setCurrencyAmountSO(BigDecimal currencyAmountSO) {
		this.currencyAmountSO = currencyAmountSO;
	}

	public void setNttDollarMonthly(Integer nttDollarMonthly) {
		this.nttDollarMonthly = nttDollarMonthly;
	}

	public void setCurrencyAmountMonthly(BigDecimal currencyAmountMonthly) {
		this.currencyAmountMonthly = currencyAmountMonthly;
	}

	public void setNttDollarBalance(Integer nttDollarBalance) {
		this.nttDollarBalance = nttDollarBalance;
	}

	public void setCurrencyAmountBalance(BigDecimal currencyAmountBalance) {
		this.currencyAmountBalance = currencyAmountBalance;
	}

	public void setCreditExpiryDate(LocalDate creditExpiryDate) {
		this.creditExpiryDate = creditExpiryDate;
	}

	public void setNextRechargeDate(LocalDateTime nextRechargeDate) {
		this.nextRechargeDate = nextRechargeDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getBuLists() {
		return buLists;
	}

	public void setBuLists(List<String> buLists) {
		this.buLists = buLists;
	}

	public Boolean getIsAllBu() {
		return isAllBu;
	}

	public void setIsAllBu(Boolean isAllBu) {
		this.isAllBu = isAllBu;
	}

	public Boolean getIsAllProduct() {
		return isAllProduct;
	}

	public void setIsAllProduct(Boolean isAllProduct) {
		this.isAllProduct = isAllProduct;
	}

	public List<PriceBookDto> getPriceBookList() {
		return priceBookList;
	}

	public void setPriceBookList(List<PriceBookDto> priceBookList) {
		this.priceBookList = priceBookList;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsForfeited() {
		return isForfeited;
	}

	public void setIsForfeited(Boolean isForfeited) {
		this.isForfeited = isForfeited;
	}

	public Integer getNttDollarForfeit() {
		return nttDollarForfeit;
	}

	public void setNttDollarForfeit(Integer nttDollarForfeit) {
		this.nttDollarForfeit = nttDollarForfeit;
	}

	public BigDecimal getCurrencyAmountForfeit() {
		return currencyAmountForfeit;
	}

	public void setCurrencyAmountForfeit(BigDecimal currencyAmountForfeit) {
		this.currencyAmountForfeit = currencyAmountForfeit;
	}

	

	public List<String> getProductList() {
		return productList;
	}

	public void setProductList(List<String> productList) {
		this.productList = productList;
	}

	@Override
	public String getResultName() {
		return "adjustment";
	}
}
