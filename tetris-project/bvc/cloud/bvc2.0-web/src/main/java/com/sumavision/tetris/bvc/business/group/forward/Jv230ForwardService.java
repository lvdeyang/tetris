package com.sumavision.tetris.bvc.business.group.forward;

import java.util.List;

import org.apache.hadoop.mapred.taskstats_jsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleDao;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.user.UserQuery;

@Service
public class Jv230ForwardService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	/**
	 * qt终端全部上屏jv230<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:30:12
	 * @param String bundleId jv230 设备id
	 */
	public void totalForward(String bundleId) throws Exception{
		
	}
	
	/**
	 * qt终端切换分屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:02
	 */
	public void changeSplit() throws Exception{
		
	}
	
	/**
	 * qt终端某个分屏内容变化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:24
	 * @param int serialNum 分屏序号
	 */
	public void changeForwardBySerialNum(int serialNum) throws Exception{
		
	}
	
	/**
	 * qt结束某个分屏上的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:57
	 * @param int serialNum 分屏序号
	 */
	public void deleteForwardBySerialNum(int serialNum) throws Exception{
		
	}
	
	/**
	 * qt结束全部上屏内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:32:24
	 */
	public void deleteTotalForward() throws Exception{
		
		
	}
	
}
