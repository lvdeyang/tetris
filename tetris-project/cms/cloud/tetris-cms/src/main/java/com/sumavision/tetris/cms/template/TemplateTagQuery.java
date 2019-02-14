package com.sumavision.tetris.cms.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 内容模板分类标签查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月14日 下午2:30:25
 */
@Component
public class TemplateTagQuery {

	@Autowired
	private TemplateTagDAO templateTagDao;
	
	/**
	 * 查询全部标签并组装成树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午2:50:53
	 * @return List<TemplateTagVO> 树的根节点列表
	 */
	public List<TemplateTagVO> queryTagTree() throws Exception{
		
		List<TemplateTagPO> tags = templateTagDao.findAll();
		
		List<TemplateTagVO> rootTags = generateRootTags(tags);
		
		packTagTree(rootTags, tags);
		
		return rootTags;
	}

	/**
	 * 查询根标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午2:44:31
	 * @param Collection<TemplateTagPO> tags 查找范围
	 * @return List<TemplateTagVO> 根标签列表
	 */
	private List<TemplateTagVO> generateRootTags(Collection<TemplateTagPO> tags) throws Exception{
		if(tags==null || tags.size()<=0) return null;
		List<TemplateTagVO> rootTags = new ArrayList<TemplateTagVO>();
		for(TemplateTagPO tag:tags){
			if(tag.getParentId() == null){
				rootTags.add(new TemplateTagVO().set(tag));
			}
		}
		return rootTags;
	}
	
	/**
	 * 根据给定的根标签组装标签树，并且包装成VO<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月20日 下午2:02:38
	 * @param List<TemplateTagVO> rootTags 根标签
	 * @param List<TemplateTagPO> totalTags 全部标签
	 */
	public void packTagTree(List<TemplateTagVO> rootTags, List<TemplateTagPO> totalTags) throws Exception{
		if(rootTags==null || rootTags.size()<=0) return;
		for(int i=0; i<rootTags.size(); i++){
			TemplateTagVO rootTag = rootTags.get(i);
			for(int j=0; j<totalTags.size(); j++){
				TemplateTagPO tag = totalTags.get(j);
				if(tag.getParentId()!=null && tag.getParentId().equals(rootTag.getId())){
					if(rootTag.getSubTags() == null) rootTag.setSubTags(new ArrayList<TemplateTagVO>());
					rootTag.getSubTags().add(new TemplateTagVO().set(tag));
				}
			}
			if(rootTag.getSubTags()!=null && rootTag.getSubTags().size()>0){
				packTagTree(rootTag.getSubTags(), totalTags);
			}
		}
	}
	
	/**
	 * 查询标签下的所有子标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 下午3:59:13
	 * @param Long id 标签id
	 * @return List<TemplateTagPO> 字标签
	 */
	public List<TemplateTagPO> findAllSubTags(Long id) throws Exception{
		return templateTagDao.findAllSubTags(new StringBufferWrapper().append("'%/")
															          .append(id)
															          .append("'")
															          .toString(), 
											 new StringBufferWrapper().append("'%/")
																      .append(id)
																      .append("/%'")
																      .toString());
	}
	
}
