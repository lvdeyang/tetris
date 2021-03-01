package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/12/9 13:53
 */

import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.input.ProgramVideoBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: TemplateUtil
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/9 13:53
 */
public class TemplateUtil {

    private static TemplateUtil instance = new TemplateUtil();

    private TemplateUtil(){ }

    public static TemplateUtil getInstance(){
        return instance;
    }

    public InputBO getTaskInputBO(List<InputBO> inputBOS) throws BaseException {
        if (inputBOS==null || inputBOS.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"fail to get taskInput");
        }
        InputBO inputBO = inputBOS.get(0);
        for (int i = 0; i < inputBOS.size(); i++) {
            InputBO curInputBO = inputBOS.get(i);
            if (curInputBO.getSchedule()!=null) {
                inputBO = curInputBO;
            }
            if (curInputBO.getBack_up_raw()!=null){
                inputBO = curInputBO;
            }
            if (curInputBO.getBack_up_es()!=null){
                inputBO = curInputBO;
            }
            if (curInputBO.getBack_up_passby()!=null){
                inputBO = curInputBO;
            }
        }

        return inputBO;
    }

    public String getTaskInputElementType(List<InputBO> inputBOS, Integer pid) throws BaseException {
        InputBO inputBO = getTaskInputBO(inputBOS);
        String type = "video";
        for (int i = 0; i < inputBO.getProgram_array().size(); i++) {
            ProgramBO programBO = inputBO.getProgram_array().get(i);
            if(!CollectionUtils.isEmpty(programBO.getVideo_array()) && programBO.getVideo_array().stream().anyMatch(v -> v.getPid().equals(pid))){
                type = "video";
                break;
            }
            if(!CollectionUtils.isEmpty(programBO.getAudio_array()) && programBO.getAudio_array().stream().anyMatch(v -> v.getPid().equals(pid))){
                type = "audio";
                break;
            }
            if(!CollectionUtils.isEmpty(programBO.getSubtitle_array()) && programBO.getSubtitle_array().stream().anyMatch(v -> v.getPid().equals(pid))){
                type = "subtitle";
                break;
            }
        }
        return type;
    }

}
