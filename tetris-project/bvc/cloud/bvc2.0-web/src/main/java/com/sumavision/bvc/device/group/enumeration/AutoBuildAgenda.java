package com.sumavision.bvc.device.group.enumeration;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 自动建立的议程<br/> 
 * @author zsy
 * @date 2019年3月29日 13:33:32 
 */
public enum AutoBuildAgenda {

	CHAIRMAN_AUDIENCE("主席模式", 1L),
	ONE_VIDEO("全局模式", 2L);
	
	private String name;
	
	private Long id;
	
	private AutoBuildAgenda(String name, Long agendaId){
		this.name = name;
		this.id = agendaId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Long getId(){
		return this.id;
	}
	
	/**
	 * @Title: 根据名称获取自动议程<br/> 
	 * @param name 名称
	 * @return AutoBuildAgenda 自动建立的议程
	 */
	public static AutoBuildAgenda fromName(String name) throws Exception{
		AutoBuildAgenda[] values = AutoBuildAgenda.values();
		for(AutoBuildAgenda value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static AutoBuildAgenda fromAgendaId(String agendaId) throws Exception{
		AutoBuildAgenda[] values = AutoBuildAgenda.values();
		for(AutoBuildAgenda value:values){
			if(value.getId().toString().equals(agendaId)){
				return value;
			}
		}
		throw new ErrorTypeException("agendaId", agendaId);
	}
	
	public static List<AutoBuildAgenda> fromAgendaIds(String agendaIds) throws Exception{
		ArrayList<AutoBuildAgenda> agenda = new ArrayList<AutoBuildAgenda>();
		if(agendaIds != null){
			String[] ids = agendaIds.split(",");
			AutoBuildAgenda[] values = AutoBuildAgenda.values();
			for(String id : ids){
				for(AutoBuildAgenda value:values){
					if(value.getId().toString().equals(id)){
						agenda.add(value);
						break;
					}
				}
			}
		}
		return agenda;
	}
	
}
