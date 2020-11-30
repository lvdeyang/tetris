package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/11/5 16:28
 */

import com.sumavision.tetris.business.common.enumeration.TreatMode;
import com.sumavision.tetris.business.common.enumeration.TreatPlat;
import com.sumavision.tetris.business.common.enumeration.TreatType;

import java.util.List;

/**
 * @ClassName: ProcessVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/5 16:28
 */
public class ProcessVO {

    private TreatType treat_type;

    private TreatPlat plat;

    private Integer nv_card_idx;

    private TreatMode mode;

    private Integer width;

    private Integer height;

    private Integer x;

    private Integer y;

    private String fps;

    //-------SCALE----
    private Integer auto_blackside;

    private String ratio;

    //----------HDR--------
    private String colorspace;

    private String colortransfer;

    private String colorprimaries;

    private String colorrange;

    //----------enhance-------
    /** 亮度 -255-255 */
    private Integer brightness;

    /** 色度 */
    private Integer chrome;

    /** 对比度 -100-100 */
    private Integer contrast;

    /** 饱和度 -100-100 */
    private Integer saturation;

    /** Gamma变换 */
    private Integer gamma;

    private String localenhance;

    //--------滤波--------
    private String denoise;

    private String sharpen;

    //-------重采样------
    private Integer sample_rate;

    /** 声道布局  mono/stereo */
    private String channel_layout;

    private String format;

    //--------文本OSD---------
    private List<TextOsdVO> text_osds;
    //-----静态图片OSD--------
    //-----动态图片OSD---------
    private List<PictureOsdVO> pic_osds;
    //-------区域模糊
    private List<FuzzyVO> fuzzys;

    //------音频增益----
    private String gain_mode;

    private Integer volume;

    public TreatType getTreat_type() {
        return treat_type;
    }

    public void setTreat_type(TreatType treat_type) {
        this.treat_type = treat_type;
    }

    public TreatPlat getPlat() {
        return plat;
    }

    public void setPlat(TreatPlat plat) {
        this.plat = plat;
    }

    public Integer getNv_card_idx() {
        return nv_card_idx;
    }

    public void setNv_card_idx(Integer nv_card_idx) {
        this.nv_card_idx = nv_card_idx;
    }

    public TreatMode getMode() {
        return mode;
    }

    public void setMode(TreatMode mode) {
        this.mode = mode;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public String getColorspace() {
        return colorspace;
    }

    public void setColorspace(String colorspace) {
        this.colorspace = colorspace;
    }

    public String getColortransfer() {
        return colortransfer;
    }

    public void setColortransfer(String colortransfer) {
        this.colortransfer = colortransfer;
    }

    public String getColorprimaries() {
        return colorprimaries;
    }

    public void setColorprimaries(String colorprimaries) {
        this.colorprimaries = colorprimaries;
    }

    public String getColorrange() {
        return colorrange;
    }

    public void setColorrange(String colorrange) {
        this.colorrange = colorrange;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public Integer getChrome() {
        return chrome;
    }

    public void setChrome(Integer chrome) {
        this.chrome = chrome;
    }

    public Integer getContrast() {
        return contrast;
    }

    public void setContrast(Integer contrast) {
        this.contrast = contrast;
    }

    public Integer getSaturation() {
        return saturation;
    }

    public void setSaturation(Integer saturation) {
        this.saturation = saturation;
    }

    public Integer getGamma() {
        return gamma;
    }

    public void setGamma(Integer gamma) {
        this.gamma = gamma;
    }

    public String getLocalenhance() {
        return localenhance;
    }

    public void setLocalenhance(String localenhance) {
        this.localenhance = localenhance;
    }

    public String getDenoise() {
        return denoise;
    }

    public void setDenoise(String denoise) {
        this.denoise = denoise;
    }

    public String getSharpen() {
        return sharpen;
    }

    public void setSharpen(String sharpen) {
        this.sharpen = sharpen;
    }

    public Integer getSample_rate() {
        return sample_rate;
    }

    public void setSample_rate(Integer sample_rate) {
        this.sample_rate = sample_rate;
    }

    public String getChannel_layout() {
        return channel_layout;
    }

    public void setChannel_layout(String channel_layout) {
        this.channel_layout = channel_layout;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<TextOsdVO> getText_osds() {
        return text_osds;
    }

    public void setText_osds(List<TextOsdVO> text_osds) {
        this.text_osds = text_osds;
    }

    public List<PictureOsdVO> getPic_osds() {
        return pic_osds;
    }

    public void setPic_osds(List<PictureOsdVO> pic_osds) {
        this.pic_osds = pic_osds;
    }

    public List<FuzzyVO> getFuzzys() {
        return fuzzys;
    }

    public void setFuzzys(List<FuzzyVO> fuzzys) {
        this.fuzzys = fuzzys;
    }

    public Integer getAuto_blackside() {
        return auto_blackside;
    }

    public void setAuto_blackside(Integer auto_blackside) {
        this.auto_blackside = auto_blackside;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getGain_mode() {
        return gain_mode;
    }

    public void setGain_mode(String gain_mode) {
        this.gain_mode = gain_mode;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
