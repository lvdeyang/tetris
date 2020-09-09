package com.sumavision.bvc.device.group.bo;

import com.suma.venus.resource.pojo.FolderPO;

public class FolderBO {
	
	private Long id;
	
	//文件夹名
	private String name;
	
	//父级文件夹id
	private Long parentId;
	
	//父级文件夹所在层级/1/2/3
	private String parentPath;
	
	public Long getId() {
		return id;
	}

	public FolderBO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public FolderBO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public FolderBO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getParentPath() {
		return parentPath;
	}

	public FolderBO setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return this;
	}
	
	/**
	 * @Title: 从持久化数据中复制<br/> 
	 * @param entity 持久化数据
	 * @return FolderBO 业务数据
	 */
	public FolderBO set(FolderPO entity){
		this.setId(entity.getId())
		    .setName(entity.getName())
		    .setParentId(entity.getParentId())
		    .setParentPath(entity.getParentPath());
		return this;
	}
}
