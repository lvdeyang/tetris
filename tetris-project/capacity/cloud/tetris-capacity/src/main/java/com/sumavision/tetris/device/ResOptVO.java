package com.sumavision.tetris.device;

import java.io.Serializable;

public class ResOptVO implements Serializable {

    private static final long serialVersionUID = -3216155229056548719L;

    private Boolean beSuccess;

    private String outline;

    private String reason;

    private String detail;

    private String suggest;

    /**
     * 提示
     */
    private String tooltip;

    public ResOptVO() {
    }

    public ResOptVO(Boolean beSuccess) {
        this.beSuccess = beSuccess;
    }

    public Boolean getBeSuccess() {
        return beSuccess;
    }

    public void setBeSuccess(Boolean beSuccess) {
        this.beSuccess = beSuccess;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
