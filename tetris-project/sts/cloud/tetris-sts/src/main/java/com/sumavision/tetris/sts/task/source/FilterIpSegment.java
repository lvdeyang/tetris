package com.sumavision.tetris.sts.task.source;

import java.io.Serializable;

public class FilterIpSegment implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2858740486180286692L;
	//起始IP
    private String startIp;
    //结束IP
    private String endIp;

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }
}