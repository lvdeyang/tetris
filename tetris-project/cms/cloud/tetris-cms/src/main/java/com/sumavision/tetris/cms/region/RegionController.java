package com.sumavision.tetris.cms.region;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cms.region.exception.RegionMoveFailException;
import com.sumavision.tetris.cms.region.exception.RegionNotExistException;
import com.sumavision.tetris.cms.region.exception.UserHasNotPermissionForRegionException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cms/region")
public class RegionController {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private RegionDAO regionDao;

	@Autowired
	private RegionQuery regionQuery;

	@Autowired
	private RegionService regionService;

	/**
	 * 查询地区树<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午5:07:50
	 * @return List<RegionVO> 地区列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree")
	public Object listTree(HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		List<RegionVO> rootRegions = regionQuery.queryRegionTree(user);

		return rootRegions;
	}

	/**
	 * 添加根地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午5:10:57
	 * @return RegionVO 地区
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root")
	public Object addRoot(HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验

		RegionPO region = regionService.addRoot(user);

		return new RegionVO().set(region);
	}

	/**
	 * 添加一个地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午5:12:46
	 * @return RegionVO 地区
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append")
	public Object append(Long parentId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!regionQuery.hasPermission(parentId, user)){
			throw new UserHasNotPermissionForRegionException(parentId, user);
		}

		RegionPO parent = regionDao.findOne(parentId);
		if (parent == null) {
			throw new RegionNotExistException(parentId);
		}

		RegionPO region = regionService.append(user, parent);

		return new RegionVO().set(region);
	}

	/**
	 * 修改地区<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午5:18:10
	 * @param id 地区id
	 * @param name 地区名称
	 * @return RegionVO 地区
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/{id}")
	public Object update(@PathVariable Long id, String name, String code, HttpServletRequest request)
			throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!regionQuery.hasPermission(id, user)){
			throw new UserHasNotPermissionForRegionException(id, user);
		}

		RegionPO regionPO = regionDao.findOne(id);
		if (regionPO == null) {
			throw new RegionNotExistException(id);
		}
		
		regionPO = regionService.update(regionPO, name, code);

		return new RegionVO().set(regionPO);
	}

	/**
	 * 删除地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午5:22:10
	 * @param id 地区id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(@PathVariable Long id, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验		
		if(!regionQuery.hasPermission(id, user)){
			throw new UserHasNotPermissionForRegionException(id, user);
		}
		
		RegionPO regionPO = regionDao.findOne(id);

		if (regionPO != null) {
			regionService.remove(regionPO);
		}

		return null;
	}

	/** 
	 * 移动地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午6:38:01
	 * @param sourceId 被移动地区的id
	 * @param targetId 移动地区的id
	 * @return boolean 移动结果
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(Long sourceId, Long targetId, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!regionQuery.hasPermission(sourceId, user)){
			throw new UserHasNotPermissionForRegionException(sourceId, user);
		}
		if(!regionQuery.hasPermission(targetId, user)){
			throw new UserHasNotPermissionForRegionException(targetId, user);
		}

		RegionPO sourceRegion = regionDao.findOne(sourceId);
		if (sourceRegion == null) {
			throw new RegionNotExistException(sourceId);
		}

		RegionPO targetRegion = regionDao.findOne(targetId);
		if (targetRegion == null) {
			throw new RegionNotExistException(targetId);
		}

		if (targetRegion.getId().equals(sourceRegion.getParentId())) {
			return false;
		}

		if (targetRegion.getParentPath() != null && targetRegion.getParentPath().indexOf(sourceRegion.getId().toString()) >= 0) {
			throw new RegionMoveFailException(sourceId, targetId);
		}

		regionService.move(sourceRegion, targetRegion);

		return true;
	}

	/**
	 * 地区置顶概述<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午6:43:06
	 * @param @PathVariable id 地区id
	 * @return boolean 节点是否移动
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/top/{id}")
	public Object top(@PathVariable Long id, HttpServletRequest request) throws Exception {

		UserVO user = userQuery.current();

		// TODO 权限校验
		if(!regionQuery.hasPermission(id, user)){
			throw new UserHasNotPermissionForRegionException(id, user);
		}

		RegionPO region = regionDao.findOne(id);
		if (region == null) {
			throw new RegionNotExistException(id);
		}

		if (region.getParentId() == null)
			return false;

		regionService.top(region);

		return true;
	}
}
