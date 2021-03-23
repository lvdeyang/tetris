package com.sumavision.signal.bvc.capacity.vo;/**
 * Created by Poemafar on 2020/11/4 14:04
 */

/**
 * @ClassName: MediaVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/4 14:04
 */
public class MediaVO {

    private Integer track_id;

    private Integer pid;

    private Integer payload_type;

    private Integer bitrate;

    private String seg_format;

    private Integer bandwidth;

    public Integer getTrack_id() {
        return track_id;
    }

    public void setTrack_id(Integer track_id) {
        this.track_id = track_id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getPayload_type() {
        return payload_type;
    }

    public void setPayload_type(Integer payload_type) {
        this.payload_type = payload_type;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getSeg_format() {
        return seg_format;
    }

    public void setSeg_format(String seg_format) {
        this.seg_format = seg_format;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }
}
