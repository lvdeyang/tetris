package com.sumavision.bvc.device.monitor.vod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.monitor.vod.exception.ExternalFolderDonnotExistException;
import com.sumavision.bvc.device.monitor.vod.exception.UserHasNoPermissionForExternalFolderException;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorExternalStaticResourceFolderService{
	
	@Autowired
	private MonitorExternalStaticResourceFolderDAO monitorExternalStaticResourceFolderDao;
	
	/**
	 * 添加一个外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:34:01
	 * @param String name 别名
	 * @param String ip ip
	 * @param String port 端口
	 * @param String folderPath 目录结构
	 * @param String protocolType 协议类型
	 * @param String username 用户名
	 * @param String password 密码
	 * @param Long createUserId 创建用户id
	 * @param String createUsername 创建用户名
	 * @return MonitorExternalStaticResourceFolderPO 外部文件夹
	 */
	public MonitorExternalStaticResourceFolderPO add(
			String name,
			String ip,
			String port,
			String folderPath,
			String protocolType,
			String username,
			String password,
			Long createUserId,
			String createUsername) throws Exception{
		
		if(!folderPath.startsWith("/")) folderPath = new StringBufferWrapper().append("/").append(folderPath).toString();
		
		MonitorExternalStaticResourceFolderPO folder = new MonitorExternalStaticResourceFolderPO();
		folder.setName(name);
		folder.setIp(ip);
		folder.setPort(port);
		folder.setFolderPath(folderPath);
		folder.setProtocolType(ProtocolType.fromName(protocolType));
		folder.setUsername(username);
		folder.setPassword(password);
		folder.setCreateUserId(createUserId.toString());
		folder.setCreateUsername(createUsername);
		monitorExternalStaticResourceFolderDao.save(folder);
		
		return folder;
	} 
	
	/**
	 * 修改外部文件夹<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:44:58
	 * @param Long id 文件夹id
	 * @param String name 别名
	 * @param String ip ip
	 * @param String port 端口
	 * @param String folderPath 目录结构
	 * @param String protocolType 协议类型
	 * @param String username 用户名
	 * @param String password 密码
	 * @param Long createUserId 创建用户
	 * @return MonitorExternalStaticResourceFolderPO 文件夹
	 */
	public MonitorExternalStaticResourceFolderPO edit(
			Long id,
			String name,
			String ip,
			String port,
			String folderPath,
			String protocolType,
			String username,
			String password,
			Long createUserId) throws Exception{
		
		MonitorExternalStaticResourceFolderPO folder = monitorExternalStaticResourceFolderDao.findOne(id);
		if(folder == null){
			throw new ExternalFolderDonnotExistException(id);
		}
		if(!folder.getCreateUserId().equals(createUserId.toString())){
			throw new UserHasNoPermissionForExternalFolderException();
		}
		if(folderPath==null || "".equals(folderPath)) folderPath = "/";
		if(!folderPath.startsWith("/")) folderPath = new StringBufferWrapper().append("/").append(folderPath).toString();
		folder.setName(name);
		folder.setIp(ip);
		folder.setPort(port);
		folder.setFolderPath(folderPath);
		folder.setProtocolType(ProtocolType.fromName(protocolType));
		folder.setUsername(username);
		folder.setPassword(password);
		monitorExternalStaticResourceFolderDao.save(folder);
		
		return folder;
	}
	
	/**
	 * 删除外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:46:16
	 * @param Long id 文件夹id
	 * @param Long createUserId 用户id
	 */
	public void remove(
			Long id, 
			Long createUserId) throws Exception{
		
		MonitorExternalStaticResourceFolderPO folder = monitorExternalStaticResourceFolderDao.findOne(id);
		if(folder == null) return;
		if(!folder.getCreateUserId().equals(createUserId.toString())){
			throw new UserHasNoPermissionForExternalFolderException();
		}
		
		monitorExternalStaticResourceFolderDao.delete(folder);
	}
	
}
