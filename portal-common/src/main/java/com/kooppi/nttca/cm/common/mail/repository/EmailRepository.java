package com.kooppi.nttca.cm.common.mail.repository;

import java.util.List;

public interface EmailRepository {

	public void sendEmail(String sender, List<String> recipients, List<String> bccRecipients,String subject, String message);
		
}