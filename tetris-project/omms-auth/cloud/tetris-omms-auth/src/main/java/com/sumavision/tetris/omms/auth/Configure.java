package com.sumavision.tetris.omms.auth;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Configure {

	/**
	 * 读取"gadgetConfig.json"文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 下午1:45:08
	 * @return String 文件内容
	 */
	public String readGadgetConfig()throws Exception{
		return readFile("gadgetConfig.json");
	}

	
	/**
	 * 读取配置文件信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 下午1:44:13
	 * @param fileName 文件名
	 * @return json
	 */
	public  String readFile(String fileName) throws Exception{
		BufferedReader in = null;
		InputStreamReader reader = null;
		String json = null;
		try{
			in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName)));
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
