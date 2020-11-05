package com.sumavision.bvc.vo;

import lombok.Builder;
import lombok.Data;


@Builder(toBuilder=true)
@Data
public class UserVO {
	
	private Long id;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;
	
}
