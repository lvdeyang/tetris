package com.sumavision.tetris.cms.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月24日 下午3:59:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService {

	public ArticlePO add(){
		return null;
	}
	
}
