package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageVO;

@Component
public class OperationPackageUserPermissionQuery {
	@Autowired
	private OperationPackageUserPermissionDAO operationPackageUserPermissionDAO;
	
	@Autowired
	private OperationPackageQuery operationPackageQuery;
	
	/**
	 * 根据套餐id获取绑定该套餐的用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午3:40:43
	 * @param Long packageId 套餐id
	 */
	public List<OperationPackageUserPermissionVO> queryByPackageId(Long packageId) throws Exception {
		List<OperationPackageUserPermissionPO> userPermissionPOs = operationPackageUserPermissionDAO.findByPackageId(packageId);
		return userPermissionPOs == null || userPermissionPOs.isEmpty()
				? new ArrayList<OperationPackageUserPermissionVO>()
						: OperationPackageUserPermissionVO.getConverter(OperationPackageUserPermissionVO.class)
						.convert(userPermissionPOs, OperationPackageUserPermissionVO.class);
	}
	
	/**
	 * 根据用户id获取用户套餐关联关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午4:43:37
	 * @param Long userId 用户id
	 * @return List<OperationPackageUserPermissionVO> 用户套餐关联关系
	 */
	public List<OperationPackageUserPermissionVO> queryPermissionByUserId(Long userId) throws Exception {
		List<OperationPackageUserPermissionPO> userPermissionPOs = operationPackageUserPermissionDAO.findByUserId(userId);
		return OperationPackageUserPermissionVO.getConverter(OperationPackageUserPermissionVO.class)
				.convert(userPermissionPOs, OperationPackageUserPermissionVO.class);
	}
	
	/**
	 * 根据用户id获取用户绑定套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午4:46:08
	 * @param Long userId 用户id
	 * @return List<OperationPackageVO> 绑定的套餐列表
	 */
	public List<OperationPackageUserPermissionVO> queryPackageByUserId(Long userId) throws Exception {
		List<OperationPackageUserPermissionPO> userPermissionPOs = operationPackageUserPermissionDAO.findByUserIdAndStatusIn(
				userId,
				new ArrayListWrapper<String>()
				.add(OperationPackageUserUseStatus.USING.toString())
				.add(OperationPackageUserUseStatus.FRESH.toString())
				.getList());
		return getUserPermissionPackageInfo(userPermissionPOs);
	}
	
	/**
	 * 根据用户id获取用户可用的套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午4:43:37
	 * @param Long userId 用户id
	 * @return List<OperationPackageUserPermissionVO> 可用的套餐
	 */
	public List<OperationPackageUserPermissionVO> queryAvailableByUserId(Long userId) throws Exception {
		return queryPermissionByUserIdAndStatusIn(userId, new ArrayListWrapper<String>()
				.add(OperationPackageUserUseStatus.USING.toString())
				.add(OperationPackageUserUseStatus.FRESH.toString())
				.getList());
	}
	
	/**
	 * 根据用户id获取用户未使用套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午4:31:12
	 * @param Long userId 用户id
	 * @return List<OperationPackageUserPermissionVO> 未使用的套餐
	 */
	public List<OperationPackageUserPermissionVO> queryFreshByUserId(Long userId) throws Exception {
		return queryPermissionByUserIdAndStatusIn(userId, new ArrayListWrapper<String>().add(OperationPackageUserUseStatus.FRESH.toString()).getList());
	}
	
	/**
	 * 根据用户id获取用户正在使用的套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午4:43:37
	 * @param Long userId 用户id
	 * @return List<OperationPackageUserPermissionVO> 正在使用的套餐
	 */
	public List<OperationPackageUserPermissionVO> queryUsingByUserId(Long userId) throws Exception {
		return queryPermissionByUserIdAndStatusIn(userId, new ArrayListWrapper<String>().add(OperationPackageUserUseStatus.USING.toString()).getList());
	}
	
	/**
	 * 根据用户id和套餐使用状态获取套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午4:26:14
	 * @param Long userId 用户Id
	 * @param List<OperationPackageUserUseStatus> 查询状态数组
	 * @return List<OperationPackageUserPermissionVO> 用户套餐关联关系
	 */
	public List<OperationPackageUserPermissionVO> queryPermissionByUserIdAndStatusIn(Long userId, List<String> statuses) throws Exception {
		List<OperationPackageUserPermissionPO> userPermissionPOs = operationPackageUserPermissionDAO.findByUserIdAndStatusIn(userId, statuses);
		return OperationPackageUserPermissionVO.getConverter(OperationPackageUserPermissionVO.class)
				.convert(userPermissionPOs, OperationPackageUserPermissionVO.class);
	}
	
	/**
	 * 查询绑定关系的套餐信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午10:36:20
	 * @param userPermissionPOs
	 * @throws Exception
	 */
	public List<OperationPackageUserPermissionVO> getUserPermissionPackageInfo(List<OperationPackageUserPermissionPO> userPermissionPOs) throws Exception {
		List<OperationPackageUserPermissionVO> userPermissionVOs = new ArrayList<OperationPackageUserPermissionVO>();
		if (userPermissionPOs == null || userPermissionPOs.isEmpty()) return userPermissionVOs;
		List<Long> ids = userPermissionPOs.stream().map(OperationPackageUserPermissionPO::getPackageId).collect(Collectors.toList());
		List<OperationPackageVO> packageVOs = operationPackageQuery.queryByIds(ids);
		for (OperationPackageUserPermissionPO userPermissionPO : userPermissionPOs) {
			userPermissionVOs.add(new OperationPackageUserPermissionVO().set(userPermissionPO)
					.setPackageInfo(packageVOs.stream().filter(item -> item.getId() == userPermissionPO.getPackageId()).findFirst().get()));
		}
		return userPermissionVOs;
	}
	
	/**
	 * 查询结算策略使用情况<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:00:53
	 * @param Long statisticId 结算策略Id
	 * @return List<OperationPackageUserPermissionVO>
	 */
	public List<OperationPackageUserPermissionVO> queryStatisticPermission(Long statisticId) throws Exception {
		List<OperationPackageUserPermissionPO> userPermissionPOs = operationPackageUserPermissionDAO.findByStatisticId(statisticId);
		return userPermissionPOs == null || userPermissionPOs.isEmpty()
				? new ArrayList<OperationPackageUserPermissionVO>()
						: OperationPackageUserPermissionVO.getConverter(OperationPackageUserPermissionVO.class)
						.convert(userPermissionPOs, OperationPackageUserPermissionVO.class);
	}
}
