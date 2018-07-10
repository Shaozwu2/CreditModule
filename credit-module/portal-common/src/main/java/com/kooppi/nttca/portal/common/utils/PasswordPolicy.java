//package com.kooppi.nttca.portal.common.utils;
//
//import java.util.List;
//
//import com.google.common.collect.Lists;
//
//import edu.vt.middleware.password.CharacterCharacteristicsRule;
//import edu.vt.middleware.password.DigitCharacterRule;
//import edu.vt.middleware.password.LengthRule;
//import edu.vt.middleware.password.LowercaseCharacterRule;
//import edu.vt.middleware.password.Password;
//import edu.vt.middleware.password.PasswordData;
//import edu.vt.middleware.password.PasswordValidator;
//import edu.vt.middleware.password.Rule;
//import edu.vt.middleware.password.RuleResult;
//import edu.vt.middleware.password.UppercaseCharacterRule;
//import edu.vt.middleware.password.WhitespaceRule;
//
//public class PasswordPolicy {
//	
//	private List<Rule> passwordRules = Lists.newArrayList(
//			
//			);
//	private static PasswordValidator validator;
//	
//	private static PasswordValidator getValidator(){
//		if (validator == null) {
//			LengthRule lengthRule = new LengthRule(8, 16);
//			WhitespaceRule spaceRule = new WhitespaceRule();
//			CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
//			charRule.getRules().add(new UppercaseCharacterRule(1));
//			charRule.getRules().add(new LowercaseCharacterRule(1));
//			charRule.getRules().add(new DigitCharacterRule(1));
//
//			charRule.setNumberOfCharacteristics(3);
//			List<Rule> passwordRules = Lists.newArrayList(
//					lengthRule,spaceRule,charRule
//					);
//			validator = new PasswordValidator(passwordRules);
//		}
//		return validator;
//	}
//	
//	public static boolean validatePassword(String password){
//		PasswordData passwordData = new PasswordData(new Password(password));
//		RuleResult result = getValidator().validate(passwordData);
//		return result.isValid();
//	}
//	
//	public static void main(String args[]){
//		
//		
//		boolean result = PasswordPolicy.validatePassword("aaaaaaaaaaaAa");
//
//		if (result) {
//
//		  System.out.println("Valid password");
//
//
//		}
//	}
//	 
//}
