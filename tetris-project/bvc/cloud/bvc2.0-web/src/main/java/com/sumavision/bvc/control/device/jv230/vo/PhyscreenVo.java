package com.sumavision.bvc.control.device.jv230.vo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class PhyscreenVo extends AbstractBaseVO<PhyscreenVo, Jv230PO>{

	private Long id;

	//uuid，站点间设备唯一识别码
	private String uuid;
	
	//能力所属JV230设备的uuid
	private String jv230Uuid;

	//物理屏名称	
	private String name;
	
	//物理屏ip	
	private String ip;
	
	//物理屏端口号
	private String port;
	
	//横向分辨率	
	private Long width;
	
	//纵向分辨率	
	private Long height;
	
	//物理屏能力号id
	private Long abilityId;
	
	//物理屏对应设备
	private Long deviceId;
	
	//物理屏在大屏中序号
	private Long serialNum;
	
	//物理屏解码数
	private List<DecodeVo> decodeList = new ArrayList<DecodeVo>();

	public String getJv230Uuid() {
		return jv230Uuid;
	}

	public PhyscreenVo setJv230Uuid(String jv230Uuid) {
		this.jv230Uuid = jv230Uuid;
		return this;
	}

	public List<DecodeVo> getDecodeList() {
		return decodeList;
	}

	public PhyscreenVo setDecodeList(List<DecodeVo> decodeList) {
		this.decodeList = decodeList;
		return this;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public PhyscreenVo setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public Long getId() {
		return id;
	}

	public PhyscreenVo setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public PhyscreenVo setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public PhyscreenVo setName(String name) {
		this.name = name;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public PhyscreenVo setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getPort() {
		return port;
	}

	public PhyscreenVo setPort(String port) {
		this.port = port;
		return this;
	}

	public Long getWidth() {
		return width;
	}

	public PhyscreenVo setWidth(Long width) {
		this.width = width;
		return this;
	}

	public Long getHeight() {
		return height;
	}

	public PhyscreenVo setHeight(Long height) {
		this.height = height;
		return this;
	}

	public Long getAbilityId() {
		return abilityId;
	}

	public PhyscreenVo setAbilityId(Long abilityId) {
		this.abilityId = abilityId;
		return this;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public PhyscreenVo setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	@Override
	public PhyscreenVo set(Jv230PO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setJv230Uuid(entity.getBundleId())
			.setName(entity.getBundleName())
			//TODO:自设的长和宽
			.setWidth(1920l)
			.setHeight(1080l)
			.setSerialNum(Long.valueOf(entity.getSerialnum()));
		return this;
	}
}
