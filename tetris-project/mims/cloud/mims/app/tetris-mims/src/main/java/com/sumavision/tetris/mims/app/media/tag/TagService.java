package com.sumavision.tetris.mims.app.media.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.exception.NoMediaFoundException;
import com.sumavision.tetris.mims.app.media.tag.exception.TagHasExistsException;
import com.sumavision.tetris.mims.app.media.tag.exception.TagNotExistsException;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
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
	
	@Autowired
	private MediaVideoDAO mediaVideoDao;
	
	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	
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
	
	
	public void addHotCount(List<TagVO> tags) throws Exception{
		
		List<TagPO> tagPOs=new ArrayList<TagPO>();
		for (TagVO tagVO : tags) {
			TagPO tagPO=tagDAO.findById(tagVO.getId());
			tagPO.setHotCount(tagPO.getHotCount()==null?1l:tagPO.getHotCount()+1);
			tagPOs.add(tagPO);
		}
		tagDAO.saveAll(tagPOs);
		
		
	}
	
	public void remove(UserVO user, Long id) throws Exception{
		TagPO tag = tagDAO.findByIdAndGroupId(id, user.getGroupId());
		
		if (tag == null) throw new TagNotExistsException(id);
		
		List<TagPO> tags = new ArrayListWrapper<TagPO>().addAll(tagQuery.getChildTagList(user.getGroupId(), id))
				.add(tag)
				.getList();
		
		tagDAO.deleteInBatch(tags);
	}
	
	public void handleVideoImport(UserVO user,List<TagsExcelModel> excelList) throws Exception{
		//根据媒资名查媒资，看是否存在，再根据标签名查询标签，存在则加不存在则创建后再加
		for (TagsExcelModel excel : excelList) {
			String mediaName = excel.getMediaName();
			List<MediaVideoPO> mediaVideoPO = mediaVideoDao.findByFileName(mediaName);
			if(mediaVideoPO.size()==0){
				//抛异常，媒资不存在
				throw new NoMediaFoundException(mediaName);
			}
			String tagName = excel.getTagName();
			String tagNames[] = tagName.split("，");
			List<String> tagList = Arrays.asList(tagNames);
			for (String tag : tagList) {
				TagPO tagPO = tagDAO.findByName(tag);
				if(tagPO==null){
					//标签不存在则创建
					TagPO newTag = new TagPO();
					newTag.setUpdateTime(new Date());
					newTag.setName(tag);
					tagDAO.save(newTag);
					
					TagGroupPermissionPO permission = new TagGroupPermissionPO();
					permission.setTagId(newTag.getId());
					permission.setGroupId(user.getGroupId());
					tagGroupPermissionDAO.save(permission);
					
				}
			}
			//将各标签整合为一个字符串
			String transTags = tagList==null?"":StringUtils.join(tagList.toArray(), MediaVideoPO.SEPARATOR_TAG);
			mediaVideoPO.get(0).setTags(transTags);
			//将标签添加到节目
			mediaVideoDao.saveAll(mediaVideoPO);
			
			
		}	
	}
	
	public void handleAudioImport(UserVO user,List<TagsExcelModel> excelList) throws Exception{
		
				for (TagsExcelModel excel : excelList) {
					String audioName = excel.getMediaName();
					List<MediaAudioPO> mediaAudioPO = mediaAudioDao.findByFileName(audioName);
					if(mediaAudioPO.size()==0){
						throw new NoMediaFoundException(audioName);
					}
					String tagName = excel.getTagName();
					String tagNames[] = tagName.split("，");
					List<String> tagList = Arrays.asList(tagNames);
					for (String tag : tagList) {
						TagPO tagPO = tagDAO.findByName(tag);
						if(tagPO==null){
							//标签不存在则创建
							TagPO newTag = new TagPO();
							newTag.setUpdateTime(new Date());
							newTag.setName(tag);
							tagDAO.save(newTag);
							
							TagGroupPermissionPO permission = new TagGroupPermissionPO();
							permission.setTagId(newTag.getId());
							permission.setGroupId(user.getGroupId());
							tagGroupPermissionDAO.save(permission);
						}
					}
					//将各标签整合为一个字符串
					String transTags = tagList==null?"":StringUtils.join(tagList.toArray(), MediaAudioPO.SEPARATOR_TAG);
					mediaAudioPO.get(0).setTags(transTags);
					//将标签添加到节目
					mediaAudioDao.saveAll(mediaAudioPO);
					
					
				}	
		
		
		
	}
}
