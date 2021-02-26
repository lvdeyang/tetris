package com.sumavision.tetris.mims.app.media.stream.video.program;/**
 * Created by Poemafar on 2021/2/25 11:03
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName: MediaProgramVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/25 11:03
 */
public class MediaProgramVO {


    private String name;

    private String provider;

    private Integer num;

    private Integer pcrPid;

    private Integer pmtPid;

    private JSONArray audios;

    private JSONArray videos;

    private JSONArray subtitles;

    public MediaProgramVO(MediaProgramPO programPO) {
        this.name=programPO.getName();
        this.num=programPO.getNum();
        this.provider=programPO.getProvider();
        this.pcrPid=programPO.getPcrPid();
        this.pmtPid=programPO.getPmtPid();
        this.audios= JSON.parseArray(programPO.getAudioJson()) ;
        this.videos= JSON.parseArray(programPO.getVideoJson()) ;
        this.subtitles=JSON.parseArray(programPO.getSubtitleJson()) ;
    }

    public String getName() {
        return name;
    }

    public MediaProgramVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public MediaProgramVO setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public MediaProgramVO setNum(Integer num) {
        this.num = num;
        return this;
    }

    public Integer getPcrPid() {
        return pcrPid;
    }

    public MediaProgramVO setPcrPid(Integer pcrPid) {
        this.pcrPid = pcrPid;
        return this;
    }

    public Integer getPmtPid() {
        return pmtPid;
    }

    public MediaProgramVO setPmtPid(Integer pmtPid) {
        this.pmtPid = pmtPid;
        return this;
    }

    public JSONArray getAudios() {
        return audios;
    }

    public MediaProgramVO setAudios(JSONArray audios) {
        this.audios = audios;
        return this;
    }

    public JSONArray getVideos() {
        return videos;
    }

    public MediaProgramVO setVideos(JSONArray videos) {
        this.videos = videos;
        return this;
    }

    public JSONArray getSubtitles() {
        return subtitles;
    }

    public MediaProgramVO setSubtitles(JSONArray subtitles) {
        this.subtitles = subtitles;
        return this;
    }
}
