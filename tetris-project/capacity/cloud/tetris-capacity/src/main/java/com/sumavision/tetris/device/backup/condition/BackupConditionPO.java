package com.sumavision.tetris.device.backup.condition;

import com.sumavision.tetris.orm.po.AbstractBasePO;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Poemafar on 2019/5/10 16:55
 * 保存自动触发备份的条件，满足条件的才能触发主备切换
 */
@Entity
@Table(name="tetris_capacity_backup_condition")
public class BackupConditionPO extends AbstractBasePO {

    /**
     * 控制口断连
      */
    private Boolean  ctrlPortDisconnect = false;

    /**
     * NONE 不触发；ANY任一个坏了触发；ALL全坏了触发
     */
    public enum NetCardErrorType{
        NONE,ANY,ALL
    }


    /**
     * 输入网卡异常，default不触发；any任一个坏触发；all全坏触发
     * 区分any和all是因为：有可能某设备两个输入网口，一个是另一个的备，坏了一个也不用触发主备切换还能继续用。
     */
    private NetCardErrorType inputNetCardError = NetCardErrorType.NONE;

    /**
     * 输出网卡异常，false不触发；true任一个网卡坏了都触发
     */
    private Boolean outputNetCardError = true;

//  功能单元离线，必备份，不可配
//    /**
//     * 设备节点离线，功能单元离线
//     */
//    private Boolean deviceNodeOffline = true;

    /**
     * CPU过载，设备检测告警会报CPU过载告警
     */
    private Boolean cpuOverride = false;

    /**
     * GPU过载，设备检测告警会报GPU过载告警
     */
    private Boolean gpuOverride = false;


    private Boolean memOverride = false;

    private Boolean diskOverride = false;

    @Column
    @Type(type = "yes_no")
    public Boolean getCtrlPortDisconnect() {
        return ctrlPortDisconnect;
    }

    public void setCtrlPortDisconnect(Boolean ctrlPortDisconnect) {
        this.ctrlPortDisconnect = ctrlPortDisconnect;
    }



    //    @Column
//    @Type(type = "yes_no")
//    public Boolean getDeviceNodeOffline() {
//        return deviceNodeOffline;
//    }
//
//    public void setDeviceNodeOffline(Boolean deviceNodeOffline) {
//        this.deviceNodeOffline = deviceNodeOffline;
//    }

    @Column
    @Type(type = "yes_no")
    public Boolean getCpuOverride() {
        return cpuOverride;
    }

    public void setCpuOverride(Boolean cpuOverride) {
        this.cpuOverride = cpuOverride;
    }

    @Column
    @Type(type = "yes_no")
    public Boolean getGpuOverride() {
        return gpuOverride;
    }

    public void setGpuOverride(Boolean gpuOverride) {
        this.gpuOverride = gpuOverride;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public NetCardErrorType getInputNetCardError() {
        return inputNetCardError;
    }

    public void setInputNetCardError(NetCardErrorType inputNetCardError) {
        this.inputNetCardError = inputNetCardError;
    }

    @Column
    @Type(type = "yes_no")
    public Boolean getOutputNetCardError() {
        return outputNetCardError;
    }

    public void setOutputNetCardError(Boolean outputNetCardError) {
        this.outputNetCardError = outputNetCardError;
    }

    @Column
    @Type(type = "yes_no")
    public Boolean getMemOverride() {
        return memOverride;
    }

    public void setMemOverride(Boolean memOverride) {
        this.memOverride = memOverride;
    }

    @Column
    @Type(type = "yes_no")
    public Boolean getDiskOverride() {
        return diskOverride;
    }

    public void setDiskOverride(Boolean diskOverride) {
        this.diskOverride = diskOverride;
    }

}
