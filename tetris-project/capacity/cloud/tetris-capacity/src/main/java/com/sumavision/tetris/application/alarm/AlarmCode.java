package com.sumavision.tetris.application.alarm;

/**
 * @ClassName: AlarmCode
 * @Description TODO 告警码
 * final类 不能被继承
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/28 8:42
 */
public final class AlarmCode {

    /**
     * 授权失败
     */
    public static final String LICENSE_FAIL = "11077000";

    /**
     * 设备离线
     */
    public static final String DEVICE_OFFLINE = "11011000";

    /**
     * 设备不同步
     */
    public static final String DEVICE_NOSYNC = "11011001";

    /**
     * 设备切换，提示
     */
    public static final String DEVICE_SWITCH = "11011002";

    private AlarmCode(){

    }
}
