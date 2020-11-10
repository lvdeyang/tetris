package com.suma.venus.resource.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.service.OutlandService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/outland")
public class OutlandController extends ControllerBase{
	
	@Autowired
	private OutlandService outlandService;
	
	/**
	 * 创建本域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午4:38:02
	 * @param name 本域名称
	 * @param password 本域口令
	 */
	public Object createrInland(String name,String password)throws Exception{
		
		return outlandService.createrInland(name, password);
	}
	/**
	 * 修改本域名称及口令<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:15:58
	 * @param name 名称
	 * @param password 口令
	 * @return SerNodeVO 本域节点信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/inland")
	public Object inland(String name,String password)throws Exception{
		return outlandService.inland(name, password);
	}
	
	/**
	 * 查询外域信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:21:51
	 * @return List<SerNodeVO> 外域信息列表
	 */
	public Object queryOutland()throws Exception{
		return outlandService.queryOutland();
	}
	
	/**
	 * 创建外域<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午3:10:25
	 * @param name 外域名称
	 * @param password 外域口令
	 * @param roleIds 外域绑定的角色id
	 * @return data(成功时返回外域名称，失败时返回错误信息)
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/outland")
	public Object addOutland(String name,String password,List<Long> roleIds)throws Exception{
		
		return outlandService.addOutland(name, password, roleIds);
	}

	/**
	 * 查询外域设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午4:14:28
	 * @param Long serNodeId 外域id
	 * @return 
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "query/outland/bundle")
	public Object queryOutlandBundle(Long serNodeId)throws Exception{
		
		return outlandService.queryOutlandBundle(serNodeId);
	}
	
	/**
	 * 断开外域连接<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午5:13:47
	 * @param id 外域id
	 * @return SerNodeVO 外域信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/outland/off")
	public Object outlandOff (Long id)throws Exception{
		return outlandService.outlandOff(id);
	}
	
	
	/**
	 * 修改外域信息（口令、外域名称）<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:08:52
	 * @param id 外域id
	 * @param name 外域新名称
	 * @param password 外域口令
	 * @return serNodeVO 外域信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/outland/change")
	public Object outlandChange(Long id,String name,String password)throws Exception{
		return outlandService.outlandChange(id,name,password);
	}

	/**
	 * 连接外域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:21:39
	 * @param id 外域id
	 * @return serNodeVO 外域信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/outland/on")
	public Object outlandOn (Long id)throws Exception{
		return outlandService.outlandOn(id);
	}
}
