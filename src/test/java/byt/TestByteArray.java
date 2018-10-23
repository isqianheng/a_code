package test.java.byt;

public class TestByteArray {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 从字节数组中截取一段
	 * @param src 源字节数组
	 * @param srcPos 源数组起始位置
	 * @param length 需要拷贝的长度
	 * @return
	 */
	private byte[] cutByteArray(byte[] src, int srcPos, int length){
		byte[] by = new byte[length];
		System.arraycopy(src, srcPos, by, 0, length);//native方法，源数组，原数组开始下标，目标数组，目标数组开始位置，拷贝的长度
		return by;
	}
}
