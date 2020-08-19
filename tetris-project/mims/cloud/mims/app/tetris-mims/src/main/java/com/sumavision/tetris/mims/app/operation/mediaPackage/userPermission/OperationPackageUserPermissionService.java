package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.operation.accessRecord.OperationRecordDAO;
import com.sumavision.tetris.mims.app.operation.accessRecord.OperationRecordPO;
import com.sumavision.tetris.mims.app.operation.accessRecord.OperationRecordQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationMediaType;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionVO;
import com.sumavision.tetris.mims.app.operation.statistic.OperationStatisticStrategyQuery;
import com.sumavision.tetris.mims.app.operation.statistic.OperationStatisticStrategyVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationPackageUserPermissionService {
	@Autowired
	private OperationPackageUserPermissionDAO operationPackageUserPermissionDAO;
	
	@Autowired
	private OperationPackageUserPermissionQuery operationPackageUserPermissionQuery;
	
	@Autowired
	private OperationPackageQuery operationPackageQuery;
	
	@Autowired
	private OperationPackageStreamPermissionQuery streamPermissionQuery;
	
	@Autowired
	private OperationPackageMediaTypePermissionQuery mediaTypePermissionQuery;
	
	@Autowired
	private OperationPackageMediaPermissionQuery mediaPermissionQuery;
	
	@Autowired
	private OperationRecordQuery recordQuery;
	
	@Autowired
	private OperationRecordDAO operationRecordDAO;
	
	@Autowired
	private OperationStatisticStrategyQuery operationStatisticStrategyQuery;
	
	/**
	 * 添加绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午3:51:50
	 * @param Long userId 用户id
	 * @param Long packageId 套餐id
	 */
	public OperationPackageUserPermissionVO addPermission(Long userId, Long packageId) throws Exception {
		OperationPackageVO packageVO = operationPackageQuery.queryById(packageId);
		OperationPackageUserPermissionPO userPermissionPO = new OperationPackageUserPermissionPO();
		userPermissionPO.setUpdateTime(new Date());
		userPermissionPO.setPackageId(packageId);
		userPermissionPO.setUserId(userId);
		userPermissionPO.setStatus(OperationPackageUserUseStatus.FRESH);
		OperationStatisticStrategyVO statisticStrategyVO = operationStatisticStrategyQuery.queryLastAvailable();
		if (statisticStrategyVO != null) userPermissionPO.setStatisticId(statisticStrategyVO.getId());;
		operationPackageUserPermissionDAO.save(userPermissionPO);
		return new OperationPackageUserPermissionVO().setPackageInfo(packageVO);
	}
	
	/**
	 * 根据用户id批量绑定套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:42:49
	 * @param Long userId 用户id
	 * @param List<Long> packageIds 套餐id列表
	 * @return List<OperationPackageVO> 套餐列表
	 */
	public List<OperationPackageUserPermissionVO> addPermissions(Long userId, List<Long> packageIds) throws Exception {
		List<OperationPackageUserPermissionVO> userPermissionVOs = new ArrayList<OperationPackageUserPermissionVO>();
		if (packageIds == null || packageIds.isEmpty()) return userPermissionVOs;
		List<OperationPackageVO> packageVOs = operationPackageQuery.queryByIds(packageIds);
		OperationStatisticStrategyVO statisticStrategyVO = operationStatisticStrategyQuery.queryLastAvailable();
		List<OperationPackageUserPermissionPO> packageUserPermissionPOs = new ArrayList<OperationPackageUserPermissionPO>();
		for (Long packageId : packageIds) {
			OperationPackageUserPermissionPO userPermissionPO = new OperationPackageUserPermissionPO();
			userPermissionPO.setUpdateTime(new Date());
			userPermissionPO.setPackageId(packageId);
			userPermissionPO.setUserId(userId);
			userPermissionPO.setStatus(OperationPackageUserUseStatus.FRESH);
			if (statisticStrategyVO != null) userPermissionPO.setStatisticId(statisticStrategyVO.getId());;
			packageUserPermissionPOs.add(userPermissionPO);
		}
		operationPackageUserPermissionDAO.save(packageUserPermissionPOs);
		
		for (OperationPackageUserPermissionPO userPermissionPO : packageUserPermissionPOs) {
			userPermissionVOs.add(new OperationPackageUserPermissionVO().set(userPermissionPO)
					.setPackageInfo(packageVOs.stream().filter(item -> item.getId() == userPermissionPO.getPackageId()).findFirst().get()));
		}
		return userPermissionVOs;
	}
	
	/**
	 * 移除有效的套餐绑定<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午4:41:44
	 * @param Long userId 用户id
	 * @param Long packageId 套餐id
	 * @return OperationPackageUserPermissionVO 套餐绑定信息
	 */
	public void removePermission(Long userId, Long packageId) throws Exception {
		List<OperationPackageUserPermissionPO> userPermissionPOs =
				operationPackageUserPermissionDAO.findByUserIdAndPackageIdAndStatusIn(
						userId,
						packageId,
						new ArrayListWrapper<String>()
						.add(OperationPackageUserUseStatus.FRESH.toString())
						.add(OperationPackageUserUseStatus.USING.toString())
						.getList());
		if (userPermissionPOs.isEmpty()) return;
		for (OperationPackageUserPermissionPO userPermissionPO : userPermissionPOs) {
			if (userPermissionPO != null) {
//				operationPackageUserPermissionDAO.delete(userPermissionPO);
				userPermissionPO.setStatus(OperationPackageUserUseStatus.USE_UP);
				operationPackageUserPermissionDAO.save(userPermissionPO);
			}
		}
	}
	
	/**
	 * 移除有效的套餐绑定<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午4:41:44
	 * @param Long userId 用户id
	 * @param Long packageId 套餐id
	 * @return OperationPackageUserPermissionVO 套餐绑定信息
	 */
	public void removePermissionById(Long id) throws Exception {
		operationPackageUserPermissionDAO.delete(id);
	}
	
	/**
	 * 根据关联关系id设置套餐正在使用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午4:49:22
	 * @param Long id 关联关系id
	 * @return OperationPackageUserPermissionVO 关联关系信息
	 */
	public OperationPackageUserPermissionVO using(Long id) throws Exception {
		return setStatus(id, OperationPackageUserUseStatus.USING);
	}
	
	/**
	 * 根据关联关系id设置套餐使用完成<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午4:49:22
	 * @param Long id 关联关系id
	 * @return OperationPackageUserPermissionVO 关联关系信息
	 */
	public OperationPackageUserPermissionVO finish(Long id) throws Exception {
		return setStatus(id, OperationPackageUserUseStatus.USE_UP);
	}
	
	/**
	 * 根据id设置套餐关联关系使用状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月25日 下午2:54:46
	 * @param Long id 关联关系
	 * @param OperationPackageUserUseStatus status 使用状态
	 * @return OperationPackageUserPermissionVO 关联信息
	 */
	public OperationPackageUserPermissionVO setStatus(Long id, OperationPackageUserUseStatus status) throws Exception {
		OperationPackageUserPermissionPO userPermissionPO = operationPackageUserPermissionDAO.findOne(id);
		if (userPermissionPO != null && userPermissionPO.getStatus() != status) {
			userPermissionPO.setStatus(status);
			operationPackageUserPermissionDAO.save(userPermissionPO);
		}
		return new OperationPackageUserPermissionVO().set(userPermissionPO);
	}
	
	/**
	 * 根据id设置套餐关联关系使用状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月25日 下午2:54:46
	 * @param Long id 关联关系
	 * @param OperationPackageUserUseStatus status 使用状态
	 * @return OperationPackageUserPermissionVO 关联信息
	 */
	public OperationPackageUserPermissionVO setStatus(OperationPackageUserPermissionPO userPermissionPO, OperationPackageUserUseStatus status) throws Exception {
		if (userPermissionPO != null && userPermissionPO.getStatus() != status) {
			userPermissionPO.setStatus(status);
			operationPackageUserPermissionDAO.save(userPermissionPO);
		}
		return new OperationPackageUserPermissionVO().set(userPermissionPO);
	}
	
	/**
	 * 更新套餐使用状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月25日 下午5:34:44
	 * @param Long id 套餐关联关系id
	 */
	public void refreshStatus(Long id) throws Exception {
		OperationPackageUserPermissionPO userPermissionPO = operationPackageUserPermissionDAO.findOne(id);
		if (userPermissionPO == null) return;
		Long packageId = userPermissionPO.getPackageId();
		
		//没有使用记录
		List<OperationRecordPO> recordPOs = operationRecordDAO.findByPermissionId(id);
		if ((recordPOs == null || recordPOs.isEmpty())) {
			setStatus(userPermissionPO, OperationPackageUserUseStatus.FRESH);
			return;
		}
		
		//校验流量使用情况
		OperationPackageStreamPermissionVO streamPermissionPO = streamPermissionQuery.queryByPackageId(packageId);
		if (streamPermissionPO != null) {
			Long streamNum = recordQuery.queryByStreamPermissionIdUsedNum(id);
			if (streamPermissionPO.getNum() > streamNum) {
				setStatus(userPermissionPO, OperationPackageUserUseStatus.USING);
				return;
			}
		}
		
		//校验资源类型使用情况
		List<OperationPackageMediaTypePermissionVO> mediaTypePermissionVOs = mediaTypePermissionQuery.queryByPackageId(packageId);
		if (mediaTypePermissionVOs != null && !mediaTypePermissionVOs.isEmpty()) {
			for (OperationPackageMediaTypePermissionVO mediaTypePermissionVO : mediaTypePermissionVOs) {
				OperationMediaType mediaType = OperationMediaType.fromName(mediaTypePermissionVO.getMediaType());
				Long mediaTypeUseNum = recordQuery.queryMediaTypePermissionUsedNum(id, mediaType);
				if (mediaTypePermissionVO.getNum() > mediaTypeUseNum) {
					setStatus(userPermissionPO, OperationPackageUserUseStatus.USING);
					return;
				}
			}
		}
		
		//校验资源使用情况
		List<OperationPackageMediaPermissionVO> mediaPermissionVOs = mediaPermissionQuery.queryByPackageId(packageId);
		if (mediaPermissionVOs != null && !mediaPermissionVOs.isEmpty()) {
			for (OperationPackageMediaPermissionVO mediaPermissionVO : mediaPermissionVOs) {
				Long mediaUseNum = recordQuery.queryMimsPermissionUsedNum(id, mediaPermissionVO.getUuid());
				if (mediaPermissionVO.getNum() > mediaUseNum) {
					setStatus(userPermissionPO, OperationPackageUserUseStatus.USING);
					return;
				}
			}
		}
		
		setStatus(userPermissionPO, OperationPackageUserUseStatus.USE_UP);
	}
}
