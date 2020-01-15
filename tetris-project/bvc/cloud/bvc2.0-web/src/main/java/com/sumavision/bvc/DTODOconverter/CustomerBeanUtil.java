/**   

* @Title: CustomerBeanUtil.java 

* @Package com.sumavision.bvc.converter 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月13日 上午10:50:02 

* @version V1.0   

*/

package com.sumavision.bvc.DTODOconverter;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;

/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-service
 * 
 * 类名称：CustomerBeanUtil
 * 
 * 类描述：
 * 
 * 创建人：rain
 * 
 * 创建时间：2018年6月13日 上午10:50:02
 * 
 * 修改人：rain
 * 
 * 修改时间：2018年6月13日 上午10:50:02
 * 
 * 修改备注：
 * 
 * @version
 *
 * 
 * 
 */

public class CustomerBeanUtil extends BeanUtils {  
    static {  
        // 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空  
        ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);  
        // 注册util.date的转换器，即允许BeanUtils.copyProperties时的源目标的util类型的值允许为空  
        ConvertUtils.register(new DateConverter(), java.util.Date.class);  
    }  
  
    public static void copyProperties(Object target, Object source)  
        throws InvocationTargetException, IllegalAccessException {  
    	
        BeanUtils.copyProperties(target, source);  
    }  
  
}  
