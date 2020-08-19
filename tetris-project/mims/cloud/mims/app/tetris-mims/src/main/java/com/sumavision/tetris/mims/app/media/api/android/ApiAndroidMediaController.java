package com.sumavision.tetris.mims.app.media.api.android;

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
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/android/media")
public class ApiAndroidMediaController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;

	/**
	 * 加载根文件夹<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return FolderPO 根目录
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/{folderType}/get/root")
	public Object getRoot(@PathVariable String folderType, HttpServletRequest request) throws Exception{
		return new HashMapWrapper<String, Object>().put("id", 0)
			  		 .put("name", "根目录")
			  		 .getMap();
	}
}
