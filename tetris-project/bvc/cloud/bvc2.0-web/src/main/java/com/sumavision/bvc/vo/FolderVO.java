package com.sumavision.bvc.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder=true)
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class FolderVO {
	private Long groupId;
	private String name;
	private Long parentId; 
}
