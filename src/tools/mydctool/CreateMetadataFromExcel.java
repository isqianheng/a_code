package tools.mydctool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**将决策引擎接口中的字段转换成中间业务平台的数据字典文件。输出到控制台
 * 目前有string double int array类型的映射
 * 
 * excel格式如下：
 * name	字段名	String(50)
 * @throws DocumentException 
 */
public class CreateMetadataFromExcel {

	public static List<String> metedataList = new ArrayList<String>();//元结构中的参数
	public static String prefix = "";//数据字典字段在原接口字段添加前缀
	
	public static File pathRoot = new File("C:/qh/SVN/hebanben/HFB180201001/ECIS/resources/brconfig/metadata");//元结构文件的根路径（包含多层子目录）
//	public static File file = new File("F:\\HFbank\\A\\决策引擎字段.xlsx");//需要生成数据字典的字段excel
//	public static File pathRoot = new File("F:/HF_SVN/HFB180201001/ECSS/resources/brconfig/metadata");//元结构文件的根路径（包含多层子目录）
//	public static File file = new File("F:\\HFbank\\开发\\ecss二代拨款\\查询接口字段.xlsx");
//	public static File file = new File("C:\\qh\\HFbank\\开发\\网联\\退货通知接口字段.xlsx");
//	public static File file = new File("C:\\qh\\HFbank\\开发\\网联\\退货通知接口字段.xlsx");
//	public static File file = new File("/src/tools/mydctool/创建数据字典excel模板.xlsx");
	static String f = "/src/tools/mydctool/创建数据字典excel模板.xlsx";
	public static File file = new File(System.getProperty("user.dir")+f);
	
	
	public static void main(String[] args) throws InvalidFormatException, IOException, DocumentException {
		getMetaList();
		StringBuilder txt = new StringBuilder();
//		<PAY_MSG_TYPE type="string" length="4" chinese_name="缴费MESSAGE_TYPE" metadataid="PAY_MSG_TYPE" remark=""/>
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> it = sheet.iterator();
		for(;it.hasNext();){
			XSSFRow row = (XSSFRow) it.next();
			String metadataid = prefix + getCellString(row.getCell(0)).trim();
			String chinese_name = getCellString(row.getCell(1)).trim();
			String typeAndLength = getCellString(row.getCell(2)).trim();

			String type = "";
			String length = "";
			String scale = "0";
			if(null != typeAndLength && typeAndLength.indexOf("(")>-1){//有括号
				int l = typeAndLength.indexOf("(");
				int r = typeAndLength.indexOf(")");
				type = typeAndLength.substring(0,l).trim();
				if(r>l){
					String lenAndSca = typeAndLength.substring(l+1,r);
					String[] sa = lenAndSca.split(",");
					length = sa[0].trim();
					if(null != sa && sa.length>1){
						scale = sa[1].trim();
					}
				}
				
			}else{
				type = typeAndLength.trim();
			}
			type = typeTrance(type);
			String s = "<"+metadataid+" name=\""+metadataid+"\" type=\""+type+"\" length=\""+length+"\" chinese_name=\""+chinese_name+"\" metadataid=\""+metadataid+"\" scale=\""+scale+"\"/>\n";
			if(metedataList.contains(metadataid)){
				s = "<!-- ********数据字典已存在********** -->" +s;
			}
			txt.append(s);
		}
		System.out.println(txt);
	}
	public static String getCellString(XSSFCell cell){
		if(null == cell){
			return "";
		}else{
			return cell.getStringCellValue()==null?"":cell.getStringCellValue().toString();
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
		}
		return type;
	}
	
	public static void getMetaList() throws DocumentException{
		Collection fileCollections = FileUtils.listFiles(pathRoot, FileFilterUtils.suffixFileFilter("xml"), DirectoryFileFilter.INSTANCE);
		
		Iterator<File> iterator = fileCollections.iterator();
		for(;iterator.hasNext();){
			File xmlFile = iterator.next();
			SAXReader saxReader = new SAXReader();
			Document document = null;
			//解析文件
			document = saxReader.read(xmlFile);
			Element elementRoot = document.getRootElement();//最外层的标签就是根节点
			//Element basicinfoElement2 = elementRoot.element("BASICINFO");//获取节点
		 	Iterator<Element> metedataElementIterator = elementRoot.elementIterator();
		 	
		 	for(;metedataElementIterator.hasNext();){
		 		Element e = metedataElementIterator.next();
		 		Attribute metadataid = e.attribute("metadataid");
		 		metedataList.add( String.valueOf(null == metadataid?"":metadataid.getData()));
		 	}
		}
	}

}
