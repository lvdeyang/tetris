package com.sumavision.tetris.bvc.model.terminal.editor;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalEncodeVideoChannelWithAudioChannelPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 终端拓补图节点<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月11日 上午9:37:57
 */
public class TerminalGraphNodeVO {

	/** id格式：type_entity.id */
	private String id;
	
	/** 节点名称 */
	private String name;
	
	/** 节点类型 */
	private String type;
	
	/** svg图标path */
	private String icon;
	
	/** svg图标大小 */
	private int size;
	
	/** 附加参数 */
	private JSONObject params;
	
	/** 子节点 */
	private List<TerminalGraphNodeVO> children;
	
	public String getId() {
		return id;
	}

	public TerminalGraphNodeVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public TerminalGraphNodeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalGraphNodeVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public TerminalGraphNodeVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public int getSize() {
		return size;
	}

	public TerminalGraphNodeVO setSize(int size) {
		this.size = size;
		return this;
	}

	public List<TerminalGraphNodeVO> getChildren() {
		return children;
	}

	public TerminalGraphNodeVO setChildren(List<TerminalGraphNodeVO> children) {
		this.children = children;
		return this;
	}
	
	public JSONObject getParams() {
		return params;
	}

	public TerminalGraphNodeVO setParams(JSONObject params) {
		this.params = params;
		return this;
	}

