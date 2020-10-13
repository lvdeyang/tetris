package com.sumavision.signal.bvc.capacity.bo.input;

import com.sumavision.signal.bvc.capacity.bo.source.MediaSourceBO;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月28日 下午2:43:00
 */
public class InputBO extends InputBaseBO<InputBO>{


    public void setEncapsulateInfo(MediaSourceBO mediaSourceBO){
        switch (mediaSourceBO.getProtocolType()) {
            case UDP_TS:
                CommonTsBO udpBO = new CommonTsBO(mediaSourceBO);
                this.setUdp_ts(udpBO);
                break;
            case RTP_TS:
                CommonTsBO rtpBO = new CommonTsBO(mediaSourceBO);
                this.setRtp_ts(rtpBO);
                break;
            case HTTP_TS:
                this.setHttp_ts(mediaSourceBO);
                break;
            case SRT_TS:
                SrtTsBO srtTsBO = new SrtTsBO(mediaSourceBO);
                this.setSrt_ts(srtTsBO);
                break;
            case HLS:
                this.setHls(mediaSourceBO);
                break;
            case DASH:
                this.setDash(mediaSourceBO);
                break;
            case MSS:
                this.setMss(mediaSourceBO);
                break;
            case RTSP:
                this.setRtsp(mediaSourceBO);
                break;
            case RTMP:
                this.setRtmp(mediaSourceBO);
                break;
            case HTTP_FLV:
                this.setHttp_flv(mediaSourceBO);
                break;
            case SDI:
                this.setSdi(new SdiBO(mediaSourceBO.getUrl()));
                break;
            case ZIXI:
                InputZiXiBO ziXiBO = new InputZiXiBO();
                ziXiBO.setUrl(mediaSourceBO.getUrl());
                ziXiBO.setMode(0);
                this.setZixi(ziXiBO);
                break;
            case FILE:
                InputFileObjectBO fileObjectBO = new InputFileObjectBO();
                fileObjectBO.setUrl(mediaSourceBO.getUrl());
                fileObjectBO.setLoop_count(-1);
                List<InputFileObjectBO> file_array = new ArrayList<>();
                file_array.add(fileObjectBO);
                this.setFile(new InputFileBO(file_array));
                break;
        }
    }


}
