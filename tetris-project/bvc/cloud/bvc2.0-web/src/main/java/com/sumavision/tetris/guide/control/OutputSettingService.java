/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

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
	
	public OutputSettingVO addOutput(Long groupId) throws Exception{
		OutputSettingPO outputSettingPO = new OutputSettingPO();
		outputSettingPO.setGroupId(groupId);
		outputSettingDAO.save(outputSettingPO);
		return new OutputSettingVO().set(outputSettingPO);
	}
	
	public Object edit(String outputs) throws Exception{
		 List<OutputSettingVO> list1 = JSON.parseArray(outputs, OutputSettingVO.class);
		 
		 List<OutputSettingPO> list2 = new ArrayList<OutputSettingPO>();
		 
		 for (OutputSettingVO e : list1){
			 OutputSettingPO outputSettingPO = outputSettingDAO.findOne(e.getId());
			 OutputProtocol protocol = OutputProtocol.fromName(e.getOutputProtocolName());
			 outputSettingPO.setOutType(OutType.valueOf(e.getOutType()));
			 outputSettingPO.setOutputProtocol(protocol);
			 outputSettingPO.setOutputAddress(e.getOutputAddress());
			 RateCtrl rateCtrl = RateCtrl.fromName(e.getRateCtrlName());
			 outputSettingPO.setRateCtrl(rateCtrl);
			 outputSettingPO.setBitrate(e.getBitrate());
			 list2.add(outputSettingPO);
		 }
		 outputSettingDAO.save(list2);
		 return null;
	}
	
	public VideoParametersVO editVideo(
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
		videoParametersDAO.save(videoParametersPO);		
		return new VideoParametersVO().set(videoParametersPO);
	}
	
	public AudioParametersVO editAudio(
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
		audioParametersDAO.save(audioParametersPO);
		return new AudioParametersVO().set(audioParametersPO);
		
	}
	
	public void deleteOutput(String ids){
		List<Long> list1 = JSON.parseArray(ids, Long.class);
		List<OutputSettingPO> list2 = outputSettingDAO.findAll(list1);
		outputSettingDAO.deleteInBatch(list2);
	}

}
