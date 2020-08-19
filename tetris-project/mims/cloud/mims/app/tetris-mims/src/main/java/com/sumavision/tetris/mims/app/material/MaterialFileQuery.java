package com.sumavision.tetris.mims.app.material;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaterialFileQuery {
	
	@Autowired
	private MaterialFileDAO materialFileDao;

	/**
	 * 根据uuid查找素材（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 素材uuid
	 * @param Collection<MaterialFilePO> materials 查找范围
	 * @return MaterialFilePO 查找结果
	 */
	public MaterialFilePO loopForUuid(String uuid, Collection<MaterialFilePO> materials){
		if(materials==null || materials.size()<=0) return null;
		for(MaterialFilePO material:materials){
			if(material.getUuid().equals(uuid)){
				return material;
			}
		}
		return null;
	}
	
	/**
	 * 获取文件夹（单个）下的素材（上传完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:07:44
	 * @param Long folerId 文件夹id
	 * @return List<MaterialFilePO> 素材
	 */
	public List<MaterialFilePO> findMaterialsByFolderId(Long folderId){
		return materialFileDao.findByFolderIdAndUploadStatusOrderByTypeAscNameAsc(folderId, MaterialFileUploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的素材（上传完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:15:43
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MaterialFilePO> 素材
	 */
	public List<MaterialFilePO> findMaterialsByFolderIds(Collection<Long> folderIds){
		return materialFileDao.findByFolderIdInAndUploadStatus(folderIds, MaterialFileUploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MaterialFilePO> 上传任务列表
	 */
	public List<MaterialFilePO> findTasksByFolderIds(Collection<Long> folderIds){
		return materialFileDao.findByFolderIdInAndUploadStatusNotOrderByIdAsc(folderIds, MaterialFileUploadStatus.UPLOADING);
	}
	
}
