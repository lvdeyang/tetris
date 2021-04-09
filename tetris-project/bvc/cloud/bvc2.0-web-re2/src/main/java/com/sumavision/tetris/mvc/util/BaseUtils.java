package com.sumavision.tetris.mvc.util;

import java.util.Collection;
import java.util.List;

public class BaseUtils<T> {

	/**
	 * String不为null并不为空字符串<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 上午10:14:42
	 * @return boolean
	 */
	public static boolean stringIsNotBlank(String str) {
		if (str != null && !"".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 集合中有值返回true<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午11:20:38
	 * @param str
	 * @return
	 */
	public static <T> boolean collectionIsNotBlank(Collection<T> collection) {
		if (collection != null && collection.size()>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * <br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午7:04:38
	 * @return 
	 * @return
	 */
	public static <T>T findByList(List<T> list,T t){
		for(T target:list){
			if(target.equals(t)){
				return t;
			}
		}
		return null;
	}
	
	/**
	 * 实例对象不为空<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月3日 下午1:41:57
	 * @param t
	 * @return
	 */
	public static <T>boolean objectIsNotNull(T t){
		return t != null;
	}
	
}
