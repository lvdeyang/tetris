package com.sumavision.signal.bvc.director.bo;

public class RequestSourceBo {
    private Integer index;
    private String type;
    private String url;
    private String local_ip;
    private String srt_mode;
    private Integer latency;
    private Integer select_index;
    private Integer select_program_number;
    private String program_array;

    public Integer getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getLocal_ip() {
        return local_ip;
    }

    public String getSrt_mode() {
        return srt_mode;
    }

    public Integer getLatency() {
        return latency;
    }

    public Integer getSelect_index() {
        return select_index;
    }

    public Integer getSelect_program_number() {
        return select_program_number;
    }

    public String getProgram_array() {
        return program_array;
    }

    public RequestSourceBo setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public RequestSourceBo setType(String type) {
        this.type = type;
        return this;
    }

    public RequestSourceBo setUrl(String url) {
        this.url = url;
        return this;
    }

    public RequestSourceBo setLocal_ip(String local_ip) {
        this.local_ip = local_ip;
        return this;
    }

    public RequestSourceBo setSrt_mode(String srt_mode) {
        this.srt_mode = srt_mode;
        return this;
    }

    public RequestSourceBo setLatency(Integer latency) {
        this.latency = latency;
        return this;
    }

    public RequestSourceBo setSelect_index(Integer select_index) {
        this.select_index = select_index;
        return this;
    }

    public RequestSourceBo setSelect_program_number(Integer select_program_number) {
        this.select_program_number = select_program_number;
        return this;
    }

    public RequestSourceBo setProgram_array(String program_array) {
        this.program_array = program_array;
        return this;
    }
}
