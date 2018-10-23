package tools.mydctool;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import test.format.FormatXml;



/**
 * 通讯接出适配器
 * 
 * @author air
 */
public class TcpSendDemo  {
	// Socket链接地址  // Socket链接端口（默认值为0）
	public String address =null;
	public int commport = 1;
	// 消息长度
	public int msglen = 4;
	// Socket链接超时时间（默认值为30秒）
	public int timeout = 8000;
	// 报文类型
	// private String msgtype = null;
	// 字符集
	private String characterset = "UTF-8";

	// 报文读取缓存长度默认2048
	private int maxbytes = 2048;

	private String system_no = "CBS";

	
	//测试发送报文 
	public static void main(String[] args) throws IOException {
		TcpSendDemo  demo =new TcpSendDemo();
		String f = null;
		
		
//		address = "10.3.148.42";//开发esb
//		address = "10.3.148.43";
//		commport = 10090;//开发esb
//		address = "10.3.110.158";
//		address = "10.3.110.154";
//		address = "10.3.120.43";
//		address = "10.3.97.96";
//		address = "10.3.130.134";
//		commport = 9001;
//		commport = 10090;
		
		// ecim dev 
		demo.address = "10.3.97.101";
		demo.commport = 8001;
		demo.msglen = 10;
		
		// ecis uat
		demo.address = "10.3.120.43";
		demo.commport = 9001;
		demo.msglen = 4;
		
		
		f = "/src/tools/mydctool/tcpSendDemo.xml";
//		f = "/src/tools/mydctool/qianyue.xml";
//		f = "/src/tools/mydctool/jieyue.xml";
//		f = "/src/tools/mydctool/xiaofei.xml";
//		f = "/src/tools/mydctool/tcpmsg/账户信息验证.xml";
//		f = "/src/tools/mydctool/tuihuo.xml";
//		f = "/src/tools/mydctool/tcpmsg/冲正2.xml";
//		f = "/src/tools/mydctool/tcpmsg/退货1.xml";
//		f = "/src/tools/mydctool/tcpmsg/支付3.xml";
//		f = "/src/tools/mydctool/tcpmsg/分期订单查询.xml";
//		f = "/resource/tempfile/5.xml";
//		f = "/src/tools/mydctool/tcpmsg/分期订单查询新201806.xml";
//		f = "/src/tools/mydctool/tcpmsg/测试报文头.xml";
//		f = "/src/tools/mydctool/tcpmsg/决策引擎.xml";
		
//		f = "/src/tools/mydctool/网联支付请求报文.xml";
//		f = "/src/tools/mydctool/tcpmsg/zhanghuxinxiyanzheng.xml";
//		f = "/src/tools/mydctool/tcpmsg/对账发往ecim查询报文.xml";
		
		
		
		
		String msg = FileUtils.readFileToString(new File(System.getProperty("user.dir")+f));
		demo.doComm(msg);
	}

	

	/**
	 * 获得socket连接
	 * 
	 * @return
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	private Socket getSocket() throws UnknownHostException, IOException {
		return new Socket(this.address, this.commport);
	}

	/**
	 * 获得输入流对象
	 * 
	 * @return
	 * @throws IOException
	 */
	private InputStream getInputStream(Socket client) throws IOException {
		// 设置本次通讯输入流等待超时时间
		client.setSoTimeout(this.timeout);
		return client.getInputStream();
	}

	/**
	 * 发送消息
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private void sendMsg(byte[] bytes, Socket client) throws IOException {
		System.out.println("添加长度后发送报文："+this.msgCharType2String(bytes,this.characterset));
		
		OutputStream os = client.getOutputStream();
		os.write(bytes);
		os.flush();
	
	}

	

	/**
	 * 读取报文头长度
	 * 
	 * @return -1 输入流异常<br>
	 *         -2 输入流对象为null
	 * @throws IOException
	 */
	private ArrayList<Object> readMsgLength(Socket client) throws IOException,
			NumberFormatException {
		// 报文长度定义的长度,结束位置
		int len_read_end = this.msglen;
		// 报文长度读取起始位置
		int len_read_start = 0;
		// 报文长度读取长度
		int len_read_count = -1;
		// 报文长度
		byte[] bytes_msg_len = new byte[len_read_end];

		InputStream is = null;
		is = this.getInputStream(client);

		ArrayList<Object> al = new ArrayList<Object>(2);
		if (null != is) {
			while (true) {
				len_read_count = is.read(bytes_msg_len, len_read_start,
						len_read_end - len_read_start);

				if (len_read_count == -1) {
						System.out.println("client.isClosed[" + client.isClosed() + "]");

						System.out.println("输入流异常,已接收到的长度报文["
								+ new String(bytes_msg_len, 0, len_read_start)
								+ "]");
					al.add(0, null);
					// -1 输入流异常
					al.add(1, -1);
					return al;
				}

				len_read_start += len_read_count;
				if (len_read_start == len_read_end) {
					break;
				}

			}
			System.out.println("读取的报文长度为:[" + new String(bytes_msg_len) + "]");
			al.add(0, bytes_msg_len);
			al.add(1, Integer.parseInt(this.msgCharType2String(bytes_msg_len,
					this.characterset)));
			return al;
		}
		al.add(0, null);
		// -2 输入流对象为null
		al.add(1, -2);
		return al;
	}

