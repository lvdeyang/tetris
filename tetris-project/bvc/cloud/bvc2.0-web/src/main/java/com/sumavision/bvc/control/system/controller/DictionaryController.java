package com.sumavision.bvc.control.system.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.system.vo.DictionaryVO;
import com.sumavision.bvc.meeting.logic.record.omc.BoService;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.enumeration.ServLevel;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;


/**
 * @ClassName: 参数模板接口 
 * @author lvdeyang
 * @date 2018年7月25日 上午9:01:14 
 */
@Controller
@RequestMapping(value = "/system/dictionary")
public class DictionaryController {
	
	@Autowired
	private DictionaryDAO conn_dictionary;
	@Autowired
	private BoService boService;
	
	/**
	 * @Title: 获取表中枚举字段的枚举列表 
	 * @return Object    返回类型 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query", method=RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		Set<String> dicTYpes = new HashSet<String>();
		Set<String> servLevels = new HashSet<String>();
		DicType[] dicTypeEnums = DicType.values();
		ServLevel [] servLevelEnums = ServLevel.values();
		
		for(DicType dicType:dicTypeEnums){
			dicTYpes.add(dicType.getName());
		}
		for(ServLevel servLevel : servLevelEnums){
			servLevels.add(servLevel.getName());
		}
	    List<DictionaryPO> parentLevelNames = conn_dictionary.findByServLevelAndDicType(ServLevel.LEVEL_ONE, DicType.DEMAND);
	    List<DictionaryVO> _parentLevelNames = DictionaryVO.getConverter(DictionaryVO.class).convert(parentLevelNames, DictionaryVO.class);
		List<DictionaryPO> regionDics=conn_dictionary.findByDicType(DicType.REGION);
		List<DictionaryVO> _regionDics = DictionaryVO.getConverter(DictionaryVO.class).convert(regionDics, DictionaryVO.class);
		return new HashMapWrapper<String, Object>().put("dicTypes", dicTYpes)
												   .put("servLevels", servLevels)
												   .put("parentLevelNames", _parentLevelNames)
												   .put("regions", _regionDics)
												   .getMap();

	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/region", method = RequestMethod.GET)
	public Object queryRegion(HttpServletRequest request) throws Exception{
		List<DictionaryPO> regions = conn_dictionary.findByDicType(DicType.REGION);
		DictionaryPO defaultRegion = new DictionaryPO();
		defaultRegion.setContent("默认");
		defaultRegion.setBoId("");
		regions.add(defaultRegion);
		List<DictionaryVO> _regions = DictionaryVO.getConverter(DictionaryVO.class).convert(regions, DictionaryVO.class);
		return _regions;
	}
	
	/**
	 * @Title: 分页查询 
	 * @param pageSize 每页数据量
	 * @param currentPage 当前页
	 * @return rows 数据行
	 * @return total 总数据量
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load", method=RequestMethod.GET)
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);

		Page<DictionaryPO> pagedDics = conn_dictionary.findDics(page);
		long total = pagedDics.getTotalElements();
		List<DictionaryVO> _dics = DictionaryVO.getConverter(DictionaryVO.class).convert(pagedDics.getContent(), DictionaryVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _dics);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param content 名称
	 * @param boId 地区的boid
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String content,
			String dicType,
			String servLevel,
			String parentLevelName,
			String code,
			String ip,
			String boId,
			HttpServletRequest request) throws Exception{
		
		DictionaryPO dic=new DictionaryPO();
		DicType type=DicType.fromName(dicType);
		JSONObject jObj=new JSONObject();
		
		dic.setServLevel(ServLevel.fromName(servLevel));
		dic.setParentLevelName(parentLevelName);
		if(parentLevelName.equals("默认") || parentLevelName.equals("")){
			dic.setParentLevelId(null);
		}else {
			DictionaryPO t_dic = conn_dictionary.findByContentAndDicTypeAndServLevel(parentLevelName, DicType.DEMAND, ServLevel.LEVEL_ONE);
			dic.setParentLevelId(t_dic.getId());
			jObj.put("parentID", t_dic.getBoId());
		}
		
		jObj.put("name", content);
		jObj.put("parentRegionCode", boId);
		if(type.equals(DicType.REGION)){
		    jObj.put("code", code);
		    jObj.put("ip", ip);
		    JSONObject ret=boService.createLocation(jObj);
		    dic.setBoId(ret.getJSONObject("data").getJSONObject("result").getString("locationID"));
		}else if(type.equals(DicType.LIVE)){
			//如果是“会议”或“监控”开头，则先找同名的直播栏目
			if(content.startsWith("会议")){
				List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicType("会议_", DicType.LIVE);
				if(dicPO.size() > 0){
					throw new BaseException(StatusCode.FORBIDDEN, "已经建立了名为“" + dicPO.get(0).getContent() + "”的直播栏目");
				}
			}else if(content.startsWith("监控")){
				List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicType("监控_", DicType.LIVE);
				if(dicPO.size() > 0){
					throw new BaseException(StatusCode.FORBIDDEN, "已经建立了名为“" + dicPO.get(0).getContent() + "”的直播栏目");
				}
			}
			
			JSONObject ret=boService.createChannelCategory(jObj);
			dic.setLiveBoId(ret.getJSONObject("data").getJSONObject("result").getString("categoryLiveID"));
			dic.setBoId("默认");
		}else if(type.equals(DicType.DEMAND)) {
			//如果是一级栏目且以“会议”或“监控”开头，则先查找有无同名的直播栏目
			if(ServLevel.LEVEL_ONE.equals(ServLevel.fromName(servLevel))){
				if(content.startsWith("会议")){
					List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicTypeAndServLevel("会议_", DicType.DEMAND, ServLevel.LEVEL_ONE);
					if(dicPO.size() > 0){
						throw new BaseException(StatusCode.FORBIDDEN, "已经建立了名为“" + dicPO.get(0).getContent() + "”的点播一级栏目");
					}
				}else if(content.startsWith("监控")){
					List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicTypeAndServLevel("监控_", DicType.DEMAND, ServLevel.LEVEL_ONE);
					if(dicPO.size() > 0){
						throw new BaseException(StatusCode.FORBIDDEN, "已经建立了名为“" + dicPO.get(0).getContent() + "”的点播一级栏目");
					}
				}
			}			
			//如果是二级栏目，则必须先找到“会议”一级栏目
			else if(ServLevel.LEVEL_TWO.equals(ServLevel.fromName(servLevel))){
				List<DictionaryPO> dicPO = conn_dictionary.findByContentPrefixAndDicTypeAndServLevel("会议", DicType.DEMAND, ServLevel.LEVEL_ONE);
				if(dicPO.size() == 0){
					throw new BaseException(StatusCode.FORBIDDEN, "请先建立“会议_”开头的点播一级栏目");
				}
				jObj.put("parentID", dicPO.get(0).getBoId());
			}
			else{
				throw new BaseException(StatusCode.FORBIDDEN, "请选择正确的栏目层级");
			}
			
			JSONObject ret=boService.createVideoCategory(jObj);
			dic.setBoId(ret.getJSONObject("data").getString("categoryID"));
			dic.setParentBoId(ret.getJSONObject("data").getString("parentID"));			
		}
		
		if(type.equals(DicType.REGION)){
			dic.setCode(code);
			dic.setIp(ip);
			dic.setParentRegionId("默认");			
		}else if(type.equals(DicType.STORAGE_LOCATION)){
			if(code==null || code.equals("")){
				throw new BaseException(StatusCode.FORBIDDEN, "code不能为空");
			}
			DictionaryPO dicPO = conn_dictionary.findByDicTypeAndCode(DicType.STORAGE_LOCATION, code);
			if(dicPO != null){
				throw new BaseException(StatusCode.FORBIDDEN, "不能与存储位置“" + dicPO.getContent() + "”的code相同");
			}
			dic.setCode(code);
			dic.setIp("默认");
			dic.setParentRegionId("默认");			
		}else{
			dic.setIp("默认");
			dic.setCode("默认");
			dic.setParentRegionId(boId);
		}
		
		dic.setContent(content);
		dic.setDicType(DicType.fromName(dicType));
		dic.setUpdateTime(new Date());
		conn_dictionary.save(dic);
		
		DictionaryVO _dic = new DictionaryVO().set(dic);
		
		return _dic;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param name
	 * @param videoFormat
	 * @param videoFormatSpare
	 * @param audioFormat
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String content,
			String dicType,
			String servLevel,
			String parentLevelName,
			String code,
			String ip,
			String boId,
			HttpServletRequest request) throws Exception{
		
		if(!dicType.equals(DicType.STORAGE_LOCATION.getName())){
			throw new BaseException(StatusCode.FORBIDDEN, "此类型不能修改");
		}
		DictionaryPO dic = conn_dictionary.findOne(id);
		dic.setContent(content);
		dic.setDicType(DicType.fromName(dicType));
		dic.setCode(code);
		
		dic.setServLevel(ServLevel.fromName(servLevel));
		
		
		dic.setParentLevelName(parentLevelName);
		if(null==parentLevelName || parentLevelName.equals("默认") || parentLevelName.equals("")){
			dic.setParentLevelId(null);
		}else {
			DictionaryPO t_dic = conn_dictionary.findByContentAndDicTypeAndServLevel(parentLevelName, DicType.DEMAND, ServLevel.LEVEL_ONE);
			dic.setParentLevelId(t_dic.getId());
		}
		
        dic.setParentRegionId(boId);
        dic.setIp(ip);
		dic.setUpdateTime(new Date());
		conn_dictionary.save(dic);
		
		DictionaryVO _dic = new DictionaryVO().set(dic);
		
		return _dic;
	}
	
	/**
	 * @Title: 根据id删除数据 
	 * @param id
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		conn_dictionary.delete(id);
		return null;
	}
	
	/**
	 * @Title: 根据id批量删除 
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		
		List<DictionaryPO> dics = conn_dictionary.findAll(ids);
		conn_dictionary.deleteInBatch(dics);
		
		return null;
	}
	
}
