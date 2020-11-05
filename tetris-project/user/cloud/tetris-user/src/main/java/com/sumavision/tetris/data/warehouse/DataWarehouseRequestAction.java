package com.sumavision.tetris.data.warehouse;

public enum DataWarehouseRequestAction {
	DO_AUTH("鉴权", "doAuth"),
	CREATE_PROJECT("创建项目", "createProject"),
	GET_PROJECT_LIST("获取项目列表", "getProjectList"),
	GET_PROJECT_HISTORY_LIST("获取项目历史", "getProjectHistoryList"),
	DELETE_PROJECT("删除项目", "deleteProject"),
	DOWNLOAD_PROJECT_ZIP("下载完整项目zip", "downloadProjectZip"),
	GET_FILE_LIST("获取文件列表", "getFileList"),
	GET_FILE_HISTORY_LIST("获取文件历史", "getFileHistoryList"),
	UPLOAD_FILE("上传文件", "uploadFile"),
	GET_FILE_DETAIL("获取文件内容", "getFileDetail"),
	DOWNLOAD_FILE("下载文件", "downloadFile"),
	DELETE_FILE("删除文件", "deleteFile"),
	GET_USER_DETAIL("获取用户信息", "getUserDetail");
	
	/** 描述 */
	private String detail;
	/** 请求action值 */
	private String action;
	
	private DataWarehouseRequestAction(String detail, String action) {
		this.detail = detail;
		this.action = action;
	}

	public String getDetail() {
		return detail;
	}

	public String getAction() {
		return action;
	}
}
