package com.sumavision.tetris.mims.app.folder;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 文件夹结构<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 上午11:45:20
 */
@Entity
@Table(name = "MIMS_FOLDER")
public class FolderPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
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
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_PATH")
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
	@Column(name = "DEPTH")
	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public FolderType getType() {
		return type;
	}

	public void setType(FolderType type) {
		this.type = type;
	}
	
	@Column(name = "AUTHOR_ID")
	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * 根据parentPath计算节点深度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:06:11
	 */
	public void setDepth(){
		String path = this.getParentPath();
		if(path==null || "".equals(path)){
			this.depth = 1;
		}else{
			this.depth = path.split("/").length;
		}
	}
	
	/**
	 * 文件夹复制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午10:17:31
	 * @param String name 自定义文件夹名称
	 * @param FolderPO parent 父文件夹
	 * @return FolderPO 新文件夹
	 */
	public FolderPO copy(String name, FolderPO parent){
		FolderPO copy_folder = new FolderPO();
		copy_folder.setUuid(this.getUuid());
		copy_folder.setUpdateTime(new Date());
		copy_folder.setName(name==null?this.getName():name);
		copy_folder.setType(this.getType());
		copy_folder.setParentId(parent==null?null:parent.getId());
		String parentPath = null;
		if(parent != null){
			if(parent.getParentPath() == null){
				parentPath = new StringBufferWrapper().append("/")
													  .append(parent.getId())
													  .toString();
			}else{
				parentPath = new StringBufferWrapper().append(parent.getParentPath())
													  .append("/")
													  .append(parent.getId())
													  .toString();
			}
		}
		copy_folder.setParentPath(parentPath);
		copy_folder.setDepth();
		return copy_folder;
	}
	
	/**
	 * 复制文件夹基本信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:05:26
	 * @return
	 */
	public FolderPO copy(String name){
		FolderPO copy_folder = new FolderPO();
		copy_folder.setUuid(this.getUuid());
		copy_folder.setUpdateTime(new Date());
		copy_folder.setName(name==null?this.getName():name);
		copy_folder.setType(this.getType());
		return copy_folder;
	}
	
}
