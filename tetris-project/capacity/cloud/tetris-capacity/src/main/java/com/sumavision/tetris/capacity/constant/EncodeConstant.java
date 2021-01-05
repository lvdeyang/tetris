package com.sumavision.tetris.capacity.constant;/**
 * Created by Poemafar on 2020/7/28 10:12
 */

import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

import java.util.Locale;

/**
 * @ClassName: EncodeConstant
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/7/28 10:12
 */
public class EncodeConstant {

    public enum VideoType{
        h264,hevc,mpeg2,avs,passby;

        public static VideoType getVideoType(String codec) throws BaseException {
            String type = codec.toLowerCase(Locale.ENGLISH);
            if (type.contains("passby")){
                return passby;
            }
            if (type.contains("264")){
                return h264;
            }
            if (type.contains("265") || type.contains("hevc") || type.contains("svt") || type.contains("orion")){
                return hevc;
            }
            if (type.contains("mpeg2") || type.contains("m2v")){
                return mpeg2;
            }
            if (type.contains("avs")){
                return avs;
            }

            throw new BaseException(StatusCode.ERROR,"not support codec: "+codec);
        }



        public static VideoType getVideoType(TplVideoEncoder type) throws CommonException {
            switch (type) {
                case VENCODER_X264:
                case VENCODER_UUX264:
                case VENCODER_MSDK_H264:
                case VENCODER_CUDA_H264:
                    return h264;
                case VENCODER_X265:
                case VENCODER_SVT:
                case VENCODER_ORION:
                case VENCODER_MSDK_H265:
                case VENCODER_CUDA_H265:
                    return hevc;
                case VENCODER_CPU_MPEG2:
                case VENCODER_MSDK_MPEG2:
                    return mpeg2;
                case VENCODER_AVS2:
                    return avs;
            }

            throw new CommonException("unknown tpl video encoder :"+type);
        }
    }



    public enum TplVideoEncoder {
        VENCODER_X264,      //"x264"
        VENCODER_UUX264,   //"uux264"
        VENCODER_X265,     //"x265"
        VENCODER_SVT,      //"svt"
        VENCODER_ORION,    //"ux265"
        VENCODER_CPU_MPEG2,//"m2v"
        VENCODER_AVS2,     //"avs2"
        VENCODER_MSDK_H264,//"msdk_h264"
        VENCODER_MSDK_H265,//"msdk_h265"
        VENCODER_MSDK_MPEG2,//"msdk_mpeg2"
        VENCODER_CUDA_H264, //"cuda_h264"
        VENCODER_CUDA_H265;  //"cuda_h265"

        public static TplVideoEncoder getDefaultCodeLibForVideoType(VideoType videoType) throws BaseException {
            //默认视频类型对应的编码库
            switch (videoType){
                case h264:
                    return VENCODER_X264;
                case hevc:
                    return VENCODER_X265;
                case mpeg2:
                    return VENCODER_CPU_MPEG2;
                case avs:
                    return VENCODER_AVS2;
            }
            throw new BaseException(StatusCode.ERROR,"unknown videotype:"+videoType);
        }

        public static TplVideoEncoder getTplVideoEncoder(String codelib) throws  CommonException {
            String encodeType = codelib.toUpperCase(Locale.ENGLISH);
            switch (encodeType) {
                case "H264": //h264默认库
                case "X264":
                case "VENCODER_X264":
                    return VENCODER_X264;
                case "UUX264":
                case "VENCODER_UUX264":
                    return VENCODER_UUX264;
                case "HEVC":  //h265默认库
                case "H265":  //h265默认库
                case "X265":
                case "VENCODER_X265":
                    return VENCODER_X265;
                case "SVT":
                case "VENCODER_SVT":
                    return VENCODER_SVT;
                case "UX265":
                case "ORION":
                case "VENCODER_ORION":
                    return VENCODER_ORION;
                case "MPEG2": //mpeg2默认库
                case "M2V":
                case "VENCODER_CPU_MPEG2":
                    return VENCODER_CPU_MPEG2;
                case "AVS":    //avs库
                case "AVS2":
                case "VENCODER_AVS2":
                    return VENCODER_AVS2;
                case "MSDK_H264":
                case "VENCODER_MSDK_H264":
                    return VENCODER_MSDK_H264;
                case "MSDK_H265":
                case "VENCODER_MSDK_H265":
                    return VENCODER_MSDK_H265;
                case "MSDK_MPEG2":
                case "VENCODER_MSDK_MPEG2":
                    return VENCODER_MSDK_MPEG2;
                case "CUDA_H264":
                case "VENCODER_CUDA_H264":
                    return VENCODER_CUDA_H264;
                case "CUDA_H265":
                case "VENCODER_CUDA_H265":
                    return VENCODER_CUDA_H265;
            }

            throw new CommonException("unknown codelib :" + codelib);
        }

