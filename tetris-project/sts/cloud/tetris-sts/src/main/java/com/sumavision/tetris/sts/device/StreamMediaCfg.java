package com.sumavision.tetris.sts.device;

import org.dom4j.Element;

import java.io.Serializable;
import java.util.Iterator;

public class StreamMediaCfg implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7414405273779200736L;

	private int dashPort;

    private String dashName;

    private int hdsFragTime;

    private int hdsFragsPerSeg;

    private int hdsPort;

    private int hdsSegFileCount;

    private String hdsName;

    private int hlsPort;

    private int httpAsfPort;

    private boolean httpFlvTime;

    private boolean httpFlvTransfer;

    private int httpFlvPort;

    private int httpFlvCacheTime;

    private String httpFlvName;

    private boolean httpTsTransfer;

    private int httpTsPort;

    private int rtmpPort;

    private String rtmpName;

    private int rtspPort;

    private int ssmPort;

    public int getDashPort() {
        return dashPort;
    }

    public void setDashPort(int dashPort) {
        this.dashPort = dashPort;
    }

    public String getDashName() {
        return dashName;
    }

    public void setDashName(String dashName) {
        this.dashName = dashName;
    }

    public int getHdsFragTime() {
        return hdsFragTime;
    }

    public void setHdsFragTime(int hdsFragTime) {
        this.hdsFragTime = hdsFragTime;
    }

    public int getHdsFragsPerSeg() {
        return hdsFragsPerSeg;
    }

    public void setHdsFragsPerSeg(int hdsFragsPerSeg) {
        this.hdsFragsPerSeg = hdsFragsPerSeg;
    }

    public int getHdsPort() {
        return hdsPort;
    }

    public void setHdsPort(int hdsPort) {
        this.hdsPort = hdsPort;
    }

    public int getHdsSegFileCount() {
        return hdsSegFileCount;
    }

    public void setHdsSegFileCount(int hdsSegFileCount) {
        this.hdsSegFileCount = hdsSegFileCount;
    }

    public String getHdsName() {
        return hdsName;
    }

    public void setHdsName(String hdsName) {
        this.hdsName = hdsName;
    }

    public int getHlsPort() {
        return hlsPort;
    }

    public void setHlsPort(int hlsPort) {
        this.hlsPort = hlsPort;
    }

    public int getHttpAsfPort() {
        return httpAsfPort;
    }

    public void setHttpAsfPort(int httpAsfPort) {
        this.httpAsfPort = httpAsfPort;
    }

    public boolean isHttpFlvTime() {
        return httpFlvTime;
    }

    public void setHttpFlvTime(boolean httpFlvTime) {
        this.httpFlvTime = httpFlvTime;
    }

    public boolean isHttpFlvTransfer() {
        return httpFlvTransfer;
    }

    public void setHttpFlvTransfer(boolean httpFlvTransfer) {
        this.httpFlvTransfer = httpFlvTransfer;
    }

    public int getHttpFlvPort() {
        return httpFlvPort;
    }

    public void setHttpFlvPort(int httpFlvPort) {
        this.httpFlvPort = httpFlvPort;
    }

    public int getHttpFlvCacheTime() {
        return httpFlvCacheTime;
    }

    public void setHttpFlvCacheTime(int httpFlvCacheTime) {
        this.httpFlvCacheTime = httpFlvCacheTime;
    }

    public String getHttpFlvName() {
        return httpFlvName;
    }

    public void setHttpFlvName(String httpFlvName) {
        this.httpFlvName = httpFlvName;
    }

    public boolean isHttpTsTransfer() {
        return httpTsTransfer;
    }

    public void setHttpTsTransfer(boolean httpTsTransfer) {
        this.httpTsTransfer = httpTsTransfer;
    }

    public int getHttpTsPort() {
        return httpTsPort;
    }

    public void setHttpTsPort(int httpTsPort) {
        this.httpTsPort = httpTsPort;
    }

    public int getRtmpPort() {
        return rtmpPort;
    }

    public void setRtmpPort(int rtmpPort) {
        this.rtmpPort = rtmpPort;
    }

    public String getRtmpName() {
        return rtmpName;
    }

    public void setRtmpName(String rtmpName) {
        this.rtmpName = rtmpName;
    }

    public int getRtspPort() {
        return rtspPort;
    }

    public void setRtspPort(int rtspPort) {
        this.rtspPort = rtspPort;
    }

    public int getSsmPort() {
        return ssmPort;
    }

    public void setSsmPort(int ssmPort) {
        this.ssmPort = ssmPort;
    }

    public void set(Element element) {
        switch (element.attributeValue("name")) {
            case "dash" :
                this.dashPort = Integer.parseInt(getAttribute(element , "port"));
                this.dashName = getAttribute(element , "live4dash");
                break;
            case "hds" :
                this.hdsName = getAttribute(element , "live4hds");
                this.hdsPort = Integer.parseInt(getAttribute(element , "port"));
                this.hdsFragTime = Integer.parseInt(getAttribute(element , "fragTime"));
                this.hdsFragsPerSeg = Integer.parseInt(getAttribute(element , "fragsPerSeg"));
                this.hdsSegFileCount = Integer.parseInt(getAttribute(element , "segFileCount"));
                break;
            case "hls" :
                this.hlsPort = Integer.parseInt(getAttribute(element , "port"));
                break;
            case "http4asf" :
                this.httpAsfPort = Integer.parseInt(getAttribute(element , "port"));
                break;
            case "http4flv" :
                this.httpFlvName = getAttribute(element , "live4flv");
                this.httpFlvPort = Integer.parseInt(getAttribute(element , "port"));
                this.httpFlvTime = Boolean.parseBoolean(getAttribute(element , "absoluteTime"));
                this.httpFlvTransfer = Boolean.parseBoolean(getAttribute(element , "chunked-transfer"));
                String cacheTimeString = getAttribute(element , "cache-time");
                this.httpFlvCacheTime = cacheTimeString == null ? 2000 : Integer.parseInt(cacheTimeString);
                break;
            case "httptsapp" :
                this.httpTsPort = Integer.parseInt(getAttribute(element , "port"));
                this.httpTsTransfer = Boolean.parseBoolean(getAttribute(element , "chunked-transfer"));
                break;
            case "naturalrtmp" :
                this.rtmpName = getAttribute(element , "live");
                break;
            case "rtmpappselector" :
                this.rtmpPort = Integer.parseInt(getAttribute(element , "port"));
                break;
            case "rtsp" :
                this.rtspPort = Integer.parseInt(getAttribute(element , "port"));
                break;
            case "smoothstream" :
                this.ssmPort = Integer.parseInt(getAttribute(element , "port"));
                break;
        }
    }

    private String getAttribute(Element element , String name) {
        if (element.attributeValue("name").equals(name)) {
            return element.getText();
        }
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            String str = getAttribute(iterator.next() , name);
            if (str != null) return str;
        }
        return null;
    }

}
