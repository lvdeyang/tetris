package com.sumavision.bvc.device.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.enumeration.CodecParamType;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.monitor.mux.MuxService;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplPO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AvtplService{
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private MuxService muxService;
	
	@Autowired
	private MeetingUtil meetingUtil;

	/**
	 * 添加一个参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:31:07
	 * @param String name 模板名称
	 * @param String videoFormat 视频编码格式
	 * @param String videoFormatSpare 备用视频编码格式
	 * @param String audioFormat 音频编码格式
	 * @param String usageType 模板用途
	 * @param Boolean mux 是否开启端口复用
	 * @param Long userId 业务用户id
	 * @return AvtplPO 参数模板
	 */
	public AvtplPO save(
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			String usageType,
			Boolean mux,
			Long userId) throws Exception{
		
		if(avtplDao.findByName(name).size() > 0){
			throw new CommonNameAlreadyExistedException("参数模板", name);
		}
		AvtplPO tpl = new AvtplPO();
		tpl.setName(name);
		tpl.setVideoFormat(VideoFormat.fromName(videoFormat));
		tpl.setVideoFormatSpare(VideoFormat.fromName(videoFormatSpare));
		tpl.setAudioFormat(AudioFormat.fromName(audioFormat));
		tpl.setUsageType(AvtplUsageType.fromName(usageType));
		tpl.setUpdateTime(new Date());
		tpl.setMux(mux);
		avtplDao.save(tpl);
		
		if(AvtplUsageType.VOD.equals(tpl.getUsageType())){
			muxService.switchMux(mux, userId);
		}
		
		return tpl;
	}
	
	/**
	 * 修改参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:56:19
	 * @param Long id 模板id
	 * @param String name 模板名称
	 * @param String videoFormat 视频编码格式
	 * @param String videoFormatSpare 备用视频编码格式
	 * @param String audioFormat 音频编码格式
	 * @param String usageType 模板用途
	 * @param String mux 是否开启端口复用
	 * @param Long userId 业务用户id
	 * @return AvtplPO 参数模板
	 */
	public AvtplPO update(
			Long id,
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			String usageType,
			Boolean mux,
			Long userId) throws Exception{
		
		boolean muxChange = false;
		
		AvtplPO tpl = avtplDao.findOne(id);
		if(!tpl.getName().equals(name)){
			if(avtplDao.findByName(name).size() > 0){
				throw new CommonNameAlreadyExistedException("参数模板", name);
			}
		}
		tpl.setName(name);
		tpl.setVideoFormat(VideoFormat.fromName(videoFormat));
		tpl.setVideoFormatSpare(VideoFormat.fromName(videoFormatSpare));
		tpl.setAudioFormat(AudioFormat.fromName(audioFormat));
		AvtplUsageType newAvtpl = AvtplUsageType.fromName(usageType);
		if(!tpl.getUsageType().equals(AvtplUsageType.VOD) && newAvtpl.equals(AvtplUsageType.VOD)){
			muxChange = true;
		}
		tpl.setUsageType(newAvtpl);
		tpl.setUpdateTime(new Date());
		if(!mux.equals(tpl.getMux())){
			tpl.setMux(mux);
			muxChange = true;
		}
		avtplDao.save(tpl);
		
		if(muxChange && AvtplUsageType.VOD.equals(tpl.getUsageType())){
			muxService.switchMux(mux, userId);
		}
		
		return tpl;
	}
	/**
	 * 给各种类型创建默认的参数模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月13日 下午2:30:51
	 * @throws Exception
	 */
	public void generateDefaultAvtpls() throws Exception{

		log.info("==============AvtplService 开始添加默认模板=================");
		List<AvtplPO> newAvtpls = new ArrayList<AvtplPO>();
		
		List<AvtplPO> avtpls_DEVICE_GROUP = avtplDao.findByUsageType(AvtplUsageType.DEVICE_GROUP);
		if(avtpls_DEVICE_GROUP.size() == 0){
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "MEETING1");
			if(sys_avtpl != null){
				sys_avtpl.setUsageType(AvtplUsageType.DEVICE_GROUP);
				newAvtpls.add(sys_avtpl);
			}
		}
		
		List<AvtplPO> avtpls_PLAYER = avtplDao.findByUsageType(AvtplUsageType.WEB_PLAYER);
		if(avtpls_PLAYER.size() == 0){
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "PLAYER1");
			if(sys_avtpl != null){
				sys_avtpl.setUsageType(AvtplUsageType.WEB_PLAYER);
				newAvtpls.add(sys_avtpl);
			}
		}
		
		List<AvtplPO> avtpls_VOD = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avtpls_VOD.size() == 0){
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "VOD1");
			if(sys_avtpl != null){
				sys_avtpl.setUsageType(AvtplUsageType.VOD);
				newAvtpls.add(sys_avtpl);
			}
		}
		
		List<AvtplPO> avtpls_COMMAND = avtplDao.findByUsageType(AvtplUsageType.COMMAND);
		if(avtpls_COMMAND.size() == 0){
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "COMMAND1");
			if(sys_avtpl != null){
				sys_avtpl.setUsageType(AvtplUsageType.COMMAND);
				newAvtpls.add(sys_avtpl);
			}
		}
		
		List<AvtplPO> avtpls_STB = avtplDao.findByUsageType(AvtplUsageType.STB);
		if(avtpls_STB.size() == 0){
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "STB1");
			if(sys_avtpl != null){
				sys_avtpl.setUsageType(AvtplUsageType.STB);
				newAvtpls.add(sys_avtpl);
			}
		}
		
		List<AvtplPO> avtpls_MOBILE = avtplDao.findByUsageType(AvtplUsageType.MOBILE);
		if(avtpls_MOBILE.size() == 0){
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "MOBILE1");
			if(sys_avtpl != null){
				sys_avtpl.setUsageType(AvtplUsageType.MOBILE);
				newAvtpls.add(sys_avtpl);
			}
		}
		
		avtplDao.save(newAvtpls);
	}
	
}
