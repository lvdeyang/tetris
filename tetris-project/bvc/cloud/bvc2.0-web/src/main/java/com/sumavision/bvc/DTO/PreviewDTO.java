/**   

* @Title: PreviewDTO.java 

* @Package com.sumavision.bvc.DTO 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月13日 上午11:17:09 

* @version V1.0   

*/

package com.sumavision.bvc.DTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-service
 * 
 * 类名称：PreviewDTO
 * 
 * 类描述：
 * 
 * 创建人：cll
 * 
 * 创建时间：2018年6月13日 上午11:17:09
 * 
 * 修改人：cll
 * 
 * 修改时间：2018年6月13日 上午11:17:09
 * 
 * 修改备注：
 * 
 * @version
 *
 * 
 * 
 */
@Setter
@Getter
@Component
public class PreviewDTO extends DTO {

	/**
	 * 用户Id
	 */
	private Long userId = 1l;

	/**
	 * 摄像头的bundleId
	 */
	private String bundleId;

	/**
	 * 摄像头的通道名
	 */
	private String channelName = "video_encode";

	/**
	 * 摄像头的通道类型
	 */
	private String channelType = "simple_video_encode";

	/**
	 * 摄像头的通道参数
	 */
	private String channel_param = new ChannelParam("h264", 25, "4CIF").assembleChannleParam();

	private static class ChannelParam {

		private String codec_param;

		private String codec_type;

		private Integer framePerSecond;

		private String resolution;

		public ChannelParam() {

		}

		public ChannelParam(String codec_type, Integer framePerSecond, String resolution) {
			this.codec_type = codec_type;
			this.framePerSecond = framePerSecond;
			this.resolution = resolution;
		}

		public String assembleChannleParam() {
			if (StringUtils.isBlank(codec_type) || framePerSecond == null || StringUtils.isBlank(resolution)) {
				return null;
			}
			JSONObject jsonObject = new JSONObject();
			JSONObject codec_paramJson = new JSONObject();
			JSONObject codec_TypeJson = new JSONObject();
			jsonObject.put("codec_param", codec_paramJson);
			codec_paramJson.put("codec_type", codec_type);
			codec_paramJson.put(codec_type, codec_TypeJson);
			codec_TypeJson.put("framePerSecond", framePerSecond);
			codec_TypeJson.put("resolution", resolution);
			return jsonObject.toString();

		}
	}

}
