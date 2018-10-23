/*
 * <p>Title: :BusiRepotSubmit.java </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: digitalchina.Ltd</p>
 * @author 
 * Created :2016-7-8 16:21:58
 * @version 1.0
 * ModifyList:
 * <Author> <Time(yyyy/mm/dd)>  <Description>
 */

package com.dcfs.branch.prod.busi.pscommon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.dc.branch.service.AbstractBusiService;
import com.dc.esb.container.core.data.IServiceDataObject;
import com.dc.esb.container.core.sclite.IBaseContext;
import com.dc.interfaces.db.IDBAction;
import com.dc.interfaces.db.IDBTransaction;
import com.dcfs.branch.prod.cache.CacheManager;
import com.dcfs.branch.prod.exception.BusiException;
import com.dcfs.branch.prod.tools.CompositeDataUtil;
import com.dc.eai.data.CompositeData;

/**
 * 生成业务统计报表，直接放到XLS_PATH目录 <br>
 * 需要统计的内容有：title业务类型 account业务笔数 valid账户数量 amount成功金额 
 * 需要做报表的业务有：紧急止付，快速冻结 ，快速查询 涉及到机构：1法院，2电信,3公安 页面下拉菜单 1 2 3
 * 如果有新的权力机关，只需要修改doExecute中的 [前端参数映射]和[查数据库]部分,并写相应sql
 */
public class BusiReportPS extends AbstractBusiService {
	private Log log = LogFactory.getLog(BusiReportPS.class);

	@Override
	public IServiceDataObject doExecute(IServiceDataObject sdo,
			IBaseContext context) {
		if (log.isInfoEnabled()) {
			log.info("Execute Busi-Service:BusiRepotSubmit");
		}

		CompositeData cd = (CompositeData) sdo;
		String xls_path = (String) CacheManager.getInstance().getKeyValue(
				"CM_SYS_PARA", "9999|XLS_PATH", "PARA_VALUE");// EXCEL导出临时目录
//		String EXPORT_EXCEL_EXAMPLE_PATH = (String) CacheManager.getInstance().getKeyValue(
//				"CM_SYS_PARA", "9999|EXPORT_EXCEL_EXAMPLE_PATH","PARA_VALUE");//业务统计报表excel模板存放位置
//		String onLinePath = System.getProperty("com.dc.branch.deploy_path") + EXPORT_EXCEL_EXAMPLE_PATH + "report_model.xlsx";
		String onLinePath =System.getProperty("com.dc.branch.deploy_path")+"/brconfig/files/common/"+"report_model.xlsx";
		//从报文取前端参数
		String BRANCH_ID = CompositeDataUtil.getSysHeaderString(cd, "BRANCH_ID");
		String startTime = CompositeDataUtil.getFieldString(cd, "STARTDATE");
		String endTime = CompositeDataUtil.getFieldString(cd, "ENDDATE");
		String ORG_NO = CompositeDataUtil.getFieldString(cd, "ORG_NO");
		if(null == ORG_NO || "".equals(ORG_NO)){
			ORG_NO = BRANCH_ID;
		}
		log.info("-------ORG_NO--------" + ORG_NO);
		String POWER_OFFICE = CompositeDataUtil.getFieldString(cd,"POWER_OFFICE");
		if(null == POWER_OFFICE || "".equals(POWER_OFFICE)){
			POWER_OFFICE = "全部";
		}else if("1".equals(POWER_OFFICE)){
			POWER_OFFICE = "法院";
		}else if("2".equals(POWER_OFFICE)){
			POWER_OFFICE = "电信";
		}else if("3".equals(POWER_OFFICE)){
			POWER_OFFICE = "公安";
		}else{
			throw new BusiException("9999","没有该权力机关");
		}
		
		/*
		 * 查数据库
		 */
		IDBTransaction trans = super.getDBTransaction();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime + "000000");
		map.put("endTime", endTime + "235959");
		map.put("BRANCH_ID", ORG_NO);
		//sql查出的数据与模板对应:SPAY, FREEZE, QUERY, RELIEVE_SPAY, DELAY_SPAY,  RELIEVE_FREEZE, DELAY_FREEZE, TRANSFER
		HashMap<String,List<HashMap<String, Object>>> infoMap = new HashMap<String, List<HashMap<String,Object>>>();//成功和不成功
		if("法院".equals(POWER_OFFICE) || "全部".equals(POWER_OFFICE)){
			IDBAction action = super.newDBAction("QryFYCK_reportPS", map);
			infoMap.put("法院", trans.selectList(action));
		}
		if("电信".equals(POWER_OFFICE) || "全部".equals(POWER_OFFICE)){
			IDBAction action = super.newDBAction("QryPS_reportPS", map);
			infoMap.put("电信", trans.selectList(action));
		}
		if("公安".equals(POWER_OFFICE) || "全部".equals(POWER_OFFICE)){
			IDBAction action = super.newDBAction("QryGA_reportPS", map);
			infoMap.put("公安", trans.selectList(action));
		}
		/*
		 * 写数据到模板
		 */
		String name = CompositeDataUtil.getFieldString(cd,"POWER_OFFICE");
		if(null == name)name="all";
		String OutPath = xls_path + startTime + "_" + endTime +"_"+name+ ".xlsx";
		File outPathFile = new File(OutPath);
		if(outPathFile.exists()){
			outPathFile.delete();
		}
		log.info("--qh--OutPath:"+OutPath);
		try {
			File file = new File(onLinePath);
			log.info("--qh--从这个地址获取模板文件:"+file);
			InputStream s = new FileInputStream(file);
			OutputStream o = new FileOutputStream(outPathFile);

			XSSFWorkbook wb = new XSSFWorkbook(s);
			XSSFSheet s_home = wb.getSheetAt(0);

			//主页填入数据
			s_home.getRow(15).getCell(1).setCellValue(startTime);
			s_home.getRow(16).getCell(1).setCellValue(endTime);
			s_home.getRow(17).getCell(1).setCellValue(ORG_NO);
			s_home.getRow(18).getCell(1).setCellValue(POWER_OFFICE);
			//向各工作表填入数据
			Iterator<String> iterator = infoMap.keySet().iterator();
			for(int n = 2; iterator.hasNext(); n++){
				String key = iterator.next();
				this.writeAPowerInfo(wb, infoMap.get(key), n, key);
			}
			//刷新公式
			this.resetFormula(wb);
			//写出文件
			wb.write(o);

		}catch(FileNotFoundException e1){
			log.error(e1,e1);
			throw new BusiException("9999","文件没有找到");
		}catch(IOException e2){
			log.error(e2,e2);
			throw new BusiException("9999","IO异常");
		}catch(Exception e){
			log.error(e,e);
			throw new BusiException("9999","未知错误");
		}
		
