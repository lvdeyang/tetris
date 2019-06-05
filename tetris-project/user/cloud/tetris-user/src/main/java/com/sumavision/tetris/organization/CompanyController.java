package com.sumavision.tetris.organization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/company")
public class CompanyController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CompanyQuery companyQuery;
	
	/**
	 * 分页查询公司<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 上午11:44:44
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<CompanyVO> rows 公司列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return companyQuery.list(currentPage, pageSize);
	}
	
	/**
	 * 获取用户公司信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月24日 下午4:54:11
	 * @return CompanyVO 修改后的数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/subordinate")
	public Object company(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		if(user.getId() != null){
			return companyQuery.findByUserId(user.getId());
		}else if(user.getUuid() != null){
			return companyQuery.findByUserId(Long.parseLong(user.getUuid()));
		}else {
			return null;
		}
	}
}
