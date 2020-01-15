package com.sumavision.bvc.DTODOconverter;

import org.apache.commons.beanutils.Converter;
/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-service
 * 
 * 类名称：DateConverter
 * 
 * 类描述：日期转换器
 * 
 * 创建人：cll
 * 
 * 创建时间：2018年6月13日 上午10:41:32
 * 
 * 修改人：cll
 * 
 * 修改时间：2018年6月13日 上午10:41:32
 * 
 * 修改备注：
 * 
 * @version
 *
 * 
 * 
 */

public class DateConverter implements Converter {
	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class type, Object value) {

		if (value == null) {
			return null;
		}

		// 相同类型不需要转换
		if (type.equals(value.getClass())) {
			return value;
		}

		// 字符串转日期格式 TODO 自定义格式转换
		if (value instanceof String) {
			return null;
			//return DateUtil.getDateFormatStr(((String) value).trim(), DateUtil.);
		}

		// 其他不支持
		return null;
	}
}
