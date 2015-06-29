package com.bfulton.PasswordCracker;

public interface PasswordCrackListener {
	
	public void passwordCracked(String crackedPassword);
	
	public void passwordNotCracked();
}
