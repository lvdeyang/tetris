package com.sumavision.bvc.control.device.jv230.tree.handler;

import java.util.List;

import org.springframework.context.annotation.DependsOn;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceFolderDAO;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.model.BaseHandler;

@DependsOn("SpringContextUtil")
public abstract class QueryHandler<T> extends BaseHandler<T> {
	
	protected ResourceFolderDAO folderDao = SpringContext.getBean(ResourceFolderDAO.class);
	
	protected ResourceBundleDAO bundleDao = SpringContext.getBean(ResourceBundleDAO.class);
	
	protected ResourceQueryUtil resourceQueryUtil = SpringContext.getBean(ResourceQueryUtil.class);
	
	//获取组织列表
	public abstract List<FolderPO> getInstitutionList();
	
	//获取设备列表
	public abstract List<BundlePO> getDeviceList() throws Exception;
	
	public QueryHandler(){}
	
	public QueryHandler(T params){
		super(params);
	}
	
}
