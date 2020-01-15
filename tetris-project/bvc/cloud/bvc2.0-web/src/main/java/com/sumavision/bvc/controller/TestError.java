/**  

 * Project Name:monitor-web  
 * File Name:etst.java  
 * Package Name:com.sumavision.bvc.controller  
 * Date:2018年6月28日上午10:10:02  
 * Copyright (c) 2018, caoleili@126.com All Rights Reserved.  
 *  
*/

package com.sumavision.bvc.controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName:etst <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年6月28日 上午10:10:02 <br/>
 * 
 * @author cll
 * @version
 * @since JDK 1.8
 * @see
 */
@Controller
@RequestMapping("/testError")
public class TestError {
	/**
	 * 随机抛出异常.
	 */
	private void randomException() throws Exception {
		Exception[] exceptions = { // 异常集合
				new NullPointerException(), new ArrayIndexOutOfBoundsException(), new NumberFormatException(),
				new SQLException() };
		// 发生概率
		double probability = 0.75;
		if (Math.random() < probability) {
			// 情况1：要么抛出异常
			throw exceptions[(int) (Math.random() * exceptions.length)];
		} else {
			// 情况2：要么继续运行
		}

	}

	/**
	 * 模拟用户数据访问.
	 */
	@GetMapping("/")
	public List index() throws Exception {
		randomException();
		return Arrays.asList("正常用户数据1!", "正常用户数据2! 请按F5刷新!!");
	}
}
