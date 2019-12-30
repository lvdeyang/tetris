package com.sumavision.tetris.sts.communication.tcp;

import com.sumavision.tetris.sts.communication.xml.AlarmInfo;

/**
 * Created by Lost on 2017/1/13.
 */
public interface TcpClientCallback {
    void onLine(String socketAddress);
    void offLine(String socketAddress);
    void alarm(AlarmInfo alarmInfo);
}
