package com.sumavision.tetris.sts.communication.xml;

/**
 * Created by Lost on 2017/1/24.
 */
public class AbstractMsg {

    protected Integer id;

    protected String ip;

    protected Integer port;

    protected String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSocketAddress(){
        return ip + ":" + port;
    }

    public void setSocketAddress(String socketAddress){
        ip = socketAddress.split(":")[0];
        port = Integer.parseInt(socketAddress.split(":")[1]);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
