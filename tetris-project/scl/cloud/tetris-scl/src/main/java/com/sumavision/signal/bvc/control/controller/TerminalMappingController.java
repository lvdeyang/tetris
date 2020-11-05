package com.sumavision.signal.bvc.control.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.entity.dao.InternetAccessDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.dao.TerminalBindRepeaterDAO;
import com.sumavision.signal.bvc.entity.enumeration.InternetAccessType;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.enumeration.TreeNodeIcon;
import com.sumavision.signal.bvc.entity.enumeration.TreeNodeType;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.signal.bvc.entity.vo.TerminalVO;
import com.sumavision.signal.bvc.entity.vo.TreeNodeVO;
import com.sumavision.signal.bvc.resource.util.ResourceQueryUtil;
import com.sumavision.signal.bvc.service.TerminalMappingService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/signal/control/terminal")
public class TerminalMappingController {
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private RepeaterDAO repeaterDao;

	@Autowired
	private InternetAccessDAO internetAccessDao;
	
	@Autowired
	private TerminalBindRepeaterDAO terminalBindRepeaterDao;
	
	@Autowired
	private TerminalMappingService terminalMappingService;
	
	/**
	 * 获取转发器输入网口树<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午1:26:03
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/repeater")
	public Object queryRepeater(HttpServletRequest request) throws Exception{
		
		List<RepeaterPO> repeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<InternetAccessPO> inAccesss = internetAccessDao.findByType(InternetAccessType.STREAM_IN);
		
		TreeNodeVO inRoot = new TreeNodeVO().setId("-1")
										  	.setName("转发器输入网口")
										  	.setIcon(TreeNodeIcon.FOLDER.getName())
										  	.setType(TreeNodeType.FOLDER)
										  	.setKey()
										  	.setChildren(new ArrayList<TreeNodeVO>());
		
		recursionFolder(inRoot, repeaters, inAccesss);	
		
		return inRoot;
	}

	/**
	 * 根据网口id查询绑定关系<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午1:27:04
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/{id}")
	public Object queryAll(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findByAccessId(id);		
		
		return binds;
	}
	
	/**
	 * 查询未绑定的终端<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午1:27:04
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/unbind")
	public Object queryUnbind(HttpServletRequest request) throws Exception{
		
		String layerId = RegisterStatus.getNodeId();
		List<BundlePO> bundles = new ArrayList<BundlePO>();
		bundles.addAll(resourceQueryUtil.queryBundlesByDeviceModel("jv210", "播放器"));
		bundles.addAll(resourceQueryUtil.queryBundlesByDeviceModel("tvos"));
		bundles.addAll(resourceQueryUtil.queryBundlesByDeviceModel("proxy"));
		bundles.addAll(resourceQueryUtil.queryBundlesByDeviceModel("mixer"));
		bundles.addAll(resourceQueryUtil.queryBundlesByDeviceModel("virtual"));
		bundles.addAll(resourceQueryUtil.queryBundlesByDeviceModel("jv220"));
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findAll();
		
		List<BundlePO> unbindBundles = new ArrayList<BundlePO>();
		for(BundlePO bundle: bundles){
			boolean flag = true;
			for(TerminalBindRepeaterPO bind: binds){
				if(bind.getBundleId().equals(bundle.getBundleId())){
					flag = false;
					break;
				}
			}
			if(flag){
				unbindBundles.add(bundle);
			}
		}
		
		List<TerminalVO> vos = TerminalVO.getConverter(TerminalVO.class).convert(unbindBundles, TerminalVO.class);
		
		return vos;
	}
	
	/**
	 * 转发器网口绑定终端<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:44:25
	 * @param String selected 选中的终端 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bind")
	public Object bind(
			Long accessId,
			String selected,
			HttpServletRequest request) throws Exception{
		
		List<JSONObject> selects = JSONArray.parseArray(selected, JSONObject.class);
		InternetAccessPO access = internetAccessDao.findById(accessId);
		
		List<TerminalBindRepeaterPO> binds = new ArrayList<TerminalBindRepeaterPO>();
		
		for(JSONObject select: selects){
			String bundleId = select.getString("bundleId");
			String bundleName = select.getString("bundleName");
			String bundleIp = select.getString("bundleIp");
			String bundleType = select.getString("bundleType");
			String deviceModel = select.getString("deviceModel");
			String layerId = select.getString("layerId");
			
			TerminalBindRepeaterPO bind = new TerminalBindRepeaterPO();
			bind.setAccessAddress(access.getAddress());
			bind.setAccessId(accessId);
			bind.setAccessType(access.getType());
			bind.setBundleId(bundleId);
			bind.setBundleIp(bundleIp);
			bind.setBundleName(bundleName);
			bind.setBundleType(bundleType);
			bind.setDeviceModel(deviceModel);
			bind.setLayerId(layerId);
			bind.setRepeaterId(access.getRepeaterId());
			
			binds.add(bind);
		}
		
		List<PortMappingPO> mappings = terminalMappingService.generatePortMapping(access,binds);
		
		terminalBindRepeaterDao.save(binds);
		
		return binds;
	}
	
	/**
	 * 生成转发器任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月24日 上午8:47:38
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/task")
	public Object generateTask() throws Exception{
		
		terminalMappingService.generateTask();
		
		return null;
	}
	
	/**
	 * 销毁转发器任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午1:14:01
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/task")
	public Object removeTask() throws Exception{
		
		terminalMappingService.removeTask();
		
		return null;
	}
	
	/**
	 * 终端解绑转发器网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午3:53:41
	 * @param id 绑定关系id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/unbind/{id}")
	public Object unbind(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		terminalMappingService.remove(id);
		
		return null;
	}
	
	/**
	 * 方法概述<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月25日 下午2:37:17
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/unbind/all")
	public Object unbindAll(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
		List<Long> ids = JSON.parseArray(wrapper.getString("ids"), Long.class);
		
		terminalMappingService.removeAll(ids);
		
		return null;
	}
	
	/**
	 * 刷新设备信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月6日 上午8:26:24
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		List<TerminalBindRepeaterPO> binds = terminalMappingService.updateBundle(id);
		
		return binds;
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
