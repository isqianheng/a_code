package test.javautil.collection.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AllList {

	public static void main(String[] args) {
		System.out.println(listlist.indexOf(Arrays.asList("1","2")));
		
		String s = null;
		System.out.println(String.valueOf(s));

	}
	/**
	 * 初始化一个list
	 */
	private static List<List<String>> listlist = Arrays.asList(
			Arrays.asList("1","2"),
			Arrays.asList("3","4")
			);
	

}
