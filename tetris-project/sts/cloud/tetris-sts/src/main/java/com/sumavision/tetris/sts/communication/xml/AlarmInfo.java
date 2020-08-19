package com.sumavision.tetris.sts.communication.xml;

/**
 * Created by Lost on 2017/1/24.
 */
public class AlarmInfo extends AbstractMsg {

    private Long inputId;

    private Long programId;

    private Long taskId;

    private Long outputId;

    private Integer alarmCode;

    private String alarmTime;

    private String alarmText;

    public Integer getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(Integer alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Long getInputId() {
        return inputId;
    }

    public void setInputId(Long inputId) {
        this.inputId = inputId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getOutputId() {
        return outputId;
    }

    public void setOutputId(Long outputId) {
        this.outputId = outputId;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }

	@Override
	public String toString() {
		return "AlarmInfo [inputId=" + inputId + ", programId=" + programId
				+ ", taskId=" + taskId + ", outputId=" + outputId
				+ ", alarmCode=" + alarmCode + ", alarmTime=" + alarmTime
				+ ", alarmText=" + alarmText + "]";
	}
    
    
}
