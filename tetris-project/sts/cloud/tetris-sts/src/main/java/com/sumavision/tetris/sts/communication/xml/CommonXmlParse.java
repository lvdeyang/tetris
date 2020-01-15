package com.sumavision.tetris.sts.communication.xml;

import com.suma.xianrd.communication.message.XmlManager;
import com.sumavision.tetris.sts.common.CommonConstants.*;
import com.sumavision.tetris.sts.common.CommonUtil;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.sts.task.outputBO.*;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lost on 2017/2/23.
 */
public class CommonXmlParse {

    static Logger logger = LogManager.getLogger(CommonXmlParse.class);

    public static List<NetCardInfoPO> parseNetCardInfo(Element root) {
        List<NetCardInfoPO> netCardInfoPOs = new ArrayList<>();
        Iterator<Element> iterator = root.element("body").elementIterator("netcard");
        while (iterator.hasNext()) {
            NetCardInfoPO netCardInfoPO = new NetCardInfoPO();
            Element netCard = iterator.next();
            netCardInfoPO.setStatus(netCard.attributeValue("connect").equals("true") ? 1 : 0);
            Element ipv4 = netCard.element("ipv4");
            if (ipv4 != null) {
                netCardInfoPO.setIpv4(ipv4.attributeValue("ip"));
                netCardInfoPO.setIpv4Gate(ipv4.attributeValue("gateway"));
                netCardInfoPO.setIpv4Mask(ipv4.attributeValue("mask"));
                netCardInfoPO.setMac(ipv4.attributeValue("mac"));
            }
            Element ipv6 = netCard.element("ipv6");
            if (ipv6 != null) {
                netCardInfoPO.setIpv6(ipv6.attributeValue("ip"));
                netCardInfoPO.setIpv6Gate(ipv6.attributeValue("gateway"));
                netCardInfoPO.setIpv6Mask(ipv6.attributeValue("mask"));
                netCardInfoPO.setMac(ipv4.attributeValue("mac"));
            }
            netCardInfoPOs.add(netCardInfoPO);
        }
        return netCardInfoPOs;
    }



    public static String parsePlatform(Element element) {
        return element.element("body").element("platform").attributeValue("type");
    }

    public static String parseVersion(Element element , FunUnitType unitType) {
        if (unitType.equals(FunUnitType.STREAM_MEDIA)) {
            return getByAttributeName(element.element("body").element("MAP").element("MAP") , "version");
        }
        List<Element> plugins = element.element("body").element("version").elements("plugin");
        for (Element p : plugins) {
            if (p.attributeValue("name").equals("xStream"))
                return p.attributeValue("version");
        }
        return null;
    }



