package com.sumavision.tetris.sts.task;

import javax.servlet.http.HttpServletRequest;






//import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.sts.common.CommonController;
import com.sumavision.tetris.sts.device.ChannelVideoTypeDao;
import com.sumavision.tetris.sts.device.ChannelVideoTypePO;
import com.sumavision.tetris.sts.task.source.SourceVO;
import com.sumavision.tetris.sts.task.tasklink.TaskLinkPO.TaskLinkStatus;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping(value = "task")
public class TaskManagerController extends CommonController {
	
	@Autowired
	TaskAnalysisService taskAnalysisService;

	@Autowired
	ChannelVideoTypeDao channelVideoTypeDao;

//	@Autowired
//	CapacityService capacityService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addTask", method = RequestMethod.POST)
	public Object addTask(String obj)  throws Exception{
		System.out.println(obj);
//		JSONObject addObj = taskAnalysisService.analysisAddTask(JSONObject.parseObject(obj));
//		System.out.println(addObj.toJSONString());
//		capacityService.addTranscode(addObj.toJSONString());
		return null;
	}
	
	@JsonBody
	@RequestMapping(value = "/taskManage/getTasks")
	@ResponseBody
	public Object getTasksByKeywords(@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "pageNum") Integer pageNum,
			@RequestParam(value = "keyWord") String keyWord,
			@RequestParam(value = "taskGroupId") Long taskGroupId,
			@RequestParam(value = "deviceNodeId") Long deviceNodeId,
			@RequestParam(value = "sdmDeviceId") Long sdmDeviceId,
			@RequestParam(value = "alarmFlag") Boolean alarmFlag,
			@RequestParam(value = "linkStatus") TaskLinkStatus linkStatus){
// 		Map<String, Object> data = makeAjaxData();
// 		List<TaskLinkVO> taskVoList = new ArrayList<TaskLinkVO>();
//		try {
//			List<Long> taskGroupIds;
//			if(taskGroupId == 0){
//				taskGroupIds = taskGroupDao.findAllId();
//			}else{
//				taskGroupIds = new ArrayList<Long>();
//				taskGroupIds.add(taskGroupId);
//			}
//			List<Long> deviceIds = new ArrayList<Long>();
//			if(deviceNodeId == 0){
//				deviceIds.addAll(deviceNodeDao.findAllIds());
//				//设备已删除的任务，所属设备id为0
//				deviceIds.add(0l);
//			}else{
//				deviceIds.add(deviceNodeId);
//			}
//			List<Long> deviceGroupIds = new ArrayList<>();
// 			if (sdmDeviceId == 0){
//				deviceGroupIds.addAll(deviceGroupDao.findAllIds());
//			}else{
// 				deviceGroupIds.add(sdmDeviceId);
//			}
//			
//
//			Page<TaskLinkPO> page = tasklinkDaoService.getTasklinkByKeyWords(keyWord, taskGroupIds,deviceGroupIds, deviceIds,pageNum, pageSize,alarmFlag,linkStatus);
//			if (null == page) {
//				data.put("taskVoList", taskVoList);
//				data.put("totalNum", 0);
//				data.put("getTotalElements", 0);
//			}else {
//				page.getContent().stream().forEach(task -> {
//					taskVoList.add(new TaskLinkVO(task));
//				});
//				data.put("taskVoList", taskVoList);
//				data.put("totalNum", page.getTotalPages());
//				data.put("getTotalElements", page.getTotalElements());
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error("getSourceKeywords err",e);
//			data.put(ERRMSG, errorMsg(getRequest() , ErrorCodes.SYS_ERR));
//			//data.put(ERRMSG, "System err");
//		}
//		return data;
// 		TaskLinkPO po = new TaskLinkPO();
// 		po.setLinkName("111");
// 		po.setLinkStatus(TaskLinkStatus.RUN);
// 		po.setId(1L);
// 		taskVoList.add(new TaskLinkVO(po));
		List<taskManageVO> taskVoList = new ArrayList<taskManageVO>();
 		taskManageVO vo = new taskManageVO();
 		taskManageVO vo1 = new taskManageVO();
 		taskVoList.add(vo);
 		taskVoList.add(vo1);
		return new HashMapWrapper<String, Object>().put("total", 1)
				   .put("rows", taskVoList)
				   .getMap();
	}
	
	@JsonBody
	@RequestMapping(value = "/source/getVideoStream")
	@ResponseBody
	public Object getVideoStreamByKeywords(@RequestParam(value = "keyWord") String keyWord){
		
//		List<taskManageVO> taskVoList = new ArrayList<taskManageVO>();
// 		taskManageVO vo = new taskManageVO();
// 		taskManageVO vo1 = new taskManageVO();
// 		taskVoList.add(vo);
// 		taskVoList.add(vo1);
		
//		String reslut ="{ 'data': [ { 'id': 152, 'uuid': 'bd131038854e488883b3de176b0000fa', 'updateTime': '2019-11-03 15:55:38', 'previewUrl': [ 'udp://@192.165.56.84:1234' ], 'name': 'test2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 153, 'uuid': 'c183e4660f1f48388079b134b2efb016', 'updateTime': '2019-11-03 19:40:30', 'previewUrl': [ 'udp://@5555555555555:333333' ], 'name': 'test3', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 229, 'uuid': 'c21cb0af44864691ad9a4bd2b19c72d3', 'updateTime': '2019-11-05 14:59:34', 'previewUrl': [ 'udp://@:' ], 'name': 'ch0', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 319, 'uuid': '195cd24c96684dea9e9fc4538d917da3', 'updateTime': '2019-12-13 09:32:48', 'previewUrl': [ 'rtsp://192.165.56.85/live/livestreame' ], 'name': 'test4', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 321, 'uuid': '99c295e2f05743dc8711ea275b80488c', 'updateTime': '2019-12-13 09:42:33', 'previewUrl': [ 'rtsp://192.165.56.85/live/test', 'rtsp://192.165.56.85/live/mytest' ], 'name': 'test5', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 322, 'uuid': '04aaa281291d4cc8b7eecfe0652dd3a6', 'updateTime': '2019-12-13 11:09:33', 'previewUrl': [ 'udp://@10.10.40.125:15000' ], 'name': 'remote_udp', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 334, 'uuid': '6965ea54cfe94e5fa844ca5feb5c1cfc', 'updateTime': '2019-12-13 13:56:40', 'previewUrl': [ 'udp://@10.10.40.125:15002' ], 'name': 'remote_udp', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 339, 'uuid': '8f7965bae28b4bd09e961aea5060f917', 'updateTime': '2019-12-13 14:45:19', 'previewUrl': [ 'udp://@10.10.40.125:15001' ], 'name': 'remote_udp', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 341, 'uuid': '5e961f1af64347d982b5be90b36491a0', 'updateTime': '2019-12-13 15:01:40', 'previewUrl': [ 'udp://@10.10.40.125:1234' ], 'name': 'test4', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 342, 'uuid': '1e89115eda804014b98ab76d7a0d61d1', 'updateTime': '2019-12-16 14:24:38', 'previewUrl': [ 'rtsp://admin:suma1234@192.165.56.67:554/' ], 'name': 'rtsp_1', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 343, 'uuid': 'b8a9a85beb0d4298914d6ee231c3c4f9', 'updateTime': '2019-12-16 14:24:55', 'previewUrl': [ 'rtsp://admin:suma1234@192.165.56.219:554/' ], 'name': 'rtsp_2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null } ], 'message': '操作成功', 'status': 200 }";
		String result = " [ { 'id': 152, 'uuid': 'bd131038854e488883b3de176b0000fa', 'updateTime': '2019-11-03 15:55:38', 'previewUrl': [ 'udp://@192.165.56.84:1234' ], 'name': 'test2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 153, 'uuid': 'c183e4660f1f48388079b134b2efb016', 'updateTime': '2019-11-03 19:40:30', 'previewUrl': [ 'udp://@5555555555555:333333' ], 'name': 'test3', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 229, 'uuid': 'c21cb0af44864691ad9a4bd2b19c72d3', 'updateTime': '2019-11-05 14:59:34', 'previewUrl': [ 'udp://@:' ], 'name': 'ch0', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 319, 'uuid': '195cd24c96684dea9e9fc4538d917da3', 'updateTime': '2019-12-13 09:32:48', 'previewUrl': [ 'rtsp://192.165.56.85/live/livestreame' ], 'name': 'test4', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 321, 'uuid': '99c295e2f05743dc8711ea275b80488c', 'updateTime': '2019-12-13 09:42:33', 'previewUrl': [ 'rtsp://192.165.56.85/live/test', 'rtsp://192.165.56.85/live/mytest' ], 'name': 'test5', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 322, 'uuid': '04aaa281291d4cc8b7eecfe0652dd3a6', 'updateTime': '2019-12-13 11:09:33', 'previewUrl': [ 'udp://@10.10.40.125:15000' ], 'name': 'remote_udp', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 334, 'uuid': '6965ea54cfe94e5fa844ca5feb5c1cfc', 'updateTime': '2019-12-13 13:56:40', 'previewUrl': [ 'udp://@10.10.40.125:15002' ], 'name': 'remote_udp', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 339, 'uuid': '8f7965bae28b4bd09e961aea5060f917', 'updateTime': '2019-12-13 14:45:19', 'previewUrl': [ 'udp://@10.10.40.125:15001' ], 'name': 'remote_udp', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 341, 'uuid': '5e961f1af64347d982b5be90b36491a0', 'updateTime': '2019-12-13 15:01:40', 'previewUrl': [ 'udp://@10.10.40.125:1234' ], 'name': 'test4', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 342, 'uuid': '1e89115eda804014b98ab76d7a0d61d1', 'updateTime': '2019-12-16 14:24:38', 'previewUrl': [ 'rtsp://admin:suma1234@192.165.56.67:554/' ], 'name': 'rtsp_1', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }, { 'id': 343, 'uuid': 'b8a9a85beb0d4298914d6ee231c3c4f9', 'updateTime': '2019-12-16 14:24:55', 'previewUrl': [ 'rtsp://admin:suma1234@192.165.56.219:554/' ], 'name': 'rtsp_2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null } ]";
//		String result = "[{ 'id': 152, 'uuid': 'bd131038854e488883b3de176b0000fa', 'updateTime': '2019-11-03 15:55:38', 'previewUrl': [ 'udp://@192.165.56.84:1234' ], 'name': 'test2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null },{ 'id': 153, 'uuid': 'c183e4660f1f48388079b134b2efb016', 'updateTime': '2019-11-03 19:40:30', 'previewUrl': [ 'udp://@5555555555555:333333' ], 'name': 'test3', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }]";
//		String result = "{ 'id': 152, 'uuid': 'bd131038854e488883b3de176b0000fa', 'updateTime': '2019-11-03 15:55:38', 'previewUrl': [ 'udp://@192.165.56.84:1234' ], 'name': 'test2', 'authorName': '应急广播管理员用户', 'createTime': '', 'remarks': '', 'tags': [ '' ], 'keyWords': [ '' ], 'igmpv3Status': 'close', 'igmpv3Mode': null, 'igmpv3IpArray': null, 'type': 'VIDEO_STREAM', 'resourceType': null, 'removeable': true, 'icon': 'icon-film', 'style': 'font-size:16px; position:relative; top:1px;', 'reviewStatus': '', 'processInstanceId': null, 'addition': null, 'children': null }";
		
		JSONArray json = JSONArray.fromObject(result);
//		json.toList(json);
//		 List<Object>  list = json.toList(json);
		List<SourceVO>  list =  json.toList(json, SourceVO.class);
		return new HashMapWrapper<String, Object>().put("total", json.size())
				   .put("rows", list)
				   .getMap();
	}

}
