package com.sumavision.bvc.common.group.po;

import java.util.Comparator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/** 
 * @ClassName: 设备组成员屏幕
 * @author wjw
 * @date 2018年11月8日 下午2:22:26
 */
@Entity
@Table(name="BVC_COMMON_MEMBER_SCREEN")
public class CommonMemberScreenPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 屏幕名 */
	private String name;
	
	/** 来自于资源层屏幕id */
	private String screenId;
	
	/** 来自于资源层设备id */
	private String bundleId;
	
	/** 关联屏幕区域 */
	private Set<CommonMemberScreenRectPO> rests;
	
	/** 关联设备组成员 */
	private CommonMemberPO member;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SCREEN_ID")
	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@OneToMany(mappedBy = "screen", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonMemberScreenRectPO> getRests() {
		return rests;
	}

	public void setRests(Set<CommonMemberScreenRectPO> rests) {
		this.rests = rests;
	}

	@ManyToOne
	@JoinColumn(name = "DEVICE_GROUP_MEMBER_ID")
	public CommonMemberPO getMember() {
		return member;
	}

	public void setMember(CommonMemberPO member) {
		this.member = member;
	}
	
	/**
	 * @ClassName: 屏幕排序器，id为 screen-编号， 按照编号从小到大排列<br/> 
	 * @author wjw
	 * @date 2018年12月24日 上午8:36:10 
	 */
	public static final class ScreenComparatorFromPO implements Comparator<CommonMemberScreenPO>{
		@Override
		public int compare(CommonMemberScreenPO o1, CommonMemberScreenPO o2) {
			
			long id1 = Long.parseLong(o1.getScreenId().split("_")[1]);
			long id2 = Long.parseLong(o2.getScreenId().split("_")[1]);
			
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
