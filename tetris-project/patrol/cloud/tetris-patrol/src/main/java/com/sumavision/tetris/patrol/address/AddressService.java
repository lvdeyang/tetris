package com.sumavision.tetris.patrol.address;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.patrol.config.server.ServerProps;
import com.sumavision.tetris.patrol.sign.SignDAO;
import com.sumavision.tetris.patrol.sign.SignPO;

@Service
public class AddressService {

	@Autowired
	private AddressDAO addressDao;
	
	@Autowired
	private SignDAO signDao;
	
	@Autowired
	private ServerProps serverProps;
	
	/**
	 * 添加地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:05:38
	 * @param String name 地址名称
	 * @return AddressVO 地址
	 */
	public AddressVO add(String name) throws Exception{
		AddressPO address = new AddressPO();
		address.setName(name);
		address.setUpdateTime(new Date());
		addressDao.save(address);
		return new AddressVO().set(address)
							  .setUrl(new StringBufferWrapper().append("http://")
			                  //.append(serverProps.getIp())
							  .append("124.126.224.49")
			                  .append(":")
			                  .append(serverProps.getPort())
			                  .append("/sign/sign/page/")
			                  .append(address.getUuid())
			                  .toString());
	}
	
	/**
	 * 修改地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:09:22
	 * @param Long id 地址id
	 * @param String name 地址名称
	 * @return AddressVO 地址
	 */
	public AddressVO edit(Long id, String name) throws Exception{
		AddressPO address = addressDao.findOne(id);
		address.setName(name);
		addressDao.save(address);
		return new AddressVO().set(address)
							  .setUrl(new StringBufferWrapper().append("http://")
							  //.append(serverProps.getIp())
							  .append("124.126.224.49")	  
			                  .append(":")
			                  .append(serverProps.getPort())
			                  .append("/sign/sign/page/")
			                  .append(address.getUuid())
			                  .toString());
	}
	
	/**
	 * 删除地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:10:36
	 * @param Long id 地址id
	 * @param Boolean deleteSigns 是否删除签到信息
	 */
	public void delete(Long id, Boolean deleteSigns) throws Exception{
		AddressPO address = addressDao.findOne(id);
		if(address == null) return;
		addressDao.delete(address);
		if(deleteSigns){
			List<SignPO> signs = signDao.findByAddressId(id);
			if(signs!=null && signs.size()>0){
				signDao.deleteInBatch(signs);
			}
		}
	}
	
}
