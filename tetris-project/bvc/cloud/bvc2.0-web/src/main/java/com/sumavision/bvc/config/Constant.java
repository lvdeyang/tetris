package com.sumavision.bvc.config;

public interface Constant {
    public Long RESOURCE_APPLY_TIME_OUT=10000L;//资源层申请超时时间
    public Long RESOURCE_LOCK_TIME_OUT=10000L;//业务绑定超时时间
    public Long ACESS_ACESS_TIME_OUT=10000L;//接入层访问超时时间

    										//返回错误码定义
    
    public String SIGNAL_CONTROL_LAYER_ID = "suma-venus-signal";
}
