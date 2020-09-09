package com.sumavision.bvc.vo;

import java.util.List;

import com.sumavision.bvc.vo.ResourceVO.ResourceVOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder=true)
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class BindingResourceVO {
	List<Long> userIds;
	List<String> resourceIds;
}
