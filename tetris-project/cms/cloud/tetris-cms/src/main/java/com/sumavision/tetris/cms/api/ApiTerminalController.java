package com.sumavision.tetris.cms.api;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.eb.YingJGBEXTCALLDLL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.column.ColumnQuery;
import com.sumavision.tetris.cms.column.ColumnService;
import com.sumavision.tetris.cms.column.ColumnVO;
import com.sumavision.tetris.cms.region.RegionQuery;
import com.sumavision.tetris.cms.region.RegionVO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 外部app接口<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月11日 下午1:12:35
 */
@Controller
@RequestMapping(value = "/api/terminal/cms/column")
public class ApiTerminalController {
	
	private static int i = 0;
	
	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ColumnService columnService;
	
	@Autowired
	private ColumnRelationArticleService columnRelationArticleService;
	
	@Autowired
	private RegionQuery regionQuery;
	
	@Autowired
	private ArticleDAO articleDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;

	/**
	 * 根据组织id查询目录<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:51:02
	 * @param groupId 组织id
	 * @return List<ColumnVO> 目录列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/root")
	public Object listRoot(String groupId, HttpServletRequest request) throws Exception {

		UserVO user = new UserVO().setGroupId(groupId);
		
		List<ColumnVO> rootColumns = columnQuery.querycolumnTree(user);

		return rootColumns;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/{id}")
	public Object query(
			String groupId,
			@PathVariable Long id,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {
		
		UserVO userVO = new UserVO().setGroupId(groupId);

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.query(userVO, id, page);

		return column;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/query/{id}")
	public Object userQuery(
			String groupId,
			Long userId,
			@PathVariable Long id,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {
		
		UserVO userVO = new UserVO().setId(userId).setGroupId(groupId);

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.query(userVO, id, page);

		return column;
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryCommand")
	public Object queryCommand(
			String groupId,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {

		UserVO user = new UserVO().setGroupId(groupId);
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryCommand(user, page);

		return column;
	}
	
	/**
	 * 根据组织和用户获取推荐文章<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:04:07
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/queryCommand")
	public Object userQueryCommand(
			String groupId,
			Long userId,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {

		UserVO user = new UserVO().setId(userId).setGroupId(groupId);
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryCommand(user, page);

		JSONObject json= (JSONObject) JSONObject.toJSON(column);
		SignatureResponse response=new SignatureResponse();
		response.setMessage(json.toJSONString());
		YingJGBEXTCALLDLL.openDevice(1);
		response.setSign(YingJGBEXTCALLDLL.platformCalculateSignature(1,1,
				response.getMessage().getBytes(StandardCharsets.UTF_8)));
		YingJGBEXTCALLDLL.closeDevice(1);

		return response;
	}
	
	/**
	 * 根据地区信息获取服务器ip<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月17日 下午1:52:46
	 * @param groupId
	 * @param province
	 * @param city
	 * @param istrict
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryRegion/server")
	public Object queryRegionServer(
			String groupId,
			String province,
			String city,
			String district,
			HttpServletRequest request) throws Exception {
		UserVO user = new UserVO().setGroupId(groupId);
		RegionVO regionVO = regionQuery.queryIp(user, province, city, district);
		if (regionVO == null || regionVO.getIp() == null || regionVO.getIp().isEmpty()) return mimsServerPropsQuery.queryProps().getIp();
		return regionVO.getIp();
	}
	
	/**
	 * 获取栏目地区下的文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:24:45
	 * @param id
	 * @param province
	 * @param city
	 * @param district
	 * @return ColumnVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryRegion")
	public Object queryRegion(
			String groupId,
			Long id,
			String province, 
			String city, 
			String district,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {
		
		UserVO user = new UserVO().setGroupId(groupId);

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryByRegion(user, id, province, city, district, page);

		return column;
	}
	
	/**
	 * 根据组织和用户获取栏目地区下的文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:24:45
	 * @param id
	 * @param province
	 * @param city
	 * @param district
	 * @return ColumnVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/queryRegion")
	public Object userQueryRegion(
			String groupId,
			Long userId,
			Long id,
			String province, 
			String city, 
			String district,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception {
		
		UserVO user = new UserVO().setId(userId).setGroupId(groupId);

		Pageable page = new PageRequest(currentPage-1, pageSize);
		ColumnVO column = columnService.queryByRegion(user, id, province, city, district, page);

		return column;
	}
	
	/**
	 * 文章搜索<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午3:18:58
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/search")
	public Object search(
			String groupId,
			String search,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = new UserVO().setGroupId(groupId);
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		List<ArticleVO> list = columnService.search(user, search, page);
		
		return new HashMapWrapper<String, Object>().put("articles", list)
												   .getMap();
	}
	
	/**
	 * 根据组织id和用户id文章搜索<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午3:18:58
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/search")
	public Object search(
			String groupId,
			Long userId,
			String search,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = new UserVO().setId(userId).setGroupId(groupId);
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		List<ArticleVO> list = columnService.search(user, search, page);
		
		return new HashMapWrapper<String, Object>().put("articles", list)
												   .getMap();
	}
	
	/**
	 * 保存浏览历史<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @param articleId 浏览文章id
	 * @param columnId 文章栏目id
	 * @return article 浏览文章信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/history/save")
	public Object historySave(
			Long userId,
			Long articleId,
			Long columnId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		ArticleVO article = columnService.saveHistory(user, articleId, columnId);
		
		return article;
	}
	
	/**
	 * 删除浏览历史文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @param articleId 收藏文章id
	 * @param columnId 文章栏目id
	 * @return article 文章信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/history/remove")
	public Object historyRemove(
			Long userId,
			Long articleId,
			Long columnId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		List<ArticleVO> articles = columnService.removeHistory(user, articleId);
		
		return new HashMapWrapper<String, Object>().put("articles", articles)
				   .getMap();
	}
	
	/**
	 * 添加浏览历史<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @return articles 浏览文章信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/history/get")
	public Object historyGet(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		List<ArticleVO> articles = columnService.getHistory(user);
		
		return new HashMapWrapper<String, Object>().put("articles", articles)
												   .getMap();
	}
	
	/**
	 * 清空浏览历史<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/history/clear")
	public Object historyClear(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		columnService.clearHistory(user);
		
		return null;
	}
	
	/**
	 * 添加收藏文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @param articleId 收藏文章id
	 * @param columnId 文章栏目id
	 * @return article 文章信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/keep")
	public Object keep(
			Long userId,
			Long articleId,
			Long columnId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		ArticleVO article = columnService.keep(user, articleId, columnId);
		
		return article;
	}
	
	/**
	 * 删除收藏文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @param articleId 收藏文章id
	 * @param columnId 文章栏目id
	 * @return article 文章信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/keep/remove")
	public Object keepRemove(
			Long userId,
			Long articleId,
			Long columnId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		List<ArticleVO> articles = columnService.removeKeep(user, articleId);
		
		return new HashMapWrapper<String, Object>().put("articles", articles)
				   .getMap();
	}
	
	/**
	 * 添加收藏文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @return articles 文章信息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/keep/get")
	public Object keepSave(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		List<ArticleVO> articles = columnService.getKeep(user);
		
		return new HashMapWrapper<String, Object>().put("articles", articles)
				   .getMap();
	}
	
	/**
	 * 清空收藏列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/keep/clear")
	public Object keepClear(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		columnService.clearKeep(user);
		
		return null;
	}
	
	/**
	 * 获取订阅列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param groupId 用户企业id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/subscription/get")
	public Object getSubscription(
			String groupId,
			Long userId,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId).setGroupId(groupId);
		
		return columnQuery.querySubscriptionColumnTree(user);
	}
	
	/**
	 * 设置订阅列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param userId 用户id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/subscription/set")
	public Object setSubscription(
			Long userId,
			String columnList,
			HttpServletRequest request) throws Exception{
		
		UserVO testUserVO = userQuery.current();
		
		UserVO user = new UserVO().setId(userId);
		
		List<Long> columnListObjList = JSON.parseArray(columnList, Long.class);
		
		columnService.setSubscriptionColumnTree(user,columnListObjList);
		
		return null;
	}
	
	/**
	 * 文章通知<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月15日 下午6:09:59
	 * @param Long columId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticlePO> 栏目文章关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/inform")
	public Object inform(HttpServletRequest request) throws Exception{
		
		ArrayList<Long> aa = new ArrayList<Long>();
		
		aa.add(17l);
		aa.add(15l);
		aa.add(10l);
		aa.add(5l);
		
		Long articleId = null;
		
		if(i < 4){
			articleId = aa.get(i++);
		}else{
			i = 1;
			articleId = aa.get(0);
		}
		
		System.out.println(articleId);
		
		columnRelationArticleService.inform(null, articleId);		
	
		return null;
	}
	
}
