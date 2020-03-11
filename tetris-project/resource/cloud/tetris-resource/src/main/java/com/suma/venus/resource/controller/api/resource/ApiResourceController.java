package com.suma.venus.resource.controller.api.resource;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/api/zoom/jv220/resource")
public class ApiResourceController {
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private UserQueryService userQueryService;

	/**
	 * 资源上线/离线<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午9:42:49
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/status")
	public Object updateJv220Status(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");
		String status = requestWrapper.getString("status");
		String local_layer_id = requestWrapper.getString("local_layer_id");
		
		UserBO user = userQueryService.current();
		
		BundlePO bundle = bundleDao.findByUserIdAndDeviceModel(user.getId(), "jv220");
		
		String responseStatus = "";
		
		if(bundle != null){
			if("online".equals(status)){
				responseStatus = "user_online";
				bundle.setOnlineStatus(ONLINE_STATUS.ONLINE);
			}
			if("offline".equals(status)){
				responseStatus = "user_offline";
				bundle.setOnlineStatus(ONLINE_STATUS.OFFLINE);
			}
			if(local_layer_id != null){
				bundle.setAccessNodeUid(local_layer_id);
			}
			
			bundleDao.save(bundle);
		}
		
		return new HashMapWrapper<String, String>().put("identify_id", identify_id)
												   .put("operate", responseStatus)
												   .put("local_user_no", local_user_no)
												   .getMap();
		
	}
	
}
