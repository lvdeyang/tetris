package com.sumavision.tetris.sts.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class OsdCfgBO implements Cloneable{

	private Long id;
	
	private String osdType;
	
	private String position;
	
	private String zone;
	
	private String resolution;
	
	private String scaleOrder;
	
	private Integer priority;
	
	private String textContent;
	
	private String trackType;
	
	private Integer fontSize;
	
	private String fontType;
	
	private Long trackSpeed;
	
	private String showOutLine;
	
	private String showBkg;
	
	private String fontColor;
	
	private String bkgColor;
	
	private String outLineColor;
	
	private String picUrl;

	/**
	 * 编码类型，该参数默认了utf-8 ，且返回xml未解析encode-type
	 */
	private String encodeType;
	
	private String alpha;
	
	private String dynamicLogoPriority;
	
	private String osdMode;
	
	private String fuzzyEffect;
	
	private String cycle;

	private String logoRate;
	
	private String startIndex;
	
//	private String fps_p;
	
	public static List<OsdCfgBO> transFromJson(String osdJson){
		try {
			JSONArray jsonArray = JSONArray.parseArray(osdJson);
			List<OsdCfgBO> osdList = new ArrayList<OsdCfgBO>();
			for(int index = 0; index < jsonArray.size(); index++){
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				OsdCfgBO osdCfgBO = JSONObject.toJavaObject(jsonObject, OsdCfgBO.class);
				osdList.add(osdCfgBO);
			}
			return osdList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ArrayList<OsdCfgBO>();
		}
	}
	
//	public OsdCfgBO clone(){
//		return new OsdCfgBO();
//	}

	@Override
	public OsdCfgBO clone() throws CloneNotSupportedException {  
        return (OsdCfgBO)super.clone();  
    } 

	@XmlAttribute(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlAttribute(name = "type")
	public String getOsdType() {
		return osdType;
	}

	public void setOsdType(String osdType) {
		this.osdType = osdType;
	}

	@XmlAttribute(name = "position")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@XmlAttribute(name = "zone")
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@XmlAttribute(name = "resolution")
	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	@XmlAttribute(name = "order")
	public String getScaleOrder() {
		return scaleOrder;
	}

	public void setScaleOrder(String scaleOrder) {
		this.scaleOrder = scaleOrder;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@XmlAttribute(name = "text-content")
	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	@XmlAttribute(name = "track-type")
	public String getTrackType() {
		return trackType;
	}

	public void setTrackType(String trackType) {
		this.trackType = trackType;
	}

	@XmlAttribute(name = "font-size")
	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	@XmlAttribute(name = "font-type")
	public String getFontType() {
		return fontType;
	}

	public void setFontType(String fontType) {
		this.fontType = fontType;
	}

	@XmlAttribute(name = "track-speed")
	public Long getTrackSpeed() {
		return trackSpeed;
	}

	public void setTrackSpeed(Long trackSpeed) {
		this.trackSpeed = trackSpeed;
	}

	@XmlAttribute(name = "show-bkg")
	public String getShowBkg() {
		return showBkg;
	}

	public void setShowBkg(String showBkg) {
		this.showBkg = showBkg;
	}

	@XmlAttribute(name = "font-color")
	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	@XmlAttribute(name = "bkg-color")
	public String getBkgColor() {
		return bkgColor;
	}

	public void setBkgColor(String bkgColor) {
		this.bkgColor = bkgColor;
	}

	@XmlAttribute(name = "url")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@XmlAttribute(name = "encode-type")
	public String getEncodeType() {
		return encodeType;
	}

	public void setEncodeType(String encodeType) {
		this.encodeType = encodeType;
	}

	@XmlAttribute(name = "alpha")
	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getDynamicLogoPriority() {
		return dynamicLogoPriority;
	}

	public void setDynamicLogoPriority(String dynamicLogoPriority) {
		this.dynamicLogoPriority = dynamicLogoPriority;
	}

	@XmlAttribute(name = "osd-mode")
	public String getOsdMode() {
		return osdMode;
	}

	public void setOsdMode(String osdMode) {
		this.osdMode = osdMode;
	}

	@XmlAttribute(name = "fuzzy-effect")
	public String getFuzzyEffect() {
		return fuzzyEffect;
	}

	public void setFuzzyEffect(String fuzzyEffect) {
		this.fuzzyEffect = fuzzyEffect;
	}

	@XmlAttribute(name = "show-outline")
	public String getShowOutLine() {
		return showOutLine;
	}

	public void setShowOutLine(String showOutLine) {
		this.showOutLine = showOutLine;
	}

	@XmlAttribute(name = "outline-color")
	public String getOutLineColor() {
		return outLineColor;
	}

	public void setOutLineColor(String outLineColor) {
		this.outLineColor = outLineColor;
	}

	@XmlAttribute(name = "cycle")
	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	@XmlAttribute(name = "logo-rate")
	public String getLogoRate() {
		return logoRate;
	}

	public void setLogoRate(String logoRate) {
		this.logoRate = logoRate;
	}

	@XmlAttribute(name = "start-index")
	public String getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	/*public String getFps_p() {
		return fps_p;
	}

	public void setFps_p(String fps_p) {
		this.fps_p = fps_p;
	}
	*/

	public Boolean isSameWithCfg(OsdCfgBO osdCfgBO){
		if(osdCfgBO == null){
			return false;
		}
		
		//公共参数
		if (this.osdType == null) {
			if (osdCfgBO.osdType != null)
				return false;
		} else if (!this.osdType.equals(osdCfgBO.osdType))
			return false;
		if (this.position == null) {
			if (osdCfgBO.position != null)
				return false;
		} else if (!this.position.equals(osdCfgBO.position))
			return false;
		if (this.zone == null) {
			if (osdCfgBO.zone != null)
				return false;
		} else if (!this.zone.equals(osdCfgBO.zone))
			return false;
		if (this.resolution == null) {
			if (osdCfgBO.resolution != null)
				return false;
		} else if (!this.resolution.equals(osdCfgBO.resolution.replace("x", ",")))
			return false;
		if (this.scaleOrder == null) {
			if (osdCfgBO.scaleOrder != null)
				return false;
		} else if (!this.scaleOrder.equals(osdCfgBO.scaleOrder))
			return false;
		
		//字幕参数
		if (this.textContent == null) {
			if (osdCfgBO.textContent != null)
				return false;
		} else {
			try {
				if(osdCfgBO.textContent == null)
					return false;
					//转码能力问题返回ftp中文乱码，
					// 万一此处解析错误报不同步了都不知道为啥不同步，从而导致假不同步，所以打个日志
				else if(!new String(this.textContent.getBytes("utf-8"),"gbk").equals(osdCfgBO.textContent)) {
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} 
		if (this.trackType == null) {
			if (osdCfgBO.trackType != null)
				return false;
		} else if (!this.trackType.equals(osdCfgBO.trackType))
			return false;
		if (this.trackSpeed == null) {
			if (osdCfgBO.trackSpeed != null)
				return false;
		} else if (!this.trackSpeed.equals(osdCfgBO.trackSpeed))
			return false;
		if (this.fontType == null) {
			if (osdCfgBO.fontType != null)
				return false;
		} else if (!this.fontType.equals(osdCfgBO.fontType))
			return false;
		if (this.fontSize == null) {
			if (osdCfgBO.fontSize != null)
				return false;
		} else if (!this.fontSize.equals(osdCfgBO.fontSize))
			return false;
		if (this.fontColor == null) {
			if (osdCfgBO.fontColor != null)
				return false;
		} else if (!this.fontColor.equals(osdCfgBO.fontColor))
			return false;
		if (this.showBkg == null) {
			if (osdCfgBO.showBkg != null)
				return false;
		} else if (!this.showBkg.equals(osdCfgBO.showBkg))
			return false;
		if (this.showOutLine == null) {
			if (osdCfgBO.showOutLine != null)
				return false;
		} else if (!this.showOutLine.equals(osdCfgBO.showOutLine))
			return false;
		if (this.bkgColor == null) {
			if (osdCfgBO.bkgColor != null)
				return false;
		} else if (!this.bkgColor.equals(osdCfgBO.bkgColor))
			return false;
		if (this.outLineColor == null) {
			if (osdCfgBO.outLineColor != null)
				return false;
		} else if (!this.outLineColor.equals(osdCfgBO.outLineColor))
			return false;
		
		//台标参数
		if (this.picUrl == null) {
			if (osdCfgBO.picUrl != null)
				return false;
		} else {
			try {
				if(osdCfgBO.picUrl == null)
					return false;
					//集群发送了encode-type:utf8，但是转码能力问题返回ftp中文乱码，
					// 万一此处解析错误报不同步了都不知道为啥不同步，从而导致假不同步，所以打个日志
				else if(!new String(this.picUrl.getBytes("utf-8"),"gbk").equals("ftp://"+ osdCfgBO.picUrl)) {
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (this.encodeType == null) {
			if (osdCfgBO.encodeType != null)
				return false;
		} else if (!this.encodeType.equals(osdCfgBO.encodeType))
			return false;
		if (this.alpha == null) {
			if (osdCfgBO.alpha != null)
				return false;
		} else if (!this.alpha.equals(osdCfgBO.alpha))
			return false;
		if (this.osdMode == null) {
			if (osdCfgBO.osdMode != null)
				return false;
		} else if (!this.osdMode.equals(osdCfgBO.osdMode))
			return false;
		if (this.cycle == null) {
			if (osdCfgBO.cycle != null)
				return false;
		} else if (!this.cycle.equals(osdCfgBO.cycle))
			return false;
		if (this.logoRate == null) {
			if (osdCfgBO.logoRate != null)
				return false;
		} else if (!this.logoRate.equals(osdCfgBO.logoRate))
			return false;
		if (this.startIndex == null) {
			if (osdCfgBO.startIndex != null)
				return false;
		} else if (!this.startIndex.equals(osdCfgBO.startIndex))
			return false;
		
		//遮盖参数
		if (this.fuzzyEffect == null) {
			if (osdCfgBO.fuzzyEffect != null)
				return false;
		} else if (!this.fuzzyEffect.equals(osdCfgBO.fuzzyEffect))
			return false;
		
		return true;
	}
	
	
}