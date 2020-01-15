       
        /*  
        * Copyright @ 2018 com.iflysse.trains  
        * bvc-monitor-ui 上午9:45:28  
        * All right reserved.  
        *  
        */  

package com.sumavision.bvc.DTO;

import java.util.HashMap;



/**  
        * @desc: bvc-monitor-ui  
        * @author: cll 
        * @createTime: 2018年6月5日 上午9:45:28  
        * @history:  
        * @describe: key包括：code-200/500, msg-success, operateIndex, operateCount, lockResult-true/false, taskId-ChannelStatusBody
        * @version: v1.0    
        */

public class ResultMap extends HashMap<String,Object> {
	
	private static final long serialVersionUID = 1L;
	
	//---value值---
	public static final String SUCCESS = "success";
	
	public static final String FAILURE = "failure";
	
	//---key值---
	public static final String CODE = "code";
	
	public static final String LOCK_RESULT = "lockResult";
	
	public static final String OPERATE_INDEX = "operateIndex";
	
	public static final String OPERATE_COUNT = "operateCount";
	
	public static final String BUNDLE_RESULT_MAP_LIST = "bundleResultMapList";
	
	public ResultMap() { }

	/**
	 * 返回成功
	 */
	public static ResultMap ok() {
		return ok(200, "success");
	}

	/**
	 * 返回成功
	 */
	public static ResultMap ok(int code, String message) {
		ResultMap resultMap = new ResultMap();
		resultMap.put("code", code);
		resultMap.put("msg", message);
		return resultMap;
	}
	
	/**
	 * 返回成功及operateIndex，operateCount设为0
	 */
	public static ResultMap ok(int operateIndex) {
		ResultMap resultMap = ok();
		resultMap.put("operateIndex", operateIndex);
		resultMap.put("operateCount", 0);
		return resultMap;
	}
	
	/**
	 * 返回成功、operateIndex及opearateCount
	 */
	public static ResultMap ok(int operateIndex, int operateCount) {
		ResultMap resultMap = ok();
		resultMap.put("operateIndex", operateIndex);
		resultMap.put("operateCount", operateCount);
		return resultMap;
	}
	
	/**
	 * 返回失败
	 */
	public static ResultMap failure() {
		return failure(500, "failure");
	}

	/**
	 * 返回失败
	 */
	public static ResultMap failure(int code, String message) {
		ResultMap resultMap = new ResultMap();
		resultMap.put("code", code);
		resultMap.put("msg", message);
		return resultMap;
	}
	
	/**
	 * 放入object
	 */
	@Override
	public ResultMap put(String key, Object object){
		super.put(key, object);
		return this;
	}
}
  
    