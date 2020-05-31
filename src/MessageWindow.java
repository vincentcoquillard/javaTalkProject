import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class MessageWindow extends JFrame implements ActionListener {
	Connection myConn = null;
	String prevSelectedUser = "nul";
	private JFrame win;
	private JPanel topPanel, bottomPanel;
	JButton add = new JButton("  ADD CONTACT  ");
	JButton group = new JButton("  CREATE GROUP  ");
	JButton send = new JButton("  SEND >>  ");
	//JPanel conv = new JPanel(new FlowLayout());
	JPanel conv = new JPanel();
	
	JScrollPane scrollMessage = new JScrollPane(conv);
	private GroupWindow groupWin;
	String selectedUser="null";
	User moi;
	
	JTextField MSG = new JTextField("Type your message here !", 60);
	
		/*public String getSelectedUser() {
			selected = contactList.getSelectedIndex();
			selectedUser = (String) contactList.getModel().getElementAt(selected);
			return selectedUser;
		}*/
	
	public DefaultListModel<String> dlm = new DefaultListModel<String>();
	
	@SuppressWarnings({ "rawtypes" })
	JList contactList = new JList<>(dlm);
	
	JScrollPane scroll = new JScrollPane(contactList);
	JTextField CONTACT;
	
	public MessageWindow (User moi, Connection myConn) {
	this.moi=moi;
	this.myConn=myConn;
	JFrame win = new JFrame();
	win.setTitle("Messagerie");
	   win.setSize(1500, 800);
	   win.setLocationRelativeTo(null);
	   win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
	   //1er BORDER
	   JPanel border1 = new JPanel(new BorderLayout());
	   border1.setBackground(Color.black);
	
	   
	   //TOP 1er BORDER
	JPanel topPanel = new JPanel();
	topPanel.setBackground(new Color(140,90,219));
	topPanel.add(new JLabel("MESSAGERIE REVVS - "+moi.getPseudo()));
	
	border1.add(topPanel, BorderLayout.NORTH);
	
	//2eme BORDER
	JPanel border2 = new JPanel(new BorderLayout());
	border2.setBackground(new Color(147,112,219));
	
	
	//TOP 2eme BORDER
	JLabel message = new JLabel("MESSAGES");
	message.setForeground(new Color(176,196,232));
	border2.add(message, BorderLayout.NORTH);
	
	//CENTER
	JPanel centerPanel = new JPanel(new BorderLayout());
	
	//CENTER LEFT
	JPanel leftside = new JPanel(new BorderLayout());
	leftside.setBackground(new Color(176,196,232));
	//leftside.setBorder(BorderFactory.createRaisedBevelBorder()); //Borders Raised
	leftside.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, new Color(147,112,219)));
	//leftside.setBorder(BorderFactory.createLineBorder(Color.black)); //Black Line Borders
	
	//CENTER LEFT TOP
	JPanel topleftside = new JPanel();
	topleftside.setBackground(new Color(176,196,232));
	CONTACT = new JTextField("Select contact",25);
	
	
	
	CONTACT.addFocusListener(new FocusListener() {
	
		@Override
		public void focusGained(FocusEvent arg0) {
			
			CONTACT.setText("");
		}
	
		@Override
		public void focusLost(FocusEvent arg0) {
			/*if(arg0.getSource() != add) {
				CONTACT.setText("Select contact");
			}*/
		}
	});
	
	
	topleftside.add(CONTACT);
	add.setBackground(new Color(176,196,222));
	add.setBorder(BorderFactory.createRaisedBevelBorder());
	add.addActionListener(this);
	topleftside.add(add);
	leftside.add(topleftside, BorderLayout.NORTH);
	
	//CENTER LEFT LEFT
	JPanel leftleftside = new JPanel();
	leftleftside.setBackground(new Color(176,196,232));
	
	leftside.add(leftleftside, BorderLayout.WEST);
	
	//CENTER LEFT RIGHT
	JPanel rightleftside = new JPanel();
	rightleftside.setBackground(new Color(176,196,232));
	leftside.add(rightleftside, BorderLayout.EAST);
	
	//CENTER LEFT MIDDLE
	JPanel middleleftside = new JPanel();
	middleleftside.setBackground(new Color(70,130,180));
	middleleftside.setBorder(BorderFactory.createLoweredBevelBorder());
	contactList.setBackground(new Color(70, 130, 180));
	middleleftside.add(scroll);
	scroll.setFont(new Font("Dialog", Font.PLAIN, 30));
	contactList.setFont(new Font("Calibri", Font.PLAIN, 30));
	
	/*dlm.addElement("vincoz");*/
	try {
		loadContacts(moi);
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	
	//middleleftside.setBorder(BorderFactory.createLineBorder(Color.black));
	leftside.add(scroll, BorderLayout.CENTER);
	
	//CENTER LEFT BOTTOM
	JPanel bottomleftside = new JPanel();
	bottomleftside.setBackground(new Color(176,196,232));
	group.addActionListener(this);
	group.setBackground(new Color(176,196,222));
	group.setBorder(BorderFactory.createRaisedBevelBorder());
	bottomleftside.add(group);
	leftside.add(bottomleftside, BorderLayout.SOUTH);
	
	leftside.setPreferredSize(new Dimension(400,600));
	centerPanel.add(leftside, BorderLayout.WEST);
	
	//CENTER MIDDLE
	JPanel middle = new JPanel(new BorderLayout());
	middle.setBackground(new Color(176,196,232));
	//middle.setBorder(BorderFactory.createLoweredBevelBorder()); //Borders Lowered
	middle.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(147,112,219)));
	//middle.setBorder(BorderFactory.createLineBorder(Color.black)); //Black Line Borders
	
	//CENTER MIDDLE LEFT
	JPanel separationLEFT = new JPanel();
	separationLEFT.setBackground(new Color(176,196,232));
	middle.add(separationLEFT, BorderLayout.WEST);
	
	//CENTER MIDDLE RIGHT
	JPanel separationRIGHT = new JPanel();
	separationRIGHT.setBackground(new Color(176,196,232));
	middle.add(separationRIGHT, BorderLayout.EAST);
	
	//CENTER MIDDLE TOP
	JPanel NAME = new JPanel();
	NAME.setBackground(new Color(176,196,232));
	NAME.add(new JLabel("'NAME'"));
	NAME.setPreferredSize(new Dimension(100,36));
	middle.add(NAME, BorderLayout.NORTH);
	
	//CENTER MIDDLE CENTER
	
	conv.setLayout(new BoxLayout(conv, BoxLayout.Y_AXIS));
	conv.setBackground(new Color(130,160,237));
	//conv.setPreferredSize(new Dimension(450,3000));
	//conv.setSize(new Dimension(450,(int)getPreferredSize().getHeight()));
	conv.setBorder(BorderFactory.createLoweredBevelBorder());
	conv.setBorder(BorderFactory.createLineBorder(Color.black));
	
	

	middle.add(scrollMessage);
	
	//CENTER MIDDLE BOTTOM
	JPanel TEXT = new JPanel();
	TEXT.setBackground(new Color(176,196,232));
	
	
	
	MSG.addFocusListener(new FocusListener() {
	
		@Override
		public void focusGained(FocusEvent arg0) {
			MSG.setText("");
		}
	
		@Override
		public void focusLost(FocusEvent arg0) {
			//MSG.setText("Type your message here !");
			
		}
	});
	TEXT.add(MSG);
	String getSMSValue = MSG.getText();
	
	send.setBackground(new Color(176,196,222));
	send.setBorder(BorderFactory.createRaisedBevelBorder());
	send.addActionListener(this);
	
	TEXT.add(send);
	
	middle.add(TEXT, BorderLayout.SOUTH);
	
	centerPanel.add(middle, BorderLayout.CENTER);
	border2.add(centerPanel, BorderLayout.CENTER);
	
	//BORDER 2 SOUTH
	JLabel share = new JLabel("SHARE WITH YOUR FRIENDS:   [f]  [o]");
	share.setForeground(new Color(176,196,232));
	border2.add(share, BorderLayout.SOUTH);
	
	border1.add(border2, BorderLayout.CENTER);
	  //BORDER 1 BOTTOM
	JPanel bottomPanel = new JPanel();
	bottomPanel.setBackground(new Color(140,90,219));
	bottomPanel.add(new JLabel("TERMS AND CONDITIONS"));
	
	border1.add(bottomPanel, BorderLayout.SOUTH);
	win.add(border1);
	
	win.pack();
	scroll.requestFocus();
	win.setVisible(true);
	
	scrollToBottom(scrollMessage); //Scroll to bottom at start
	 
	   MouseListener mouseListener = new MouseAdapter() {
		      public void mouseClicked(MouseEvent mouseEvent) {
		    	
		        JList theList = (JList) mouseEvent.getSource();
		        if (mouseEvent.getClickCount() == 1) {
		          int index = theList.locationToIndex(mouseEvent.getPoint());
		          if (index >= 0) {
		            Object o = theList.getModel().getElementAt(index);
		           	try {
		           		if(!prevSelectedUser.equals(o.toString())) {
		           			NAME.removeAll();
				           	NAME.add(new JLabel(o.toString()));
				           	selectedUser=o.toString();
				           	NAME.updateUI();
		           			loadMessages(getIDFromPseudo(selectedUser));
		           			prevSelectedUser=selectedUser; //Prevent click spamming
		           		}
		    			
		    		} catch (SQLException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		          }
		        }
		      }
		    };
		contactList.addMouseListener(mouseListener);
		win.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(true);
		
		new Thread(new Runnable() {
		    public void run() {
		    	try {
		    		while(true) {
		    			TimeUnit.SECONDS.sleep(5);
						if(selectedUser != "null") {
							loadMessages(getIDFromPseudo(selectedUser));
							loadContacts(moi);
						}
		    		}
				} catch (InterruptedException | SQLException e) {
					e.printStackTrace();
				}
		    }
		}).start();
		
		win.addWindowListener(new WindowAdapter() { //Close database connection when quiting
			 
			@Override
			 
			public void windowClosing(WindowEvent e) {
				try {
					myConn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			    System.exit(0);
			 
			}
			 
			  });
	}
	
	private void scrollToBottom(JScrollPane scrollPane) { //Scroll to bottom when new message
	    JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
	    AdjustmentListener downScroller = new AdjustmentListener() {
	        @Override
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	            Adjustable adjustable = e.getAdjustable();
	            adjustable.setValue(adjustable.getMaximum());
	            verticalBar.removeAdjustmentListener(this);
	        }
	    };
	    verticalBar.addAdjustmentListener(downScroller);
	}
	
	
	
	String getGroupMembers(int GroupID) throws SQLException{
		
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from groups where GroupID="+GroupID+";");
			if(myRs.next()) {
				return myRs.getString("MembersNames");
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
		return "null";
	}
	
	void loadContacts(User moi) throws SQLException {
			Statement myStmt = null;
			ResultSet myRs = null;
			Statement myStmt2 = null;
			ResultSet myRs2 = null;
			try {
				myStmt = myConn.createStatement();
				myRs = myStmt.executeQuery("select * from messages where SenderID="+moi.getUserID()+" or ReceiverID="+moi.getUserID()+" AND IsGroup is NULL;");
				
				while(myRs.next()) {
						if((myRs.getInt("SenderID") != moi.getUserID())) {
							if(!dlm.contains(getPseudoFromID(myRs.getInt("SenderID")))) {
								String pseudo = getPseudoFromID(myRs.getInt("SenderID"));
								if(pseudo!="null") {
									dlm.addElement(pseudo);
								}
							}
						}
						if(myRs.getInt("ReceiverID") != moi.getUserID()) {
							if(!dlm.contains(getPseudoFromID(myRs.getInt("ReceiverID")))) {
								String pseudo = getPseudoFromID(myRs.getInt("ReceiverID"));
								if(pseudo!="null") {
									dlm.addElement(pseudo);
								}
									
							}
						}
				}
				myStmt2 = myConn.createStatement();
				myRs2 = myStmt2.executeQuery("select * from messages where IsGroup is not NULL;");
				while(myRs2.next()) {
					String vartest = getGroupMembers(myRs2.getInt("IsGroup"));
					ArrayList<String> list = new ArrayList<String>(Arrays.asList(vartest.split(";")));
					for (String e : list) { 		      
				           if(e.equals(moi.getPseudo())) {
				        	   if(!dlm.contains(getGroupName(myRs2.getInt("IsGroup"))+" - GROUP with: "+getGroupMembers(myRs2.getInt("IsGroup")))) {
				        		   
				        		   dlm.addElement(getGroupName(myRs2.getInt("IsGroup"))+" - GROUP with: "+getGroupMembers(myRs2.getInt("IsGroup")));
				        	   }
				           }
				           e=null; //Free memory
				    }
					
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
				if (myRs2 != null) {
					myRs2.close();
				}
				if (myStmt2 != null) {
					myStmt2.close();
				}
			}
	}
	
	void loadMessages(int friendID) throws SQLException {
		Statement myStmt = null;
		ResultSet myRs = null;
		Statement myStmt2 = null;
		ResultSet myRs2 = null;
		conv.removeAll();
		try {
			if(friendID>0) {
				myStmt = myConn.createStatement();
				myRs = myStmt.executeQuery("select * from messages where (SenderID="+moi.getUserID()+" AND ReceiverID="+friendID+") OR (ReceiverID="+moi.getUserID()+" AND SenderID="+friendID+") AND IsGroup IS NULL ORDER BY DateHeure;");
				while(myRs.next()) {
					if(myRs.getInt("SenderID")==friendID) {
						JLabel label2 = new JLabel();
						label2.setText("<html><p style=\"width:350px; padding: 10px 6px 10px 12px;\">"+myRs.getString("Content")+"</p></html>"); //gauche
						conv.add(label2);
						conv.updateUI();
						scrollMessage.updateUI();
						scrollToBottom(scrollMessage);
					}
					if(myRs.getInt("SenderID")==moi.getUserID()){
						JLabel label2 = new JLabel();
						label2.setText("<html><p style=\"width:350px; padding: 10px 12px 10px 6px; text-align: right;\">"+myRs.getString("Content")+"</p></html>"); //Droit
						label2.setHorizontalAlignment(SwingConstants.RIGHT);
						conv.add(label2);
						conv.updateUI();
						scrollMessage.updateUI();
						scrollToBottom(scrollMessage);
					}
				}
			}else {
				myStmt2 = myConn.createStatement();
				myRs2 = myStmt2.executeQuery("select * from messages where IsGroup IS NOT NULL ORDER BY DateHeure;");
				while(myRs2.next()) {
					String vartest = getGroupMembers(myRs2.getInt("IsGroup"));
					ArrayList<String> list = new ArrayList<String>(Arrays.asList(vartest.split(";")));
					for (String e : list) { 		      
				           if(e.equals(moi.getPseudo())) {
				        	   if(myRs2.getInt("SenderID") != moi.getUserID()) {
				        		   String str = selectedUser;
				        		   String substr = " - GROUP with: ";
				        		   String after = str.substring(str.indexOf(substr) + substr.length());
				        		   if(vartest.equals(after)) {
										JLabel label2 = new JLabel();
										label2.setText("<html><p style=\"width:350px; padding: 10px 6px 10px 12px;\">"+myRs2.getString("Content")+"</p></html>"); //gauche
										conv.add(label2);
										conv.updateUI();
										scrollMessage.updateUI();
										scrollToBottom(scrollMessage);
				        		   }
								}
								if(myRs2.getInt("SenderID") == moi.getUserID()){
									String str = selectedUser;
									String substr = " - GROUP with: ";
									String after = str.substring(str.indexOf(substr) + substr.length());
									if(vartest.equals(after)) {
										JLabel label2 = new JLabel();
										label2.setText("<html><p style=\"width:350px; padding: 10px 12px 10px 6px; text-align: right;\">"+myRs2.getString("Content")+"</p></html>"); //Droit
										label2.setHorizontalAlignment(SwingConstants.RIGHT);
										conv.add(label2);
										conv.updateUI();
										scrollMessage.updateUI();
										scrollToBottom(scrollMessage);
									}
								}
				           }
				           e=null; //Free memory
				    }
				}
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
			if (myRs2 != null) {
				myRs2.close();
			}
			if (myStmt2 != null) {
				myStmt2.close();
			}
		}
	}
	
	String getPseudoFromID(int ID) throws SQLException{
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from users where UserID="+ID+";");
			if(myRs.next()) {
				return myRs.getString("Pseudo");
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
		return "null";
	}
	
	int getIDFromPseudo(String Pseudo) throws SQLException{
		Statement myStmt = null;
		ResultSet myRs = null;
		if(Pseudo.contains(" - GROUP with:")) {
			return 0;
		}else {
			try {
				myStmt = myConn.createStatement();
				myRs = myStmt.executeQuery("select * from users where Pseudo='"+Pseudo+"';");
				if(myRs.next()) {
					return myRs.getInt("UserID");
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
		}
		return 0;
	}
	
	String getGroupName(int GroupID) throws SQLException{
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from groups where GroupID="+GroupID+";");
			if(myRs.next()) {
				return myRs.getString("GroupName");
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
		return "null";
	}
	
	boolean checkContactexists(String Pseudo) throws SQLException  {
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
	
	void addContact(String toUser) throws SQLException{
		Statement myStmt = null;
		int myRs = 0;
		if(checkContactexists(toUser)) {
			try {
				myStmt = myConn.createStatement();
				//myRs = myStmt.executeQuery("select * from users where UserID="+ID+";");
				myRs = myStmt.executeUpdate("INSERT INTO `messages` (`SenderID`, `ReceiverID`, `Content`, `DateHeure`) VALUES ("+moi.getUserID()+", "+getIDFromPseudo(toUser)+", 'Bonjour, je vous ai ajouté à mes contacts !', now());");
				
				if(myRs==1) {
					JOptionPane.showMessageDialog(null, "Great! You have added a user.");
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
		}else {
			JOptionPane.showMessageDialog(null, "Oups! This contact does not exists.");
		}
	}
	
	int getGroupIDFromGroupName(String GroupName) throws SQLException{
		Statement myStmt = null;
		ResultSet myRs = null;
		String str = GroupName;
		String substr = " - GROUP with: ";
		String before = str.substring(0, str.indexOf(substr));
		String after = str.substring(str.indexOf(substr) + substr.length());
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from groups where GroupName='"+before+"' and MembersNames='"+after+"';");
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
	
	void sendMessage(String toUser, String MessageContent) throws SQLException{
		Statement myStmt = null;
		int myRs = 0;
		MessageContent = moi.getPseudo() + ": " + MessageContent;
		if(toUser.contains("GROUP") && toUser.contains("with")) { //Check if it is a group
			try {
				myStmt = myConn.createStatement();
				myRs = myStmt.executeUpdate("INSERT INTO `messages` (`SenderID`, `ReceiverID`, `Content`, `DateHeure`, `IsGroup`) VALUES ("+moi.getUserID()+", "+0+", '"+MessageContent+"', now(), "+getGroupIDFromGroupName(toUser)+");");
				if(myRs==1) {
					//System.out.print("OK");
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
		}else {
			try {
				myStmt = myConn.createStatement();
				myRs = myStmt.executeUpdate("INSERT INTO `messages` (`SenderID`, `ReceiverID`, `Content`, `DateHeure`) VALUES ("+moi.getUserID()+", "+getIDFromPseudo(toUser)+", '"+MessageContent+"', now());");
				if(myRs==1) {
					//System.out.print("OK");
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
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == send) {
			try {
				if(selectedUser != "null" || MSG.getText().length()>0) {
					sendMessage(selectedUser, MSG.getText());
					loadMessages(getIDFromPseudo(selectedUser));
				}else {
					JOptionPane.showMessageDialog(null, "Please select a contact to the left. And you must enter a message.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getSource() == group) {
			groupWin = new GroupWindow(moi,myConn);
		}
		if(arg0.getSource() == add) {
			try {
				addContact(CONTACT.getText());
				loadContacts(moi);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
