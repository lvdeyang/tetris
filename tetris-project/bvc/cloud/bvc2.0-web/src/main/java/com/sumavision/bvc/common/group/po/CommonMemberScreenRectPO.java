package com.sumavision.bvc.common.group.po;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: DeviceGroupMemberScreenRectPO 
 * @author wjw
 * @date 2018年11月8日 下午2:35:07
 */
@Entity
@Table(name = "BVC_COMMON_MEMBER_SCREEN_RECT")
public class CommonMemberScreenRectPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 设备id */
	private String bundleId;
	
	/** 屏幕id */
	private String screenId;
	
	/** 区域id */
	private String rectId;
	
	/** 以json字符串的格式存 x、y、width、height、cut等参数 */
	private String param;
	
	/** 区域绑定的通道，以","分隔 */
	private String channel;
	
	/** 关联设备组成员屏幕 */
	private CommonMemberScreenPO screen;

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "SCREEN_ID")
	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@Column(name = "RECT_ID")
	public String getRectId() {
		return rectId;
	}

	public void setRectId(String rectId) {
		this.rectId = rectId;
	}

	@Column(name = "PARAM", length = 1024)
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Column(name = "CHANNEL")
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@ManyToOne
	@JoinColumn(name = "DEVICE_GROUP_MEMBER_SCREEN_ID")
	public CommonMemberScreenPO getScreen() {
		return screen;
	}

	public void setScreen(CommonMemberScreenPO screen) {
		this.screen = screen;
	}	
	
	/**
	 * @ClassName: 区域排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author wjw
	 * @date 2018年11月13日 上午8:36:10 
	 */
	public static final class RectComparatorFromPO implements Comparator<CommonMemberScreenRectPO>{
		@Override
		public int compare(CommonMemberScreenRectPO o1, CommonMemberScreenRectPO o2) {
			
			long id1 = Long.parseLong(o1.getRectId().split("_")[1]);
			long id2 = Long.parseLong(o2.getRectId().split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}

}
