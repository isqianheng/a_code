import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;


public class Test3 {
	public static void main(String[] args){
		
		//list中存放map，只是存放了对map的引用
		HashMap<String, String> map = new HashMap<String, String>(){{
			put("1", "1");
			put("2", "2");
			put("3", "3");}};
		ArrayList<HashMap<String, String>> a = new ArrayList<HashMap<String,String>>();
		a.add(map);
		a.add(map);
		System.out.println(a);
		a.get(0).put("1", "qq");
		System.out.println(a);
		
		//需要
		ArrayList<HashMap<String, String>> b = new ArrayList<HashMap<String,String>>();
		b.add(map);
		b.add(new HashMap<String, String>(){{
			put("a", "a");
			put("b", "b");
			put("c", "c");}} );
		System.out.println(b);
		b.get(0).put("1", "qq");
		System.out.println(b);
		
		
		String s = ".8";
		System.out.println(s.startsWith("."));
		
	}

}
