package test.javautil.date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 到数据库的时间转换
 * @author qian
 *
 */
public class TestDate {
	public static void main(String args[]){
		//util中获取当前时间a,格式为 Tue Mar 01 14:34:15 CST 2016
		 java.util.Date a = new java.util.Date();
		 System.out.println(a);
		 
		 
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		 //转换后 的格式为k,2016-03-01 14:37:12
         SimpleDateFormat sd= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String k=sd.format(a);//转换,将Date类型的数据转换成字符串
         try {
			java.util.Date date=sd.parse("1999-01-01 01:01:01");//解析,将字符串解析成util中的Date类型
		} catch (ParseException e) {
			e.printStackTrace();
		}
         System.out.println(k);
         
         //sql包中获取的初始时间q, 1970-01-01 08:00:00.0
         java.sql.Timestamp q=new Timestamp(0);
         System.out.println("Timestamp:"+q);
         
         //sql中的Date类型只存储日期b,2016-03-01
         java.sql.Date b = new java.sql.Date(a.getTime());
         System.out.println(b);
         
         //sql中的Time类只存储时间c,14:43:18
         java.sql.Time c = new java.sql.Time(a.getTime());
         System.out.println(c);
         
         //sql中的Timestamp存储日期+时间d,2016-03-01 14:43:18.231
         java.sql.Timestamp d=new java.sql.Timestamp(a.getTime());
         System.out.println(d);
         
       //toString:2016-03-01 14:43:18.231
         System.out.println(d.toString());
	}
}
/*
 * java向ORACLE插入时间 

public class Test{
        public static void main (String args []){              
                java.util.Date a = new java.util.Date();
                System.out.println(a);
                java.sql.Date b = new java.sql.Date(a.getTime());
                System.out.println(b);
                java.sql.Time c = new java.sql.Time(a.getTime());
                System.out.println(c);
                java.sql.Timestamp d=new java.sql.Timestamp(a.getTime());
                System.out.println(d);
        }
}
 
Mon Apr 03 18:00:34 CST 2006
2006-04-03
18:00:34
2006-04-03 18:00:34.388
 
1.         oracle默认的系统时间就是sysdate函数，储存的数据形如25-3-200510:55:33
2.         java 中取时间的对象是java.util.Date。
3.         oracle中对应的时间对象是java.util.Date，java.sql.Time，java.sql.Timestamp、它们都是是java.util.Date的子类。
4.         oracle中与date操作关系最大的就是两个转换函数：to_date(),to_char()。to_date()一般用于写入日期到数据库时用到的函数。to_char()一般用于从数据库读入日
期时用到的函数。
 
DATE、TIME 和 TIMESTAMP：
SQL 定义了三种与时间有关的数据类型：DATE 由日、月和年组成。TIME 由小时、分钟和秒组成。 TIMESTAMP 将 DATE 和 TIME 结合起来，并添加了纳秒域。
标准 Java 类 java.util.Date 可提供日期和时间信息。但由于该类包含 DATE 和 TIME 信息而没有 TIMESTAMP 所需的纳秒，因此并不与上述三种 SQL 类型完全相配。
因此我们定义了 java.util.Date 的三种子类。它们是：
1.       有关 SQL DATE 信息的 java.sql.Date
2.       有关 SQL TIME 信息的 java.sql.Time
3.       有关 SQL TIMESTAMP 信息的 java.sql.Timestamp
对于 java.sql.Time，java.util.Time 基本类的小时、分钟、秒和毫秒域被设置为零。 对于 java.sql.Date，java.util.Date 基本类的年、月和日域被分别设置为 1970 年 1
月 1 日。这是在 Java 新纪元中的“零”日期。java.sql.date中的日期可以和标准的SQL语句中含有日期的字段进行比较.java.sql.Timestamp 类通过添加纳秒域来扩展
java.util.Date。
 
oracle中两个转换函数：
1.       to_date() 作用将字符类型按一定格式转化为日期类型：
具体用法:to_date(''2004-11-27'',''yyyy-mm-dd''),前者为字符串，后者为转换日期格式，注意，前后两者要以一对应。如;to_date(''2004-11-27 13:34:43'', ''yyyy-mm-dd
hh24:mi:ss'') 将得到具体的时间。
2.       to_char():将日期转按一定格式换成字符类型：
具体用法:to_char(sysdate,''yyyy-mm-dd hh24:mi:ss'')
 
to_date()与24小时制表示法及mm分钟的显示：
在使用Oracle的to_date函数来做日期转换时，很多Java程序员也许会直接的采用“yyyy-MM-dd HH:mm:ss”的格式作为格式进行转换，但是在Oracle中会引起错误：“ORA 01810
格式代码出现两次”。
如：select to_date('2005-01-01 13:14:20','yyyy-MM-dd HH24:mm:ss') from dual;
原因是SQL中不区分大小写，MM和mm被认为是相同的格式代码，所以Oracle的SQL采用了mi代替分钟。 oracle默认的系统时间就是sysdate函数，储存的数据形如2005-3-2510:55:33
，java 中取时间的对象是java.util.Date。
select to_date('2005-01-01 13:14:20','yyyy-MM-dd HH24:mi:ss') from dual
 
在java对oracle的操作中，对日期字段操作的例子：
表 book 中有name varchar2(20)//书籍名称,buydate Date //购买日期 两个字段。
已经创建了数据库连接Connection conn;
 
方法一、使用java.sql.Date实现比较简单的yyyy-mm-dd格式日期。java.sql.Date不支持时间格式。切记不要使用new java.sql.Date(int year,int month,int date),因为还要处
理时间差问题。
PreparedStatement pstmt = conn.prepareStatement("insert into book (name,buydate) values (?,?)");
java.sql.Date buydate=java.sql.Date.valueOf("2005-06-08");
pstmt.setString(1, "Java编程思想");
pstmt.setDate(2,buydate );
pstmt.execute();
方法二、使用java.sql.Timestamp,同上不使用new Timestamp(....)
PreparedStatement pstmt = conn.prepareStatement("insert into book (name,buydate) values (?,?)");
java.sql.Timestamp buydate=java.sql.Timestamp.valueOf("2004-06-08 05:33:99");
pstmt.setString(1, "Java编程思想");
pstmt.setTimestamp(2,buydate );
pstmt.execute();
方法三、使用oracle 的to_date内置函数
PreparedStatement pstmt = conn.prepareStatement("insert into book (name,buydate) values (?,to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
String buydate="2004-06-08 05:33:99";
pstmt.setString(1, "Java编程思想");
pstmt.setString(2,buydate );
pstmt.execute();
附:oracle日期格式参数含义说明
d:一周中的星期几
day:天的名字，使用空格填充到9个字符
dd:月中的第几天
ddd:年中的第几天
dy:天的简写名
iw: ISO标准的年中的第几周
iyyy:ISO标准的四位年份
yyyy:四位年份
yyy,yy,y:年份的最后三位，两位，一位
hh: 小时，按12小时计
hh24:小时，按24小时计
mi:分
ss:秒
mm:月
mon:月份的简写
month:月份的全名
w:该月的第几个星期
ww:年中的第几个星期

=====================
sql111= select * from  logincount where logtime = to_date('2009-11-30','yyyy-MM-dd')
st = con.prepareStatement("select * from  logincount where logtime = to_date('"+ new java.sql.Date(date.getTime())+"','yyyy-MM-dd')");


Java.util.date与java.sql.date区别和转换
标签： calendardate数据库stringjavaexception
2012-09-05 11:41 5195人阅读 评论(2) 收藏 举报
 分类：  数据库（1）  
java.util.Date 是 java.sql.Date 的父类（注意拼写） 前者是常用的表示时间的类，我们通常格式化或者得到当前时间都是用他

 后者之后在读写数据库的时候用他，因为PreparedStament的setDate()的第2参数和ResultSet的 getDate()方法的第2个参数都是java.sql.Date 

转换是 java.sql.Date date=new Java.sql.Date(); java.util.Date d=new java.util.Date (date.getTime()); 反过来是一样的。

  java.util.Date实际是一个时间戳，它的数值是年月日包括当前的时间 java.sql.Date也是一个时间戳，但它把当前的时间剪掉了，保证它一定是那个日期的0点0分0秒的时间戳

  所以如果你用new java.util.Date()会得到当前时间 但如果用new java.sql.Date(new java.util.Date().getTime())，得到的时间会是那一天的0点0分0秒  

格式如下:

 java.util.Date 是 年-月-日 时:分:秒.毫秒

 java.sql.Date 是 年-月-日 java.sql.Time 是 时:分:秒 

java.sql.Timestamp 是 年-月-日 时:分:秒 

 １、将java.util.Date 转换为 java.sql.Date 

java.sql.Date sd;

 java.util.Date ud;

 //initialize the ud such as ud = new java.util.Date();   

sd = new java.sql.Date(ud.getTime());   

２、若要插入到数据库并且相应的字段为Date类型 可使用PreparedStatement.setDate(int ,java.sql.Date)方法 其中的java.sql.Date可以用上面的方法得到 

  也可以用数据库提供TO_DATE函数 比如 现有 ud TO_DATE(new SimpleDateFormat().format(ud,"yyyy-MM-dd HH:mm:ss"), "YYYY-MM-DD HH24:MI:SS")

 注意java中表示格式和数据库提供的格式的不同 sql="update tablename set timer=to_date('"+x+"','yyyymmddhh24miss') where ....."  

 这里的x为变量为类似:20080522131223     

３、如何将"yyyy-mm-dd"格式的字符串转换为java.sql.Date 

  方法１   SimpleDateFormat bartDateFormat =   new SimpleDateFormat("yyyy-MM-dd");

   String dateStringToParse = "2007-7-12";  

 try{          java.util.Date date = bartDateFormat.parse(dateStringToParse);   

       java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 

       System.out.println(sqlDate.getTime());   }   

catch (Exception ex) {     

   System.out.println(ex.getMessage());   }    

 方法２ 

String    strDate    =    "2002-08-09";  

  StringTokenizer    st    =    new    StringTokenizer(strDate,    "-"); 

   java.sql.Date    date    =    new    java.sql.Date(Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));

 java.util.Date和java.sql.Date的区别 

java.sql.Date,java.sql.Time和java.sql.Timestamp三个都是java.util.Date的子类（包装类）。 

   但是为什么java.sql.Date类型的值插入到数据库中Date字段中会发生数据截取呢？   

java.sql.Date是为了配合SQL DATE而设置的数据类型。“规范化”的java.sql.Date只包含年月日信息，时分秒毫秒都会清零。格式类似：YYYY-MM-DD。

当我们调用ResultSet的getDate()方法来获得返回值时，java程序会参照"规范"的java.sql.Date来格式化数据库中的数值。因此，如果数据库中存在的非规范化部分的信息将会被劫取。   

在sun提供的ResultSet.java中这样对getDate进行注释的： Retrieves the of the designated column in the current row of this <code>ResultSet</code> object as a “java.sql.Date” object in the Java programming language.    

 同理。如果我们把一个java.sql.Date值通过PrepareStatement的setDate方法存入数据库时，java程序会对传入的java.sql.Date规范化，非规范化的部分将会被劫取。

然而，我们java.sql.Date一般由java.util.Date转换过来，如：java.sql.Date sqlDate=new java.sql.Date(new java.util.Date().getTime()). 

显然，这样转换过来的java.sql.Date往往不是一个规范的java.sql.Date.要保存java.util.Date的精确值， 我们需要利用java.sql.Timestamp. Calendar:  

 Calendar calendar=Calendar.getInstance();  //获得当前时间，声明时间变量

 int year=calendar.get(Calendar.YEAR);  //得到年

 int month=calendar.get(Calendar.MONTH);  //得到月，但是，月份要加上1

  month=month+1;  

int date=calendar.get(Calendar.DATE); 

 //获得日期  String today=""+year+"-"+month+"-"+date+"";  

原文地址:http://wenku.baidu.com/view/6ed160100b4e767f5acfcea6.html


 */