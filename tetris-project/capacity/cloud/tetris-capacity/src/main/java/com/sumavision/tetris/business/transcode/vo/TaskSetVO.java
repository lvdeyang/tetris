package com.sumavision.tetris.business.transcode.vo;

import com.sumavision.tetris.capacity.bo.request.*;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskSetVO implements Serializable {
    private static final long serialVersionUID = 6230258714652958134L;

    private Long task_link_id;

    private String device_ip;

    private CreateInputsRequest create_input;
    private DeleteInputsRequest delete_input;

    private CreateProgramsRequest create_program;
    private DeleteProgramRequest delete_program;


    //修改任务源
    private ArrayList<PutTaskSourceRequest> modify_source;
    //修改指定输入参数
    private ArrayList<PutInputsRequest> modify_input_params;

    private ArrayList<PutBackupModeRequest> modify_backup_mode;
    //修改去隔行
    private ArrayList<PutTaskDecodeProcessRequest> modify_decode_process;
    //修改解码方式
    private ArrayList<PatchDecodeRequest> modify_decode_mode;

    //修改任务编码
    private ArrayList<PutTaskEncodeRequest> modify_encoders;

    private ArrayList<AddTaskEncodeRequest> add_encoders;

    private ArrayList<DeleteTaskEncodeResponse> delete_encoders;

    //添加任务
    private CreateTaskRequest add_task;

    //删任务
    private DeleteTasksRequest delete_task;

    //修改输出
    private ArrayList<PutOutputRequest> modify_output;

    private CreateOutputsRequest add_output;

    private DeleteOutputsRequest delete_output;

    public Long getTask_link_id() {
        return task_link_id;
    }

    public void setTask_link_id(Long task_link_id) {
        this.task_link_id = task_link_id;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public CreateInputsRequest getCreate_input() {
        return create_input;
    }

    public void setCreate_input(CreateInputsRequest create_input) {
        this.create_input = create_input;
    }

    public DeleteInputsRequest getDelete_input() {
        return delete_input;
    }

    public void setDelete_input(DeleteInputsRequest delete_input) {
        this.delete_input = delete_input;
    }

    public CreateProgramsRequest getCreate_program() {
        return create_program;
    }

    public void setCreate_program(CreateProgramsRequest create_program) {
        this.create_program = create_program;
    }

    public DeleteProgramRequest getDelete_program() {
        return delete_program;
    }

    public void setDelete_program(DeleteProgramRequest delete_program) {
        this.delete_program = delete_program;
    }

    public ArrayList<PutTaskSourceRequest> getModify_source() {
        return modify_source;
    }

    public void setModify_source(ArrayList<PutTaskSourceRequest> modify_source) {
        this.modify_source = modify_source;
    }

    public ArrayList<PutInputsRequest> getModify_input_params() {
        return modify_input_params;
    }

    public void setModify_input_params(ArrayList<PutInputsRequest> modify_input_params) {
        this.modify_input_params = modify_input_params;
    }

    public ArrayList<PutTaskDecodeProcessRequest> getModify_decode_process() {
        return modify_decode_process;
    }

    public void setModify_decode_process(ArrayList<PutTaskDecodeProcessRequest> modify_decode_process) {
        this.modify_decode_process = modify_decode_process;
    }

    public ArrayList<PatchDecodeRequest> getModify_decode_mode() {
        return modify_decode_mode;
    }

    public void setModify_decode_mode(ArrayList<PatchDecodeRequest> modify_decode_mode) {
        this.modify_decode_mode = modify_decode_mode;
    }

    public ArrayList<PutTaskEncodeRequest> getModify_encoders() {
        return modify_encoders;
    }

    public void setModify_encoders(ArrayList<PutTaskEncodeRequest> modify_encoders) {
        this.modify_encoders = modify_encoders;
    }

    public ArrayList<AddTaskEncodeRequest> getAdd_encoders() {
        return add_encoders;
    }

    public void setAdd_encoders(ArrayList<AddTaskEncodeRequest> add_encoders) {
        this.add_encoders = add_encoders;
    }

    public ArrayList<DeleteTaskEncodeResponse> getDelete_encoders() {
        return delete_encoders;
    }

    public void setDelete_encoders(ArrayList<DeleteTaskEncodeResponse> delete_encoders) {
        this.delete_encoders = delete_encoders;
    }

    public ArrayList<PutOutputRequest> getModify_output() {
        return modify_output;
    }

    public void setModify_output(ArrayList<PutOutputRequest> modify_output) {
        this.modify_output = modify_output;
    }

    public CreateOutputsRequest getAdd_output() {
        return add_output;
    }

    public void setAdd_output(CreateOutputsRequest add_output) {
        this.add_output = add_output;
    }

    public DeleteOutputsRequest getDelete_output() {
        return delete_output;
    }

    public void setDelete_output(DeleteOutputsRequest delete_output) {
        this.delete_output = delete_output;
    }

    public DeleteTasksRequest getDelete_task() {
        return delete_task;
    }

    public void setDelete_task(DeleteTasksRequest delete_task) {
        this.delete_task = delete_task;
    }

    public CreateTaskRequest getAdd_task() {
        return add_task;
    }

    public void setAdd_task(CreateTaskRequest add_task) {
        this.add_task = add_task;
    }

    public ArrayList<PutBackupModeRequest> getModify_backup_mode() {
        return modify_backup_mode;
    }

    public void setModify_backup_mode(ArrayList<PutBackupModeRequest> modify_backup_mode) {
        this.modify_backup_mode = modify_backup_mode;
    }
}
