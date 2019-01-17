package com.sumavision.tetris.mims.app.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Component
public class UserQuery {

	/**
	 * 获取当前登录用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午10:14:26
	 * @return UserVO 当前用户
	 */
	public UserVO current(){
		return new UserVO().setUuid("1")
						   .setName("用户1")
						   .setClassify(UserClassify.MAINTENANCE.toString())
						   .setIcon("")
						   .setGroupId(null)
						   .setGroupName(null);
	}
	
	/**
	 * 获取用户组下所有的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:53:11
	 * @param String groupId 用户组id
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> list(String groupId){
		return this.users;
	}
	
	/**
	 * 分页获取用户组下的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午5:24:19
	 * @param String groupId 用户组id
	 * @param Integer pageSize 每页数量
	 * @param Integer currentSize 当前页码
	 * @oaran Collection<String> except 例外用户
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> list(String groupId, Integer pageSize, Integer currentPage, Collection<String> except){
		if(except==null || except.size()<=0) return this.users;
		List<UserVO> users = this.users;
		List<UserVO> filtered = new ArrayList<UserVO>();
		for(UserVO user:users){
			if(!except.contains(user.getUuid())){
				filtered.add(user);
			}
		}
		return filtered;
	}
	
	/**
	 * 计算企业内部用户总数（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 下午2:19:03
	 * @param String groupId 企业id
	 * @param Collection<String> except 例外用户id列表
	 * @return Integer 总数
	 */
	public Integer count(String groupId, Collection<String> except){
		if(except==null || except.size()<=0) return count(groupId);
		List<UserVO> users = this.users;
		int count = 0;
		for(UserVO user:users){
			if(!except.contains(user.getUuid())){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 获取企业内用户总数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午5:26:09
	 * @param String groupId 用户组id
	 * @return Integer 用户数量
	 */
	public Integer count(String groupId){
		return this.users.size();
	}
	
	/**
	 * 根据userId查找用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午6:20:07
	 * @param Collection<String> userIds 用户id列表
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> find(Collection<String> userIds){
		List<UserVO> users = this.users;
		List<UserVO> findUsers = new ArrayList<UserVO>();
		for(String userId:userIds){
			for(UserVO user:users){
				if(user.getUuid().equals(userId)){
					findUsers.add(user);
					break;
				}
			}
		}
		return findUsers;
	}
	
	/**
	 * 根据userId分页查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 上午10:08:22
	 * @param Collection<String> userIds 用户id列表
	 * @param Integer pageSize 每页数据量
	 * @param Integer currentPage 当前页码
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> find(Collection<String> userIds, Integer pageSize, Integer currentPage){
		return find(userIds);
	}
	
	/** 测试数据 */
	private List<UserVO> users = new ArrayListWrapper<UserVO>().add(new UserVO().setUuid("1")
																        .setName("用户1")
																        .setStatus(UserStatus.ONLINE.getName())
																        .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("2")
															            .setName("用户2")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("3")
															            .setName("用户3")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("4")
														                .setName("用户4")
															            .setStatus(UserStatus.ONLINE.getName())
															            .setNumbersOfMessage(13))
														.add(new UserVO().setUuid("5")
															            .setName("用户5")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("6")
														                .setName("用户6")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(2))
														.getList();
	
}
