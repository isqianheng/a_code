import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;



public class T {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		try {
			System.out.println(sf.parse("201801010101sfdfsfe"));;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		String math = "0001";
//		Boolean bool = math.matches("0*");
//		System.out.println(bool);
		
//		String s = "e9(<>";
//		char[] c = s.toCharArray();
//		byte[] b = s.getBytes("utf-8");
//		System.out.println(s.length());
//		System.out.println(c.length);
//		for(int i=0;i<c.length;i++){
//			System.out.println((int)c[i]);
//		}
//		
//		System.out.println("----------");
//		System.out.println(b.length);
//		for(int i=0;i<c.length;i++){
//			System.out.println(b[i]);
//		}

	}

}
