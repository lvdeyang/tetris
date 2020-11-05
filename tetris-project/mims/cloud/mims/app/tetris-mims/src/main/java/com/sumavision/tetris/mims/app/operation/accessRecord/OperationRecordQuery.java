package com.sumavision.tetris.mims.app.operation.accessRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackagePermissionType;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.detail.OperationPackageDetailQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationMediaType;

@Component
public class OperationRecordQuery {
	
	@Autowired
	private OperationRecordDAO operationRecordDAO;
	
	@Autowired
	private OperationPackageQuery operationPackageQuery;
	
	@Autowired
	private OperationPackageDetailQuery operationPackageDetailQuery;
	
	/**
	 * 根据用户id获取记录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午4:55:07
	 * @param userId
	 * @return List<OperationRecordVO> 用户使用记录
	 */
	public List<OperationRecordVO> queryByUserId(Long userId) throws Exception {
		List<OperationRecordPO> recordPOs = operationRecordDAO.findByUserId(userId);
		return OperationRecordVO.getConverter(OperationRecordVO.class).convert(recordPOs, OperationRecordVO.class);
	}
	
	/**
	 * 获取资源的套餐使用情况<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午2:12:51
	 * @param Long permissionId 用户套餐关联id
	 * @param String mimsUuid 资源uuid
	 * @return List<OperationRecordVO> 使用情况
	 */
	public List<OperationRecordVO> queryByPackagePermissionIdAndMimsUuid(Long permissionId, String mimsUuid) throws Exception {
		List<OperationRecordPO> recordPOs = operationRecordDAO.findByPermissionIdAndMimsUuidAndPermissionType(
				permissionId, mimsUuid, OperationPackagePermissionType.PACKAGE_MEDIA_PERMISSION);
		return OperationRecordVO.getConverter(OperationRecordVO.class).convert(recordPOs, OperationRecordVO.class);
	}
	
	/**
	 * 获取资源的套餐使用量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午2:24:48
	 * @param Long permissionId 用户套餐关联id
	 * @param String mimsUuid 资源uuid
	 * @return Long 使用数量
	 */
	public Long queryMimsPermissionUsedNum(Long permissionId, String mimsUuid) throws Exception {
		List<OperationRecordVO> recordVOs = queryByPackagePermissionIdAndMimsUuid(permissionId, mimsUuid);
		return queryTotal(recordVOs);
	}
	
	/**
	 * 获取资源类型的套餐使用情况<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午3:04:04
	 * @param Long permissionId 用户套餐关联id
	 * @param OperationMediaType mediaType 资源类型
	 * @return List<OperationRecordVO> 使用情况
	 */
	public List<OperationRecordVO> queryByPackagePermissionIdAndMediaType(Long permissionId, OperationMediaType mediaType) throws Exception {
		List<OperationRecordPO> recordPOs = operationRecordDAO.findByPermissionIdAndMimsTypeAndPermissionType(
				permissionId, mediaType.getName(), OperationPackagePermissionType.PACKAGE_MEDIA_TYPE_PERMISSION);
		return OperationRecordVO.getConverter(OperationRecordVO.class).convert(recordPOs, OperationRecordVO.class);
	}
	
	/**
	 * 获取资源类型的套餐使用量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午2:24:48
	 * @param Long permissionId 用户套餐关联id
	 * @param String mimsUuid 资源uuid
	 * @return Long 使用数量
	 */
	public Long queryMediaTypePermissionUsedNum(Long permissionId, OperationMediaType mediaType) throws Exception {
		List<OperationRecordVO> recordVOs = queryByPackagePermissionIdAndMediaType(permissionId, mediaType);
		return queryTotal(recordVOs);
	}
	
	/**
	 * 获取流量的套餐使用情况<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午3:04:04
	 * @param Long permissionId 用户套餐关联id
	 * @return List<OperationRecordVO> 使用情况
	 */
	public List<OperationRecordVO> queryByStreamPermissionId(Long permissionId) throws Exception {
		List<OperationRecordPO> recordPOs = operationRecordDAO.findByPermissionIdAndPermissionType(
				permissionId, OperationPackagePermissionType.PACKAGE_STREAM_PERMISSION);
		return OperationRecordVO.getConverter(OperationRecordVO.class).convert(recordPOs, OperationRecordVO.class);
	}
	
	/**
	 * 获取流量的套餐使用量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午2:24:48
	 * @param Long permissionId 用户套餐关联id
	 * @param String mimsUuid 资源uuid
	 * @return Long 使用数量
	 */
	public Long queryByStreamPermissionIdUsedNum(Long permissionId) throws Exception {
		List<OperationRecordVO> recordVOs = queryByStreamPermissionId(permissionId);
		return queryTotal(recordVOs);
	}
	
	private Long queryTotal(List<OperationRecordVO> recordVOs) throws Exception {
		Long total = 0l;
		for (OperationRecordVO operationRecordVO : recordVOs) {
			Long num = operationRecordVO.getNum();
			if (num != null) total += num;
		}
		return total;
	}
}
