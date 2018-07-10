package com.kooppi.nttca.wallet.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kooppi.nttca.portal.common.domain.Modifiable;
import com.kooppi.nttca.portal.wallet.dto.termsAndConditions.TnCDto;

@Entity
@Table(name = "tnc_template")
public class TnC extends Modifiable {

	private static final long serialVersionUID = -6756021879666855011L;

	@Column(name = "template_name")
	private String templateName;
	
	@Column(name = "bu_name")
	private String buName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_default")
	private Boolean isDefault;
	
	@Column(name = "is_visible")
	private Boolean isVisible;
	
	@Column(name = "uploaded_name")
	private String uploadedName;
	
	@Column(name = "file_name")
	private String fileName;
	
	public static TnC create(String templateName, String buName, String description, Boolean isDefault,
			Boolean isVisible, String uploadedName, String fileName) {
		TnC tnc = new TnC();
		tnc.templateName = templateName;
		tnc.buName = buName;
		tnc.description = description;
		tnc.isDefault = isDefault;
		tnc.isVisible = isVisible;
		tnc.uploadedName = uploadedName;
		tnc.fileName = fileName;
		return tnc;
	}

	public void update(String templateName, String description, String buName, Boolean isDefault, Boolean isVisible) {
		this.templateName = templateName;
		this.buName = buName;
		this.description = description;
		this.isDefault = isDefault;
		this.isVisible = isVisible;
	}

	public TnCDto toTnCDto() {
		TnCDto dto = new TnCDto();
		dto.setUid(getUid());
		dto.setTemplateName(getTemplateName());
		dto.setFileName(getUploadedName());
		dto.setBuName(getBuName());
		dto.setDescription(getDescription());
		dto.setIsDefault(getIsDefault());
		dto.setIsVisible(getIsVisible());
		dto.setCreatedOn(getCreatedDate().toString());
		return dto;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getUploadedName() {
		return uploadedName;
	}

	public void setUploadedName(String uploadedName) {
		this.uploadedName = uploadedName;
	}
}
