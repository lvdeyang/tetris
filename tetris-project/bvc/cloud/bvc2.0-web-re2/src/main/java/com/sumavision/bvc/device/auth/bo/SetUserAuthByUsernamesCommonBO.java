package com.sumavision.bvc.device.auth.bo;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetUserAuthByUsernamesCommonBO {
	private ArrayList<String> usernames = new ArrayList<String>();

	public void setUsernames(ArrayList<String> stringArray) {
		this.usernames = stringArray;
	}
	
	//通过jsonArray直接赋值（未测试）
	public void setUsernames(JSONArray jsonArray) {
		ArrayList<String> ss = new ArrayList<String>();
		for(int i=0; i<jsonArray.size(); i++){
			ss.add(jsonArray.getString(i));
		}
		this.usernames = ss;
	}
}
