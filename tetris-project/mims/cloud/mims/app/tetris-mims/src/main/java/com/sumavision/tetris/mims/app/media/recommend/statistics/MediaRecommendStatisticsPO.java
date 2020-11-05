package com.sumavision.tetris.mims.app.media.recommend.statistics;

import java.text.ParseException;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_RECOMMEND_STATISTICS")
public class MediaRecommendStatisticsPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 统计日期 */
	private String date;
	
	/** 用户id */
	private Long userId;
	
	/** 组织id */
	private String groupId;
	
	/** 推荐总数 */
	private Long count;

	@Column(name = "DATE")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	@Column(name = "USER_ID" )
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "COUNT")
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
	/**
	 * 排序(根据时间从小到大)
	 * @author lzp
	 *
	 */
	public static final class MediaRecommendStatisticsDateOrderComparator implements Comparator<MediaRecommendStatisticsPO>{
		@Override
		public int compare(MediaRecommendStatisticsPO o1, MediaRecommendStatisticsPO o2) {
			try {
				if(DateUtil.parse(o1.getDate(), DateUtil.defaultDatePattern).getTime() < DateUtil.parse(o2.getDate(), DateUtil.defaultDatePattern).getTime()){
					return -1;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(o1.getDate().equals(o2.getDate())){
				return 0;
			}
			return 1;
		}
	}
}
