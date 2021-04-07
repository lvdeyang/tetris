package com.suma.venus.resource.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.service.OutlandService;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.bvc.business.query.QueryForeignInformation;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/outland")
public class OutlandController extends ControllerBase{
	
	@Autowired
	private OutlandService outlandService;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;
	
	@Autowired
	private QueryForeignInformation queryForeignInformation;
	
	/**
	 * 查询本域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午4:38:02
	 * SerNodeVO 本域信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/inland")
	public Object createrInland()throws Exception{
		
		return outlandService.queryInland();
	}
	/**
	 * 修改本域名称<br/>
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
	public Object inland(String name,String fakeName)throws Exception{
		return outlandService.inland(name, fakeName);
	}
	
	/**
	 * 查询外域信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:21:51
	 * @return List<SerNodeVO> 外域信息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/outland")
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
	 * @param extraInfoVOList 扩展参数
	 * @return data(成功时返回外域名称，失败时返回错误信息)
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/outland")
	public Object addOutland(String name, String password, String roleIds, String ip, String port, String extraInfoVOList, String fakeName)throws Exception{
		
		return outlandService.addOutland(name, password, roleIds, ip, port, extraInfoVOList, fakeName);
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
	public Object outlandOff (Long serNodeId)throws Exception{
		return outlandService.outlandOff(serNodeId);
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
	public Object outlandChange(Long serNodeId,String name,String password,String roleIds, String ip, String port, String extraInfoVOList,String fakeName)throws Exception{
		Map<String, Object> data =  outlandService.outlandChange(serNodeId, name, password, roleIds, ip, port, extraInfoVOList,fakeName);
		
		PassByBO passByBO = (PassByBO) data.get("passby");
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		System.out.println(JSON.toJSONString(passByBO));
		return data.get("serNodeVO");
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
	public Object outlandOn (Long serNodeId)throws Exception{
		return outlandService.outlandOn(serNodeId);
	}
	
	
	/**
	 * 删除外域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 上午10:59:22
	 * @param id 外域id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/outland/delete")
	public Object outlandDelete(Long serNodeId) throws Exception{
		return outlandService.outlandDelete(serNodeId);
	}
	
	/**
	 * 外域设备授权本域用户查询<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午3:15:36
	 * @param roleId 角色id
	 * @param serNodeId 外域id
	 * @param deviceModel 设备类型
	 * @param keyword 关键字
	 * @param folderId 目录id
	 * @param pageNum 页码
	 * @param countPerPage 每页数量
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/outland/bundle/privilege")
	public  Object queryOutlandBundlePrivilege(
			Long roleId,
			Long serNodeId, 
			String deviceModel, 
			String keyword, 
			Long folderId,
			String codec,
			int pageNum, 
			int countPerPage) throws Exception{
		
		return outlandService.queryOutlandBundlePrivilege(roleId, serNodeId, deviceModel, keyword, folderId, codec, pageNum, countPerPage);
	}
	
	/**
	 * 外域设备授权本域用户修改<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午5:11:29
	 * @param roleId 角色id
	 * @param preBundlePrivilege 修改前的权限状态 
	 * @param bundleprivilege 修改后的权限状态
	 * @return data 错误信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/submit/bundle/privilege")
	public Object submitBundlePrivilege(
			Long roleId, 
			String preBundlePrivilege, 
			String bundleprivilege)throws Exception{
		
		return outlandService.submitBundlePrivilege(roleId, preBundlePrivilege, bundleprivilege);
	}
	
	/**
	 * 获取ws地址<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月3日 下午2:46:13
	 * @return String ws地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/websocket/addr")
	public Object websocketAddr()throws Exception{
		
		return outlandService.websocketAddr();
	}
	
	/**
	 * 查询外域目录设备信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月3日 下午3:42:12
	 * @param String folderPath 目录路径
	 * @param String serNodeNamePath 外域路径
	 * @param String permissionType 管理页面查询：权限类型
	 * @param Long roleId 管理页面查询：角色id
	 * @param String childType 类型
	 * @param String uuid 页面的一个唯一标识 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "find/institution/tree/foreign/bundle")
	public Object queryForeignInformation(
			String folderPath,
			String serNodeNamePath,
			String permissionType,
			Long roleId,
			String childType,
			String uuid)throws Exception{
		
		return queryForeignInformation.stopLiveDevice(folderPath, serNodeNamePath, permissionType, roleId, childType, uuid);
	} 
}
