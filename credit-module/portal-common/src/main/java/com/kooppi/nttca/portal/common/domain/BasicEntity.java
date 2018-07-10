package com.kooppi.nttca.portal.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.google.common.base.Strings;
import com.kooppi.nttca.portal.common.domain.listener.BasicEntityListener;
import com.kooppi.nttca.portal.common.persistence.convertor.LocalDateTimePersistenceConverter;

@MappedSuperclass
@EntityListeners({BasicEntityListener.class})
public abstract class BasicEntity implements Serializable {

	private static final long serialVersionUID = 7055071078408033652L;
	@Transient
	protected String currentUpdateUserName;
	protected static final String DEFAULT_USER = "system";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uid")
	protected Long uid;
	
	@Column(name = "created_by")
	protected String createdBy;

	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name = "created_date")
	protected LocalDateTime createdDate;
	
	public Long getUid() {
		return uid;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setModifiedUserId(String userName){
		this.currentUpdateUserName = userName;
	}
	
	public void prepareCreateAudit(String userId){
		this.createdBy = DEFAULT_USER;
		if (!Strings.isNullOrEmpty(userId)) {
			this.createdBy = userId;
		}
		this.createdDate = LocalDateTime.now();
	}
	
	
}
