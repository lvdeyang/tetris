package com.sumavision.tetris.cms.region;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegionService {

	@Autowired
	private RegionDAO regionDao;	

	@Autowired
	private RegionQuery regionQuery;

	/**
	 * 添加一个根地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:11:33
	 * @return RegionPO 地区
	 */
	public RegionPO addRoot() throws Exception {

		RegionPO regionPO = new RegionPO();
		regionPO.setName("新建的标签");
		regionPO.setUpdateTime(new Date());

		regionDao.save(regionPO);

		return regionPO;
	}

	/**
	 * 添加一个地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:13:25
	 * @return RegionPO 地区
	 */
	public RegionPO append(RegionPO parent) throws Exception {

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

		return regionPO;
	}

	/**
	 * 修改地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:39:14
	 * @param regionPO 地区
	 * @param name 名称
	 * @return RegionPO 地区
	 */
	public RegionPO update(RegionPO regionPO, String name) throws Exception {

		regionPO.setName(name);
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
				if (paths.length == 1) {
					subRegion.setParentPath(parentPath.append("/").append(targetRegion.getId()).toString());
				} else {
					subRegion.setParentPath(parentPath.append("/").append(targetRegion.getId()).append(paths[1]).toString());
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
}
