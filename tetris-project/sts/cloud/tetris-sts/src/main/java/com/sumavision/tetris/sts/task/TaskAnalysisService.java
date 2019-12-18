package com.sumavision.tetris.sts.task;



import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.*;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.source.SourceDao;
import com.sumavision.tetris.sts.task.source.SourcePO;
import com.sumavision.tetris.sts.task.source.SourcePO.SourceType;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkDao;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO.TaskLinkStatus;

@Component
public class TaskAnalysisService {
	static Logger logger = LogManager.getLogger(TaskAnalysisService.class);
	
	@Autowired
	private SourceDao sourceDao;
	
	@Autowired
	private TaskLinkDao taskLinkDao;
	
	public void analysisAddTask(JSONObject jsonObject) throws Exception{
		//服务端参数校验
		//checkTaskObjAtServer(jsonObject);
		
		Long taskLinkId = jsonObject.getLong("id");
		if (taskLinkId == null || taskLinkId == 0) {
			// 添加任务
			TaskLinkPO taskLinkPO = new TaskLinkPO();
			taskLinkPO.generateFromJson(jsonObject);
			taskLinkPO.setAddTime(new Date());
			taskLinkPO.setPreview(0);
			SourcePO sourcePO = sourceDao.findOne(taskLinkPO.getSourceId());
			if(SourceType.PASSBY.equals(sourcePO.getSourceType())){
				taskLinkPO.setProgramName("ts透传");
				taskLinkPO.setWorkProgramName("ts透传");
				taskLinkPO.setWorkProgramNum(0);
			}else{
				ProgramPO programPO = getProgramPOByNum(taskLinkPO.getProgramNum(), sourcePO.getProgramPOs());
				taskLinkPO.setProgramName(programPO.getProgramName());
				taskLinkPO.setWorkProgramName(programPO.getProgramName());
				taskLinkPO.setWorkProgramNum(programPO.getProgramNum());	
			}
			taskLinkPO.setSourceName(sourcePO.getSourceName());
			taskLinkPO.setWorkSourceName(sourcePO.getSourceName());
			taskLinkPO.setWorkSourceId(sourcePO.getId());

			TaskLinkStatus linkStatus = null;
			try {
				linkStatus = jsonObject.getObject("status", TaskLinkStatus.class);
			}catch (JSONException e){
				logger.error(e.getMessage(),e.getCause());
				throw new BaseException(StatusCode.ERROR, "任务状态错误！");
			}
			
			

			taskLinkDao.save(taskLinkPO);
		}
	}
	
	private ProgramPO getProgramPOByNum(Integer programNum,List<ProgramPO> programPOs){
    	for(ProgramPO programPO : programPOs){
    		if(programPO.getProgramNum().intValue() == programNum){
    			return programPO;
    		}
    	}
    	return null;
    }
//	public void checkTaskObjAtServer(JSONObject jsonObject) throws CommonException {
//		Integer taskType = APIUtil.getDocParam(jsonObject,"taskType",Integer.class);
//		if (taskType<0 || taskType>1){
//			throw new CommonException("taskType error");
//		}
//		Long deviceNode = APIUtil.getDocParam(jsonObject,"deviceNode",Long.class);
//		DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceNode);
//		if (deviceNodePO == null) {
//			throw new CommonException("device node not exists");
//		}
//		Integer nCardIndex = APIUtil.getDocParam(jsonObject,"nCardIndex",Integer.class);
//		if (nCardIndex<-1 || nCardIndex >= deviceNodePO.getnCardNum()){
//			throw new CommonException("nCardIndex should greater than -1 and less than the card count of device");
//		}
//		JSONObject taskParam = APIUtil.getDocParam(jsonObject,"taskParam",JSONObject.class);
//		JSONArray outputs = APIUtil.getDocParam(taskParam,"outputs",JSONArray.class);
//		Iterator outIter=outputs.iterator();
//		while (outIter.hasNext()){
//			JSONObject output = (JSONObject) outIter.next();
//			if (output.containsKey("ip")){
//				String ip = output.getString("ip");
//				Pattern pattern = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
//				Matcher matcher = pattern.matcher(ip);
//				if (!matcher.matches()) {
//                    logger.error(MessageFormat.format("some output ip format {0} error",ip));
//					throw new CommonException(MessageFormat.format("some output ip format {0} error",ip));
//				}
//			}
//			if (output.containsKey("port")){
//				Integer port = output.getInteger("port");
//				if (port > 65535 || port < 100){
//                    logger.error(MessageFormat.format("some output port {0} error",port));
//					throw new CommonException(MessageFormat.format("some output port {0} error",port));
//				}
//			}
//			if (output.containsKey("type")){
//			    try {
//			        output.getObject("type", CommonConstants.ProtoType.class);
//                }catch (JSONException e){
//			        logger.error(e.getMessage(),e.getCause());
//			        throw new CommonException("some output type error");
//                }
//            }
//		}
//
//	}

}
