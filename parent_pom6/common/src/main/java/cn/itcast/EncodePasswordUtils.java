package cn.itcast;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class EncodePasswordUtils {
	
	public static String encodePassword(String password) throws Exception{
		
		//MD5摘要加密
		MessageDigest instance = MessageDigest.getInstance("MD5");
		byte[] digest = instance.digest(password.getBytes());
		
		//16进制加密
		char[] encodeHex = Hex.encodeHex(digest);
		
		return new String(encodeHex);
	}
}
