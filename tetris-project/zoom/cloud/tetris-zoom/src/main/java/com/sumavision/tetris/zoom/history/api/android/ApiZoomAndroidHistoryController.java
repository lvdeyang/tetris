package com.sumavision.tetris.zoom.history.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.history.HistoryQuery;
import com.sumavision.tetris.zoom.history.HistoryService;

@Controller
@RequestMapping(value = "/api/zoom/android/history")
public class ApiZoomAndroidHistoryController {

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private HistoryQuery historyQuery;
	
	/**
	 * 删除历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:04:42
	 * @param Long id 历史记录id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id) throws Exception{
		
		historyService.remove(id);
		return null;
	}
	
	/**
	 * 分页查询用户历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:26:27
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<HistoryVO> 历史记录列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object zoomList(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return historyQuery.zoomList(currentPage, pageSize);
	}
	
	/**
	 * 查询用户历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午6:00:02
	 * @return List<HistoryVO> 历史记录列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/zoom/list/all")
	public Object zoomListAll() throws Exception{
		
		return historyQuery.zoomListAll();
	}
	
	
}
