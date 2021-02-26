package com.sumavision.tetris.mims.app.media.stream.video.program;/**
 * Created by Poemafar on 2021/2/25 9:00
 */

import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: MediaProgramPO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/25 9:00
 */
@Entity
@Table(name = "MIMS_MEDIA_PROGRAM")
public class MediaProgramPO extends AbstractBasePO {

    /**
     * 节目名
     */
    @JSONField(name="name")
    private String name;

    /**
     * 节目提供商
     */
    @JSONField(name="provider")
    private String provider;

    /**
     * 节目号
     */
    @JSONField(name="program_number")
    private Integer num;

    @JSONField(name="pcr_pid")
    private Integer pcrPid;

    private Integer pmtPid;

    @JSONField(name="video_array")
    private String videoJson;

    @JSONField(name="audio_array")
    private String audioJson;

    @JSONField(name="subtitle_array")
    private String subtitleJson;

    public String getName() {
        return name;
    }

    public MediaProgramPO setName(String name) {
        this.name = name;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public MediaProgramPO setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public MediaProgramPO setNum(Integer num) {
        this.num = num;
        return this;
    }

    public Integer getPcrPid() {
        return pcrPid;
    }

    public MediaProgramPO setPcrPid(Integer pcrPid) {
        this.pcrPid = pcrPid;
        return this;
    }

    public Integer getPmtPid() {
        return pmtPid;
    }

    public MediaProgramPO setPmtPid(Integer pmtPid) {
        this.pmtPid = pmtPid;
        return this;
    }
    @Column(columnDefinition="TEXT")
    public String getVideoJson() {
        return videoJson;
    }

    public MediaProgramPO setVideoJson(String videoJson) {
        this.videoJson = videoJson;
        return this;
    }
    @Column(columnDefinition="TEXT")
    public String getAudioJson() {
        return audioJson;
    }

    public MediaProgramPO setAudioJson(String audioJson) {
        this.audioJson = audioJson;
        return this;
    }
    @Column(columnDefinition="TEXT")
    public String getSubtitleJson() {
        return subtitleJson;
    }

    public MediaProgramPO setSubtitleJson(String subtitleJson) {
        this.subtitleJson = subtitleJson;
        return this;
    }
}
