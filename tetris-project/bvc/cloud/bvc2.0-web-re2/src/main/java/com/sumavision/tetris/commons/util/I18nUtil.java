/**
 * Copyright (C) 2014 Sumavision
 *
 *
 * @className:platform.base.util.I18nUtil
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:zhuzheng
 * 
 */
package com.sumavision.tetris.commons.util;

import com.sumavision.tetris.commons.context.SpringContext;

public class I18nUtil {
	private I18nUtil(){}
	
	public static String i18n(String... codes){
		StringBuilder sb = new StringBuilder();
		for (String code:codes){
			sb.append(SpringContext.i18n(code, null));
		}
		return sb.toString();
	}
}
