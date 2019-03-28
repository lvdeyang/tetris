package com.sumavision.tetris.cms.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleService;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/cms")
public class ApiServerController {
	
	@Autowired
	private ColumnDAO columnDao;
	
	@Autowired
	private ApiServerService apiServerService;
	
	@Autowired
	private ArticleService articleService;

	/**
	 * 批量添加栏目<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月11日 下午5:20:24
	 * @param column
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/column/add")
	public Object addColumn(
			String column,
			HttpServletRequest request) throws Exception{
		
		List<JSONObject> columns = JSONArray.parseArray(column, JSONObject.class);
		
		List<ColumnPO> columnsPos = new ArrayList<ColumnPO>();
		for(JSONObject columnObject: columns){
			ColumnPO columnPO = new ColumnPO();
			columnPO.setName(columnObject.getString("name"));
			columnPO.setUpdateTime(new Date());
			columnPO.setCode(columnObject.getString("code"));
			columnPO.setParentId(columnObject.getLong("parentId"));
			columnPO.setParentPath(columnObject.getString("parentPath"));
			columnsPos.add(columnPO);
		}
		
		columnDao.save(columnsPos);
		
		return null;
	}
	
	/**
	 * 添加文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午8:33:12
	 * @param column 栏目 -- "[code1, code2]"
	 * @param name 文章名
	 * @param type 文章类型  TEXT/AVIDEO
	 * @param author 作者名
	 * @param publishTime 发表时间
	 * @param thumbnail 缩略图--媒资图片路径
	 * @param remark 备注
	 * @param content 内容--json字符串，结构:[{"type":"内容类型",--yjgb_title(标题)/yjgb_txt(文本)/yjgb_picture(图片)/yjgb_video(视频)/yjgb_audio(音频)
	 * 										"value":"内容值",--标题和文本传对应的String,图片、视频和音频传对应的url}]
	 * @param region 地区 -- "[code1,code2]"
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/article/add")
	public Object addArticle(
			String column,
			String name, 
			String type,
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			Boolean command,
			String content,
			String region, 
			HttpServletRequest request) throws Exception{
		
		//TODO:暂时自己创建一个UserVO
		UserVO user = new UserVO().setGroupId("4")
								  .setGroupName("数码视讯")
								  .setUuid("5")
								  .setNickname("lvdeyang");
		
		List<String> columns = JSONArray.parseArray(column, String.class);
		List<JSONObject> contents = JSONArray.parseArray(content, JSONObject.class);
		List<String> regions = JSONArray.parseArray(region, String.class);

		ArticlePO article = apiServerService.add(columns, name, type, author, publishTime, thumbnail, remark, command, contents, regions);
		articleService.addPermission(user, article);
		
		return null;
	}
}
