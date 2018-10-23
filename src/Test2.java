import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;

import tools.mydctool.metadata.Table2Metadata;



public class Test2 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String txtPathString = null;
		txtPathString = 	"C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/HY_STAGE_APPROVE4075.tab";
		
		txtPathString = "C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/CRETABLE/HY_PROD.tab";
		txtPathString = "C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/CRETABLE/HY_PROD_HIST.sql";
		txtPathString = "C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/CRETABLE/HY_PROD_CARD_INFO.tab";
//		txtPathString = "C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/CRETABLE/HY_PROD_CARD_INFO_HIST.sql";
		txtPathString = "C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/CRETABLE/HY_PROD_CATE_INFO.tab";
//		txtPathString = "C:/qh/SVN/hebanben/HFB180201001/ECI_DB/DB/ECI_DB/Patch/HFB180201001DB/EXCUTE/CRETABLE/HY_PROD_CATE_INFO_HIST.sql";
		String txt = "";
		try {
			
			txt = FileUtils.readFileToString(new File(txtPathString));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HashMap<String, HashMap<String, String>> map = null;
		map = new Table2Metadata().table2MetadataByTxt(txt);
		String tableName = map.get("000000").get("tableName");
		
		System.out.println(map);
		
		//INSERT INTO HY_PROD_HIST(HY_SEQ_NO,CP_NO,PROD_ID) VALUES ('11','11','11')
		
		
		
		System.out.println(new Random().nextInt(555));
		
		StringBuffer columns = new StringBuffer();
		StringBuffer values = new StringBuffer();
		
		for (String column :map.keySet()) {
			if ("000000".equals(column)) {
				continue;
			}
			columns.append(column).append(",");
			
			String value = column  +  map.get(column).get("comment");
			System.out.println(value);
			int l = Integer.parseInt(map.get(column).get("length"));
			value = value.length()<l? value:value.substring(0,l);
			values.append("'").append(value).append("',");
		}
		String c = columns.substring(0,columns.length()-1);
		String v = values.substring(0,values.length()-1);
		String sql = "INSERT INTO "+tableName +" (" + c + ") VALUES (" +v + ")";
		System.out.println(sql);

		
	}
	


}
