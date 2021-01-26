package com.sumavision.tetris.mims.app.operation.mediaPackage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.operation.mediaPackage.exception.OperationPackageNotExistException;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserUseStatus;

@Component
public class OperationPackageQuery {
	@Autowired
	private OperationPackageDAO operationPackageDAO;

	/**
	 * 根据id查询套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:28:03
	 * @param Long id 套餐id
	 * @return OperationPackageVO 套餐信息
	 */
	public OperationPackageVO queryById(Long id) throws Exception {
		OperationPackagePO packagePO = operationPackageDAO.findById(id);
		if (packagePO != null) {
			return new OperationPackageVO().set(packagePO);
		}
		throw new OperationPackageNotExistException(id);
	}
	
	/**
	 * 根据id查询套餐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:28:03
	 * @param List<Long> ids 套餐id数组
	 * @return List<OperationPackageVO> 套餐信息
	 */
	public List<OperationPackageVO> queryByIds(List<Long> ids) throws Exception {
		List<OperationPackagePO> packagePO = operationPackageDAO.findAllById(ids);
		if (packagePO == null || packagePO.isEmpty()) throw new OperationPackageNotExistException();
		return OperationPackageVO.getConverter(OperationPackageVO.class).convert(packagePO, OperationPackageVO.class);
	}
	
	/**
	 * 获取所有套餐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午2:49:29
	 * @return List<OperationPackageVO> 套餐列表
	 */
	public List<OperationPackageVO> queryPackageList(String groupId) throws Exception{
		List<OperationPackagePO> packagePOs = operationPackageDAO.findByGroupIdAndStatusOrderByUpdateTimeDesc(groupId, OperationPackageStatus.AVAILABLE);
		return OperationPackageVO.getConverter(OperationPackageVO.class).convert(packagePOs, OperationPackageVO.class);
	}
	
	/**
	 * 根据用户id获取绑定的套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午4:49:03
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<OperationPackageVO> queryPackagesByUserId(Long userId) throws Exception {
		List<OperationPackagePO> packagePOs = operationPackageDAO.findByUserIdAndPermissionAndUseStatusIn(userId,
				new ArrayListWrapper<String>()
				.add(OperationPackageUserUseStatus.FRESH.toString())
				.add(OperationPackageUserUseStatus.USING.toString())
				.getList());
		return packagePOs != null 
				? OperationPackageVO.getConverter(OperationPackageVO.class).convert(packagePOs, OperationPackageVO.class)
						: new ArrayList<OperationPackageVO>();
	}
}
