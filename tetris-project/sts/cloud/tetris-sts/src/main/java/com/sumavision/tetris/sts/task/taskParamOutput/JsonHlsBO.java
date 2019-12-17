package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonHlsBO extends JsonTsCommonBO implements OutputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8789100641686161026L;

	private Integer group_count;
	private Integer group_file_count;
	private Integer chunk_span;
	private String mode;
	private String enc_mode;
	private String IV_mode;
	private String drm_provider;
	private String IV_set;
	private Integer key_rotation_count;
	private String veri_resource_id;
	private String key_server_addr;
	private String server_vod_name;
	private Boolean subdir_flag;
	private String packet_type;
	private String primary_m3u8;
	private ArrayList<JsonHlsVideoBO> video_array;
	private ArrayList<JsonHlsAudioBO> audio_array;
	private ArrayList<JsonHlsSubtitleBO> subtitle_array;
	private ArrayList<OutputMediaGroupBO> media_group;
	private ArrayList<OutputMediaBO> media_array;
	private StorageBO storage;
	public Integer getGroup_count() {
		return group_count;
	}
	public void setGroup_count(Integer group_count) {
		this.group_count = group_count;
	}
	public Integer getGroup_file_count() {
		return group_file_count;
	}
	public void setGroup_file_count(Integer group_file_count) {
		this.group_file_count = group_file_count;
	}
	public Integer getChunk_span() {
		return chunk_span;
	}
	public void setChunk_span(Integer chunk_span) {
		this.chunk_span = chunk_span;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getEnc_mode() {
		return enc_mode;
	}
	public void setEnc_mode(String enc_mode) {
		this.enc_mode = enc_mode;
	}
	public String getIV_mode() {
		return IV_mode;
	}
	public void setIV_mode(String iV_mode) {
		IV_mode = iV_mode;
	}
	public String getDrm_provider() {
		return drm_provider;
	}
	public void setDrm_provider(String drm_provider) {
		this.drm_provider = drm_provider;
	}
	public String getIV_set() {
		return IV_set;
	}
	public void setIV_set(String iV_set) {
		IV_set = iV_set;
	}
	public Integer getKey_rotation_count() {
		return key_rotation_count;
	}
	public void setKey_rotation_count(Integer key_rotation_count) {
		this.key_rotation_count = key_rotation_count;
	}
	public String getVeri_resource_id() {
		return veri_resource_id;
	}
	public void setVeri_resource_id(String veri_resource_id) {
		this.veri_resource_id = veri_resource_id;
	}
	public String getKey_server_addr() {
		return key_server_addr;
	}
	public void setKey_server_addr(String key_server_addr) {
		this.key_server_addr = key_server_addr;
	}
	public String getServer_vod_name() {
		return server_vod_name;
	}
	public void setServer_vod_name(String server_vod_name) {
		this.server_vod_name = server_vod_name;
	}
	public Boolean getSubdir_flag() {
		return subdir_flag;
	}
	public void setSubdir_flag(Boolean subdir_flag) {
		this.subdir_flag = subdir_flag;
	}
	public String getPacket_type() {
		return packet_type;
	}
	public void setPacket_type(String packet_type) {
		this.packet_type = packet_type;
	}
	public String getPrimary_m3u8() {
		return primary_m3u8;
	}
	public void setPrimary_m3u8(String primary_m3u8) {
		this.primary_m3u8 = primary_m3u8;
	}
	public ArrayList<JsonHlsVideoBO> getVideo_array() {
		return video_array;
	}
	public void setVideo_array(ArrayList<JsonHlsVideoBO> video_array) {
		this.video_array = video_array;
	}
	public ArrayList<JsonHlsAudioBO> getAudio_array() {
		return audio_array;
	}
	public void setAudio_array(ArrayList<JsonHlsAudioBO> audio_array) {
		this.audio_array = audio_array;
	}
	public ArrayList<JsonHlsSubtitleBO> getSubtitle_array() {
		return subtitle_array;
	}
	public void setSubtitle_array(ArrayList<JsonHlsSubtitleBO> subtitle_array) {
		this.subtitle_array = subtitle_array;
	}
	public ArrayList<OutputMediaGroupBO> getMedia_group() {
		return media_group;
	}
	public void setMedia_group(ArrayList<OutputMediaGroupBO> media_group) {
		this.media_group = media_group;
	}
	public ArrayList<OutputMediaBO> getMedia_array() {
		return media_array;
	}
	public void setMedia_array(ArrayList<OutputMediaBO> media_array) {
		this.media_array = media_array;
	}
	public StorageBO getStorage() {
		return storage;
	}
	public void setStorage(StorageBO storage) {
		this.storage = storage;
	}
	
	
}
