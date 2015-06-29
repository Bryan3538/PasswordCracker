package com.bfulton.PasswordCracker;

import java.util.concurrent.BlockingQueue;

public class BruteForcePasswordGenerator implements Runnable {
	
	BlockingQueue<String> queue;
	int minAscii = 32, maxAscii = 126, maxChars;
	int startAscii, endAscii;

	public BruteForcePasswordGenerator(BlockingQueue<String> queue, int maxChars) {
		this.queue = queue;
		this.maxChars = maxChars;
	}

	@Override
	public void run() {
		String guess = "";
		try {
			for(int i = 1; i <= maxChars; i++)
			{
				guess = prepString(i);
				queue.put(guess);
				
				while(guess != null)
				{
					guess = getNext(guess);
					//System.out.println(guess);
					
						if(guess != null)
							queue.put(guess);
					
				}
			}	
		} catch (InterruptedException e) {
			return;
		}
	}
	
	private String prepString(int count) {
		String str = "";
		
		str += (char)minAscii;
		for(int i = 1 ; i < count; i++)
			str += (char)minAscii;
		
		return str;
	}
	
	private String getNext(String str) {
		int ascii = minAscii;
		StringBuilder b = new StringBuilder(str);
		
		for(int i = str.length() - 1; i >= 0; i--) {
			ascii = (int)str.charAt(i);
			if(ascii < maxAscii) {
				b.setCharAt(i, (char)(ascii + 1));
				return b.toString();
			} else {
				b.setCharAt(i, (char)minAscii);
			}
		}
		return null;
	}
}
