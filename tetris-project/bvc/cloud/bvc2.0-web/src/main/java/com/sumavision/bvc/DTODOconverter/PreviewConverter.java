/**   

* @Title: PreviewConverter.java 

* @Package com.sumavision.bvc.converter 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月13日 下午1:24:13 

* @version V1.0   

*/

package com.sumavision.bvc.DTODOconverter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.DO.PreviewDO;
import com.sumavision.bvc.DTO.PreviewDTO;

/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-service
 * 
 * 类名称：PreviewConverter
 * 
 * 类描述：预览DTO转化为领域模型DO
 * 
 * 创建人：cll
 * 
 * 创建时间：2018年6月13日 下午1:24:13
 * 
 * 修改人：cll
 * 
 * 修改时间：2018年6月13日 下午1:24:13
 * 
 * 修改备注：
 * 
 * @version
 *
 * 
 * 
 */
@Component
public class PreviewConverter extends BaseConverter<PreviewDTO, PreviewDO> {
	@Override
	protected void copyDiffPropertiesFromDO2DTO(PreviewDTO target, PreviewDO source) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void copyDiffPropertiesFromDTO2DO(PreviewDO target, PreviewDTO source) {
		// TODO Auto-generated method stub

	}
}
