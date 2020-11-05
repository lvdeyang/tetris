package com.sumavision.tetris.bvc.business.dispatch.po;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.dispatch.bo.ChannelBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.SourceParamBO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: ChannelBO 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zsy
 * @date 2020年3月11日 上午11:14:03 
 */
@Entity
@Table(name="TETRIS_DISPATCH_CHANNEL")
public class TetrisDispatchChannelPO extends AbstractBasePO {
	
	private static final long serialVersionUID = 1L;

	/** 设备能力通道ID。只在设备调度中有效，用户调度中无效 */
	private String channelId;
	
	/** 源类型，为null时表示没有源
	明确知道转发源的channel时，type为channel；做合屏混音等不知道channel的时候为combineVideo/combineAudio */
	private String sourceType;
	
	private String sourceLayerId;
	
	private String sourceBundleId;
	
	private String sourceChannelId;
	
	private String sourceUuid;

//	private SourceParamBO source_param;
	
	private String codecParamType = "AUTO";

	/** 编解码参数 */
//	private CodecParamBO codec_param = new CodecParamBO();
	private DispatchVideoParamPO videoParam = new DispatchVideoParamPO();
	
	private DispatchAudioParamPO audioParam = new DispatchAudioParamPO();
	
	/** 关联调度 */
	private TetrisDispatchPO dispatch;

	public String getChannelId() {
		return channelId;
	}

	public TetrisDispatchChannelPO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

//	public CodecParamBO getCodec_param() {
//		return codec_param;
//	}
//
//	public TetrisDispatchChannelPO setCodec_param(CodecParamBO codec_param) {
//		this.codec_param = codec_param;
//		return this;
//	}
	
//	public SourceParamBO getSource_param() {
//		return source_param;
//	}
//
//	public DispatchChannelPO setSource_param(SourceParamBO source_param) {
//		this.source_param = source_param;
//		return this;
//	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public void setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
	}

	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public void setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
	}

	public String getSourceUuid() {
		return sourceUuid;
	}

	public void setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
	}

	public String getCodecParamType() {
		return codecParamType;
	}

	public TetrisDispatchChannelPO setCodecParamType(String codecParamType) {
		this.codecParamType = codecParamType;
		return this;
	}
	
	@OneToOne(mappedBy = "channel", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public DispatchVideoParamPO getVideoParam() {
		return videoParam;
	}

	public TetrisDispatchChannelPO setVideoParam(DispatchVideoParamPO videoParam) {
		this.videoParam = videoParam;
		return this;
	}

	@OneToOne(mappedBy = "channel", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public DispatchAudioParamPO getAudioParam() {
		return audioParam;
	}

	public TetrisDispatchChannelPO setAudioParam(DispatchAudioParamPO audioParam) {
		this.audioParam = audioParam;
		return this;
	}

	@ManyToOne
	@JoinColumn(name = "DISPATCH_ID")
	public TetrisDispatchPO getDispatch() {
		return dispatch;
	}

	public TetrisDispatchChannelPO setDispatch(TetrisDispatchPO dispatch) {
		this.dispatch = dispatch;
		return this;
	}
	
	/**
	 * 清除通道的源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午10:07:35
	 * @return
	 */
	public TetrisDispatchChannelPO clearSource(){
		this.sourceType = null;
		this.sourceLayerId = null;
		this.sourceBundleId = null;
		this.sourceChannelId = null;
		this.sourceUuid = null;
		return this;
	}
	
	/**
	 * 设置通道的源，暂不支持合屏混音<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 下午1:09:01
	 * @return
	 */
	public TetrisDispatchChannelPO setSource(ChannelBO channelBO){
		if(channelBO==null || channelBO.getSource_param()==null){
			return this;
		}
		this.sourceType = channelBO.getSource_param().getType();
		this.sourceLayerId = channelBO.getSource_param().getLayerId();
		this.sourceBundleId = channelBO.getSource_param().getBundleId();
		this.sourceChannelId = channelBO.getSource_param().getChannelId();
		this.sourceUuid = channelBO.getSource_param().getUuid();
		return this;
	}
	
	public TetrisDispatchChannelPO set(ChannelBO channelBO){
		if(channelBO==null){
			return this;
		}
		this.setChannelId(channelBO.getChannelId())
			.setSource(channelBO)
			.setCodecParamType(channelBO.getCodecParamType())
			.setAudioParam(this.audioParam.set(channelBO.getCodec_param().getAudio_param()).setChannel(this))
			.setVideoParam(this.videoParam.set(channelBO.getCodec_param().getVideo_param()).setChannel(this));
		return this;
	}
	
	/**
	 * 对比PO与BO的源是否相同，暂不支持合屏混音<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 下午1:35:01
	 * @param channelBO
	 * @return
	 */
	public boolean sourceEquals(ChannelBO channelBO){
		if(channelBO == null){
			return false;
		}		
		if(channelBO.getSource_param() == null){
			if(this.sourceType==null && this.sourceLayerId==null && this.sourceBundleId==null && this.sourceChannelId==null && this.sourceUuid==null){
				return true;
			}else{
				return false;
			}
		}
		
		SourceParamBO sourceParamBO = channelBO.getSource_param();
		if(this.sourceType!=null && this.sourceType.equals(sourceParamBO.getType())
				&& this.sourceLayerId!=null && this.sourceLayerId.equals(sourceParamBO.getLayerId())
				&& this.sourceBundleId!=null && this.sourceBundleId.equals(sourceParamBO.getBundleId())
				&& this.sourceChannelId!=null && this.sourceChannelId.equals(sourceParamBO.getChannelId())){
//				&& this.sourceUuid!=null && this.sourceUuid.equals(sourceParamBO.getUuid())){
			return true;
		}
		return false;
	}
	
//
//	/**
//	 * @Title: 生成通道占用打开协议 
//	 * @param channel 业务层通道数据
//	 * @param codec 参数模板
//	 * @return ConnectBO
//	 */
//	public ChannelBO set(DeviceGroupMemberChannelPO channel, CodecParamBO codec){
//		this.setLock_type("write")
//		    .setLayerId(channel.getMember().getLayerId())
//		    .setBase_type(channel.getChannelType())
//		    .setBundleId(channel.getBundleId())
//		    .setChannelId(channel.getChannelId())
//		    .setChannel_status("Open")
//		    .setCodec_param(codec);
//		return this;
//	}
	
}
