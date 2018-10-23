import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String typeAndLength = " VARCHAR2(30 CHAR) NOT NULL, ";
		//"(?i)\\b\\w+\\b(\\s*\\(.*\\))*"
//		Matcher matcher = Pattern.compile("(?i)\\b\\w+\\b(\\s*\\(.*\\))*").matcher(typeAndLength);
//		if(matcher.find()){
//			typeAndLength = matcher.group();
//			System.out.println(typeAndLength);
//		}
		
		Matcher matcherType = Pattern.compile("(?i)\\b\\w+\\b").matcher(typeAndLength);
		Matcher matcherLength = Pattern.compile("\\(.*\\)").matcher(typeAndLength);
		if(matcherType.find()){
			String type = matcherType.group();
			System.out.println("type"+type);
			if(matcherLength.find()){
				String lengthNotTrim = matcherLength.group();
				Matcher m = Pattern.compile("\\d+\\b").matcher(lengthNotTrim);//匹配数字
				if (m.find()) {
					String length = m.group();
					System.out.println("length"+length);
				}
				if (m.find()) {
					String scala = m.group();
					System.out.println("scala"+scala);
				}
			}else {
				System.out.println("没找到数字");
			}
		}
		
		
		
	}
}
