package com.sumavision.tetris.capacity.bo.input;/**
 * Created by Poemafar on 2021/1/4 17:52
 */

import com.sumavision.tetris.business.common.po.TaskInputPO;

/**
 * @ClassName: InputWrapperBO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/1/4 17:52
 */
public class InputWrapperBO extends InputBO {

    private Boolean beCreate;

    private TaskInputPO taskInputPO;

    public Boolean getBeCreate() {
        return beCreate;
    }

    public InputWrapperBO setBeCreate(Boolean beCreate) {
        this.beCreate = beCreate;
        return this;
    }

    public TaskInputPO getTaskInputPO() {
        return taskInputPO;
    }

    public InputWrapperBO setTaskInputPO(TaskInputPO taskInputPO) {
        this.taskInputPO = taskInputPO;
        return this;
    }
}
