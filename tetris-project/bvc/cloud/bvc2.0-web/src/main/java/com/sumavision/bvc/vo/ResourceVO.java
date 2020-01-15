package com.sumavision.bvc.vo;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.base.bo.BundleParam;
import com.suma.venus.resource.base.bo.UpdateBundleParam;
import com.suma.venus.resource.bo.CreateBundleRequest;
import com.suma.venus.resource.bo.DeleteBundleRequest;
import com.suma.venus.resource.bo.UpdateBundleRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceVO {

	private String bundle_Id;
	/**
	 * 资源名称
	 */
	private String bundle_name;// name
	/**
	 * 设备形态
	 */
	private String device_model = "ipc";
	
	/**
	 * 设备类型
	 */
	private String device_type;

	/**
	 * 资源类型
	 */
	final private String bundle_type = "VenusTerminal";// device_model
	/**
	 * e-164号码
	 */
	private String call_number;// 扩展字段
	/**
	 * 归属服务器ip
	 */
	private String device_ip;
	/**
	 * 端口
	 */
	private String device_port;
	/**
	 * 协议类型
	 */
	private String protocol;// 扩展字段
	/*
	 * 
	 * 接入层ID
	 * 
	 */
	private String access_node_uid;
	/**
	 * 设备账户
	 */

	private String username;
	/**
	 * 设备密码
	 */
	private String password;
	
	/**
	 * 分组Id 
	 */
	private Long folderId;
	
	/**
	 * 设备经度
	 */
	private String device_longitude;
	
	/**
	 *设备纬度
	 */
	private String device_latitude;
	
	
	public static ResourceVO parseResourVOfromBundleBody(BundleBody bundleBody){
		JSONObject resourveJson = new JSONObject();
		resourveJson.put("bundle_Id", bundleBody.getBundle_id());
		resourveJson.put("bundle_name", bundleBody.getBundle_name());
		resourveJson.put("device_model",bundleBody.getDevice_model());
		resourveJson.put("bundle_type", bundleBody.getBundle_type());
		resourveJson.put("username", bundleBody.getUsername());
		resourveJson.put("password", bundleBody.getPassword());
		resourveJson.put("folderId",bundleBody.getFolderId() ==null ? 0 : bundleBody.getFolderId() );
		resourveJson.put("call_number", bundleBody.getExtra_info() == null ? ""
																		  : bundleBody.getExtra_info().get("call_number"));
		resourveJson.put("protocol", bundleBody.getExtra_info() == null ? "" 
																	    : bundleBody.getExtra_info().get("protocol"));
		resourveJson.put("device_type",bundleBody.getExtra_info() == null ? ""
				: bundleBody.getExtra_info().get("device_type"));
		resourveJson.put("device_latitude",bundleBody.getExtra_info() == null ? ""
				: bundleBody.getExtra_info().get("device_latitude"));
		resourveJson.put("device_longitude",bundleBody.getExtra_info() == null ? ""
				: bundleBody.getExtra_info().get("device_longitude"));
		return (ResourceVO)JSONObject.toJavaObject(resourveJson, ResourceVO.class);
	}
	public CreateBundleRequest ConvertToCreateBundleRequest(ResourceVO resource) {
		BundleBody bundleBody = new BundleBody();
		bundleBody.setDevice_model(resource.getDevice_model());
		bundleBody.setBundle_name(resource.getBundle_name());
		bundleBody.setPassword(resource.getPassword());
		bundleBody.setBundle_type("VenusTerminal");
		//目前没有
		bundleBody.setAccess_node_uid(null);
		bundleBody.setUsername(resource.getUsername());
		JSONObject extraInfo = new JSONObject();
		extraInfo.put("protocol", resource.getProtocol());
		extraInfo.put("call_number", resource.getCall_number());
		extraInfo.put("device_type", resource.getDevice_type());		
		extraInfo.put("device_longitude", resource.getDevice_longitude());
		extraInfo.put("device_latitude", resource.getDevice_latitude());
		
		bundleBody.setExtra_info(extraInfo);
		CreateBundleRequest createBundleRequest = new CreateBundleRequest();
		BundleParam bundleParam = new BundleParam();
		bundleParam.setBundle(bundleBody);
		createBundleRequest.setCreate_bundle_request(bundleParam);
		return createBundleRequest;
	}

	public UpdateBundleRequest ConvertToUpadateBundleRequest(ResourceVO resource) {
		UpdateBundleRequest updateBundleRequest = new UpdateBundleRequest();
		UpdateBundleParam updateBundleParam = new UpdateBundleParam();
		JSONObject attrJson = new JSONObject();
		attrJson.put("device_longitude", resource.getDevice_longitude());
		attrJson.put("device_latitude", resource.getDevice_latitude());
		attrJson.put("protocol", resource.getProtocol());
		attrJson.put("call_number", resource.getCall_number());
		updateBundleParam.setBundle_id(resource.getBundle_Id());
		updateBundleParam.setAttrs(attrJson);
		updateBundleRequest.setUpdate_bundle_request(updateBundleParam);
		return updateBundleRequest;

	}

	public DeleteBundleRequest ConvertToDeleteBundleRequest(ResourceVO resource) {
		DeleteBundleRequest deleteBundleRequest = new DeleteBundleRequest();
		BundleBody bundleBody = new BundleBody();
		bundleBody.setBundle_id(resource.getBundle_Id());
		deleteBundleRequest.setDelete_bundle_request(bundleBody);
		return deleteBundleRequest;

	}		
}
