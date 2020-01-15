package com.sumavision.tetris.sts.task.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sumavision.tetris.sts.common.SpringBeanFactory;
import com.sumavision.tetris.sts.exeception.CommonException;
import com.sumavision.tetris.sts.exeception.ErrorCodes;
import com.sumavision.tetris.sts.exeception.ExceptionI18Message;
import com.sumavision.tetris.sts.node.CmdNode;
import com.sumavision.tetris.sts.task.source.AudioParamPO;
import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.source.VideoElement;
import com.sumavision.tetris.sts.task.source.VideoParamPO;
import com.sumavision.tetris.sts.task.tasklink.TransTaskPO;

public class CommonTaskUtil {
	/**
     * 根据节目号从节目list获取节目
     * @param programNum
     * @param programPOs
     * @return
     * @throws CommonException
     */
    public static ProgramPO getProgramPOByNum(Integer programNum,List<ProgramPO> programPOs){
    	for(ProgramPO programPO : programPOs){
    		if(programPO.getProgramNum().intValue() == programNum){
    			return programPO;
    		}
    	}
    	return null;
    }
    
    /**
     * 根据videoPid查找响应video参数
     * @param videoPid
     * @param programPO
     * @return
     * @throws CommonException
     */
    public static VideoElement getVideoElementByPid(Integer videoPid, ProgramPO programPO) throws CommonException{
    	ExceptionI18Message exceptionI18Message = SpringBeanFactory.getBean(ExceptionI18Message.class);
    	for(VideoElement videoElement :programPO.getVideoElements()){
    		if(videoElement.getPid().equals(videoPid)){
    			return videoElement;
    		}
    	}
    	throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.TASK_VIDEO_PARAM_NOT_FOUND));
    }
    
    /**
     * 将节点加入对应的map队列中
     * @param funUnitId
     * @param cmdNode
     * @param optCmdMap
     */
    public static void addNodeInMap(String socketAddress,CmdNode cmdNode,Map<String, List<CmdNode>> optCmdMap){
    	List<CmdNode> cmdList = optCmdMap.get(socketAddress);
    	if(cmdList == null){
    		cmdList = new ArrayList<CmdNode>();
    		optCmdMap.put(socketAddress, cmdList);
    	}
    	cmdList.add(cmdNode);
    }
    public static void addNodeListInMap(String sokcetAddress,List<CmdNode> addCmdList,Map<String, List<CmdNode>> optCmdMap){
    	List<CmdNode> cmdList = optCmdMap.get(sokcetAddress);
    	if(cmdList == null){
    		cmdList = new ArrayList<CmdNode>();
    		optCmdMap.put(sokcetAddress, cmdList);
    	}
    	cmdList.addAll(addCmdList);
    }
    
    
    /**
     * 计算内部转发的时候，转码输出的码率，目前规则是 ∑视频码率+∑音频码率，最后再增加0.1倍
     * @param transTaskPO
     * @return
     */
    public static Long calculateSysRate(TransTaskPO transTaskPO){
    	Long sysRate = transTaskPO.getVideoParams().stream().collect(Collectors.summingLong(VideoParamPO::getBitrate));
    	sysRate += transTaskPO.getAudioParams().stream().collect(Collectors.summingLong(AudioParamPO::getBitrate));
    	return sysRate + sysRate / 10;
    }
}
