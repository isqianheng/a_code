import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;



public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		HashMap<String, String> cd = new HashMap<String, String>();
		String ORDER_DETAIL = "2|2|商户名称 商户名称 1%123456789012345%01%11%123456789012345%5045%2#2#商品 1^CNY1000.00^1#商品 2^CNY999.99^ 99999|商户名称 商户名称 2%223456789012345%04%01%410109197001019871%5046%1#1#商品 3^CNY1.00^1|";
		nc_split_ORDER_DETAIL(cd, ORDER_DETAIL);
		System.out.println(cd);

	}
	
	private static void nc_split_ORDER_DETAIL(HashMap<String, String> cd, String ORDER_DETAIL){
		System.out.println("--qhdebug--网联拆分订单详情");
		if(null != ORDER_DETAIL && !"".equals(ORDER_DETAIL)){
			try {
				String[] sa1 = ORDER_DETAIL.split("\\|");// 商户列表数|商户实际数|商户信息1|商户信息2|商户信息3|商户信息4|
				setMap(cd, "MERCH_COUNT", sa1[1]);//商户实际数
				//2级数组：商户信息      商户简称%编码%类型%证件类型%证件编码%行业类别%商品信息(数组)
				int len = sa1.length;
				if(len>=3){//商户信息1
					String[] sInfo = sa1[2].split("%");
					int len2 = sInfo.length;
					if(len2>0)setMap(cd, "MERCH_SHORT_NAME1", sInfo[0]);//简称
					if(len2>1)setMap(cd, "MERCH_CODE1", sInfo[1]);//编码
					if(len2>2)setMap(cd, "MERCH_TYPE1", sInfo[2]);//类型
					if(len2>5)setMap(cd, "MERCH_BUSI_TYPE1", sInfo[5]);//行业类别
					if(len2>6)setMap(cd, "MERCH_GOODS_INFO1", sInfo[6]);//商品信息（数组不拆）
//					if(len2>6){//商品信息(数组)
//						String[] goodsInfo = sInfo[6].split("#");
//						int len3 = goodsInfo.length;
//						if(len3>1)setMap(cd, "", goodsInfo[1]);//商品实际数
//						if(len3>2)setMap(cd, "", goodsInfo[2]);//商品单元信息（数组）不拆
//					}
				}
				if(len>=4){//商户信息2
					String[] sInfo = sa1[3].split("%");
					int len2 = sInfo.length;
					if(len2>0)setMap(cd, "MERCH_SHORT_NAME2", sInfo[0]);//简称
					if(len2>1)setMap(cd, "MERCH_CODE2", sInfo[1]);//编码
					if(len2>2)setMap(cd, "MERCH_TYPE2", sInfo[2]);//类型
					if(len2>5)setMap(cd, "MERCH_BUSI_TYPE2", sInfo[5]);//行业类别
					if(len2>6)setMap(cd, "MERCH_GOODS_INFO2", sInfo[6]);//商品信息（数组不拆）
				}
				if(len>=5){//商户信息3
					String[] sInfo = sa1[4].split("%");
					int len2 = sInfo.length;
					if(len2>0)setMap(cd, "MERCH_SHORT_NAME3", sInfo[0]);//简称
					if(len2>1)setMap(cd, "MERCH_CODE3", sInfo[1]);//编码
					if(len2>2)setMap(cd, "MERCH_TYPE3", sInfo[2]);//类型
					if(len2>5)setMap(cd, "MERCH_BUSI_TYPE3", sInfo[5]);//行业类别
					if(len2>6)setMap(cd, "MERCH_GOODS_INFO3", sInfo[6]);//商品信息（数组不拆）
				}
				if(len>=6){//商户信息4
					String[] sInfo = sa1[5].split("%");
					int len2 = sInfo.length;
					if(len2>0)setMap(cd, "MERCH_SHORT_NAME4", sInfo[0]);//简称
					if(len2>1)setMap(cd, "MERCH_CODE4", sInfo[1]);//编码
					if(len2>2)setMap(cd, "MERCH_TYPE4", sInfo[2]);//类型
					if(len2>5)setMap(cd, "MERCH_BUSI_TYPE4", sInfo[5]);//行业类别
					if(len2>6)setMap(cd, "MERCH_GOODS_INFO4", sInfo[6]);//商品信息（数组不拆）
				}
			} catch (Exception e) {
				System.out.println("网联拆分订单详情异常");
				System.out.println();
			}
		}
	}
	
	static void setMap(HashMap<String, String> cd,String key,String v){
		cd.put(key, v);
	}

}
