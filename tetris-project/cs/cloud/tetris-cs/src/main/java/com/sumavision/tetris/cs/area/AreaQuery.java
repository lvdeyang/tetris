package com.sumavision.tetris.cs.area;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.cs.bak.AreaSendPO;
import com.sumavision.tetris.cs.bak.AreaSendQuery;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelQuery;

@Component
public class AreaQuery {
	@Autowired
	AreaDAO areaDao;

	@Autowired
	ChannelQuery channelQuery;

	@Autowired
	AreaSendQuery areaSendQuery;

	@Autowired
	DivisionDAO divisionDAO;

	/**
	 * 获取地区根目录(当前以空地区id获取根)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<AreaPO> 地区列表
	 */
	public Object getRootList(Long channelId) throws Exception {
		return getChildList(channelId, "", true);
	}

	/**
	 * 获取子地区目录(当前使用的平台地区根id为3204000000，平台数据类型转本地地区数据类型)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<AreaPO> 地区列表
	 */
	public Object getChildList(Long channelId, String areaId, Boolean disabled) throws Exception {
		List<AreaData> allArea;
		if (areaId.isEmpty()) {
			allArea = getRootDivision();
		} else {
			allArea = getChildDivision(areaId);
		}
		List<AreaSendPO> abledArea = areaSendQuery.getArea(channelId);

		List<AreaVO> areas;
		if (!disabled) {
			areas = new ArrayList<AreaVO>();
			for (AreaData item : allArea) {
				AreaPO areaPO = new AreaPO();
				areaPO.setAreaId(item.getId());
				areaPO.setChannelId(channelId);
				areaPO.setName(item.getName());
				AreaVO areaVO = new AreaVO().set(areaPO).setDisabled(false);
				areas.add(areaVO);
			}
		} else {
			areas = this.setDisabled(channelId, allArea, abledArea);
		}

		List<String> checkList = new ArrayList<String>();

		if (allArea != null && allArea.size() > 0) {
			for (AreaData item : allArea) {
				if (areaDao.findByChannelIdAndAreaId(channelId, item.getId()) != null) {
					checkList.add(item.getId());
				}
			}
		}
		// List<AreaPO> areaList = areaDao.findByChannelId(channelId);
		// if (areaList != null && areaList.size() > 0) {
		// for (int i = 0; i < areaList.size(); i++) {
		// checkList.add(areaList.get(i).getAreaId());
		// }
		// }

		JSONObject returnObject = new JSONObject();
		returnObject.put("treeData", areas);
		returnObject.put("checkList", checkList);

		return returnObject;
	}

	public List<AreaData> getRootDivision() throws Exception {
		return getChildDivision("3204000000");
	}

	/**
	 * 获取子地区目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<AreaData> 平台的地区数据类型数列
	 */
	public List<AreaData> getChildDivision(String areaId) throws Exception {
		List<AreaData> returnList = new ArrayList<AreaData>();

		String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
		if (!url.isEmpty()) {
			JSONObject jsonObject = HttpRequestUtil
					.httpGet("http://" + url + "/ed/ed/regiondivision/queryDivisionTree?division=" + areaId);

			if (jsonObject != null && jsonObject.containsKey("divisionTrees")
					&& jsonObject.get("divisionTrees") != null) {
				JSONArray jsonArray = jsonObject.getJSONArray("divisionTrees");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject item = (JSONObject) jsonArray.get(i);
					AreaData areaData = new AreaData();
					areaData.setId(item.get("division").toString());
					areaData.setName(item.get("name").toString());
					areaData.setType("division");
					areaData.setSubColumns(new ArrayList<AreaQuery.AreaData>());
					returnList.add(areaData);
				}
			}
		}

