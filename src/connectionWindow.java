import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.sql.*;

public class connectionWindow implements ActionListener{
	Connection myConn = null;
	public String pseudo;
	public String password;
	private JFrame frame = new JFrame("ConnectionGUI");
	private JPanel panel = new JPanel();
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private JPanel panel4 = new JPanel();
	private JPanel panel5 = new JPanel();
	private JLabel labelName = new JLabel("Talk Software");
	private JLabel labelPseudo = new JLabel("Pseudo:      ");
	private JLabel labelPassword = new JLabel("Password:  ");
	private JLabel labelError = new JLabel("<html><p>You are not registered yet.<br>Or your Pseudo/Password is incorrect.</p></html>");
	private JTextField pseudoText = new JTextField();
	private JTextField passwordText = new JTextField();
	private JButton registerButton = new JButton("Register");
	public  JButton connectionButton = new JButton("Sign in");
	
	int currentID = 0;

	public String getPseudo() {
		pseudo = pseudoText.getText();
		return pseudo;
	}
	
	public String getPassword() {
		password = passwordText.getText();
		return password;
	}
	
	public connectionWindow(Connection myConn) {
		this.myConn=myConn;
		frame.setMinimumSize(new Dimension(500, 350));
		frame.setBackground(Color.DARK_GRAY);
		frame.getContentPane().setBackground(Color.blue);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		labelName.setFont(new Font("Calibri", Font.BOLD, 30));
		labelPseudo.setFont(new Font("Dialog", Font.ITALIC, 19));
		labelPassword.setFont(new Font("Dialog", Font.ITALIC, 19));
		pseudoText.setFont(new Font("Dialog", Font.PLAIN, 16));
		passwordText.setFont(new Font("Dialog", Font.PLAIN, 16));
		registerButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		connectionButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		labelError.setFont(new Font("Dialog", Font.PLAIN, 16));
		labelError.setForeground(Color.red);
		
		pseudoText.setMaximumSize(new Dimension(150, 30));
		passwordText.setMaximumSize(new Dimension(150, 30));
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(170, 80));

		
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		panel1.setMaximumSize(new Dimension(250, 50));

		panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
		panel2.setMaximumSize(new Dimension(250, 50));

		panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
		panel3.setMaximumSize(new Dimension(200, 50));

		panel4.setLayout(new BoxLayout(panel4, BoxLayout.PAGE_AXIS));
			
		panel5.setLayout(new BoxLayout(panel5, BoxLayout.LINE_AXIS));
		panel5.setMaximumSize(new Dimension(200, 100));
		panel5.setBackground(Color.GRAY);
		panel5.add(labelError);
		
		panel.add(labelName);
		
		panel1.add(labelPseudo);
		panel1.add(pseudoText);
		
		panel2.add(labelPassword);
		panel2.add(passwordText);
		
		panel3.add(registerButton);
		panel3.add(connectionButton);
		
		panel4.setBackground(Color.GRAY);
		panel.setBackground(Color.GRAY);
		panel1.setBackground(Color.GRAY);
		panel2.setBackground(Color.GRAY);
		panel3.setBackground(Color.GRAY);
		
		panel4.add(panel);
		panel4.add(panel1);
		panel4.add(panel2);
		panel4.add(panel3);
		frame.add(panel4);

		registerButton.addActionListener(this);
		connectionButton.addActionListener(this);
		
		frame.pack();
		frame.setVisible(true);
	}

	boolean checkConnexion(String Pseudo, String Password) throws SQLException  {
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from users where Pseudo='"+Pseudo+"' and Password='"+Password+"';");
			if(myRs.next()) {
				currentID=myRs.getInt("UserID");
				return true;
			}else {
				return false;
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
		}
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == connectionButton) {
			pseudo = getPseudo();
			password = getPassword();
			try {
				if(checkConnexion(pseudo,password)==true) {
					frame.dispose();
					User moi = new User(pseudo,currentID,password);
					MessageWindow message = new MessageWindow(moi, myConn);
				}else {
					panel4.add(panel5);
					panel4.updateUI();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getSource() == registerButton) {
			frame.dispose();
			RegisterGUI register = new RegisterGUI(myConn);
		}
	}
	
	

}
