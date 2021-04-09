package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.model.agenda.AgendaQuery;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.model.agenda.AgendaVO;
import com.sumavision.tetris.bvc.model.role.RoleVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/virtual/source")
public class VirtualSourceController {

	@Autowired
	private AgendaQuery agendaQuery;
	
	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private VirtualSourceService virtualSourceService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	/**
	 * 查询议程业务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月30日 上午8:45:29
	 * @return Set<String> 业务类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/business/types")
	public Object queryBusinessTypes(HttpServletRequest request) throws Exception{
		
		return agendaQuery.queryBusinessTypes();
	}
	
	/**
	 * 根据业务查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 上午11:46:04
	 * @param Long businessId 业务id
	 * @param BusinessInfoType businessInfoType 业务类型
	 * @return List<AgendaVO> 议程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/business/id")
	public Object loadByBusinessId(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		return agendaQuery.loadByBusinessIdAndBusinessInfoType(businessId, BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE.toString());
	}
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Long businessId 关联业务id
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String remark,
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		AgendaVO agenda =  virtualSourceService.add(name, remark, businessId);
		
		
		return agenda;
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		return agendaService.edit(id, name, remark, "合屏虚拟源");
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		agendaService.delete(id);
		return null;
	}
	
	/**
	 * 根据业务查询合屏虚拟源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月23日 下午3:20:27
	 * @param businessId groupId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object andVirtualSourceLoadByGroupIdAndTypeWithChannel(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		List<AgendaVO> virtualAgendas = agendaQuery.loadByBusinessIdAndBusinessInfoType(businessId, BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE.toString());
		
		return new HashMapWrapper<String, Object>()
				.put("virtualSources", virtualAgendas)
				.getMap();
	}
	
}
