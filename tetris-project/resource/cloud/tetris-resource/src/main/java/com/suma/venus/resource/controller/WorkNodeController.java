package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.WorkNodeService;
import com.suma.venus.resource.vo.WorkNodeVO;

@Controller
@RequestMapping("/layernode")
public class WorkNodeController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkNodeController.class);

	@Autowired
	private WorkNodeService workNodeService;
	
	@Autowired
	private ExtraInfoService extraInfoService;

	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> query(@RequestParam(name = "type", required=false) String type, @RequestParam(name = "keyword", required=false) String keyword) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<WorkNodePO> poList = workNodeService.findAll();
			if(type == null){
				type = "";
			}
			if (!type.isEmpty()) {
				poList.retainAll(workNodeService.findByType(NodeType.fromString(type)));
			}

			if(keyword == null){
				keyword ="";
			}
			if (!keyword.isEmpty()) {
				poList.retainAll(workNodeService.findByNameLike(keyword));
			}

			List<WorkNodeVO> voList = new LinkedList<WorkNodeVO>();
			for (WorkNodePO po : poList) {
				voList.add(WorkNodeVO.fromPO(po));
			}
			data.put("nodes", voList);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	@RequestMapping("/getNode")
	@ResponseBody
	public Map<String, Object> getNodeById(@RequestParam(name = "id") Long id) {
		Map<String, Object> data = makeAjaxData();
		try {
			WorkNodePO po = workNodeService.get(id);
			WorkNodeVO vo = WorkNodeVO.fromPO(po);
			data.put("node", vo);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> save(@RequestParam(name = "json") String json,
			String extraInfoVOList) {
		Map<String, Object> data = makeAjaxData();
		LOGGER.info("WorkNodeController save json=" + json);

		try {
			WorkNodeVO vo = JSONObject.parseObject(json, WorkNodeVO.class);
			
			List<ExtraInfoPO> extraInfos = JSONArray.parseArray(extraInfoVOList, ExtraInfoPO.class);
			
			if (null != vo.getNodeUid()) {// 校验nodeUid是否重复
				WorkNodePO checkNode = workNodeService.findByNodeUid(vo.getNodeUid());
				if (null != checkNode && !checkNode.getId().equals(vo.getId())) {
					data.put(ERRMSG, "层节点ID " + vo.getNodeUid() + "已存在");
					return data;
				}
			}
			//联网接入只能有一个校验
			if(NodeType.ACCESS_LIANWANG.toString().equals(vo.getType())){
				List<WorkNodePO> lianwangs = workNodeService.findByType(NodeType.ACCESS_LIANWANG);
				if (null != lianwangs && lianwangs.size() > 0) {
					data.put(ERRMSG, "联网接入已存在");
					return data;
				}
			}
			WorkNodePO po = vo.toPO();
			workNodeService.save(po);
			if (extraInfos!=null && extraInfos.size()>0) {
				for (ExtraInfoPO extraInfo : extraInfos) {
					extraInfo.setWorknodeId(po.getNodeUid());
					extraInfoService.save(extraInfo);
				}
			}
//            if(null == po.getNodeUid() || po.getNodeUid().isEmpty()){
//            	po.setNodeUid("suma-venus-access-" + po.getId());
//            	workNodeService.save(po);
//            }
			data.put("nodeUid", po.getNodeUid());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Map<String, Object> update(@RequestParam(name = "json") String json,String extraInfoVOList) {
		Map<String, Object> data = makeAjaxData();
		LOGGER.info("WorkNodeController save json=" + json);

		try {
			WorkNodeVO vo = JSONObject.parseObject(json, WorkNodeVO.class);
			if (null != vo.getNodeUid()) {// 校验nodeUid是否重复
				WorkNodePO checkNode = workNodeService.findByNodeUid(vo.getNodeUid());
				if (null != checkNode && !checkNode.getId().equals(vo.getId())) {
					data.put(ERRMSG, "层节点ID " + vo.getNodeUid() + "已存在");
					return data;
				}
			}
			WorkNodePO po = vo.toPO();
			workNodeService.save(po);
//            if(null == po.getNodeUid() || po.getNodeUid().isEmpty()){
//            	po.setNodeUid("suma-venus-access-" + po.getId());
//            	workNodeService.save(po);
//            }
			
			// 删除旧数据
			extraInfoService.deleteByWorknodeId(po.getId().toString());

				// 添加新数据
			List<ExtraInfoPO> newData = JSONArray.parseArray(extraInfoVOList, ExtraInfoPO.class);
			if (null != newData) {
				for (ExtraInfoPO extraInfoPO : newData) {
					extraInfoPO.setWorknodeId(po.getNodeUid());
					extraInfoService.save(extraInfoPO);
				}
			}
			data.put("nodeUid", po.getNodeUid());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam(name = "nodeIds") String nodeIds) {
		Map<String, Object> data = makeAjaxData();
		try {
			String[] ids = nodeIds.split(",");
			for (String id : ids) {
				WorkNodePO node = workNodeService.get(Long.valueOf(id));
				workNodeService.delete(node);
				// 通知接入层节点
//                interfaceFromResource.delLayerNotify(node);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/queryExtraInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> detail(String worknodeId) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<ExtraInfoPO> extraInfos = extraInfoService.findByWorknodeId(worknodeId);
			data.put("extraInfos", extraInfos);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	
	/**
	 * 查询接入层节点<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月15日 上午8:42:33
	 * @return workNodeVO
	 */
	@RequestMapping(value = "/load")
	@ResponseBody
	public Map<String,Object> load(){
		Map<String, Object> data = makeAjaxData();
		try {
			List<WorkNodePO> workNodePOs = workNodeService.findAll();
			List<WorkNodeVO> workNodeVOs = new ArrayList<WorkNodeVO>();
			for (WorkNodePO workNodePO : workNodePOs) {
				WorkNodeVO workNodeVO = new WorkNodeVO();
				workNodeVO.setName(workNodePO.getName());
				workNodeVO.setNodeUid(workNodePO.getNodeUid());
				workNodeVO.setIp(workNodePO.getIp());
				workNodeVOs.add(workNodeVO);
			}
			data.put("rows", workNodeVOs);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}
		
		return data;
	}
}
