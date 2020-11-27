package com.sumavision.tetris.business.common.Util;/**
 * Created by Poemafar on 2020/7/26 22:16
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
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

    public static List<String> types = new ArrayList<String>();
    static {
        types.add("java.lang.Integer");
        types.add("java.lang.Double");
        types.add("java.lang.Float");
        types.add("java.lang.Long");
        types.add("java.lang.Short");
        types.add("java.lang.Byte");
        types.add("java.lang.Boolean");
        types.add("java.lang.Character");
        types.add("java.lang.String");
        types.add("int");
        types.add("double");
        types.add("long");
        types.add("short");
        types.add("byte");
        types.add("boolean");
        types.add("char");
        types.add("float");
        types.add("enum");
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
     * source覆盖target
     * @param source
     * @param target
     * @return
     */
    public static JSONObject coverJSONObject(JSONObject source,JSONObject target){
        target.keySet().stream().forEach(tk->{
            if (target.get(tk) instanceof JSONArray){
                if (source.get(tk)!=null){
                    Integer srcLen = source.getJSONArray(tk).size();
                    Integer tarLen = target.getJSONArray(tk).size();
                    for (int i = 0; i < srcLen && i < tarLen; i++) {
                        coverJSONObject(source.getJSONArray(tk).getJSONObject(i),target.getJSONArray(tk).getJSONObject(i));
                    }
                    //target多出来的加到source里
                    for (int i=srcLen;i<tarLen;i++){
                        source.getJSONArray(tk).add(target.getJSONArray(tk).getJSONObject(i));
                    }
                }else{
                    source.put(tk,target.get(tk));
                }
            }else if (target.get(tk) instanceof JSONObject){
                if (source.get(tk)!=null) {
                    coverJSONObject(source.getJSONObject(tk), target.getJSONObject(tk));
                }else{
                    source.put(tk,target.get(tk));
                }
            } else{
                source.put(tk,target.get(tk));
            }
        });

        return source;
    }



    /**
     * 利用反射合并两个实例类，将newProperties中的属性覆盖到preProperties，
     * 如果newProperties中的属性为空，则保留preProperties中的属性
     *
     * 已经支持递归覆盖.对成员变量是Object的成员变量进行递归
     * 如果成员变量是子类,还可以找它的父类向上覆盖
     *
     * @param preProperties 之前旧的实例属性
     * @param newProperties 新的实例属性
     * @param <T>                 返回合并后的实例属性
     * @return
     */
    public static <T> T combineSydwCore(T preProperties, T newProperties) throws IllegalAccessException {
        if (preProperties == null) {
            preProperties = newProperties;
            return preProperties;
        }
        Class tClass = newProperties.getClass();
        while (tClass != null) {
            merge(preProperties, tClass.cast(newProperties));
            tClass = tClass.getSuperclass();
        }
        return preProperties;
    }

    private static <T> T merge(T preProperties, T newProperties) throws IllegalAccessException {
        Field[] preFileds = preProperties.getClass().getDeclaredFields();
        Field[] newFileds = newProperties.getClass().getDeclaredFields();

        for (int i = 0; i < newFileds.length; i++) {
            Field preField = preFileds[i];
            Field newField = preFileds[i];
            if (Modifier.isStatic(newField.getModifiers())) {
                continue;
            }
            Field targetField = newFileds[i];
            if (Modifier.isStatic(targetField.getModifiers())) {
                continue;
            }

            preField.setAccessible(true);
            newField.setAccessible(true);

            if(null != newField.get(newProperties)  &&  !"serialVersionUID".equals(newField.getName())){
                //如果成员变量是个数组，那么遍历数组的每个对象并进行递归合并
                if(newField.getType().isArray()){
                    int newLength = Array.getLength(newField.get(newProperties));
                    int preLength = Array.getLength(preField.get(preProperties));
                    for(int j=0; j<newLength && j<preLength;j++){
                        Object preDeProperty = Array.get(preField.get(preProperties),j);
                        Object newDeProperty = Array.get(newField.get(newProperties),j);
                        combineSydwCore(preDeProperty,newDeProperty);
                    }
                }
                //如果成员变量不是基本数据类型或者包装类型，则进行递归遍历
                else if (!types.contains(newField.getType().getName())) {
                    preField.set(preProperties,combineSydwCore(preField.get(preProperties), newField.get(newProperties)));
                }  else {
                    preField.set(preProperties, newField.get(newProperties));
                }
            }
        }
        return preProperties;
    }

//    /**
//     * 该方法是用于相同对象不同属性值的合并<br>
//     * 如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖tagetBean重点的值<br>
//     * 如果sourceBean有值，targetBean没有，则采用sourceBean的值<br>
//     * 如果sourceBean没有值，targetBean有，则保留targetBean的值
//     *
//     * @param sourceBean    被提取的对象bean
//     * @param targetBean    用于合并的对象bean
//     * @return targetBean,合并后的对象
//     */
//    public static <T> T combineSydwCore(T sourceBean, T targetBean){
//        Class sourceBeanClass = sourceBean.getClass();
//        Class targetBeanClass = targetBean.getClass();
//
//        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
//        Field[] targetFields = targetBeanClass.getDeclaredFields();
//        for(int i=0; i<sourceFields.length; i++){
//            Field sourceField = sourceFields[i];
//            if(Modifier.isStatic(sourceField.getModifiers())){
//                continue;
//            }
//            Field targetField = targetFields[i];
//            if(Modifier.isStatic(targetField.getModifiers())){
//                continue;
//            }
//
//            sourceField.setAccessible(true);
//            targetField.setAccessible(true);
//            if (targetField.getType().)
//
//            try {
//                if( !(sourceField.get(sourceBean) == null) &&  !"serialVersionUID".equals(sourceField.getName().toString())){
//                    targetField.set(targetBean,sourceField.get(sourceBean));
//                }
//            } catch (IllegalArgumentException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return targetBean;
//    }

}
