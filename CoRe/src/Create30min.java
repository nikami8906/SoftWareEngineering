import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Create30min {
	public static Calendar cal;
	public static Random rnd;

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		cal = Calendar.getInstance();
		rnd = new Random();
		cal.set(2011, 0, 1 , 11 , 0);
		for (int year = 2011; year <= 2016; year++) {
			try {
				File file = new File("d:\\" + year +".csv");
				FileWriter fw = new FileWriter(file);
				while(cal.get(Calendar.YEAR) == year) {
					for (int areaNum = 1; areaNum <= 6; areaNum++) {
						String str = sdf.format(cal.getTime()) + "," + areaNum + ",\'" + dayOfWeek() + "\'," + genConData();
						fw.write(str + "\r\n");
					}

					add();
				}
				fw.close();
			} catch (IOException e) {
				System.out.println("エラー");
			}
		}
	}


	//時間を30分進めます。営業時間外に進む場合は次の日に進みます。
	public static void add() {
		cal.add(Calendar.MINUTE, 30);
		if(cal.get(Calendar.HOUR_OF_DAY) == 22) {
			cal.set(Calendar.HOUR_OF_DAY, 11);
			cal.set(Calendar.MINUTE, 00);
			cal.add(Calendar.DATE, 1);
		}
	}

	//曜日を漢字で返すメソッドです。
	public static String dayOfWeek() {
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return "月";
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
			return "火";
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			return "水";
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
			return "木";
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
			return "金";
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return "土";
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return "日";
		} else {
			return "";
		}
	}

	//時間によって、違う混雑状況データを生成します。
	public static int genConData() {
		if (cal.get(Calendar.HOUR_OF_DAY) == 11) {
			return 6 + rnd.nextInt(15);
		} else if(cal.get(Calendar.HOUR_OF_DAY) == 12) {
			return 85 + rnd.nextInt(14);
		} else if (cal.get(Calendar.HOUR_OF_DAY) >=13 && cal.get(Calendar.HOUR_OF_DAY) <=15) {
			return 25 + rnd.nextInt(15);
		} else if (cal.get(Calendar.HOUR_OF_DAY) >=16 && cal.get(Calendar.HOUR_OF_DAY) <= 17) {
			return 35 + rnd.nextInt(15);
		} else if (cal.get(Calendar.HOUR_OF_DAY) == 18 ) {
			return 75 + rnd.nextInt(15);
		} else {
			return 40 + rnd.nextInt(15);
		}
	}
}
