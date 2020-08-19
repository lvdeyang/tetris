package com.sumavision.tetris.data.warehouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.shell.RunShell;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.data.warehouse.exception.DataWarehouseRequestErrorException;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;

@Service
@Transactional(rollbackFor = Exception.class)
public class DataWarehouseRequestAdapter {
	
	private static final String EXT = ".tar";
	
	@Autowired
	private Path path;

	private String token;
	
	private String userId;
	
	/**
	 * 数据仓库鉴权<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:37:13
	 * @return String token 
	 */
	public String doAuth() throws Exception {
		if (token != null && !token.isEmpty()
				&& userId != null && !userId.isEmpty()) return token;
		JSONObject questjJson = new JSONObject();
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);
		questjJson.put("username", jsonObject.getString("dataWarehouseUsername"));
		questjJson.put("password", jsonObject.getString("dataWarehousePassword"));
		JSONObject response = httpRequestObject(DataWarehouseRequestAction.DO_AUTH, questjJson);
		token = response.getString("token");
		userId = response.getString("user_id");
		return token;
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
	public JSONObject createProject(String name, String path, String remark) throws Exception {
		JSONObject questJson = new JSONObject();
		questJson.put("token", token);
		questJson.put("project_name", name);
		questJson.put("project_path", path);
		questJson.put("project_description", remark);
		JSONObject response = httpRequestObject(DataWarehouseRequestAction.CREATE_PROJECT, questJson);
		return response;
	}
	
	/**
	 * 获取项目列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:43:58
	 */
	public JSONArray getProjectList() throws Exception {
		JSONObject questJson = new JSONObject();
		questJson.put("token", token);
		questJson.put("user_id", userId);
		JSONArray response = httpRequestArray(DataWarehouseRequestAction.GET_PROJECT_LIST, questJson);
		return response;
	}
	
	/**
	 * 删除项目<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:44:18
	 * @param String projectId 项目id
	 */
	public void deleteProject(String projectId) throws Exception {
		JSONObject questJson = new JSONObject();
		questJson.put("token", token);
		questJson.put("project_id", projectId);
		httpRequestObject(DataWarehouseRequestAction.DELETE_PROJECT, questJson);
	}
	
