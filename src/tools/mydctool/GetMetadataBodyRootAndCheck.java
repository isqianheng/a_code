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
public class GetMetadataBodyRootAndCheck {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File xmlFile = new File("C:/qh/SVN/HFB180201001/ECIS/resources/configs/out_conf/metadata/COMMON/HIGHYIELD_ECIS/service_BusiHyDEStageOut_system_DESYSTEM.xml");
			SAXReader saxReader = new SAXReader();
			Document document = null;
			//解析文件
			document = saxReader.read(xmlFile);
			Element elementRoot = document.getRootElement();//最外层的标签就是根节点
			Element body = elementRoot.element("BODY");
			Iterator<Element> it = body.elementIterator();
			for(;it.hasNext();){
				Element e = it.next();
				String name = e.getName();
				String id = e.attributeValue("metadataid");
				String echo ="";
				if(!("DE_"+name).equals(id)){
					echo = "\t#####id与标签不一致，id为"+id;
				}
				System.out.println(name + echo);
			}
			

		} catch (DocumentException e) {
			System.out.println(e);
		}catch (Exception e) {
			System.out.println(e);
		}
	}

}
