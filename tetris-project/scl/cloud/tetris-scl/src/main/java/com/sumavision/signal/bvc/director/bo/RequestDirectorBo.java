package com.sumavision.signal.bvc.director.bo;

import java.util.List;

public class RequestDirectorBo {
    private String task_ip;
    private String template;
    private List<RequestSourceBo> map_sources;
    private List<RequestTaskBo> map_tasks;
    private List<RequestTaskBo> map_outputs;

    public String getTask_ip() {
        return task_ip;
    }

    public void setTask_ip(String task_ip) {
        this.task_ip = task_ip;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<RequestSourceBo> getMap_sources() {
        return map_sources;
    }

    public void setMap_sources(List<RequestSourceBo> map_sources) {
        this.map_sources = map_sources;
    }

    public List<RequestTaskBo> getMap_tasks() {
        return map_tasks;
    }

    public void setMap_tasks(List<RequestTaskBo> map_tasks) {
        this.map_tasks = map_tasks;
    }

    public List<RequestTaskBo> getMap_outputs() {
        return map_outputs;
    }

    public void setMap_outputs(List<RequestTaskBo> map_outputs) {
        this.map_outputs = map_outputs;
    }
}
