package com.sumavision.tetris.business.common.Util;/**
 * Created by Poemafar on 2021/2/1 16:09
 */

import com.sumavision.tetris.business.common.enumeration.ProtocolType;

/**
 * @ClassName: MediaUtil
 * @Description 媒体工具类，分析拉流推流协议等
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/1 16:09
 */
public class MediaUtil {


    public static boolean bePushFromSourceType(ProtocolType type, String srtMode){
        if (ProtocolType.UDP_TS==type ||ProtocolType.RTP_TS==type || (ProtocolType.SRT_TS==type && srtMode.equals("listener"))){
            return true;
        }
        return false;
    }

}
