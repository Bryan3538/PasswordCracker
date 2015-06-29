package com.bfulton.PasswordCracker;

public enum CrackType {
	DICTIONARY_ATTACK("Dictionary attack"),
	BRUTE_FORCE_CRACK("Brute force crack"),
	CRACK_TIME_ESTIMATE("Estimated time to crack");
	
	private final String message;
	
	CrackType(String message)
	{
		this.message = message;
	}
	
	public String toString()
	{
		return message;
	}
	
}
