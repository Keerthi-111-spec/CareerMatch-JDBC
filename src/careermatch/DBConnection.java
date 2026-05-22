package careermatch;
import java.sql.*;
public class DBConnection {
	static final String URL="jdbc:mysql://localhost:3306/career_match";
	static final String user="YOUR_USERNAME";
	static final String password="YOUR_PASSWORD";
	public static Connection getConnection() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL,user,password);
	}
}
