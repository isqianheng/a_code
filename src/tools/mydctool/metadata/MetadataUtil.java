package tools.mydctool.metadata;

import java.io.File;
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

import com.sun.org.apache.bcel.internal.generic.NEW;
/**
 * 获取原结构
 * @author win7
 *
 */
public class MetadataUtil {
	
	/**
	 * 获取原结构 放到list中 <br>  
	 *  metadata  name  type  length  chinese_name  metadataid  remark                      
	 * @return  List<HashMap<String, String>>
	 */
	public List<HashMap<String, String>> readMetadata(){
		String metadata_file = null;
		//C:/qh/SVN/hebanben/HFB180201001
		metadata_file = "C:/qh/SVN/hebanben/HFB180201001/ECIS/resources/brconfig/metadata";
		File pathRoot = new File(metadata_file);
		Collection fileCollections = FileUtils.listFiles(pathRoot, FileFilterUtils.suffixFileFilter("xml"), DirectoryFileFilter.INSTANCE);
		List<HashMap<String, String>> metedataList = new ArrayList<HashMap<String,String>>();//元结构中的参数
		Iterator<File> iterator = fileCollections.iterator();
		for(;iterator.hasNext();){
			File xmlFile = iterator.next();
			SAXReader saxReader = new SAXReader();
			Document document = null;
			//解析文件
			try {
				document = saxReader.read(xmlFile);
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
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
		return metedataList;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new MetadataUtil().readMetadata());
	}

}
