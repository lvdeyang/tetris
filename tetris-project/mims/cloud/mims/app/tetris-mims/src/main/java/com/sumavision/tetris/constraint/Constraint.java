package com.sumavision.tetris.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内部约束定义<br/>
 * <b>作者:</b>lvdeynag<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午5:38:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Constraint {

	/** 约束名称 */
	String name();
	
	/** 备注 */
	String remarks() default "";
	
}
