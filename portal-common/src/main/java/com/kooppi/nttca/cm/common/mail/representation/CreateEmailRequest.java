package com.kooppi.nttca.cm.common.mail.representation;

public class CreateEmailRequest {
	private WS_Email email;
	private int queuePriority;

	public WS_Email getEmail()
	{
		return email;
	}

	public void setEmail(WS_Email email)
	{
		this.email = email;
	}

	public int getQueuePriority()
	{
		return queuePriority;
	}

	public void setQueuePriority(int queuePriority)
	{
		this.queuePriority = queuePriority;
	}
}
