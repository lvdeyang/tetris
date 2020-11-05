package com.sumavision.signal.bvc.fifthg.bo.http;/**
 * Created by Poemafar on 2020/8/31 9:04
 */

/**
 * @ClassName: AudioEncParam
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/31 9:04
 */
public class AudioEncParam {

    /**
     * 0：关闭；Off
     * 1：编码；Enc（默认）
     * 2：透传：Passthrough
     */
    private Integer audOnOff = 1;
    /**
     * 1：SDI-Ch 1,2（默认）
     * 2：SDI-Ch 3,4
     * 3：SDI-Ch 5,6
     * 4：SDI-Ch 7,8
     * 5：SDI-Ch 9,10
     * 6：SDI-Ch 11,12
     * 7：SDI-Ch 13,14
     * 8：SDI-Ch 15,16
     * 9：HDMI-Ch 1,2
     * 10：HDMI-Ch 3,4
     * 11：HDMI-Ch 5,6
     * 12：HDMI-Ch 7,8
     * 0：Test Tone
     */
    private Integer audLr = 0;

    /**
     * 音频编码格式,默认编AAC
     * 0：AAC-LC
     * 1：HE-AAC v1
     * 2：HE-AAC v2
     * 3：MPEG-1 Layer II（默认）
     * 4：Dolby Digital
     * 5：Dolby Digital Plus
     * 6：AC3
     */
    private Long audStd = 0L;
    /**
     * 音频码率
     * 0：32kbps
     * 1：48kbps
     * 2：56kbps
     * 3：64kbps
     * 4：80kbps
     * 5：96kbps
     * 6：112kbps
     * 7：128kbps（默认）
     * 8：160kbps
     * 9：192kbps
     * 10：224kbps
     * 11：256kbps
     * 12：320kbps
     * 13：384kbps
     */
    private Long audRate = 7L;
    private Integer audDelay = 0;

    public Integer getAudOnOff() {
        return audOnOff;
    }

    public void setAudOnOff(Integer audOnOff) {
        this.audOnOff = audOnOff;
    }

    public Integer getAudLr() {
        return audLr;
    }

    public void setAudLr(Integer audLr) {
        this.audLr = audLr;
    }

    public Long getAudStd() {
        return audStd;
    }

    public void setAudStd(Long audStd) {
        this.audStd = audStd;
    }

    public Long getAudRate() {
        return audRate;
    }

    public void setAudRate(Long audRate) {
        this.audRate = audRate;
    }

    public Integer getAudDelay() {
        return audDelay;
    }

    public void setAudDelay(Integer audDelay) {
        this.audDelay = audDelay;
    }
}
