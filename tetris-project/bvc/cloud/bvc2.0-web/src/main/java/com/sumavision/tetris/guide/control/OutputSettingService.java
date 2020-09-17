/**
 * 
 */
package com.sumavision.tetris.guide.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月4日 上午10:38:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OutputSettingService {
	
	@Autowired
	OutputSettingDAO outputSettingDAO;
	
	@Autowired
	VideoParametersDAO videoParametersDAO;
	
	@Autowired
	AudioParametersDAO audioParametersDAO;
	
	public OutputSettingPO edit(
			Long id,
			String outputProtocol,
			String outputAddress,
			String rateCtrl,
			Long bitrate
			){
		OutputSettingPO outputSettingPO = outputSettingDAO.findOne(id);
		outputSettingPO.setId(id);
		outputSettingPO.setOutputProtocol(outputProtocol);
		outputSettingPO.setOutputAddress(outputAddress);
		outputSettingPO.setRateCtrl(rateCtrl);
		outputSettingPO.setBitrate(bitrate);
		return outputSettingDAO.save(outputSettingPO);
	}
	
	public VideoParametersPO editVideo(
			Long id,
			String codingObject,
			String fps,
			Long bitrate,
			String resolution,
			String ratio,
			String rcMode,
			Long maxBitrate) throws Exception{
		VideoParametersPO videoParametersPO = videoParametersDAO.findOne(id);
		CodingObject object = CodingObject.fromName(codingObject);
		videoParametersPO.setCodingObject(object);
		videoParametersPO.setFps(fps);
		videoParametersPO.setBitrate(bitrate);
		Resolution re = Resolution.fromName(resolution);
		videoParametersPO.setResolution(re);
		Ratio ra = Ratio.fromName(ratio);
		videoParametersPO.setRatio(ra);
		RcMode rc = RcMode.fromName(rcMode);
		videoParametersPO.setRcMode(rc);
		videoParametersPO.setMaxBitrate(maxBitrate);
		return videoParametersDAO.save(videoParametersPO);		
	}
	
	public AudioParametersPO editAudio(
			Long id,
			String codingFormat,
			String channelLayout,
			String bitrate,
			String sampleRate,
			String codingType) throws Exception{
		AudioParametersPO audioParametersPO = audioParametersDAO.findOne(id);
		CodingFormat format = CodingFormat.fromName(codingFormat);
		audioParametersPO.setCodingFormat(format);
		ChannelLayout channel = ChannelLayout.fromName(channelLayout);
		audioParametersPO.setChannelLayout(channel);
		audioParametersPO.setBitrate(bitrate);
		audioParametersPO.setSampleRate(sampleRate);
		CodingType type = CodingType.fromName(codingType);
		audioParametersPO.setCodingType(type);
		return audioParametersDAO.save(audioParametersPO);
		
	}
	
	
	public void delete(Long id){
		outputSettingDAO.delete(id);
	}

}
