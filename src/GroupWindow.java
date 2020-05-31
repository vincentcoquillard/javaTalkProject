import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GroupWindow implements ActionListener, KeyListener{
	User moi;
	Connection myConn = null;
	
	String contactName = new String();
	String groupName = new String();
	ArrayList<String> contactList = new ArrayList<String>();

	public DefaultListModel<String> dlm = new DefaultListModel<String>();
	
	

	JFrame frame = new JFrame("Create Group");
	JPanel panel = new JPanel();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	JPanel panel6 = new JPanel();

	JLabel title = new JLabel("CREATE GROUP");
	JLabel nameHint = new JLabel("Choose name:  ");
	TextField name = new TextField(10);
	JLabel contactHint = new JLabel("Select Contact: ");
	TextField contact = new TextField(10);
	JButton addButton = new JButton("ADD");
	JButton createButton = new JButton("CREATE");
	JLabel labelError = new JLabel("You didn't select enough contacts !");
	JLabel labelNameError = new JLabel("The group has no name !");
	JLabel labelExistError = new JLabel("We don't know this contact");
	
	@SuppressWarnings({ "rawtypes" })
	JList memberList = new JList<>(dlm);
	
	JScrollPane scroll = new JScrollPane(memberList);
	
	public String getContactName() {
		contactName = contact.getText();
		return contactName;
	}
	
	public String getGroupName() {
		groupName = name.getText();
		return groupName;
	}
	
	public ArrayList<String> getContactList() {
		for(int i = 0 ; i<dlm.getSize() ; i++) {
			contactList.add(dlm.get(i));
		}
		return contactList;
	}
	
	public GroupWindow(User moi, Connection myConn) {
		this.myConn=myConn;
		this.moi=moi;
		
		frame.setSize(400, 350);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		name.setMaximumSize(new Dimension(100, 25));
		contact.setMaximumSize(new Dimension(100, 25));
		labelError.setMaximumSize(new Dimension(150,20));
		labelNameError.setMaximumSize(new Dimension(150,20));



		title.setFont(new Font("Calibri", Font.BOLD, 30));
		nameHint.setFont(new Font("Dialog", Font.PLAIN, 15));
		contactHint.setFont(new Font("Dialog", Font.PLAIN, 15));
		contact.setFont(new Font("Dialog", Font.PLAIN, 15));
		labelError.setFont(new Font("Dialog", Font.PLAIN, 20));
		labelError.setForeground(Color.red);
		labelNameError.setFont(new Font("Dialog", Font.PLAIN, 20));
		labelNameError.setForeground(Color.red);
		labelExistError.setFont(new Font("Dialog", Font.PLAIN, 20));
		labelExistError.setForeground(Color.red);
		
		addButton.setSize(30, 30);
		createButton.setSize(30, 30);
		addButton.setFont(new Font("Dialog", Font.PLAIN, 15));
		createButton.setFont(new Font("Dialog", Font.PLAIN, 15));
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(200, 50));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		panel1.setMaximumSize(new Dimension(275, 25));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));
		panel2.setMaximumSize(new Dimension(275, 40));
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.LINE_AXIS));
		panel3.setMaximumSize(new Dimension(275, 80));
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.LINE_AXIS));
		panel4.setMaximumSize(new Dimension(100, 40));
		panel5.setLayout(new BoxLayout(panel5, BoxLayout.PAGE_AXIS));
		panel6.setLayout(new BoxLayout(panel6, BoxLayout.LINE_AXIS));
		panel6.setMaximumSize(new Dimension(220, 20));
		
		scroll.setViewportView(memberList);
		memberList.setLayoutOrientation(JList.VERTICAL);
		
		contact.addKeyListener(this);
		addButton.addActionListener(this);
		createButton.addActionListener(this);
		
		panel.add(title);
		panel1.add(nameHint);
		panel1.add(name);
		panel2.add(contactHint);
		panel2.add(contact);
		panel2.add(addButton);
		panel3.add(scroll);
		panel4.add(createButton);
		panel5.add(panel);
		panel5.add(panel1);
		panel5.add(panel2);
		panel5.add(panel3);
		panel5.add(panel4);

		frame.add(panel5);

		frame.setVisible(true);

	}
	
	void sendFirstMessagetoGroup(int GroupID) throws SQLException{
		Statement myStmt = null;
		int myRs = 0;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeUpdate("INSERT INTO `messages` (`SenderID`, `ReceiverID`, `Content`, `DateHeure`,`IsGroup`) VALUES ("+moi.getUserID()+", "+0+", 'Bonjour vous avez été ajouté à ce groupe', now(),"+GroupID+");");
			if(myRs==1) {
				//OK
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (myRs != 0) {
				
			}
			if (myStmt != null) {
				myStmt.close();
			}
		}
	}
	

	
	int getGroupID(String GroupName, String MembersList, int MemberNumber) throws SQLException{
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from groups where NumberOfMembers='"+MemberNumber+"' and GroupName='"+GroupName+"' and MembersNames='"+MembersList+"';");
			if(myRs.next()) {
				return myRs.getInt("GroupID");
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
		return 0;
	}
	
	void createGroup(String me) throws SQLException{
		Statement myStmt = null;
		int myRs = 0;
		String MembersNames="";
		for (String s : getContactList())
		{
			MembersNames += s + ";";
		}
		MembersNames = MembersNames + me + ";";
		int var2 = dlm.size()+1;
			try {
				myStmt = myConn.createStatement();
				myRs = myStmt.executeUpdate("INSERT INTO `groups` (`GroupName`, `MembersNames`, `NumberofMembers`) VALUES ('"+getGroupName()+"', '"+MembersNames+"', "+var2+");");
				
				if(myRs==1) {
					JOptionPane.showMessageDialog(null, "Great! You have created a group.");
					int var = getGroupID(getGroupName(), MembersNames, var2);
					sendFirstMessagetoGroup(var);
					frame.dispose();
				}
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
			finally {
				if (myRs != 0) {
				}
				if (myStmt != null) {
					myStmt.close();
				}
				
			}
	}

	boolean checkIfExists(String Pseudo) throws SQLException  {
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7343452", "sql7343452" , "pJljWyAJ8x");
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
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == addButton) {
			contactName = getContactName();
			try {
				if(checkIfExists(contactName) && !contactName.equals(moi.getPseudo()) && !dlm.contains(contactName)) {
						dlm.addElement(contactName);
				}else {
					JOptionPane.showMessageDialog(null, "Please select a contact that exists. And not your pseudo.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getSource() == createButton) {
			if(dlm.isEmpty() == true || dlm.getSize() == 1) {
				panel6.add(labelError);
				panel5.add(panel6);
				frame.add(panel5);
				frame.setVisible(true);
			}
			else if(name == null) {
				panel6.add(labelNameError);
				panel5.add(panel6);
				frame.add(panel5);
				frame.setVisible(true);
			}else {
				try {
					createGroup(moi.getPseudo());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			addButton.doClick();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
