package com.sumavision.tetris.tool.test.flow.client.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: 处理测试结果返回<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午4:47:29 
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface TestResult {

}
