package com.sumavision.tetris.bvc.model.terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class TerminalBundleQuery {

	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	/**
	 * 查询终端设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午9:03:43
	 * @param Long terminalId 终端id
	 * @return List<TerminalBundleVO> 设备列表
	 */
	public List<TerminalBundleVO> loadAll(Long terminalId) throws Exception{
		List<TerminalBundlePO> entities = terminalBundleDao.findByTerminalId(terminalId);
		return TerminalBundleVO.getConverter(TerminalBundleVO.class).convert(entities, TerminalBundleVO.class);
	}
	
	/**
	 * 分页查询终端下的设备模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 上午9:13:02
	 * @param Long terminalId 终端类型id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return rows List<TerminalBundleVO> 设备模板列表
	 */
	public Map<String, Object> load(
			Long terminalId, 
			int currentPage, 
			int pageSize) throws Exception{
		Long total = terminalBundleDao.countByTerminalId(terminalId);
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<TerminalBundlePO> pagedEntities = terminalBundleDao.findByTerminalIdOrderByBundleTypeAscNameAsc(terminalId, page);
		List<TerminalBundlePO> entities = null;
		if(pagedEntities != null) entities = pagedEntities.getContent();
		List<TerminalBundleVO> rows = TerminalBundleVO.getConverter(TerminalBundleVO.class).convert(entities, TerminalBundleVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 根据终端和类型查询设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午8:50:24
	 * @param Long terminalId 终端id
	 * @param String type 编解码类型
	 * @return List<TerminalBundleVO> 设备列表
	 */
	public List<TerminalBundleVO> loadByType(
			Long terminalId,
			String type) throws Exception{
		List<TerminalBundleType> types = new ArrayList<TerminalBundleType>();
		if(type.indexOf("ENCODE") >= 0){
			types.add(TerminalBundleType.ENCODER);
			types.add(TerminalBundleType.ENCODER_DECODER);
		}else if(type.indexOf("DECODE") >= 0){
			types.add(TerminalBundleType.DECODER);
			types.add(TerminalBundleType.ENCODER_DECODER);
		}
		List<TerminalBundlePO> entities = terminalBundleDao.findByTerminalIdAndTypeIn(terminalId, types);
		return TerminalBundleVO.getConverter(TerminalBundleVO.class).convert(entities, TerminalBundleVO.class);
	}

}
