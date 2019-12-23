package com.sumavision.tetris.sts.communication.httptool;

import com.sumavision.tetris.sts.common.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2018/1/25.
 */
@Component
public class XToolMsgType {

    @Autowired
    ConstantUtil constantUtil;

    public static final String GET_NETCARD_INFO = "get_netcard_info";

    public static final String SET_NETCARD_INFO = "set_netcard_info";

    public static final String GET_IP_INFO = "get_ip_info";

    public static final String SET_IP_INFO = "set_ip_info";

    public static final String GET_ROUTE_INFO = "get_route_info";

    public static final String SET_ROUTE_INFO = "set_route_info";

    public static final String SET_BONDING = "set_bonding";

    public static final String REMOVE_BONDING = "remove_bonding";

    public static final String ADD_VLAN = "add_vlan";

    public static final String DEL_VLAN = "del_vlan";

    public static final String SET_NOTIFY = "set_notify_param";


    public static final String getUrl(String ip , String type) {
        return "http://" + ip + ":8910/action/" + type;
    }

    public String getCallBackUrl () {
        return "http://" + constantUtil.getServerIp() + ":"+ constantUtil.getServerPort() +"/web-app/alarm/netcardNotice";
    }

}
