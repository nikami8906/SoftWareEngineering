import java.sql.SQLException;

public class Test {
	public static void main(String args[]) {
		try {
			database.MySqlQuery sql = new database.MySqlQuery();
			System.out.println("接続に成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
