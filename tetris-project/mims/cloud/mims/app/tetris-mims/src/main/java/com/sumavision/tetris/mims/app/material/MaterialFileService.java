package com.sumavision.tetris.mims.app.material;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.store.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.store.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.store.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserVO;

/**
 * 素材文件相关操作，主增删改<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月23日 下午3:40:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialFileService {

	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;
	
	@Autowired
	private MaterialFileDAO materialFileDao;
	
	@Autowired
	private Path path;
	
	/**
	 * 素材文件删除<br/>
	 * <p>
	 * 	初步设想，考虑到文件夹下可能包含大文件以及文件数量等<br/>
	 * 	情况，这里采用线程删除文件，主要步骤如下：<br/>
	 * 	  1.生成待删除存储文件数据<br/>
	 *    2.删除素材文件元数据<br/>
	 *    3.保存待删除存储文件数据<br/>
	 *    4.调用flush使sql生效<br/>
	 *    5.将待删除存储文件数据押入存储文件删除队列<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:43:03
	 * @param Collection<MaterialFilePO> materials 素材列表
	 */
	public void remove(Collection<MaterialFilePO> materials) throws Exception{
		
		//生成待删除存储文件数据
		List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMaterials(materials);
		
		//删除素材文件元数据
		materialFileDao.deleteInBatch(materials);
		
		//保存待删除存储文件数据
		preRemoveFileDao.save(preRemoveFiles);
		
		//调用flush使sql生效
		preRemoveFileDao.flush();
		
		//将待删除存储文件数据押入存储文件删除队列
		storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
		
		Set<Long> materialIds = new HashSet<Long>();
		for(MaterialFilePO material:materials){
			materialIds.add(material.getId());
		}
		
		//删除临时文件
		for(MaterialFilePO material:materials){
			List<MaterialFilePO> results = materialFileDao.findByTmpPathAndIdNotIn(material.getTmpPath(), materialIds);
			if(results==null || results.size()<=0){
				File file = new File(new File(material.getTmpPath()).getParent());
				File[] children = file.listFiles();
				if(children != null){
					for(File sub:children){
						if(sub.exists()) sub.delete();
					}
				}
				if(file.exists()) file.delete();
			}
		}
		
	}
	
	/**
	 * 添加素材上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param List<MaterialFileTaskVO> tasks 任务列表
	 * @param FolderPO folder 文件夹
	 * @return List<MaterialFilePO> 任务列表
	 */
	public List<MaterialFilePO> addTasks(UserVO user, List<MaterialFileTaskVO> tasks, FolderPO folder) throws Exception{
		String separator = File.separator;
		//临时路径采取/base/username/folderuuid/timestamp
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath)
												   .append(separator)
												   .append("upload")
												   .append(separator)
												   .append("tmp")
												   .append(separator).append(user.getNickname())
												   .append(separator).append(folder.getUuid())
												   .toString();
		List<MaterialFilePO> entities = new ArrayList<MaterialFilePO>();
		for(MaterialFileTaskVO task:tasks){
			Date date = new Date();
			String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(date.getTime()).toString();
			File file = new File(folderPath);
			if(!file.exists()) file.mkdirs();
			//这个地方保证每个任务的路径都不一样
			Thread.sleep(1);
			MaterialFilePO entity = new MaterialFilePO();
			entity.setLastModified(task.getLastModified());
			entity.setName(task.getName());
			entity.setSize(task.getSize());
			entity.setMimetype(task.getMimetype());
			entity.setType(MaterialType.fromMimetype(task.getMimetype()));
			entity.setFolderId(folder.getId());
			entity.setUploadStatus(MaterialFileUploadStatus.UPLOADING);
			entity.setTmpPath(new StringBufferWrapper().append(folderPath)
													   .append(separator)
													   .append(task.getName())
													   .toString());
			entity.setPreviewUrl(new StringBufferWrapper().append("/upload/tmp/")
														  .append(user.getNickname())
														  .append("/")
														  .append(folder.getUuid())
														  .append("/")
														  .append(date.getTime())
														  .append("/")
														  .append(task.getName())
														  .toString());
			entity.setUpdateTime(date);
			entities.add(entity);
		}
		materialFileDao.save(entities);
		return entities;
	}
	
	/**
	 * 素材上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MaterialFilePO task 素材上传任务
	 */
	public void uploadCancel(MaterialFilePO task) throws Exception{
		File file = new File(new File(task.getTmpPath()).getParent());
		File[] children = file.listFiles();
		for(File sub:children){
			sub.delete();
		}
		file.delete();
		materialFileDao.delete(task);
	}
	
	/**
	 * 复制素材文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MaterialFilePO material 待复制的素材
	 * @param FolderPO target 目标文件夹
	 * @return MaterialFilePO 复制后的素材文件
	 */
	public MaterialFilePO copy(MaterialFilePO material, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(material.getFolderId())) moved = false;
		
		MaterialFilePO copiedMaterial = material.copy();
		copiedMaterial.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMaterial.setName(moved?material.getName():new StringBufferWrapper().append(material.getName())
																		         .append("（副本：")
																		         .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																		         .append("）")
																		         .toString());
		copiedMaterial.setFolderId(target.getId());
		
		materialFileDao.save(copiedMaterial);
		
		return copiedMaterial;
	}
	
}
