package com.sumavision.tetris.sts.task.source;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

public class SubtitleElement implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8653677952717785660L;

	private Integer pid;

    private String type;
    
    private String language;
    
    private String decode_mode;
    
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

	public String getDecode_mode() {
		return decode_mode;
	}

	public void setDecode_mode(String decode_mode) {
		this.decode_mode = decode_mode;
	}
    
    
    
}
