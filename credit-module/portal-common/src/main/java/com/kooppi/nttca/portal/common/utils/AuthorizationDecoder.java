package com.kooppi.nttca.portal.common.utils;

import java.util.Base64;
import java.util.StringTokenizer;


public class AuthorizationDecoder {
	
	private static final String AUTHENTICATION_SCHEME = "Basic";
	
	public static class UserPass {
		private String username;
		private String password;
		public UserPass(String username, String password) {
			this.username = username;
			this.password = password;
		}
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
	}
	
	public static UserPass fromAuthorizationValue(String authorization) {
		final String encodedUserPassword = authorization.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		
		// Decode username and password
		String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		return new UserPass(username, password);
	}
	
	public static void main(String[] args) {
		UserPass fromAuthorizationValue = fromAuthorizationValue("Basic TkVXX1BPUlRBTDoxMjM0NTY=");
		System.out.println(fromAuthorizationValue.getUsername());
		System.out.println(fromAuthorizationValue.getPassword());
		
		byte[] encode = Base64.getEncoder().encode("NEW_PORTAL:123456".getBytes());
		String usernameAndPassword = new String(encode);
		System.out.println(usernameAndPassword);
		// 123456 U3dhZ2dlcjoxMjM0NTY=
		// 12345678 U3dhZ2dlcjoxMjM0NTY3OA==
	}

}
