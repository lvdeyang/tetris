package com.sumavision.tetris.cs.area;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cs.bak.AreaSendPO;
import com.sumavision.tetris.cs.bak.AreaSendQuery;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;

@Component
public class AreaQuery {
	@Autowired
	AreaDAO areaDao;

	@Autowired
	ChannelQuery channelQuery;

	@Autowired
	AreaSendQuery areaSendQuery;

	public Object getAreaList(Long channelId) throws Exception {
		List<AreaVO> returnList = new ArrayList<AreaVO>();
		ChannelPO channel = channelQuery.findByChannelId(channelId);

		List<AreaData> allArea = getAllArea();

		List<AreaSendPO> abledArea = areaSendQuery.getArea(channelId);

		List<AreaVO> areas = this.setDisabled(channelId, 0l, allArea, abledArea);

		List<Long> checkList = new ArrayList<Long>();

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

	public List<AreaVO> getCheckArea(Long channelId) throws Exception {
		List<AreaVO> returnList = new ArrayList<AreaVO>();
		List<AreaPO> areaList = areaDao.findByChannelId(channelId);
		if (areaList != null && areaList.size() > 0) {
			for(AreaPO item:areaList){
				returnList.add(new AreaVO().set(item));
			}
		}
		return returnList;
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

	private List<AreaData> getAllArea() {
		List<AreaData> returnList = new ArrayList<AreaData>();

		AreaData area6 = new AreaData();
		area6.setId(6l);
		area6.setName("国贸");

		AreaData area5 = new AreaData();
		area5.setId(5l);
		area5.setName("朝阳区");
		List<AreaData> list3 = new ArrayList<AreaData>();
		list3.add(area6);
		area5.setSubColumns(list3);

		AreaData area4 = new AreaData();
		area4.setId(4l);
		area4.setName("五道口");
		
		AreaData area7 = new AreaData();
		area7.setId(7l);
		area7.setName("五街");

		AreaData area3 = new AreaData();
		area3.setId(3l);
		area3.setName("上地");
		List<AreaData> list4 = new ArrayList<AreaData>();
		list4.add(area7);
		area3.setSubColumns(list4);

		AreaData area2 = new AreaData();
		area2.setId(2l);
		area2.setName("海淀区");
		List<AreaData> list1 = new ArrayList<AreaData>();
		list1.add(area3);
		list1.add(area4);
		area2.setSubColumns(list1);

		AreaData area1 = new AreaData();
		area1.setId(1l);
		area1.setName("北京市");
		List<AreaData> list2 = new ArrayList<AreaData>();
		list2.add(area2);
		list2.add(area5);
		area1.setSubColumns(list2);

		returnList.add(area1);

		return returnList;
	}

	public class AreaData {
		private Long id;
		private String name;
		private String unique;
		private List<AreaData> subColumns;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUnique() {
			return unique;
		}

		public void setUnique(String unique) {
			this.unique = unique;
		}

		public List<AreaData> getSubColumns() {
			return subColumns;
		}

		public void setSubColumns(List<AreaData> subColumns) {
			this.subColumns = subColumns;
		}
	}
}
