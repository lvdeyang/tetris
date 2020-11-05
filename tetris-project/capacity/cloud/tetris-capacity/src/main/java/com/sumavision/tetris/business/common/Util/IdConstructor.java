package com.sumavision.tetris.business.common.Util;/**
 * Created by Poemafar on 2020/9/21 11:08
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: IdConstructor
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/21 11:08
 */
public class IdConstructor {

    public enum IdType{
        INPUT,TASK,OUTPUT,ENCODE
    }

    public String jobId;

    public List<String> inputIds = new ArrayList();

    public List<String> taskIds = new ArrayList();

    public List<String> encodeIds = new ArrayList();

    public List<String> outputIds = new ArrayList();

    public String getJobId() {
        return jobId;
    }


    public IdConstructor() {
        this.jobId = UUID.randomUUID().toString().substring(0,16);;
    }

    public IdConstructor(String jobId){
        this.jobId = jobId;
    }

    public String getId(Integer index,IdType type){
        StringBuilder idSB = new StringBuilder();
        String id = "";
        switch (type){
            case INPUT:
                if (inputIds.size()<=index){
                    id = idSB.append(type.name()).append("_").append(index).append("_").append(this.jobId).toString();
                    inputIds.add(id);
                }else {
                    id = inputIds.get(index);
                }
                break;
            case TASK:
                if (taskIds.size()<=index){
                    id = idSB.append(type.name()).append("_").append(index).append("_").append(this.jobId).toString();
                    taskIds.add(id);
                }else {
                    id = taskIds.get(index);
                }
                break;
            case ENCODE:
                if (encodeIds.size()<=index){
                    id = idSB.append(type.name()).append("_").append(index).append("_").append(this.jobId).toString();
                    encodeIds.add(id);
                }else {
                    id = encodeIds.get(index);
                }
                break;
            case OUTPUT:
                if (outputIds.size()<=index){
                    id = idSB.append(type.name()).append("_").append(index).append("_").append(this.jobId).toString();
                    outputIds.add(id);
                }else {
                    id = outputIds.get(index);
                }
                break;
        }

        return id;
    }

}
