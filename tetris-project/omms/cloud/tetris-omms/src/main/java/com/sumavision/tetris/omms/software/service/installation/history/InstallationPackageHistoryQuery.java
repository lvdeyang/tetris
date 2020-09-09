package com.sumavision.tetris.omms.software.service.installation.history;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class InstallationPackageHistoryQuery {
	@Autowired
	private InstallationPackageHistoryDAO installationPackageHistoryDAO;

	/**
	 * 据服务类型查询历史安装包<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 下午5:01:38
	 * @param serviceTypeId 服务类型
	 * @return List<InstallationPackageHistoryVO> rows 安装包列表
	 * @return int total 安装包总数
	 */
	public Map<String, Object> searchPackagePage(
			Long serviceTypeId,
			int currentPage, 
			int pageSize
			) throws Exception {
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<InstallationPackageHistoryPO> paged = installationPackageHistoryDAO.findByServiceTypeId(serviceTypeId, page);
		List<InstallationPackageHistoryPO> entities = paged.getContent();
		List<InstallationPackageHistoryVO> rows = InstallationPackageHistoryVO.getConverter(InstallationPackageHistoryVO.class).convert(entities, InstallationPackageHistoryVO.class);
		int total = installationPackageHistoryDAO.countByServiceTypeId(serviceTypeId);
		
		return new HashMapWrapper<String,Object>().put("total", total)
												  .put("rows", rows)
												  .getMap();
	}
	
}
