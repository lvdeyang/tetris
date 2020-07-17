package com.sumavision.tetris.bvc.business.group.forward;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class Jv230ForwardQuery {

	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private Jv230ForwardDAO jv230ForwardDao;
	
	/**
	 * 查询用户有权限的jv230设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月16日 下午2:50:06
	 * @return List<TreeNodeVO> jv230列表
	 */
	public List<TreeNodeVO> queryUsableJv230Bundles() throws Exception{
		UserVO user = userQuery.current();
		Set<String> bundleIds = resourceQueryUtil.queryUseableBundleIds(user.getId());
		List<BundlePO> jv230Entities = bundleDao.findByBundleIdInAndDeviceModelIn(bundleIds, new ArrayListWrapper<String>().add("jv230").getList());
		List<TreeNodeVO> nodes = new ArrayList<TreeNodeVO>();
		if(jv230Entities!=null && jv230Entities.size()>0){
			for(BundlePO entity:jv230Entities){
				nodes.add(new TreeNodeVO().set(entity));
			}
		}
		return nodes;
	}
	
	/**
	 * 查询上屏的jv230<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月17日 上午11:33:15
	 * @return List<TreeNodeVO> jv230列表
	 */
	public List<TreeNodeVO> queryForwardJv230Bundles() throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<TreeNodeVO> nodes = new ArrayList<TreeNodeVO>();
		List<String> bundleIds = jv230ForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(bundleIds!=null && bundleIds.size()>0){
			List<BundlePO> bundles = bundleDao.findByBundleIdIn(bundleIds);
			if(bundles!=null && bundles.size()>0){
				for(BundlePO bundle:bundles){
					nodes.add(new TreeNodeVO().set(bundle));
				}
			}
		}
		return nodes;
	}
	
	/**
	 * 查询jv230转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月17日 下午1:24:52
	 * @param String bundleId jv230 bundleId
	 * @return List<Jv230ForwardVO> 转发列表
	 */
	public List<Jv230ForwardVO> queryJv230Forwards(String bundleId) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		
		List<Jv230ForwardPO> forwardEntities = jv230ForwardDao.findByBundleIdAndUserIdAndTerminalIdAndBusinessTypeOrderBySerialNum(bundleId, String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		List<Jv230ForwardVO> forwards = new ArrayList<Jv230ForwardVO>();
		if(forwardEntities!=null && forwardEntities.size()>0){
			for(int i=0; i<forwardEntities.size(); i++){
				forwards.add(new Jv230ForwardVO().set(forwardEntities.get(i)));
			}
		}
		return forwards;
	}
	
}
