package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlQuery {
	private Connection con = null;
	public MySqlQuery() throws SQLException {
		try {
			con = MySqlConnection.getConnection();
		} catch (SQLException e) {
			System.out.println("データベース接続に失敗しました");
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ec) {
					System.out.println("データベースのクローズに失敗しました。");
				}
			}
			throw new SQLException();
		}
	}

	public void close() throws SQLException{
		con.close();
	}

	//sql文を入力することで，データの問い合わせを行う．
	public ResultSet myExecuteQuery (String sql) throws SQLException {
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		return rs;
	}


	//sql文を入力することで，レコードの挿入・更新を行う．
	public int  myExecuteUpdate(String sql) throws SQLException {
		Statement stm = con.createStatement();
		int num = stm.executeUpdate(sql);
		return num;
	}


	//カメラコードと込み具合を指定したテーブルに挿入する
	public void insertNewData(int areaNum, int data , String table)
	throws SQLException {
		//String str1 = Integer.toString(CameraNum);
		//String str2 = Integer.toString(data);
		String sql = "INSERT INTO " + table +
				" VALUES (" + areaNum + "," + data + ");";
		myExecuteUpdate(sql);
	}

	public static void main (String[] args) throws Exception {
		MySqlQuery msq = new MySqlQuery();//データベース操作クラスをインスタンス化
		//msq.insertNewData(8, 100, "sample1");
		SqlSelectTest.printAllTable(msq.con, "sample1");
	}

	protected void finalize() {
		try {
			con.close();
			System.out.println("終了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
