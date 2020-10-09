package com.sumavision.tetris.patrol.sign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.patrol.address.AddressDAO;
import com.sumavision.tetris.patrol.address.AddressPO;
import com.sumavision.tetris.patrol.address.exception.AddressNotFoundException;
import com.sumavision.tetris.patrol.sign.exception.NameCannotBeNullInSignException;
import com.sumavision.tetris.patrol.sign.exception.PhoneCannotBeNullInSignException;
import com.sumavision.tetris.patrol.sign.exception.PhoneFormatILegalException;

@Service
public class SignService {

	@Autowired
	private SignDAO signDao;
	
	@Autowired
	private AddressDAO addressDao;
	
	/**
	 * 用户签到<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:41:12
	 * @param String addressUuid 地址uuid
	 * @param String name 用户姓名
	 * @param String phone 手机号码
	 */
	public void add(
			String addressUuid, 
			String name, 
			String phone) throws Exception{
		
		AddressPO address = addressDao.findByUuid(addressUuid);
		if(address == null){
			throw new AddressNotFoundException(addressUuid);
		}
		
		if(name==null || "".equals(name)) throw new NameCannotBeNullInSignException();
		if(phone==null || "".equals(phone)) throw new PhoneCannotBeNullInSignException();
		if(!PhoneCheck.isPhoneLegal(phone)) throw new PhoneFormatILegalException();
		
		Date now = new Date();
		SignPO sign = new SignPO();
		sign.setUpdateTime(now);
		sign.setSignTime(now);
		sign.setName(name);
		sign.setPhone(phone);
		sign.setAddressId(address.getId());
		signDao.save(sign);
	}
	
	/**
	 * 删除签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:43:02
	 * @param Long id 签到信息id
	 */
	public void delete(Long id) throws Exception{
		SignPO sign = signDao.findOne(id);
		if(sign != null){
			signDao.delete(sign);
		}
	}
	
}