	/**
	 * 读取报文体部分
	 * 
	 * @param bytes_msg_len
	 *            报文长度byte数组
	 * @param msglen
	 *            报文长度 int
	 * @return 全报文体
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private byte[] readMsg(byte[] bytes_msg_len, int msglen, Socket client)
			throws UnsupportedEncodingException, IOException {

		// 报文长度定义的长度,结束位置
		int len_read_end = this.msglen;
		// 全报文
		byte[] requestCache = new byte[len_read_end + msglen];
		// 从输入流中获取数据的起始位置
		int msg_read_cur = len_read_end;
		// 每次读取的数据的长度
		int msg_read_count = -1;

		InputStream is = null;
		is = this.getInputStream(client);

		if (null != is) {
			System.arraycopy(bytes_msg_len, 0, requestCache, 0,
					bytes_msg_len.length);
			while (true) {
				if (maxbytes + msg_read_cur > msglen) {
					msg_read_count = is.read(requestCache, msg_read_cur, msglen
							- msg_read_cur + len_read_end);
				} else {
					msg_read_count = is.read(requestCache, msg_read_cur,
							maxbytes);
				}

				if (msg_read_count == -1) {
					System.out.println("client.isClosed[" + client.isClosed() + "]");

					System.out.println("输入流异常,已接收到的报文正文["
								+ new String(requestCache, 0, msg_read_cur)
								+ "]");
					return null;
				}

				msg_read_cur += msg_read_count;
				if (msg_read_cur - len_read_end == msglen) {
					break;
				}

			}
			System.out.println("接收到的报文["
						+ new String(requestCache, this.characterset) + "]");
			return requestCache;
		}
		return null;
	}

	/**
	 * 添加报文长度
	 * 
	 * @param in
	 *            需发送的报文,已经是通过字符集要求转换后的byte
	 * @return
	 */
	private byte[] addLength(byte[] in) {
		int length = in.length;
		String len = String.valueOf(length);
		byte[] out = null;

		if (len.length() > this.msglen) {
			System.out.println("发送报文长度超出已定义长度,定义长度=[" + this.msglen + "],实际报文长度=["
						+ len + "]");
			throw new RuntimeException("[" + system_no + "]发送报文长度超出已定义长度.");
		}

		System.out.println("报文体长度[" + length + "]");

		while (len.length() < this.msglen) {
			len = "0" + len;
		}

		out = new byte[length + this.msglen];
		System.arraycopy(len.getBytes(), 0, out, 0, this.msglen);
		System.arraycopy(in, 0, out, this.msglen, length);
		return out;
	}

	

	/**
	 * 报文编码格式转换2String
	 * 
	 * @param in
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String msgCharType2String(byte[] in, String cahrCode)
			throws UnsupportedEncodingException {
		return new String(in, cahrCode);
	}

	/**
	 * 去掉报文中长度信息
	 * 
	 * @param in
	 * @return
	 */
	private byte[] takeOutMsgLen(byte[] in) {
		byte[] in_bytes = null;
		if (null != in) {
			in_bytes = new byte[in.length - this.msglen];
			System.arraycopy(in, this.msglen, in_bytes, 0, in_bytes.length);
		}
		return in_bytes;
	}

