package com.bfulton.PasswordCracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	public static String sha1(String input) {
        MessageDigest mDigest;
        StringBuilder sb = new StringBuilder();
        
		try {
			mDigest = MessageDigest.getInstance("SHA1"); //provides the hash algorithm
			byte[] result = mDigest.digest(input.getBytes()); 

	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }

		} catch (NoSuchAlgorithmException e) {
			System.out.println("No SHA1 Algorithm exists!");
		} 
		return sb.toString();
    }
	
	public static String md5(String input) {
		StringBuilder sb = new StringBuilder();
        MessageDigest mDigest;
        
        try {
	        mDigest = MessageDigest.getInstance("MD5"); //provides the hash algorithm
	        byte[] result = mDigest.digest(input.getBytes()); 
	        
	        
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
        } catch(NoSuchAlgorithmException e) {
        	System.out.println("No md5 Algorithm exists!");
        }
         
        return sb.toString();
    }
	
    public static String sha256(String input) {
        MessageDigest mDigest;
        StringBuilder sb = new StringBuilder();
        
        try {
	        mDigest = MessageDigest.getInstance("SHA-256"); //provides the hash algorithm
	        byte[] result = mDigest.digest(input.getBytes()); 
	        
	        
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
        } catch (NoSuchAlgorithmException e) {
        	System.out.println("No SHA256 Algorithm exists!");
        }
         
        return sb.toString();
    }
}
