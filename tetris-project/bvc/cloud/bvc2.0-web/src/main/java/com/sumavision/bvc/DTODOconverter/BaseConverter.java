/**   

* @Title: BaseConverter.java 

* @Package com.sumavision.bvc.util 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月13日 上午10:08:44 

* @version V1.0   

*/

package com.sumavision.bvc.DTODOconverter;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-service
 * 
 * 类名称：BaseConverterUtiil
 * 
 * 类描述：DTO转化DO工具类
 * 
 * 创建人：cll
 * 
 * 创建时间：2018年6月13日 上午10:08:44
 * 
 * 修改人：cll
 * 
 * 修改时间：2018年6月13日 上午10:08:44
 * 
 * 修改备注：
 * 
 * @version
 *
 * 
 * 
 */
@Slf4j
@Service
public abstract class BaseConverter<K, V> {
	public enum ConverterFlag {
		DTO2DO, DO2DTO
	}

	/**
	 * 值对象与域对象之间属性复制
	 * 
	 * @param dto
	 *            值对象
	 * @param domain
	 *            域对象
	 * @param flag
	 *            复制方向
	 */
	public  void copyProperties(K dto, V domain, ConverterFlag flag) {
		switch (flag) {
		case DO2DTO:
			copySameProperties(dto, domain);
			copyDiffPropertiesFromDO2DTO(dto, domain);
			break;

		case DTO2DO:
			copySameProperties(domain, dto);
			copyDiffPropertiesFromDTO2DO(domain, dto);
			break;

		default:
			break;
		}

	}

	/**
	 * 同名属性复制
	 * 
	 * @param target
	 *            目标对象
	 * @param source
	 *            来源对象
	 */
	protected static void copySameProperties(Object target, Object source) {

		try {
			CustomerBeanUtil.copyProperties(target, source);
		} catch (IllegalAccessException e) {
			log.error("对象属性值复制出错：原数据为{}， 目标数据为{}。", source, target);
		} catch (InvocationTargetException e) {
			log.error("对象属性值复制出错：原数据为{}， 目标数据为{}。", source, target);
		}
	}

	/**
	 * VO非同名属性复制到PO属性
	 * 
	 * @param target
	 *            域对象
	 * @param source
	 *            值对象
	 */
	protected abstract void copyDiffPropertiesFromDO2DTO(K target, V source);

	/**
	 * PO非同名属性复制到VO属性
	 * 
	 * @param target
	 *            值对象
	 * @param source
	 *            域对象
	 */
	protected abstract void copyDiffPropertiesFromDTO2DO(V target, K source);
}
