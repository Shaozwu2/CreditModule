package com.kooppi.nttca.wallet.common.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kooppi.nttca.wallet.common.infra.PasswordAESConverter;

@Entity
@Table(name = "api_user")
public class ApiUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "username")
	private String username;
	
	@Convert(converter = PasswordAESConverter.class)
	@Column(name = "password")
	private String password;
	
	@Column(name = "incoming_channel")
	private String incomingChannel;
	
	ApiUser() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIncomingChannel() {
		return incomingChannel;
	}

	public void setIncomingChannel(String incomingChannel) {
		this.incomingChannel = incomingChannel;
	}
	
}
