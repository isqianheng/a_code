package tools.mydctool.metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
/**
 * 根据建表语句生成原结构map
 * @author win7
 *
 */
public class Table2Metadata {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 根据建表语句生成原结构map <br>
	 * map属性有：column  type  length   scala  comment <br>
	 * 特殊： HashMap<"000000", {tableName="表名";tableNameChinese="表名注释"}>
	 * @param tableTxtPath
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, HashMap<String, String>> table2MetadataByPath(String tableTxtPath) throws Exception{
		String txt = "";
		try {
			txt = FileUtils.readFileToString(new File(tableTxtPath));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return table2MetadataByTxt(txt);
	}
	
	/**
	 * 根据建表语句生成原结构map <br>
	 * map属性有：column  type  length   scala  comment <br>
	 * 特殊： HashMap<"000000", {tableName="表名";tableNameChinese="表名注释"}>
	 * @param tableTxt
	 * @return HashMap<String, HashMap<String, String>>
	 * @throws Exception
	 */
	public HashMap<String, HashMap<String, String>> table2MetadataByTxt(String tableTxt) throws Exception{
		String tableName = null;
		String tableNameChinese = null;
		String fieldArea = null;
		int count = 0; //总字段数
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();//存放字段和属性
		//map属性有：column  type  length   scala  comment
		
		//表名
		Pattern patternTableName = Pattern.compile("CREATE\\s+TABLE\\s+\\w+\\.(.*)\\s*",Pattern.CASE_INSENSITIVE);
		Matcher matcherTableName = patternTableName.matcher(tableTxt);
		if(matcherTableName.find()){
			tableName = matcherTableName.group(1);
			System.out.println("表名:"+tableName);
		}else{
			throw new Exception("没找到表名");
		}
		
		
		//表中文名
		Pattern patternTableChinese = Pattern.compile("CREATE\\s+TABLE\\s+\\w+\\.(.*)\\s*",Pattern.CASE_INSENSITIVE);
		Matcher matcherTableChinese = patternTableChinese.matcher(tableTxt);
		if(matcherTableChinese.find()){
			tableNameChinese = matcherTableChinese.group(1);
			System.out.println("表名:"+tableNameChinese);
		}else{
			System.out.println("没找到表名注释");
		}
		HashMap<String, String> m1 = new HashMap<String, String>();
		m1.put("tableName", tableName);
		m1.put("tableNameChinese", tableNameChinese);
		map.put("000000", m1);
		
		
		//表字段区域
		Pattern patternFieldArea = Pattern.compile("(?is)CREATE\\s+TABLE\\s+.+?(.*?);");
		Matcher matcherFieldArea = patternFieldArea.matcher(tableTxt);
		if(matcherFieldArea.find()){
			fieldArea = matcherFieldArea.group(1);
			System.out.println("fieldArea:"+fieldArea);
		}else{
			throw new Exception("没找到字段区域");
		}
		
		System.out.println("--------------开始------------------");
		//取表字段
		Pattern patternNext = Pattern.compile(".*");
		Matcher matcherNext = patternNext.matcher(fieldArea);
		
		Pattern patternField = Pattern.compile("\\s*(\\w+)\\s");//一行的pattern
		Matcher matcherField =null;
		while (matcherNext.find()) {
			//当前行
			String line = matcherNext.group();
			if(line.matches("(?i).*\\bTABLESPACE\\b.*")){
				continue;
			}
			System.out.println("---line----"+line);
			//一个字段
			matcherField = patternField.matcher(line);
			if(matcherField.find()){
				HashMap<String, String> p = new HashMap<String, String>();
				//字段
				String column = matcherField.group(1);
				System.out.println(column+"---"+count);
				
				int i = matcherField.end();
				String halfLine = line.substring(i);
				
				//类型和长度的
				String[] sa = typeFormat(halfLine);
				if(null != sa){
					p.put("type", sa[0]);
					p.put("length", sa[1]);
					p.put("scala", sa[2]);
				}
				
				if(map.containsKey(column)){
					System.out.println("#############重复的字段 #####################"+map.get(column)+"-->"+p);
				}
				map.put(column, p);
				count++;
			}else{
				System.out.println("当前行没找到字段");
			}
			
		}
		
		
		
		
		
		//字段注释
		Pattern patternCommentLine = Pattern.compile("(?i)COMMENT\\sON\\sCOLUMN(.*)");
		Matcher matcherCommentLine = patternCommentLine.matcher(tableTxt);
		Pattern pColumn = Pattern.compile("(?i)\\.(\\w+)\\s+is");//字段  获取1
		Pattern pComment = Pattern.compile("'(.*)'");//注释  获取1
		while(matcherCommentLine.find()){
			String commentLine = matcherCommentLine.group(1);
			System.out.println("patternComment:"+commentLine);
			Matcher mColumn = pColumn.matcher(commentLine);//匹配字段
			Matcher mComment = pComment.matcher(commentLine);//匹配注释
			if(mColumn.find() && mComment.find()){
				String column = mColumn.group(1);
				String comment = mComment.group(1);
				if (map.containsKey(column)) {
					map.get(column).put("comment", comment.trim());
				}else {
					System.out.println("----异常--字段中没有包含注释");
				}
			}else {
				System.out.println("注释行未匹配到字段或注释内容");
			}
		}
		
		
		System.out.println("------------结果-------------"+map.size());
		System.out.println(map.toString());
		return map;
	}
	
	/**
	 * 类型转换
	 * @param type
	 * @return
	 */
	public static String typeTrance(String type){
		if(null == type)return "";
		if(type.equalsIgnoreCase("String")){
			type = "string";
		}else if(type.equalsIgnoreCase("double")){
			type = "double";
		}else if(type.equalsIgnoreCase("Integer")){
			type = "int";
		}else if(type.equalsIgnoreCase("Int")){
			type = "int";
		}else if(type.equalsIgnoreCase("Array")){
			type = "array";
		}
		return type;
	}
	/**
	 * 类型和长度String格式化 <br>
	 * VARCHAR2(30 CHAR) NOT NULL, <br>
	 * VARCHAR2(30 CHAR)
	 * INTEGER,
	 * CLOB,
	 * @param typeAndL
	 * @return String [] sa = {"类型","长度","精度"};   可以为null
	 */
	public static String[] typeFormat(String typeAndLength){
		//类型    长度    精度    
		String [] sa = {"","",""};
		Matcher matcherType = Pattern.compile("(?i)\\b\\w+\\b").matcher(typeAndLength);//匹配类型
		Matcher matcherLength = Pattern.compile("\\(.*\\)").matcher(typeAndLength);
		if(matcherType.find()){//类型
			String type = matcherType.group();
			sa[0] = typeTrance(type);
			if(matcherLength.find()){
				String lengthNotTrim = matcherLength.group();
				Matcher m = Pattern.compile("\\d+\\b").matcher(lengthNotTrim);//匹配数字
				if (m.find()) {//长度
					String length = m.group();
					sa[1] = length;
				}
				if (m.find()) {//精度
					String scala = m.group();
					sa[2] = scala;
				}
			}else {
				System.out.println("没找到数字---"+typeAndLength);
			}
			return sa;
		}else{
			System.out.println("没找到类型-----"+typeAndLength);
			return null;
		}
	}
}
