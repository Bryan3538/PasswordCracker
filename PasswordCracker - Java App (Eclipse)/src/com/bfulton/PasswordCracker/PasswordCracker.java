/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bfulton.PasswordCracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bryan Fulton
 */
public class PasswordCracker {
    private static final String DEFAULT_DICTIONARY = "dictionary.txt";
	public int maxChars;
    private int minAscii;
    private int maxAscii;
    private String dictionary;
    private String password;
    private LinkedList<PasswordCrackListener> listeners;
    private CrackType crackMethod;
    

   

    /**
     * Sets dictionary to default
     * Sets crackMethod to brute force
     */
    public PasswordCracker()
    {
    	//dictionary = new File("human_passwords_10.txt");
    	dictionary = DEFAULT_DICTIONARY;
    	crackMethod = CrackType.BRUTE_FORCE_CRACK;
    	maxChars = 4;
    	minAscii = 32;
    	maxAscii = 126;
    	listeners = new LinkedList<>();
    }

	/**
     * Sets dictionary to default
     * Sets crackMethod to brute force
     * Sets maximum number of allowed characters in the password to maxChars
     * @param maxChars The maximum number of characters to allow
     */
    public PasswordCracker(int maxChars)
    {
    	this();
    	if(maxChars > 0)
    		this.maxChars = maxChars;
    	else
    		throw new IllegalArgumentException("maxChars must be > 0");
    }
    
    /**
     * Sets dictionary to default
     * Sets crackMethod to brute force
     * @param maxChars The maximum number of characters to allow
     * @param minAscii The first character to start brute force cracks at
     * @param maxAscii The last character to check in brute force cracks
     */
    public PasswordCracker(int maxChars, char minAscii, char maxAscii) {	
    	this(maxChars);
    
    	if((int)minAscii >= 0 && (int) maxAscii <= 127) {
    		this.minAscii = (int)minAscii;
    		this.maxAscii = (int)maxAscii;
    	} else {
    		throw new IllegalArgumentException("The range of ASCII characters was invalid. " 
    				+ "minAscii: " + minAscii + "\tmaxAscii: " + maxAscii);
    	}
    }
    
    /**
     * Sets crackMethod to brute force
     * @param maxChars The maximum number of characters to allow
     * @param minAscii The first character to start brute force cracks at
     * @param maxAscii The last character to check in brute force cracks
     * @param dictionaryPath The path to the dictionary to use for dictionary attacks
     */
    public PasswordCracker(int maxChars, char minAscii, char maxAscii, String dictionaryPath) {
    	this(maxChars, minAscii, maxAscii);
    	
    	if(!setDictionary(dictionaryPath))
    		throw new IllegalArgumentException("The dictionary provided does not exist!");
    	
    }
    
    /**
     * Sets the type of password crack algorithm to use
     * @param crackMethod A CrackType constant representing the crack method to use
     */
    public void setCrackType(CrackType crackMethod) {
    	this.crackMethod = crackMethod;
    }
    
    public CrackType getCrackType() {
    	return crackMethod;
    }
    
    public void setMaxChars(int max) {
    	maxChars = max;
    }
    
    public int getMaxChars() {
    	return maxChars;
    }
    
    public void setMaxAscii(char max) {
    	maxAscii = (int)max;
    }
    
    public char getMaxAscii() {
    	return (char)maxAscii;
    }
    
    public boolean setDictionary(String dictionaryPath) {
    	File f = new File(dictionaryPath);
    	
    	if(f.exists()) {
    		dictionary = dictionaryPath;
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public String getDictionary() {
    	return dictionary.toString();
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
    
    public void addPasswordCrackListener(PasswordCrackListener listener) {
    	if(!listeners.contains(listener) && listener != null)
    		listeners.add(listener);
    }
    
    private String estimateTimeToCrack(String password)
    { 
    	//I divided triesPerMilli and combinations by 1000
    	//to try and keep numbers lower
        long triesPerMilli = 800;
        int numberOfCharacters = (maxAscii - minAscii) + 1;
        long time = 0;
        long combinations = 0;
        for(int i = password.length(); i > 0; i--) {
        	combinations = (long) (Math.pow(numberOfCharacters, i));
        	time += (combinations / triesPerMilli);
        }
        return TimeFormatter.formatTime(time);
    }
    
    private String bruteForce(String password)  {
    	String guess = "";
		String raw = "";
    	BlockingQueue<String> queue = new LinkedBlockingQueue<>(50000);
		
		Thread producer = new Thread(new BruteForcePasswordGenerator(queue, maxChars));
		producer.start();
		
		while(!password.equals(guess) && producer.isAlive()) {
			try {
				raw = queue.take();
				guess = Hasher.md5(raw);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
		//stop threads 
		producer.interrupt();
		if(password.equals(guess))
			return raw;
		else
			return null;
    }
    
	private String dictionaryAttack(String password) {
		String line = null;
		BufferedReader reader = null;

		try {
			if (dictionary == DEFAULT_DICTIONARY) {
				if (PasswordCracker.class.getClassLoader().getResource(dictionary) != null) {
					reader = new BufferedReader(new InputStreamReader(
							PasswordCracker.class.getClassLoader()
									.getResourceAsStream(DEFAULT_DICTIONARY)));
				} else {
					return null;
				}
			} else {
				reader = new BufferedReader(new FileReader(dictionary));
			}

		} catch (FileNotFoundException ex) {
			return null;
		}

		try {
			while ((line = reader.readLine()) != null) {
				if (line.equals(password)) {
					reader.close();
					break;
				}
			}
			reader.close();
		} catch (IOException ex) {
			Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
		}

		return line;
	}
    
    public void crackPassword()
    {
    	String guess = null;
    	
    	switch(crackMethod) {

		case BRUTE_FORCE_CRACK:
			if(password.length() <= maxChars)
				guess = bruteForce(Hasher.md5(password));
			else
				guess = null;
			break;
		case CRACK_TIME_ESTIMATE:
			guess = estimateTimeToCrack(password);
			break;
		case DICTIONARY_ATTACK:
			guess = dictionaryAttack(password);
			break;
		default:
			break;
    	}
    	
    	//raise password crack event and feed it guess
    	if(guess != null)
    		for(PasswordCrackListener listener : listeners)
    			listener.passwordCracked(guess);
    	else
    		for(PasswordCrackListener listener : listeners)
    			listener.passwordNotCracked();
    }

    
    //Scrapped for final version, may re-implement later
    /*
     private void saveToDictionary(String password) {
        FileWriter writer = null;
        
        try {
            writer = new FileWriter(dictionary, true);
            writer.append("\n" + password);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }
    }
    */
}

