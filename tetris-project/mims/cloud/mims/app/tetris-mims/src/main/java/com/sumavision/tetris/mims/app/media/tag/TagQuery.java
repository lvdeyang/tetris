package com.sumavision.tetris.mims.app.media.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.user.UserVO;

@Component
public class TagQuery {
	
	@Autowired
	private TagDAO tagDAO;
	
	public List<TagVO> getTagTree(UserVO user) throws Exception{
		List<TagVO> roots = getRootTag(user);
		
		if (roots != null){
			for (TagVO tagVO : roots) {
				tagVO.setSubColumns(getChildTagTree(user.getGroupId(), tagVO));
			}
		}
		
		return roots;
	}
	
	public List<TagVO> getRootTag(UserVO user) throws Exception{
		List<TagPO> rootTagPOs = tagDAO.findRootByGroupId(user.getGroupId());
		
		List<TagVO> rootTagVOs = TagVO.getConverter(TagVO.class).convert(rootTagPOs, TagVO.class);
		
		return rootTagVOs;
	}
	
	public List<TagVO> getChildTagTree(String groupId, TagVO parent) throws Exception{
		List<TagPO> tagPOs = tagDAO.findByParentIdAndGroupId(parent.getId(), groupId);
		
		List<TagVO> tagVOs = TagVO.getConverter(TagVO.class).convert(tagPOs, TagVO.class);
		
		if (tagVOs == null || tagVOs.isEmpty()) {
			
		}else {
			parent.setDisabled(true);
			for (TagVO tag : tagVOs) {
				tag.setSubColumns(getChildTagTree(groupId, tag));
			}
		}
		
		return tagVOs;
	}
	
	public List<TagPO> getChildTagList(String groupId, Long parentId) throws Exception{
		List<TagPO> tags = tagDAO.findByParentIdAndGroupId(parentId, groupId);
		
		if (tags != null) {
			for (TagPO tag : tags) {
				tags.addAll(getChildTagList(groupId, tag.getId()));
			}
		}
		
		return tags;
	}
}