	public TerminalGraphNodeVO set(TerminalPO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.TERMINAL).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(TerminalGraphNodeType.TERMINAL.toString())
			.setIcon(TerminalGraphNodeType.TERMINAL.getPath())
			.setSize(TerminalGraphNodeType.TERMINAL.getSize())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalPhysicalScreenPO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.PHYSICAL_SCREEN).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(TerminalGraphNodeType.PHYSICAL_SCREEN.toString())
			.setIcon(TerminalGraphNodeType.PHYSICAL_SCREEN.getPath())
			.setSize(TerminalGraphNodeType.PHYSICAL_SCREEN.getSize())
			.setParams(new JSONObject())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		this.getParams().put("terminalAudioOutputId", entity.getTerminalAudioOutputId());
		this.getParams().put("x", entity.getX());
		this.getParams().put("y", entity.getY());
		this.getParams().put("width", entity.getWidth());
		this.getParams().put("height", entity.getHeight());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.VIDEO_DECODE_CHANNEL).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(TerminalGraphNodeType.VIDEO_DECODE_CHANNEL.toString())
			.setIcon(TerminalGraphNodeType.VIDEO_DECODE_CHANNEL.getPath())
			.setSize(TerminalGraphNodeType.VIDEO_DECODE_CHANNEL.getSize())
			.setParams(new JSONObject())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.CHANNEL_PERMISSION).append("_").append(entity.getParamsPermissionId()).toString())
			.setName(new StringBufferWrapper().append(entity.getChannelParamsType().getName()).append("-").append(entity.getBundleName()).append("-").append(entity.getDeviceMode()).append("-").append(entity.getChannelId()).toString())
			.setType(TerminalGraphNodeType.CHANNEL_PERMISSION.toString())
			.setIcon(TerminalGraphNodeType.CHANNEL_PERMISSION.getPath())
			.setSize(TerminalGraphNodeType.CHANNEL_PERMISSION.getSize())
			.setParams(new JSONObject());
		this.getParams().put("terminalBundleChannelId", entity.getId());
		this.getParams().put("channelId", entity.getChannelId());
		this.getParams().put("type", entity.getType().toString());
		this.getParams().put("typeName", entity.getType().getName());
		this.getParams().put("terminalBundleId", entity.getTerminalBundleId());
		this.getParams().put("bundleName", entity.getBundleName());
		this.getParams().put("deviceMode", entity.getDeviceMode());
		this.getParams().put("bundleType", entity.getBundleType().toString());
		this.getParams().put("bundleTypeName", entity.getBundleType().getName());
		this.getParams().put("terminalId", entity.getTerminalId());
		this.getParams().put("terminalChannelId", entity.getTerminalChannelId());
		this.getParams().put("channelParamsType", entity.getChannelParamsType().toString());
		this.getParams().put("channelParamsTypeName", entity.getChannelParamsType().getName());
		this.getParams().put("paramsPermissionId", entity.getParamsPermissionId());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalBundleChannelPO channelEntity, TerminalBundlePO bundleEntity, TerminalChannelBundleChannelPermissionPO permission){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.CHANNEL_PERMISSION).append("_").append(permission.getId()).toString())
			.setName(new StringBufferWrapper().append(permission.getChannelParamsType().getName()).append("-").append(bundleEntity.getName()).append("-").append(bundleEntity.getBundleType()).append("-").append(channelEntity.getChannelId()).toString())
			.setType(TerminalGraphNodeType.CHANNEL_PERMISSION.toString())
			.setIcon(TerminalGraphNodeType.CHANNEL_PERMISSION.getPath())
			.setSize(TerminalGraphNodeType.CHANNEL_PERMISSION.getSize())
			.setParams(new JSONObject());
		this.getParams().put("terminalBundleChannelId", channelEntity.getId());
		this.getParams().put("channelId", channelEntity.getChannelId());
		this.getParams().put("type", channelEntity.getType().toString());
		this.getParams().put("typeName", channelEntity.getType().getName());
		this.getParams().put("terminalBundleId", channelEntity.getTerminalBundleId());
		this.getParams().put("bundleName", bundleEntity.getName());
		this.getParams().put("deviceMode", bundleEntity.getBundleType());
		this.getParams().put("bundleType", bundleEntity.getType().toString());
		this.getParams().put("bundleTypeName", bundleEntity.getType().getName());
		this.getParams().put("terminalId", bundleEntity.getTerminalId());
		this.getParams().put("terminalChannelId", permission.getTerminalChannelId());
		this.getParams().put("channelParamsType", permission.getChannelParamsType().toString());
		this.getParams().put("channelParamsTypeName", permission.getChannelParamsType().getName());
		this.getParams().put("paramsPermissionId", permission.getId());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalAudioOutputPO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.AUDIO_OUTPUT).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(TerminalGraphNodeType.AUDIO_OUTPUT.toString())
			.setIcon(TerminalGraphNodeType.AUDIO_OUTPUT.getPath())
			.setSize(TerminalGraphNodeType.AUDIO_OUTPUT.getSize())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.AUDIO_DECODE_CHANNEL).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(TerminalGraphNodeType.AUDIO_DECODE_CHANNEL.toString())
			.setIcon(TerminalGraphNodeType.AUDIO_DECODE_CHANNEL.getPath())
			.setSize(TerminalGraphNodeType.AUDIO_DECODE_CHANNEL.getSize())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalChannelPO entity){
		TerminalGraphNodeType channelType = null;
		if(TerminalChannelType.AUDIO_DECODE.equals(entity.getType())){
			channelType = TerminalGraphNodeType.AUDIO_DECODE_CHANNEL;
		}else if(TerminalChannelType.AUDIO_ENCODE.equals(entity.getType())){
			channelType = TerminalGraphNodeType.AUDIO_ENCODE_CHANNEL;
		}else if(TerminalChannelType.VIDEO_DECODE.equals(entity.getType())){
			channelType = TerminalGraphNodeType.VIDEO_DECODE_CHANNEL;
		}else if(TerminalChannelType.VIDEO_ENCODE.equals(entity.getType())){
			channelType = TerminalGraphNodeType.VIDEO_ENCODE_CHANNEL;
		}
		this.setId(new StringBufferWrapper().append(channelType).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(channelType.toString())
			.setIcon(channelType.getPath())
			.setSize(channelType.getSize())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		return this;
	}
	
	public TerminalGraphNodeVO set(TerminalEncodeVideoChannelWithAudioChannelPermissionDTO entity){
		this.setId(new StringBufferWrapper().append(TerminalGraphNodeType.VIDEO_ENCODE_CHANNEL).append("_").append(entity.getId()).toString())
			.setName(entity.getName())
			.setType(TerminalGraphNodeType.VIDEO_ENCODE_CHANNEL.toString())
			.setIcon(TerminalGraphNodeType.VIDEO_ENCODE_CHANNEL.getPath())
			.setSize(TerminalGraphNodeType.VIDEO_ENCODE_CHANNEL.getSize())
			.setChildren(new ArrayList<TerminalGraphNodeVO>());
		return this;
	}
	
}
