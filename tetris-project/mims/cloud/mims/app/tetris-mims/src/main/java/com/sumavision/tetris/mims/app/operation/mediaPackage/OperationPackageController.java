package com.sumavision.tetris.mims.app.operation.mediaPackage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/operation/package")
public class OperationPackageController {

	@Autowired
	private OperationPackageService operationPackageService;
	
	@Autowired
	private OperationPackageQuery operationPackageQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取套餐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午3:55:41
	 * @return List<OperationPackagePO> 套餐列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/get")
	public Object getList(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return operationPackageQuery.queryPackageList(user.getGroupId());
	}
	
	/**
	 * 添加套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:38:23
	 * @param String name 套餐名
	 * @param Long price 价格
	 * @param String remark 备注
	 * @return OperationPackageVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String name, Long price, String remark, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		return operationPackageService.add(user.getGroupId(), name, price, remark);
	}
	
	/**
	 * 编辑套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:38:23
	 * @param Long id 套餐id
	 * @param String name 套餐名
	 * @param Long price 价格
	 * @param String remark 备注
	 * @return OperationPackageVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			Long price,
			String remark,
			HttpServletRequest request) throws Exception {
		return operationPackageService.edit(id, name, price, remark);
	}
	
	/**
	 * 删除套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午5:30:32
	 * @param Long packageId 套餐id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id, HttpServletRequest request) throws Exception {
		operationPackageService.delete(id);
		return null;
	}
}
