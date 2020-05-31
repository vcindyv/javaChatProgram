import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnection {
	public Connection makeConnection() {
		
		String url="jdbc:mysql://davichiar1.cafe24.com/davichiar1";//Web database url.
		Connection con=null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");//Get the jdbc driver.	
			System.out.println("connecting database...");
			con=DriverManager.getConnection(url,"davichiar1","a1b1c1**");//Store the passageway to the database.
			System.out.println("database connection success!");}
		catch(ClassNotFoundException e) {System.out.println("JDBC not found!");}
		catch(SQLException e) {System.out.println("failed to connect database!");}
		return con;
	}
}