    private static OutputPO parseOutput(Element element) {
        OutputPO outputPO = new OutputPO();
        outputPO.setNodeId(Long.parseLong(element.attributeValue("id")));
        //outputPO.setName(element.attributeValue("name"));
        outputPO.setType(ProtoType.getProtoType(element.attributeValue("type")));
        outputPO.setAudSelect(element.attributeValue("aud-select"));
        outputPO.setVidSelect(element.attributeValue("vid-select"));
        outputPO.setSubSelect(element.attributeValue("sub-select"));
        switch (outputPO.getType()) {
            case TSUDP:
                Element tsUdpNode = element.element("TS-UDP");
                TsUdpBO tsUdp = new TsUdpBO();
                outputPO.setLocalIp(tsUdpNode.attributeValue("localIP"));
                outputPO.setIp(tsUdpNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(tsUdpNode.attributeValue("port")));
                tsUdp.setName(element.attributeValue("name"));
                tsUdp.setProvider(tsUdpNode.attributeValue("provider"));
                tsUdp.setVidSelect(element.attributeValue("vid-select"));
                tsUdp.setAudSelect(element.attributeValue("aud-select"));
                tsUdp.setSubSelect(element.attributeValue("sub-select"));
                tsUdp.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                tsUdp.setProgramNum(XmlManager.attributeValueInt(tsUdpNode , "program-num"));
                tsUdp.setSysRate(XmlManager.attributeValueLong(tsUdpNode , "sys-rate"));
                tsUdp.setPmtPid(XmlManager.attributeValueInt(tsUdpNode , "pmt-pid"));
                tsUdp.setPcrPid(XmlManager.attributeValueInt(tsUdpNode , "pcr-pid"));
                tsUdp.setPcrInt(XmlManager.attributeValueInt(tsUdpNode , "pcr-int"));
                tsUdp.setPatInt(XmlManager.attributeValueInt(tsUdpNode , "pat-int"));
                tsUdp.setPmtInt(XmlManager.attributeValueInt(tsUdpNode , "pmt-int"));
                tsUdp.setSdtInt(XmlManager.attributeValueInt(tsUdpNode , "sdt-int"));
                tsUdp.setRateCtrl(tsUdpNode.attributeValue("rate-ctrl"));
                tsUdp.setAvMode(tsUdpNode.attributeValue("av-mode"));
                tsUdp.setMaxBitrate(XmlManager.attributeValueInt(tsUdpNode , "max-bitrate"));
                tsUdp.setBuf(XmlManager.attributeValueInt(tsUdpNode , "buf"));
                tsUdp.setTsidPid(XmlManager.attributeValueInt(tsUdpNode , "tsid-pid"));
                tsUdp.setAudioDelay(XmlManager.attributeValueInt(tsUdpNode , "audio-delay"));
                tsUdp.setTdtInt(XmlManager.attributeValueInt(tsUdpNode , "tdt-int"));
                tsUdp.setTotInt(XmlManager.attributeValueInt(tsUdpNode , "tot-int"));
                if (tsUdp.getAudSelect() != null && !tsUdp.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(tsUdpNode , "aud" + i + "-pid"));
                    }
                    tsUdp.setAudPids(CommonUtil.listToString(audPids));
                }
                if (tsUdp.getVidSelect() != null && !tsUdp.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(tsUdpNode , "vid" + i + "-pid"));
                    }
                    tsUdp.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(tsUdp);
                break;
            case ASI:
