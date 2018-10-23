package test.javautil.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IteratorMap {

	public static void main(String[] args) {
		  Map<String, String> map = new HashMap<String, String>();
		  map.put("1", "value1");
		  map.put("2", "value2");
		  map.put("3", "value3");
		  
		  //ֵ
		  System.out.println("foreach 遍历key");
		  for (String key : map.keySet()) {
		   System.out.println("key= "+ key + " and value= " + map.get(key));
		  }
		  
		  //
		  System.out.println("ͨIterator");
		  Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		  while (it.hasNext()) {
		   Map.Entry<String, String> entry = it.next();
		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		  }
		  
		  //
		  System.out.println("ͨforeach 转为set");
		  for (Map.Entry<String, String> entry : map.entrySet()) {
		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		  }

		  //
		  System.out.println("ͨforeach 遍历value，但取不到key");
		  for (String v : map.values()) {
		   System.out.println("value= " + v);
		  }

	}

}
