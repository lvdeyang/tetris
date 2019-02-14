package com.sumavision.tetris.cms.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 内容模板标签操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月14日 下午3:02:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TemplateTagService {

	@Autowired
	private TemplateTagDAO templateTagDao;
	
	@Autowired
	private TemplateTagQuery templateTagQuery;
	
	@Autowired
	private TemplateDAO templateDao;
	
	/**
	 * 添加一个根标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:18:52
	 * @return TemplateTagPO 新建的标签
	 */
	public TemplateTagPO addRoot() throws Exception{
		
		TemplateTagPO tag = new TemplateTagPO();
		tag.setName("新建的标签");
		tag.setUpdateTime(new Date());
		
		templateTagDao.save(tag);
		
		return tag;
	}
	
	/**
	 * 添加一个内容模板标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:07:03
	 * @param String name 标签名称
	 * @param String remark 标签备注
	 * @param TemplateTagPO parent 父标签
	 * @return TemplateTagPO 新建的标签
	 */
	public TemplateTagPO add(String name, String remark, TemplateTagPO parent) throws Exception{
		
		StringBufferWrapper parentPath = new StringBufferWrapper();
		if(parent.getParentId() == null){
			parentPath.append("/").append(parent.getParentId());
		}else{
			parentPath.append(parent.getParentPath()).append("/").append(parent.getParentId());
		}
		
		TemplateTagPO tag = new TemplateTagPO();
		tag.setName(name);
		tag.setRemark(remark);
		tag.setParentId(parent.getId());
		tag.setParentPath(parentPath.toString());
		tag.setUpdateTime(new Date());
		
		templateTagDao.save(tag);
		
		return tag;
	}
	
	/**
	 * 修改标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:31:36
	 * @param TemplateTagPO tag 标签
	 * @param String name 标签名称
	 * @param String remark 标签备注
	 * @return TemplateTagPO 标签
	 */
	public TemplateTagPO update(
			TemplateTagPO tag, 
			String name, 
			String remark) throws Exception{
		
		tag.setName(name);
		tag.setRemark(remark);
		tag.setUpdateTime(new Date());
		templateTagDao.save(tag);
		
		return tag;
	}
	
	/**
	 * 删除标签<br/>
	 * <p>
	 * 	删除当前标签<br/>
	 *  删除子标签<br/>
	 *  将相关模板的标签置空<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午4:10:28
	 * @param TemplateTagPO tag 标签
	 */
	public void remove(TemplateTagPO tag) throws Exception{
		
		List<TemplateTagPO> subTags = templateTagQuery.findAllSubTags(tag.getId());
		
		Set<Long> tagIds = new HashSetWrapper<Long>().add(tag.getId()).getSet();
		if(subTags!=null && subTags.size()>0){
			for(TemplateTagPO subTag:subTags){
				tagIds.add(subTag.getId());
			}
		}
		
		List<TemplatePO> templates = templateDao.findByTemplateTagIdIn(tagIds);
		if(templates!=null && templates.size()>0){
			for(TemplatePO template:templates){
				template.setTemplateTagId(null);
			}
		}
		templateDao.save(templates);
		
		if(subTags == null) subTags = new ArrayList<TemplateTagPO>();
		subTags.add(tag);
		templateTagDao.deleteInBatch(subTags);
		
	}
	
	/**
	 * 移动标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午4:34:52
	 * @param TemplateTagPO sourceTag 待移动的标签
	 * @param TemplateTagPO targetTag 移动目标标签
	 */
	public void move(TemplateTagPO sourceTag, TemplateTagPO targetTag) throws Exception{
		
		StringBufferWrapper parentPath = new StringBufferWrapper();
		if(targetTag.getParentId() == null){
			parentPath.append("/").append(targetTag.getId());
		}else{
			parentPath.append(targetTag.getParentPath()).append("/").append(targetTag.getId());
		}
		
		sourceTag.setParentId(targetTag.getId());
		sourceTag.setParentPath(parentPath.toString());
		
		List<TemplateTagPO> subTags = templateTagQuery.findAllSubTags(sourceTag.getId());
		
		if(subTags!=null && subTags.size()>0){
			for(TemplateTagPO subTag:subTags){
				String[] paths = subTag.getParentPath().split(new StringBufferWrapper().append("/").append(sourceTag.getId()).toString());
				if(paths.length == 1){
					subTag.setParentPath(parentPath.append("/").append(targetTag.getId()).toString());
				}else{
					subTag.setParentPath(parentPath.append("/").append(targetTag.getId()).append(paths[1]).toString());
				}
			}
		}
		
		if(subTags==null || subTags.size()<=0) subTags = new ArrayList<TemplateTagPO>();
		subTags.add(sourceTag);
		
		templateTagDao.save(subTags);
	}
	
}
