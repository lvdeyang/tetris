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
}
