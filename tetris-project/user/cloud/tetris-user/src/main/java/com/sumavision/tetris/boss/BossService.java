package com.sumavision.tetris.boss;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.boss.util.HttpUtil;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;

/**
 * 
 * 对接boss系统<br/>
 * <p>同步用户</p>
 * <b>作者:</b>Mr.h<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月19日 上午11:03:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BossService {


	@Autowired
	private UserDAO userDao;
	/**
	 * 
	 * 添加用户接口<br/>
	 * <p>向boss添加用户，不判断是否成功</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 上午11:04:36
	 * @param userId
	 */
	public void addUser(long userId){
		try {
			JSONObject json=new JSONObject();
			UserPO userPO=userDao.findOne(userId);
			json.put("userId", userId);
			json.put("userName", userPO.getUsername());
			json.put("userUuid", userPO.getUuid());
			JSONObject configJson=JSONObject.parseObject(readProfile());
			HttpUtil.httpPost(configJson.getString("bossAddUserUrl"), json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * 获取配置项<br/>
	 * <p>获取配置项</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 上午11:03:49
	 * @return String json字符串
	 * @throws Exception
	 */
	public String readProfile() throws Exception{
		BufferedReader in = null;
		InputStreamReader reader = null;
		String json = null;
		try{
			in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("profile.json")));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null){
			    buffer.append(line);
			}
			json = buffer.toString();
		} finally{
			if(in != null) in.close();
			if(reader != null) reader.close();
		}
		return json;
	}
	
}
