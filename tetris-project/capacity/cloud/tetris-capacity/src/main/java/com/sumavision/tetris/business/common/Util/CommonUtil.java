package com.sumavision.tetris.business.common.Util;/**
 * Created by Poemafar on 2020/7/26 22:16
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: CommonUtil
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/7/26 22:16
 */
public class CommonUtil {

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
}
