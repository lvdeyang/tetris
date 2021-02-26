package com.sumavision.bvc.command.bandwidth;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.service.BundleService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping("/command/station/bandwidth")
public class CommandStationBandwidthController {

	@Autowired
	private CommandStationBandwidthDAO commandStationBandwidthDao;
	
	@Autowired
	private CommandStationBandwidthService commandStationBandwidthService;
	
	@Autowired
	private BundleService bundleService;
	
	
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
		
		commandStationBandwidthService.alreadyExist(stationName, identity);
		
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
	public Object delete(Long id)throws Exception{
		
		CommandStationBandwidthPO station = commandStationBandwidthDao.findOne(id);
		
		Boolean bundle = bundleService.checkedRegion(station.getStationName());
		if(bundle){
			throw new BaseException(StatusCode.FORBIDDEN, "当前站点下存在设备，无法删除");
		}
		
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
	 * @throws BaseException 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/edit")
	public Object edit(
			Long id,
			String stationName,
			Integer totalWidth,
			Integer singleWidth,
			String identity) throws Exception{
		
		commandStationBandwidthService.alreadyExist(stationName, identity);

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
		
//		commandStationBandwidthService.syncSerNodeToStation();
		return new HashMapWrapper<String, Object>().put("rows", commandStationBandwidthDao.findAll()).getMap();
		
	}
	
}
