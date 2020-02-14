package com.sumavision.tetris.omms.hardware.server;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ServerService {

	@Autowired
	private ServerDAO serverDao;
	
	/**
	 * 添加一个服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:54:40
	 * @param String name 名称
	 * @param String ip ip地址
	 * @param String gadgetPort 小工具端口
	 * @param String remark 备注
	 * @param String creator 创建者
	 * @param String createTime 创建时间
	 * @return ServerPO 服务器
	 */
	public ServerPO add(
			String name,
			String ip,
			String gadgetPort,
			String remark,
			String creator,
			Date createTime) throws Exception{
		ServerPO entity = new ServerPO();
		entity.setName(name);
		entity.setIp(ip);
		entity.setGadgetPort(gadgetPort);
		entity.setRemark(remark);
		entity.setCreator(creator);
		entity.setCreateTime(createTime);
		entity.setUpdateTime(new Date());
		entity.setStatus(ServerStatus.OFFLINE);
		serverDao.save(entity);
		return entity;
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
	 * @param String remark 备注
	 * @param String creator 创建者
	 * @return ServerPO 服务器
	 */
	public ServerPO edit(
			Long id,
			String name,
			String ip,
			String gadgetPort,
			String remark,
			String creator) throws Exception{
		ServerPO entity = serverDao.findOne(id);
		if(entity != null){
			entity.setName(name);
			entity.setIp(ip);
			entity.setGadgetPort(gadgetPort);
			entity.setRemark(remark);
			entity.setCreator(creator);
			entity.setUpdateTime(new Date());
			serverDao.save(entity);
		}
		return entity;
	}
	
	/**
	 * 删除一个服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:59:35
	 * @param Long id 服务器id
	 * @return ServerPO 删除的服务器
	 */
	public ServerPO delete(Long id) throws Exception{
		ServerPO entity = serverDao.findOne(id);
		if(entity != null){
			serverDao.delete(entity);
		}
		return entity;
	}
	
}
