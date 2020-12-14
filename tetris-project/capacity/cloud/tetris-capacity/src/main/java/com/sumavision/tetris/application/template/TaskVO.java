package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/10/27 14:35
 */

import java.util.List;

/**
 * @ClassName: TaskVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/10/27 14:35
 */
public class TaskVO {

    private Integer index;

    private String codec;

    private String resolution;

    private String fps;

    private String profile;

    /**
     * 视频码率
     */
    private Long vbitrate;

    /**
     * 码率控制方式
     */
    private String rc_mode;



    private String code_lib;

//---------------------------h264--------------------------------------
    private Integer level;

    private Integer ref_frames;

    /** 编码延时 0-100*/
    private Integer lookahead;

    private boolean scenecut;

    private boolean mbtree;

    /** 熵编码算法 cabac/cavlc */
    private String entropy_type;

    /** 编码方式 Progressive/Interlace/MbAFF/PAFF */
    private String encoding_type;

    private Integer vbv_buffer_size;

    //-------x264-----
    private Integer keyint_min;

    private Integer keyint_max;

    /** 质量模式 */
    private Integer refine;

    /** 原始数据格式 yuv420/yuv422/yuv420_10bit/yuv422_10bit */
    private String pixel_format;

    /** b帧参考模式 None/Strict/Normal */
    private String bframe_reference;

    private boolean bframe_adaptive;

    /** 运动搜索范围 16-256 */
    private Integer me_range;

    private String tune_mode ;

    //--------ux264----------
    /** 统计复用ID */
    private Integer stastical_id;

    private boolean stastical_flag;

    //-------264msdk----------
    private Integer max_bframe;

    private Integer quality;

    //----------264n卡--------
    private Integer nv_card_idx;

    private String tier;

    //----------------------------h265--------------------------------
    //----------x265------------
    private String preset;
    //----------ux265-----------
    private Integer max_cu_size;

    private Integer max_tu_size;

    private Integer tu_depth;

    private Integer cu_depth;

    private Integer frame_num_threads;
    //-----------265svt----------
    private Integer enc_mode;
    //-----------265cuda----------
    //-----------265msdk----------

    //----------------------------mpeg2-----------------------------------
    //---------m2v----------
    private boolean ts_control;
    //----------------------------avs2------------------------------------

    //----------------------------------------------------audio-------------------------------------------

    private String abitrate;

    private String sample_rate;

    private String sample_fmt;

    private Integer sample_byte;

    private String channel_layout;

    //-----------g711a,g711u---------
    private Integer ptime;

    //------------------------------------------预处理-------------------------------------------------------
    private List<ProcessVO> pretreatments;

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Long getVbitrate() {
        return vbitrate;
    }

    public void setVbitrate(Long vbitrate) {
        this.vbitrate = vbitrate;
    }

    public String getRc_mode() {
        return rc_mode;
    }

    public void setRc_mode(String rc_mode) {
        this.rc_mode = rc_mode;
    }

    public String getCode_lib() {
        return code_lib;
    }

