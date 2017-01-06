import java.sql.SQLException;

public class Test {
	public static void main(String args[]) {
		try {
			database.MySqlQuery msq = new database.MySqlQuery();
			int[] data = msq.dbGraphData(2012, 1, "火", 1);
			for (int i = 0; i < data.length; i++) {
				System.out.print(data[i] + " ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
