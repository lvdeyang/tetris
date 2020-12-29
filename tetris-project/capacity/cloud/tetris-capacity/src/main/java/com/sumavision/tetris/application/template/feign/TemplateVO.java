package com.sumavision.tetris.application.template.feign;/**
 * Created by Poemafar on 2020/12/11 8:34
 */

/**
 * @ClassName: TemplateTaskVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/11 8:34
 */
public class TemplateVO {


    /**
     * 模板名
     */
    private String name;

    /**
     * 模板所属的业务类型
     */
    private String businessType;


    /**
     * 模板的任务类型：转码(包括转封装)，只转封装，转发
     */
    private String taskType;


    /**
     * 模板参数
     */
    private String param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public TemplateVO() {
    }

    /**
     * @MethodName: TemplateVO
     * @Description: TODO
     * @param name 1 模板名
     * @param businessType 2 业务类型
     * @param taskType 3 任务类型
     * @param param 4 模板参数
     * @Return:
     * @Author: Poemafar
     * @Date: 2020/12/11 8:47
     **/
    public TemplateVO(String name, String businessType, String taskType, String param) {
        this.name = name;
        this.businessType = businessType;
        this.taskType = taskType;
        this.param = param;
    }
}
