package com.sumavision.tetris.mims.app.media.txt.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtDAO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtPO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/android/media/txt")
public class ApiAndroidTxtController {

	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	
	@Autowired
	private MediaTxtService mediaTxtService;
	
	@Autowired
	private MediaTxtDAO mediaTxtDao;
	
	/**
	 * 加载文件夹下的文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaTxtVO> 文本媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaTxtQuery.loadForAndroid(folderId);
	}
	
	/**
	 * 编辑文本媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午5:04:34
	 * @param id 文本媒资id
	 * @param content 文本内容
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
 	 * @return MediaTxtVO 文本媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/edit/{id}")
	public Object editTask(
			@PathVariable Long id,
			String content, 
			String name,
            String tags,
            String keyWords,
            String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		MediaTxtPO txt = mediaTxtDao.findOne(id);
		
		MediaTxtPO entity = mediaTxtService.editTask(user, txt, name, null, null, remark, content);
		
		return new MediaTxtVO().set(entity);
		
	}
	
	/**
	 * 删除文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午9:07:53
	 * @param @PathVariable Long id 媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MediaTxtPO media = mediaTxtDao.findOne(id);
		
		if(media == null){
			throw new MediaTxtNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		mediaTxtService.remove(new ArrayListWrapper<MediaTxtPO>().add(media).getList());
		
		return null;
	}
	
	/**
	 * 查询文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 上午9:25:28
	 * @param @PathVariable Long id 媒资id
	 * @return String 文本内容
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/content/{id}")
	public Object queryContent(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		return mediaTxtQuery.queryContent(id);
	}
	
	/**
	 * 保存文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 上午9:33:03
	 * @param @PathVariable Long id 媒资id
	 * @param String content 文本内容
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/content/{id}")
	public Object saveContent(
			@PathVariable Long id,
			String content,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		MediaTxtPO txt = mediaTxtDao.findOne(id);
		
		if(txt == null){
			throw new MediaTxtNotExistException(id);
		}
		
		txt.setContent(content);
		mediaTxtDao.save(txt);
		
		return null;
	}
}
