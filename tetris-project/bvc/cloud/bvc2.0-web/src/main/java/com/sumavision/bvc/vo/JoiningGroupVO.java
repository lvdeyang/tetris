package com.sumavision.bvc.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder=true)
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class JoiningGroupVO {
	String folderJsonString;
	String[] resourceIds;
	String children;
}
