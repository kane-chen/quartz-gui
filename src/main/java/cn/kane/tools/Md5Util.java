package cn.kane.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	
	 /**
     * MD5加密，不为空的参数String按顺序连接，并加上KEY后获得散列串
     * @param datium 要加密的参数序列
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static final String md5Encrypted(String key, Object... datium)
		throws NoSuchAlgorithmException, UnsupportedEncodingException {
		StringBuilder srcBuffer = new StringBuilder("");
		if(datium != null && datium.length > 0){
			for(Object data: datium){
				if(data != null){
					srcBuffer.append(data.toString()).append("&");
				}
			}
		}
		srcBuffer.append(key);
		System.out.println(srcBuffer.toString());
		LOGGER.debug("[MD5] source-string is:"+srcBuffer.toString());
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(srcBuffer.toString().getBytes());
		byte[] mdbytes = md.digest();
		
//		BigInteger hash = new BigInteger(1, mdbytes);
//        String md5Str = hash.toString(16);
//        if((md5Str.length() % 2) != 0) {
//            md5Str = "0" + md5Str;
//        }
//		System.out.println(md5Str);
//		return md5Str ;
		
		int pos;
		StringBuffer encryptedBuffer = new StringBuffer();
		for (int offset = 0; offset < mdbytes.length; offset++) {
			pos = mdbytes[offset];
			if (pos < 0){
				pos += 256;
			}
			if (pos < 16) {
				encryptedBuffer.append("0");
			}
			encryptedBuffer.append(Integer.toHexString(pos));
		}
		LOGGER.info("[MD5] encrypted-string is"+encryptedBuffer.toString());
		return encryptedBuffer.toString();
    }
	
}
