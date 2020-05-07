package com.sumavision.tetris.mims.app.media.picture.feign;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureDAO;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/picture/feign")
public class MediaPictureFeignController {
	@Autowired
	private MediaPictureDAO mediaPictureDao;

	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaPictureService mediaPictureService;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 查询媒资库图片feign接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:30:44
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaPictureVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId, 
			HttpServletRequest request) throws Exception{
		
		
		return mediaPictureQuery.load(folderId);
	}
	
	/**
	 * 加载所有的图片媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午4:03:27
	 * @return List<MediaPictureVO> 图片媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		return mediaPictureQuery.loadAll();
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaPictureVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/collection")
	public Object loadCollection(Long folderId, HttpServletRequest request) throws Exception{
		return mediaPictureQuery.loadPictureCollection(folderId);
	}
	
	/**
	 * 根据预览地址查询图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:34:58
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaPictureVO> 图片列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls,
			HttpServletRequest request) throws Exception{
		return mediaPictureQuery.findByPreviewUrlIn(JSON.parseArray(previewUrls, String.class));
	}
	
	/**
	 * 删除图片媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:07:53
	 * @param Long id 媒资id
	 * @return deleted List<MediaPictureVO> 删除列表
	 * @return processed List<MediaPictureVO> 待审核列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String ids,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if (ids == null || ids.isEmpty()) return null;
		
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		List<MediaPicturePO> picturePOs = new ArrayList<MediaPicturePO>();
		for (Long id : idList) {
			MediaPicturePO media = mediaPictureDao.findOne(id);
			if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
				throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
			}
			picturePOs.add(media);
		}
		
		return mediaPictureService.remove(picturePOs);
	}
}
