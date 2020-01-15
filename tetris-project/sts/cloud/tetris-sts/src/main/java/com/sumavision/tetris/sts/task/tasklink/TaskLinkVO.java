package com.sumavision.tetris.sts.task.tasklink;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.common.SpringBeanFactory;
import com.sumavision.tetris.sts.common.TotalOptControl;
import com.sumavision.tetris.sts.device.DeviceNodeDao;
import com.sumavision.tetris.sts.device.DeviceNodePO;
import com.sumavision.tetris.sts.device.StreamAccConfigDao;
import com.sumavision.tetris.sts.device.StreamAccConfigPO;
import com.sumavision.tetris.sts.netgroup.NetCardInfoDao;
import com.sumavision.tetris.sts.task.OutputBO;
import com.sumavision.tetris.sts.task.TaskParamVO;
import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.source.SourceDao;
import com.sumavision.tetris.sts.task.source.SourcePO;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO.TaskLinkStatus;
import com.sumavision.tetris.sts.task.util.CommonTaskUtil;

/**
 * 任务管理页面呈现
 * @author gaofeng
 *
 */
public class TaskLinkVO {
	
	static Logger logger = LogManager.getLogger(TaskLinkVO.class);

	private String linkName;
	
	// taskLinkPO ID 
    private Long id;
	
	private TaskLinkStatus linkStatus;
	
	/**
	 * 是否有告警标识
	 */
	private Boolean alarmFlag = false;
	
	/**
	 * 所属任务分组TaskGroupPO的id
	 */
	private Long taskGroupId;
	/**
	 * 任务链创建时间
	 */
	private Date addTime;
	/**
	 * 所用节目源的id
	 */
	private Long sourceId;
	
	private Long programId;
	/**
	 * 节目号
	 */
	private Integer programNum;
	
	private String sourceName;
	
	private String programName;
	
	private String sourceUrl;
	
	private Boolean optFlag = false;
	
	/**
	 * 当前工作源的信息
	 */
	private Long workSourceId;
	private Integer workProgramNum;
	private String workSourceName;
	private String workProgramName;
	
	/**
	 * 任务类型，是否转码
	 * 0：不转码
	 * 1：转码
	 */
	private Integer taskType;
	
	private Long deviceGroupId;
	
	private String errInfo;
    
    private List<OutputVO> outputList = new ArrayList<OutputVO>();
    
    private List<List<String>> linkDetail = new ArrayList<List<String>>();
    
    private String blackWhiteListType;
    
    private String ipRange;
    
    private Boolean useInpub;
    
    private Integer nCardIndex;

    private String taskDeviceIp;

    private String coverPalyerDetail;

