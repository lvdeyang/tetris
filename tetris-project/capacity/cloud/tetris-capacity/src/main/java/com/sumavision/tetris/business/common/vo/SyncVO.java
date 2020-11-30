package com.sumavision.tetris.business.common.vo;/**
 * Created by Poemafar on 2020/11/26 9:52
 */

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
}
