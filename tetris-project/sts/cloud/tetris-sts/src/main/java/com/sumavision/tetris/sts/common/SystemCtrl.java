package com.sumavision.tetris.sts.common;


import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Lost on 2017/2/6.
 */
public class SystemCtrl {

    public static int TRANS_DEV_PORT = 8800;
    public static int PACKET_DEV_PORT = 8900;
    public static int STREAM_MEDIA_DEV_PORT = 8040;
    public static int MATRIX_DEV_PORT = 8200;
    public static int BACKUP_TOOL_PORT = 8300;

    public static String getDeviceAddress(String socketAddress){
        return socketAddress + ":" + BACKUP_TOOL_PORT;
    }

    public static String getPacketAddress(String socketAddress){
        return socketAddress + ":" +  PACKET_DEV_PORT;
    }

    public static String getTransAddress(String socketAddress){
        return socketAddress + ":" +  TRANS_DEV_PORT;
    }

    public static String getMatrixAddress(String socketAddress){
        return socketAddress + ":" +  MATRIX_DEV_PORT;
    }

    public static String getStreamMediaAddress(String socketAddress){
        return socketAddress + ":" +  STREAM_MEDIA_DEV_PORT;
    }

//    public static String getSocketAddress(String socketAddress , FunUnitType unitType) {
//        switch (unitType) {
//            case TRANS:
//                return getTransAddress(socketAddress);
//            case ENCAPSULATE:
//                return getPacketAddress(socketAddress);
//            case STREAM_MEDIA:
//                return getStreamMediaAddress(socketAddress);
//            case MATRIX:
//                return getMatrixAddress(socketAddress);
//            default:
//                return null;
//        }
//    }

//    public static FunUnitType getFunUnitType(String socketAddress) {
//        try {
//            Integer port = Integer.parseInt(socketAddress.split(":")[1]);
//            if (port.equals(TRANS_DEV_PORT))
//                return FunUnitType.TRANS;
//            else if (port.equals(PACKET_DEV_PORT))
//                return FunUnitType.ENCAPSULATE;
//            else if (port.equals(STREAM_MEDIA_DEV_PORT))
//                return FunUnitType.STREAM_MEDIA;
//            else if (port.equals(MATRIX_DEV_PORT))
//                return FunUnitType.MATRIX;
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static Set<String> localIpSet() {
        Set<String> strings = new HashSet<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    if (inetAddress instanceof Inet4Address)
                        strings.add(inetAddress.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return strings;
    }

    public void init(){
        try {
            Properties p = new Properties();
            InputStream p_in = this.getClass().getResourceAsStream("config/port.properties");
            p.load(p_in);
            TRANS_DEV_PORT = Integer.parseInt(p.getProperty("TRANS_DEV_PORT"));
            PACKET_DEV_PORT = Integer.parseInt(p.getProperty("PACKET_DEV_PORT"));
            STREAM_MEDIA_DEV_PORT = Integer.parseInt(p.getProperty("STREAM_MEDIA_DEV_PORT"));
            MATRIX_DEV_PORT = Integer.parseInt(p.getProperty("MATRIX_DEV_PORT"));
            BACKUP_TOOL_PORT = Integer.parseInt(p.getProperty("BACKUP_TOOL_PORT"));
            p_in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