	public TaskLinkVO(TaskLinkPO taskLinkPO){
		setId(taskLinkPO.getId());
		setLinkName(taskLinkPO.getLinkName());
		setLinkStatus(taskLinkPO.getLinkStatus()==null?TaskLinkStatus.RUN:taskLinkPO.getLinkStatus());
		setAlarmFlag(taskLinkPO.getAlarmFlag());
		setTaskGroupId(taskLinkPO.getTaskGroupId());
		setAddTime(taskLinkPO.getAddTime());
		setSourceId(taskLinkPO.getSourceId());
		setProgramNum(taskLinkPO.getProgramNum());
		setSourceName(taskLinkPO.getSourceName());
		setProgramName(taskLinkPO.getProgramName());
		setTaskType(taskLinkPO.getTaskType());
		setDeviceGroupId(taskLinkPO.getDeviceGroupId());
		setErrInfo(taskLinkPO.getErrInfo());
		setWorkSourceId(taskLinkPO.getWorkSourceId());
		setWorkSourceName(taskLinkPO.getWorkSourceName());
		setWorkProgramNum(taskLinkPO.getWorkProgramNum());
		setWorkProgramName(taskLinkPO.getWorkProgramName());
		setUseInpub(taskLinkPO.getUseInpub());
		setnCardIndex(taskLinkPO.getnCardIndex());
		setCoverPalyerDetail(taskLinkPO.getCoverPalyerDetail());

		DeviceNodeDao deviceNodeDao = SpringBeanFactory.getBean(DeviceNodeDao.class);
		DeviceNodePO deviceNodePO = deviceNodeDao.findOne(taskLinkPO.getCurDeviceNodeId());
		if (deviceNodePO != null) {
			setTaskDeviceIp(deviceNodePO.getDeviceIp());
		}

		StreamAccConfigDao streamAccConfigDao = SpringBeanFactory.getBean(StreamAccConfigDao.class);
		if (null!=taskLinkPO.getStreamAccConfigId()) {
			StreamAccConfigPO streamAccConfigPO = streamAccConfigDao.findOne(taskLinkPO.getStreamAccConfigId());
			setBlackWhiteListType(streamAccConfigPO.getAccType());
			setIpRange(streamAccConfigPO.getFilterJson());
		}
		
		if(TotalOptControl.getOperatingTaskIds().get(taskLinkPO.getId()) != null){
			setOptFlag(true);
		}
		try {
			NetCardInfoDao netCardInfoDao = SpringBeanFactory.getBean(NetCardInfoDao.class);
			SourceDao sourceDao = SpringBeanFactory.getBean(SourceDao.class);
			SourcePO sourcePO = sourceDao.findOne(taskLinkPO.getSourceId());
			setSourceUrl(sourcePO.getSourceUrl());
			
			ProgramPO programPO = CommonTaskUtil.getProgramPOByNum(taskLinkPO.getProgramNum(), sourcePO.getProgramPOs());
			if(programPO ==null){
				setProgramId(0L);
			}else{
				setProgramId(programPO.getId());
			}
//			TaskLinkNodeInfoService taskLinkNodeInfoService = SpringBeanFactory.getBean(TaskLinkNodeInfoService.class);
//			TaskParamVO taskParamVO = JSONObject.parseObject(taskLinkPO.getTaskParamDetail(), TaskParamVO.class);
//			for(OutputBO outputBO : taskParamVO.getOutputs()){
//				OutputVO outputVO = new OutputVO();
//				if(outputBO.getIp() != null && outputBO.getPort() != null){
//					outputVO.setOutput(outputBO.getIp()+":"+outputBO.getPort());
//				}else{
//					outputVO.setOutput("----");
//				}
//				//hls比较特殊，本地发布的时候，bo里面是没有ip信息的。 没办法，本地发布起初设计就被砍了，现在强行加，只能委屈代码了
//				TaskLinkNodeInfoDao taskLinkNodeInfoDao = SpringBeanFactory.getBean(TaskLinkNodeInfoDao.class);
//				List<Long> nodeIds = taskLinkNodeInfoDao.findNodeIdByTaskLinkIdAndUnitTypeAndNodeType(taskLinkPO.getId(),CommonConstants.ENCAPSULATE, TaskLinkNodeInfoPO.NodeType.OUTPUT,true);
//				if((outputBO.getType().equals(ProtoType.HLS)||outputBO.getType().equals(ProtoType.DASH)||outputBO.getType().equals(ProtoType.HDS)||outputBO.getType().equals(ProtoType.MSS)||outputBO.getType().equals(ProtoType.TSSRT)) && nodeIds.size() > 0){
//					OutputDao outputDao = SpringBeanFactory.getBean(OutputDao.class);
//					for (Long nodeId:nodeIds){
//						OutputPO outputPO = outputDao.findTopByNodeId(nodeId);
//						if (outputPO.getPubName()!=null && outputPO.getPubName().equals(outputBO.getPubName())){
//							outputVO.setUrl(outputBO.getOutputUrl(outputPO.getLocalIp()));
//							break;
//						}
//					}
//				}else{
//					outputVO.setUrl(outputBO.getOutputUrl());
//				}
//				outputList.add(outputVO);
//			}
//			if(!TaskLinkStatus.RUN.equals(taskLinkPO.getLinkStatus())){
//				getLinkDetail().addAll(taskLinkNodeInfoService.getTaskLinkDetail(taskLinkPO.getId()));
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("get TaskLinkVO err, tasklink : " + taskLinkPO.getId(),e);
		}
	}

	
	public Integer getTaskType() {
		return taskType;
	}


	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}


	public String getErrInfo() {
		return errInfo;
	}


	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}


	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public TaskLinkStatus getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(TaskLinkStatus linkStatus) {
		this.linkStatus = linkStatus;
	}

	public Long getTaskGroupId() {
		return taskGroupId;
	}

	public void setTaskGroupId(Long taskGroupId) {
		this.taskGroupId = taskGroupId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getProgramNum() {
		return programNum;
	}

	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	
	

	public List<OutputVO> getOutputList() {
		return outputList;
	}

	public void setOutputList(List<OutputVO> outputList) {
		this.outputList = outputList;
	}

	public Boolean getAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(Boolean alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public List<List<String>> getLinkDetail() {
		return linkDetail;
	}

	public void setLinkDetail(List<List<String>> linkDetail) {
		this.linkDetail = linkDetail;
	}

	public Long getWorkSourceId() {
		return workSourceId;
	}

	public void setWorkSourceId(Long workSourceId) {
		this.workSourceId = workSourceId;
	}

	public Integer getWorkProgramNum() {
		return workProgramNum;
	}

	public void setWorkProgramNum(Integer workProgramNum) {
		this.workProgramNum = workProgramNum;
	}

	public String getWorkSourceName() {
		return workSourceName;
	}

	public void setWorkSourceName(String workSourceName) {
		this.workSourceName = workSourceName;
	}

	public String getWorkProgramName() {
		return workProgramName;
	}

	public void setWorkProgramName(String workProgramName) {
		this.workProgramName = workProgramName;
	}

	public Boolean getOptFlag() {
		return optFlag;
	}

	public void setOptFlag(Boolean optFlag) {
		this.optFlag = optFlag;
	}

	public String getBlackWhiteListType() {
		return blackWhiteListType;
	}

	public void setBlackWhiteListType(String blackWhiteListType) {
		this.blackWhiteListType = blackWhiteListType;
	}

	public String getIpRange() {
		return ipRange;
	}

	public void setIpRange(String ipRange) {
		this.ipRange = ipRange;
	}

	public Boolean getUseInpub() {
		return useInpub;
	}

	public void setUseInpub(Boolean useInpub) {
		this.useInpub = useInpub;
	}

	public Integer getnCardIndex() {
		return nCardIndex;
	}

	public void setnCardIndex(Integer nCardIndex) {
		this.nCardIndex = nCardIndex;
	}

	public Long getDeviceGroupId() {
		return deviceGroupId;
	}


	public void setDeviceGroupId(Long deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	public String getTaskDeviceIp() {
		return taskDeviceIp;
	}

	public void setTaskDeviceIp(String taskDeviceIp) {
		this.taskDeviceIp = taskDeviceIp;
	}

	public String getCoverPalyerDetail() {
		return coverPalyerDetail;
	}

	public void setCoverPalyerDetail(String coverPalyerDetail) {
		this.coverPalyerDetail = coverPalyerDetail;
	}

}
