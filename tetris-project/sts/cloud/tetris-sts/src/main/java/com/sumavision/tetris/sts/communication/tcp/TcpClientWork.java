package com.sumavision.tetris.sts.communication.tcp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

/**
 * Created by Lost on 2016/12/29.
 */
@Component
public class TcpClientWork implements Runnable{

    static Logger logger = LogManager.getLogger(TcpClientWork.class);

    private int reconnect ;
    private int reconnect_time ;
    private int offline ;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    TcpClientManager tcpClientManager;

    @PostConstruct
    public void start(){
        reconnect = tcpClientManager.getReconnect();
        reconnect_time = tcpClientManager.getReconnect_time();
        offline = tcpClientManager.getOffline();
		Executors.newSingleThreadExecutor().execute(this);
	}

    @Override
    public void run() {
        taskExecutor.execute(new TcpClientOffline());
        while(true){
            try {
                String socketAddress = TcpClientManager.RECONNECT_QUEUE.take();
                taskExecutor.execute(new TcpClientReconnect(socketAddress));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public class TcpClientReconnect implements Runnable{
        String socketAddress;

        public TcpClientReconnect(String socketAddress){
            this.socketAddress = socketAddress;
        }

        @Override
        public void run() {
            int i = 0;
            try {
                while(i < reconnect_time){
                    if(tcpClientManager.reConnect(socketAddress)){
                        return;
                    }else{
                        Thread.sleep(reconnect);
                    }
                    i++;
                }
                tcpClientManager.offLine(socketAddress);
            } catch (Exception e) {
                logger.error(e.getMessage());
                tcpClientManager.offLine(socketAddress);
            }
        }
    }

    public class TcpClientOffline implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    for (String socketAddress: TcpClientManager.OFFLINE_SET) {
                        if(tcpClientManager.reConnect(socketAddress))
                            TcpClientManager.OFFLINE_SET.remove(socketAddress);
                    }
                    Thread.sleep(offline);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

}
