package tools.mydctool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 创建xml测试报文的一级表情字段
 * @author qian
 *
 */
public class CreateSimpleTestMessageFromInterfaceExcel {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	/**
	 * 接口字段开始的行数
	 */
	private static int lineMis = 7;
	/**
	 * 接口字段结束的行数
	 */
	private static int lineMax = 53;
	/**
	 * 字段名的列位置   列位置从0开始，A列为0
	 */
	private static int pName = 0;
	/**
	 * 字段描述的列位置
	 */
	private static int pDes = 1;
	/**
	 * 字段类型的列位置  两种格式   带长度  string(9)   或者 不带长度 string 
	 */
	private static int pType = 2;
	/**
	 * 字段类型长度的列位置    如果长度在单独到列中就配这个。否者配  -1
	 */
	private static int pLen = 3;
	/**
	 * Sheet名称     必输
	 */
	private static String sheetName = "退货失败通知接口";
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub
		File file = new File("C:\\qh\\SVN\\svn_doc2\\work\\信用卡交易辅助系统二期\\网联项目\\04.设计开发\\04.04.接口文档\\P_Y_201801_012网联接口整理.xlsx");
		List<String> list = new ArrayList<String>();
		StringBuilder txt = new StringBuilder();
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet sheet = wb.getSheet(sheetName);
		for(int i=lineMis;i<=lineMax ;i++){
			XSSFRow row = sheet.getRow(i);
			if(null == row ){
				System.out.println("--------row==null-------");
				continue;
			}
			String name = row.getCell(pName)==null?"": getCellString(row.getCell(pName)).trim();
			String des = row.getCell(pName)==null?"":getCellString(row.getCell(pDes)).trim();
			String typeAndLength = row.getCell(pName)==null?"":getCellString(row.getCell(pType)).trim();
			
			if(list.contains(name)){//重复字段就不要了
				System.out.println("###############重复字段["+name+des+"]");
				continue;
			}else{
				list.add(name);
			}
			String value = "";
			
			String type = "";
			String length = "";
			int len = 0;
			if(null != typeAndLength && typeAndLength.indexOf("(")>-1){//有括号
				int l = typeAndLength.indexOf("(");
				int r = typeAndLength.indexOf(")");
				type = typeAndLength.substring(0,l).trim();
				if(r>l){
					String lenAndSca = typeAndLength.substring(l+1,r);
					String[] sa = lenAndSca.split(",");
					length = sa[0].trim();
					if(!"".equals(length)){
						try {
							len = Integer.parseInt(length);
						} catch (Exception e) {
						}
					}
//					if(null != sa && sa.length>1){
//						scale = sa[1].trim();
//					}
				}	
			}else{
				type = typeAndLength.trim();
				if(pLen >= 0){
					length = getCellString(row.getCell(pLen)).trim();
				}
			}
			type = typeTrance(type);
			//根据长度和类型来生成值
			if(len==0){
				//不生成值
			}else{
				value = name + des;
				if("int".equals(type)||"double".equals(type)){//数字类型
					value =String.valueOf(new Random().nextInt(10)) ;
				}else if("string".equals(type)){//字符类型
					if(value.length()>len){
						value = value.substring(0,len);
					}
				}
			}
			//<ChnlCode>AG</ChnlCode>
			String s = "<"+name+">"+value+"</"+name+">\n";
			txt.append(s);
		}
		System.out.println(txt);
	}
	public static String getCellString(XSSFCell cell){
		if(null == cell){
			return "";
		}else{
			if(cell.getCellType() == cell.CELL_TYPE_STRING){
				return cell.getStringCellValue()==null?"":cell.getStringCellValue().toString();
			}else if(cell.getCellType() == cell.CELL_TYPE_NUMERIC){
				return Double.toString(cell.getNumericCellValue());
			}else{
				return cell.getStringCellValue()==null?"":cell.getStringCellValue().toString();
			}
		}
	}
	public static String typeTrance(String type){
		if(null == type)return "";
		if(type.equalsIgnoreCase("String")){
			type = "string";
		}else if(type.equalsIgnoreCase("double")){
			type = "double";
		}else if(type.equalsIgnoreCase("Integer")){
			type = "int";
		}else if(type.equalsIgnoreCase("Array")){
			type = "array";
		}else if(type.equalsIgnoreCase("Struct")){
			type = "struct";
		}
		return type;
	}

}
