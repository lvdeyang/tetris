package com.sumavision.tetris.mims.app.folder;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 公司角色feign接口<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月25日 下午7:05:08
 */
@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface FolderRolePermissionFeign {
	
	/**
	 * 解除文件夹授权<br/>
	 * <p>包括文件夹的子文件夹一并解除授权</p>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:30:46
	 * @param SubordinateRoleVO role 待解除授权的角色
	 */
	@RequestMapping(value = "/folder/feign/delete/by/role")
	public JSONObject deleteByRole(@RequestParam("roleId") String roleId)throws Exception;
	
}
