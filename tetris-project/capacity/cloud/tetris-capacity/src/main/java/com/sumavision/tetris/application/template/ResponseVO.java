package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/12/10 16:14
 */

/**
 * @ClassName: ResponseVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/10 16:14
 */
public class ResponseVO {

    private String taskId;

    private Integer status;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
