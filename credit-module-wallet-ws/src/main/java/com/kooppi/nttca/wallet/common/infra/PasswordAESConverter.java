package com.kooppi.nttca.wallet.common.infra;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PasswordAESConverter implements AttributeConverter<String, String> {
	
	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String KEY_SPEC = "AES";
	private static final byte[] SECRET = "054016ef-74ba-4d".getBytes();

	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET, KEY_SPEC));
			return Base64.getEncoder().encodeToString(c.doFinal(attribute.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET, KEY_SPEC));
			return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		PasswordAESConverter c = new PasswordAESConverter();
		String encoded = c.convertToDatabaseColumn("123456");
		System.out.println(encoded);
		System.out.println(c.convertToEntityAttribute(encoded));
		String encoded2 = c.convertToDatabaseColumn("87654321");
		System.out.println(encoded2);
		System.out.println(c.convertToEntityAttribute(encoded2));
		
		String encoded3 = c.convertToDatabaseColumn("12345678");
		System.out.println(encoded3);
		System.out.println(c.convertToEntityAttribute(encoded3));
		
		String encoded4 = c.convertToDatabaseColumn("12345ams");
		System.out.println(encoded4);
		System.out.println(c.convertToEntityAttribute(encoded4));
		
		String encoded5 = c.convertToDatabaseColumn("12345vdc");
		System.out.println(encoded5);
		System.out.println(c.convertToEntityAttribute(encoded5));
	}

}
