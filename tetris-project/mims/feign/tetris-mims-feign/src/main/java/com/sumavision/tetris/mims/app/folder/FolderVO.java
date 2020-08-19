package com.sumavision.tetris.mims.app.folder;

public class FolderVO {
	private String uuid; 
	
	/** 文件夹名称 */
	private String name;
	
	/** 父目录id */
	private Long parentId;
	
	/** 父目录路径，格式：/id/id/id */
	private String parentPath;
	
	/** 节点的深度, 从0开始 */
	private Integer depth;
	
	/** 文件夹类型 */
	private FolderType type;
	
	/** 创建者id */
	private String authorId;
	
	/** 创建者名称 */
	private String authorName;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public FolderType getType() {
		return type;
	}

	public void setType(FolderType type) {
		this.type = type;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
}
