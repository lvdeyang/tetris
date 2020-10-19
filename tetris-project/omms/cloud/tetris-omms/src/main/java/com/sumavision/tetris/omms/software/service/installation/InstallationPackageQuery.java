package com.sumavision.tetris.omms.software.service.installation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstallationPackageQuery {

	@Autowired
	private InstallationPackageDAO installationPackageDao;
	
	/**
	 * 查询服务安装包<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月27日 下午1:33:41
	 * @param Long serviceTypeId 服务类型id
	 * @return List<InstallationPackageVO> 安装包列表
	 */
	public List<InstallationPackageVO> load(Long serviceTypeId) throws Exception{
		List<InstallationPackagePO> entities = installationPackageDao.findByServiceTypeId(serviceTypeId);
		return InstallationPackageVO.getConverter(InstallationPackageVO.class).convert(entities, InstallationPackageVO.class);
	}
	
}
