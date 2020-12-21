package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.VedioCapacityDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.VedioCapacityPO;
import com.suma.venus.resource.vo.VedioCapacityVO;
import com.sumavision.tetris.bvc.business.query.CommandSystemQueryService;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.user.UserVO;


@Controller
@RequestMapping("/vedioCapacity")
public class VedioCapacityController {
	
	@Autowired
	private VedioCapacityDAO vedioCapacityDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private CommandSystemQueryService commandSystemQueryService;
	
	@Autowired
	private UserService userService;
 	
	/**
	 * 容量状态查询<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月17日 下午3:12:23
	 * @param id 容量
	 * @param groupId 组织id
	 * @return vedioCapacityVO 容量状态参数
	 * @throws Exception
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Object query() throws Exception{
		List<VedioCapacityPO> vedioCapacityPOs = vedioCapacityDAO.findAll();
		if(vedioCapacityPOs.size()==0 || vedioCapacityPOs.isEmpty()){
			VedioCapacityPO vedioCapacityPO = new VedioCapacityPO();
			vedioCapacityPO.setUserCapacity(200l);
			vedioCapacityPO.setVedioCapacity(1024l);
			vedioCapacityPO.setTurnCapacity(128l);
			vedioCapacityPO.setReplayCapacity(20l);
			userService.setUserCapacity(200l);
			vedioCapacityDAO.save(vedioCapacityPO);
			vedioCapacityPOs.add(vedioCapacityPO);
		}
		VedioCapacityPO vedioCapacityPO = vedioCapacityPOs.get(0);
		VedioCapacityVO vedioCapacityVO = new VedioCapacityVO();
		vedioCapacityVO.setUserCapacity(vedioCapacityPO.getUserCapacity());
		vedioCapacityVO.setVedioCapacity(vedioCapacityPO.getVedioCapacity());
		vedioCapacityVO.setTurnCapacity(vedioCapacityPO.getUserCapacity());
		vedioCapacityVO.setReplayCapacity(vedioCapacityPO.getReplayCapacity()); 
		List<UserVO> userVOs = userQuery.queryUserOnline();
		Integer user = userVOs.size();
		Long userCount = user.longValue();
		vedioCapacityVO.setUserCount(userCount);
		List<BundlePO> bundlePOs = bundleDao.findAll();
		List<BundlePO> bundleCountList = new ArrayList<BundlePO>();
		for (BundlePO bundlePO : bundlePOs) {
			if(bundlePO.getDeviceModel().equalsIgnoreCase("jv210")){
				bundleCountList.add(bundlePO);
			}
		}
		Long turnCapacity = commandSystemQueryService.queryCountOfTransmit();
		Long replayCapacity = commandSystemQueryService.queryCountOfReview();
		vedioCapacityVO.setTurnCount(turnCapacity);
		vedioCapacityVO.setReCount(replayCapacity);
		Integer bundle = bundleCountList.size();
		Long bundleCount = bundle.longValue();
		vedioCapacityVO.setVedioCount(bundleCount);
		Long idleUser = vedioCapacityPO.getUserCapacity() - userCount;
		Long idleVedio = vedioCapacityPO.getVedioCapacity() - bundleCount;
		Long turnIdleCount = vedioCapacityPO.getTurnCapacity() - turnCapacity;
		Long reIdleCount = vedioCapacityPO.getReplayCapacity() - replayCapacity;
		vedioCapacityVO.setReIdleCount(reIdleCount);
		vedioCapacityVO.setTurnIdleCount(turnIdleCount);
		vedioCapacityVO.setUserIdleCount(idleUser);
		vedioCapacityVO.setVedioIdleCount(idleVedio);		
		vedioCapacityVO.setId(vedioCapacityPO.getId());
		return vedioCapacityVO;
	}
	
	/**
	 * 容量修改<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月17日 下午3:17:12
	 * @param id
	 * @param userCapacity 用户容量
	 * @param vedioCapacity 图像容量
	 * @param turnCapacity 转发容量
	 * @param replayCapacity 回放容量
	 * @return vedioCapacityVO 容量
	 */
	@RequestMapping("/change/capacity")
	@ResponseBody
	public Object changeCapacity(
			Long userCapacity,
			Long vedioCapacity,
			Long turnCapacity,
			Long replayCapacity) throws Exception{
		List<VedioCapacityPO> vedioCapacityPOs = vedioCapacityDAO.findAll();
		VedioCapacityPO vedioCapacityPO = vedioCapacityPOs.get(0);
		vedioCapacityPO.setUserCapacity(userCapacity);
		vedioCapacityPO.setVedioCapacity(vedioCapacity);
		vedioCapacityPO.setTurnCapacity(turnCapacity);
		userService.setUserCapacity(userCapacity);
		vedioCapacityPO.setReplayCapacity(replayCapacity);
		vedioCapacityDAO.save(vedioCapacityPO);
		VedioCapacityVO vedioCapacityVO = new VedioCapacityVO();
		vedioCapacityVO.setUserCapacity(vedioCapacityPO.getUserCapacity());
		vedioCapacityVO.setVedioCapacity(vedioCapacityPO.getVedioCapacity());
		vedioCapacityVO.setTurnCapacity(turnCapacity);
		vedioCapacityVO.setReplayCapacity(replayCapacity);
		return vedioCapacityVO;
	}

}
