package com.sumavision.tetris.sts.device.auth;


import com.sumavision.tetris.sts.device.Authorization;

public class StreamMediaAuth implements Authorization {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3765161280252439798L;

	private boolean hds;

    private boolean hls;

    private boolean httpAsf;

    private boolean httpFlv;

    private boolean httpTs;

    private boolean mpegDash;

    private boolean rds;

    private boolean rtmp;

    private boolean rtsp;

    private boolean ssm;

    private int clientNum;

    private int programNum;

    public boolean isHds() {
        return hds;
    }

    public void setHds(boolean hds) {
        this.hds = hds;
    }

    public boolean isHls() {
        return hls;
    }

    public void setHls(boolean hls) {
        this.hls = hls;
    }

    public boolean isHttpAsf() {
        return httpAsf;
    }

    public void setHttpAsf(boolean httpAsf) {
        this.httpAsf = httpAsf;
    }

    public boolean isHttpFlv() {
        return httpFlv;
    }

    public void setHttpFlv(boolean httpFlv) {
        this.httpFlv = httpFlv;
    }

    public boolean isHttpTs() {
        return httpTs;
    }

    public void setHttpTs(boolean httpTs) {
        this.httpTs = httpTs;
    }

    public boolean isMpegDash() {
        return mpegDash;
    }

    public void setMpegDash(boolean mpegDash) {
        this.mpegDash = mpegDash;
    }

    public boolean isRds() {
        return rds;
    }

    public void setRds(boolean rds) {
        this.rds = rds;
    }

    public boolean isRtmp() {
        return rtmp;
    }

    public void setRtmp(boolean rtmp) {
        this.rtmp = rtmp;
    }

    public boolean isRtsp() {
        return rtsp;
    }

    public void setRtsp(boolean rtsp) {
        this.rtsp = rtsp;
    }

    public boolean isSsm() {
        return ssm;
    }

    public void setSsm(boolean ssm) {
        this.ssm = ssm;
    }

    public int getClientNum() {
        return clientNum;
    }

    public void setClientNum(int clientNum) {
        this.clientNum = clientNum;
    }

    public int getProgramNum() {
        return programNum;
    }

    public void setProgramNum(int programNum) {
        this.programNum = programNum;
    }

    public void set(String name , String value) {
        switch (name) {
            case "HDS" :
                this.hds = Boolean.parseBoolean(value); break;
            case "HLS" :
                this.hls = Boolean.parseBoolean(value); break;
            case "HTTP-ASF" :
                this.httpAsf = Boolean.parseBoolean(value); break;
            case "HTTP-FLV" :
                this.httpFlv = Boolean.parseBoolean(value); break;
            case "HTTP-TS" :
                this.httpTs = Boolean.parseBoolean(value); break;
            case "MPEG-DASH" :
                this.mpegDash = Boolean.parseBoolean(value); break;
            case "RDS" :
                this.rds = Boolean.parseBoolean(value); break;
            case "RTMP" :
                this.rtmp = Boolean.parseBoolean(value); break;
            case "RTSP" :
                this.rtsp = Boolean.parseBoolean(value); break;
            case "Smooth Stream" :
                this.ssm = Boolean.parseBoolean(value); break;
            case "client-count" :
                this.clientNum = Integer.parseInt(value); break;
            case "program-count" :
                this.programNum = Integer.parseInt(value);
        }
    }

}
