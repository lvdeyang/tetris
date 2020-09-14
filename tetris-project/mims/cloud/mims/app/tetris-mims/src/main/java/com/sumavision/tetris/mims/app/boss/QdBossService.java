package com.sumavision.tetris.mims.app.boss;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mims.app.boss.util.HttpUtil;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;

/**
 * 
 * 向boss同步媒资<br/>
 * <p>同步媒资</p>
 * <b>作者:</b>Mr.h<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月19日 上午11:46:02
 */
@Service
public class QdBossService {
	@Autowired
	private MediaAudioDAO mediaAudioDAO;
	@Autowired
	private FolderDAO folderDAO;
	@Autowired
	private MediaPriceDAO mediaPriceDao;
	/**
	 * 添加媒资
	 * 方法概述<br/>
	 * <p>向boss添加媒资</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 上午11:46:35
	 * @param mediaId
	 * @param mediaType 媒资类型：video audio live
	 */
	
	public void addMedia(long mediaId,MediaType mediaType){
		try {
			JSONObject json=new JSONObject();
			MediaAudioPO mediaAudioPO=mediaAudioDAO.findOne(mediaId);
			json.put("mediaId", mediaAudioPO.getUuid());
			json.put("mediaName", mediaAudioPO.getName());
			json.put("authorName", mediaAudioPO.getName());
			json.put("authorId", mediaAudioPO.getAuthorId());
			json.put("folderId", mediaAudioPO.getFolderId());
			FolderPO folderPO=folderDAO.findOne(mediaAudioPO.getFolderId());
		    String parentPath=folderPO.getParentPath();
			json.put("folderName", generateFolderStrPath(parentPath)+File.separator+folderPO.getName());
			
			JSONObject configJson=JSONObject.parseObject(readProfile());
			HttpUtil.httpPost(configJson.getString("bossAddMediaUrl"), json);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 播放记录同步到boss
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 下午1:42:55
	 * @param mediaId 媒资的uuid
	 * @param userId 用户的uuid
	 * @param mediaType 媒资类型
	 */
	public void playMedia(long mediaId,String userId, MediaType mediaType){
		try {
			JSONObject json=new JSONObject();
			MediaAudioPO mediaAudioPO=mediaAudioDAO.findOne(mediaId);
			json.put("mediaId", mediaAudioPO.getUuid());
			json.put("userId",userId);
			JSONObject configJson=JSONObject.parseObject(readProfile());
			HttpUtil.httpPost(configJson.getString("bossPlayRecordUrl"), json);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 设置boss上传的媒资价格
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 下午1:43:00
	 * @param mediaId 媒资的uuid
	 * @param mediaType 媒资类型
	 * @param price 价格
	 */
	public void setMediaPrice(String mediaId, MediaType mediaType,long price){
		try {
			MediaAudioPO mediaAudioPO=mediaAudioDAO.findByUuid(mediaId);
			List<MediaPricePO> mediaPricePOs=mediaPriceDao.findByMediaIdAndMediaType(mediaAudioPO.getId(), mediaType);
			if(mediaPricePOs!=null&&!mediaPricePOs.isEmpty()){
				mediaPricePOs.get(0).setPrice(price);
				mediaPriceDao.save(mediaPricePOs.get(0));
			}else{
				MediaPricePO mediaPricePO=new MediaPricePO();
				mediaPricePO.setMediaId(mediaAudioPO.getId());
				mediaPricePO.setMediaType(mediaType);
				mediaPricePO.setPrice(price);
				mediaPriceDao.save(mediaPricePO);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 路径id转换为路径名称字符串
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 上午11:48:18
	 * @param folderIdPath
	 * @return 路径名称字符串
	 */
	private String generateFolderStrPath(String folderIdPath){
		String[] folderIds=folderIdPath.split(File.separator);
		String result="";
		for (String idstr : folderIds) {
			FolderPO folder=folderDAO.findOne(Long.parseLong(idstr));
			result+=File.separator+folder.getName();
		}
		return result;
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
