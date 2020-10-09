package com.sumavision.signal.bvc.common;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;
import java.net.SocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonUtil {

    /**
     * 将List<T>数组转换为字符串   (*该方法输出的字符串如2,2,2,  注：每个元素后面都会跟','，方便查询)
     * @return
     */
    public static<T> String anylistToString(List<T> list){
        if(list==null){
           return null;
        }
        StringBuilder result = new StringBuilder();
        //第一个前面不拼接","
        for(T string :list) {
           result.append(string).append(",");
        }
        return result.toString();
     }


    public static boolean ipEquals(String ip0 , String ip1) {
        return IpV4Util.getIpV4Value(ip0) == IpV4Util.getIpV4Value(ip1);
    }

    public static String socketAddressFormat(String socketAddress) {
        String[] strs = socketAddress.split(":");
        return ipFormat(strs[0]) + ":" + strs[1];
    }

    public static String ipFormat(String ip) {
        return IpV4Util.trans2IpStr(IpV4Util.getIpV4Value(ip));
    }

    public static String ipFormat(String ip , String mask) {
        return IpV4Util.trans2IpStr(IpV4Util.getIpV4Value(ip) & IpV4Util.getIpV4Value(mask));
    }

    public static String getSocketAddress(SocketAddress socketAddress){
        return socketAddress.toString().substring(1);
    }

    public static String getIp(SocketAddress socketAddress){
        return socketAddress.toString().substring(1).split(":")[0];
    }

    public static Integer getPort(SocketAddress socketAddress){
        return Integer.parseInt(socketAddress.toString().substring(1).split(":")[1]);
    }

    public static Integer getPort(String socketAddress){
        return Integer.parseInt(socketAddress.trim().split(":")[1]);
    }

    //(*该方法输出的字符串中间有空格如2, 2, 2)
    public static String listToString(List list) {
        String str = list.toString();
        return str.substring(1 , str.length() - 1);
    }

    public static boolean stringEquals(String str0 , String str1) {
        if (str0 == null && str1 == null)
            return true;
        else if (str0 == null || str1 == null)
            return false;
        else
            return str0.equals(str1);
    }

    public static boolean isMulticast(String ip){
        //补全ip格式  224.1.1.1 -> 224.001.001.001
        String[] ipSplit = ip.split("\\.");
        String ipS = new String();
        for(int index = 0; index < ipSplit.length; index++){
            while(ipSplit[index].length() < 3){
                ipSplit[index] = "0" + ipSplit[index];
            }
            if(index == 0){
                ipS = ipSplit[index];
            }else{
                ipS = ipS + "." + ipSplit[index];
            }
        }
        String regEx = "2((2[4-9])|(3\\d))(\\.(([01]\\d{2})|(2(([0-4]\\d)|(5[0-5]))))){3}";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);

        Matcher matcher = pattern.matcher(ipS);

        return matcher.matches();
    }


    /**
     * java对象转换为xml文件
     * @param load    java对象.Class
     * @return    xml文件的String
     * @throws JAXBException
     */
    public static String beanToXml(Object obj,Class<?> load) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj,writer);
        return writer.toString();
    }


    /**
     * xml文件配置转换为对象
     * @param xmlPath  xml文件路径
     * @param load    java对象.Class
     * @return    java对象
     * @throws JAXBException
     */
    public static Object xmlToBean(String xmlPath,Class<?> load) throws Exception{
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(new File(xmlPath));
        return object;
    }

    /**
     * 希望从某些仅含一个元素的list 中拿到值,,,jpa查询常常查到的就是list
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T listToBean(List<T> list) throws BaseException {
        if (list.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"list is empty");
        }else if (list.size()==1){
            return list.get(0);
        }else {
            throw new BaseException(StatusCode.ERROR,"list is too much element");
        }
    }

    public static String getIpAndPortFromUrl(String url) {
        String host="";//格式为ip:port
        // 1.判断是否为空
        if (url == null || url.trim().equals("")) {
            return "";
        }
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group() ;
        }
        return host;
    }

    public static String getIpFromUrl(String url) {
        String ip="";//格式为ip:port
        // 1.判断是否为空
        if (url == null || url.trim().equals("")) {
            return "";
        }
        Pattern p = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            ip = matcher.group() ;
        }
        return ip;
    }

    public static Integer getPortFromUrl(String url) {
        String socketAddr = getIpAndPortFromUrl(url);
        return getPort(socketAddr);
    }

    //判断list是否存在相同元素
    public static boolean hasSame(List<? extends Object> list)
    {
        if(null == list)
            return false;
        return list.size() == new HashSet<Object>(list).size();
    }

    public static ArrayList<String> stringToArrayList(String str, String separator) {
        ArrayList<String> arr = new ArrayList<String>();
        if ((str == null) || (separator == null)) {
            return arr;
        }
        StringTokenizer st = new StringTokenizer(str, separator);
        while (st.hasMoreTokens()) {
            arr.add(st.nextToken());
        }
        return arr;
    }

    public static String getUUID32(){
        return UUID.randomUUID().toString().toLowerCase(Locale.ENGLISH);
    }

    public static JSONObject httpPost(String url, JSONObject jsonObject)throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity(jsonObject.toJSONString(),headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,entity,String.class);
        String rstStr = responseEntity.getBody();
        return JSONObject.parseObject(rstStr);
    }

    /**
     * 计算时差，秒为单位
     * @param lastDate
     * @return
     */
    public static Integer calLastedTime(Date lastDate){
        return new Long(new Date().getTime() - lastDate.getTime()).intValue() / 1000;
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }


    public static List<String> getAllIp(String startIp, String endIp) {
        ArrayList<String> ips = new ArrayList<String>();
        String[] ipfromd = startIp.split("\\.");
        String[] iptod = endIp.split("\\.");
        int[] int_ipf = new int[4];
        int[] int_ipt = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipfromd[i]);
            int_ipt[i] = Integer.parseInt(iptod[i]);
        }
        for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
            for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1]
                    : 255); B++) {
                for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2]
                        : 255); C++) {
                    for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3]
                            : 255); D++) {
                        ips.add(new String(A + "." + B + "." + C + "." + D));
                    }
                }
            }
        }
        return ips;
    }
}
