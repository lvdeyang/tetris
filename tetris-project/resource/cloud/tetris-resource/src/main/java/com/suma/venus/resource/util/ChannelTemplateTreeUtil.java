package com.suma.venus.resource.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.pojo.ChannelParamPO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.service.ChannelParamService;
import com.suma.venus.resource.vo.ChannelTemplateTreeVO;
import com.suma.venus.resource.vo.EleTreeNodeVO;
import com.suma.venus.resource.vo.ParamTreeNodeVO;

/**
 * 能力模型节点树转化工具类
 * @author lxw
 *
 */
@Component
public class ChannelTemplateTreeUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelTemplateTreeUtil.class);
	
	@Autowired
	private ChannelParamService channelParamService;
	
	/**前端使用element-ui的树插件*/
	public List<EleTreeNodeVO> createEleTree(List<ChannelTemplatePO> channelTemplates) throws Exception{
		List<EleTreeNodeVO> tree = new ArrayList<EleTreeNodeVO>();
		String device_model = channelTemplates.get(0).getDeviceModel();
		List<EleTreeNodeVO> treeChildren = new ArrayList<EleTreeNodeVO>();
		treeChildren.add(createChannelsTree(channelTemplates));
		tree.add(new EleTreeNodeVO(device_model, treeChildren));
		return tree;
	}
	
	private EleTreeNodeVO createChannelsTree(List<ChannelTemplatePO> channelTemplates){
		List<EleTreeNodeVO> channelsChildren = new ArrayList<EleTreeNodeVO>();
		EleTreeNodeVO channelsTree = new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_CHANNELS, channelsChildren);
