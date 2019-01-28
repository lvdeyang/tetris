package com.sumavision.tetris.mims.app.media.picture;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.mims.app.store.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.store.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.store.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;

/**
 * 图片媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaPictureService {

	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;
	
	@Autowired
	private MediaPictureDAO mediaPictureDao;
	
	@Autowired
	private Path path;
	
	/**
	 * 图片媒资删除<br/>
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
	public void remove(Collection<MediaPicturePO> pictures) throws Exception{
		
		//生成待删除存储文件数据
		List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaPictures(pictures);
		
		//删除素材文件元数据
		mediaPictureDao.deleteInBatch(pictures);
		
		//保存待删除存储文件数据
		preRemoveFileDao.save(preRemoveFiles);
		
		//调用flush使sql生效
		preRemoveFileDao.flush();
		
		//将待删除存储文件数据押入存储文件删除队列
		storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
		
		Set<Long> pictureIds = new HashSet<Long>();
		for(MediaPicturePO picture:pictures){
			pictureIds.add(picture.getId());
		}
		
		//删除临时文件
		for(MediaPicturePO picture:pictures){
			List<MediaPicturePO> results = mediaPictureDao.findByTmpPathAndIdNotIn(picture.getUploadTmpPath(), pictureIds);
			if(results==null || results.size()<=0){
				File file = new File(new File(picture.getUploadTmpPath()).getParent());
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
	
}
