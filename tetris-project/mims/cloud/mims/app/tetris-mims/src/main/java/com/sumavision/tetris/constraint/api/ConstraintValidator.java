package com.sumavision.tetris.constraint.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.constraint.InternalConstraintBean;

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
	 * @param String value 带测值
	 * @param String constraintExpression 约束表达式
	 * @return boolean 检验结果
	 */
	public boolean validate(String primaryKey, String value, String constraintExpression) throws Exception{
		
		//加入内置约束校验
		Map<String, InternalConstraintBean> internalConstraints = constraintQuery.formatList();
		
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable(primaryKey, value);
		context.setVariable("internalConstraints", internalConstraints);
		
		Expression expression = spelExpressionParser.parseExpression(constraintExpression);
		boolean result = expression.getValue(context, boolean.class);
		return result;
	}
	
}
