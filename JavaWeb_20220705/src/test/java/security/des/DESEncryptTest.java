package security.des;

import java.math.BigInteger;
import java.util.Arrays;

public class DESEncryptTest {
	
	public static void main(String[] args) throws Exception {
		// 設定密鑰的路徑與名稱
		String key_path = "C:/Users/MB-207/git/JavaWeb_20220705-2-/JavaWeb_20220705/key/user2.key";
		// 建立 DESEncryptService
		DESEncryptService des = new DESEncryptService(key_path);
		
		// 明文
		String msg = "跟家人中秋賞月吃月餅吃燒烤...";
		System.out.println("明文: " + msg);
		
		// 加密
		byte[] enMsg = des.encrytor(msg); // 將明文加密
		System.out.println(Arrays.toString(enMsg));
		//String output = String.format("%X", new BigInteger(enMsg));
		String output = des.bytesToHex(enMsg);
		System.out.println("密文: " + output);
		
		
	}

}