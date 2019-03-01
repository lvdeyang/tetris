package com.sumavision.tetris.cms.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 地区操作<br/>
 * <b>作者: </b>ldy<br/>
 * <b>版本: </b>1.0<br/> 
 * <b>日期: </b>2019年2月28日 下午2:25:18
 */
@Component
public class RegionQuery {
	
	@Autowired
	private RegionDAO regionDao;

	/**
	 * 查询地区树<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午2:57:34
	 * @return List<RegionVO> 地区树结构
	 */
	public List<RegionVO> queryRegionTree() throws Exception {

		List<RegionPO> regions = regionDao.findAll();

		List<RegionVO> rootRegions = generateRootRegions(regions);

		packRegionTree(rootRegions, regions);

		return rootRegions;
	}

	/**
	 * 获得根地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午2:40:48
	 * @param regions 所有地区
	 * @return List<RegionVO> 所有根地区
	 */
	private List<RegionVO> generateRootRegions(Collection<RegionPO> regions) throws Exception {
		if (regions == null || regions.size() <= 0)
			return null;
		List<RegionVO> rootRegions = new ArrayList<RegionVO>();
		for (RegionPO region : regions) {
			if (region.getParentId() == null) {
				rootRegions.add(new RegionVO().set(region));
			}
		}
		return rootRegions;
	}

	/**
	 * 根地区节点加子地区方法<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午2:52:06
	 * @param rootRegions 根地区
	 * @param totalRegions 所有地区
	 * @return void
	 */
	public void packRegionTree(List<RegionVO> rootRegions, List<RegionPO> totalRegions) throws Exception {
		if (rootRegions == null || rootRegions.size() <= 0)
			return;
		for (int i = 0; i < rootRegions.size(); i++) {
			RegionVO rootRegion = rootRegions.get(i);
			for (int j = 0; j < totalRegions.size(); j++) {
				RegionPO region = totalRegions.get(j);
				if (region.getParentId() != null && region.getParentId() == rootRegion.getId()) {
					if (rootRegion.getSubRegions() == null)
						rootRegion.setSubRegions(new ArrayList<RegionVO>());
					rootRegion.getSubRegions().add(new RegionVO().set(region));
				}
			}
			if (rootRegion.getSubRegions() != null && rootRegion.getSubRegions().size() > 0) {
				packRegionTree(rootRegion.getSubRegions(), totalRegions);
			}
		}
	}
	
	/**
	 * 查询地区下所有子地区<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月28日下午3:04:47
	 * @param id 父地区id
	 * @return List<RegionPO> 子地区
	 */
	public List<RegionPO> findAllSubTags(Long id) throws Exception{
		return regionDao.findAllSubRegions(new StringBufferWrapper().append("%/")
															          .append(id)
															          .toString(), 
											 new StringBufferWrapper().append("%/")
																      .append(id)
																      .append("/%")
																      .toString());
	}
	
}
