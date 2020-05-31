import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class RegisterGUI implements ActionListener {
	Connection myConn = null;
	private JFrame frame = new JFrame("RegisterGUI");
	public String pseudo;
	public String password;
	private JPanel panel = new JPanel();
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private JPanel panel4 = new JPanel();
	private JPanel panel5 = new JPanel();
	private JLabel labelName = new JLabel("Register");
	private JLabel labelPseudo = new JLabel("Pseudo:      ");
	private JLabel labelPassword = new JLabel("Password:  ");
	private JLabel labelErrorPseudo = new JLabel("Pseudo already used");

	private JTextField pseudoText = new JTextField();
	private JTextField passwordText = new JTextField();
	private JButton registerButton = new JButton("Register");
	
	public String getPseudo() {
		pseudo = pseudoText.getText();
		return pseudo;
	}
	
	public String getPassword() {
		password = passwordText.getText();
		return password;
	}
	
	
	public RegisterGUI(Connection myConn) {
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
		labelErrorPseudo.setFont(new Font("Dialog", Font.PLAIN, 16));
		labelErrorPseudo.setForeground(Color.red);

		pseudoText.setMaximumSize(new Dimension(150, 30));
		passwordText.setMaximumSize(new Dimension(150, 30));
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(100, 80));

		
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		panel1.setMaximumSize(new Dimension(250,50));

		panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
		panel2.setMaximumSize(new Dimension(250, 50));

		panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
		panel3.setMaximumSize(new Dimension(100, 50));
		panel3.setBackground(Color.red);

		panel4.setLayout(new BoxLayout(panel4, BoxLayout.PAGE_AXIS));
			
		panel5.setLayout(new BoxLayout(panel5, BoxLayout.LINE_AXIS));
		panel5.setMaximumSize(new Dimension(150, 50));
		labelErrorPseudo.setForeground(Color.red);
		panel5.setBackground(Color.GRAY);
		
		panel.add(labelName);
		
		panel1.add(labelPseudo);
		panel1.add(pseudoText);
		
		panel2.add(labelPassword);
		panel2.add(passwordText);
		
		panel3.add(registerButton);
		
		panel4.setBackground(Color.GRAY);
		panel.setBackground(Color.GRAY);
		panel1.setBackground(Color.GRAY);
		panel2.setBackground(Color.GRAY);
		panel3.setBackground(Color.GRAY);

		panel5.add(labelErrorPseudo);
		
		panel4.add(panel);
		panel4.add(panel1);
		panel4.add(panel2);
		panel4.add(panel3);
		frame.add(panel4);
		

		registerButton.addActionListener(this);
		
		
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	boolean checkAlreadyExists(String Pseudo, String Password) throws SQLException  {
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from users where Pseudo='"+Pseudo+"';");
			if(myRs.next()) {
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
	
	void addUser(String Pseudo, String Password) throws SQLException  {
		Statement myStmt = null;
		int myRs = 0;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeUpdate("insert into users (Pseudo, Password) values ('"+Pseudo+"','"+Password+"');");
			if(myRs==1) {
				System.out.print("OK");
			}else {
				System.out.print("PAS ok");
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (myStmt != null) {
				myStmt.close();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == registerButton) {
			pseudo = getPseudo();
			password = getPassword();
			try {
				if(checkAlreadyExists(pseudo,password)==true) {
					panel4.add(panel5);
					panel4.updateUI();
				}else {
					addUser(pseudo,password);
					frame.dispose();
					connectionWindow co = new connectionWindow(myConn);
				}
			} catch (SQLException e) {
				//e.printStackTrace();
			}
			
		}
	}

}
