package com.sumavision.tetris.mims.app.media.tag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.media.tag.exception.TagNotExistsException;
import com.sumavision.tetris.user.UserVO;

@Component
public class TagQuery {
	
	@Autowired
	private TagDAO tagDAO;
	
	/**
	 * 获取标签树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 * @param UserVO user 用户信息
	 * @return List<TagVO> 标签树
	 */
	public List<TagVO> getTagTree(UserVO user) throws Exception{
		List<TagVO> roots = getRootTag(user);
		
		if (roots != null){
			for (TagVO tagVO : roots) {
				tagVO.setSubColumns(getChildTagTree(user.getGroupId(), tagVO));
			}
		}
		
		return roots;
	}
	
	/**
	 * 根据父标签获取子树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:46:44
	 * @param UserVO user 用户信息
	 * @param Long parentId 父标签树id
	 * @return List<TagVO> 标签树
	 */
	public List<TagVO> getTagTreeByParent(UserVO user, Long parentId) throws Exception {
		TagPO parent = tagDAO.findOne(parentId);
		if (parent == null) throw new TagNotExistsException(parentId);
		
		TagVO parentVO = new TagVO().set(parent);
		return new ArrayListWrapper<TagVO>().add(parentVO.setSubColumns(getChildTagTree(user.getGroupId(), parentVO))).getList();
	}
	
	/**
	 * 获取根标签<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:48:09
	 * @param UserVO user 用户信息
	 * @return List<TagVO> 根标签数组
	 */
	public List<TagVO> getRootTag(UserVO user) throws Exception{
		List<TagPO> rootTagPOs = tagDAO.findRootByGroupId(user.getGroupId());
		
		List<TagVO> rootTagVOs = TagVO.getConverter(TagVO.class).convert(rootTagPOs, TagVO.class);
		
		return rootTagVOs;
	}
	
	/**
	 * 递归查子标签获取树(带父标签禁选)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:48:46
	 * @param String groupId 用户组id
	 * @param TagVO parent 父标签
	 * @return List<TagVO> 标签树
	 */
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
	
	/**
	 * 递归查询标签数(数组结构删除用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:51:50
	 * @param String groupId 用户组id
	 * @param Long parentId 父标签id
	 * @return List<TagPO> 标签数组
	 */
	public List<TagPO> getChildTagList(String groupId, Long parentId) throws Exception{
		List<TagPO> tags = tagDAO.findByParentIdAndGroupId(parentId, groupId);
		List<TagPO> childTags = new ArrayList<TagPO>();
		if (tags != null) {
			for (TagPO tag : tags) {
				List<TagPO> tagList = getChildTagList(groupId, tag.getId());
				if (tagList != null) childTags.addAll(tagList);
			}
			tags.addAll(childTags);
		}
		
		return tags;
	}
	
	/**
	 * 根据标签名和用户组id批量查询标签<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:52:52
	 * @param String groupId 用户组id
	 * @param List<String> names 标签名
	 * @return List<TagVO> 查询的标签数组
	 */
	public List<TagVO> queryFromNameAndGroupId(String groupId, List<String> names) throws Exception{
		List<TagPO> tags = tagDAO.findByNameIn(groupId, names);
		
		return TagVO.getConverter(TagVO.class).convert(tags, TagVO.class);
	}
	
	/**
	 * 根据标签id查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:53:54
	 * @param Long tagId 标签id
	 * @return TagVO 标签信息
	 */
	public TagVO queryById(Long tagId) throws Exception {
		if (tagId == null) return null;
		
		TagPO tag = tagDAO.findOne(tagId);
		
		return tag != null ? new TagVO().set(tag) : null;
	}
	
	/**
	 * 批量根据标签id查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 下午2:54:32
	 * @param List<Long> tagIds 标签id数组
	 * @return List<TagVO> 标签信息
	 */
	public List<TagVO> queryByIds(List<Long> tagIds) throws Exception {
		return TagVO.getConverter(TagVO.class).convert(tagDAO.findAll(tagIds), TagVO.class);
	}
}
