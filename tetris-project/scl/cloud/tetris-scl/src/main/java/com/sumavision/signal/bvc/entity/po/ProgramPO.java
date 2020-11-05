package com.sumavision.signal.bvc.entity.po;/**
 * Created by Poemafar on 2020/9/3 15:39
 */

import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @ClassName: ProgramPO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/3 15:39
 */
@Entity
@Table(name = "TETRIS_SCL_PROGRAM")
public class ProgramPO  extends AbstractBasePO {

    /**
     * 关联capacitypermissionportpo表信息，记录该permission输出地址的刷源信息
     */

    private String name;

    private String provider;

    @JSONField(name = "program_number")
    private Integer programNum;

    @JSONField(name = "pcr_pid")
    private Integer pcrPid;

    @JSONField(name = "pmt_pid")
    private Integer pmtPid;

    private Integer encryption;

    private String deinterlaceMode = "off";

    @JSONField(name = "audio_array")
    private String audioJson;

    @JSONField(name = "video_array")
    private String videoJson;

    @JSONField(name = "subtitle_array")
    private String subtitleJson;

    //所属任务输入（InputPO）ID
    private Long inputId;

    //解码方式:cpu/gpu/auto
    private String decodeMode;

    //所属编码卡卡号
    private Integer cardNum;

    //备源切换条件
    private Boolean media_lost = true;;

    private Boolean cutoff = false;

    private Boolean plp_high = false;

    private Boolean highFirstFlag = false;

    //转换能力新参数：断流备份模式：none，still_picture，pattern
    private String backup_mode = "none";

    private String audio_backup_mode = "none";

    private Integer cutoff_time = 2000;

    private String pattern_path = "";

    //手动切换模式下，选择的索引
    private String backup_select_index = "0";

    //输入延时设置
    private Integer delay_ms = 0;

    //节目映射类型
    private String programMapType = "normal_map";


    @Column
    public String getAudio_backup_mode() {
        return audio_backup_mode;
    }

    public void setAudio_backup_mode(String audio_backup_mode) {
        this.audio_backup_mode = audio_backup_mode;
    }

    @Column
    public String getProgramMapType() {
        return programMapType;
    }

    public void setProgramMapType(String programMapType) {
        this.programMapType = programMapType;
    }

    @Column
    public Integer getDelay_ms() {
        return delay_ms;
    }

    public void setDelay_ms(Integer delay_ms) {
        this.delay_ms = delay_ms;
    }


    @Column
    public String getBackup_select_index() {
        return backup_select_index;
    }

    public void setBackup_select_index(String backup_select_index) {
        this.backup_select_index = backup_select_index;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }


    @Column
    public Integer getEncryption() {
        return encryption;
    }
    public void setEncryption(Integer encryption) {
        this.encryption = encryption;
    }
    @Column(columnDefinition="TEXT")
    public String getAudioJson() {
        return audioJson;
    }

    public void setAudioJson(String audioJson) {
        this.audioJson = audioJson;
    }
    @Column(columnDefinition="TEXT")
    public String getVideoJson() {
        return videoJson;
    }

    public void setVideoJson(String videoJson) {
        this.videoJson = videoJson;
    }
    @Column(columnDefinition="TEXT")
    public String getSubtitleJson() {
        return subtitleJson;
    }

    public void setSubtitleJson(String subtitleJson) {
        this.subtitleJson = subtitleJson;
    }

    @Column
    public Integer getProgramNum() {
        return programNum;
    }

    public void setProgramNum(Integer programNum) {
        this.programNum = programNum;
    }
    @Column
    public Integer getPcrPid() {
        return pcrPid;
    }

    public void setPcrPid(Integer pcrPid) {
        this.pcrPid = pcrPid;
    }
    @Column
    public Integer getPmtPid() {
        return pmtPid;
    }

    public void setPmtPid(Integer pmtPid) {
        this.pmtPid = pmtPid;
    }


    @Column
    public String getDeinterlaceMode() {
        return deinterlaceMode;
    }
    public void setDeinterlaceMode(String deinterlaceMode) {
        this.deinterlaceMode = deinterlaceMode;
    }


    @Column
    public Boolean getHighFirstFlag() {
        return highFirstFlag;
    }
    public void setHighFirstFlag(Boolean highFirstFlag) {
        this.highFirstFlag = highFirstFlag;
    }

    @Column
    public Long getInputId() {
        return inputId;
    }
    public void setInputId(Long inputId) {
        this.inputId = inputId;
    }

    @Column
    public Integer getCardNum() {
        return cardNum;
    }
    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    @Column
    public String getDecodeMode() {
        return decodeMode;
    }
    public void setDecodeMode(String decodeMode) {
        this.decodeMode = decodeMode;
    }


    @Column
    public Boolean getMedia_lost() {
        return media_lost;
    }

    public void setMedia_lost(Boolean media_lost) {
        this.media_lost = media_lost;
    }

    @Column
    public Boolean getCutoff() {
        return cutoff;
    }

    public void setCutoff(Boolean cutoff) {
        this.cutoff = cutoff;
    }

    @Column
    public Boolean getPlp_high() {
        return plp_high;
    }

    public void setPlp_high(Boolean plp_high) {
        this.plp_high = plp_high;
    }

    @Column
    public String getBackup_mode() {
        return backup_mode;
    }

    public void setBackup_mode(String backup_mode) {
        this.backup_mode = backup_mode;
    }

    @Column
    public Integer getCutoff_time() {
        return cutoff_time;
    }

    public void setCutoff_time(Integer cutoff_time) {
        this.cutoff_time = cutoff_time;
    }

    @Column
    public String getPattern_path() {
        return pattern_path;
    }

    public void setPattern_path(String pattern_path) {
        this.pattern_path = pattern_path;
    }



}
