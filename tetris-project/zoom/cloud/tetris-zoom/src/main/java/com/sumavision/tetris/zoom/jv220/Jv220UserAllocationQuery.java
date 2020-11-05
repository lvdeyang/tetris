package com.sumavision.tetris.zoom.jv220;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Jv220UserAllocationQuery {

	@Autowired
	private Jv220UserAllocationDAO jv220UserAllocationDao;
	
	/**
	 * 判断用户是否分配给jv220使用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:12:59
	 * @param Long userId 用户id
	 * @return boolean 判断结果
	 */
	public boolean isJv220(Long userId){
		Jv220UserAllocationPO jv220UserAllocation = jv220UserAllocationDao.findOne(userId);
		if(jv220UserAllocation == null){
			return false;
		}
		return true;
	}
	
	/**
	 * 过滤分配给jv220使用的用户id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午2:12:59
	 * @param Collection<Long> userIds 用户id集合
	 * @return List<Long> 分配给jv220使用的用户id集合
	 */
	public List<Long> filterJv220(Collection<Long> userIds){
		List<Long> jv220UserIds = new ArrayList<Long>();
		if(userIds!=null && userIds.size()>0){
			List<Jv220UserAllocationPO> jv220UserAllocations = jv220UserAllocationDao.findByUserIdIn(userIds);
			if(jv220UserAllocations!=null && jv220UserAllocations.size()>0){
				for(Jv220UserAllocationPO jv220UserAllocation:jv220UserAllocations){
					jv220UserIds.add(jv220UserAllocation.getUserId());
				}
			}
		}
		return jv220UserIds;
	}
	
}
