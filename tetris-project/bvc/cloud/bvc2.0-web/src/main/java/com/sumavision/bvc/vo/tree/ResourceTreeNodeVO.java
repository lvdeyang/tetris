package com.sumavision.bvc.vo.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceTreeNodeVO implements Serializable {
	private Long groupId;
	private String name;
	private Long parentId;
	//页面显示bundle的Id
	private String bundle_Id;
	private Boolean isEdit = false;
	private Boolean isFolder;
	private List<ResourceTreeNodeVO> children = new ArrayList<>();
	
}
