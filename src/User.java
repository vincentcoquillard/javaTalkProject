
public class User {
	
	private String pseudo;
	private int userID;
	private String password;
	
	public User(String pseudo, int userID, String password) {
		this.pseudo = pseudo;
		this.userID = userID;
		this.password = password;	
	}
	
	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
