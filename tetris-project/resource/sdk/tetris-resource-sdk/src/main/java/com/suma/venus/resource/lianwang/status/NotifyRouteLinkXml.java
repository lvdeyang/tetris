package com.suma.venus.resource.lianwang.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.suma.venus.resource.pojo.BundlePO;

@XmlRootElement(name="notify")
public class NotifyRouteLinkXml {
	
	private String commandname = "syncroutelink";
	
	private String seq = BundlePO.createBundleId();
	
	private Long ts = new Date().getTime();
	
	private Integer matsize;
	
	private String mattype;
	
	private String matcontent;
	
	private List<String> nlist = new ArrayList<String>();
	
	public NotifyRouteLinkXml() {
		super();
	}
	
	public NotifyRouteLinkXml(Integer matsize, String mattype, String matcontent, List<String> nlist) {
		super();
		this.matsize = matsize;
		this.mattype = mattype;
		this.matcontent = matcontent;
		this.nlist = nlist;
	}



	@XmlElement(name="commandname")
	public String getCommandname() {
		return commandname;
	}

	public void setCommandname(String commandname) {
		this.commandname = commandname;
	}

	@XmlElement(name="seq")
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	@XmlElement(name="ts")
	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	@XmlElement(name="matsize")
	public Integer getMatsize() {
		return matsize;
	}

	public void setMatsize(Integer matsize) {
		this.matsize = matsize;
	}

	@XmlElement(name="mattype")
	public String getMattype() {
		return mattype;
	}

	public void setMattype(String mattype) {
		this.mattype = mattype;
	}

	@XmlElement(name="matcontent")
	public String getMatcontent() {
		return matcontent;
	}

	public void setMatcontent(String matcontent) {
		this.matcontent = matcontent;
	}

	@XmlElementWrapper(name = "nlist") 
    @XmlElement(name = "nid")
	public List<String> getNlist() {
		return nlist;
	}

	public void setNlist(List<String> nlist) {
		this.nlist = nlist;
	}
	
}
