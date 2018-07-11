package com.kooppi.nttca.wallet.common.persistence.domain;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.Modifiable;

@Entity
@Table(name = "report")
public class Report extends Modifiable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "report_date")
	private Date reportDate;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "report_file")
	private byte[] reportFile;
	
	/* Getter && Setter */

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public byte[] getReportFile() {
		return reportFile;
	}

	public void setReportFile(byte[] reportFile) {
		this.reportFile = reportFile;
	}

}
