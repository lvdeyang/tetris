package com.sumavision.tetris.data.warehouse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/data/warehouse")
public class DataWarehouseController {
	
	@Autowired
	private DataWarehouseRequestAdapter adapter;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 数据仓库鉴权<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:37:13
	 * @return String token 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/auth")
	public Object doAuth(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return adapter.doAuth();
	}
	
	/**
	 * 创建项目<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:41:20
	 * @param String name 项目名
	 * @param String path 项目地址(不为中文的唯一标识)
	 * @param String remark 项目说明
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/project/create")
	public Object createProject(String name, String path, String remark, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return adapter.createProject(name, path, remark);
	}
	
	/**
	 * 获取项目列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:43:58
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/project/list/get")
	public Object getProjectList(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return adapter.getProjectList();
	}
	
	/**
	 * 删除项目<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:44:18
	 * @param String projectId 项目id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/project/delete")
	public Object deleteProject(String projectId, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		adapter.deleteProject(projectId);
		return null;
	}
	
	/**
	 * 获取项目下的文件列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:44:37
	 * @param String projectId 项目id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/file/list/get")
	public Object getFileList(String projectId, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return adapter.getFileList(projectId);
	}
	
	/**
	 * 添加备份<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:45:01
	 * @param String projectId 项目id
	 * @param String fileName 备份文件名称
	 * @param String remark 备份说明
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/file/upload")
	public Object upload(String projectId, String fileName, String remark, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return adapter.uploadFile(projectId, fileName, remark);
	}
	
	/**
	 * 备份文件下载<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:48:00
	 * @param String projectId 项目id
	 * @param String filePath 文件地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/file/download")
	public Object download(String projectId, String filePath, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		adapter.downloadFile(projectId, filePath);
		return null;
	}
	
	/**
	 * 删除备份文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:50:31
	 * @param String projectId 项目id
	 * @param String filePath 文件地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/file/delete")
	public Object deleteFile(String projectId, String filePath, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		adapter.deleteFile(projectId, filePath);
		return null;
	}
}