//                Element asiNode = element.element("ASI_SEND_10K744");
//                AsiBO asiBO = new AsiBO();
//                outputPO.setOutputBO();
                break;
            case TSRTP:
            	Element tsRtpNode = element.element("TS-RTP");
                TsRtpBO tsRtp = new TsRtpBO();
                outputPO.setLocalIp(tsRtpNode.attributeValue("localIP"));
                outputPO.setIp(tsRtpNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(tsRtpNode.attributeValue("port")));
                tsRtp.setName(tsRtpNode.attributeValue("name"));
                tsRtp.setProvider(tsRtpNode.attributeValue("provider"));
                tsRtp.setVidSelect(element.attributeValue("vid-select"));
                tsRtp.setAudSelect(element.attributeValue("aud-select"));
                tsRtp.setSubSelect(element.attributeValue("sub-select"));
                tsRtp.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                tsRtp.setProgramNum(XmlManager.attributeValueInt(tsRtpNode , "program-num"));
                tsRtp.setSysRate(XmlManager.attributeValueLong(tsRtpNode , "sys-rate"));
                tsRtp.setPmtPid(XmlManager.attributeValueInt(tsRtpNode , "pmt-pid"));
                tsRtp.setPcrPid(XmlManager.attributeValueInt(tsRtpNode , "pcr-pid"));
                tsRtp.setPcrInt(XmlManager.attributeValueInt(tsRtpNode , "pcr-int"));
                tsRtp.setPatInt(XmlManager.attributeValueInt(tsRtpNode , "pat-int"));
                tsRtp.setPmtInt(XmlManager.attributeValueInt(tsRtpNode , "pmt-int"));
                tsRtp.setSdtInt(XmlManager.attributeValueInt(tsRtpNode , "sdt-int"));
                tsRtp.setRateCtrl(tsRtpNode.attributeValue("rate-ctrl"));
                tsRtp.setMaxBitrate(XmlManager.attributeValueInt(tsRtpNode , "max-bitrate"));
                tsRtp.setBuf(XmlManager.attributeValueInt(tsRtpNode , "buf"));
                tsRtp.setTsidPid(XmlManager.attributeValueInt(tsRtpNode , "tsid-pid"));
                if (tsRtp.getAudSelect() != null && !tsRtp.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(tsRtpNode , "aud" + i + "-pid"));
                    }
                    tsRtp.setAudPids(CommonUtil.listToString(audPids));
                }
                if (tsRtp.getVidSelect() != null && !tsRtp.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(tsRtpNode , "vid" + i + "-pid"));
                    }
                    tsRtp.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(tsRtp);
            	break;
            case HTTPTS:
            	Element httpTsNode = element.element("TS-HTTP");
                HttpTsBO httpTs = new HttpTsBO();
                outputPO.setLocalIp(httpTsNode.attributeValue("localIP"));
                outputPO.setIp(httpTsNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(httpTsNode.attributeValue("port")));
                outputPO.setPubName(httpTsNode.attributeValue("pub-name"));
                httpTs.setName(element.attributeValue("name"));
                httpTs.setProvider(httpTsNode.attributeValue("provider"));
                httpTs.setVidSelect(element.attributeValue("vid-select"));
                httpTs.setAudSelect(element.attributeValue("aud-select"));
                httpTs.setSubSelect(element.attributeValue("sub-select"));
                httpTs.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                httpTs.setProgramNum(XmlManager.attributeValueInt(httpTsNode , "program-num"));
                httpTs.setSysRate(XmlManager.attributeValueLong(httpTsNode , "sys-rate"));
                httpTs.setPmtPid(XmlManager.attributeValueInt(httpTsNode , "pmt-pid"));
                httpTs.setPcrPid(XmlManager.attributeValueInt(httpTsNode , "pcr-pid"));
                httpTs.setPcrInt(XmlManager.attributeValueInt(httpTsNode , "pcr-int"));
                httpTs.setPatInt(XmlManager.attributeValueInt(httpTsNode , "pat-int"));
                httpTs.setPmtInt(XmlManager.attributeValueInt(httpTsNode , "pmt-int"));
                httpTs.setSdtInt(XmlManager.attributeValueInt(httpTsNode , "sdt-int"));
                httpTs.setRateCtrl(httpTsNode.attributeValue("rate-ctrl"));
                httpTs.setAvMode(httpTsNode.attributeValue("av-mode"));
                httpTs.setMaxBitrate(XmlManager.attributeValueInt(httpTsNode , "max-bitrate"));
                httpTs.setBuf(XmlManager.attributeValueInt(httpTsNode , "buf"));
                httpTs.setTsidPid(XmlManager.attributeValueInt(httpTsNode , "tsid-pid"));
                if (httpTs.getAudSelect() != null && !httpTs.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(httpTsNode , "aud" + i + "-pid"));
                    }
                    httpTs.setAudPids(CommonUtil.listToString(audPids));
                }
                if (httpTs.getVidSelect() != null && !httpTs.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(httpTsNode , "vid" + i + "-pid"));
                    }
                    httpTs.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(httpTs);
            	break;
            case RTSPRTP:
            	Element rtspNode = element.element("RTSP");
            	RtspBO rtsp = new RtspBO();
            	outputPO.setLocalIp(rtspNode.attributeValue("localIP"));
                outputPO.setIp(rtspNode.attributeValue("streamserver-ip"));
                outputPO.setPort(Integer.parseInt(rtspNode.attributeValue("streamserver-port")));
                outputPO.setPubName(rtspNode.attributeValue("pub-name"));
                rtsp.setVidSelect(element.attributeValue("vid-select"));
                rtsp.setAudSelect(element.attributeValue("aud-select"));
                rtsp.setAutoBitrate(XmlManager.attributeValueInt(element, "auto-bitrate"));
                rtsp.setVidBitrates(element.attributeValue("vid-bitrate"));
                rtsp.setAudBitrates(element.attributeValue("aud-bitrate"));
                rtsp.setMTU(XmlManager.attributeValueInt(rtspNode, "MTU"));
                rtsp.setVidDestPort(XmlManager.attributeValueInt(rtspNode, "vid-dest-port"));
                rtsp.setAudDestPort(XmlManager.attributeValueInt(rtspNode, "aud-dest-port"));
                rtsp.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                outputPO.setOutputBO(rtsp);
                break;
            case TSSRT:
            	Element tsSrtNode = element.element("TS-SRT");
            	TsSrtBO tsSrt = new TsSrtBO();
            	outputPO.setLocalIp(tsSrtNode.attributeValue("localIP"));
                outputPO.setIp(tsSrtNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(tsSrtNode.attributeValue("port")));
                tsSrt.setName(element.attributeValue("name"));
                tsSrt.setProvider(tsSrtNode.attributeValue("provider"));
                tsSrt.setVidSelect(element.attributeValue("vid-select"));
                tsSrt.setAudSelect(element.attributeValue("aud-select"));
                tsSrt.setSubSelect(element.attributeValue("sub-select"));
                tsSrt.setModeSelect(tsSrtNode.attributeValue("srt-mode"));
                tsSrt.setEncryption(tsSrtNode.attributeValue("encryption"));
                tsSrt.setLatency(XmlManager.attributeValueInt(tsSrtNode, "latency"));
                tsSrt.setRwTimeout(XmlManager.attributeValueInt(tsSrtNode, "rw-timeout"));
                tsSrt.setMaxBw(XmlManager.attributeValueInt(tsSrtNode, "max-bw"));
                tsSrt.setPassphrase(tsSrtNode.attributeValue("passphrase"));
                tsSrt.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                tsSrt.setProgramNum(XmlManager.attributeValueInt(tsSrtNode , "program-num"));
                tsSrt.setSysRate(XmlManager.attributeValueLong(tsSrtNode , "sys-rate"));
                tsSrt.setPmtPid(XmlManager.attributeValueInt(tsSrtNode , "pmt-pid"));
                tsSrt.setPcrPid(XmlManager.attributeValueInt(tsSrtNode , "pcr-pid"));
                tsSrt.setPcrInt(XmlManager.attributeValueInt(tsSrtNode , "pcr-int"));
                tsSrt.setPatInt(XmlManager.attributeValueInt(tsSrtNode , "pat-int"));
                tsSrt.setPmtInt(XmlManager.attributeValueInt(tsSrtNode , "pmt-int"));
                tsSrt.setSdtInt(XmlManager.attributeValueInt(tsSrtNode , "sdt-int"));
                tsSrt.setRateCtrl(tsSrtNode.attributeValue("rate-ctrl"));
                tsSrt.setAvMode(tsSrtNode.attributeValue("av-mode"));
                tsSrt.setMaxBitrate(XmlManager.attributeValueInt(tsSrtNode , "max-bitrate"));
                tsSrt.setBuf(XmlManager.attributeValueInt(tsSrtNode , "buf"));
                tsSrt.setTsidPid(XmlManager.attributeValueInt(tsSrtNode , "tsid-pid"));
                if(tsSrtNode.attributeValue("audio-delay").equals("0")){
                	tsSrt.setAudioDelay(null);
                }else{
                	tsSrt.setAudioDelay(XmlManager.attributeValueInt(tsSrtNode , "audio-delay"));
                }
                tsSrt.setTdtInt(XmlManager.attributeValueInt(tsSrtNode , "tdt-int"));
                tsSrt.setTotInt(XmlManager.attributeValueInt(tsSrtNode , "tot-int"));
                if (tsSrt.getAudSelect() != null && !tsSrt.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(tsSrtNode , "aud" + i + "-pid"));
                    }
                    tsSrt.setAudPids(CommonUtil.listToString(audPids));
                }
                if (tsSrt.getVidSelect() != null && !tsSrt.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(tsSrtNode , "vid" + i + "-pid"));
                    }
                    tsSrt.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(tsSrt);
            	break;
            case RTMPFLV:
            	Element rtmpFlvNode = element.element("RTMP-FLV");
            	RtmpFlvBO rtmpFlv = new RtmpFlvBO();
            	outputPO.setLocalIp(rtmpFlvNode.attributeValue("localIP"));
                outputPO.setIp(rtmpFlvNode.attributeValue("pub-ip"));
                outputPO.setPort(Integer.parseInt(rtmpFlvNode.attributeValue("streamserver-port")));
                outputPO.setPubName(rtmpFlvNode.attributeValue("pub-name"));
                if(element.attributeValue("vid-select").equals("-1")){
                	rtmpFlv.setVidSelect(element.attributeValue("0"));
                }else{
                	rtmpFlv.setVidSelect(element.attributeValue("vid-select"));
                }
                rtmpFlv.setAudSelect(element.attributeValue("aud-select"));
                rtmpFlv.setAutoBitrate(XmlManager.attributeValueInt(element, "auto-bitrate"));
                rtmpFlv.setVidBitrates(getBitrateSReverse(element.attributeValue("vid-bitrate")));
                rtmpFlv.setAudBitrates(getBitrateSReverse(element.attributeValue("aud-bitrate")));
                rtmpFlv.setResolutions(element.attributeValue("resolutions"));
                rtmpFlv.setPubPoint(rtmpFlvNode.attributeValue("pub-point"));
                rtmpFlv.setPubUser(rtmpFlvNode.attributeValue("pub-user"));
                rtmpFlv.setPubPasswd(rtmpFlvNode.attributeValue("pub-passwd"));
                rtmpFlv.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                outputPO.setOutputBO(rtmpFlv);
            	break;
            case HTTPFLV:
            	Element httpFlvNode = element.element("HTTP-FLV");
            	HttpFlvBO httpFlv = new HttpFlvBO();
            	outputPO.setLocalIp(httpFlvNode.attributeValue("localIP"));
                outputPO.setIp(httpFlvNode.attributeValue("pub-ip"));
                outputPO.setPort(Integer.parseInt(httpFlvNode.attributeValue("streamserver-port")));
                outputPO.setPubName(httpFlvNode.attributeValue("pub-name"));
                httpFlv.setVidSelect(element.attributeValue("vid-select"));
                httpFlv.setAudSelect(element.attributeValue("aud-select"));
                httpFlv.setAutoBitrate(XmlManager.attributeValueInt(element, "auto-bitrate"));
                httpFlv.setVidBitrates(element.attributeValue("vid-bitrate"));
                httpFlv.setAudBitrates(element.attributeValue("aud-bitrate"));
                httpFlv.setResolutions(element.attributeValue("resolutions"));
                httpFlv.setPubPoint(httpFlvNode.attributeValue("pub-point"));
                httpFlv.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                outputPO.setOutputBO(httpFlv);
                break;
            case MSS:
            	Element mssNode = element.element("SSM");
            	MssBO mss = new MssBO();
            	outputPO.setLocalIp(mssNode.attributeValue("localIP"));
                outputPO.setIp(mssNode.attributeValue("pub-ip"));
                outputPO.setPort(Integer.parseInt(mssNode.attributeValue("streamserver-port")));
                outputPO.setPubName(mssNode.attributeValue("pub-name"));
                mss.setVidSelect(element.attributeValue("vid-select"));
                mss.setAudSelect(element.attributeValue("aud-select"));
                mss.setAutoBitrate(XmlManager.attributeValueInt(element, "auto-bitrate"));
                mss.setVidBitrates(element.attributeValue("vid-bitrate"));
                mss.setAudBitrates(element.attributeValue("aud-bitrate"));
                mss.setSrvName(mssNode.attributeValue("srv-name"));
                mss.setSrvPwd(mssNode.attributeValue("srv-pwd"));
                mss.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                outputPO.setOutputBO(mss);
                break;
            case HLS:
            	Element hlsNode = element.element("HLS");
            	HlsBO hls = new HlsBO();
                outputPO.setLocalIp(hlsNode.attributeValue("localIP"));
                outputPO.setPubName(hlsNode.attributeValue("pub-name"));
                hls.setResolutions(element.attributeValue("resolutions"));
                hls.setProvider(hlsNode.attributeValue("provider"));
                hls.setVidSelect(element.attributeValue("vid-select"));
                hls.setAudSelect(element.attributeValue("aud-select"));
                hls.setSubSelect(element.attributeValue("sub-select"));
                hls.setVidBitrates(getBitrateSReverse(element.attributeValue("vid-bitrate")));
                hls.setAudBitrates(getBitrateSReverse(element.attributeValue("aud-bitrate")));
                hls.setSubdirFlag(hlsNode.attributeValue("subdir-flag"));
                hls.setM3u8Name(hlsNode.attributeValue("primary-m3u8"));
                hls.setGroupCount(XmlManager.attributeValueInt(hlsNode, "group-count"));
                hls.setGroupFileCount(XmlManager.attributeValueInt(hlsNode, "file-count"));
                hls.setChunkSpan(XmlManager.attributeValueInt(hlsNode, "chunk-span"));
                hls.setProvider(hlsNode.attributeValue("provider"));
                hls.setProgramNum(XmlManager.attributeValueInt(hlsNode , "program-num"));
                hls.setEncrypt(hlsNode.attributeValue("enc-mode"));
                hls.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                Element hlsFtp = hlsNode.element("ftp");
                if (hlsFtp!=null) {
                    hls.setUrl(hlsFtp.attributeValue("ftp-url"));
                }
                List<Element> mediagroups = hlsNode.elements("mediagroup");
                String mgStr = "[";
                int index = 0;
                for(Element mediagroup : mediagroups){
                	if(index != 0){
                		mgStr = mgStr +",";
                	}
                	index++;
                	mgStr = mgStr + "{\"subtitles\":[],";
                	String bandwidth = mediagroup.attributeValue("bandwidth");
                	if(bandwidth == null){}
                	else if(bandwidth.equals(""))
                		mgStr = mgStr + "\"bandwidth\":\"\",";
                	else
                		mgStr = mgStr +"\"bandwidth\":"+ bandwidth +",";
                	Element video = mediagroup.element("video");
                	if(video != null)
                		mgStr = mgStr + "\"video\":" + video.attributeValue("idx") +",";
                	List<Element> audioes = mediagroup.elements("audio");
                	mgStr = mgStr + "\"audio\":[";
                	int indexTemp = 0;
                	for(Element audio : audioes){
                		if(indexTemp != 0){
                			mgStr = mgStr +",";
                		}
                		mgStr = mgStr + audio.attributeValue("idx");
                		indexTemp++;
                	}
                	mgStr = mgStr +"]}";	
                }
                mgStr = mgStr +"]";
                hls.setMediaGroup(mgStr);
                outputPO.setOutputBO(hls);
            	break;
            case HDS:
            	Element hdsNode = element.element("HDS-FLV");
            	HdsBO hds = new HdsBO();
            	outputPO.setLocalIp(hdsNode.attributeValue("localIP"));
                outputPO.setIp(hdsNode.attributeValue("pub-ip"));
                outputPO.setPort(Integer.parseInt(hdsNode.attributeValue("streamserver-port")));
                outputPO.setPubName(hdsNode.attributeValue("pub-name"));
                hds.setVidSelect(element.attributeValue("vid-select"));
                hds.setAudSelect(element.attributeValue("aud-select"));
                hds.setAutoBitrate(XmlManager.attributeValueInt(element, "auto-bitrate"));
                hds.setVidBitrates(getBitrateSReverse(element.attributeValue("vid-bitrate")));
                hds.setAudBitrates(getBitrateSReverse(element.attributeValue("aud-bitrate")));
                hds.setPubPoint(hdsNode.attributeValue("pub-point"));
                hds.setPubUser(hdsNode.attributeValue("pub-user"));
                hds.setPubPasswd(hdsNode.attributeValue("pub-passwd"));
                hds.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
                outputPO.setOutputBO(hds);
            	break;
            case DASH:
            	Element dashNode = element.element("DASH");
            	DashBO dash = new DashBO();
            	outputPO.setPubName(dashNode.attributeValue("pub-name"));
            	dash.setVidSelect(element.attributeValue("vid-select"));
            	dash.setAudSelect(element.attributeValue("aud-select"));
            	dash.setSubSelect(element.attributeValue("sub-select"));
            	dash.setVidBitrates(getBitrateSReverse(element.attributeValue("vid-bitrate")));
            	dash.setAudBitrates(getBitrateSReverse(element.attributeValue("aud-bitrate")));
            	dash.setResolutions(element.attributeValue("resolutions"));
            	dash.setUrl(dashNode.attributeValue("localIP"));
            	dash.setSubdirFlag(dashNode.attributeValue("subdir-flag"));
            	dash.setM3u8Name(dashNode.attributeValue("mpd-name"));
            	dash.setGroupCount(XmlManager.attributeValueInt(dashNode, "group-count"));
            	dash.setGroupFileCount(XmlManager.attributeValueInt(dashNode, "file-count"));
            	dash.setChunkSpan(XmlManager.attributeValueInt(dashNode, "chunk-span"));
            	dash.setProvider(dashNode.attributeValue("provider"));
            	dash.setProgramNum(XmlManager.attributeValueInt(dashNode , "program-num"));
            	dash.setDstType(XmlManager.attributeValueInt(element, "dst-type"));
            	if(element.attributeValue("dst-type").equals("1")){
                	Element ftp = dashNode.element("ftp");
                	dash.setUrl(ftp.attributeValue("ftp-url"));
                }
            	List<Element> dashMediagroups = dashNode.elements("mediagroup");
                String dashMgStr = "[";
                int dashIndex = 0;
                for(Element dashMediagroup : dashMediagroups){
                	if(dashIndex != 0){
                		dashMgStr = dashMgStr +",";
                	}
                	dashIndex++;
                	dashMgStr = dashMgStr + "{\"subtitles\":[],";
                	String bandwidth = dashMediagroup.attributeValue("bandwidth");
                	if(bandwidth == null){}
                	else if(bandwidth.equals(""))
                		dashMgStr = dashMgStr + "\"bandwidth\":\"\",";
                	else
                		dashMgStr = dashMgStr +"\"bandwidth\":"+ bandwidth +",";
                	Element video = dashMediagroup.element("video");
                	if(video != null)
                		dashMgStr = dashMgStr + "\"video\":" + video.attributeValue("idx") +",";
                	List<Element> audioes = dashMediagroup.elements("audio");
                	dashMgStr = dashMgStr + "\"audio\":[";
                	int dashIndexTemp = 0;
                	for(Element audio : audioes){
                		if(dashIndexTemp != 0){
                			dashMgStr = dashMgStr +",";
                		}
                		dashMgStr = dashMgStr + audio.attributeValue("idx");
                		dashIndexTemp++;
                	}
                	dashMgStr = dashMgStr +"]}";
                }
                dashMgStr = dashMgStr +"]";
                dash.setMediaGroup(dashMgStr);
            	outputPO.setOutputBO(dash);
            	break;
            case TSUDPPASSBY:
            	Element tsUdpPassByNode = element.element("TS-UDP-PASSBY");
                TsUdpBO tsUdpPassBy = new TsUdpBO();
                outputPO.setLocalIp(tsUdpPassByNode.attributeValue("localIP"));
                outputPO.setIp(tsUdpPassByNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(tsUdpPassByNode.attributeValue("port")));
                tsUdpPassBy.setName(tsUdpPassByNode.attributeValue("name"));
                tsUdpPassBy.setVidSelect(element.attributeValue("vid-select"));
                tsUdpPassBy.setAudSelect(element.attributeValue("aud-select"));
                tsUdpPassBy.setProvider(tsUdpPassByNode.attributeValue("provider"));
                if (tsUdpPassBy.getAudSelect() != null && !tsUdpPassBy.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(tsUdpPassByNode , "aud" + i + "-pid"));
                    }
                    tsUdpPassBy.setAudPids(CommonUtil.listToString(audPids));
                }
                if (tsUdpPassBy.getVidSelect() != null && !tsUdpPassBy.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(tsUdpPassByNode , "vid" + i + "-pid"));
                    }
                    tsUdpPassBy.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(tsUdpPassBy);
                break;
            case TSSRTPASSBY:
            	Element tsSrtPassByNode = element.element("TS-SRT-PASSBY");
            	TsSrtBO tsSrtPassBy = new TsSrtBO();
            	outputPO.setLocalIp(tsSrtPassByNode.attributeValue("localIP"));
                outputPO.setIp(tsSrtPassByNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(tsSrtPassByNode.attributeValue("port")));
                tsSrtPassBy.setName(element.attributeValue("name"));
                tsSrtPassBy.setVidSelect(element.attributeValue("vid-select"));
                tsSrtPassBy.setAudSelect(element.attributeValue("aud-select"));
                tsSrtPassBy.setProvider(tsSrtPassByNode.attributeValue("provider"));
                tsSrtPassBy.setModeSelect(tsSrtPassByNode.attributeValue("mode"));
                if (tsSrtPassBy.getAudSelect() != null && !tsSrtPassBy.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(tsSrtPassByNode , "aud" + i + "-pid"));
                    }
                    tsSrtPassBy.setAudPids(CommonUtil.listToString(audPids));
                }
                if (tsSrtPassBy.getVidSelect() != null && !tsSrtPassBy.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(tsSrtPassByNode , "vid" + i + "-pid"));
                    }
                    tsSrtPassBy.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(tsSrtPassBy);
            	break;
            case HTTPTSPASSBY:
            	Element tsHttpPassByNode = element.element("TS-HTTP-PASSBY");
            	HttpTsBO tsHttpPass = new HttpTsBO();
                outputPO.setLocalIp(tsHttpPassByNode.attributeValue("localIP"));
                outputPO.setIp(tsHttpPassByNode.attributeValue("ip"));
                outputPO.setPort(Integer.parseInt(tsHttpPassByNode.attributeValue("port")));
                tsHttpPass.setName(tsHttpPassByNode.attributeValue("name"));
                tsHttpPass.setVidSelect(element.attributeValue("vid-select"));
                tsHttpPass.setAudSelect(element.attributeValue("aud-select"));
                tsHttpPass.setProvider(tsHttpPassByNode.attributeValue("provider"));
                if (tsHttpPass.getAudSelect() != null && !tsHttpPass.getAudSelect().isEmpty()) {
                    List<Integer> audPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getAudSelect().split(",").length; i++) {
                        audPids.add(XmlManager.attributeValueInt(tsHttpPassByNode , "aud" + i + "-pid"));
                    }
                    tsHttpPass.setAudPids(CommonUtil.listToString(audPids));
                }
                if (tsHttpPass.getVidSelect() != null && !tsHttpPass.getVidSelect().isEmpty()) {
                    List<Integer> vidPids = new ArrayList<>();
                    for (int i = 1; i <= outputPO.getVidSelect().split(",").length; i++) {
                        vidPids.add(XmlManager.attributeValueInt(tsHttpPassByNode , "vid" + i + "-pid"));
                    }
                    tsHttpPass.setVidPids(CommonUtil.listToString(vidPids));
                }
                outputPO.setOutputBO(tsHttpPass);
            	break;
            default:
                break;
        }

        return outputPO;
    }
    
    public static String getBitrateSReverse(String bitrateS) {
        if (bitrateS == null){
            return bitrateS;
        }
		StringBuilder sb = new StringBuilder();
		String[] str = bitrateS.split(",");
		for (int i = 0; i < str.length; i++) {
			if (sb.length() != 0)
				sb.append(",");
			if (!str[i].equals("")) 
				sb.append(Integer.parseInt(str[i]) / 1000);
		}
		return sb.toString();
	}


    
    public static List<String> parseRunningStatusToStringList(Element element) {
    	List<String> ipAndPorts = new ArrayList<String>(); 
    	List<Element> clientOrEncoderIps = getByElementName(element, "STR", new ArrayList<Element>());
    	clientOrEncoderIps.stream().forEach(clientOrEncoderIp -> {
    		if (clientOrEncoderIp.attributeValue("name").equals("client-ip")) {
    			ipAndPorts.add(getByAttributeName(clientOrEncoderIp , "client-ip") + ":" + getByAttributeName(clientOrEncoderIp.getParent().element("UINT32") , "client-port"));
			}
        });
		return ipAndPorts;
    }

    public static String getByAttributeName(Element element , String name) {
        if (element.attributeValue("name").equals(name)) {
            return element.getText();
        }
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            String str = getByAttributeName(iterator.next() , name);
            if (str != null) return str;
        }
        return null;
    }
    
    //获取element下所有指定节点名的节点
    public static List<Element> getByElementName(Element element,String name,List<Element> elements){
    	Iterator<Element> it = element.elementIterator();
    	while (it.hasNext()) {
			Element el= it.next();
			if (el.getName().equals(name)) {
				elements.add(el);
			}else{
				getByElementName(el, name, elements);
			}
		}
    	return elements;
    }
    


}
