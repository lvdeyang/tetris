package com.suma.venus.resource.service.router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.pojo.SerNodePO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 路由相关Service<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月6日 上午10:00:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RouterService {
	
	/**
	 * 起点到终点的最优路径<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 上午9:59:27
	 * @param List<SerNodePO> nodes 所有节点信息
	 * @param SerNodePO start 起点信息
	 * @param SerNodePO end 终点信息
	 * @return String 路径（uuid,uuid,uuid）
	 */
	public String queryReasonableRouter(
			List<SerNodePO> nodes, 
			SerNodePO start, 
			SerNodePO end) throws Exception{
		
		List<RouterNodeBO> nodeBOs = new ArrayList<RouterNodeBO>();
		for(SerNodePO node: nodes){
			nodeBOs.add(generateRouterNodeBO(node, nodes));
		}
		
		RouterNodeBO startBO = generateRouterNodeBO(start, nodes);
		RouterNodeBO endBO = generateRouterNodeBO(end, nodes); 
		
		return queryReasonableRouter(nodeBOs, startBO, endBO);
	}
	
	/**
	 * nodePO生成nodeBO<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 上午9:41:41
	 * @param SerNodePO node 节点数据
	 * @param List<SerNodePO> all 所有节点信息
	 * @return RouterNodeBO 节点路由信息
	 */
	public RouterNodeBO generateRouterNodeBO(SerNodePO node, List<SerNodePO> all) throws Exception{
		
		RouterNodeBO nodeBO = new RouterNodeBO();
		nodeBO.setNodeId(node.getNodeUuid());
		nodeBO.setRelations(new ArrayList<String>());
		
		//父节点
		if(node.getNodeFather() != null && !node.getNodeFather().equals("NULL")){
			String[] fatherNodes = node.getNodeFather().split(",");
			nodeBO.getRelations().addAll(Arrays.asList(fatherNodes));
		}
		
		//关联节点
		if(node.getNodeRelations() != null && !node.getNodeRelations().equals("NULL")){
			String[] relationNodes = node.getNodeRelations().split(",");
			nodeBO.getRelations().addAll(Arrays.asList(relationNodes));
		}
		
		//子节点
		for(SerNodePO nodePO: all){
			String nodeFather = nodePO.getNodeFather();
			if(nodeFather != null && !nodeFather.equals("NULL")){
				String[] fatherNodes = nodePO.getNodeFather().split(",");
				List<String> _fathers = Arrays.asList(fatherNodes);
				if(_fathers.contains(node.getNodeUuid())){
					nodeBO.getRelations().add(nodePO.getNodeUuid());
				}
			}
		}
		
		return nodeBO;
		
	}
	
	/**
	 * 起点到终点的最优路径<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 下午4:49:14
	 * @param List<RouterNodeBO> nodes 所有节点
	 * @param RouterNodeBO start 起点
	 * @param RouterNodeBO end 终点
	 * @return String 路径信息
	 */
	public String queryReasonableRouter(
			List<RouterNodeBO> nodes, 
			RouterNodeBO start, 
			RouterNodeBO end) throws Exception{
		
		if(start.getNodeId().equals(end.getNodeId())){
			return new StringBufferWrapper().append(start.getNodeId())
											.append(",")
											.append(end.getNodeId())
											.toString();
		}
		
		if(start.getRelations().contains(end.getNodeId())){
			return new StringBufferWrapper().append(start.getNodeId())
											.append(",")
											.append(end.getNodeId())
											.toString();
		}
		
		List<RouterNodeBO> _nodes = queryNodes(nodes, start);
		for(RouterNodeBO _node: _nodes){
			_node.setRouter(new StringBufferWrapper().append(start.getNodeId())
					                                 .append(",")
					                                 .append(_node.getNodeId())
					                                 .toString());
		}
		
		RouterNodeBO nodeBO = queryReasonableRouter(nodes, _nodes, end);
		
		return nodeBO.getRouter();
		
	}

	/**
	 * 查询起点下某一个层级上的节点和终点的路径<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 下午4:43:20
	 * @param List<RouterNodeBO> nodes 所有节点
	 * @param List<RouterNodeBO> starts 某一层节点
	 * @param RouterNodeBO end 终点
	 * @return RouterNodeBO 节点路径信息，放在router里面
	 */
	public RouterNodeBO queryReasonableRouter(
			List<RouterNodeBO> nodes, 
			List<RouterNodeBO> starts, 
			RouterNodeBO end) throws Exception{
		
		if(starts == null || starts.size() <= 0){
			return new RouterNodeBO().setRouter(null);
		}
		
		for(RouterNodeBO start: starts){
			
			if(start.getRelations().contains(end.getNodeId())){
				start.setRouter(new StringBufferWrapper().append(start.getRouter())
									                     .append(",")
									                     .append(end.getNodeId())
									                     .toString());
				return start;
			}
			
		}
		
		List<RouterNodeBO> relationStarts = new ArrayList<RouterNodeBO>();
		for(RouterNodeBO start: starts){
			List<RouterNodeBO> _nodes = queryNodes(nodes, start);
			for(RouterNodeBO _node: _nodes){
				_node.setRouter(new StringBufferWrapper().append(start.getRouter())
						                                 .append(",")
						                                 .append(_node.getNodeId())
						                                 .toString());
			}
			relationStarts.addAll(_nodes);
		}
		
		return queryReasonableRouter(nodes, relationStarts, end);
		
	}
	
	/**
	 * 查询节点的所有关联节点，并且去点该节点的关联，防止反向再查<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 下午3:36:34
	 * @param List<RouterNodeBO> nodes 所有节点信息
	 * @param RouterNodeBO _node 某节点
	 * @return List<RouterNodeBO> 某节点所有关联节点
	 */
	public List<RouterNodeBO> queryNodes(List<RouterNodeBO> nodes, RouterNodeBO _node) throws Exception{
		
		List<RouterNodeBO> nodeList = new ArrayList<RouterNodeBO>();
		for(RouterNodeBO node: nodes){
			if(_node.getRelations().contains(node.getNodeId())){
				node.getRelations().remove(_node.getNodeId());
				nodeList.add(node);
				continue;
			}
		}
		
		return nodeList;
	}
	
	public static void main(String[] args) throws Exception{
		
		RouterNodeBO node1 = new RouterNodeBO();
		node1.setNodeId("1");
	    node1.setRelations(new ArrayList<String>());
	    node1.getRelations().add("2");
	    
	    RouterNodeBO node2 = new RouterNodeBO();
	    node2.setNodeId("2");
	    node2.setRelations(new ArrayList<String>());
	    node2.getRelations().add("1");
	    
	    RouterNodeBO node3 = new RouterNodeBO();
	    node3.setNodeId("3");
	    node3.setRelations(new ArrayList<String>());
	    node3.getRelations().add("5");
	    
	    RouterNodeBO node4 = new RouterNodeBO();
	    node4.setNodeId("4");
	    node4.setRelations(new ArrayList<String>());
	    node4.getRelations().add("5");
	    
	    RouterNodeBO node5 = new RouterNodeBO();
	    node5.setNodeId("5");
	    node5.setRelations(new ArrayList<String>());
	    node5.getRelations().add("4");
	    node5.getRelations().add("3");
	    
	    List<RouterNodeBO> all = new ArrayList<RouterNodeBO>();
	    all.add(node1);
	    all.add(node2);
	    all.add(node3);
	    all.add(node4);
	    all.add(node5);
	    
	    //System.out.println(RouterService.queryReasonableRouter(all, node4, node4));
		
	}
	
}
