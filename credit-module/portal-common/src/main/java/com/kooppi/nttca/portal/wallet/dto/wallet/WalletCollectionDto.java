package com.kooppi.nttca.portal.wallet.dto.wallet;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

public class WalletCollectionDto extends ResponseResult{

	@XmlElement(name = "wallets")
	private List<WalletDto> wallets = Lists.newArrayList();
	
	@XmlElement(name = "count")
	private Integer count;

	@XmlElement(name = "offset")
	private Integer offset;
	
	@XmlElement(name = "maxRows")
	private Integer maxRows;
	
	public WalletCollectionDto() {}
	
	public static WalletCollectionDto create(List<WalletDto> wallets, Integer count, Integer offset, Integer maxRows) {
		WalletCollectionDto dto = new WalletCollectionDto();
		dto.wallets.addAll(wallets);
		dto.count = count;
		dto.maxRows = maxRows;
		dto.offset = offset;
		return dto;
	}
	
	public List<WalletDto> getWallets() {
		return wallets;
	}
	
	public void setWallets(List<WalletDto> wallets) {
		this.wallets = wallets;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	@Override
	public String getResultName() {
		return "walletlist";
	}

}
