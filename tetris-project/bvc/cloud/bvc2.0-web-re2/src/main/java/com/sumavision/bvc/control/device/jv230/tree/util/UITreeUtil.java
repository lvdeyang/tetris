package com.sumavision.bvc.control.device.jv230.tree.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.jv230.tree.handler.QueryHandler;
import com.sumavision.bvc.control.device.jv230.tree.vo.UINodeVO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBuilderWrapper;

/**
 * 树形结构生成工具 
 * lvdeyang 2017年4月26日
 */
public class UITreeUtil {
	
	//channelId
	public static final String VIDEO_CHANNEL_ID = "VenusVideoIn_1";
	
	public static final String AUDIO_CHANNEL_ID = "VenusAudioIn_1";

	//获取树形节点的类型
	public static final String LEVELTYPE_SOURCE = "source";
	public static final String LEVELTYPE_INST = "inst";
	public static final String TREENODE_ROOT = "root";
	public static final String TREENODE_INST = "inst";
	public static final String TREENODE_DEVICE = "device";
	
	//从根节点获取树，指定树的深度（最高两级）
	public static UINodeVO getTree(String openLevel, QueryHandler firstLevelQuery, QueryHandler secondLevelQuery, boolean draggable, boolean checkable, Long accountId) throws Exception{
		//创建根节点
		UINodeVO rootNode = UITreeUtil.getNodeList(null, false, draggable, checkable).get(0);
		if(openLevel != null){
			int level = Integer.parseInt(openLevel);
			if(level > 2) level = 2;
			for(int i=1; i<=level; i++){
				if(i == 1){
					rootNode.setChildrenList(UITreeUtil.getNodeList(firstLevelQuery, true, draggable, checkable));
				}else if(i == 2){
					List<UINodeVO> nodeList = rootNode.getChildrenList();
					if(nodeList!=null && nodeList.size()>0){
						for(UINodeVO node:nodeList){
							String[] params = node.getParam().split(UINodeVO.SPLITER);
							if(LEVELTYPE_INST.equals(params[0])){
								Long id = Long.valueOf(params[1]);
								JSONObject json = new JSONObject();
								json.put("instId", id);
								json.put("accountId", accountId);
								secondLevelQuery.setParams(json);
								node.setChildrenList(UITreeUtil.getNodeList(secondLevelQuery, false, draggable, checkable));
							}
						}
					}
				}
			}
		}
		return rootNode;
	}
	
	//获取树形结构
	public static List<UINodeVO> getNodeList(QueryHandler handler, boolean open, boolean draggable, boolean checkable) throws Exception{
		if(handler == null){
			UINodeVO rootNode = new UINodeVO();
			rootNode.setNodeContent("资源列表")
					.setParam(TREENODE_ROOT)
					.setType(UINodeVO.TYPE_FOLDER)
					.setIcon(TREENODE_ROOT)
					.setStatus(UINodeVO.STATUS_OPEN)
					.setDraggable(false)
					.setCheckable(checkable);
			List<UINodeVO> nodeList = new ArrayList<UINodeVO>();
			nodeList.add(rootNode);
			return nodeList;
		}else{
			return generateNode(handler.getInstitutionList(), handler.getDeviceList(), open, draggable, checkable);
		}
	}
	
	/***************************
	 * 		生成树节点（新树）
	 **************************/
	
	//生成一组node
	public static List<UINodeVO> generateNode(List<FolderPO> instList, List<BundlePO> deviceList, boolean open, boolean draggable, boolean checkable) {
		List<UINodeVO> nodeList = new ArrayList<UINodeVO>();
		
		if(deviceList!=null && deviceList.size()>0){
			for(BundlePO device:deviceList){
				nodeList.add(generateNode(device, false, draggable, checkable));
			}
		}
		
		if(instList!=null && instList.size()>0){
			for(FolderPO inst:instList){
				nodeList.add(generateNode(inst, open, false, checkable));
			}
		}
		
		return nodeList;
	}
	
	//生成一个node
	public static UINodeVO generateNode(Object target, boolean open, boolean draggable, boolean checkable){
		
		UINodeVO node = new UINodeVO();
		
		if(target instanceof FolderPO){
			FolderPO inst = (FolderPO) target;
			node.setNodeContent(inst.getName())
			.setParam(new StringBufferWrapper().append(TREENODE_INST)
												.append(UINodeVO.SPLITER)
												.append(inst.getId())
												.append(UINodeVO.SPLITER)
												.append(inst.getName())
												.append(UINodeVO.SPLITER)
												.append(inst.getParentId())
												.toString())
			.setType(UINodeVO.TYPE_FOLDER)
			.setIcon(TREENODE_INST)
			.setStatus(open?UINodeVO.STATUS_OPEN:UINodeVO.STATUS_CLOSE)
			.setDraggable(draggable)
			.setCheckable(checkable)
			.setChildrenList(new ArrayList<UINodeVO>());
		}else if(target instanceof BundlePO){
			BundlePO device = (BundlePO) target;
			node.setNodeContent(device.getBundleName())
			    .setParam(new StringBuilderWrapper().append(TREENODE_DEVICE)
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getBundleId())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getBundleId())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getBundleName())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getFolderId())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getBundleName())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getDeviceModel())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getAccessNodeUid())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getBundleId())
					                                .append(UINodeVO.SPLITER)
					                                .append(device.getBundleType())
					                                .append(UINodeVO.SPLITER)
					                                .append(VIDEO_CHANNEL_ID)
					                                .append(UINodeVO.SPLITER)
					                                .append(AUDIO_CHANNEL_ID)
					                                .toString())
				.setType(UINodeVO.TYPE_FILE)
				.setIcon(new StringBuilderWrapper().append(TREENODE_DEVICE)
												   .toString())
				.setStatus(UINodeVO.STATUS_ONLINE)
				.setDraggable(draggable)
				.setCheckable(checkable);
		}
		return node;
	}
}