	/**
	 * 对象转换为bytes
	 * 
	 * @param in
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private byte[] object2Bytes(Object in) throws UnsupportedEncodingException {

		byte[] in_bytes = null;
		// 先判断是否是String 
		 if (in instanceof String) {
			in_bytes = ((String) in).getBytes(this.characterset);
		} else if (in instanceof byte[]) {
			in_bytes = (byte[]) in;
		} else {
			throw new RuntimeException("未支持的数据对象");
		}

		if (null != in_bytes) {
				System.out.println("ThreadId=[" + Thread.currentThread().getId()
						+ "],发送的报文体为["
						+ this.msgCharType2String(in_bytes, this.characterset)
						+ "]");
			return in_bytes;
		}
		return null;
	}
	/**
	 * bytes转换为对象
	 * 
	 * @param in
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private Object bytes2object(byte[] in, Object send_msg)
			throws UnsupportedEncodingException {

		Object in_object = null;
		// 先判断是否是CD

		if (send_msg instanceof String) {
			in_object = this.msgCharType2String(in, this.characterset);
		} else if (send_msg instanceof byte[]) {
			in_object = in;
		} else {
			throw new RuntimeException("未支持的数据对象");
		}

		if (null != in_object) {
			System.out.println("ThreadId=[" + Thread.currentThread().getId()
						+ "],接收的报文体xml格式化后为[\n"
//						+ this.msgCharType2String(in, this.characterset)
						+ FormatXml.formatXml(this.msgCharType2String(in, this.characterset))
						+ "\n]");
		}
		return in_object;
	}

	
	/**
	 * 通讯接出执行主方法
	 * 
	 * @param send_msg
	 * @return
	 */
	public Object doComm(Object send_msg) {
		// Socket客户端
		Socket client = null;

		System.out.println("[" +" system_no交易接出执行开始...");

		// 存放发送报文数据
		byte[] send_msg_bytes = null;
		// 存放发送报文数据
		byte[] send_msg_bytes_all = null;
		// 存放返回报文数据
		byte[] receive_msg_bytes = null;
		// 存放返回报文最终数据类型
		Object receive_msg = null;


		ArrayList<Object> al = null;
		// 接收报文长度
		int msglen = 0;
		// 接收报文长度byte
		byte[] bytes_msg_len = null;

		try {

			// 根据报文类型做报文转换，默认byte数组
			send_msg_bytes = this.object2Bytes(send_msg);
			if (null == send_msg_bytes) {
				System.out.println("请求报文类型转换失败.");
				throw new UnsupportedEncodingException();
			}

			
			// 报文添加长度头
			send_msg_bytes_all = this.addLength(send_msg_bytes);
			
			


			client = this.getSocket();

			// 发送报文
			this.sendMsg(send_msg_bytes_all, client);

			// 读取返回报文长度
			al = this.readMsgLength(client);
			if (null != al) {
				bytes_msg_len = (byte[]) al.get(0);
				msglen = (Integer) al.get(1);
				if (null == bytes_msg_len || msglen <= 0) {
					System.out.println("未读取到报文长度信息,-1:输入流异常 -2:输入流对象为null[" + msglen
								+ "]");
					throw new RuntimeException("未读取到上送报文长度信息.");
				}
			} else {
				System.out.println("读取报文长度信息失败.");
				throw new RuntimeException("读取报文长度信息失败.");
			}

			// 读取报文体
			receive_msg_bytes = this.readMsg(bytes_msg_len, msglen, client);
			if (null == receive_msg_bytes) {
				System.out.println("读取上送报文体失败.");
				throw new RuntimeException("读取上送报文体失败.");

			}

			System.out.println("应读取长度[" + (msglen + this.msglen) + "],实际读取长度["
						+ receive_msg_bytes.length + "]");
			if (receive_msg_bytes.length != (msglen + this.msglen)) {
				System.out.println("读取报文输入流异常中断,应读取长度[" + (msglen + this.msglen)
							+ "],实际读取长度[" + receive_msg_bytes.length + "]");
				throw new RuntimeException("读取上送报文未读取完整.");
			}

			System.out.println("收到上送报文["
						+ this.msgCharType2String(receive_msg_bytes,
								characterset) + "]");

			// 去掉报文长度
			receive_msg_bytes = this.takeOutMsgLen(receive_msg_bytes);
			if (null == receive_msg_bytes) {
				System.out.println("去掉报文长度失败.");
				throw new RuntimeException("去掉报文长度失败.");
			}

			// 根据报文类型做报文转换，默认byte数组
			receive_msg = this.bytes2object(receive_msg_bytes, send_msg);
			if (null == receive_msg) {
				System.out.println("接收报文类型转换失败.");
				throw new RuntimeException("接收报文类型转换失败.");
			}
		} catch (Exception e) {
			System.out.println("运行异常"+e);
			throw new RuntimeException("运行异常.", e);
		} finally {
			try {
				if (null != client) {
					client.close();
				}
			} catch (IOException e) {
				System.out.println("关闭连接失败"+e);
				throw new RuntimeException("关闭连接失败", e);
			}
			System.out.println("[]交易接出执行结束...");
		}
		return receive_msg;
	}
}
