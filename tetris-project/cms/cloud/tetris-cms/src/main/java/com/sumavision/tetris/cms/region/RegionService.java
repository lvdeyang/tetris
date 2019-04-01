package com.sumavision.tetris.cms.region;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cms.article.ArticleRegionPermissionDAO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegionService {

	@Autowired
	private RegionDAO regionDao;	

	@Autowired
	private RegionQuery regionQuery;
	
	@Autowired
	private RegionUserPermissionDAO regionUserPermissionDao;
	
	@Autowired
	private ArticleRegionPermissionDAO articleRegionPermissionDao;

	/**
	 * 添加一个根地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:11:33
	 * @return RegionPO 地区
	 */
	public RegionPO addRoot(UserVO user) throws Exception {

		RegionPO regionPO = new RegionPO();
		regionPO.setName("新建的标签");
		regionPO.setUpdateTime(new Date());

		regionDao.save(regionPO);
		
		addPermission(user, regionPO);

		return regionPO;
	}

	/**
	 * 添加一个地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:13:25
	 * @return RegionPO 地区
	 */
	public RegionPO append(UserVO user, RegionPO parent) throws Exception {

		StringBufferWrapper parentPath = new StringBufferWrapper();
		if (parent.getParentId() == null) {
			parentPath.append("/").append(parent.getId());
		} else {
			parentPath.append(parent.getParentPath()).append("/").append(parent.getId());
		}

		RegionPO regionPO = new RegionPO();
		regionPO.setName("新建的标签");
		regionPO.setParentId(parent.getId());
		regionPO.setParentPath(parentPath.toString());
		regionPO.setUpdateTime(new Date());

		regionDao.save(regionPO);
		
		addPermission(user, regionPO);

		return regionPO;
	}

	/**
	 * 修改地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:39:14
	 * @param regionPO 地区
	 * @param name 名称
	 * @param code 编号
	 * @return RegionPO 地区
	 */
	public RegionPO update(RegionPO regionPO, String name, String code) throws Exception {

		regionPO.setName(name);
		regionPO.setCode(code);
		regionPO.setUpdateTime(new Date());
		regionDao.save(regionPO);

		return regionPO;
	}

	/**
	 * 删除地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午3:46:38
	 * @param regionPO 地区
	 */
	public void remove(RegionPO regionPO) throws Exception {

		List<RegionPO> subRegionPOs = regionQuery.findAllSubTags(regionPO.getId());

		Set<Long> regionIds = new HashSetWrapper<Long>().add(regionPO.getId()).getSet();
		if (subRegionPOs != null && subRegionPOs.size() > 0) {
			for (RegionPO region : subRegionPOs) {
				regionIds.add(region.getId());
			}
		}

		if (subRegionPOs == null)
			subRegionPOs = new ArrayList<RegionPO>();
		subRegionPOs.add(regionPO);
		regionDao.deleteInBatch(subRegionPOs);
		
		List<RegionUserPermissionPO> permissions = regionUserPermissionDao.findByRegionIdIn(regionIds);
		regionUserPermissionDao.deleteInBatch(permissions);
		articleRegionPermissionDao.deleteByRegionIdIn(regionIds);
	}

	/**
	 * 移动地区<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午3:52:48
	 * @param sourceRegion
	 * @param targetRegion
	 * @throws Exception
	 */
	public void move(RegionPO sourceRegion, RegionPO targetRegion) throws Exception {

		StringBufferWrapper parentPath = new StringBufferWrapper();
		if (targetRegion.getParentId() == null) {
			parentPath.append("/").append(targetRegion.getId());
		} else {
			parentPath.append(targetRegion.getParentPath()).append("/").append(targetRegion.getId());
		}

		sourceRegion.setParentId(targetRegion.getId());
		sourceRegion.setParentPath(parentPath.toString());

		List<RegionPO> subRegions = regionQuery.findAllSubTags(sourceRegion.getId());

		if (subRegions != null && subRegions.size() > 0) {
			for (RegionPO subRegion : subRegions) {
				String[] paths = subRegion.getParentPath()
						.split(new StringBufferWrapper().append("/").append(sourceRegion.getId()).toString());
				if (paths.length == 1 || paths.length == 0) {
					subRegion.setParentPath(parentPath.append("/").append(sourceRegion.getId()).toString());
				} else {
					subRegion.setParentPath(parentPath.append("/").append(sourceRegion.getId()).append(paths[1]).toString());
				}
			}
		}

		if (subRegions == null || subRegions.size() <= 0)
			subRegions = new ArrayList<RegionPO>();
		subRegions.add(sourceRegion);

		regionDao.save(subRegions);
	}

	/**
	 * 置顶一个地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日 下午4:45:18
	 * @param regionPO 地区
	 */
	public void top(RegionPO regionPO) throws Exception {

		if (regionPO.getParentId() == null)
			return;

		List<RegionPO> subRegions = regionQuery.findAllSubTags(regionPO.getId());

		if (subRegions != null && subRegions.size() > 0) {

			String reg = new StringBufferWrapper().append("/").append(regionPO.getParentId()).toString();

			for (RegionPO subRegion : subRegions) {
				subRegion.setParentPath(subRegion.getParentPath().split(reg)[1]);
			}

		}
		
		regionPO.setParentId(null);
		regionPO.setParentPath(null);

		if (subRegions == null)
			subRegions = new ArrayList<RegionPO>();
		subRegions.add(regionPO);
		regionDao.save(subRegions);

	}
	
	/**
	 * 添加地区用户关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 下午2:24:03
	 * @param user 用户信息
	 * @param region 地区信息
	 * @return RegionUserPermissionPO 地区用户关联
	 */
	public RegionUserPermissionPO addPermission(UserVO user, RegionPO region) throws Exception{
		
		RegionUserPermissionPO permission = new RegionUserPermissionPO();
		permission.setRegionId(region.getId());
		permission.setUserId(user.getUuid());
		permission.setGroupId(user.getGroupId());
		
		regionUserPermissionDao.save(permission);
		
		return permission;
	}
}
