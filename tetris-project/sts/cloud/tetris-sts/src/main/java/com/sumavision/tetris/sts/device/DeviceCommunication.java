package com.sumavision.tetris.sts.device;

import com.suma.xianrd.communication.transport.CommunicationException;
import com.sumavision.tetris.sts.communication.tcp.TcpClientManager;
import com.sumavision.tetris.sts.communication.xml.AckInfo;
import com.sumavision.tetris.sts.communication.xml.MsgInfo;
import com.sumavision.tetris.sts.communication.xml.MsgManager;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.CommonUtil;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.common.SystemCtrl;
import com.sumavision.tetris.sts.communication.xml.CommonXmlCreate;
import com.sumavision.tetris.sts.communication.xml.CommonXmlParse;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import com.sumavision.tetris.sts.task.source.SourcePO;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Lost on 2017/2/16.
 */
@Component
public class DeviceCommunication {
    @Autowired
    TcpClientManager tcpClientManager;


    public List<NetCardInfoPO> getNetCardInfo(DeviceNodePO deviceNodePO) throws BaseException {
        MsgInfo msgInfo = MsgManager.getMsg(SystemCtrl.getDeviceAddress(deviceNodePO.getDeviceIp()) , "device-info");
        try {
            AckInfo ackInfo = tcpClientManager.sendAndClose(msgInfo);
            if (ackInfo.getAck() != 0) {
                throw new BaseException(StatusCode.ERROR, ErrorCodes.RESPONSE_ERROR);
            }
            List<NetCardInfoPO> netCardInfoPOs = CommonXmlParse.parseNetCardInfo(ackInfo.getAckXml());
            if (netCardInfoPOs.isEmpty()) {
                throw new BaseException(StatusCode.ERROR, ErrorCodes.NETCARD_ERROR);
            }
            return netCardInfoPOs;
        } catch (CommunicationException e) {
            throw new BaseException(StatusCode.ERROR,ErrorCodes.COMMUNICATE_ERROR);
        }
    }

    public List<SourcePO> refreshSDI(DeviceNodePO deviceNodePO) {
       return null;
    }

    public void initDevice(DeviceNodePO deviceNodePO) {

    }

    public void sendNtp(DeviceNodePO deviceNodePO) {
        new Thread(new NtpService(deviceNodePO)).start();
    }

    public class NtpService implements Runnable{

        private DeviceNodePO deviceNodePO;

        public NtpService(DeviceNodePO deviceNodePO) {
            this.deviceNodePO = deviceNodePO;
        }

        @Override
        public void run() {
            String socketAddress = SystemCtrl.getDeviceAddress(deviceNodePO.getDeviceIp());
            Channel channel = null;
            try {
                channel = tcpClientManager.newConnect(socketAddress);
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
            MsgInfo msgInfo = MsgManager.getMsg(socketAddress , CommonXmlCreate.generateNTP(CommonUtil.getIp(channel.localAddress())));
            try {
                tcpClientManager.sendAndClose(msgInfo , channel , 40 * 1000L);
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
        }
    }

}
