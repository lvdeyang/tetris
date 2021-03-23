package com.suma.venus.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.service.CloudVirtualService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserService;

@Controller
@RequestMapping(value = "/cloud/virtual" )
public class CloudVirtualController {

	@Autowired
	private CloudVirtualService cloudVirtualService;
	
	
	/**
	 * 添加虚拟设备-输出<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午1:56:55
	 * @param bundleName 设备名称
	 * @param url 输入源地址
	 * @param rateCtrl 码率控制方式 
	 * @param bitrate 系统码率
	 * @param videos 视频通道
	 * @param audios 音频通道
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/output/add")
	public Object outputAdd(String bundleName,String type, String url , String rateCtrl, String bitrate, String videos, String audios)throws Exception{
		
		return cloudVirtualService.outputAdd(bundleName,type,url,rateCtrl,bitrate,videos,audios);
	}
	
	/**
	 * 添加虚拟设备-输出<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午1:59:27
	 * @param bundleName 设备名称
	 * @param bundleId 设备id
	 * @param url 输入源地址
	 * @param rateCtrl 码率控制方式 
	 * @param bitrate 系统码率
	 * @param videos 视频通道
	 * @param audios 音频通道
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/output/modify")
	public Object outputModify(String bundleName, String bundleId, String type,String url , String rateCtrl, String bitrate, String videos, String audios)throws Exception{
		
		return cloudVirtualService.outputModify(bundleName,type, bundleId, url, rateCtrl, bitrate, videos, audios);
	}
	
	/**
	 * 删除虚拟设备-输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午2:20:09
	 * @param bundleId 设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "output/delete")
	public void outputDelete(String bundleId)throws Exception{
		
		cloudVirtualService.outputDelete(bundleId);
	}
	
	/**
	 * 查询有权限的虚拟设备-输入<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:01:44
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/input/query")
	public Object inputQuery()throws Exception{
		
		return cloudVirtualService.inputQuery();
	}
	
	/**
	 * 根据设备id列表查询设备<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:40:20
	 * @param bundleIds 设备id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/input/bundleId")
	public Object inputBundleId(String bundleIds)throws Exception{
		
		return cloudVirtualService.inputBundleId(bundleIds);
	}
	
	/**
	 * 查询有权限虚拟设备-输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:44:05
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/output/query")
	public Object outputQuery(String bundleId)throws Exception{
		
		return cloudVirtualService.outputQuery(bundleId);
	}
	
}
