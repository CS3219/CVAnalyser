package com.UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserInterface extends JFrame {

	private JPanel contentPane;
	private JTextField txtCv;
	private JTextField textField;
	private JTextField txtPosition;
	private JTextField txtEducationalRequirements;
	private JTextField textField_1;
	private JTextField txtTechnicalSkillsRequired;
	private JTextField textField_2;
	private JButton btnSubmit;

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
		setBounds(100, 100, 468, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtCv = new JTextField();
		txtCv.setText("CV: ");
		txtCv.setEditable(false);
		txtCv.setBounds(10, 24, 86, 20);
		contentPane.add(txtCv);
		txtCv.setColumns(10);
		
		textField = new JTextField();
		textField.setBounds(119, 24, 206, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(335, 23, 89, 23);
		contentPane.add(btnNewButton);
		
		txtPosition = new JTextField();
		txtPosition.setEditable(false);
		txtPosition.setText("Job Description: ");
		txtPosition.setBounds(10, 78, 181, 20);
		contentPane.add(txtPosition);
		txtPosition.setColumns(10);
		
		txtEducationalRequirements = new JTextField();
		txtEducationalRequirements.setEditable(false);
		txtEducationalRequirements.setText("Educational Requirements: ");
		txtEducationalRequirements.setBounds(10, 125, 199, 20);
		contentPane.add(txtEducationalRequirements);
		txtEducationalRequirements.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(219, 125, 223, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		txtTechnicalSkillsRequired = new JTextField();
		txtTechnicalSkillsRequired.setEditable(false);
		txtTechnicalSkillsRequired.setText("Technical Skills Required: ");
		txtTechnicalSkillsRequired.setBounds(10, 173, 199, 23);
		contentPane.add(txtTechnicalSkillsRequired);
		txtTechnicalSkillsRequired.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(219, 173, 223, 22);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(198, 239, 89, 23);
		contentPane.add(btnSubmit);
	}
}
