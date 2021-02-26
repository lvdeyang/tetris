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
    ZIXI_TS,
    UDP_PASSBY,
    RTP_TS_PASSBY,
    HTTP_TS_PASSBY,
    SRT_TS_PASSBY,
    ZIXI_TS_PASSBY,
    HLS,
    DASH,
    RTSP,
    RTP_ES,
    RTMP,
    HTTP_FLV,
    HLS_RECORD,
    UDP_PCM,
    ASI,
    MSS,
    SDI,
    SDIIP,
    FILE,
    BACKUP,
    SCHEDULE;
    public static ProtocolType getProtocolType(String type) throws BaseException {
        switch (type.toLowerCase(Locale.ENGLISH)){
            case "udp":
            case "udpts":
            case "tsudp":
            case "udp_ts":
            case "udp-ts":
            case "ts-udp":
                return UDP_TS;
            case "tsudppassby":
            case "udp_passby":
                return UDP_PASSBY;
            case "rtp":
            case "rtpts":
            case "tsrtp":
            case "rtp_ts":
            case "rtp-ts":
            case "ts-rtp":
                return RTP_TS;
            case "rtp_passby":
            case "tsrtppassby":
                return RTP_TS_PASSBY;
            case "httpts":
            case "tshttp":
            case "http_ts":
                return HTTP_TS;
            case "http_passby":
                return HTTP_TS_PASSBY;
            case "srt":
            case "srt_ts":
            case "ts_srt":
            case "srtts":
            case "tssrt":
                return SRT_TS;
            case "srt_passby":
            case "tssrtpassby":
                return SRT_TS_PASSBY;
            case "hls":
                return HLS;
            case "dash":
                return DASH;
            case "rtsp":
            case "rtsprtp":
            case "rtsp-rtp":
            case "rtsp_rtp":
                return RTSP;
            case "rtp_es":
            case "rtpes":
                return RTP_ES;
            case "rtmp":
            case "rtmp_flv":
            case "rtmpflv":
                return RTMP;
            case "http_flv":
            case "httpflv":
                return HTTP_FLV;
            case "record":
            case "hlsrecord":
            case "hls_record":
                return HLS_RECORD;
            case "zixi":
            case "zixi_ts":
                return ZIXI_TS;
            case "zixi_passby":
            case "zixipassby":
                return ZIXI_TS_PASSBY;
            case "pcm":
            case "udp_pcm":
            case "udppcm":
            case "pcmudp":
                return UDP_PCM;
            case "asi":
                return ASI;
            case "mss":
                return MSS;
            case "sdi":
                return SDI;
            case "sdiip":
                return SDIIP;
            case "file":
                return FILE;
            case "schedule":
                return SCHEDULE;
            case "back_up":
            case "backup":
                return BACKUP;
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

    public static ProtocolType getPassbyType(ProtocolType type) throws BaseException {
        switch (type){
            case UDP_TS:
                return UDP_PASSBY;
            case RTP_TS:
                return RTP_TS_PASSBY;
            case HTTP_TS:
                return HTTP_TS_PASSBY;
            case SRT_TS:
                return SRT_TS_PASSBY;
            case ZIXI_TS:
                return ZIXI_TS_PASSBY;
        }
        throw new BaseException(StatusCode.FORBIDDEN,"no passby protocol type:"+type);
    }

    public static boolean beSpecialType(String type) throws BaseException {
        ProtocolType protocolType = getProtocolType(type);
        if (protocolType==BACKUP || protocolType==SCHEDULE) {
            return true;
        }
        return false;
    }
}
