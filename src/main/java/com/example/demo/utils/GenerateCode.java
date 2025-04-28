package com.example.demo.utils;

public class GenerateCode {
	public static String generateVerifyCode() {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuilder code = new StringBuilder();
	    for (int i = 0; i < 5; i++) {
	        int index = (int) (Math.random() * chars.length());
	        code.append(chars.charAt(index));
	    }
	    return code.toString();
	}
}
