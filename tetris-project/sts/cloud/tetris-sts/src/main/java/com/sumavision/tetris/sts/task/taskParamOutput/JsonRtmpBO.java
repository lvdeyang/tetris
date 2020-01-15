package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonRtmpBO implements OutputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3952181928920901521L;

	private String server_url;
	private String pub_user;
	private String pub_password;
	private Boolean vid_exist;
	private Boolean aud_exist;
	private ArrayList<OutputMediaBO> media_array;
	public String getServer_url() {
		return server_url;
	}
	public void setServer_url(String server_url) {
		this.server_url = server_url;
	}
	public String getPub_user() {
		return pub_user;
	}
	public void setPub_user(String pub_user) {
		this.pub_user = pub_user;
	}
	public String getPub_password() {
		return pub_password;
	}
	public void setPub_password(String pub_password) {
		this.pub_password = pub_password;
	}
	public Boolean getVid_exist() {
		return vid_exist;
	}
	public void setVid_exist(Boolean vid_exist) {
		this.vid_exist = vid_exist;
	}
	public Boolean getAud_exist() {
		return aud_exist;
	}
	public void setAud_exist(Boolean aud_exist) {
		this.aud_exist = aud_exist;
	}
	public ArrayList<OutputMediaBO> getMedia_array() {
		return media_array;
	}
	public void setMedia_array(ArrayList<OutputMediaBO> media_array) {
		this.media_array = media_array;
	}
	
}
