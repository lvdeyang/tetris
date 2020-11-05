package com.sumavision.signal.bvc.control.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.signal.bvc.entity.dao.InternetAccessDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.enumeration.InternetAccessType;
import com.sumavision.signal.bvc.entity.enumeration.TreeNodeIcon;
import com.sumavision.signal.bvc.entity.enumeration.TreeNodeType;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.vo.TreeNodeVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/signal/control/mapping")
public class RepeaterMappingController {
	
	@Autowired
	private RepeaterDAO repeaterDao;

	@Autowired
	private InternetAccessDAO internetAccessDao;
	
	/**
	 * 转发器树(输入加输出)<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午5:33:49
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/repeater/tree")
	public Object repeaterInTree(HttpServletRequest request){
		
		List<RepeaterPO> repeaters = repeaterDao.findAll();
		List<InternetAccessPO> inAccesss = internetAccessDao.findByType(InternetAccessType.STREAM_IN);
		List<InternetAccessPO> outAccesss = internetAccessDao.findByType(InternetAccessType.STREAM_OUT);
		
		TreeNodeVO inRoot = new TreeNodeVO().setId("-1")
										  	.setName("转发器输入网口")
										  	.setIcon(TreeNodeIcon.FOLDER.getName())
										  	.setType(TreeNodeType.FOLDER)
										  	.setKey()
										  	.setChildren(new ArrayList<TreeNodeVO>());
		
		recursionFolder(inRoot, repeaters, inAccesss);	
		
		TreeNodeVO outRoot = new TreeNodeVO().setId("-1")
											 .setName("转发器输出网口")
											 .setIcon(TreeNodeIcon.FOLDER.getName())
											 .setType(TreeNodeType.FOLDER)
											 .setKey()
											 .setChildren(new ArrayList<TreeNodeVO>());

		recursionFolder(outRoot, repeaters, outAccesss);	
		
		return new HashMapWrapper<String, Object>().put("in", inRoot)
												   .put("out", outRoot)
												   .getMap();
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<RepeaterPO> repeaters, 
			List<InternetAccessPO> accesss){
		
		if(repeaters != null && repeaters.size()>0){
			for(RepeaterPO repeater: repeaters){
				TreeNodeVO repeaterNode = new TreeNodeVO().set(repeater)
														  .setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(repeaterNode);
				recursionFolder(repeaterNode, null, accesss);
			}
		}
		
		if(accesss != null && accesss.size()>0){
			for(InternetAccessPO access: accesss){
				if(access.getRepeaterId().toString().equals(root.getId())){
					TreeNodeVO accessNode = new TreeNodeVO().set(access);
					root.getChildren().add(accessNode);
				}
			}
		}
	}	
}