		return returnList;
	}

	/**
	 * 设置目的是否可用(播发过的频道未播发的地区则下次播发不可用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<AreaData> 平台的地区数据类型数列
	 */
	private List<AreaVO> setDisabled(Long channelId, List<AreaData> allArea, List<AreaSendPO> abledArea)
			throws Exception {
		List<AreaVO> returnList = new ArrayList<AreaVO>();
		if (allArea != null && allArea.size() > 0) {
			for (AreaData item : allArea) {
				AreaPO areaPO = new AreaPO();
				areaPO.setAreaId(item.getId());
				areaPO.setChannelId(channelId);
				areaPO.setName(item.getName());
				AreaVO areaVO = new AreaVO().set(areaPO);

				if (abledArea != null && abledArea.size() > 0) {
					for (AreaSendPO sendItem : abledArea) {
						if (sendItem.getAreaId().equals(item.getId())) {
							areaVO.setDisabled(false);
							break;
						}
					}
				} else {
					areaVO.setDisabled(false);
				}
				returnList.add(areaVO);
			}
		}
		return returnList;
	}

	/**以下为测试使用方法
	 */
	public Object getAreaList(Long channelId) throws Exception {
		List<AreaData> allArea = getAllArea();

		List<AreaSendPO> abledArea = areaSendQuery.getArea(channelId);

		List<AreaVO> areas = this.setDisabled(channelId, 0l, allArea, abledArea);

		List<String> checkList = new ArrayList<String>();
		List<AreaPO> areaList = areaDao.findByChannelId(channelId);
		if (areaList != null && areaList.size() > 0) {
			for (int i = 0; i < areaList.size(); i++) {
				checkList.add(areaList.get(i).getAreaId());
			}
		}

		JSONObject returnObject = new JSONObject();
		returnObject.put("treeData", areas);
		returnObject.put("checkList", checkList);

		return returnObject;
	}

	private List<AreaVO> setDisabled(Long channelId, Long parentId, List<AreaData> allArea, List<AreaSendPO> abledArea)
			throws Exception {
		List<AreaVO> returnList = new ArrayList<AreaVO>();
		if (allArea != null && allArea.size() > 0) {
			for (AreaData item : allArea) {
				AreaPO areaPO = new AreaPO();
				areaPO.setAreaId(item.getId());
				areaPO.setChannelId(channelId);
				areaPO.setName(item.getName());
				if (parentId != null && parentId != 0l)
					areaPO.setParentId(parentId);
				AreaVO areaVO = new AreaVO().set(areaPO);

				if (abledArea != null && abledArea.size() > 0) {
					for (AreaSendPO sendItem : abledArea) {
						if (sendItem.getAreaId() == item.getId()) {
							areaVO.setDisabled(false);
							break;
						}
					}
				} else {
					areaVO.setDisabled(false);
				}
				if (item.getSubColumns() != null && item.getSubColumns().size() > 0) {
					areaVO.setSubColumns(this.setDisabled(channelId, areaVO.getId(), item.getSubColumns(), abledArea));
				}
				returnList.add(areaVO);
			}
		}
		return returnList;
	}

	public List<AreaVO> getCheckArea(Long channelId) throws Exception {
		List<AreaVO> returnList = new ArrayList<AreaVO>();
		List<AreaPO> areaList = areaDao.findByChannelId(channelId);
		if (areaList != null && areaList.size() > 0) {
			for (AreaPO item : areaList) {
				returnList.add(new AreaVO().set(item));
			}
		}
		return returnList;
	}

	public List<String> getCheckAreaIdList(Long channelId) throws Exception {
		List<String> returnList = new ArrayList<String>();

		List<AreaVO> checkAreaVOs = getCheckArea(channelId);
		if (checkAreaVOs != null && checkAreaVOs.size() > 0) {
			for (AreaVO item : checkAreaVOs) {
				returnList.add(item.getAreaId());
			}
		}

		return returnList;
	}

	private List<AreaData> getAllArea() {
		List<AreaData> returnList = new ArrayList<AreaData>();

		AreaData area6 = new AreaData();
		area6.setId("6");
		area6.setName("国贸");

		AreaData area5 = new AreaData();
		area5.setId("5");
		area5.setName("朝阳区");
		List<AreaData> list3 = new ArrayList<AreaData>();
		list3.add(area6);
		area5.setSubColumns(list3);

		AreaData area4 = new AreaData();
		area4.setId("4");
		area4.setName("五道口");

		AreaData area7 = new AreaData();
		area7.setId("7");
		area7.setName("五街");

		AreaData area3 = new AreaData();
		area3.setId("3");
		area3.setName("上地");
		List<AreaData> list4 = new ArrayList<AreaData>();
		list4.add(area7);
		area3.setSubColumns(list4);

		AreaData area2 = new AreaData();
		area2.setId("2");
		area2.setName("海淀区");
		List<AreaData> list1 = new ArrayList<AreaData>();
		list1.add(area3);
		list1.add(area4);
		area2.setSubColumns(list1);

		AreaData area1 = new AreaData();
		area1.setId("1");
		area1.setName("北京市");
		List<AreaData> list2 = new ArrayList<AreaData>();
		list2.add(area2);
		list2.add(area5);
		area1.setSubColumns(list2);

		returnList.add(area1);

		return returnList;
	}

	public class AreaData {
		private String id;
		private String name;
		private String type;
		private List<AreaData> subColumns;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<AreaData> getSubColumns() {
			return subColumns;
		}

		public void setSubColumns(List<AreaData> subColumns) {
			this.subColumns = subColumns;
		}
	}
}
