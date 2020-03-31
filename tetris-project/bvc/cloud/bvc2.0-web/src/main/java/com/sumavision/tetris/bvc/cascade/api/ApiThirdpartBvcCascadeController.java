package com.sumavision.tetris.bvc.cascade.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/hirdpart/bvc/cascade")
public class ApiThirdpartBvcCascadeController {
	
	/**
	 * 查询服务节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午5:53:38
	 * @param layer_id 自己的接入id
	 * @return 
	 * {
	 *		layer_id:String(自己的接入id)
	 *		self:{ //本节点的信息
	 *			app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址)
	 *		},
	 *		supers:[{ //上级节点的信息
	 *			app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址),
	 *			is_publish:bool(是否发布信息，最多只有一个子节点可以为true)
	 *		}],
	 *		subors:[{ //下级节点的信息
	 *		app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址)
	 *		}],
	 *		relations:[{ //关联节点的信息
	 *			app_code:String(应用的号码),
	 *			sig_code:String(信令的号码),
	 *			sip_addr:String(192.165.56.66:5060，信令的地址)
	 *		}]
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/node/info")
	public Object queryNodeInfo(HttpServletRequest request) throws Exception{
		
		return null;
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午6:01:16
	 * @param seq:String(命令标识号),
	 * @param no:String(待查询的号码),
	 * @param type:String(all, device, user, app)
	 * @return
	 * {
	 *	   seq:String(命令标识号，与请求相同),
	 *	   no:String(待查询的号码),
	 *	   next_sip_no:String(下一跳的信令号码,应该在上级、下级、关联之中)
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/next/node")
	public Object queryNextNode(HttpServletRequest request) throws Exception{
		
		return null;
	}
	
	
	/**
	 * 级联业务信息转换<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 上午11:54:01
	 * @param  src_user 源的用户号码
	 * @param content xml
	 * @return 
	 * {
	 *	   status:200,
	 *	   message:"",
	 *	   data:{
	 *	       content:xml
	 * 	   }
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/conversion/information")
	public Object conversionInformation(HttpServletRequest request) throws Exception{
		
		return null;
	}
	
	/**
	 * 开启/停止一个跨节点点播/呼叫业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午6:32:51
	 * @param uuid:String
	 * @param operate:String(start; stop)
	 * @param src_userno:String(发起方的用户号码)
	 * @param dst_no:String(被叫的号码)
	 * @param type:String(play,call,play-call)
	 * @return
	 * {
	 * 	   status:200,
	 * 	   message:""
	 * }
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/streaming")
	public Object streaming(HttpServletRequest request) throws Exception{
		
		return null;
	}
	
}
