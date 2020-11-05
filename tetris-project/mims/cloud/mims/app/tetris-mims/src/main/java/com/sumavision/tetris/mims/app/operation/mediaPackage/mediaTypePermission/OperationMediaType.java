package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission;

import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum OperationMediaType {
	PICTURE("图片资源", "picture", FolderType.COMPANY_PICTURE),
	VIDEO("视频资源", "video", FolderType.COMPANY_VIDEO),
	AUDIO("音频资源", "audio", FolderType.COMPANY_AUDIO),
	TXT("文本资源", "txt", FolderType.COMPANY_TXT);
//	COMPRESS("压缩包资源", "compress", FolderType.COMPANY_COMPRESS);
	

	private String name;
	
	private String primaryKey;
	
	private FolderType folderType;
	
	private OperationMediaType(String name){
		this.name = name;
	}
	
	private OperationMediaType(String name, String primaryKey, FolderType folderType){
		this.name = name;
		this.primaryKey = primaryKey;
		this.folderType = folderType;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPrimaryKey(){
		return this.primaryKey;
	}
	
	public FolderType getFolderType(){
		return this.folderType;
	}
	
	public static OperationMediaType fromName(String name) throws Exception{
		OperationMediaType[] values = OperationMediaType.values();
		for(OperationMediaType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static OperationMediaType fromPrimaryKey(String primaryKey) throws Exception{
		OperationMediaType[] values = OperationMediaType.values();
		for(OperationMediaType value:values){
			if(value.getPrimaryKey()!=null && value.getPrimaryKey().equals(primaryKey)){
				return value;
			}
		}
		throw new ErrorTypeException("primaryKey", primaryKey);
	}
	
	public static OperationMediaType fromFolderType(FolderType folderType) throws Exception{
		OperationMediaType[] values = OperationMediaType.values();
		for(OperationMediaType value:values){
			if(value.getFolderType()!=null && value.getFolderType() == folderType){
				return value;
			}
		}
		throw new ErrorTypeException("folderType", folderType);
	}
}
