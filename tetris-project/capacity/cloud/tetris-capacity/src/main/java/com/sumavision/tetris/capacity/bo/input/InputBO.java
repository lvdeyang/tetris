package com.sumavision.tetris.capacity.bo.input;

import com.sumavision.tetris.business.common.bo.MediaSourceBO;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;

import java.util.ArrayList;
import java.util.List;

import static com.sumavision.tetris.business.common.enumeration.ProtocolType.UDP_TS;

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
                CommonTsBO udpBO = new CommonTsBO(mediaSourceBO.getUrl(),mediaSourceBO.getLocalIp());
                this.setUdp_ts(udpBO);
                break;
            case RTP_TS:
                CommonTsBO rtpBO = new CommonTsBO(mediaSourceBO.getUrl(),mediaSourceBO.getLocalIp());
                this.setRtp_ts(rtpBO);
                break;
            case HTTP_TS:
                SourceUrlBO httpts = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setHttp_ts(httpts);
                break;
            case SRT_TS:
                SrtTsBO srtTsBO = new SrtTsBO(mediaSourceBO);//todo 暂不支持，参数不够
                this.setSrt_ts(srtTsBO);
                break;
            case HLS:
                SourceUrlBO hls = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setHls(hls);
                break;
            case DASH:
                SourceUrlBO dash = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setDash(dash);
                break;
            case MSS:
                SourceUrlBO mss = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setMss(mss);
                break;
            case RTSP:
                SourceUrlBO rtsp = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setRtsp(rtsp);
                break;
            case RTMP:
                SourceUrlBO rtmp = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setRtmp(rtmp);
                break;
            case HTTP_FLV:
                SourceUrlBO httpflv = new SourceUrlBO().setType(mediaSourceBO.getProtocolType().name().toLowerCase()).setUrl(mediaSourceBO.getUrl());
                this.setHttp_flv(httpflv);
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
