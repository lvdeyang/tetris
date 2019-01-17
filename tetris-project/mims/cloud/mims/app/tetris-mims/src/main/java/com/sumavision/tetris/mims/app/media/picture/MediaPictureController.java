package com.sumavision.tetris.mims.app.media.picture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.user.UserClassify;
import com.sumavision.tetris.mims.app.user.UserQuery;
import com.sumavision.tetris.mims.app.user.UserVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/picture")
public class MediaPictureController {

	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userTool;
	
	/**
	 * 加载文件夹下的图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		UserClassify classify = UserClassify.valueOf(user.getClassify());
		
		if(!classify.equals(UserClassify.COMPANY_ADMIN) || !classify.equals(UserClassify.COMPANY_USER)){
			throw new UserHashNoPermissionForMediaPictureException(user.getUuid());
		}
		
		List<FolderPO> folders = null;
		if(folderId == 0l){
			folders = folderDao.findPermissionCompanyRootFolder(user.getUuid(), FolderType.COMPANY_PICTURE);
		}else{
			folders = folderDao.findPermissionCompanyFoldersByParentId(user.getUuid(), folderId, FolderType.COMPANY_PICTURE);
		}
		
		List<MediaPictureVO> medias = new ArrayList<MediaPictureVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaPictureVO().set(folder));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", null)
																  		 .getMap();
		
		return result;
	}
	
}
