
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-ui 下午1:51:04  
* All right reserved.  
*  
*/

package com.sumavision.aop.annotation;

import java.lang.annotation.*;

/**
 * @desc: bvc-monitor-ui
 * 自定义aop注解，实例
 * @author: cll
 * @createTime: 2018年6月5日 下午1:51:04
 * @history:
 * @version: v1.0
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {

}
