package com.sumavision.tetris.mims.app.operation.mediaPackage;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.operation.mediaPackage.detail.OperationPackageDetailService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.exception.OperationPackageNotExistException;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission.OperationPackageUserPermissionQuery;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationPackageService {
	@Autowired
	private OperationPackageDAO operationPackageDAO;
	
	@Autowired
	private OperationPackageUserPermissionQuery userPermissionQuery;
	
	@Autowired
	private OperationPackageDetailService detailService;
	
	@Autowired
	private OperationPackageMediaPermissionService mediaPermissionService;
	
	@Autowired
	private OperationPackageMediaTypePermissionService mediaTypePermissionService;
	
	@Autowired
	private OperationPackageStreamPermissionService streamPermissionService;
	
	/**
	 * 添加套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:28:56
	 * @param String name 套餐名
	 * @param Long price 套餐价格
	 * @param String remark 备注
	 * @return OperationPackageVO
	 */
	public OperationPackageVO add(String groupId, String name, Long price, String remark) throws Exception {
		OperationPackagePO packagePO = new OperationPackagePO();
		packagePO.setName(name);
		packagePO.setPrice(price);
		packagePO.setRemark(remark);
		packagePO.setGroupId(groupId);
		packagePO.setStatus(OperationPackageStatus.AVAILABLE);
		packagePO.setUpdateTime(new Date());
		operationPackageDAO.save(packagePO);
		return new OperationPackageVO().set(packagePO);
	}
	
	/**
	 * 编辑套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:29:38
	 * @param Long packageId 套餐id
	 * @param String name 套餐名
	 * @param Long price 套餐价格
	 * @param String remark 备注
	 * @return OperationPackageVO
	 */
	public OperationPackageVO edit(Long packageId, String name, Long price, String remark) throws Exception {
		OperationPackagePO packagePO = operationPackageDAO.findById(packageId);
		if (packagePO != null) {
			if (!packageUsed(packageId) || packagePO.getPrice() == price) {
				packagePO.setName(name);
				packagePO.setPrice(price);
				packagePO.setRemark(remark);
				packagePO.setStatus(OperationPackageStatus.AVAILABLE);
				packagePO.setUpdateTime(new Date());
				operationPackageDAO.save(packagePO);
				return new OperationPackageVO().set(packagePO);
			} else {
				packagePO.setStatus(OperationPackageStatus.INVALID);
				operationPackageDAO.save(packagePO);
				//新增套餐并复制一份套餐配置
				OperationPackageVO newPackage = add(packagePO.getGroupId(), name, price, remark);
				detailService.copeByPakcageId(packageId, newPackage.getId());
				return newPackage;
			}
		} else {
			throw new OperationPackageNotExistException(packageId);
		}
	}
	
	/**
	 * 根据套餐id复制<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:59:42
	 * @param Long sourceId 源套餐id
	 * @return OperationPackageVO
	 */
	public OperationPackageVO copy(Long sourceId) throws Exception {
		OperationPackagePO sourcePackage = operationPackageDAO.findById(sourceId);
		return add(sourcePackage.getGroupId(), sourcePackage.getName(), sourcePackage.getPrice(), sourcePackage.getRemark());
	}
	
	/**
	 * 删除套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:30:32
	 * @param Long packageId 套餐id
	 */
	public void delete(Long packageId) throws Exception {
		OperationPackagePO packagePO = operationPackageDAO.findById(packageId);
		if (packagePO != null) {
			if (!packageUsed(packageId)) {
				operationPackageDAO.deleteById(packageId);
				mediaPermissionService.removeByPackageId(packageId);
				mediaTypePermissionService.removeByPackageId(packageId);
				streamPermissionService.removeByPackageId(packageId);
			} else {
				packagePO.setStatus(OperationPackageStatus.INVALID);
				operationPackageDAO.save(packagePO);
			}
		} else {
			throw new OperationPackageNotExistException(packageId);
		}
	}
	
	/**
	 * 失效套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月9日 上午8:54:33
	 * @param Long packageId 套餐id
	 */
	public void setInvalid(Long packageId) throws Exception {
		OperationPackagePO packagePO = operationPackageDAO.findById(packageId);
		if (packagePO != null) {
			packagePO.setStatus(OperationPackageStatus.INVALID);
			operationPackageDAO.save(packagePO);
		}
	}
	
	/**
	 * 查询套餐是否被使用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:31:15
	 * @param Long packageId 套餐id
	 */
	public Boolean packageUsed(Long packageId) throws Exception {
		return !userPermissionQuery.queryByPackageId(packageId).isEmpty();
	}
}
