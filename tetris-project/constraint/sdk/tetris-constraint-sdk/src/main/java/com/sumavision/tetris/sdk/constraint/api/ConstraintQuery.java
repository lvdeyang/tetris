package com.sumavision.tetris.sdk.constraint.api;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.sdk.constraint.Constraint;
import com.sumavision.tetris.sdk.constraint.ConstraintBO;
import com.sumavision.tetris.sdk.constraint.InternalConstraintBean;
import com.sumavision.tetris.sdk.constraint.Param;
import com.sumavision.tetris.sdk.constraint.ParamBO;
import com.sumavision.tetris.sdk.constraint.Params;

/**
 * 约束查询器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午5:35:12
 */
@Component
public class ConstraintQuery {

	/**
	 * 获取所有的约束bean<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月14日 上午10:38:02
	 * @return List<ConstraintBO<InternalConstraintBean>> 约束列表
	 */
	@Cacheable(value = "constraintCache")
	public List<ConstraintBO<InternalConstraintBean>> list() throws Exception{
		Map<String, InternalConstraintBean> beanMap = SpringContext.getBeanOfType(InternalConstraintBean.class);
		if(beanMap==null || beanMap.size()<=0) return null;
		Collection<InternalConstraintBean> beans = beanMap.values();
		List<ConstraintBO<InternalConstraintBean>> constraints = new ArrayList<ConstraintBO<InternalConstraintBean>>();
		for(InternalConstraintBean bean:beans){
			Class<? extends InternalConstraintBean> clazz = bean.getClass();
			Method[] methods = clazz.getDeclaredMethods();
			if(methods!=null && methods.length>0){
				for(Method method:methods){
					Constraint ann_cons = method.getDeclaredAnnotation(Constraint.class);
					if(ann_cons == null) continue;
					ConstraintBO<InternalConstraintBean> constraint = new ConstraintBO<InternalConstraintBean>();
					constraint.setBean(bean);
					constraint.setBeanClass(clazz);
					constraint.setBeanId(clazz.getName());
					constraint.setBeanName(clazz.getName());
					constraint.setId(new StringBufferWrapper().append("#internalConstraints.get('").append(clazz.getName()).append("').").append(method.getName()).toString());
					constraint.setName(ann_cons.name());
					constraint.setRemarks(ann_cons.remarks());
					Parameter[] parameters = method.getParameters();
					if(parameters!=null && parameters.length>0){
						constraint.setParams(new ArrayList<ParamBO>());
						for(int i=0; i<parameters.length; i++){
							Parameter curr_parameter = parameters[i];
							Params ann_params = method.getDeclaredAnnotation(Params.class);
							Param[] ann_paramArray = ann_params.value();
							Param curr_ann_p = null;
							for(Param ann_p:ann_paramArray){
								if(ann_p.serial() == i){
									curr_ann_p = ann_p;
									break;
								}
							}
							ParamBO param = new ParamBO();
							param.setType(curr_parameter.getType());
							param.setKey(curr_parameter.getName());
							param.setName(curr_ann_p.name());
							param.setConstraintExpression(curr_ann_p.constraintExpression());
							param.setSerial(curr_ann_p.serial());
							param.setRemarks(curr_ann_p.remarks());
							param.setParamType(curr_ann_p.type());
							param.setEnums(Arrays.asList(curr_ann_p.enums()));
							constraint.getParams().add(param);
						}
						Collections.sort(constraint.getParams(), new ParamBO.ParamComparator());
					}
					constraints.add(constraint);
				}
			}
		}
		return constraints;
	} 
	
	/**
	 * 格式化查询内置约束用于spel校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午2:00:10
	 * @return Map<String, InternalConstraintBean> 约束列表
	 */
	@Cacheable(value = "formatConstraintCache")
	public Map<String, InternalConstraintBean> formatList() throws Exception{
		Map<String, InternalConstraintBean> internalConstraints = new HashMap<String, InternalConstraintBean>();
		List<ConstraintBO<InternalConstraintBean>> constraintBeans = list();
		if(constraintBeans!=null && constraintBeans.size()>0){
			for(ConstraintBO<InternalConstraintBean> constraintBean:constraintBeans){
				internalConstraints.put(constraintBean.getBeanId(), constraintBean.getBean());
			}
		}
		return internalConstraints;
	}
	
}
