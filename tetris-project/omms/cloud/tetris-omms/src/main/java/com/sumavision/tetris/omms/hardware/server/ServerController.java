package com.sumavision.tetris.omms.hardware.server;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;

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
			String gadgetPort,
			String gadgetUsername,
			String gadgetPassword,
			String remark,
			String creator,
			String ftpUsername,
			String ftpPort,
			String ftpPassword,
			HttpServletRequest request) throws Exception{
		ServerPO entity = serverService.edit(id, name, gadgetPort, gadgetUsername, gadgetPassword, remark, creator,ftpUsername,ftpPort,ftpPassword);
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
	
	/**
	 * 
	 * 根据服务器id查询数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午5:26:07
	 * @param serverId 服务器id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/findDatabase")
	public Object findDatabase(
			Long serverId,
			HttpServletRequest request) throws Exception{
		
		return serverQuery.findDatabase(serverId);
	}
	
	/**
	 * 
	 * 删除数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午5:55:53
	 * @param databaseId 数据库id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/deleteDatabase")
	public Object deleteDatabase(
			Long databaseId,
			HttpServletRequest request) throws Exception{
		
		serverService.deleteDatabase(databaseId);
		return null;
	}
	
	/**
	 * 
	 * 添加数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午7:13:37
	 * @param serverId 服务器id
	 * @param databaseIP 数据库IP
	 * @param databasePort 数据库端口
	 * @param username 用户名
	 * @param password 密码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addDatabase")
	public Object addDatabase(
			Long serverId,  
			String databaseIp,
			String databasePort, 
			String databaseName,
			String username, 
			String password,
			HttpServletRequest request) throws Exception{
		
		return serverService.addDatabase(serverId, databaseIp, databasePort, databaseName, username, password);
		
	}
	
	/**
	 * 
	 * 查询所有数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 下午2:05:38
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/findAllDatabase")
	public Object findAllDatabase(HttpServletRequest request) throws Exception{
		
		return serverQuery.findAllDatabase();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/importauth")
	public Object importdev(HttpServletRequest request) throws Exception{
		MultipartHttpServletRequestWrapper requestWrapper = new MultipartHttpServletRequestWrapper(request, -1);
		Long id = requestWrapper.getLong("id");
		FileItem authFile = requestWrapper.getFileItem("authFile");
		return serverService.importAuth(id, authFile);
	}
	
	/**
	 * 获取设备唯一标识<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月16日 上午10:50:18
	 * @param id 服务器id
	 * @param request 
	 * @return deviceId.ini 设备标识文件
	 */
	@RequestMapping(value = "/export/{id}")
	public ResponseEntity<byte[]> handleExport(@PathVariable Long id,HttpServletRequest request) throws Exception{
		//String deviceId="xydkk09033kkkdadcccxsd0993d";
		//对接小工具获取设备唯一标识
		String deviceId = serverService.exportDeviceid(id);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8")).toString());
        headers.add("Content-Disposition", "attchement;filename=auth("+deviceId+").csv");
        byte[] fileBytes = deviceId.getBytes();
        byte[] utf8Bytes = new byte[fileBytes.length+3];
        utf8Bytes[0] = (byte)0xEF;
        utf8Bytes[1] = (byte)0xBB;
        utf8Bytes[2] = (byte)0xBF;
        for(int i=0; i<fileBytes.length; i++){
        	utf8Bytes[i+3] = fileBytes[i];
        }
        return new ResponseEntity<byte[]>(utf8Bytes, headers, HttpStatus.OK);
	}
	
	/**
	 * 查询和开启告警<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月26日 下午1:47:58
	 * @return ServerAlarmVO cpu、内存、硬盘使用告警限制，默认80、80、80
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/limit/rate")
	public Object queryLimitRate()throws Exception{
		
		return serverQuery.queryLimitRate();
	}
	
	/**
	 * 修改CPU、内存、硬盘告警限制<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月26日 下午2:02:44
	 * @param cpuRate CPU告警限度
	 * @param memoryRate 内存告警限度
	 * @param diskRate 硬盘告警限度
	 * @return ServerAlarmVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/limit/rate")
	public Object editLimitRate(HttpServletRequest request)throws Exception{
		
		MultipartHttpServletRequestWrapper requestWrapper = new MultipartHttpServletRequestWrapper(request, -1);
		Long cpuRate = requestWrapper.getLong("cpuRate");
		Long memoryRate = requestWrapper.getLong("memoryRate");
		Long diskRate = requestWrapper.getLong("diskRate");
		Long processCpu = requestWrapper.getLong("processCpu");
		
		return serverService.editLimitRate(cpuRate, memoryRate, diskRate, processCpu);
	}
	
	/**
	 * 查询存在告警信息的服务器数据<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:00:18
	 * @param serverId 服务器id
	 * @return List<ServerOneDimensionalDataVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/show/alarm/message")
	public Object showAlarmMessage(Long serverId)throws Exception{
		
		return serverQuery.showAlarmMessage(serverId);
		
	}
	
	/**
	 * 查询具体的进程CPU占用率<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午6:20:27
	 * @param dataId 服务器数据id
	 * @return List<ServerProcessUsageVO> 进程的详细数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/show/alarm/details")
	public Object showAlarmDetails(Long dataId)throws Exception{
		
		return serverQuery.showAlarmDetails(dataId);
	}
	
	/**
	 * 删除告警信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午7:55:04
	 * @param serverId 服务器id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/alarm/message")
	public Object deleteAlarmMessage(Long serverId)throws Exception{
		
		return serverService.deleteAlarmMessage(serverId);
	}
}
