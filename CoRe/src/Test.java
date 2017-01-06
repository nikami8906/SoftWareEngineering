import java.sql.SQLException;

public class Test {
	public static void main(String args[]) {
		try {
			database.MySqlQuery msq = new database.MySqlQuery();
			System.out.println(msq.mySqlTest());
			msq.dbGraphData(2012, 1, "火", 7);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
