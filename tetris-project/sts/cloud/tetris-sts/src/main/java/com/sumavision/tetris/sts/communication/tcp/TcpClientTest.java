package com.sumavision.tetris.sts.communication.tcp;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2016/12/30.
 */
@Component
public class TcpClientTest implements InitializingBean {
    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    TcpClientManager tcpClientManager;

    @Override
    public void afterPropertiesSet() throws Exception {
//        System.out.println("------wolaila--------");
//        taskExecutor.execute(new Tcp("10.10.40.150"));
//        taskExecutor.execute(new Tcp("10.10.40.143"));
    }

    public class Tcp implements  Runnable{

        private String socketAddress;

        public Tcp(String socketAddress) {
            this.socketAddress = socketAddress;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //   MsgInfo msgInfo = new MsgInfo(socketAddress , 8300 , CmdType.GET_DEVICE_INFO);
        //    tcpClientManager.send(msgInfo.generateXml());
        }
    }
}
