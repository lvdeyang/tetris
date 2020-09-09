package com.sumavision.tetris.mims.app.operation.mediaPackage.detail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/operation/package/detail")
public class OperationPackageDetailController {
	@Autowired
	private OperationPackageDetailQuery operationPackageDetailQuery;
	
	@Autowired
	private OperationPackageDetailService operationPackageDetailService;
	
	@Autowired
	private OperationPackageMediaTypePermissionQuery mediaTypePermissionQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	
	/**
	 * 根据套餐id获取套餐详细信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 上午10:25:33
	 * @param Long id 套餐id
	 * @return OperationMediaPackagePermissionVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/{id}")
	public Object loadPermission(@PathVariable Long id, HttpServletRequest request) throws Exception {
		return operationPackageDetailQuery.queryByPackageId(id);
	}
	
	/**
	 * 设置套餐绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午4:07:42
	 * @param Long id 套餐id
	 * @param String mediaPermission 资源绑定关系
	 * @param String mediaTypePermission 资源类型绑定关系
	 * @param String streamPermission 流量绑定关系
	 * @return OperationPackageDetailVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/{id}")
	public Object setPermission(@PathVariable Long id, String mediaPermission, String mediaTypePermission, String streamPermission) throws Exception {
		List<OperationPackageMediaPermissionVO> mediaPermissionVOs = new ArrayList<OperationPackageMediaPermissionVO>();
		if (mediaPermission != null && !mediaPermission.isEmpty()) {
			mediaPermissionVOs = JSONArray.parseArray(mediaPermission, OperationPackageMediaPermissionVO.class);
		}
		
		List<OperationPackageMediaTypePermissionVO> mediaTypePermissionVOs = new ArrayList<OperationPackageMediaTypePermissionVO>();
		if (mediaTypePermission != null && !mediaTypePermission.isEmpty()) {
			mediaTypePermissionVOs = JSONArray.parseArray(mediaTypePermission, OperationPackageMediaTypePermissionVO.class);
		}
		
		OperationPackageStreamPermissionVO streamPermissionVO = new OperationPackageStreamPermissionVO();
		if (streamPermission != null && !streamPermission.isEmpty()) {
			streamPermissionVO = JSONObject.parseObject(streamPermission, OperationPackageStreamPermissionVO.class);
		}
		
		return operationPackageDetailService.setByPackage(id, mediaPermissionVOs, mediaTypePermissionVOs, streamPermissionVO);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/tree")
	public Object getMediaTree(HttpServletRequest request) throws Exception {
		List<MediaPictureVO> pictureVOs = mediaPictureQuery.loadAll();
		List<MediaVideoVO> videoVOs = mediaVideoQuery.loadAll();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAll();
		List<MediaTxtVO> txtVOs = mediaTxtQuery.loadAll();
		return new HashMapWrapper<String, Object>()
				.put("picture", pictureVOs)
				.put("video", videoVOs)
				.put("audio", audioVOs)
				.put("txt", txtVOs)
				.getMap();
	}
	
	/**
	 * 获取媒资类型数组<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 上午11:28:53
	 * @return List<String> 媒资类型数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/type/list")
	public Object getTypeList(HttpServletRequest request) throws Exception {
		return mediaTypePermissionQuery.queryMimsTypeList();
	}
}
