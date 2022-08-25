package security.des;

public class DESDncryptTest {
	public static void main(String[] args) throws Exception {
		// 解密
		// 密文: 33197874B0B0172348475081FAFFD26BF2BF130FA889DE3D06AEC45869605AAC06796A364609C9C0C143CFF885919DED
		// 取得密文位置
		String key_path = "C:/Users/MB-207/git/JavaWeb_20220705-2-/JavaWeb_20220705/key/user.key";
		DESEncryptService des = new DESEncryptService(key_path);
		
		//待解密文 hex
		String hex = "33197874B0B0172348475081FAFFD26BF2BF130FA889DE3D06AEC45869605AAC06796A364609C9C0C143CFF885919DED";
		System.out.println("待解密文 : " + hex);
		// 將 hex 轉 byte[]
		byte [] byteArray = des.hexToByteArray(hex);
		// 解密
		byte[] deMsg = des.decryptor(byteArray);
		//將 byte[] 轉字串
 		String output = new String(deMsg);
 		System.out.println("解密 :" + output);
		
	}
}
