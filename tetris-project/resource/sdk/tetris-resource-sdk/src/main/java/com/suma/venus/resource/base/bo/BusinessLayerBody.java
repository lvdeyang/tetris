package com.suma.venus.resource.base.bo;

public class BusinessLayerBody {

	/**业务层ID*/
	private String business_layer_id;
	
	private String business_layer_type;
	
	public BusinessLayerBody() {
	}

	public BusinessLayerBody(String business_layer_id,String business_layer_type) {
		this.business_layer_id = business_layer_id;
		this.business_layer_type = business_layer_type;
	}

	public String getBusiness_layer_id() {
		return business_layer_id;
	}

	public void setBusiness_layer_id(String business_layer_id) {
		this.business_layer_id = business_layer_id;
	}

	public String getBusiness_layer_type() {
		return business_layer_type;
	}

	public void setBusiness_layer_type(String business_layer_type) {
		this.business_layer_type = business_layer_type;
	}
	
}
