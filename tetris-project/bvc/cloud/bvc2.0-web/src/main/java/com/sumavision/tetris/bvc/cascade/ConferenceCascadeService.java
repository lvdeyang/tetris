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
 * 业务调用联网同步会议业务信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午7:32:10
 */
@Component
public class ConferenceCascadeService {

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
	
	public ConferenceCascadeService(TemplateLoader templateLoader){
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
		ConferenceCascadeService service = new ConferenceCascadeService(templateLoader);
		service.create(group);
	}
	
	/**
	 * 发送passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月31日 上午11:49:52
	 * @param GroupBO group 业务数据
	 * @param String type all, device, user, app
	 * @param Template template xml协议模板
	 */
	private void sendPassBy(
			GroupBO group,
			String type,
			Template template) throws Exception{
		
		//根据用户号码或设备号码查询隶属应用号码列表（过滤本应用）
		List<MinfoBO> mlist = group.getMlist();
		List<DeviceInfoBO> devices = new ArrayList<DeviceInfoBO>();
		for(MinfoBO m:mlist){
			DeviceInfoBO device = new DeviceInfoBO();
			device.setCode(m.getMid());
			device.setType(m.getMtype().equals("usr")?"user":"device");
			devices.add(device);
		}
		List<String> dstnos = resourceRemoteService.querySerNodeList(devices);
		
		if(dstnos.size() == 0){
			return;
		}
		
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		System.out.println(protocol);
		
		//查询本联网layerid
		String localLayerId = resourceRemoteService.queryLocalLayerId();
		
		JSONObject passbyContent = new JSONObject();
		passbyContent.put("cmd", "send_node_message");
		passbyContent.put("src_user", group.getOp());
		passbyContent.put("type", type);
		passbyContent.put("dst_no", dstnos);
		passbyContent.put("content", protocol);
		
		PassByBO passBy = new PassByBO().setLayer_id(localLayerId)
										.setPass_by_content(passbyContent);
		tetrisDispatchService.passby(new ArrayListWrapper<PassByBO>().add(passBy).getList());
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
		return new StringBufferWrapper().append("com/sumavision/tetris/bvc/cascade/templates/cnf/")
										.append(fileName)
										.toString();
	}
	
	/**
	 * 创建会议<br/>
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
		String fullName = generateFullName("createCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 删除会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:35:17
	 * @param String gid 组id
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void delete(GroupBO group) throws Exception{
		String fullName = generateFullName("deleteCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 开始会议<br/>
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
		String fullName = generateFullName("startCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 停止会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void stop(GroupBO group) throws Exception{
		String fullName = generateFullName("stopCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 暂停会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void pause(GroupBO group) throws Exception{
		String fullName = generateFullName("pauseCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 恢复会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void resume(GroupBO group) throws Exception{
		String fullName = generateFullName("resumeCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 进入会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mAddList 进入会议成员列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void join(GroupBO group) throws Exception{
		String fullName = generateFullName("memberEnterCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 退出会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 退出成员号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void exit(GroupBO group) throws Exception{
		String fullName = generateFullName("memberExitCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
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
		String fullName = generateFullName("memberKikoutCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
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
		String fullName = generateFullName("mediaTransmitStartInCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
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
		String fullName = generateFullName("mediaTransmitStopInCnf.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 主席指定发言<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 发言人号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void speakerSetByChairman(GroupBO group) throws Exception{
		String fullName = generateFullName("speakerSetByChairmanNotice.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 取消发言<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 发言人号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void speakerSetCancel(GroupBO group) throws Exception{
		String fullName = generateFullName("speakerSetCancel.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 申请发言<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 发言人号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（主席信息）
	 */
	public void speakerSetRequest(GroupBO group) throws Exception{
		String fullName = generateFullName("speakerSetRequest.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 申请发言响应<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:37:10
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 发言人号码
	 * @param String code 响应，0表示不同意、1表示同意 
	 * @param List<MinfoBO> mlist 需要级联的成员信息（发言人成员信息）
	 */
	public void speakerSetResponse(GroupBO group) throws Exception{
		String fullName = generateFullName("speakerSetResponse.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
}