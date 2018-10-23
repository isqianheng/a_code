package test.javautil.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestCalender {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Date date = new Date();//获取当前时间    
		 Calendar calendar = Calendar.getInstance();    
		 calendar.setTime(date);    
		 calendar.add(Calendar.YEAR, -1);//当前时间减去一年，即一年前的时间    
		 calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间    
		 calendar.getTime();//获取一年前的时间，或者一个月前的时间 
		 System.out.println(calendar.getTime());
		 
		 
		 Calendar startCalendar = Calendar.getInstance();
		 Calendar endCalendar = Calendar.getInstance();
		 
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date start = dateFormat.parse("20170808");
			Date end = dateFormat.parse("20171012");
			
			startCalendar.setTime(start);
			startCalendar.add(Calendar.DATE, 90);
			Date start90 = startCalendar.getTime();
			System.out.println(start90.after(end));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
