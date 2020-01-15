/**   

* @Title: PreviewBo.java 

* @Package com.sumavision.bvc.bo 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月11日 下午7:59:30 

* @version V1.0   

*/

package com.sumavision.bvc.DO;

import net.sf.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-service
 * 
 * 类名称：PreviewBo
 * 
 * 类描述：
 * 
 * 创建人：cll
 * 
 * 创建时间：2018年6月11日 下午7:59:30
 * 
 * 修改人：cll
 * 
 * 修改时间：2018年6月11日 下午7:59:30
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
public class PreviewDO extends DO{
	
	Integer splitNumer;
	
	Integer location;
	
	/**
	 * 用户Id
	 */
	private Long userId;
	
	/**
	 * 任务ID
	 */
	private Long taskId;
	/**
	 * 摄像头的bundleId
	 */
	private String bundleId;
	
	/**
	 * 摄像头的通道名
	 */
	private String channelName;
	
	/**
	 * 摄像头的通道类型
	 */
	private String channelType;
	
	/**
	 * 摄像头的通道参数
	 */
	private String channelParam;
	
}
