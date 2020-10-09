package com.sumavision.tetris.patrol.address;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/address")
public class AddressController {

	@Autowired
	private AddressQuery addressQuery;
	
	@Autowired
	private AddressService addressService;
	
	/**
	 * 根据名城动态分页查询地址信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午5:51:58
	 * @param String name 地址名称
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 数据总量
	 * @return List<AddressVO> rows 地址列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String name, 
			int currentPage, 
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return addressQuery.load(name, currentPage, pageSize);
	}
	
	/**
	 * 添加地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:05:38
	 * @param String name 地址名称
	 * @return AddressVO 地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			HttpServletRequest request) throws Exception{
		
		return addressService.add(name);
	}
	
	/**
	 * 修改地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:09:22
	 * @param Long id 地址id
	 * @param String name 地址名称
	 * @return AddressVO 地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return addressService.edit(id, name);
	}
	
	/**
	 * 删除地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:10:36
	 * @param Long id 地址id
	 * @param Boolean deleteSigns 是否删除签到信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			Boolean deleteSigns,
			HttpServletRequest request) throws Exception{
		
		addressService.delete(id, deleteSigns);
		return null;
	}
	
}
