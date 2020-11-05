package com.sumavision.bvc.control.device.jv230.tree.handler.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.jv230.tree.handler.QueryHandler;

public class RootExceptSuperQueryhandlerImpl extends QueryHandler<JSONObject>{

	@Override
	public List<FolderPO> getInstitutionList() {
		System.out.print(folderDao);
		return folderDao.findRootFolder();
	}

	@Override
	public List<BundlePO> getDeviceList() {
		return null;
	}

}
