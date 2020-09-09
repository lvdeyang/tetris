package com.sumavision.tetris.guide.BO;

public class GuideH264BO {
	
	/**gop开关 */
	private boolean open_gop;
	
	/**gop大小 */
	private int gop_size;
	
	/**编码级别 */
	private int level;
	
	/**编码档次 */
	private String profile;
	
	/**帧率 */
	private int fps;
	
	/**宏块树搜索开关 */
	private boolean mbtree;
	
	/**码率 */
	private int bitrate;
	
	/**熵编码 */
	private String entropy_type;
	
	/**参考帧个数 */
	private int ref_frames;
	
	/**分辨率 */
	private String resolution;
	
	/**最大码率 */
	private int max_bitrate;
	
	private String encoding_type;
	
	/**编码库对象，h264编码暂时写死x264，不考虑别的库 */
	private GuideX264BO x264;
	
	/**编码延时 */
	private int lookahead;
	
	/**场景切换 */
	private boolean scenecut;
	
	/**场景切换 */
	private String rc_mode;
	
	private int max_bframe;
	
	/**宽高比 */
	private String ratio;

	public boolean isOpen_gop() {
		return open_gop;
	}

	public void setOpen_gop(boolean open_gop) {
		this.open_gop = open_gop;
	}

	public int getGop_size() {
		return gop_size;
	}

	public void setGop_size(int gop_size) {
		this.gop_size = gop_size;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public boolean isMbtree() {
		return mbtree;
	}

	public void setMbtree(boolean mbtree) {
		this.mbtree = mbtree;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public String getEntropy_type() {
		return entropy_type;
	}

	public void setEntropy_type(String entropy_type) {
		this.entropy_type = entropy_type;
	}

	public int getRef_frames() {
		return ref_frames;
	}

	public void setRef_frames(int ref_frames) {
		this.ref_frames = ref_frames;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public int getMax_bitrate() {
		return max_bitrate;
	}

	public void setMax_bitrate(int max_bitrate) {
		this.max_bitrate = max_bitrate;
	}

	public String getEncoding_type() {
		return encoding_type;
	}

	public void setEncoding_type(String encoding_type) {
		this.encoding_type = encoding_type;
	}

	public GuideX264BO getX264() {
		return x264;
	}

	public void setX264(GuideX264BO x264) {
		this.x264 = x264;
	}

	public int getLookahead() {
		return lookahead;
	}

	public void setLookahead(int lookahead) {
		this.lookahead = lookahead;
	}

	public boolean isScenecut() {
		return scenecut;
	}

	public void setScenecut(boolean scenecut) {
		this.scenecut = scenecut;
	}

	public String getRc_mode() {
		return rc_mode;
	}

	public void setRc_mode(String rc_mode) {
		this.rc_mode = rc_mode;
	}

	public int getMax_bframe() {
		return max_bframe;
	}

	public void setMax_bframe(int max_bframe) {
		this.max_bframe = max_bframe;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	
}
