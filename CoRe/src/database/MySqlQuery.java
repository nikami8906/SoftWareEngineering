package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * このクラスは端末でデータを扱うためのクラスです。
 * @author nikami
 * ver.1.0
 */
public class MySqlQuery {
	private String TABLE = "OneDayTable";
	public Connection con = null;

	/**
	 * データベースサーバに接続を行います。
	 * @throws java.sql.SQLException
	 * 例外が発生するおそれがあります
	 */
	public MySqlQuery() throws  SQLException {
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
		}
	}

	/**
	 *
	 * @throws SQLException
	 */
	private void close() throws SQLException{
		con.close();
	}

	/**
	 * データベースサーバにsql文を送信するためのメソッドです。
	 * @param sql
	 * データベースサーバに送信するsql文
	 * @return
	 * データベースサーバから返信された結果
	 * @throws SQLException
	 * このメソッドはSQLExceptionが発生する可能性があります。
	 */
	public ResultSet myExecuteQuery (String sql) throws SQLException {
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		return rs;
	}


	/**
	 * データベースサーバにsql文を送信し、更新を行うためのメソッドです。
	 * @param sql
	 * sql文
	 * @return
	 * 更新されたデータ数
	 * @throws SQLException
	 * 例外が発生する可能性があります
	 */
	public int  myExecuteUpdate(String sql) throws SQLException {
		Statement stm = con.createStatement();
		int num = stm.executeUpdate(sql);
		return num;
	}


	/**
	 * 混雑状況データを送信するメソッドです。
	 * @param areaNum エリア番号
	 * @param data 混雑状況
	 */
	public void insertNewData(int areaNum, int data ) {
		Date date = new Date();
	    SimpleDateFormat time = new SimpleDateFormat("HHmm");
		String str = Integer.toString(data);
		String sql = "INSERT INTO " + TABLE +
				" VALUES (" + time.format(date) + "," + areaNum + "," + str + ");";
		try {
			myExecuteUpdate(sql);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * 最新の混雑状況データを取得するメソッドです。<br>
	 * 指定されたエリア番号の混雑状況データを返却します。
	 * @param areaNum エリア番号
	 * @return 混雑状況データ
	 * @throws java.sql.SQLException
	 * 例外を発生させる恐れがあります。
	 */
	public int dbNewData (int areaNum ) throws SQLException {
		String sql = "select MAX(Time), AreaCode, Congestion "
				+ "from OneDayTable "
				+ "group by AreaCode;";
		ResultSet result = myExecuteQuery(sql);
		while(result.next()) {
			if(result.getInt("AreaCode") == areaNum) {
				return result.getInt("Congestion");
			}
		}
		return -1;
	}

	public int[] dbNewData () throws SQLException{
		String sql = "select MAX(Time), AreaCode, Congestion "
				+ "from OneDayTable "
				+ "group by AreaCode;";
		ResultSet result = myExecuteQuery(sql);
		int maxNum = -1;
		while(result.next()){
			int areaCode = result.getInt("AreaCode");
			if (maxNum < result.getInt("AreaCode")){
				maxNum = areaCode;
			}
		}
		int[] data = new int[maxNum + 1];
		result.beforeFirst();
		while(result.next()) {
			data[result.getInt("AreaCode")] = result.getInt("Congestion");
		}
		result.close();
		return data;
	}

	/**
	 * 過去4週間の現在時刻の混雑状況データを取得するメソッドです。
	 * @param areaNum エリア番号
	 * @return 過去の混雑状況データ
	 */
	public int dbPastData(int areaNum) throws SQLException{
		int[] data = dbPastData();
		if (data.length > areaNum + 1){
			return data[areaNum];
		}else {
			return -1;
		}
	}
	
	public int[] dbPastData() throws SQLException{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");//sql文用フォーマット
		String sql = "select * from ThiMinTable" + "\nwhere (";
		int min = (cal.get(Calendar.MINUTE)/30) * 30;//現在時刻を30分毎にするために切り捨て
		cal.set(Calendar.MINUTE, min);
		
		//4週間前までの、その時刻、その曜日の状況を取得するためのsql文の生成
		for (int i = 0; i < 3; i++) {
			cal.add(Calendar.DATE, -7);
			sql = sql + "Date = " + sdf.format(cal.getTime()) + "\nor ";
		}
		cal.add(Calendar.DATE, -7);
		sql = sql + "Date = " +sdf.format(cal.getTime()) + ");";
		ResultSet result = myExecuteQuery(sql);
		
		int maxNum = 0;
		while(result.next()){
			int areaCode = result.getInt("AreaCode");
			if (maxNum < result.getInt("AreaCode")){
				maxNum = areaCode;
			}
		}
		result.beforeFirst();
		int[] data = new int[maxNum + 1];
		while(result.next()) {
			data[result.getInt("AreaCode")] += result.getInt("ConSit"); 
		}
		for (int i = 0 ; i < data.length; i ++) {
			data[i] /= 4;
		}
		result.close();
		return data;
	}
	

	/**
	 * 混雑状況データのグラフ用データを取得するメソッドです。
	 * 指定された条件のグラフ用データを配列として返却します。
	 * @param year 年
	 * @param Quarter クォータ
	 * @param day 曜日
	 * @param areaNum エリア番号
	 * @return グラフ用配列データ
	 */
	public int[] dbGraphData(int year, int Quarter, String day, int areaNum) {
		int[] graph_data ={10,20,30,40,50,60,70,80,90,70,60,50,40,30,20,10,20,30,40,50,60,70};
		return graph_data;
	}

	public static void main (String[] args) throws Exception {
		MySqlQuery msq = new MySqlQuery();
		System.out.println(msq.dbPastData(1));
		int[] data = msq.dbPastData();
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	/**
	 * ログインIDからパスワードのハッシュ値を取得するメソッドです。
	 * @param id ログインID
	 * @return パスワードのハッシュ値
	 */
	public String getKey(String id) {
		return "sdfsdfsdfsdfsdfsdfsdfsdfsdfsd";
	}

	/**
	 * 一日の終わりに呼び出すメソッドです。
	 * 一日のデータを編集し、データベースに格納します。
	 */
	public void insertPreData() {
		return;
	}

	public String mySqlTest() throws SQLException {
		ResultSet result = myExecuteQuery("select * from test order by column1 asc");
		result.next();
		String str = result.getString("column1") + ":" + result.getString("column2") + "\n";
		result.last();
		str += "2:" + result.getString("column2") + "\n";
		str += "3:接続に成功しました。";
		String next = result.getString("column1");
		int nextNo = Integer.parseInt(next);
		nextNo++;
		myExecuteUpdate("INSERT INTO `CoRe`.`test` (`column1`, `column2`) VALUES ('"
				+ nextNo + "', '"
				+ nextNo + "回目のテストです。" + "');");
		return str;
	}


}
