package com.sumavision.tetris.omms.software.service.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/service/type")
public class ServiceTypeController {

	@Autowired
	private ServiceTypeDAO serviceTypeDao;
	
	@Autowired
	private ServiceTypeService serviceTypeService;
	
	/**
	 * 查询所有服务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月20日 下午3:55:56
	 * @return List<OmmsSoftwareServiceTypeTreeNodeVO> 服务类型树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/all")
	public Object findAll(HttpServletRequest request) throws Exception{
		
		List<ServiceTypePO> entities = serviceTypeDao.findAll();
		Collections.sort(entities, new Comparator<ServiceTypePO>() {
			@Override
			public int compare(ServiceTypePO o1, ServiceTypePO o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		GroupType[] values = GroupType.values();
		List<GroupType> groupTypes = Arrays.asList(values);
		Collections.sort(groupTypes, new Comparator<GroupType>(){
			@Override
			public int compare(GroupType o1, GroupType o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		List<OmmsSoftwareServiceTypeTreeNodeVO> roots = new ArrayList<OmmsSoftwareServiceTypeTreeNodeVO>();
		for(int i=0; i<groupTypes.size(); i++){
			roots.add(new OmmsSoftwareServiceTypeTreeNodeVO().set(groupTypes.get(i)));
		}
		
		for(int i=0; i<entities.size(); i++){
			for(int j=0; j<roots.size(); j++){
				if(entities.get(i).getGroupType().getName().equals(roots.get(j).getName())){
					roots.get(j).getChildren().add(new OmmsSoftwareServiceTypeTreeNodeVO().set(entities.get(i)));
					break;
				}
			}
		}
		return roots;
	}
	
	/**
	 * 一键创建服务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月20日 下午4:22:00
	 * @return List<ServiceTypeVO> 创建的服务类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/one/button/create")
	public Object oneButtonCreate(HttpServletRequest request) throws Exception{
		return serviceTypeService.oneButtonCreate();
	}
	
}
