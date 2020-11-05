package com.sumavision.tetris.sdk.constraint.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.sdk.constraint.Constraint;
import com.sumavision.tetris.sdk.constraint.InternalConstraintBean;
import com.sumavision.tetris.sdk.constraint.Param;
import com.sumavision.tetris.sdk.constraint.ParamType;
import com.sumavision.tetris.sdk.constraint.Params;

@Component
public class DemoConstraint implements InternalConstraintBean{

	/**
	 * 判断是否为数字<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月14日 上午10:28:16
	 * @param Object value 参数值
	 * @return boolean 判断结果
	 */
	@Constraint(
			name = "判断是否为数字", 
			remarks = "判断目标值是否为数字")
	@Params({
		@Param(name = "value", serial = 0, type = ParamType.TARGET, remarks="带测值")
	})
	public boolean isNumber(Object value){
		Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(value.toString());
        if( !isNum.matches() ){
            return false;
        }
        return true;
	}
	
	/**
	 * 长度范围<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月20日 下午2:47:24
	 * @param Object value 带测值
	 * @param Integer min 下限值
	 * @param Integer max 上限值
	 * @param boolean boundary 是否带边界
	 * @return
	 */
	@Constraint(
			name = "字符长度范围", 
			remarks = "字符长度范围")
	@Params({
		@Param(name = "value", serial = 0, remarks = "待测值", type = ParamType.TARGET),
		@Param(name = "min", serial = 1, remarks = "下限长度", type = ParamType.NUMBER),
		@Param(name = "max", serial = 2, remarks = "上限长度", type = ParamType.NUMBER),
		@Param(name = "boundary", serial = 3, remarks = "是否带边界", type = ParamType.BOOLEAN)
	})
	public boolean lengthBetween(Object value, Integer min, Integer max, boolean boundary){
		if(!(value instanceof String)) return false; 
		int length = value.toString().length();
		if(boundary){
			if(length>=min.intValue() && length<=max.intValue()){
				return true;
			}else{
				return false;
			}
		}else{
			if(length>min.intValue() && length<max.intValue()){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 值域<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月20日 下午2:41:39
	 * @param Object value 带测值
	 * @param Integer min 下限
	 * @param Integer max 上限
	 * @param boolean boundary 是否包含边界
	 */
	@Constraint(
			name = "值域", 
			remarks = "取值范围")
	@Params({
		@Param(name = "value", serial = 0, remarks = "待测值", type = ParamType.TARGET),
		@Param(name = "min", serial = 1, remarks = "下限值", type = ParamType.NUMBER),
		@Param(name = "max", serial = 2, remarks = "上限值", type = ParamType.NUMBER),
		@Param(name = "boundary", serial = 3, remarks = "是否带边界", type = ParamType.BOOLEAN)
	})
	public boolean betweent(Object value, Integer min, Integer max, boolean boundary){
		if(!isNumber(value)) return false;
		float v = Float.parseFloat(value.toString());
		if(boundary){
			if(v>=min.intValue() && v<=max.intValue()){
				return true;
			}else{
				return false;
			}
		}else{
			if(v>min.intValue() && v<max.intValue()){
				return true;
			}else{
				return false;
			}
		}
	}
	
}
