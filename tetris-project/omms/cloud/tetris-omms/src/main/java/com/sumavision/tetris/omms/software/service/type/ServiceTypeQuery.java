package com.sumavision.tetris.omms.software.service.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.omms.software.service.installation.PropertyValueType;

@Component
public class ServiceTypeQuery {

	@Autowired
	private ServiceTypeDAO serviceTypeDao;
	
	
	/**
	 * 查询所有服务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月20日 下午3:55:56
	 * @return List<OmmsSoftwareServiceTypeTreeNodeVO> 服务类型树
	 */
	public List<OmmsSoftwareServiceTypeTreeNodeVO> findAll() throws Exception{
		
		List<ServiceTypePO> entities = serviceTypeDao.findAll();
		Collections.sort(entities, new Comparator<ServiceTypePO>() {
			@Override
			public int compare(ServiceTypePO o1, ServiceTypePO o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		Set<Long> serviceTypeIds = new HashSet<Long>();
		if(entities!=null && entities.size()>0){
			for(ServiceTypePO entity:entities){
				serviceTypeIds.add(entity.getId());
			}
		}
		
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
	 * 查询服务类型枚举<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午11:16:10
	 * @return Set<String> 查询服务类型枚举
	 */
	public Set<String> findGroupTypes(){
		Set<String> values = new HashSet<String>();
		GroupType[] types = GroupType.values();
		for(GroupType type:types){
			values.add(type.getName());
		}
		return values;
	}
	
}
