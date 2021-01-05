package com.sumavision.tetris.capacity.bo.task;/**
 * Created by Poemafar on 2020/12/23 13:47
 */

/**
 * @ClassName: ScreenCapBO 截图
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/23 13:47
 */
public class ScreenCapBO {

    private String plat;

    private Integer nv_card_idx;

    private String path;

    private String time_span;

    private Integer max_buf;

    private String jpeg_pre;

    private String resolution;

    public String getPlat() {
        return plat;
    }

    public ScreenCapBO setPlat(String plat) {
        this.plat = plat;
        return this;
    }

    public Integer getNv_card_idx() {
        return nv_card_idx;
    }

    public ScreenCapBO setNv_card_idx(Integer nv_card_idx) {
        this.nv_card_idx = nv_card_idx;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ScreenCapBO setPath(String path) {
        this.path = path;
        return this;
    }

    public String getTime_span() {
        return time_span;
    }

    public ScreenCapBO setTime_span(String time_span) {
        this.time_span = time_span;
        return this;
    }

    public Integer getMax_buf() {
        return max_buf;
    }

    public ScreenCapBO setMax_buf(Integer max_buf) {
        this.max_buf = max_buf;
        return this;
    }

    public String getJpeg_pre() {
        return jpeg_pre;
    }

    public ScreenCapBO setJpeg_pre(String jpeg_pre) {
        this.jpeg_pre = jpeg_pre;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public ScreenCapBO setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }
}
