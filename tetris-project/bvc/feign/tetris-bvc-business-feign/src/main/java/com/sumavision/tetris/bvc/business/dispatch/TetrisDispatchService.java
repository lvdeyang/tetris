package com.sumavision.tetris.bvc.business.dispatch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;

@Service
public class TetrisDispatchService {

	@Autowired
	private DispatchFeign dispatchFeign;
	
	/**
	 * 执行调度<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午5:02:01
	 * @param dispatch
	 * @throws Exception
	 */
	public void dispatch(DispatchBO dispatch) throws Exception{
		dispatchFeign.dispatch(JSON.toJSONString(dispatch));
	}
	
	/**
	 * 批量透传<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午5:02:16
	 * @param passbyBOs
	 * @throws Exception
	 */
	public void dispatch(List<PassByBO> passbyBOs) throws Exception{
		dispatchFeign.passby(JSON.toJSONString(passbyBOs));
	}
	
}
