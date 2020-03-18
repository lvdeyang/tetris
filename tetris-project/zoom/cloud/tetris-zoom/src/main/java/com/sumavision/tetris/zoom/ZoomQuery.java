package com.sumavision.tetris.zoom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ZoomQuery {

	/**
	 * 封装会议成员的bundle信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午2:09:07
	 * @param List<ZoomMemberVO> members 成员列表
	 * @return List<ZoomMemberVO> 成员列表
	 */
	private List<ZoomMemberVO> queryBundleInfo(List<ZoomMemberVO> members) throws Exception{
		List<ZoomMemberVO> terminals = new ArrayList<ZoomMemberVO>();
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO member:members){
			
		}
		
		
		
		return members;
	}
	
	/**
	 * 封装会议成员的bundle信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午2:09:07
	 * @param ZoomVO zoom 会议室信息
	 * @return ZoomVO 会议室信息
	 */
	private ZoomVO queryBundleInfo(ZoomVO zoom) throws Exception{
		
		return zoom;
	}
	
}
