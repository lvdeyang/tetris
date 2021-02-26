package com.sumavision.tetris.business.common;/**
 * Created by Poemafar on 2021/2/24 8:57
 */

import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.constant.UrlConstant;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @ClassName: TransformModule
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/24 8:57
 */
public class TransformModule {

    private String ip;

    private Integer port;

    public String getIp() {
        return ip;
    }

    public TransformModule setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public TransformModule setPort(Integer port) {
        this.port = port;
        return this;
    }

    public TransformModule() {
    }

    public TransformModule(String ip) {
        this.ip=ip;
    }

    public TransformModule(String ip, Integer port) {
         this.ip=ip;
         this.port=port;
    }

    public String getSocketAddress(){
        CapacityProps capacityProps = SpringBeanFactory.getBean(CapacityProps.class);
        StringBufferWrapper sbw = new StringBufferWrapper().append(UrlConstant.URL_PREFIX);
        if (this.ip!=null&&!this.ip.isEmpty()){
            sbw.append(this.ip);
        }else{
            sbw.append(capacityProps.getIp());
        }
        sbw.append(":");
        if(this.port!=null){
            sbw.append(this.port);
        }else{
            sbw.append(capacityProps.getPort());
        }

        return sbw.toString();
    }
}
