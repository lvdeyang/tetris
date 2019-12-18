package com.sumavision.tetris.sts.common;

import java.util.List;

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
}