    public void setCode_lib(String code_lib) {
        this.code_lib = code_lib;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRef_frames() {
        return ref_frames;
    }

    public void setRef_frames(Integer ref_frames) {
        this.ref_frames = ref_frames;
    }

    public Integer getLookahead() {
        return lookahead;
    }

    public void setLookahead(Integer lookahead) {
        this.lookahead = lookahead;
    }

    public boolean isScenecut() {
        return scenecut;
    }

    public void setScenecut(boolean scenecut) {
        this.scenecut = scenecut;
    }

    public boolean isMbtree() {
        return mbtree;
    }

    public void setMbtree(boolean mbtree) {
        this.mbtree = mbtree;
    }

    public String getEntropy_type() {
        return entropy_type;
    }

    public void setEntropy_type(String entropy_type) {
        this.entropy_type = entropy_type;
    }

    public String getEncoding_type() {
        return encoding_type;
    }

    public void setEncoding_type(String encoding_type) {
        this.encoding_type = encoding_type;
    }

    public Integer getVbv_buffer_size() {
        return vbv_buffer_size;
    }

    public void setVbv_buffer_size(Integer vbv_buffer_size) {
        this.vbv_buffer_size = vbv_buffer_size;
    }

    public Integer getKeyint_min() {
        return keyint_min;
    }

    public void setKeyint_min(Integer keyint_min) {
        this.keyint_min = keyint_min;
    }

    public Integer getKeyint_max() {
        return keyint_max;
    }

    public void setKeyint_max(Integer keyint_max) {
        this.keyint_max = keyint_max;
    }

    public Integer getRefine() {
        return refine;
    }

    public void setRefine(Integer refine) {
        this.refine = refine;
    }

    public String getPixel_format() {
        return pixel_format;
    }

    public void setPixel_format(String pixel_format) {
        this.pixel_format = pixel_format;
    }

    public String getBframe_reference() {
        return bframe_reference;
    }

    public void setBframe_reference(String bframe_reference) {
        this.bframe_reference = bframe_reference;
    }

    public boolean isBframe_adaptive() {
        return bframe_adaptive;
    }

    public void setBframe_adaptive(boolean bframe_adaptive) {
        this.bframe_adaptive = bframe_adaptive;
    }

    public Integer getMe_range() {
        return me_range;
    }

    public void setMe_range(Integer me_range) {
        this.me_range = me_range;
    }

    public String getTune_mode() {
        return tune_mode;
    }

    public void setTune_mode(String tune_mode) {
        this.tune_mode = tune_mode;
    }

    public Integer getStastical_id() {
        return stastical_id;
    }

    public void setStastical_id(Integer stastical_id) {
        this.stastical_id = stastical_id;
    }

    public boolean isStastical_flag() {
        return stastical_flag;
    }

    public void setStastical_flag(boolean stastical_flag) {
        this.stastical_flag = stastical_flag;
    }

    public Integer getMax_bframe() {
        return max_bframe;
    }

    public void setMax_bframe(Integer max_bframe) {
        this.max_bframe = max_bframe;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getNv_card_idx() {
        return nv_card_idx;
    }

    public void setNv_card_idx(Integer nv_card_idx) {
        this.nv_card_idx = nv_card_idx;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public Integer getMax_cu_size() {
        return max_cu_size;
    }

    public void setMax_cu_size(Integer max_cu_size) {
        this.max_cu_size = max_cu_size;
    }

    public Integer getMax_tu_size() {
        return max_tu_size;
    }

    public void setMax_tu_size(Integer max_tu_size) {
        this.max_tu_size = max_tu_size;
    }

    public Integer getTu_depth() {
        return tu_depth;
    }

    public void setTu_depth(Integer tu_depth) {
        this.tu_depth = tu_depth;
    }

    public Integer getCu_depth() {
        return cu_depth;
    }

    public void setCu_depth(Integer cu_depth) {
        this.cu_depth = cu_depth;
    }

    public Integer getFrame_num_threads() {
        return frame_num_threads;
    }

    public void setFrame_num_threads(Integer frame_num_threads) {
        this.frame_num_threads = frame_num_threads;
    }

    public Integer getEnc_mode() {
        return enc_mode;
    }

    public void setEnc_mode(Integer enc_mode) {
        this.enc_mode = enc_mode;
    }

    public boolean isTs_control() {
        return ts_control;
    }

    public void setTs_control(boolean ts_control) {
        this.ts_control = ts_control;
    }

    public String getAbitrate() {
        return abitrate;
    }

    public void setAbitrate(String abitrate) {
        this.abitrate = abitrate;
    }

    public String getSample_rate() {
        return sample_rate;
    }

    public void setSample_rate(String sample_rate) {
        this.sample_rate = sample_rate;
    }

    public String getSample_fmt() {
        return sample_fmt;
    }

    public void setSample_fmt(String sample_fmt) {
        this.sample_fmt = sample_fmt;
    }

    public Integer getSample_byte() {
        return sample_byte;
    }

    public void setSample_byte(Integer sample_byte) {
        this.sample_byte = sample_byte;
    }

    public String getChannel_layout() {
        return channel_layout;
    }

    public void setChannel_layout(String channel_layout) {
        this.channel_layout = channel_layout;
    }

    public Integer getPtime() {
        return ptime;
    }

    public void setPtime(Integer ptime) {
        this.ptime = ptime;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
