package com.sumavision.tetris.easy.process.core;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.easy.process.core.exception.ProcessParamReferenceNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/process/param/reference")
public class ProcessParamReferenceController {

	@Autowired
	private ProcessParamReferenceDAO processParamReferenceDao;
	
	/**
	 * 查询流程下的参数映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 上午10:37:35
	 * @param Long processId 流程id
	 * @return List<ProcessParamReferenceVO> 参数映射列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long processId,
			HttpServletRequest request) throws Exception{
		
		List<ProcessParamReferencePO> entities = processParamReferenceDao.findByProcessId(processId);
		
		List<ProcessParamReferenceVO> references = ProcessParamReferenceVO.getConverter(ProcessParamReferenceVO.class).convert(entities, ProcessParamReferenceVO.class);
		
		return references;
	}
	
	/**
	 * 添加一个流程参数映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 上午10:39:44
	 * @param Long processId 流程id
	 * @param JSONArray reference 参数主键路径列表
	 * @return ProcessParamReferenceVO 参数映射
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long processId,
			String reference,
			HttpServletRequest request) throws Exception{
		
		ProcessParamReferencePO entity = new ProcessParamReferencePO();
		entity.setUpdateTime(new Date());
		entity.setProcessId(processId);
		
		if(reference!=null && !"".equals(reference)){
			List<String> primaryKeys = JSON.parseArray(reference, String.class);
			entity.setReference(primaryKeys);
		}
		
		processParamReferenceDao.save(entity);
		
		return new ProcessParamReferenceVO().set(entity);
	}
	
	/**
	 * 修改参数映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 上午11:19:02
	 * @param @PathVariable Long id 映射id
	 * @param JSONArray reference 参数主键路径列表
	 * @return ProcessParamReferenceVO 参数映射
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String reference,
			HttpServletRequest request) throws Exception{
		
		ProcessParamReferencePO entity = processParamReferenceDao.findOne(id);
		if(entity == null){
			throw new ProcessParamReferenceNotExistException(id);
		}
		
		if(reference==null || "".equals(reference)) return new ProcessParamReferenceVO().set(entity);
		
		entity.setReference(JSON.parseArray(reference, String.class));
		
		processParamReferenceDao.save(entity);
		
		return new ProcessParamReferenceVO().set(entity);
	}
	
	/**
	 * 删除一个参数映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 上午11:22:49
	 * @param @PathVariable Long id 参数映射id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		ProcessParamReferencePO entity = processParamReferenceDao.findOne(id);
		if(entity != null){
			processParamReferenceDao.delete(entity);
		}
		
		return null;
	}
	
}