	/**
	 * 获取项目下的文件列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:44:37
	 * @param String projectId 项目id
	 */
	public JSONArray getFileList(String projectId) throws Exception {
		JSONObject questJson = new JSONObject();
		questJson.put("token", token);
		questJson.put("project_id", projectId);
		JSONArray response = httpRequestArray(DataWarehouseRequestAction.GET_FILE_LIST, questJson);
		return response;
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
	public JSONObject uploadFile(String projectId, String fileName, String remark) throws Exception {
		JSONObject questjJson = new JSONObject();
		questjJson.put("token", token);
		questjJson.put("project_id", projectId);
		questjJson.put("commit_message", remark);
		
		String now = DateUtil.getCurrentDateTime();
		String fileWholeName = fileName + EXT;
		String source = new StringBufferWrapper().append(path.webappPath())
				.append("dataBak")
				.append(File.separator)
				.append(now)
				.append(File.separator)
				.append(fileWholeName)
				.toString();
		//shell打包
		String shellFile = new StringBufferWrapper().append(path.webappPath())
				.append("dataBak")
				.append(File.separator)
				.append("saveData.sh")
				.toString();
		RunShell.runShell(new StringBufferWrapper().append(shellFile)
				.append(" ")
				.append(fileWholeName)
				.append(" ")
				.append(now)
				.toString());
		Thread.sleep(2000);
		
		FileInputStream fis = null;
    	File file = new File(source);
    	byte[] img_content = null;
    	try {
    		fis = new FileInputStream(file);
        	img_content = new byte[(int) file.length()];
        	fis.read(img_content);
		} finally {
			if (fis != null) fis.close();
		}
		
    	questjJson.put("file_content", img_content);
		questjJson.put("file_name", fileWholeName);
		JSONObject response = httpRequestObject(DataWarehouseRequestAction.UPLOAD_FILE, questjJson);
		JSONObject responseFileDetail = new JSONObject();
		responseFileDetail.put("id", response.getString("commit_id"));
		responseFileDetail.put("path", fileWholeName);
		responseFileDetail.put("name", fileWholeName);
		responseFileDetail.put("type", "file");
		return responseFileDetail;
	}
	
	/**
	 * 备份文件下载<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:48:00
	 * @param String projectId 项目id
	 * @param String filePath 文件地址
	 */
	public void downloadFile(String projectId, String filePath) throws Exception {
		JSONObject questJson = new JSONObject();
		questJson.put("token", token);
		questJson.put("project_id", projectId);
		questJson.put("file_path", filePath);
		DataWarehouseRequestVO dataWarehouseRequestVO = new DataWarehouseRequestVO(DataWarehouseRequestAction.DOWNLOAD_FILE.getAction());
		dataWarehouseRequestVO.setData(questJson);
		String purpose = new StringBufferWrapper().append(path.webappPath())
				.append("dataDownload")
				.append(File.separator)
				.append(DateUtil.getLongDate())
				.append(File.separator)
				.append(filePath)
				.toString();
		HttpRequestUtil.downloadTask(getRequestUrl(),
				JSONObject.toJSONString(dataWarehouseRequestVO), purpose);
	}
	
	/**
	 * 删除备份文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:50:31
	 * @param String projectId 项目id
	 * @param String filePath 文件地址
	 */
	public void deleteFile(String projectId, String filePath) throws Exception {
		JSONObject questJson = new JSONObject();
		questJson.put("token", token);
		questJson.put("project_id", projectId);
		questJson.put("file_path", filePath);
		httpRequestObject(DataWarehouseRequestAction.DELETE_FILE, questJson);
	}
	
	/**
	 * 返回json对象的http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:56:43
	 * @param DataWarehouseRequestAction action 请求动作
	 * @param JSONObject data 请求内容
	 * @return JSONObject 返回的对象内容
	 */
	private JSONObject httpRequestObject (DataWarehouseRequestAction action, JSONObject data) throws Exception {
		DataWarehouseRequestVO dataWarehouseRequestVO = new DataWarehouseRequestVO(action.getAction());
		dataWarehouseRequestVO.setData(data);
		JSONObject response = HttpRequestUtil.httpJsonPost(getRequestUrl(), JSONObject.toJSONString(dataWarehouseRequestVO));
		DataWarehouseResponseObjectVO responseVO = JSON.parseObject(response.toJSONString(), DataWarehouseResponseObjectVO.class);
		if (responseVO.getStatcode().equals("200")) {
			return responseVO.getData();
		} else {
			throw new DataWarehouseRequestErrorException(action.getDetail(), responseVO.getErr());
		}
	}
	
	/**
	 * 返回数组的http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:56:43
	 * @param DataWarehouseRequestAction action 请求动作
	 * @param JSONObject data 请求内容
	 * @return JSONArray 返回的数组内容
	 */
	private JSONArray httpRequestArray (DataWarehouseRequestAction action, JSONObject data) throws Exception {
		DataWarehouseRequestVO dataWarehouseRequestVO = new DataWarehouseRequestVO(action.getAction());
		dataWarehouseRequestVO.setData(data);
		JSONObject response = HttpRequestUtil.httpJsonPost(getRequestUrl(), JSONObject.toJSONString(dataWarehouseRequestVO));
		DataWarehouseResponseArrayVO responseVO = JSON.parseObject(response.toJSONString(), DataWarehouseResponseArrayVO.class);
		if (responseVO.getStatcode().equals("200")) {
			return responseVO.getData();
		} else {
			throw new DataWarehouseRequestErrorException(action.getDetail(), responseVO.getErr());
		}
	}
	
	/**
	 * 根据配置文件获取请求地址<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午4:56:07
	 * @return String 完整请求地址
	 */
	private String getRequestUrl() throws Exception {
		String json = readProfile();
		JSONObject jsonObject = JSONObject.parseObject(json);
		String ip = jsonObject.getString("dataWarehouseIp");
		String port = jsonObject.getString("dataWarehousePort");
		return new StringBufferWrapper().append("http://")
				.append(ip)
				.append(":")
				.append(port)
				.append("/actionCtrl")
				.toString();
	}
	
	/**
	 * 获取profile.json配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午2:26:47
	 * @return String json
	 */
	public String readProfile() throws Exception{
		BufferedReader in = null;
		InputStreamReader reader = null;
		String json = null;
		try{
			in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("profile.json")));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null){
			    buffer.append(line);
			}
			json = buffer.toString();
		} finally{
			if(in != null) in.close();
			if(reader != null) reader.close();
		}
		return json;
	}
}
