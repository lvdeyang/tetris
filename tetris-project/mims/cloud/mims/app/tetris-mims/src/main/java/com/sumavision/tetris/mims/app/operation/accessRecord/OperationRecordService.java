package com.sumavision.tetris.mims.app.operation.accessRecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.operation.accessRecord.exception.OperationNoAuthorityForMediaException;
import com.sumavision.tetris.mims.app.operation.accessRecord.exception.OperationNoAvailablePackageException;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackagePermissionType;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationMediaType;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserPermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserPermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserUseStatus;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationRecordService {
	@Autowired
	private OperationRecordDAO operationRecordDAO;
	
	@Autowired
	private OperationRecordQuery operationRecordQuery;
	
	@Autowired
	private OperationPackageMediaPermissionQuery mediaPermissionQuery;
	
	@Autowired
	private OperationPackageMediaTypePermissionQuery mediaTypePermissionQuery;
	
	@Autowired
	private OperationPackageUserPermissionQuery userPermissionQuery;
	
	@Autowired
	private OperationPackageUserPermissionService userPermissionService;
	
	/**
	 * 添加套餐使用记录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午4:01:30
	 * @param Long userId 用户Id
	 * @param MediaVideoVO media 资源信息
	 * @return OperationRecordVO 该条使用记录
	 */
	public OperationRecordVO addRecord(Long userId, OperationRecordVO media, Long recordNum) throws Exception {
		OperationRecordVO recordVO = null;
		
//		//获取用户正在使用的套餐
//		List<OperationPackageUserPermissionVO> usingPermissionVOs = userPermissionQuery.queryUsingByUserId(userId);
//		if (usingPermissionVOs != null && !usingPermissionVOs.isEmpty()) recordVO = checkRecord(userId, media, usingPermissionVOs, recordNum);
		
		//获取用户未使用的套餐
		List<OperationPackageUserPermissionVO> availablePermissionVOs = userPermissionQuery.queryAvailableByUserId(userId);
		if (availablePermissionVOs == null || availablePermissionVOs.isEmpty()) throw new OperationNoAvailablePackageException(userId);
		recordVO = checkRecord(userId, media, availablePermissionVOs, recordNum);
		
		if (recordVO != null) {
			return recordVO;
		}
		throw new OperationNoAuthorityForMediaException(media.getMimsName());
	}
	
	/**
	 * 保存套餐使用记录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午4:02:35
	 * @param OperationPackageUserPermissionVO userPermissionVOuserPermissionVO 套餐与用户关联关系
	 * @param MediaVideoVO media 资源信息
	 * @param OperationPackagePermissionType permissionType 使用套餐内容的类型
	 * @return OperationRecordVO 套餐使用记录信息
	 */
	public OperationRecordVO saveRecord(OperationPackageUserPermissionVO userPermissionVO, OperationRecordVO media, OperationPackagePermissionType permissionType, Long num) throws Exception {
		OperationRecordPO recordPO = new OperationRecordPO();
		recordPO.setUpdateTime(new Date());
		recordPO.setMimsId(media.getMimsId());
		recordPO.setMimsUuid(media.getMimsUuid());
		recordPO.setMimsName(media.getMimsType());
		recordPO.setMimsType(OperationMediaType.fromPrimaryKey(media.getMimsType()).getName());
		recordPO.setNum(num);
		recordPO.setPermissionId(userPermissionVO.getId());
		recordPO.setUserId(userPermissionVO.getUserId());
		recordPO.setPermissionType(permissionType);
		operationRecordDAO.save(recordPO);
		return new OperationRecordVO().set(recordPO);
	}
	
	/**
	 * 校验套餐使用记录，判断该条记录使用的套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午4:08:19
	 * @param Long userId 用户Id
	 * @param MediaVideoVO media 资源信息
	 * @param List<OperationPackageUserPermissionVO> permissionVOs 被校验的套餐
	 * @return OperationRecordVO 校验成功
	 */
	public OperationRecordVO checkRecord(Long userId, OperationRecordVO media, List<OperationPackageUserPermissionVO> permissionVOs, Long recordNum) throws Exception {
		for (OperationPackageUserPermissionVO permissionVO : permissionVOs) {
			//获取套餐绑定的该资源的可用量
			Long packageId = permissionVO.getPackageId();
			OperationPackageMediaPermissionVO mediaPermission = mediaPermissionQuery.queryByPackageIdAndMimsUuid(permissionVO.getPackageId(), media.getMimsUuid());
			//获取套餐绑定的该类资源的可用量
			OperationPackageMediaTypePermissionVO mediaTypePermissionVO = mediaTypePermissionQuery.queryByPackageIdAndMediaType(packageId, OperationMediaType.fromPrimaryKey(media.getMimsType()).getName());
			
			if (mediaPermission != null && mediaPermission.getNum() != null) {
//				//获取当前资源使用套餐的量
//				Long mediaUseNum= operationRecordQuery.queryMimsPermissionUsedNum(permissionVO.getId(), media.getUuid());
//				
//				if (mediaPermission.getNum() >= recordNum + mediaUseNum) {
					OperationRecordVO recordVO = saveRecord(permissionVO, media, OperationPackagePermissionType.PACKAGE_MEDIA_PERMISSION, recordNum);
//					//更新套餐使用状态
//					if (mediaPermission.getNum() > recordNum + mediaUseNum) {
//						if (permissionVO.getStatus() != OperationPackageUserUseStatus.USING) userPermissionService.using(permissionVO.getId());
//					} else if (mediaPermission.getNum() == recordNum + mediaUseNum) {
//						userPermissionService.refreshStatus(permissionVO.getId());
//					}
					return recordVO;
//				}
			}
			
			if (mediaTypePermissionVO  != null && mediaTypePermissionVO.getNum() != null) {
				//获取当前资源类型使用套餐的量
				OperationMediaType mediaType = OperationMediaType.fromPrimaryKey(media.getMimsType());
				Long mediaTypeUseNum = operationRecordQuery.queryMediaTypePermissionUsedNum(permissionVO.getId(), mediaType);
				if (mediaPermission.getNum() > recordNum + mediaTypeUseNum) {
					OperationRecordVO recordVO =  saveRecord(permissionVO, media, OperationPackagePermissionType.PACKAGE_MEDIA_TYPE_PERMISSION, recordNum);
					//更新套餐使用状态
					if (mediaPermission.getNum() > recordNum + mediaTypeUseNum) {
						if (permissionVO.getStatus() != OperationPackageUserUseStatus.USING) userPermissionService.using(permissionVO.getId());
					} else if (mediaPermission.getNum() == recordNum + mediaTypeUseNum) {
						userPermissionService.refreshStatus(permissionVO.getId());
					}
					return recordVO;
				}
			}
		}
		return null;
	}
}
