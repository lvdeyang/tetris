package com.sumavision.tetris.bvc.business.dispatch.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.dispatch.bo.AudioParamBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.ChannelBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.CodecParamBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.VideoParamBO;
import com.sumavision.tetris.bvc.business.dispatch.enumeration.CodecParamType;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class DispatchCodecParamUtil {
	
	private Map<String, AvtplPO> typeAvtplMap = new HashMap<String, AvtplPO>();
	
	/**
	 * 给ChannelBO补全codec_param参数<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午4:46:48
	 * @param channelBO
	 * @throws Exception
	 */
	public void complementCodecParam(ChannelBO channelBO) throws Exception{
		
		//拷贝一个codecParamType
		String codecParamType = new StringBufferWrapper().append(channelBO.getCodecParamType()).toString();
		
		//如果codecParamType是AUTO，先修改为COMMAND1
		if(codecParamType.equals(CodecParamType.AUTO.getName())){
			//指定使用COMMAND1模板
			codecParamType = "COMMAND1";
		}
		
		//只要codecParamType不是PARAM，就生成一套参数
		if(!codecParamType.equals(CodecParamType.PARAM.getName())){
			CodecParamBO codecParamBO = generateCodecParamBO(codecParamType, null);
//			setCodecParamBO(codecParamBO, avtpl, targetGear);
			channelBO.setCodec_param(codecParamBO);
		}
	}
	
	private CodecParamBO generateCodecParamBO(String codecParamType, String codecParam) throws Exception{
		AvtplPO avtpl = generateAvtpl(codecParamType, codecParam);
		AvtplGearsPO targetGear = null;
		Set<AvtplGearsPO> gears = avtpl.getGears();
		for(AvtplGearsPO gear : gears){
			targetGear = gear;
			break;
		}
		CodecParamBO codecParamBO = generateCodecParamBO(avtpl, targetGear);
		return codecParamBO;
	}
	
	 public AvtplPO generateAvtpl(String codecParamType, String codecParam) throws Exception{
		
		//拷贝一个codecParamType
		String codecParamTypeCopy = new StringBufferWrapper().append(codecParamType).toString();
		
		AvtplPO avtpl = new AvtplPO();
		if(codecParamTypeCopy.equals(CodecParamType.AUTO.getName())){
			//取COMMAND1
			codecParamTypeCopy = "COMMAND1";
		}else if(codecParamTypeCopy.equals(CodecParamType.PARAM.getName())){
			//把codecParam转成AvtplPO
			JSONObject paramObject = JSONObject.parseObject(codecParam);
			avtpl.setName(paramObject.getString("name"));
			avtpl.setVideoFormat(VideoFormat.fromName(paramObject.getString("videoFormat")));
			avtpl.setVideoFormatSpare(VideoFormat.fromName(paramObject.getString("videoFormat2")));
			avtpl.setAudioFormat(AudioFormat.fromName(paramObject.getString("audioFormat")));
			avtpl.setGears(new HashSet<AvtplGearsPO>());
			
			List<JSONObject> gearJsons = JSONArray.parseArray(paramObject.getString("gears"), JSONObject.class);
			for(JSONObject gearJson: gearJsons){
				AvtplGearsPO gear = new AvtplGearsPO();
				gear.setName(gearJson.getString("name"));
				gear.setVideoBitRate(gearJson.getString("videoBitRate"));
				gear.setVideoBitRateSpare(gearJson.getString("videoBitRate2"));
				gear.setVideoResolution(Resolution.fromName(gearJson.getString("VideoResolution")));
				gear.setVideoResolutionSpare(Resolution.fromName(gearJson.getString("VideoResolution2")));
				gear.setAudioBitRate(gearJson.getString("AudioBitRate"));
				gear.setLevel(GearsLevel.fromLevel(gearJson.getIntValue("level")));
				gear.setAvtpl(avtpl);
				avtpl.getGears().add(gear);
			}
			return avtpl;
		}
		
		//codecParamType的取值应对应defaultCodecTempletes.json中的defaultType，如COMMAND1
		String line;
		StringBuilder sBuilder = new StringBuilder();
		
		InputStream jsonFile = DispatchCodecParamUtil.class.getResourceAsStream("/defaultCodecTempletes.json");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(jsonFile));
		
		while((line = bReader.readLine()) != null){
			sBuilder.append(line);
		}
		
		boolean has = false;
        List<JSONObject> jsonArray = JSONArray.parseArray(sBuilder.toString(), JSONObject.class);
        for(JSONObject jsonObject: jsonArray){
        	if(jsonObject.getString("defaultType").equals(codecParamTypeCopy)){
        		        		
        		if(typeAvtplMap.containsKey(codecParamTypeCopy)){
        			//从缓存里取
        			avtpl = typeAvtplMap.get(codecParamTypeCopy);
        			has = true;
        		}else{
        			//缓存里没有
	        		avtpl.setName(jsonObject.getString("name"));
	    			avtpl.setVideoFormat(VideoFormat.fromName(jsonObject.getString("videoFormat")));
	    			avtpl.setVideoFormatSpare(VideoFormat.fromName(jsonObject.getString("videoFormat2")));
	    			avtpl.setAudioFormat(AudioFormat.fromName(jsonObject.getString("audioFormat")));
	    			avtpl.setGears(new HashSet<AvtplGearsPO>());
	    			
	    			List<JSONObject> gearJsons = JSONArray.parseArray(jsonObject.getString("gears"), JSONObject.class);
	    			for(JSONObject gearJson: gearJsons){
	    				AvtplGearsPO gear = new AvtplGearsPO();
	    				gear.setName(gearJson.getString("name"));
	    				gear.setVideoBitRate(gearJson.getString("videoBitRate"));
	    				gear.setVideoBitRateSpare(gearJson.getString("videoBitRate2"));
	    				gear.setVideoResolution(Resolution.fromName(gearJson.getString("VideoResolution")));
	    				gear.setVideoResolutionSpare(Resolution.fromName(gearJson.getString("VideoResolution2")));
	    				gear.setFps(gearJson.getString("fps"));
	    				gear.setAudioBitRate(gearJson.getString("AudioBitRate"));
	    				gear.setLevel(GearsLevel.fromLevel(gearJson.getIntValue("level")));
	    				gear.setAvtpl(avtpl);
	    				avtpl.getGears().add(gear);
	    			}
	    			//放入缓存
	    			typeAvtplMap.put(codecParamTypeCopy, avtpl);
	    			has = true;
        		}
    			break;
        	}
        }
        if(!has) return null;
		
		return avtpl;
	}
	
	/**
	 * @Title: 协议参数数据转换 <br/>
	 * @param avtpl 通用会议参数模板
	 * @param gear 当前会议参数档位
	 * @return CodecParamBO 协议参数
	 */
	private CodecParamBO generateCodecParamBO(AvtplPO avtpl, AvtplGearsPO gear){
		CodecParamBO codecParamBO = new CodecParamBO();
		codecParamBO.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName()))
					.setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
				   						      .setResolution(gear.getVideoResolution().getName())
				   						      .setBitrate(gear.getVideoBitRate()));
		return codecParamBO;
	}
	
}
