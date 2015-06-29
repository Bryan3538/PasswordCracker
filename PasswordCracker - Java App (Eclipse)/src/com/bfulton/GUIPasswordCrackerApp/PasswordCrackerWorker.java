package com.bfulton.GUIPasswordCrackerApp;

import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.bfulton.PasswordCracker.*;

public class PasswordCrackerWorker extends SwingWorker<Void, Void> implements PasswordCrackListener {

	private JLabel crackingStatusLabel;
	private CrackType currentCrackMethod;
	private TextArea resultsTextArea;
	private long startTime, endTime;
	private String password;
	private static final int MAX_BRUTE_CHARS = 4;
	private static final int MAX_PW_CHARS = 16;
	private PasswordCrackerAppPanel parent;
	private PasswordCracker cracker;
	
	private PasswordCrackerWorker() {
	}
	
	PasswordCrackerWorker(PasswordCrackerAppPanel parent, TextArea resultsTextArea, 
			JLabel crackingStatusLabel, String password) {
		this.parent = parent;
		this.resultsTextArea = resultsTextArea;
		this.password = password;
		this.crackingStatusLabel = crackingStatusLabel;
	}
	
	@Override
	public void passwordCracked(String crackedPassword) {
		if(currentCrackMethod != CrackType.CRACK_TIME_ESTIMATE) {
			endTime = System.currentTimeMillis();
			double timeElapsed = (endTime - startTime) / 1000.0;
			resultsTextArea.append(
					String.format("Your password was successfully cracked after "
							+ "%.2f seconds!\n", timeElapsed));
			resultsTextArea.append("Your password is: " + crackedPassword + "\n");
			crackingStatusLabel.setText("Password Cracked!");
		} else {
			resultsTextArea.append("The estimated time to brute force crack your password is: \n    " 
					+ crackedPassword + "\n");	
		}
	}

	@Override
	public void passwordNotCracked() {
		resultsTextArea.append("I could not crack your password with the current method.\n");
		
		nextCrackMethod();
		
		if(currentCrackMethod != null) {
			startPasswordCrack();
		} else {
			resultsTextArea.append("An error has occurred while cracking your password.");
		}
	}

	private void nextCrackMethod() {
		if(currentCrackMethod == CrackType.DICTIONARY_ATTACK && password.length() <= MAX_BRUTE_CHARS)
			currentCrackMethod = CrackType.BRUTE_FORCE_CRACK;
		else if ( (currentCrackMethod == CrackType.DICTIONARY_ATTACK && 
				password.length() > MAX_BRUTE_CHARS) || (currentCrackMethod == CrackType.BRUTE_FORCE_CRACK)) {
			currentCrackMethod = CrackType.CRACK_TIME_ESTIMATE;
		} else {
			currentCrackMethod = null;
		}
	}

	private void startPasswordCrack() {
		
		if (password.length() > MAX_PW_CHARS) {
			resultsTextArea.append("Your password is too long to be cracked" +
					"!\nPlease enter another.\n");
			crackingStatusLabel.setText("Enter a Password");
			return;
		}
		
		
		resultsTextArea.append("Beginning password cracking with method: " 
				+ currentCrackMethod.toString() + "\n");
		
		cracker = new PasswordCracker();
		cracker.addPasswordCrackListener(this);
		cracker.setCrackType(currentCrackMethod);
		cracker.setPassword(password);
		cracker.setMaxChars(MAX_BRUTE_CHARS);
		
		cracker.crackPassword();
	}

	@Override
	protected Void doInBackground()  {
		currentCrackMethod = CrackType.DICTIONARY_ATTACK;
		startTime = System.currentTimeMillis();
		startPasswordCrack();
	
		parent.doneRunning();
		return null;
	}

}
