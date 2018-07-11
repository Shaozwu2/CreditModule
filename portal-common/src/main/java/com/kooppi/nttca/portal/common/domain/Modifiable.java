package com.kooppi.nttca.portal.common.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.google.common.base.Strings;
import com.kooppi.nttca.portal.common.domain.listener.ModifiableEntityListener;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;

@EntityListeners({ModifiableEntityListener.class})
@MappedSuperclass
public abstract class Modifiable extends BasicEntity{

	private static final long serialVersionUID = 7055071078408033652L;
	
	@Column(name = "modified_by")
	private String modifiedBy;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;
	
	@Version
	@Column(name = "version")
	private Long version;

	public String getModifiedBy() {
		return modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void prepareCreateAudit(String userId) {
		this.createdBy = DEFAULT_USER;
		this.modifiedBy = DEFAULT_USER;
		if (!Strings.isNullOrEmpty(userId)) {
			this.createdBy = userId;
			this.modifiedBy = userId;
		}
		this.createdDate = LocalDateTime.now();
		this.modifiedDate = LocalDateTime.now();
	}

	public void prepareUpdateAudit(String userId) {
		this.modifiedBy = DEFAULT_USER;
		if (!Strings.isNullOrEmpty(userId)) {
			this.modifiedBy = userId;
		}
		this.modifiedDate = LocalDateTime.now();
	}
}
