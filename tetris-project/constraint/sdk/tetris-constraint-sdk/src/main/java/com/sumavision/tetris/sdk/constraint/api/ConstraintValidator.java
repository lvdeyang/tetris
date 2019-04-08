package com.sumavision.tetris.sdk.constraint.api;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.sdk.constraint.InternalConstraintBean;

/**
 * 约束校验器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月9日 下午1:38:52
 */
@Component
public class ConstraintValidator {

	@Autowired
	private SpelExpressionParser spelExpressionParser;
	
	@Autowired
	private ConstraintQuery constraintQuery;
	
	/**
	 * 校验方法<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午1:43:24
	 * @param JSONObject params 带测值上下文环境
	 * @param String constraintExpression 约束表达式
	 * @return boolean 检验结果
	 */
	public boolean validate(JSONObject params, String constraintExpression) throws Exception{
		
		if(constraintExpression==null || "".equals(constraintExpression)) return true;
		
		//加入内置约束校验
		Map<String, InternalConstraintBean> internalConstraints = constraintQuery.formatList();
		
		StandardEvaluationContext context = new StandardEvaluationContext();
		if(params!=null && params.size()>0){
			Set<String> keys = params.keySet();
			for(String key:keys){
				context.setVariable(key, params.get(key));
			}
		}
		context.setVariable("internalConstraints", internalConstraints);
		
		Expression expression = spelExpressionParser.parseExpression(constraintExpression);
		boolean result = expression.getValue(context, boolean.class);
		return result;
	}
	
	/**
	 * 校验方法<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午1:43:24
	 * @param String primaryKey 待测主键
	 * @param String value 带测值
	 * @param String constraintExpression 约束表达式
	 * @return boolean 检验结果
	 */
	public boolean validate(String primaryKey, String value, String constraintExpression) throws Exception{
		JSONObject params = new JSONObject();
		params.put(primaryKey, value);
		return validate(params, constraintExpression);
	}
	
	/**
	 * 获取表达式的值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午4:47:21
	 * @param JSONObject params 带测值上下文环境
	 * @param String expressionValue 表达式
	 * @return String 值
	 */
	public String getStringValue(JSONObject params, String expressionValue) throws Exception{
		return ConstraintValidator.getValue(params, expressionValue, String.class);
	}
	
	/**
	 * 获取表达式的值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午4:47:21
	 * @param JSONObject params 带测值上下文环境
	 * @param String expressionValue 表达式
	 * @return Integer 值
	 */
	public Integer getIntegerValue(JSONObject params, String expressionValue) throws Exception{
		return ConstraintValidator.getValue(params, expressionValue, Integer.class);
	}
	
	/**
	 * 获取表达式的值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午4:47:21
	 * @param JSONObject params 带测值上下文环境
	 * @param String expressionValue 表达式
	 * @return Boolean 值
	 */
	public Boolean getBooleanValue(JSONObject params, String expressionValue) throws Exception{
		return ConstraintValidator.getValue(params, expressionValue, Boolean.class);
	}
	
	/**
	 * 获取表达式的值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午4:47:21
	 * @param JSONObject params 带测值上下文环境
	 * @param String expressionValue 表达式
	 * @return Long 值
	 */
	public Long getLongValue(JSONObject params, String expressionValue) throws Exception{
		return ConstraintValidator.getValue(params, expressionValue, Long.class);
	}
	
	/**
	 * 获取表达式的值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午4:47:21
	 * @param JSONObject params 带测值上下文环境
	 * @param String expressionValue 表达式
	 * @return Float 值
	 */
	public Float getFloatValue(JSONObject params, String expressionValue) throws Exception{
		return ConstraintValidator.getValue(params, expressionValue, Float.class);
	}
	
	/**
	 * 获取表达式的值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午4:47:21
	 * @param JSONObject params 带测值上下文环境
	 * @param String expressionValue 表达式
	 * @param Class<T> type 类型
	 * @return <T>T 值
	 */
	private static <T>T getValue(JSONObject params, String expressionValue, Class<T> type) throws Exception{
		StandardEvaluationContext context = new StandardEvaluationContext();
		if(params!=null && params.size()>0){
			Set<String> keys = params.keySet();
			for(String key:keys){
				context.setVariable(key, params.getJSONObject(key));
			}
		}
		Expression expression = ((TemplateAwareExpressionParser)SpringContext.getBean(SpelExpressionParser.class)).parseExpression(expressionValue);
		return expression.getValue(context, type);
	}
	
}
