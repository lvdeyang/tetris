package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OperationPackageMediaTypePermissionQuery {
	@Autowired
	private OperationPackageMediaTypePermissionDAO mediaTypePermissionDAO;
	
	/**
	 * 根据套餐id获取资源类型绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午9:52:25
	 * @param Long packageId 套餐id
	 * @return List<OperationMediaTypePackagePermissionVO> 关联数组
	 */
	public List<OperationPackageMediaTypePermissionVO> queryByPackageId(Long packageId) throws Exception {
		List<OperationPackageMediaTypePermissionPO> permissionPOs = mediaTypePermissionDAO.findByPackageId(packageId);
		return OperationPackageMediaTypePermissionVO.getConverter(OperationPackageMediaTypePermissionVO.class)
				.convert(permissionPOs, OperationPackageMediaTypePermissionVO.class);
	}
	
	/**
	 * 根据套餐id和媒资类型查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午1:52:34
	 * @param Long packageId 套餐id
	 * @param String mediaType 资源类型
	 * @return OperationPackageMediaTypePermissionVO 关联关系
	 */
	public OperationPackageMediaTypePermissionVO queryByPackageIdAndMediaType(Long packageId, String mediaType) throws Exception {
		OperationPackageMediaTypePermissionPO permissionPO = mediaTypePermissionDAO.findByPackageIdAndMediaType(packageId, mediaType);
		return permissionPO == null ? null : new OperationPackageMediaTypePermissionVO().set(permissionPO);
	}
	
	/**
	 * 获取媒资类型数组<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 上午11:28:01
	 * @return List<String> 媒资类型数组
	 */
	public List<String> queryMimsTypeList() throws Exception {
		List<String> types = new ArrayList<String>();
		OperationMediaType[] typeEnums = OperationMediaType.values();
		for (OperationMediaType mediaType : typeEnums) {
			types.add(mediaType.getName());
		}
		return types;
	}
}
