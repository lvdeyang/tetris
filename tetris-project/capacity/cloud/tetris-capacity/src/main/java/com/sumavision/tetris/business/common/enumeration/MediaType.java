package com.sumavision.tetris.business.common.enumeration;/**
 * Created by Poemafar on 2020/11/5 17:27
 */

import com.sumavision.tetris.capacity.constant.EncodeConstant;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * @ClassName: MediaType
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/5 17:27
 */
public enum MediaType {
    VIDEO,
    AUDIO,
    SUBTITLE;

    public static MediaType getMediaType(String type) throws BaseException {
        if (type.toUpperCase().contains("VIDEO")){
            return VIDEO;
        }
        if (type.toUpperCase().contains("AUDIO")){
            return AUDIO;
        }
        if (type.toUpperCase().contains("SUBTITLE")){
            return SUBTITLE;
        }
        switch (type.toLowerCase()){
            case "h264":
            case "h265":
            case "mpeg2":
            case "avs":
            case "x264":
            case "x265":
            case "hevc":
            case "svt":
            case "ux265":
            case "orion":
            case "m2v":
                return VIDEO;
            case "aac":
            case "aaclc":
            case "mpeg4-aac-lc":
            case "heaac":
            case "mpeg4-he-aac-lc":
            case "heaac_v2":
            case "mpeg4-he-aac-v2-lc":
            case "mp2":
            case "mpeg2_audio":
            case "mp3":
            case "ac3":
            case "eac3":
                return AUDIO;
        }
        throw new BaseException(StatusCode.ERROR,"unkown media type:"+type);
    }
}
