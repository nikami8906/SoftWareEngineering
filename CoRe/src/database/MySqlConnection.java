package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {

	public static Connection getConnection () throws SQLException{
		Connection con = DriverManager.getConnection("jdbc:mysql://222.229.69.51:3306/CoRe", "core", "ktechkut");
		return con;
	}
}