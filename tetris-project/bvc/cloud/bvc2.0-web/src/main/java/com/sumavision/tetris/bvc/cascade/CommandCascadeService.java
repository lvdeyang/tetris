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
		
		List<PassByBO> passbyBOs = new ArrayList<PassByBO>();
		for(String dstno : dstnos){
			JSONObject passbyContent = new JSONObject();
			passbyContent.put("cmd", "send_node_message");
			passbyContent.put("src_user", group.getOp());
			passbyContent.put("type", type);
			passbyContent.put("dst_no", dstno);
			passbyContent.put("content", protocol);
			
			PassByBO passBy = new PassByBO().setLayer_id(localLayerId)
											.setPass_by_content(passbyContent);
			passbyBOs.add(passBy);
		}
		
		tetrisDispatchService.passby(passbyBOs);
	}
	
	/**
	 * 【私有协议】发送即时消息<br/>
	 * <p>指挥和会议都用这个</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午2:11:46
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String subject 发送的消息内容
	 * @param List<MinfoBO> mlist 成员列表
	 */
	public void sendInstantMessage(GroupBO group) throws Exception{
		String fullName = generateFullName("sendMessage.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 【私有协议】静默<br/>
	 * <p>指挥、会议的对上对下、开始停止都用这个</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午2:46:04
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String biztype 指挥/会议 bizcmd/bizcnf
	 * @param String code 开始对上/停止对上/开始对下/停止对下 silencehigherstart/silencehigherstop/silencelowerstart/silencelowerstop
	 * @param List<MinfoBO> mlist 成员列表
	 */
	public void becomeSilence(GroupBO group) throws Exception{
		String fullName = generateFullName("becomeSilence.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}

	/**
	 * 创建指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:32:54
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String subject 会议主题
	 * @param String bizname 会议名称
	 * @param String creatorid 创建者号码
	 * @param String topid 指挥：最上级id，会议：主席id
	 * @param List<MinfoBO> mlist 成员列表
	 */
	public void create(GroupBO group) throws Exception{
		String fullName = generateFullName("createCmd.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 更新指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 上午11:06:30
	 * @param String gid 组id
	 * @param String subject 会议主题
	 * @param String bizname 会议名称
	 * @param String creatorid 创建者号码
	 * @param String topid 指挥：最上级id，会议：主席id
	 * @param List<MinfoBO> mAddList 指挥成员列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（想发谁就传谁吧）
	 */
	public void update(GroupBO group) throws Exception{
		String fullName = generateFullName("updateCmd.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 删除指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:35:17
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（全员）
	 */
	public void delete(GroupBO group) throws Exception{
		String fullName = generateFullName("deleteCmd.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 全量信息同步<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 上午10:47:14
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String subject 主题
	 * @param String stime 开始时间
	 * @param String bizname 指挥名称
	 * @param String creatorid 创建用户号码
	 * @param String topid 最高级用户号码
	 * @param List<MinfoBO> mAddList 指挥成员列表
	 * @param String status ZH状态，0表示正常业务、1表示暂停业务 
	 * @param AuthCommandBO authitem 授权指挥状态
	 * @param ReplaceCommandBO replaceitem 接替指挥状态
	 * @param List<SecretCommandBO> secretlist 专向指挥列表
	 * @param List<String> croplist 协同指挥列表
	 * @param List<CrossCommandBO> croslist 越级ZH列表
	 * @param List<MinfoBO> mlist 需要级联的成员信息（想发谁就传谁吧）
	 */
	public void info(GroupBO group) throws Exception{
		String fullName = generateFullName("cmdInfo.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 成员退出请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 上午9:54:58
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 可处理当前请求用户的号码
	 * @param List<MinfoBO> mlist 需要级联的成员信息（可处理当前请求的用户）
	 */
	public void exitRequest(GroupBO group) throws Exception{
		String fullName = generateFullName("memberExitCmdRequest.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	/**
	 * 成员退出请求响应<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 上午9:58:21
	 * @param String gid 组id
	 * @param String op 操作用户号码
	 * @param String mid 退出成员用户号码
	 * @param String code 响应，0表示不同意、1表示同意
	 * @param List<MinfoBO> mlist 需要级联的成员信息（退出的成员）
	 */
	public void exitResponse(GroupBO group) throws Exception{
		String fullName = generateFullName("memberExitCmdResponse.xml");
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
		String fullName = generateFullName("memberKikoutCmd.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
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
		sendPassBy(group, NO_TYPE_APP, template);
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
		String fullName = generateFullName("mediaTransmitStartInCmd.xml");
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
		String fullName = generateFullName("mediaTransmitStopInCmd.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
	
	
	/**
	 * 云台控制级联<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:02:18
	 * @param group GroupBO
	 * @throws Exception
	 */
	public void cloudControll(GroupBO group) throws Exception{
		String fullName = generateFullName("cloudControll.xml");
		Template template = templateLoader.load(fullName);
		sendPassBy(group, NO_TYPE_APP, template);
	}
}