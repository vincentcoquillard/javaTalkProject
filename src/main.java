import java.awt.GraphicsEnvironment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main {


	public static void main( String[] args) {
		
		Connection myConn = null; //Connect to database
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7343452", "sql7343452" , "pJljWyAJ8x");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
		}
		
		connectionWindow co = new connectionWindow(myConn); //Launch program
		
	}

}
