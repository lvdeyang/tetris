package com.sumavision.tetris.commons.util.encoder;

import java.security.MessageDigest;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.util.binary.ByteUtil;

/**
 * 消息文本编码器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月5日 下午5:35:23
 */
public class MessageEncoder {

	/**
	 * Sha256编码器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:32:14
	 */
	@Component
	public static class Sha256Encoder{
		
		/**
		 * 字符串编码<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年3月5日 下午5:32:58
		 * @param String message 待编码字符串
		 * @return String 编码后十六进制
		 */
		public String encode(String message) throws Exception{
			MessageDigest messageDigest = null;
			String encodeMessage = "";
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(message.getBytes("UTF-8"));
			encodeMessage = ByteUtil.bytesToHexString(messageDigest.digest());
			return encodeMessage;
		}
		
	}
	
}
