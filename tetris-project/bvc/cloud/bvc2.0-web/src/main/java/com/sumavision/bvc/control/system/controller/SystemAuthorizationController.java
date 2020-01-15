package com.sumavision.bvc.control.system.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.system.vo.AuthorizationVO;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.system.dao.AuthorizationDAO;
import com.sumavision.bvc.system.po.AuthorizationMemberPO;
import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * @ClassName: 看会权限模板接口 
 * @author wjw
 * @date 2019年1月8日 上午9:01:14 
 */
@Controller
@RequestMapping(value = "/system/authorization")
public class SystemAuthorizationController {

	@Autowired
	private AuthorizationDAO authorizationDao;
	
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
		
		Page<AuthorizationPO> pageParams = authorizationDao.findAll(page);
		long total = pageParams.getTotalElements();
		List<AuthorizationVO> _params = AuthorizationVO.getConverter(AuthorizationVO.class).convert(pageParams.getContent(), AuthorizationVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _params);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 查询权限下的设备成员
	 * @param @param id
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/{id}")
	public Object query(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		List<TreeNodeVO> bundleTrees = new ArrayList<TreeNodeVO>();
		
		AuthorizationPO authorization = authorizationDao.findOne(id);		
		Set<AuthorizationMemberPO> authorizationMembers = authorization.getAuthorizationMembers();		

		for(AuthorizationMemberPO member: authorizationMembers){
			TreeNodeVO node = new TreeNodeVO().set(member); 
			bundleTrees.add(node);
		}
		
		return new HashMapWrapper<String, Object>().put("bundles", bundleTrees)
				   .getMap();
	}
	
	/**
	 * @Title: 保存权限设备
	 * @param id
	 * @param bundleIds
	 * @return Object 
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/members/{id}")
	public Object saveMembers(
			@PathVariable Long id,
			String bundleIds,
			HttpServletRequest request) throws Exception{
		
		List<String> bundleIdArray = JSONArray.parseArray(bundleIds, String.class);
		
		AuthorizationPO authorization = authorizationDao.findOne(id);	
		for(String bundleId: bundleIdArray){
			AuthorizationMemberPO member = new AuthorizationMemberPO();
			member.setBundleId(bundleId);
			member.setAuthorization(authorization);
			authorization.getAuthorizationMembers().add(member);
		}
		
		authorizationDao.save(authorization);
		
		return null;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name
	 * @param remark
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		if(authorizationDao.findByName(name).size() > 0){
			throw new CommonNameAlreadyExistedException("看会权限", name);
		}
		
		AuthorizationPO authorization = new AuthorizationPO();
		authorization.setName(name);
		authorization.setRemark(remark);
		authorization.setUpdateTime(new Date());
		authorizationDao.save(authorization);
		
		AuthorizationVO _authorization = new AuthorizationVO().set(authorization);
		
		return _authorization;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param remark
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		AuthorizationPO authorization = authorizationDao.findOne(id);
		if(!authorization.getName().equals(name)){
			if(authorizationDao.findByName(name).size() > 0){
				throw new CommonNameAlreadyExistedException("看会权限", name);
			}
		}
		authorization.setName(name);
		authorization.setRemark(remark);
		authorization.setUpdateTime(new Date());
		authorizationDao.save(authorization);
		
		AuthorizationVO _authorization = new AuthorizationVO().set(authorization);
		
		return _authorization;
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
		authorizationDao.delete(id);
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
		authorizationDao.deleteByIdIn(ids);
		return null;
	}
}