//		List<String> channelNameList = new ArrayList<String>();
		for (ChannelTemplatePO channelTemplatePO : channelTemplates) {
//			channelNameList.add(channelTemplatePO.getChannelName());
			List<EleTreeNodeVO> channelChildren = new ArrayList<EleTreeNodeVO>();
			EleTreeNodeVO channelNode = new EleTreeNodeVO(channelTemplatePO.getChannelName(), channelChildren);
			channelsChildren.add(channelNode);
			List<ChannelParamPO> channelParamPOs = channelParamService.findByParentChannelTemplateId(channelTemplatePO.getId());
			if(null != channelParamPOs){
				createChannelParamEleNodes(channelChildren, channelParamPOs);
			}
		}
		
		return channelsTree;
	}

	private void createChannelParamEleNodes(List<EleTreeNodeVO> channelChildren, List<ChannelParamPO> channelParamPOs) {
		for (ChannelParamPO channelParamPO : channelParamPOs) {
			switch (channelParamPO.getParamType()) {
			case CONTAINER:
			case RSL:
			case FPS:
				List<EleTreeNodeVO> containerParamChildren = new ArrayList<EleTreeNodeVO>();
				EleTreeNodeVO containerParamNode = new EleTreeNodeVO(channelParamPO.getParamName(), containerParamChildren);
				channelChildren.add(containerParamNode);
				createChannelParamEleNodes(containerParamChildren,channelParamService.findByParentChannelParamId(channelParamPO.getId()));
				break;
			case STRING:
				channelChildren.add(createEleStringNode(channelParamPO));
				break;
			case NUM:
				channelChildren.add(createEleNumNode(channelParamPO));
				break;
			case CONSTANT:
				channelChildren.add(createEleConstNode(channelParamPO));
				break;
			case ENUM:
				channelChildren.add(createEleEnumNode(channelParamPO));
				break;
			default:
				break;
			}
		}
	}

	private EleTreeNodeVO createEleStringNode(ChannelParamPO channelParamPO) {
		List<EleTreeNodeVO> stringNodeChildren = new ArrayList<EleTreeNodeVO>();
		stringNodeChildren.add(new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_MINLENGTH + " : " + channelParamPO.getStringMinLength()));
		stringNodeChildren.add(new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_MAXLENGTH + " : " + channelParamPO.getStringMaxLength()));
		EleTreeNodeVO stringNode = new EleTreeNodeVO(channelParamPO.getParamName(),stringNodeChildren);
		return stringNode;
	}
	
	private EleTreeNodeVO createEleConstNode(ChannelParamPO channelParamPO) {
		List<EleTreeNodeVO> constNodeChildren = new ArrayList<EleTreeNodeVO>();
		constNodeChildren.add(new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_CONSTTYPE+ " : " + channelParamPO.getConstType()));
		constNodeChildren.add(new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE + " : " + channelParamPO.getConstValue()));
		EleTreeNodeVO stringNode = new EleTreeNodeVO(channelParamPO.getParamName(),constNodeChildren);
		return stringNode;
	}
	
	private EleTreeNodeVO createEleEnumNode(ChannelParamPO channelParamPO) {
		return new EleTreeNodeVO(channelParamPO.getParamName() + " : " + channelParamPO.getEnumValues());
	}
	
	private EleTreeNodeVO createEleNumNode(ChannelParamPO channelParamPO) {
		List<EleTreeNodeVO> numNodeChildren = new ArrayList<EleTreeNodeVO>();
		numNodeChildren.add(new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_MINVALUE+ " : " + channelParamPO.getNumMinValue()));
		numNodeChildren.add(new EleTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_MAXVALUE + " : " + channelParamPO.getNumMaxValue()));
		EleTreeNodeVO stringNode = new EleTreeNodeVO(channelParamPO.getParamName(),numNodeChildren);
		return stringNode;
	}
	
	/**前端使用ztree jquery插件进行展示*/
	public ChannelTemplateTreeVO createZTree(List<ChannelTemplatePO> channelTemplates) throws Exception{
		String device_model = channelTemplates.get(0).getDeviceModel();
		StringBuilder sBuilder_channelName = new StringBuilder();
		for (ChannelTemplatePO channelTemplate : channelTemplates) {
			sBuilder_channelName.append(channelTemplate.getChannelName()).append(",");
		}
		String channel_names = sBuilder_channelName.deleteCharAt(sBuilder_channelName.length()-1).toString();
		List<ParamTreeNodeVO> paramTree = new ArrayList<ParamTreeNodeVO>();
		createTopSeveralNodes(device_model, channel_names,paramTree);
		for (ChannelTemplatePO channelTemplate : channelTemplates) {
			ParamTreeNodeVO channel_name_node = new ParamTreeNodeVO(channelTemplate.getChannelName(), 
					VenusParamConstant.PARAM_JSON_KEY_CHANNELS, channelTemplate.getChannelName());
			paramTree.add(channel_name_node);
			ParamTreeNodeVO max_channel_cnt_node = new ParamTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_MAXCHANNELCNT + "_" + channelTemplate.getId(),
					channel_name_node.getId(), VenusParamConstant.PARAM_JSON_KEY_MAXCHANNELCNT + " : " + channelTemplate.getMaxChannelCnt());
			paramTree.add(max_channel_cnt_node);
			ParamTreeNodeVO channel_param_node = new ParamTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM + "_" + channelTemplate.getId(), 
					channel_name_node.getId(), VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM);
			paramTree.add(channel_param_node);
			List<ChannelParamPO> paramPOs = channelParamService.findByParentChannelTemplateId(channelTemplate.getId());
			if(null != paramPOs){
				createChannelParamNodes(paramTree, channel_param_node, paramPOs);
			}
		}
		
		ChannelTemplateTreeVO vo = new ChannelTemplateTreeVO();
		vo.setDeviceModel(device_model);
		vo.setParamTree(paramTree);
		return vo;
	}
	
	private void createTopSeveralNodes(String device_model, String channel_names,
			List<ParamTreeNodeVO> paramTree) {
		ParamTreeNodeVO rootNode = new ParamTreeNodeVO("root", null, device_model);
		paramTree.add(rootNode);
		ParamTreeNodeVO channelsNode = new ParamTreeNodeVO(
				VenusParamConstant.PARAM_JSON_KEY_CHANNELS, rootNode.getId(), VenusParamConstant.PARAM_JSON_KEY_CHANNELS);
		paramTree.add(channelsNode);
		ParamTreeNodeVO channel_names_node = new ParamTreeNodeVO(VenusParamConstant.PARAM_JSON_KEY_CHANNELNAME, channelsNode.getId(),
				VenusParamConstant.PARAM_JSON_KEY_CHANNELNAME + " : " + channel_names);
		paramTree.add(channel_names_node);
	}

	private void createChannelParamNodes(List<ParamTreeNodeVO> paramTree,
			ParamTreeNodeVO channel_param_node, List<ChannelParamPO> paramPOs) {
		for (ChannelParamPO paramPO : paramPOs) {
			switch (paramPO.getParamType()) {
			case CONTAINER:
			case RSL:
			case FPS:
				createContainerParamNode(paramTree, channel_param_node, paramPO);
				break;
			case STRING:
				createStringParamNode(paramTree, channel_param_node,paramPO);
				break;
			case NUM:
				createNumParamNode(paramTree, channel_param_node, paramPO);
				break;
			case CONSTANT:
				createConstParamNode(paramTree, channel_param_node, paramPO);
				break;
			case ENUM:
				createEnumParamNode(paramTree, channel_param_node, paramPO);
				break;
			default:
				break;
			}
		}
	}
	
	private void createStringParamNode(List<ParamTreeNodeVO> paramTree,
			ParamTreeNodeVO channel_param_node, ChannelParamPO paramPO) {
		ParamTreeNodeVO string_node = new ParamTreeNodeVO("param_string_"+paramPO.getId(), channel_param_node.getId(), paramPO.getParamName());
		paramTree.add(string_node);
		ParamTreeNodeVO min_len_node = new ParamTreeNodeVO("min_len_"+paramPO.getId(), string_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_MINLENGTH + " : " + paramPO.getStringMinLength());
		paramTree.add(min_len_node);
		ParamTreeNodeVO max_len_node = new ParamTreeNodeVO("max_len_"+paramPO.getId(), string_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_MAXLENGTH + " : " + paramPO.getStringMaxLength());
		paramTree.add(max_len_node);
	}
	
	private void createNumParamNode(List<ParamTreeNodeVO> paramTree,
			ParamTreeNodeVO channel_param_node, ChannelParamPO paramPO) {
		ParamTreeNodeVO num_node = new ParamTreeNodeVO("param_num_"+paramPO.getId(), channel_param_node.getId(), paramPO.getParamName());
		paramTree.add(num_node);
		ParamTreeNodeVO min_value = new ParamTreeNodeVO("min_vlue_"+paramPO.getId(), num_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_MINVALUE + " : " + paramPO.getNumMinValue());
		paramTree.add(min_value);
		ParamTreeNodeVO max_value = new ParamTreeNodeVO("max_value_"+paramPO.getId(), num_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_MAXVALUE + " : " + paramPO.getNumMaxValue());
		paramTree.add(max_value);
		ParamTreeNodeVO step = new ParamTreeNodeVO("step_"+paramPO.getId(), num_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_STEP + " : " + paramPO.getNumStep());
		paramTree.add(step);
	}
	
	private void createConstParamNode(List<ParamTreeNodeVO> paramTree,
			ParamTreeNodeVO channel_param_node, ChannelParamPO paramPO) {
		ParamTreeNodeVO const_node = new ParamTreeNodeVO("param_const_"+paramPO.getId(), channel_param_node.getId(), paramPO.getParamName());
		paramTree.add(const_node);
		ParamTreeNodeVO const_type = new ParamTreeNodeVO("const_type_"+paramPO.getId(), const_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_CONSTTYPE + " : " + paramPO.getConstType());
		paramTree.add(const_type);
		ParamTreeNodeVO max_value = new ParamTreeNodeVO("const_value_"+paramPO.getId(), const_node.getId(), 
				VenusParamConstant.PARAM_JSON_KEY_CONSTVALUE + " : " + paramPO.getConstValue());
		paramTree.add(max_value);
	}
	
	private void createEnumParamNode(List<ParamTreeNodeVO> paramTree,
			ParamTreeNodeVO channel_param_node, ChannelParamPO paramPO) {
		ParamTreeNodeVO enum_node = new ParamTreeNodeVO("param_enum_"+paramPO.getId(), channel_param_node.getId(), paramPO.getParamName());
		paramTree.add(enum_node);
		ParamTreeNodeVO values = new ParamTreeNodeVO("enum_values_"+paramPO.getId(), enum_node.getId(), paramPO.getEnumValues());
		paramTree.add(values);
	}
	
	private void createContainerParamNode(List<ParamTreeNodeVO> paramTree,
			ParamTreeNodeVO channel_param_node, ChannelParamPO paramPO) {
		ParamTreeNodeVO container_node = new ParamTreeNodeVO("param_container_"+paramPO.getId(), channel_param_node.getId(), paramPO.getParamName());
		paramTree.add(container_node);
		List<ChannelParamPO> childParams = channelParamService.findByParentChannelParamId(paramPO.getId());
		createChannelParamNodes(paramTree, container_node, childParams);
	}
}
