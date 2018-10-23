package test.format;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;

public class StringTo64 {


	private static String hexString = "0123456789ABCDEF";
	public static void main(String[] args) {
		String fileString = null ;
		
		fileString = System.getProperty("user.dir")+ "/resource/tempfile/1.xml";
//		fileString =  "C:/Users/win7/Documents/O/性能测试/分期产品请求.txt";
		
		String s = "";
		try {
			 s = FileUtils.readFileToString(new File(fileString));
		} catch (IOException e) {
			e.printStackTrace();
		}
		s = "34303839BDBBD2D7B3C9B9A62020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202034303839303030303030363331314147303031393930303031393930303030323130363235313931363030363036383331362020203135303031584A465135303031303031322020202020202020303132303030303030313030303030303030303030313135323030313030303030303030303030303936303030303030303030393630303030303030303030303030303030303030302020202020202020";
		
		System.out.println(s);
//	    System.out.println(encode(s));
	    System.out.println(decode(s));
	}
	
	
	
	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
	    // 根据默认编码获取字节数组
	    byte[] bytes = str.getBytes();
	    System.out.println("----------bytes.length----------"+bytes.length);
	    StringBuilder sb = new StringBuilder(bytes.length * 2);
	    // 将字节数组中每个字节拆解成2位16进制整数
	    for (int i = 0; i < bytes.length; i++) {
	    	sb.append("\\x");
		    sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
		    sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
	    }
	    return sb.toString();
	}
	 
	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
	    // 将每2位16进制整数组装成一个字节
	    for (int i = 0; i < bytes.length(); i += 2)
	    baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
	                    .indexOf(bytes.charAt(i + 1))));
	    String s = null;
		try {
			s = new String(baos.toByteArray(),"gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return s;
	}


}
