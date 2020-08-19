package com.sumavision.bvc.command.group.user.decoder;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.SrcType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 上屏方案中的分屏<br/>
 * @Description: <br/>
 * @author zsy
 * @date 2020年5月13日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_DECODER_SCREEN")
public class CommandGroupDecoderScreenPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	private SrcType businessType = SrcType.NONE;

	/** 业务名称，用于显示在播放器顶端，对应播放器信息中的businessInfo */
	private String businessInfo;

	/** 文件、录像的播放地址 */
	private String playUrl;
	
	/** 绑定的上屏设备 */
	private List<CommandGroupUserPlayerCastDevicePO> castDevices;
	
	/** 关联用户信息 */
	private CommandGroupDecoderSchemePO scheme;
	
	/** osd id */
	private Long osdId;
	
	/** osd 名称 */
	private String osdName;
	
	/** 生成字幕可能使用，用户名或设备名 */
	private String srcInfo = "";
	
	/** 生成字幕可能使用，源号码 */
	private String srcCode = "";
	
//	/** 以下用于保存源信息 */
//	/** 文件名 */
//	private String sourceUserName;
//	
//	/** 用户id */
//	private Long sourceUserId;
//	
//	/** bundleId */
//	private String sourceBundleId;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "BUSINESS_TYPE")
	public SrcType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(SrcType businessType) {
		this.businessType = businessType;
	}

	@Column(name = "BUSINESS_INFO")
	public String getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}

	@Column(name = "PLAY_URL")
	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	@OneToMany(mappedBy = "screen", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupUserPlayerCastDevicePO> getCastDevices() {
		return castDevices;
	}

	public void setCastDevices(List<CommandGroupUserPlayerCastDevicePO> castDevices) {
		this.castDevices = castDevices;
	}
		
	@ManyToOne
	@JoinColumn(name = "SCHEME_ID")
	public CommandGroupDecoderSchemePO getScheme() {
		return scheme;
	}

	public void setScheme(CommandGroupDecoderSchemePO scheme) {
		this.scheme = scheme;
	}

	@Column(name = "OSD_ID")
	public Long getOsdId() {
		return osdId;
	}

	public void setOsdId(Long osdId) {
		this.osdId = osdId;
	}

	@Column(name = "OSD_NAME")
	public String getOsdName() {
		return osdName;
	}

	public void setOsdName(String osdName) {
		this.osdName = osdName;
	}

	@Column(name = "SRC_INFO")
	public String getSrcInfo() {
		return srcInfo;
	}

	public void setSrcInfo(String srcInfo) {
		this.srcInfo = srcInfo;
	}

	@Column(name = "SRC_CODE")
	public String getSrcCode() {
		return srcCode;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	
	/**
	 * 清空该分屏的业务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月15日 下午2:42:39
	 * @return
	 */
	public CommandGroupDecoderScreenPO setFree(){
		this.setBusinessType(SrcType.NONE);
		this.setBusinessInfo(null);
		this.setPlayUrl(null);
		this.setOsdId(null);
		this.setOsdName(null);
		this.setSrcCode("");
		this.setSrcInfo("");
		return this;
	}
	
}
