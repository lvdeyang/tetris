package com.sumavision.bvc.BO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  MessageRespBO   
 * @Description:合屏执行层消息返回对象  
 * @author: 
 * @date:   2018年7月25日 下午4:29:14   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class MessageRespBO {

	private String method;
	
	private String seq;
	
	//执行结果。1代表成功；其余为失败
	private Integer result;
}
