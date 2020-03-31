package com.sumavision.tetris.bvc.cascade;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.bo.DeviceInfoBO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.cascade.templates.TemplateLoader;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import freemarker.template.Template;

/**
 * 业务调用联网同步指挥业务信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午7:32:10
 */
@Component
public class CommandCascadeService {
	
	public static final String NO_TYPE_ALL = "all";
	public static final String NO_TYPE_DEVICE = "device";
	public static final String NO_TYPE_USER = "user";
	public static final String NO_TYPE_APP = "app";

	@Autowired
	private TemplateLoader templateLoader;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;
	
	@Autowired
	private ResourceRemoteService  resourceRemoteService;
	
	public CommandCascadeService(TemplateLoader templateLoader){
		this.templateLoader = templateLoader;
	}
	
	public static void main(String[] args) throws Exception{
		GroupBO group = new GroupBO();
		group.setGid("gid")
			 .setSubject("subject")
			 .setBiztype("biztype")
			 .setCreatorid("creatorid")
			 .setTopid("topid")
			 .setMlist(new ArrayListWrapper<MinfoBO>().add(new MinfoBO().setMid("mid1")
					 													.setMname("mname1")
					 													.setMstatus("mstatus1")
					 													.setMtype("mtype1")
					 													.setPid("pid1"))
					 								  .add(new MinfoBO().setMid("mid2")
					 													.setMname("mname2")
					 													.setMstatus("mstatus2")
					 													.setMtype("mtype2")
					 													.setPid("pid2"))
					 													.getList());
		TemplateLoader templateLoader = new TemplateLoader();
		CommandCascadeService service = new CommandCascadeService(templateLoader);
		//service.create(group);
		service.delete(group);
	}
	
	/**
	 * 生成模板全名<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月31日 上午9:05:41
	 * @param String fileName 模板名称
	 * @return String 模板全名
	 */
	private String generateFullName(String fileName) throws Exception{
		return new StringBufferWrapper().append("com/sumavision/tetris/bvc/cascade/templates/cmd/")
										.append(fileName)
										.toString();
	}
	
	/**
	 * 发送passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月31日 上午11:49:52
	 * @param String src_user 操作用户号码
	 * @param String type all, device, user, app
	 * @param String protocol xml协议
	 * @param List<MinfoBO> mlist 成员列表
	 */
	private void sendPassBy(
			String src_user,
			String type,
			String protocol,
			List<MinfoBO> mlist) throws Exception{
		
		System.out.println(protocol);
		
		//查询本联网layerid
		String localLayerId = resourceRemoteService.queryLocalLayerId();
		
		//根据用户号码或设备号码查询隶属应用号码列表（过滤本应用）
		List<DeviceInfoBO> devices = new ArrayList<DeviceInfoBO>();
		for(MinfoBO m:mlist){
			DeviceInfoBO device = new DeviceInfoBO();
			device.setCode(m.getMid());
			device.setType(m.getMtype().equals("usr")?"user":"device");
		}
		List<String> dstnos = resourceRemoteService.querySerNodeList(devices);
		
		JSONObject passbyContent = new JSONObject();
		passbyContent.put("cmd", "send_node_message");
		passbyContent.put("src_user", src_user);
		passbyContent.put("type", type);
		passbyContent.put("dst_no", dstnos);
		passbyContent.put("content", protocol);
		
		PassByBO passBy = new PassByBO().setLayer_id(localLayerId)
										.setPass_by_content(passbyContent);
		tetrisDispatchService.passby(new ArrayListWrapper<PassByBO>().add(passBy).getList());
	}
	
	/**
	 * 创建指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:32:54
	 * @param String gid 组id
	 * @param String subject 会议主题
	 * @param String biztype 会议名称
	 * @param String creatorid 创建者号码
	 * @param String topid 指挥：最上级id，会议：主席id
	 * @param List<MinfoBO> mlist 成员列表
	 */
	public void create(GroupBO group) throws Exception{
		String fullName = generateFullName("createCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 删除指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:35:17
	 * @param String gid 组id
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void delete(GroupBO group) throws Exception{
		String fullName = generateFullName("deleteCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 开始指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
 	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String subject 会议主题
	 * @param String stime 开始时间 yyyy-MM-dd hh:mm:ss
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void start(GroupBO group) throws Exception{
		String fullName = generateFullName("startCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 停止指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void stop(GroupBO group) throws Exception{
		String fullName = generateFullName("stopCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 暂停指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void pause(GroupBO group) throws Exception{
		String fullName = generateFullName("pauseCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 恢复指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员） 
	 */
	public void resume(GroupBO group) throws Exception{
		String fullName = generateFullName("resumeCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 进入指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mAddList 进入会议成员列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void join(GroupBO group) throws Exception{
		String fullName = generateFullName("memberEnterCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 退出指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 退出成员号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void exit(GroupBO group) throws Exception{
		String fullName = generateFullName("memberExitCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 踢出成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 被踢出成员号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void kikout(GroupBO group) throws Exception{
		String fullName = generateFullName("memberKikoutCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 开启协同指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<String> croplist 协同指挥成员号码列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void startCooperation(GroupBO group) throws Exception{
		String fullName = generateFullName("cooperationCmdStart.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 关闭协同指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<String> croplist 协同指挥成员号码列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void stopCooperation(GroupBO group) throws Exception{
		String fullName = generateFullName("cooperationCmdStop.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 开启设备转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<String> mediaForwardMlist 媒体转发目标成员号码列表
	 * @param List<String> medialist 媒体id列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void startDeviceForward(GroupBO group) throws Exception{
		String fullName = generateFullName("mediaTransmitStartInCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
	/**
	 * 停止设备转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<String> mediaForwardMlist 媒体转发目标成员号码列表
	 * @param List<String> medialist 媒体id列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void stopDeviceForward(GroupBO group) throws Exception{
		String fullName = generateFullName("mediaTransmitStopInCmd.xml");
		Template template = templateLoader.load(fullName);
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		sendPassBy(group.getOp(), NO_TYPE_APP, protocol, group.getMlist());
	}
	
}