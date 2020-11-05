package com.sumavision.tetris.mims.app.operation.statistic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageStatus;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserPermissionQuery;
import com.sumavision.tetris.mims.app.operation.statistic.exception.OperationStatisticStrategyNotExistException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationStatisticStrategyService {
	@Autowired
	private OperationStatisticStrategyDAO statisticDistridutionDAO;
	
	@Autowired
	private OperationPackageUserPermissionQuery userPermissionQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 添加结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午4:40:31
	 * @param Integer producer 生产者占比数
	 * @param Integer operator 运营商占比数
	 * @return OperationStatisticDistridutionVO 结算策略信息
	 */
	public OperationStatisticStrategyVO add(Integer producer, Integer operator) throws Exception {
		UserVO user = userQuery.current();
		OperationStatisticStrategyPO statisticDistridutionPO = new OperationStatisticStrategyPO();
		statisticDistridutionPO.setUpdateTime(new Date());
		statisticDistridutionPO.setGroupId(user.getGroupId());
		statisticDistridutionPO.setOperator(operator);
		statisticDistridutionPO.setProducer(producer);
		statisticDistridutionPO.setStatus(OperationPackageStatus.AVAILABLE);
		statisticDistridutionDAO.save(statisticDistridutionPO);
		return new OperationStatisticStrategyVO().set(statisticDistridutionPO);
	}
	
	/**
	 * 修改结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:08:45
	 * @param Long id 结算策略id
	 * @param Integer producer 生产者占比
	 * @param Integer operator 运营商占比
	 * @return OperationStatisticDistridutionVO 结算策略信息
	 */
	public OperationStatisticStrategyVO edit(Long id, Integer producer, Integer operator) throws Exception {
		OperationStatisticStrategyPO statisticDistridutionPO = statisticDistridutionDAO.findOne(id);
		if (statisticDistridutionPO == null) throw new OperationStatisticStrategyNotExistException(id);
		
		if (checkUsed(id)) {
			statisticDistridutionPO.setStatus(OperationPackageStatus.INVALID);
			statisticDistridutionDAO.save(statisticDistridutionPO);
			return add(producer, operator);
		} else {
			statisticDistridutionPO.setOperator(operator);
			statisticDistridutionPO.setProducer(producer);
			statisticDistridutionDAO.save(statisticDistridutionPO);
			return new OperationStatisticStrategyVO().set(statisticDistridutionPO);
		}
	}
	
	/**
	 * 删除结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:17:39
	 * @param Long id 结算策略id
	 * @return OperationStatisticDistridutionVO 结算策略信息
	 */
	public OperationStatisticStrategyVO remove(Long id) throws Exception {
		OperationStatisticStrategyPO statisticDistridutionPO = statisticDistridutionDAO.findOne(id);
		if (statisticDistridutionPO == null) throw new OperationStatisticStrategyNotExistException(id);
		if (checkUsed(id)) {
			statisticDistridutionPO.setStatus(OperationPackageStatus.INVALID);
			statisticDistridutionDAO.save(statisticDistridutionPO);
		} else {
			statisticDistridutionDAO.delete(id);
		}
		return new OperationStatisticStrategyVO().set(statisticDistridutionPO);
	}
	
	/**
	 * 校验结算策略是否被使用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:03:32
	 * @param Long id 策略id
	 * @return Boolean 是否被使用
	 */
	public Boolean checkUsed(Long id) throws Exception {
		return !userPermissionQuery.queryStatisticPermission(id).isEmpty();
	}
}
