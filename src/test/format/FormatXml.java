package test.format;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class FormatXml {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		String msg = FileUtils.readFileToString(new File(System.getProperty("user.dir")+"/src/tools/mydctool/tcpSendDemo.xml"));
		System.out.println(formatXml(msg));
	}
	public static String formatXml(String str){ 
		try {
			Document doc=new SAXReader().read(new StringReader(str));  
			OutputFormat formater=OutputFormat.createPrettyPrint();
			formater.setEncoding("UTF-8");  
			StringWriter out=new StringWriter();  
			//格式化操作
			XMLWriter writer=new XMLWriter(out,formater);  
			writer.write(doc);  
			writer.close();  
			return out.toString();  
		} catch (Exception e) {
			return str;
		}
	} 

}
