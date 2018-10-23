package tools.mydctool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseMetadataAndCreateExcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//InputStream inputStream = new ByteArrayInputStream(msgByteArray);
		try {
			File pathRoot = new File("F:/HF_SVN/HFB180126004/ECIS/resources/brconfig/metadata");
			Collection fileCollections = FileUtils.listFiles(pathRoot, FileFilterUtils.suffixFileFilter("xml"), DirectoryFileFilter.INSTANCE);
			List<HashMap<String, String>> metedataList = new ArrayList<HashMap<String,String>>();//元结构中的参数
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
			 		HashMap<String, String> map = new HashMap<String, String>();
			 		Element e = metedataElementIterator.next();
			 		Attribute name = e.attribute("name");
			 		Attribute type = e.attribute("type");
			 		Attribute length = e.attribute("length");
			 		Attribute chinese_name = e.attribute("chinese_name");
			 		Attribute metadataid = e.attribute("metadataid");
			 		Attribute remark = e.attribute("remark");
			 		map.put("metadata", e.getName());
			 		map.put("name", String.valueOf(null == name?"":name.getData()));
			 		map.put("type", String.valueOf(null == type?"":type.getData()));
			 		map.put("length", String.valueOf(null == length?"":length.getData()));
			 		map.put("chinese_name", String.valueOf(null == chinese_name?"":chinese_name.getData()));
			 		map.put("metadataid", String.valueOf(null == metadataid?"":metadataid.getData()));
			 		map.put("remark", String.valueOf(null == remark?"":remark.getData()));
			 		metedataList.add(map);
			 	}
			}
			//xml循环解析结束  开始写excel
			
			XSSFWorkbook wb = new XSSFWorkbook();//创建空的。也能读取一个模板
			XSSFSheet x = wb.createSheet("A"); // wb.getSheetAt(0);
			for(int i =0 ; i< metedataList.size();i++){
				XSSFRow row = x.createRow(i);//必须先创建一个行，再在这个行下创建单元格。不能每一个单元格都再创建一个行
				row.createCell(0).setCellValue(metedataList.get(i).get("metadata"));
				row.createCell(1).setCellValue(metedataList.get(i).get("length"));
				row.createCell(2).setCellValue(metedataList.get(i).get("chinese_name"));
				row.createCell(3).setCellValue(metedataList.get(i).get("remark"));
			}
			//x.getRow(0).getCell(0).setCellValue("1");
			wb.write(new FileOutputStream("F:/HFbank/A/1.xlsx"));
			
			System.out.println(metedataList);
		} catch (DocumentException e) {
			System.out.println(e);
		}catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}

}
