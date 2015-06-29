package com.bfulton.GUIPasswordCrackerApp;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.bfulton.PasswordCracker.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JPasswordField;

import java.awt.Insets;

import javax.swing.JButton;

import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;

public class PasswordCrackerAppPanel extends JPanel implements ActionListener {
	private boolean running;
	private JPasswordField passwordField;
	private JLabel crackingStatusLabel;
	private PasswordCrackerWorker cracker;
	private TextArea resultsTextArea;
	private JButton startCrackingButton;
	private String password;
	

	/**
	 * Create the panel.
	 */
	public PasswordCrackerAppPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 150, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblEnterAPassword = new JLabel("Enter a password: ");
		GridBagConstraints gbc_lblEnterAPassword = new GridBagConstraints();
		gbc_lblEnterAPassword.insets = new Insets(0, 5, 5, 5);
		gbc_lblEnterAPassword.anchor = GridBagConstraints.EAST;
		gbc_lblEnterAPassword.ipady = 10;
		gbc_lblEnterAPassword.ipadx = 10;
		gbc_lblEnterAPassword.gridx = 0;
		gbc_lblEnterAPassword.gridy = 1;
		add(lblEnterAPassword, gbc_lblEnterAPassword);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.anchor = GridBagConstraints.WEST;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 1;
		passwordField.setPreferredSize(new Dimension(150, 20));
		add(passwordField, gbc_passwordField);
		
		crackingStatusLabel = new JLabel("Enter a Password");
		crackingStatusLabel.setMinimumSize(new Dimension(120, 20));
		crackingStatusLabel.setPreferredSize(new Dimension(120, 20));
		GridBagConstraints gbc_crackingStatusLabel = new GridBagConstraints();
		gbc_crackingStatusLabel.insets = new Insets(0, 0, 5, 0);
		gbc_crackingStatusLabel.gridx = 2;
		gbc_crackingStatusLabel.gridy = 1;
		add(crackingStatusLabel, gbc_crackingStatusLabel);
		
		startCrackingButton = new JButton("Begin Cracking");
		
		startCrackingButton.addActionListener(this);
		
		GridBagConstraints gbc_startCrackingButton = new GridBagConstraints();
		gbc_startCrackingButton.insets = new Insets(0, 0, 5, 5);
		gbc_startCrackingButton.gridx = 1;
		gbc_startCrackingButton.gridy = 2;
		add(startCrackingButton, gbc_startCrackingButton);
		
		resultsTextArea = new TextArea();
		resultsTextArea.setPreferredSize(new Dimension(400, 100));
		resultsTextArea.setEditable(false);
		GridBagConstraints gbc_resultsTextArea = new GridBagConstraints();
		gbc_resultsTextArea.insets = new Insets(0, 0, 5, 0);
		gbc_resultsTextArea.gridwidth = 3;
		gbc_resultsTextArea.gridx = 0;
		gbc_resultsTextArea.gridy = 3;
		add(resultsTextArea, gbc_resultsTextArea);
		
		running = false;	
	}
	
	public synchronized void doneRunning() {
		running = false;
		cracker.cancel(true);
		startCrackingButton.setEnabled(true);
		passwordField.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(startCrackingButton)) {
			if(passwordField.getPassword().length > 0 && !running ) {
				password = new String(passwordField.getPassword());
				cracker = new PasswordCrackerWorker(this, resultsTextArea, 
						crackingStatusLabel, password);
				crackingStatusLabel.setText("Cracking Password");
				resultsTextArea.setText("Starting password crack, this may take a moment.\n");
				startCrackingButton.setEnabled(false);
				cracker.execute();
				running = true;
			}
			else if (passwordField.getPassword().length == 0) {
				resultsTextArea.setText("You must enter a password to crack!");
				crackingStatusLabel.setText("Enter a Password");
				passwordField.requestFocus();
			}
		}
	}

}
