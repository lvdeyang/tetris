package com.sumavision.tetris.application.template.feign;/**
 * Created by Poemafar on 2020/10/27 14:32
 */

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.capacity.constant.Constant;

import java.util.List;

/**
 * @ClassName: TemplateTaskVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/10/27 14:32
 */
public class TemplateTaskVO {

    private String task_ip;               //业务必下的参数

    private Integer task_port = Constant.TRANSFORM_PORT;            //业务必下的参数

    private String template;             //业务必下的参数

    /**
     * 业务类型,模板参数
     */
    private BusinessType business_type;

    private JSONArray map_sources;   //业务必下的参数

    private JSONArray map_tasks;

    private JSONArray map_outputs;   //业务必下的参数

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public JSONArray getMap_sources() {
        return map_sources;
    }

    public void setMap_sources(JSONArray map_sources) {
        this.map_sources = map_sources;
    }

    public JSONArray getMap_tasks() {
        return map_tasks;
    }

    public void setMap_tasks(JSONArray map_tasks) {
        this.map_tasks = map_tasks;
    }

    public JSONArray getMap_outputs() {
        return map_outputs;
    }

    public void setMap_outputs(JSONArray map_outputs) {
        this.map_outputs = map_outputs;
    }

    public String getTask_ip() {
        return task_ip;
    }

    public void setTask_ip(String task_ip) {
        this.task_ip = task_ip;
    }

    public Integer getTask_port() {
        return task_port;
    }

    public void setTask_port(Integer task_port) {
        this.task_port = task_port;
    }

    public BusinessType getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(BusinessType business_type) {
        this.business_type = business_type;
    }
}
