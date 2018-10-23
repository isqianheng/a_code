import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class TxtParse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePathString = "C:\\Users\\win7\\Desktop\\20180118统一认证GTP数据文件\\20180118统一认证GTP数据文件\\nuns_user_all.txt";
		File path = new File(filePathString);
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(path);
			while(it.hasNext()){
				Object oneLine = it.next();
				if(null == oneLine){
					continue;
				}
				String[] sArray = ((String)oneLine).split("~@~");
				if(sArray.length < 43){
					System.out.println("缺少字段，长度为："+sArray.length);
					continue;
				}
				String USERID=sArray[0];//账号
				String USERNAME=sArray[1];//姓名
				String MANAGERFLAG=sArray[2];//是否管理层
				String ORGANIZATIONID=sArray[3];//所属部门ID
				String ORGANIZATIONNAME=sArray[4];//所属部门名称
				String USERCODE=sArray[5];//员工编号
				String CARTTYPE=sArray[6];//证件类型
				String CARTNAME=sArray[7];//证件类型名称
				String CARTCODE=sArray[8];//证件号码
				String MOBILE=sArray[9];//移动电话
				String TELEPHONE=sArray[10];//办公电话
				String STATUS=sArray[11];//人员状态(统一认证）
				String MANAGERCODE=sArray[12];//上级领导-部门经理
				String SUBUNITTYPE=sArray[13];//员工子组描述-正式/派遣
				String SEX=sArray[14];//性别
				String BIRTHDAY=sArray[15];//出生日期
				String WORKDATE=sArray[16];//参加工作日期
				String STARTDATE=sArray[17];//入行日期
				String POSTDATE=sArray[18];//入岗日期
				String CITY=sArray[19];//所在城市
				String EMAIL=sArray[20];//邮箱
				String EDUCATION=sArray[21];//学历
				String POSTID=sArray[22];//所属岗位ID
				String USERIDENTITY=sArray[23];//人员身份
				String QUITDATE=sArray[24];//离职日期
				String ACCESSSYSTEMID=sArray[25];//可访问的系统
				String NUMUSER=sArray[26];//序号
				String ISCOREUSER=sArray[27];//是否核心员工
				String ACTIONUSER=sArray[28];//人员状态（人力人员状态）
				String DESCUSER=sArray[29];//详细描述
				String ISBACKUSER=sArray[30];//是否后备人才
				String ISSPECIALUSER=sArray[31];//是否特殊人才
				String ISVALUEUSER=sArray[32];//是否价值员工
				String POSTNAME=sArray[33];//岗位名称
				String SOURCESYSTEM=sArray[34];//用户来源
				String COREORGANID=sArray[35];//核心机构Id
				String COREORGANNAME=sArray[36];//核心机构名称
				String PARTTIMEORGAN=sArray[37];//兼职机构
				String CORECUSTID=sArray[38];//核心客户经理号
				String CORECUSTNAME=sArray[39];//核心客户经理名称
				String SCMSMANAGERID=sArray[40];//企/个贷客户经理号
				String SCMSMANAGERNAME=sArray[41];//企/个贷客户经理名称
				String LAST_UPD_DATE=sArray[42];//更新时间
				System.out.println(LAST_UPD_DATE);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
