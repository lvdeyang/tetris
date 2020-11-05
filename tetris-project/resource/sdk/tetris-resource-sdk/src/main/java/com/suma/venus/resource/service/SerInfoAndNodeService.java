package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.bo.NodeRelationBO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.lianwang.status.NotifyRouteLinkXml;
import com.suma.venus.resource.lianwang.status.StatusXMLUtil;
import com.suma.venus.resource.pojo.SerNodePO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class SerInfoAndNodeService {
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private StatusXMLUtil statusXMLUtil;

	/**
	 * 同步路由信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 上午10:47:35
	 * @param NotifyRouteLinkXml xmlBean 路由信息协议
	 */
	public void updateSerNode(NotifyRouteLinkXml xmlBean) throws Exception{
		
		List<String> nodeList = xmlBean.getNlist();
		
		List<SerNodePO> serNodePOs = serNodeDao.findByNodeUuidIn(nodeList);
		
		//同步全量
		if(serNodePOs != null && serNodePOs.size() > 0){
			
			if(xmlBean.getMattype().equals("raw")){
				
				String[] mat = xmlBean.getMatcontent().split(",");
				
				for(int i=0; i<nodeList.size(); i++){
					SerNodePO node = queryNode(serNodePOs, nodeList.get(i));
					String[] args = mat[i].split("");
					List<String> relations = new ArrayList<String>();
					
					for(int j=0; j<args.length; j++){
						if(j == i) continue;
						if(args[j].equals("1")){
							relations.add(nodeList.get(j));
						}
					}
					
					node.setNodeRouter(JSON.toJSONString(relations));
					
				}
			}
			
			if(xmlBean.getMattype().equals("tight")){
				
				String mat = statusXMLUtil.runLengthDecoding(xmlBean.getMatcontent());
				
				List<NodeRelationBO> relations = new ArrayList<NodeRelationBO>();
				
				for(int i=0; i<nodeList.size()-1; i++){
					for(int j=i+1; j<nodeList.size(); j++){
						NodeRelationBO relation = new NodeRelationBO();
						relation.setNodeUuid1(nodeList.get(i));
						relation.setNodeUuid2(nodeList.get(j));
						relation.setConnected(false);
						relations.add(relation);
					}
				}
				
				String[] mats = mat.split("");
				
				for(int i=0; i<mats.length; i++){
					if(mats[i].equals("1")){
						relations.get(i).setConnected(true);
					}
				}
				
				for(int i=0; i<nodeList.size()-1; i++){
					
					SerNodePO node = queryNode(serNodePOs, nodeList.get(i));
					List<String> routers = generateRelations(nodeList.get(i), relations);
					node.setNodeRouter(JSON.toJSONString(routers));
				}
			}
			
			serNodeDao.save(serNodePOs);
		}
		
	}

	/**
	 * 根据nodeUuid查询node<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 上午11:00:46
	 * @param List<SerNodePO> serNodePOs
	 * @param String nodeUuid 
	 * @return SerNodePO
	 */
	public SerNodePO queryNode(List<SerNodePO> serNodePOs, String nodeUuid) throws Exception{
		
		for(SerNodePO node: serNodePOs){
			if(node.getNodeUuid().equals(nodeUuid)){
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * 生成关联关系<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午4:25:17
	 * @param String nodeUuid 当前节点
	 * @param List<NodeRelationBO> relations 路由关系
	 * @return List<String> 当前节点路由信息
	 */ 
	public List<String> generateRelations(String nodeUuid, List<NodeRelationBO> relations) throws Exception{
		
		List<String> uuids = new ArrayList<String>();
		for(NodeRelationBO relation: relations){
			if(relation.isConnected()){
				if(relation.getNodeUuid1().equals(nodeUuid)){
					uuids.add(relation.getNodeUuid2());
				}
				if(relation.getNodeUuid2().equals(nodeUuid)){
					uuids.add(relation.getNodeUuid1());
				}
			}
		}
		
		return uuids;
	}
	
	public String tranfer2String(String[] args) throws Exception{
		
		StringBufferWrapper sb = new StringBufferWrapper();
		for(int i=0; i<args.length; i++){
			if(i<args.length-1){
				sb.append(args[i]).append(",");
			}else{
				sb.append(args[i]);
			}
		}
		
		return sb.toString();
	}
	
}
