package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

	static final String URL = "jdbc:mysql://localhost/CoRe";
	static final String USERNAME = "user";
	static final String PASSWORD = "pass";

	public static void main(String args[]) {
		Connection con = null;
		String sql = "SERECT * FROM MasTable";
		try{
			con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
			PreparedStatement sta = con.prepareStatement(sql);
			ResultSet res = sta.executeQuery();
			if (res.equals(USERNAME) && res.equals(PASSWORD)) {
				System.out.println("ログイン成功");
			}
		} catch (SQLException e) {
	            e.printStackTrace();
		}
	}
}