        public static String getProtoName(TplVideoEncoder type) throws CommonException {
            switch (type) {
                case VENCODER_X264:
                    return "x264";
                case VENCODER_UUX264:
                    return "uux264";
                case VENCODER_X265:
                    return "x265";
                case VENCODER_SVT:
                    return "svt";
                case VENCODER_ORION:
                    return "ux265";
                case VENCODER_CPU_MPEG2:
                    return "m2v";
                case VENCODER_AVS2:
                    return "avs2";
                case VENCODER_MSDK_H264:
                    return "msdk_h264";
                case VENCODER_MSDK_H265:
                    return "msdk_h265";
                case VENCODER_MSDK_MPEG2:
                    return "msdk_mpeg2";
                case VENCODER_CUDA_H264:
                    return "cuda_h264";
                case VENCODER_CUDA_H265:
                    return "cuda_h265";


            }
            throw new CommonException("unknown tpl video encoder :" + type);
        }
    }


    public enum AudioType{
        aac,dolby,mp2,mp3;

        public static AudioType getAudioType(TplAudioEncoder type) throws CommonException {
            switch (type) {
                case AENCODER_AACLC:  //"aaclc"
                case AENCODER_HEAAC:      //"heaac"
                case AENCODER_HEAAC_V2:   //"heaac_v2"
                    return aac;
                case AENCODER_MP2:
                    return mp2;
                case AENCODER_MP3:
                    return mp3;
                case AENCODER_AC3:
                    return dolby;
                case AENCODER_EAC3:
                    return dolby;
            }

            throw new CommonException("unknown tpl video encoder :"+type);
        }
    }
    /**
     * 音频编码格式
     */
    public enum TplAudioEncoder{
        AENCODER_AACLC,  //"aaclc"
        AENCODER_HEAAC,      //"heaac"
        AENCODER_HEAAC_V2,   //"heaac_v2"
        AENCODER_MP2,        //"mpeg2_audio"
        AENCODER_MP3,        //"mp3"
        AENCODER_AC3,        //"ac3"
        AENCODER_EAC3,
        PASSBY;        //"eac3"
        public static TplAudioEncoder getTplAudioEncoder(String encodeType) throws CommonException {
            encodeType = encodeType.toLowerCase(Locale.ENGLISH);
            if (encodeType.contains("passby")){
                return PASSBY;
            }
            switch (encodeType) {
                case "aac":
                case "aaclc":
                case "mpeg4-aac-lc":
                case "aencoder_aaclc":
                    return AENCODER_AACLC;
                case "heaac":
                case "mpeg4-he-aac-lc":
                case "aencoder_heaac":
                    return AENCODER_HEAAC;
                case "heaacv2":
                case "heaac_v2":
                case "mpeg4-he-aac-v2-lc":
                case "aencoder_heaac_v2":
                    return AENCODER_HEAAC_V2;
                case "mp2":
                case "mpeg2_audio":
                case "aencoder_mp2":
                    return AENCODER_MP2;
                case "mp3":
                case "aencoder_mp3":
                    return AENCODER_MP3;
                case "ac3":
                case "aencoder_ac3":
                    return AENCODER_AC3;
                case "eac3":
                case "aencoder_eac3":
                    return AENCODER_EAC3;
            }
            throw new CommonException("unknown tpl audio encoder :"+encodeType);
        }
    }


    /**
     * 音频通道类型
     */
    public enum TplChannelLayout{
        ch_mono,
        ch_stereo,
        ch_3_point_0,
        ch_4_point_0,
        ch_5_point_0,
        ch_5_point_1,
        ch_7_point_1;
        public static TplChannelLayout getTplChannelLayout(String type) {
            switch (type) {
                case "mono":
                case "ch_mono":
                case "VENCODER_X264":
                    return ch_mono;
                case "stereo":
                case "ch_stereo":
                    return ch_stereo;
                case "3.0":
                case "ch_3_point_0":
                    return ch_3_point_0;
                case "4.0":
                case "ch_4_point_0":
                    return ch_4_point_0;
                case "5.0":
                case "ch_5_point_0":
                    return ch_5_point_0;
                case "5.1":
                case "ch_5_point_1":
                    return ch_5_point_1;
                case "7.1":
                case "ch_7_point_1":
                    return ch_7_point_1;
                default:
                    return ch_mono;
            }
        }
    }

    /**
     * 码率控制方式
     */
    public enum TplRateCtrl{
        CBR,
        VBR,
        ABR,
        NEAR_CBR,
        CRF;

        public static TplRateCtrl getTplRateCtrl(String type) throws CommonException {
            switch (type.toUpperCase()) {
                case "CBR":
                    return CBR;
                case "VBR":
                    return VBR;
                case "ABR":
                    return ABR;
                case "NEARCBR":
                case "NEAR_CBR":
                    return NEAR_CBR;
                case "CRF":
                    return CRF;
            }
            throw new CommonException("unknown tpl rate ctrl :"+type);
        }

    }


}
