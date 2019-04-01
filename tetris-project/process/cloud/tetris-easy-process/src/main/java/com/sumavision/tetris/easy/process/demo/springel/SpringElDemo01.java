package com.sumavision.tetris.easy.process.demo.springel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * spring-el表达式使用<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午2:37:42
 */
public class SpringElDemo01 {

	/**
	 * 范围判断<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月13日 下午2:38:23
	 */
	@Test
	public void test01(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("test0", 1);
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression0 = parser.parseExpression("#test0>0 && #test0<2");
		boolean result0 = expression0.getValue(context, boolean.class);
		System.out.println(result0);
		
		//这么写判断相等也是对的
		context.setVariable("test1", "test1");
		Expression expression1 = parser.parseExpression("#test1=='test1'");
		boolean result1 = expression1.getValue(context, boolean.class);
		System.out.println(result1);
		
		Expression expression2 = parser.parseExpression("#test1.equals('test1')");
		boolean result2 = expression2.getValue(context, boolean.class);
		System.out.println(result2);
	}
	
	/**
	 * 枚举判断（方法1）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月13日 下午2:41:14
	 */
	@Test
	public void test02(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		List<String> e = new ArrayList<String>();
		e.add("item1");
		e.add("item2");
		context.setVariable("scope", e);
		
		context.setVariable("test1", "item1");
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression0 = parser.parseExpression("#scope.contains(#test1)");
		boolean result0 = expression0.getValue(context, boolean.class);
		System.out.println(result0);
		
		context.setVariable("test2", "item3");
		Expression expression1 = parser.parseExpression("#scope.contains(#test2)");
		boolean result1 = expression1.getValue(context, boolean.class);
		System.out.println(result1);
	}
	
	/**
	 * 内置校验方法<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月13日 下午2:48:34
	 */
	@Test
	public void test03(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		Constraints constraints = new Constraints();
		context.setVariable("constraints", constraints);
		//spring会自动做类型转换，如果转换不成功会抛异常
		context.setVariable("test1", "1");
		context.setVariable("test1", 1);
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("#constraints.lengthBetween(#test1, -1, 2, true)");
		boolean result = expression.getValue(context, Boolean.class);
		System.out.println(result);
	}
	
	@Test
	public void test04(){
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("T(java.util.Arrays).asList({'aa', 'bb', 'cc', 'dd'}).contains('bb')");
		boolean result = expression.getValue(Boolean.class);
		System.out.println(result);
	}
	
	@Test
	public void test05(){
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("'500.11' matches '^-?\\d+(\\.\\d{2})+$'");
		boolean result = expression.getValue(Boolean.class);
		System.out.println(result);
	}
	
	@Test
	public void test06(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		Map<String, Object> internalConstraint = new HashMap<String, Object>();
		Constraints constraints = new Constraints();
		internalConstraint.put("com.sumavision.tetris.mims.demo.springel.Constraints", constraints);
		context.setVariable("test1", 1);
		context.setVariable("internalConstraints", internalConstraint);
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("#internalConstraints.get('com.sumavision.tetris.mims.demo.springel.Constraints').lengthBetween(#test1, -1, 2, true)");
		boolean result = expression.getValue(context, Boolean.class);
		System.out.println(result);
	}
	
	@Test
	public void test07(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("_3_param1", 10);
		context.setVariable("_3_param2", 30);
		String exp = "#_3_param1==10?T(java.util.Arrays).asList({20, 30, 40}).contains(#_3_param2):#_3_param2==10";
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(exp);
		boolean result = expression.getValue(context, Boolean.class);
		System.out.println(result);
	}
	
	@Test
	public void test08(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("test1", 5);
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("#test1&lt;=5");
		boolean result = expression.getValue(context, Boolean.class);
		System.out.println(result);
	}
	
	@Test
	public void test09(){
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("test1", 5);
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("#test1+3");
		String result = expression.getValue(context, String.class);
		System.out.println(result);
	}
	
}

class Constraints{
	
	/**
	 * 区间判断<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月13日 下午2:53:01
	 * @param int v 目标值
	 * @param int min 下限
	 * @param int max 上限
	 * @param int boundary 是否包含边界
	 * @return boolean 判断结果
	 */
	public boolean lengthBetween(int v, int min, int max, boolean boundary){
		if(boundary){
			return v>=min && v<=max;
		}else{
			return v>min && v<max;
		}
	}
	
}
