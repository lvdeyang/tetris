package com.suma.venus.resource.base.bo;

public class GetTokenResultBO extends TokenResultBO{

	private AccessToken access_token;

	public AccessToken getAccess_token() {
		return access_token;
	}

	public void setAccess_token(AccessToken access_token) {
		this.access_token = access_token;
	}

}
