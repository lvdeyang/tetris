package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestOutputVO {
	/** 输出地址 */
	private String url;
	
	private String localIp;
	
	/** 输出封装 */
	private String type;
	
	/** 输出码率 */
	private String bitrate;
	
	/** 码率控制*/
	private String rate_ctrl;
	
	/** 索引*/
	private Integer index;
	
	private String scramble_mode;
	
	private String scramble_key;
	

	public String getUrl() {
		return url;
	}

	public BroadAbilityBroadRequestOutputVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getType() {
		return type;
	}

	public BroadAbilityBroadRequestOutputVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getLocalIp() {
		return localIp;
	}

	public BroadAbilityBroadRequestOutputVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}

	public String getBitrate() {
		return bitrate;
	}

	public BroadAbilityBroadRequestOutputVO setBitrate(String bitrate) {
		this.bitrate = bitrate;
		return this;
	}

	public String getRate_ctrl() {
		return rate_ctrl;
	}

	public BroadAbilityBroadRequestOutputVO setRate_ctrl(String rate_ctrl) {
		this.rate_ctrl = rate_ctrl;
		return this;
	}

	public Integer getIndex() {
		return index;
	}

	public BroadAbilityBroadRequestOutputVO setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public String getScramble_mode() {
		return scramble_mode;
	}

	public BroadAbilityBroadRequestOutputVO setScramble_mode(String scramble_mode) {
		this.scramble_mode = scramble_mode;
		return this;
	}

	public String getScramble_key() {
		return scramble_key;
	}

	public BroadAbilityBroadRequestOutputVO setScramble_key(String scramble_key) {
		this.scramble_key = scramble_key;
		return this;
	}
}
