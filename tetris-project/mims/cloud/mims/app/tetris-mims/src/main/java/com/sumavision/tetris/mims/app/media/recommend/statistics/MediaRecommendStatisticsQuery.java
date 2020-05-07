package com.sumavision.tetris.mims.app.media.recommend.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mims.app.media.recommend.statistics.MediaRecommendStatisticsPO.MediaRecommendStatisticsDateOrderComparator;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class MediaRecommendStatisticsQuery {
	@Autowired
	private MediaRecommendStatisticsDAO mediaRecommendStatisticsDAO;
	
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取节目单推送年份统计<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 上午11:53:36
	 * @return List<MediaRecommendStatisticsPO> 节目单播送关联
	 */
	public List<MediaRecommendStatisticsPO> queryRecommendStatisticsByYear() throws Exception{
		UserVO user = userQuery.current();
		List<MediaRecommendStatisticsPO> statisticsPOs = mediaRecommendStatisticsDAO.findByGroupIdOrderByDateAsc(user.getGroupId());
		Map<String, MediaRecommendStatisticsPO> map = new HashMap<String, MediaRecommendStatisticsPO>();
		for (MediaRecommendStatisticsPO mediaRecommendStatisticsPO : statisticsPOs) {
			if (map.containsKey(mediaRecommendStatisticsPO.getDate())) {
				MediaRecommendStatisticsPO item = map.get(mediaRecommendStatisticsPO.getDate());
				item.setCount(item.getCount() + mediaRecommendStatisticsPO.getCount());
			} else {
				map.put(mediaRecommendStatisticsPO.getDate(), mediaRecommendStatisticsPO);
			}
		}
		List<MediaRecommendStatisticsPO> retuList = new ArrayList<MediaRecommendStatisticsPO>();
		for (String key : map.keySet()) {
			retuList.add(map.get(key));
		}
		Collections.sort(retuList, new MediaRecommendStatisticsDateOrderComparator());
		return retuList;
	}
	
	/**
	 * 获取节目单推送地区统计<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 上午11:53:36
	 * @return JSONObject 标签和播送量键值对
	 */
	public JSONObject queryRecommendStatisticsByArea() throws Exception {
		UserVO user = userQuery.current();
		List<UserVO> userVOs = userQuery.listByCompanyIdWithExceptAndClassify(Long.parseLong(user.getGroupId()), null, UserClassify.COMPANY);
		if (userVOs == null || userVOs.isEmpty()) return null;
		List<Long> userIds = userVOs.stream().map(UserVO::getId).collect(Collectors.toList());
		List<MediaRecommendStatisticsPO> statisticsPOs = mediaRecommendStatisticsDAO.findByUserIdIn(userIds);
		if (statisticsPOs == null || statisticsPOs.isEmpty()) return null; 
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (MediaRecommendStatisticsPO mediaRecommendStatisticsPO : statisticsPOs) {
			Long userId = mediaRecommendStatisticsPO.getUserId();
			map.put(userId, map.containsKey(userId) ? map.get(userId) + mediaRecommendStatisticsPO.getCount() : mediaRecommendStatisticsPO.getCount());
		}
		JSONObject tagAndParent = tagQuery.queryTagAndParent(user, null, null);
		JSONObject returnObject = new JSONObject();
		for (UserVO userVO : userVOs) {
			Long userId = userVO.getId();
			if (!map.containsKey(userId)) continue;
			Long count = map.get(userId);
			List<String> tags = userVO.getTags();
			if (tags == null || tags.isEmpty()) continue;
			for (String tag : tags) {
				if (tagAndParent.containsKey(tag) && tagAndParent.getString(tag).equals("行政区域")) {
					returnObject.put(tag, returnObject.containsKey(tag) ? returnObject.getLong(tag) + count : count);
					break;
				}
			}
		}
		return returnObject;
	}
}