		CompositeDataUtil.setFieldString(cd, "STATUS", "1");// STATUS
		// 1文件已生成
		CompositeDataUtil.setFieldString(cd, "DOWNLOAD_PATH", OutPath);
		return sdo;
	}


	/**
	 * 向excel中写入一个权力机关的统计信息.
	 * @param wb
	 * @param list  
	 * @param n      excel第n行 
	 * @param powerName
	 */
	private void writeAPowerInfo(XSSFWorkbook wb, List<HashMap<String, Object>> list, int n, String powerName){
		//all_account
		wb.getSheetAt(1).getRow(n).getCell(0).setCellValue(powerName);
		wb.getSheetAt(1).getRow(n).getCell(1).setCellValue(toNum(list.get(0).get("ALLACCOUNT_SPAY")));
		wb.getSheetAt(1).getRow(n).getCell(2).setCellValue(toNum(list.get(0).get("ALLACCOUNT_FREEZE")));
		wb.getSheetAt(1).getRow(n).getCell(3).setCellValue(toNum(list.get(0).get("ALLACCOUNT_QUERY")));
		wb.getSheetAt(1).getRow(n).getCell(4).setCellValue(toNum(list.get(0).get("ALLACCOUNT_RELIEVE_SPAY")));
		wb.getSheetAt(1).getRow(n).getCell(5).setCellValue(toNum(list.get(0).get("ALLACCOUNT_DELAY_SPAY")));
		wb.getSheetAt(1).getRow(n).getCell(6).setCellValue(toNum(list.get(0).get("ALLACCOUNT_RELIEVE_FREEZE")));
		wb.getSheetAt(1).getRow(n).getCell(7).setCellValue(toNum(list.get(0).get("ALLACCOUNT_DELAY_FREEZE")));
		wb.getSheetAt(1).getRow(n).getCell(8).setCellValue(toNum(list.get(0).get("ALLACCOUNT_TRANSFER")));
		//all_valid
		wb.getSheetAt(2).getRow(n).getCell(0).setCellValue(powerName);
		wb.getSheetAt(2).getRow(n).getCell(1).setCellValue(toNum(list.get(0).get("ALLVALID_SPAY")));
		wb.getSheetAt(2).getRow(n).getCell(2).setCellValue(toNum(list.get(0).get("ALLVALID_FREEZE")));
		wb.getSheetAt(2).getRow(n).getCell(3).setCellValue(toNum(list.get(0).get("ALLVALID_QUERY")));
		wb.getSheetAt(2).getRow(n).getCell(4).setCellValue(toNum(list.get(0).get("ALLVALID_RELIEVE_SPAY")));
		wb.getSheetAt(2).getRow(n).getCell(5).setCellValue(toNum(list.get(0).get("ALLVALID_DELAY_SPAY")));
		wb.getSheetAt(2).getRow(n).getCell(6).setCellValue(toNum(list.get(0).get("ALLVALID_RELIEVE_FREEZE")));
		wb.getSheetAt(2).getRow(n).getCell(7).setCellValue(toNum(list.get(0).get("ALLVALID_DELAY_FREEZE")));
		wb.getSheetAt(2).getRow(n).getCell(8).setCellValue(toNum(list.get(0).get("ALLVALID_TRANSFER")));
		//all_amount
		wb.getSheetAt(3).getRow(n).getCell(0).setCellValue(powerName);
		wb.getSheetAt(3).getRow(n).getCell(1).setCellValue(toNum(list.get(0).get("ALLAMOUNT_SPAY")));
		wb.getSheetAt(3).getRow(n).getCell(2).setCellValue(toNum(list.get(0).get("ALLAMOUNT_FREEZE")));
		wb.getSheetAt(3).getRow(n).getCell(3).setCellValue(toNum(list.get(0).get("ALLAMOUNT_QUERY")));
		wb.getSheetAt(3).getRow(n).getCell(4).setCellValue(toNum(list.get(0).get("ALLAMOUNT_RELIEVE_SPAY")));
		wb.getSheetAt(3).getRow(n).getCell(5).setCellValue(toNum(list.get(0).get("ALLAMOUNT_DELAY_SPAY")));
		wb.getSheetAt(3).getRow(n).getCell(6).setCellValue(toNum(list.get(0).get("ALLAMOUNT_RELIEVE_FREEZE")));
		wb.getSheetAt(3).getRow(n).getCell(7).setCellValue(toNum(list.get(0).get("ALLAMOUNT_DELAY_FREEZE")));
		wb.getSheetAt(3).getRow(n).getCell(8).setCellValue(toNum(list.get(0).get("ALLAMOUNT_TRANSFER")));
		//account
		wb.getSheetAt(4).getRow(n).getCell(0).setCellValue(powerName);
		wb.getSheetAt(4).getRow(n).getCell(1).setCellValue(toNum(list.get(0).get("ACCOUNT_SPAY")));
		wb.getSheetAt(4).getRow(n).getCell(2).setCellValue(toNum(list.get(0).get("ACCOUNT_FREEZE")));
		wb.getSheetAt(4).getRow(n).getCell(3).setCellValue(toNum(list.get(0).get("ACCOUNT_QUERY")));
		wb.getSheetAt(4).getRow(n).getCell(4).setCellValue(toNum(list.get(0).get("ACCOUNT_RELIEVE_SPAY")));
		wb.getSheetAt(4).getRow(n).getCell(5).setCellValue(toNum(list.get(0).get("ACCOUNT_DELAY_SPAY")));
		wb.getSheetAt(4).getRow(n).getCell(6).setCellValue(toNum(list.get(0).get("ACCOUNT_RELIEVE_FREEZE")));
		wb.getSheetAt(4).getRow(n).getCell(7).setCellValue(toNum(list.get(0).get("ACCOUNT_DELAY_FREEZE")));
		wb.getSheetAt(4).getRow(n).getCell(8).setCellValue(toNum(list.get(0).get("ACCOUNT_TRANSFER")));
		//valid
		wb.getSheetAt(5).getRow(n).getCell(0).setCellValue(powerName);
		wb.getSheetAt(5).getRow(n).getCell(1).setCellValue(toNum(list.get(0).get("VALID_SPAY")));
		wb.getSheetAt(5).getRow(n).getCell(2).setCellValue(toNum(list.get(0).get("VALID_FREEZE")));
		wb.getSheetAt(5).getRow(n).getCell(3).setCellValue(toNum(list.get(0).get("VALID_QUERY")));
		wb.getSheetAt(5).getRow(n).getCell(4).setCellValue(toNum(list.get(0).get("VALID_RELIEVE_SPAY")));
		wb.getSheetAt(5).getRow(n).getCell(5).setCellValue(toNum(list.get(0).get("VALID_DELAY_SPAY")));
		wb.getSheetAt(5).getRow(n).getCell(6).setCellValue(toNum(list.get(0).get("VALID_RELIEVE_FREEZE")));
		wb.getSheetAt(5).getRow(n).getCell(7).setCellValue(toNum(list.get(0).get("VALID_DELAY_FREEZE")));
		wb.getSheetAt(5).getRow(n).getCell(8).setCellValue(toNum(list.get(0).get("VALID_TRANSFER")));
		//amount
		wb.getSheetAt(6).getRow(n).getCell(0).setCellValue(powerName);
		wb.getSheetAt(6).getRow(n).getCell(1).setCellValue(toNum(list.get(0).get("AMOUNT_SPAY")));
		wb.getSheetAt(6).getRow(n).getCell(2).setCellValue(toNum(list.get(0).get("AMOUNT_FREEZE")));
		wb.getSheetAt(6).getRow(n).getCell(3).setCellValue(toNum(list.get(0).get("AMOUNT_QUERY")));
		wb.getSheetAt(6).getRow(n).getCell(4).setCellValue(toNum(list.get(0).get("AMOUNT_RELIEVE_SPAY")));
		wb.getSheetAt(6).getRow(n).getCell(5).setCellValue(toNum(list.get(0).get("AMOUNT_DELAY_SPAY")));
		wb.getSheetAt(6).getRow(n).getCell(6).setCellValue(toNum(list.get(0).get("AMOUNT_RELIEVE_FREEZE")));
		wb.getSheetAt(6).getRow(n).getCell(7).setCellValue(toNum(list.get(0).get("AMOUNT_DELAY_FREEZE")));
		wb.getSheetAt(6).getRow(n).getCell(8).setCellValue(toNum(list.get(0).get("AMOUNT_TRANSFER")));
		
		log.info("--qh--"+powerName+"  数据写入完成");
	}


	/**
	 * 重新填写公式，如果不这样做，excel打开时不能自动计算（需要手动双击）
	 */
	private void resetFormula(XSSFWorkbook wb){
		XSSFSheet home = wb.getSheetAt(0);//主页
		XSSFSheet all_account = wb.getSheetAt(1);//全辖业务笔数
		XSSFSheet all_valid = wb.getSheetAt(2);//全辖账户数量
		XSSFSheet all_amount = wb.getSheetAt(3);//全辖成功金额
		XSSFSheet account = wb.getSheetAt(4);
		XSSFSheet valid = wb.getSheetAt(5);
		XSSFSheet amount = wb.getSheetAt(6);
		
		resetAFormula(home,2,1);
		resetAFormula(home,2,2);
		resetAFormula(home,2,3);
		resetAFormula(home,2,4);
		resetAFormula(home,2,5);
		resetAFormula(home,2,6);
		
		resetAFormula(home,3,1);
		resetAFormula(home,3,2);
		resetAFormula(home,3,3);
		resetAFormula(home,3,4);
		resetAFormula(home,3,5);
		resetAFormula(home,3,6);
		
		resetAFormula(home,4,1);
		resetAFormula(home,4,2);
		resetAFormula(home,4,4);
		resetAFormula(home,4,5);
		resetAFormula(home,4,6);
		
		resetAFormula(home,5,1);
		resetAFormula(home,5,2);
		resetAFormula(home,5,3);
		resetAFormula(home,5,4);
		resetAFormula(home,5,5);
		resetAFormula(home,5,6);
		
		resetAFormula(home,7,1);
		resetAFormula(home,7,2);
		resetAFormula(home,7,3);
		resetAFormula(home,7,4);
		resetAFormula(home,7,5);
		resetAFormula(home,7,6);
		
		resetAFormula(home,8,1);
		resetAFormula(home,8,2);
		resetAFormula(home,8,3);
		resetAFormula(home,8,4);
		resetAFormula(home,8,5);
		resetAFormula(home,8,6);
		
		resetAFormula(home,9,1);
		resetAFormula(home,9,2);
		resetAFormula(home,9,3);
		resetAFormula(home,9,4);
		resetAFormula(home,9,5);
		resetAFormula(home,9,6);
		
		resetAFormula(home,10,1);
		resetAFormula(home,10,2);
		resetAFormula(home,10,3);
		resetAFormula(home,10,4);
		resetAFormula(home,10,5);
		resetAFormula(home,10,6);
		
		resetAFormula(home,11,1);
		resetAFormula(home,11,2);
		resetAFormula(home,11,3);
		resetAFormula(home,11,4);
		resetAFormula(home,11,5);
		resetAFormula(home,11,6);

		resetAFormula(home,13,1);
		resetAFormula(home,13,2);
		resetAFormula(home,13,3);
		resetAFormula(home,13,4);
		resetAFormula(home,13,5);
		resetAFormula(home,13,6);
		
		resetAFormula(all_account,1,1);
		resetAFormula(all_account,1,2);
		resetAFormula(all_account,1,3);
		resetAFormula(all_account,1,4);
		resetAFormula(all_account,1,5);
		resetAFormula(all_account,1,6);
		resetAFormula(all_account,1,7);
		resetAFormula(all_account,1,8);
		
		resetAFormula(all_valid,1,1);
		resetAFormula(all_valid,1,2);
		resetAFormula(all_valid,1,3);
		resetAFormula(all_valid,1,4);
		resetAFormula(all_valid,1,5);
		resetAFormula(all_valid,1,6);
		resetAFormula(all_valid,1,7);
		resetAFormula(all_valid,1,8);
		
		resetAFormula(all_amount,1,1);
		resetAFormula(all_amount,1,2);
		resetAFormula(all_amount,1,3);
		resetAFormula(all_amount,1,4);
		resetAFormula(all_amount,1,5);
		resetAFormula(all_amount,1,6);
		resetAFormula(all_amount,1,7);
		resetAFormula(all_amount,1,8);
		
		resetAFormula(account,1,1);
		resetAFormula(account,1,2);
		resetAFormula(account,1,3);
		resetAFormula(account,1,4);
		resetAFormula(account,1,5);
		resetAFormula(account,1,6);
		resetAFormula(account,1,7);
		resetAFormula(account,1,8);
		
		resetAFormula(valid,1,1);
		resetAFormula(valid,1,2);
		resetAFormula(valid,1,3);
		resetAFormula(valid,1,4);
		resetAFormula(valid,1,5);
		resetAFormula(valid,1,6);
		resetAFormula(valid,1,7);
		resetAFormula(valid,1,8);
		
		resetAFormula(amount,1,1);
		resetAFormula(amount,1,2);
		resetAFormula(amount,1,3);
		resetAFormula(amount,1,4);
		resetAFormula(amount,1,5);
		resetAFormula(amount,1,6);
		resetAFormula(amount,1,7);
		resetAFormula(amount,1,8);
	}
	
	/**
	 * 刷新一个公式
	 * @param sheet 
	 * @param row 0开始
	 * @param cell 0开始
	 */
	private void resetAFormula(XSSFSheet sheet, int row, int cell){
		sheet.getRow(row).getCell(cell).setCellFormula(sheet.getRow(row).getCell(cell).getCellFormula());
	}

	/**
	 * object null 返回 0
	 * @param obj
	 * @return
	 */
	private double toNum(Object obj){
		if(null == obj)return 0;
		else return Double.valueOf(obj.toString());
	}
}
