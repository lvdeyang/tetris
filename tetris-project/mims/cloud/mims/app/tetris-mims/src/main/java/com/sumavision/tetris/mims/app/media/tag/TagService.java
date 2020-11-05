package com.sumavision.tetris.mims.app.media.tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.media.tag.exception.TagHasExistsException;
import com.sumavision.tetris.mims.app.media.tag.exception.TagNotExistsException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class TagService {
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private TagGroupPermissionDAO tagGroupPermissionDAO;
	
	@Autowired
	private TagQuery tagQuery;
	
	public TagVO add(UserVO user, String name, Long parentId, String remark,Long hotCount) throws Exception{
		TagPO findTag = tagDAO.findByName(user.getGroupId(), name);
		if (findTag != null) throw new TagHasExistsException(name); 
		
		TagPO tag = new TagPO();
		tag.setName(name);
		tag.setParentId(parentId);
		tag.setRemark(remark);
		tag.setHotCount(hotCount);
		tag.setUpdateTime(new Date());
		tagDAO.save(tag);
		
		TagGroupPermissionPO permission = new TagGroupPermissionPO();
		permission.setTagId(tag.getId());
		permission.setGroupId(user.getGroupId());
		tagGroupPermissionDAO.save(permission);
		
		return new TagVO().set(tag).setSubColumns(new ArrayList<TagVO>());
	}
	
	public TagVO edit(UserVO user, Long id, String name, String remark,Long hotCount) throws Exception{
		TagPO findTag = tagDAO.findByNameAndId(id, user.getGroupId(), name);
		if (findTag != null) throw new TagHasExistsException(name); 
		
		TagPO tag = tagDAO.findByIdAndGroupId(id, user.getGroupId());
		
		if (tag == null) throw new TagNotExistsException(id);
		
		tag.setName(name);
		tag.setRemark(remark);
		tag.setHotCount(hotCount);
		tagDAO.save(tag);
		
		return new TagVO().set(tag);
	}
	
	public void remove(UserVO user, Long id) throws Exception{
		TagPO tag = tagDAO.findByIdAndGroupId(id, user.getGroupId());
		
		if (tag == null) throw new TagNotExistsException(id);
		
		List<TagPO> tags = new ArrayListWrapper<TagPO>().addAll(tagQuery.getChildTagList(user.getGroupId(), id))
				.add(tag)
				.getList();
		
		tagDAO.deleteInBatch(tags);
	}
}
