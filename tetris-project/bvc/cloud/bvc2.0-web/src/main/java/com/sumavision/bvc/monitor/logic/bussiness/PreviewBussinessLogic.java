/* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-service 下午1:05:38  
* All right reserved.  
*  
*/

package com.sumavision.bvc.monitor.logic.bussiness;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.ChannelBody;
import com.sumavision.bvc.BO.AcessRequestBOConverter;
import com.sumavision.bvc.DO.PreviewDO;
import com.sumavision.bvc.DTO.PreviewDTO;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.DTODOconverter.BaseConverter.ConverterFlag;
import com.sumavision.bvc.DTODOconverter.PreviewConverter;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc: bvc-monitor-service
 * @author: cll
 * @createTime: 2018年6月11日 下午1:05:38
 * @history:
 * @version: v1.0
 */
@Slf4j
@Service
public class PreviewBussinessLogic extends AbstractBussinessLogic<PreviewDTO> {
	// @Override
	// public void setServletContext(ServletContext servletContext) {
	// log.info("======Preview 调用父类方法===========");
	// super.setServletContext(servletContext);
	// }

	@Autowired
	AcessRequestBOConverter acessRequestBOConverter;

	@Autowired
	PreviewConverter previewConverter;

	/**
	 * 预览业务执行方法，by cll
	 */
	@Override
	public ResultMap executeBussiness(PreviewDTO dto) {
		// json业务DTO转DO对象
		PreviewDO previewDo = new PreviewDO();
		previewConverter.copyProperties(dto, previewDo, ConverterFlag.DTO2DO);
		// 根据业务查询资源，资源层
		try {
			// 资源层 获取业务资源,查询bundle下的channels
			List<ChannelBody> channelSchemeStrings = resourceService.queryChannelsOnBundle(previewDo.getBundleId());
			
			/*//使用java 8 stream Api转换
			List<ChannelSchemeBO> channelSchemeBOs=channelSchemeStrings
				.stream()
				.map(e -> (ChannelSchemeBO)JSONObject.toBean(JSONObject.fromObject(e),ChannelSchemeBO.class))
				.collect(Collectors.toList());
			// 比对需求参数与bundle的channel配置方案，选择适合的channel
			
			ChannelSchemeBO lockChannel = getLockResource(channelSchemeBOs, previewDo,
					PreviewStrategy.class.newInstance());
//			String lockChannelCommand = generateLockChannelCommand(lockChannel, previewDo);
			com.sumavision.bvc.BO.ChannelSchemeBO lockChannelBo = new com.sumavision.bvc.BO.ChannelSchemeBO();
			BeanUtils.copyProperties(lockChannel, lockChannelBo);
			// 资源层请求绑定
			ResultMap lockResult = resourseLayerTask.lockResource(lockChannelBo, 1l);
			if (lockResult != null && "200".equals(lockResult.get("code"))) {
				String openChannelCommand = generateOpenChannelCommand(lockChannel, previewDo);
				// 与接入层交互，打开channel
				ResultMap openChannelResult = acessLayerTask.openChannel(openChannelCommand);
				return openChannelResult;
			}*/
			// 根据
		} catch (Exception e) {
			//
			log.error("PreviewBussinessLogic executeBussiness failed", e);
		}
		return null;
	}
}
