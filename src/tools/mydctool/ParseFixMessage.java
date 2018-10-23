package tools.mydctool;

public class ParseFixMessage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] array = {4,396,4,6,4,2,6,6,6,19,1,8,1,20,11,1,30,4,1,4,12,3,1,18,30,1,1,32,120,4,10,6,1,60,4,5,11,10,6,4};
		String message = "4052                                                                                                                                                                                                                                                                                                                                                                                                            4075      0000  000000000000001487                    ????????0                                                              0000 00000000000012000001000000000000000000                              00                                                                                                                                                            00000000000000000                                                            000000000000000000000000000000000000                                                                                                           4075      0000  000000000000001487                    ????????0                                                              0000 00000000000012000001000000000000000000                              00                                                                                                                                                            00000000000000000                                                            000000000000000000000000000000000000       ";
		for(int i=0;i<array.length;i++){
			System.out.println(message.substring(0,array[i]));
			message = message.substring(array[i]);
		}
	}

}
