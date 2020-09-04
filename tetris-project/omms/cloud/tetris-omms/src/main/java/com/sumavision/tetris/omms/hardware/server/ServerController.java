package com.sumavision.tetris.omms.hardware.server;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/server")
public class ServerController {

	@Autowired
	private ServerQuery serverQuery;
	
	@Autowired
	private ServerDAO serverDao;
	
	@Autowired
	private ServerService serverService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request)throws Exception{
		
		long total = serverDao.count();
		
		List<ServerVO> servers = serverQuery.load(currentPage, pageSize);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", servers)
												   .getMap();
	}
	
	/**
	 * 添加一个服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:54:40
	 * @param String name 名称
	 * @param String ip ip地址
	 * @param String gadgetPort 小工具端口
	 * @param String gadgetUsername 小工具用户名
	 * @param String gadgetPassword 小工具密码
	 * @param String remark 备注
	 * @param String creator 创建者
	 * @return ServerVO 服务器
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String ip,
			String gadgetPort,
			String gadgetUsername,
			String gadgetPassword,
			String remark,
			String creator,
			String ftpUsername,
			String ftpPort,
			String ftpPassword,
			HttpServletRequest request) throws Exception{
		ServerPO entity = serverService.add(name, ip, gadgetPort, gadgetUsername, gadgetPassword, remark, creator, new Date(),ftpUsername,ftpPort,ftpPassword);
		return new ServerVO().set(entity);
	}
	
	/**
	 * 修改服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:57:32
	 * @param Long id 服务器id
	 * @param String name 名称
	 * @param String ip ip地址
	 * @param String gadgetPort 小工具端口
	 * @param String gadgetUsername 小工具用户名
	 * @param String gadgetPassword 小工具密码
	 * @param String remark 备注
	 * @param String creator 创建者
	 * @return ServerVO 服务器
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String ip,
			String gadgetPort,
			String gadgetUsername,
			String gadgetPassword,
			String remark,
			String creator,
			String ftpUsername,
			String ftpPort,
			String ftpPassword,
			HttpServletRequest request) throws Exception{
		ServerPO entity = serverService.edit(id, name, ip, gadgetPort, gadgetUsername, gadgetPassword, remark, creator,ftpUsername,ftpPort,ftpPassword);
		return new ServerVO().set(entity);
	}
	
	/**
	 * 删除一个服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:59:35
	 * @param Long id 服务器id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		serverService.delete(id);
		return null;
	}
	
	/**
	 * 查询服务器状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午10:58:09
	 * @param Long id 服务器id
	 * @return server ServerVO 服务器
	 * @return oneDimensionalData ServerOneDimensionalDataVO 服务器以为数据信息
	 * @return disks List<ServerHardDiskDataVO> 硬盘信息列表
	 * @return networks List<ServerNetworkCardTrafficDataVO> 网卡信息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/status")
	public Object queryStatus(
			Long id,
			HttpServletRequest request) throws Exception{
		
		return serverQuery.queryStatus(id);
	}
	
	/**
	 * 修改ip<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午10:39:43
	 * @param Long id 服务器id
	 * @param String ip 修改的ip
	 * @return ServerVO 服务器
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/modify/ip")
	public Object modifyIp(
			Long id,
			String ip,
			HttpServletRequest request) throws Exception{
		
		return serverService.modifyIp(id, ip);
	}
	
}
