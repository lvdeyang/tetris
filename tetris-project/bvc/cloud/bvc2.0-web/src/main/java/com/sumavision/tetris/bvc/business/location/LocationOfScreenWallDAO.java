package com.sumavision.tetris.bvc.business.location;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=LocationOfScreenWallPO.class,idClass=Long.class)
public interface LocationOfScreenWallDAO extends MetBaseDAO<LocationOfScreenWallPO>{
	
	public void deleteByLocationTemplateLayoutId(Long layoutId);
	
	public void deleteByLocationTemplateLayoutIdAndLocationXAfterOrLocationTemplateLayoutIdAndLocationYAfter(Long layoutId, Integer locationX, Long _layoutId, Integer locationY);
	
	public LocationOfScreenWallPO findByLocationTemplateLayoutIdAndDecoderBundleId(Long layoutId, String bundleId);
	
	public LocationOfScreenWallPO findByLocationTemplateLayoutIdAndLocationXAndLocationY(Long layoutId, Integer locationX, Integer locationY);
	
	public List<LocationOfScreenWallPO> findByLocationTemplateLayoutId(Long layoutId);
	
	public List<LocationOfScreenWallPO> findByIdIn(Collection<Long> ids);

}
