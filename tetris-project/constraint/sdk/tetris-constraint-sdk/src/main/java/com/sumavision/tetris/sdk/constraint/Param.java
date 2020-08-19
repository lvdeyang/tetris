package com.sumavision.tetris.sdk.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 约束参数<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月14日 上午10:18:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Repeatable(Params.class)
public @interface Param {

	/** 参数名称 */
	String name();
	
	/** 参数约束 */
	String constraintExpression() default "";  
	
	/** 参数顺序 */
	int serial();
	
	/** 备注说明 */
	String remarks() default "";
	
	/** 数据类型 */
	ParamType type() default ParamType.STRING;
	
	/** 枚举可选值 */
	String[] enums() default {};
	
}
