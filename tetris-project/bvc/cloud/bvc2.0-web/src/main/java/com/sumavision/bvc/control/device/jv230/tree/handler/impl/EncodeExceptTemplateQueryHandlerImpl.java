package com.sumavision.bvc.control.device.jv230.tree.handler.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.jv230.tree.handler.QueryHandler;

public class EncodeExceptTemplateQueryHandlerImpl extends QueryHandler<JSONObject>{
	
	public EncodeExceptTemplateQueryHandlerImpl(){}
	
	public EncodeExceptTemplateQueryHandlerImpl(JSONObject params){
		super(params);
	}

	@Override
	public List<FolderPO> getInstitutionList() {
		Long instId = this.getParams().getLong("instId");
		return folderDao.findChildFolderByFolderId(instId);
	}

	@Override
	public List<BundlePO> getDeviceList() throws Exception {
		Long instId = this.getParams().getLong("instId");
		Long accountId = this.getParams().getLong("accountId");
		
		return resourceQueryUtil.queryInstUseableBundles(accountId, instId);
	}
	
}
