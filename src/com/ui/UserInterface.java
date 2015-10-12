package com.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JTextArea;

import com.controller.Controller;

public class UserInterface extends JFrame {

	private JPanel contentPane;
	private JTextField txtCv;
	private JTextField fileSelected;
	private JTextField txtPosition;
	private JTextField txtEducationalRequirements;
	private JTextField eduValue;
	private JTextField txtTechnicalSkillsRequired;
	private JTextField techValue;
	private JButton btnSubmit;
	private JTextField txtPosition_1;
	private JTextField positionValue;
	private JTextField displayResults;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface frame = new UserInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 554, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ArrayList<String> cvNames = new ArrayList<String>();
		Controller control = new Controller();
		
		txtCv = new JTextField();
		txtCv.setText("CV: ");
		txtCv.setEditable(false);
		txtCv.setBounds(10, 24, 86, 20);
		contentPane.add(txtCv);
		txtCv.setColumns(10);
		
		fileSelected = new JTextField();
		fileSelected.setBounds(119, 24, 206, 20);
		contentPane.add(fileSelected);
		fileSelected.setColumns(10);
		
		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            JFileChooser chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File("."));
	            chooser.setDialogTitle("Browse the folder to process");
	            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            chooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
	            chooser.setAcceptAllFileFilterUsed(true);
	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	            	File selectedFile = chooser.getSelectedFile();
	                String fileName = selectedFile.getAbsolutePath();
	                fileSelected.setText(fileName);
	            }
	           
	        }
	        
	    });
	    getContentPane().add(browse);
		browse.setBounds(335, 23, 89, 23);
		contentPane.add(browse);
		
		txtPosition = new JTextField();
		txtPosition.setEditable(false);
		txtPosition.setText("Job Description: ");
		txtPosition.setBounds(10, 78, 181, 20);
		contentPane.add(txtPosition);
		txtPosition.setColumns(10);
		
		txtEducationalRequirements = new JTextField();
		txtEducationalRequirements.setEditable(false);
		txtEducationalRequirements.setText("Educational Requirements: ");
		txtEducationalRequirements.setBounds(10, 140, 199, 20);
		contentPane.add(txtEducationalRequirements);
		txtEducationalRequirements.setColumns(10);
		
		eduValue = new JTextField();
		eduValue.setBounds(219, 140, 296, 20);
		contentPane.add(eduValue);
		eduValue.setColumns(10);
		
		txtTechnicalSkillsRequired = new JTextField();
		txtTechnicalSkillsRequired.setEditable(false);
		txtTechnicalSkillsRequired.setText("Technical Skills Required: ");
		txtTechnicalSkillsRequired.setBounds(10, 171, 199, 23);
		contentPane.add(txtTechnicalSkillsRequired);
		txtTechnicalSkillsRequired.setColumns(10);
		
		techValue = new JTextField();
		techValue.setBounds(219, 171, 296, 22);
		contentPane.add(techValue);
		techValue.setColumns(10);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String position = positionValue.getText();
				String eduReq=eduValue.getText();
				String techSkills = techValue.getText();
				String fileName = fileSelected.getText();
				
				cvNames.add(fileName);
				 ArrayList<ArrayList<String>> results = control.processJobDescAndCV(position, eduReq, techSkills, cvNames);
				 
				 displayResults.setText(results.toString());
				  System.out.println(results.toString());
			}
			
		});
		btnSubmit.setBounds(120, 397, 89, 23);
		contentPane.add(btnSubmit);
		
		txtPosition_1 = new JTextField();
		txtPosition_1.setEditable(false);
		txtPosition_1.setText("Position: ");
		txtPosition_1.setBounds(10, 109, 199, 20);
		contentPane.add(txtPosition_1);
		txtPosition_1.setColumns(10);
		
		positionValue = new JTextField();
		positionValue.setBounds(219, 109, 296, 20);
		contentPane.add(positionValue);
		positionValue.setColumns(10);
		
		displayResults = new JTextField();
		displayResults.setBounds(10, 213, 505, 173);
		contentPane.add(displayResults);
		displayResults.setColumns(10);
		
		JButton btnNewButton = new JButton("Clear");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayResults.setText("");
				fileSelected.setText("");
				positionValue.setText("");
				eduValue.setText("");
				techValue.setText("");
				
				
			}
		});
		btnNewButton.setBounds(318, 397, 89, 23);
		contentPane.add(btnNewButton);
	}
}
