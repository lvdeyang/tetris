package com.sumavision.bvc.control.zk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/router")
public class RouterController {

	/**
	 * 导航头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/header")
	public ModelAndView zkHeader(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/header/zk-header");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/footer")
	public ModelAndView zkFooter(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/footer/zk-footer");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚保存方案弹框<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/footer/dialog/save/scheme")
	public ModelAndView zkFooterDialogSaveScheme(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/footer/dialog/save-scheme/zk-footer-dialog-save-scheme");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚切换方案弹框<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/footer/dialog/change/scheme")
	public ModelAndView zkFooterDialogChangeScheme(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/footer/dialog/change-scheme/zk-footer-dialog-change-scheme");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚关联设备弹框<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/footer/dialog/cast/devices")
	public ModelAndView zkFooterDialogCastDevices(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/footer/dialog/cast-devices/zk-footer-dialog-cast-devices");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚云台控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/footer/dialog/cloud/control")
	public ModelAndView zkFooterDialogCloudControl(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/footer/dialog/cloud-control/zk-footer-dialog-cloud-control");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 业务模块,tian<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/business")
	public ModelAndView zkBusiness(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/business/zk-business");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 业务模块向组中添加用户成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/business/add/member")
	public ModelAndView zkBusinessAddMember(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/business/add-member/zk-business-add-member");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 业务模块协同指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/business/cooperation")
	public ModelAndView zkBusinessCooperation(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/business/cooperation/zk-business-cooperation");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 业务模块指挥转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/business/forward")
	public ModelAndView zkBusinessForward(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/business/forward/zk-business-forward");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 业务模块发送通知<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/business/command/message")
	public ModelAndView zkBusinessCommandMessage(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/business/command-message/zk-business-command-message");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 消息模块<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/message")
	public ModelAndView zkMessage(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/message/zk-message");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 设置模块<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/settings")
	public ModelAndView zkSettings(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/settings/zk-settings");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * qt sdk模块<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/hidden")
	public ModelAndView zkHidden(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk/hidden/zk-hidden");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  右侧菜单<br/>
	 * <b>作者:</b>yanxiaochao<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月5日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/index")
	public ModelAndView zkLeaderIndex(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/index/right-bar");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  底部消息<br/>
	 * <b>作者:</b>yanxiaochao<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/footer")
	public ModelAndView zkLeaderFooter(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/footer/history-message");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  历史消息弹框<br/>
	 * <b>作者:</b>yanxiaochao<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月9日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/footer/pop")
	public ModelAndView zkLeaderFooterPop(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/footer/popup/pop-message");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月9日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/footer/dialog")
	public ModelAndView zkLeaderFooterDialog(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/footer/dialog/dialog-message");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端 头部 <br/>
	 * <b>作者:</b>yanxiaochao<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月17日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/header")
	public ModelAndView zkLeaderHeader(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/header/zk-leader-header");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  设置模块<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/settings")
	public ModelAndView zkLeaderSettings(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/settings/leader-settings");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  添加成员模块<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/add/member")
	public ModelAndView zkLeaderAddMember(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/add-member/leader-add-member");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  协同指挥<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/cooperation")
	public ModelAndView zkLeaderCooperation(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/cooperation/leader-cooperation");
		mv.addObject("token", token);
		return mv;
	}
	
	
	/**
	 * 首长终端  指挥转发<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/forward")
	public ModelAndView zkLeaderForward(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/forward/leader-forward");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 首长终端  发送通知<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/send/message")
	public ModelAndView zkLeaderSendMessage(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/send-message/leader-send-message");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚关联设备弹框<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/cast/devices")
	public ModelAndView zkLeaderCastDevices(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/cast-devices/leader-cast-devices");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 上屏方案关联设备弹框<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/cast/decoder/devices")
	public ModelAndView zkLeaderCastDecoderDevices(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/cast-decoder-devices/leader-cast-decoder-devices");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚发布字幕弹框<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年04月26日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/publish/subtitle")
	public ModelAndView zkLeaderPublishSubtitle(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/publish-subtitle/publishSubtitle");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 页脚云台控制<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/cloud/control")
	public ModelAndView zkLeaderCloudControl(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/cloud-control/leader-cloud-control");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 视频界面 呼叫,专项，语音<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月27日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/callUser")
	public ModelAndView zkLeadercallUser(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/callUser/call-user");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 视频界面 点播<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月27日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/order")
	public ModelAndView zkLeaderOrder(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/order/leader-order");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 底部  转发关系<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月7日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/forward-relationship/forward-relation")
	public ModelAndView zkLeaderForwardShip(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/forward-relationship/forward-relation");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 底部  录制任务管理<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月28日 上午17:21:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/rec")
	public ModelAndView zkLeaderRec(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/recManage/recManage");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 头部  设置作战时间<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月16日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/set/fightTime")
	public ModelAndView zkLeaderFightTime(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/set-fightTime/fightTime");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 添加录制任务<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/addRecord")
	public ModelAndView zkLeaderaddRecord(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/addRecord/add-record");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 点播录制文件<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/record/file")
	public ModelAndView zkLeaderRecord(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/recordFile/record-file");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 外部文件管理<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/out/file")
	public ModelAndView zkLeaderOutFile(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/outFile/outFile");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 外部文件导入<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/file/import")
	public ModelAndView zkLeaderFileImport(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/fileImport/file-import");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 字幕管理<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/subtitle/manage")
	public ModelAndView zkLeaderSubtitle(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/subtitleManage/subtitle");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 字幕发布<br/>
	 * <b>作者:</b>yxc<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 上午9:37:54
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/subtitle/layer")
	public ModelAndView zkLeaderSubtitleLayer(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/subtitle-layer/subtitleLayer");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 选择成员创建会议对话框<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 上午11:12:50
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/dialog/create/group")
	public ModelAndView zkLeaderDialogCreateGroup(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/dialog/create-group/create-group");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 选择会议加入会议对话框<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 上午11:12:50
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/dialog/add/group")
	public ModelAndView zkLeaderDialogAddGroup(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/dialog/add-group/add-group");
		mv.addObject("token", token);
		return mv;
	}
	
	/**
	 * 选择成员安排会议对话框<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 上午11:12:50
	 * @param String token 用户登录token
	 */
	@RequestMapping(value = "/zk/leader/dialog/prepare/group")
	public ModelAndView zkLeaderDialogPrepareGroup(String token){
		ModelAndView mv = new ModelAndView("web/bvc/zk-leader/dialog/prepare-group/prepare-group");
		mv.addObject("token", token);
		return mv;
	}
	
}
