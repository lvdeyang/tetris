package com.sumavision.tetris.mims.app.boss;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
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
			json.put("mediaType", mediaType);
			json.put("authorId", mediaAudioPO.getAuthorId());
			FolderPO folderPO=folderDAO.findOne(mediaAudioPO.getFolderId());
		    String parentPath=folderPO.getParentPath();
		    System.out.println(parentPath);
			json.put("folderName", generateFolderStrPath(parentPath)+File.separator+folderPO.getName());
			
			JSONObject configJson=JSONObject.parseObject(readProfile());
			HttpUtil.httpPost(configJson.getString("bossAddMediaUrl"), json);
		} catch (Exception e) {
			e.printStackTrace();
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
			json.put("mediaType", mediaType);
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			json.put("uuid", uuid);
			JSONObject configJson=JSONObject.parseObject(readProfile());
			HttpUtil.httpPost(configJson.getString("bossPlayRecordUrl"), json);
		} catch (Exception e) {
			e.printStackTrace();
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
	public Map<String, Object> setMediaPrice(JSONArray medias){
		Set<String> mediaAudioIds = new HashSet<String>();
		List<JSONObject> mediaMessage = new ArrayList<JSONObject>();
		Map<String, Object> data = new HashMap<String, Object>();
		String code = null;
		try {
			for (int i = 0; i < medias.size(); i++) {
				JSONObject jsonObject = medias.getJSONObject(i);
				mediaMessage.add(jsonObject);
				String mediaId = jsonObject.getString("mediaId");
				mediaAudioIds.add(mediaId);
			}
			List<MediaAudioPO> mediaAudioPOs = mediaAudioDAO.findByUuidIn(mediaAudioIds);
			Set<Long> mediaPriceIds = new HashSet<Long>();
			for(MediaAudioPO MediaAudioPO:mediaAudioPOs){
				mediaPriceIds.add(MediaAudioPO.getId());
			}
			List<MediaPricePO> mediaPricePOs = mediaPriceDao.findByMediaIdIn(mediaPriceIds);
			Set<MediaAudioPO> mediaAudioPOsNot = new HashSet<MediaAudioPO>();
			for (int i = 0; i < medias.size(); i++) {
				JSONObject jsonObject = medias.getJSONObject(i);
				Long price = jsonObject.getLongValue("price");
				String mediaId = jsonObject.getString("mediaId");
				if ( mediaAudioPOs!=null && !mediaAudioPOs.isEmpty()) {
					for (int j = 0; j < mediaAudioPOs.size(); j++) {
						if (mediaAudioPOs.get(j).getUuid().equals(mediaId)) {
							if(mediaPricePOs!=null&&!mediaPricePOs.isEmpty()){
								for (MediaPricePO mediaPricePO : mediaPricePOs) {
									if (mediaAudioPOs.get(j).getId().equals(mediaPricePO.getMediaId())) {
										mediaPricePO.setPrice(price);
										if (mediaPricePO.getTruePrice() != 0) {
											mediaPricePO.setTruePrice(price);
										}
										if (mediaAudioPOsNot != null && !mediaAudioPOsNot.isEmpty()) {
											for (MediaAudioPO mediaAudioPOsN : mediaAudioPOsNot) {
												if (mediaAudioPOsN.equals(mediaAudioPOs.get(j))) {
													mediaAudioPOsNot.remove(mediaAudioPOs.get(j));
													break;
												}
											}
										}
										mediaAudioPOs.remove(mediaAudioPOs.get(j));
										break;
									}else mediaAudioPOsNot.add(mediaAudioPOs.get(j));
								}
							}else mediaAudioPOsNot.add(mediaAudioPOs.get(j));
						}else{
							mediaAudioPOsNot.add(mediaAudioPOs.get(j));
						}
					}
				}
			}
			List<MediaPricePO> mediaPricePOSave = new ArrayList<MediaPricePO>();
			for (int i = 0; i < medias.size(); i++) {
				JSONObject jsonObject = medias.getJSONObject(i);
				Long price = jsonObject.getLongValue("price");
				String mediaId = jsonObject.getString("mediaId");
				MediaPricePO mediaPricePO = new MediaPricePO();
				if(mediaAudioPOsNot !=null &&!mediaAudioPOsNot.isEmpty()){
					for (MediaAudioPO mediaAudioPO : mediaAudioPOsNot) {
						if(mediaAudioPO.getUuid().equals(mediaId)){
							mediaPricePO.setMediaId(mediaAudioPO.getId());
							mediaPricePO.setMediaType(MediaType.AUDIO);
							mediaPricePO.setPrice(price);
							mediaPricePOSave.add(mediaPricePO);
						}
					}
				}
			}
			mediaPriceDao.save(mediaPricePOs);
			mediaPriceDao.save(mediaPricePOSave);
			code = "0";
		} catch (Exception e) {
			e.printStackTrace();
			code = "同步价格失败！";
		}
		data.put("code", code);
		return data;
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
		String result="";
		if(folderIdPath==null||"".equals(folderIdPath)) return result;
		String[] folderIds=folderIdPath.split("/");
		for (String idstr : folderIds) {
			if("".equals(idstr)) continue;
			FolderPO folder=folderDAO.findOne(Long.parseLong(idstr));
			result+=File.separator+folder.getName();
		}
		return result;
	}
	
	
	
	/**
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
	
	/**
	 * 开启同步计费<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月15日 下午3:02:55
	 * @param String charged “0”表示不启用计费，“1”表示启用计费。初始状态为不启用计费。
	 * @return code 0：成功，其它：失败
	 * 
	 */
	public Map<String, Object> productCharge(String charged) throws Exception{
		Map<String, Object> data = new HashMap<String, Object>();
		String code = null;
		try {
			List<MediaPricePO> mediaPricePOs = mediaPriceDao.findAll();
			List<MediaPricePO> mediaPricePOList = new ArrayList<MediaPricePO>();
			if(!mediaPricePOs.isEmpty()){
				if(charged.equals("1")){
					for (MediaPricePO mediaPricePO : mediaPricePOs) {
						if(mediaPricePO.getPrice() != 0){
							mediaPricePO.setTruePrice(mediaPricePO.getPrice());
						}else{
							mediaPricePO.setPrice(0l);
							mediaPricePO.setTruePrice(0l);
						}
						mediaPricePOList.add(mediaPricePO);
					}
					mediaPriceDao.save(mediaPricePOList);
					code = "0";
				}
				else if (charged.equals("0")) {
					for (MediaPricePO mediaPricePO : mediaPricePOs) {
						mediaPricePO.setTruePrice(0l);
						mediaPricePOList.add(mediaPricePO);
					}
					mediaPriceDao.save(mediaPricePOList);
					code = "0";
				}else {
					code = "计费同步失败！";
				}
			}else {
				code = "无商品价格！";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			code = "计费同步失败！";
		}
		data.put("code", code);
		return data;
	}
}
