package com.sumavision.tetris.business.common.enumeration;/**
 * Created by Poemafar on 2020/9/18 15:52
 */

import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

import java.util.Locale;

/**
 * @ClassName: ProtocolType
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/18 15:52
 */
public enum ProtocolType {
    UDP_TS,
    RTP_TS,
    HTTP_TS,
    SRT_TS,
    HLS,
    DASH,
    RTSP,
    RTP_ES,
    RTMP,
    HTTP_FLV,
    HLS_RECORD,
    UDP_PCM,
    ZIXI_TS,
    ASI,
    MSS,
    SDI,
    FILE,
    BACKUP,
    SCHEDULE;
    public static ProtocolType getProtocolType(String type) throws BaseException {
        switch (type.toLowerCase(Locale.ENGLISH)){
            case "udp":
            case "udp_ts":
                return UDP_TS;
            case "rtp":
            case "rtpts":
            case "rtp_ts":
                return RTP_TS;
            case "httpts":
            case "http_ts":
                return HTTP_TS;
            case "srt":
            case "srt_ts":
                return SRT_TS;
            case "hls":
                return HLS;
            case "dash":
                return DASH;
            case "rtsp":
                return RTSP;
            case "rtp_es":
            case "rtpes":
                return RTP_ES;
            case "rtmp":
            case "rtmp_flv":
                return RTMP;
            case "http_flv":
                return HTTP_FLV;
            case "record":
            case "hlsrecord":
            case "hls_record":
                return HLS_RECORD;
            case "zixi":
            case "zixi_ts":
                return ZIXI_TS;
            case "pcm":
            case "udp_pcm":
            case "udppcm":
                return UDP_PCM;
            case "asi":
                return ASI;
            case "mss":
                return MSS;
            case "sdi":
                return SDI;
            case "file":
                return FILE;
            case "schedule":
                return SCHEDULE;
        }
        throw new BaseException(StatusCode.FORBIDDEN,"unknown protocol type:"+type);
    }

    public static ProtocolType getProtocolType(InputBO inputBO) throws BaseException {
        if (inputBO.getUdp_ts() != null) {
            return UDP_TS;
        }
        if (inputBO.getRtp_ts()!=null){
            return RTP_TS;
        }
        if (inputBO.getHttp_ts() != null) {
            return HTTP_TS;
        }
        if (inputBO.getSrt_ts() != null) {
            return SRT_TS;
        }
        if (inputBO.getHls() != null) {
            return HLS;
        }
        if (inputBO.getDash() != null) {
            return DASH;
        }
        if (inputBO.getRtsp() != null) {
            return RTSP;
        }
        if (inputBO.getRtp_es() != null) {
            return RTP_ES;
        }
        if (inputBO.getRtmp() != null) {
            return RTMP;
        }
        if (inputBO.getHttp_flv() != null) {
            return HTTP_FLV;
        }
        if (inputBO.getZixi() != null) {
            return ZIXI_TS;
        }
        if (inputBO.getUdp_pcm() != null) {
            return UDP_PCM;
        }
        if (inputBO.getMss() != null) {
            return MSS;
        }
        if (inputBO.getSdi() != null) {
            return SDI;
        }
        if (inputBO.getFile() != null) {
            return FILE;
        }
        if (inputBO.getSchedule() != null) {
            return SCHEDULE;
        }
        throw new BaseException(StatusCode.FORBIDDEN,"unknown protocol type, inputId:"+inputBO.getId());
    }
}
