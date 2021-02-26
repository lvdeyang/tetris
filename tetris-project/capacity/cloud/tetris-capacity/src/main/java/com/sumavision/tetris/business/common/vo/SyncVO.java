package com.sumavision.tetris.business.common.vo;/**
 * Created by Poemafar on 2020/11/26 9:52
 */

import com.sumavision.tetris.capacity.constant.Constant;

import java.util.List;

/**
 * @ClassName: SyncVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/26 9:52
 */
public class SyncVO {

    private String deviceIp;

    private Integer devicePort= Constant.TRANSFORM_PORT;

    private String businessType;

    private List<String>  jobIds;

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public List<String> getJobIds() {
        return jobIds;
    }

    public void setJobIds(List<String> jobIds) {
        this.jobIds = jobIds;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getDevicePort() {
        return devicePort;
    }

    public SyncVO setDevicePort(Integer devicePort) {
        this.devicePort = devicePort;
        return this;
    }
}
