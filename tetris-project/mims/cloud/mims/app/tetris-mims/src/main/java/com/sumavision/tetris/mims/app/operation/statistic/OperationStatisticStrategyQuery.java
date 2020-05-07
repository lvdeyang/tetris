package com.sumavision.tetris.mims.app.operation.statistic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageStatus;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class OperationStatisticStrategyQuery {
	@Autowired
	private OperationStatisticStrategyDAO statisticDistridutionDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取结算策略列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午4:51:52
	 */
	public List<OperationStatisticStrategyVO> queryAll() throws Exception {
		UserVO user = userQuery.current();
		List<OperationStatisticStrategyPO> statisticDistridutionPOs = statisticDistridutionDAO.findByGroupId(user.getGroupId());
		return statisticDistridutionPOs == null || statisticDistridutionPOs.isEmpty()
				? new ArrayList<OperationStatisticStrategyVO>()
						: OperationStatisticStrategyVO.getConverter(OperationStatisticStrategyVO.class).convert(statisticDistridutionPOs, OperationStatisticStrategyVO.class);
	}
	
	/**
	 * 获取有效的结算策略列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:08:01
	 */
	public List<OperationStatisticStrategyVO> queryAvailable() throws Exception {
		UserVO user = userQuery.current();
		List<OperationStatisticStrategyPO> statisticDistridutionPOs = statisticDistridutionDAO.findByGroupIdAndStatus(user.getGroupId(), OperationPackageStatus.AVAILABLE);
		return statisticDistridutionPOs == null || statisticDistridutionPOs.isEmpty()
				? new ArrayList<OperationStatisticStrategyVO>()
						: OperationStatisticStrategyVO.getConverter(OperationStatisticStrategyVO.class).convert(statisticDistridutionPOs, OperationStatisticStrategyVO.class);
	}
	
	/**
	 * 获取最后一个有效的结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:08:01
	 */
	public OperationStatisticStrategyVO queryLastAvailable() throws Exception {
		List<OperationStatisticStrategyVO> statisticStrategyVOs = queryAvailable();
		if (statisticStrategyVOs == null || statisticStrategyVOs.isEmpty()) return null;
		return statisticStrategyVOs.get(statisticStrategyVOs.size() - 1);
	}
	
	/**
	 * 根据id查询结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午4:36:31
	 * @param Long id 策略id
	 * @return OperationStatisticDistridutionVO 策略信息
	 */
	public OperationStatisticStrategyVO queryById(Long id) throws Exception {
		OperationStatisticStrategyPO statisticDistridutionPO = statisticDistridutionDAO.findOne(id);
		return statisticDistridutionPO == null ? null : new OperationStatisticStrategyVO().set(statisticDistridutionPO);
	}
}
