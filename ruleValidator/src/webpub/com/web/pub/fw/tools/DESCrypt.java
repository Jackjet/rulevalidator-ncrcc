package com.web.pub.fw.tools;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES对称加密
 * <ul>
 * <li>加密器使用DES/ECB/PKCS5Padding算法</li>
 * <li>生成密钥使用了DES算法</li>
 * <li>使用了统一密钥，而非随即密钥</li>
 * <li>密钥使用base64编码存储</li>
 * <li>密钥使用main方法获取</li>
 * <li>
 * 为了解决密文格式byte[]和目标格式java.lang.String两种数据不能相互转换的问题，中间采用了base64编码，是的byte[]和java
 * .lang. String可以无损相互转换</li>
 * 
 * @author:sunshine
 * @date:2013-12-25 上午10:43:15
 */
@SuppressWarnings("restriction")
public class DESCrypt {

	/**
	 * 日志组件
	 */
	protected static Log log = LogFactory.getLog(DESCrypt.class);

	/**
	 * 密钥算法
	 */
	private final static String KEY_ALGORITHM = "DES";

	/**
	 * 算法名称/加密模式/填充方式
	 */
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

	/**
	 * 字符集
	 */
	private final static String CHAR_SET;

	/**
	 * 盐
	 */
	private final static String SALT = "---==UFIDA==--";

	/**
	 * 密钥（base64编码）
	 */
	private final static String KEY_BASE64 = "YXo+5SNb+NM=";

	/**
	 * 密钥（使用统一的密码，方便机密和解密）
	 */
	private final static SecretKey KEY;

	static {
		// 字符集取系统文件编码集
		// CHAR_SET = System.getProperty("file.encoding");
		CHAR_SET = "UTF-8";

		// 还原key
		BASE64Decoder decorder = new BASE64Decoder();
		byte[] key = null;
		try {
			key = decorder.decodeBuffer(KEY_BASE64);
		} catch (IOException e) {
			if (log.isErrorEnabled()) {
				log.error("使用base64解密密钥失败");
			}
		}
		KEY = new SecretKeySpec(key, KEY_ALGORITHM);
	}

	/**
	 * 加密
	 * 
	 * @param plaintext
	 *            明文
	 * @return 密文
	 * @author:lixl
	 * @date:2012-8-14 下午02:28:34
	 */
	public static String encrypt(final String plaintext) {
		// 目标密文
		String ciphertext = null;
		try {
			Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, KEY);
			// 加盐
			String plaintextSalted = addSalt(plaintext);
			// 加密
			byte[] plaintextSaltedArray = plaintextSalted.getBytes(CHAR_SET);
			byte[] ciphertextArary = c.doFinal(plaintextSaltedArray);
			// base64编码，解决密文byte[]到String的不可逆
			BASE64Encoder base64Encoder = new BASE64Encoder();
			ciphertext = base64Encoder.encode(ciphertextArary);

			// ciphertext = new String(ciphertextArary, CHAR_SET);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("加密出错", e);
			}
		}
		return ciphertext;
	}

	/**
	 * 解密
	 * 
	 * @param ciphertext
	 *            密文
	 * @return 明文
	 * @author:lixl
	 * @date:2012-8-14 下午02:29:35
	 */
	public static String decrypt(final String ciphertext) {
		// 目标明文
		String plaintext = null;
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] ciphertextArary = base64Decoder.decodeBuffer(ciphertext);
			Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, KEY);
			// 解密
			byte[] plaintextSaltedArray = c.doFinal(ciphertextArary);
			String plaintextSalted = new String(plaintextSaltedArray, CHAR_SET);
			// 洗盐
			plaintext = removeSalt(plaintextSalted);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("解密出错", e);
			}
		}
		return plaintext;
	}

	/**
	 * 判断是否加密
	 * 
	 * @param text
	 *            文本
	 * @return 是否加密，候选值:0,已加密;1:未加密
	 * @author:lixl
	 * @date:2012-8-13 下午06:19:16
	 */
	public static boolean isEncode(final String text) {
		// 是否加密，候选值 false:没有加密,true:加密
		boolean isEncode = false;
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] ciphertextArary = base64Decoder.decodeBuffer(text);
			Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, KEY);
			// 解密
			byte[] plaintextSaltedArray = c.doFinal(ciphertextArary);
			String plaintextSalted = new String(plaintextSaltedArray, CHAR_SET);
			// 尝一下是否是咸的
			if (plaintextSalted.indexOf(SALT) > 0) {
				isEncode = true;
			}
		} catch (IllegalBlockSizeException e) {
			isEncode = false;
		} catch (BadPaddingException e) {
			isEncode = false;
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("判断是否加密过程中出错", e);
			}
		}
		return isEncode;
	}

	/**
	 * 加盐
	 * 
	 * @param plaintext
	 * @param salt
	 * @return
	 * @author:lixl
	 * @date:2012-8-13 下午02:23:15
	 */
	private static String addSalt(String plaintext) {
		return plaintext + SALT;
	}

	/**
	 * 洗盐
	 * 
	 * @param ciphertext
	 *            加盐后的明文（一般是解密之后）
	 * @return
	 * @author:lixl
	 * @date:2012-8-13 下午02:51:53
	 */
	private static String removeSalt(String plaintextSalted) {
		return plaintextSalted.substring(0,
				plaintextSalted.length() - SALT.length());
	}

	// /**
	// * 生成base64编码的密钥
	// *
	// * @param args
	// * @author:lixl
	// * @date:2012-8-13 下午04:27:35
	// */
	// public static void main(String[] args) {
	// // 生成base64编码的密钥
	// KeyGenerator key = null;
	// try {
	// key = KeyGenerator.getInstance(KEY_ALGORITHM);
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// key.init(56);
	// SecretKey sk = key.generateKey();
	// // return encoder.encode(keyArray);
	// byte[] keyArray = sk.getEncoded();
	// BASE64Encoder encoder = new BASE64Encoder();
	// String keyCoded = encoder.encode(keyArray);
	// System.out.println("生成的密钥（base64编码）为：" + keyCoded);
	// System.exit(0);
	// }
}
