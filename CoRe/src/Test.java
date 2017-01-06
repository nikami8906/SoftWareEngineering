import java.sql.SQLException;

public class Test {
	public static void main(String args[]) {
		try {
			database.MySqlQuery msq = new database.MySqlQuery();
			System.out.println(msq.getKey("Ktec"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
