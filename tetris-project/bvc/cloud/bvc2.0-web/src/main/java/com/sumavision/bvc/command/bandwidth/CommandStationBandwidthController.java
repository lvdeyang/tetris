package com.sumavision.bvc.command.bandwidth;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping("/command/station/bandwidth")
public class CommandStationBandwidthController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandStationBandwidthDAO commandStationBandwidthDao;
	
	@Autowired
	private CommandStationBandwidthService commandStationBandwidthService;
	/**
	 * 添加站点<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月29日 下午2:56:08
	 * @param stationName 站点名称
	 * @param totalWidth 总带宽
	 * @param singleWidth 单路带宽
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/add")
	public Object add(
			String stationName,
			Integer totalWidth,
			Integer singleWidth,
			String identity) throws Exception{
		
		CommandStationBandwidthPO station=new CommandStationBandwidthPO();
		
		station.setStationName(stationName);
		station.setSingleWidth(singleWidth);
		station.setTotalWidth(totalWidth);
		station.setIdentity(identity);
		
		commandStationBandwidthDao.save(station);
		
		return null;
	}
	
	/**
	 * 删除站点<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月29日 下午2:59:53
	 * @param id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object delete(Long id){
		
		commandStationBandwidthDao.delete(id);
		
		return null;
	}
	
	/**
	 * 修改站点<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月29日 下午3:07:58
	 * @param id 站点主键
	 * @param stationName 站点名称
	 * @param totalWidth 总带宽
	 * @param singleWidth 单个带宽
	 * @return
	 * @throws ParseException
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/edit")
	public Object edit(
			Long id,
			String stationName,
			Integer totalWidth,
			Integer singleWidth,
			String identity) throws ParseException{

		CommandStationBandwidthPO station=commandStationBandwidthDao.findOne(id);
		
		station.setStationName(stationName);
		station.setSingleWidth(singleWidth);
		station.setTotalWidth(totalWidth);
		station.setIdentity(identity);
		
		commandStationBandwidthDao.save(station);
		
		return null;
	}
	
	/**
	 * 查询站点<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月29日 下午3:10:58
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/query")
	public Object query(){
		
		commandStationBandwidthService.syncSerNodeToStation();
		return new HashMapWrapper<String, Object>().put("rows", commandStationBandwidthDao.findAll()).getMap();
		
	}
	
}
