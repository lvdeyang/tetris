package com.sumavision.bvc.device.group.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.dao.DeviceGroupAvtplDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(rollbackFor = Exception.class)
@Service
public class DeviceGroupAvtplService {
	@Autowired
	DeviceGroupAvtplDAO avtplDAO;
	/**
	 * 修改会议组参数
	 * @param id
	 * @param videoFormat
	 * @param audioFormat
	 * @param portReuse 端口复用
	 * @param adaptionJson 不同档次编码参数集合
	 *     adaptionId:'',
	 *     adaptionName:'',
	 *     videoRate:'',
	 *     videoResolution:'',
	 *     frameRate:'',
	 *     audioRate:''
	 * @return
	 */
	public DeviceGroupAvtplPO modify(Long id,
									 String videoFormat,
									 String audioFormat,
									 boolean portReuse,
									 String adaptionJson) throws Exception{

		DeviceGroupAvtplPO deviceGroupAvtplPO=avtplDAO.findOne(id);
		deviceGroupAvtplPO.setVideoFormat(VideoFormat.fromName(videoFormat));
		deviceGroupAvtplPO.setAudioFormat(AudioFormat.fromName(audioFormat));
		deviceGroupAvtplPO.setMux(portReuse);
		JSONArray array=JSONArray.parseArray(adaptionJson);
		for (Object obj:array) {
			JSONObject json=(JSONObject) obj;
			for (DeviceGroupAvtplGearsPO gearPo:deviceGroupAvtplPO.getGears()) {
				if(gearPo.getId().equals(json.getLong("adaptionId"))){
					gearPo.setChannelParamsType(ChannelParamsType.fromName(json.getString("adaptionName")));
					gearPo.setVideoBitRate(json.getString("videoRate"));
					gearPo.setAudioBitRate(json.getString("audioRate"));
					gearPo.setVideoResolution(Resolution.fromName(json.getString("videoResolution")));
					gearPo.setFps(json.getString("frameRate"));
					//以下为适应页面上暂时不支持编码格式设置到gear上时
					gearPo.setVideoFormat(VideoFormat.fromName(json.getString("videoFormat")));
					gearPo.setAudioFormat(AudioFormat.fromName(json.getString("audioFormat")));
				}

			}
		}
		avtplDAO.save(deviceGroupAvtplPO);
		return deviceGroupAvtplPO;

	}
	
}
