package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/9/25 16:26
 */

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.*;

/**
 * @ClassName: TemplatePO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/25 16:26
 */
@Entity
@Table(name = "TETRIS_CAPACITY_TEMPLATE")
public class TemplatePO extends AbstractBasePO {

    enum BusinessTemplateType{
        ALL,        //任务模板
        INPUT,      //输入模板
        ENCODE,     //编码模板
        PREPROCESS, //预处理模板
        OUTPUT      //输出模板
    }


    /**
     * 模板名
     */
    private String name;


    /**
     * 模板类型
     */
    private BusinessTemplateType type;

    /**
     * 业务类型
     */
    private BusinessType businessType;

    /**
     * 模板任务类型，转码，转封装，透传
     */
    private TaskType taskType = TaskType.TRANS;

    /**
     * 模板内容
     */
    private String body;

    /**
     * 模板描述信息
     */
    private String description;

    /**
     * 模板转换后的参数
     */
    private String transformParams;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public BusinessTemplateType getType() {
        return type;
    }

    public void setType(BusinessTemplateType type) {
        this.type = type;
    }

    @Column(columnDefinition = "longtext")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Column(columnDefinition = "longtext")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(columnDefinition = "longtext")
    public String getTransformParams() {
        return transformParams;
    }

    public void setTransformParams(String transformParams) {
        this.transformParams = transformParams;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}
