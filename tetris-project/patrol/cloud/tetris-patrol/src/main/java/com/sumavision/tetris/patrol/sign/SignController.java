package com.sumavision.tetris.patrol.sign;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.patrol.address.AddressDAO;
import com.sumavision.tetris.patrol.address.AddressPO;

@Controller
@RequestMapping(value = "/sign")
public class SignController {

	@Autowired
	private SignQuery signQuery;
	
	@Autowired
	private SignService signService;
	
	@Autowired
	private AddressDAO addressDao;
	
	/**
	 * 签到页面<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月15日 下午12:55:56
	 * @param @PathVariable String uuid 地址uuid
	 */
	@RequestMapping(value = "/sign/page/{uuid}")
	public ModelAndView signPage(
			@PathVariable String uuid,
			HttpServletRequest request) throws Exception{
		ModelAndView mv = null;
		mv = new ModelAndView("web/patrol/sign-page");
		mv.addObject("uuid", uuid);
		return mv;
	}
	
	/**
	 * 根据条件分页查询签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:17:28
	 * @param String name 签到用户姓名
	 * @param String addressName 地址名称
	 * @param String beginTime 查询时间区域下限
	 * @param String endTime 查询时间区域上限
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SignVO> rows 签到信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String name,
			String addressName,
			String beginTime,
			String endTime,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		Date parsedBeginTime = null;
		Date parsedEndTime = null;
		if(beginTime!=null && !"".equals(beginTime)) parsedBeginTime = DateUtil.parse(beginTime, DateUtil.dateTimePattern);
		if(endTime!=null && !"".equals(endTime)) parsedEndTime = DateUtil.parse(endTime, DateUtil.dateTimePattern);
		
		return signQuery.load(name, addressName, parsedBeginTime, parsedEndTime, currentPage, pageSize);
	}
	
	/**
	 * 用户签到<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:41:12
	 * @param String addressUuid 地址uuid
	 * @param String name 用户姓名
	 * @param String phone 手机号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String addressUuid, 
			String name, 
			String phone,
			HttpServletRequest request) throws Exception{
		
		signService.add(addressUuid, name, phone);
		return null;
	}
	
	/**
	 * 删除签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:43:02
	 * @param Long id 签到信息id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		signService.delete(id);
		return null;
	}
	
	/**
	 * 导出excel<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午7:48:42
	 * @param String name 用户姓名
	 * @param String addressName 地址名称
	 * @param Date beginTime 查询时间下限
	 * @param Date endTime 查询时间上限
	 */
	@RequestMapping(value = "/export/excel")
	public void exportExcel(
			String name,
			String addressName,
			String beginTime,
			String endTime,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		HSSFWorkbook workbook = null;
		try{
			Date parsedBeginTime = null;
			Date parsedEndTime = null;
			if(beginTime!=null && !"".equals(beginTime)) parsedBeginTime = DateUtil.parse(beginTime, DateUtil.dateTimePattern);
			if(endTime!=null && !"".equals(endTime)) parsedEndTime = DateUtil.parse(endTime, DateUtil.dateTimePattern);
			
			List<SignPO> signs = signQuery.findByConditions(name, addressName, parsedBeginTime, parsedEndTime);
			String systemName = "传媒中心智慧巡查系统";
			
			workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(systemName);
			HSSFRow headRow = sheet.createRow(0);
			HSSFCell addressNameHead = headRow.createCell(0, CellType.STRING);
			addressNameHead.setCellValue("地址");
			HSSFCell signTimeHead = headRow.createCell(1, CellType.STRING);
			signTimeHead.setCellValue("签到时间");
			headRow.createCell(2, CellType.STRING);
			HSSFCell nameHead = headRow.createCell(2, CellType.STRING);
			nameHead.setCellValue("姓名");
			HSSFCell phoneHead = headRow.createCell(3, CellType.STRING);
			phoneHead.setCellValue("手机号");
			
			if(signs!=null && signs.size()>0){
				Set<Long> addressIds = new HashSet<Long>();
				for(SignPO sign:signs){
					addressIds.add(sign.getAddressId());
				}
				List<AddressPO> addresses = addressDao.findAll(addressIds);
				for(int i=0; i<signs.size(); i++){
					SignPO sign = signs.get(i);
					AddressPO targetAddress = null;
					for(AddressPO address:addresses){
						if(sign.getAddressId().equals(address.getId())){
							targetAddress = address;
							break;
						}
					}
					
					HSSFRow dataRow = sheet.createRow(i + 1);
					HSSFCell addressNameCell = dataRow.createCell(0, CellType.STRING);
					addressNameCell.setCellValue(targetAddress==null?"":targetAddress.getName());
					HSSFCell signTimeCell = dataRow.createCell(1, CellType.STRING);
					signTimeCell.setCellValue(DateUtil.format(sign.getSignTime(), DateUtil.dateTimePattern));
					HSSFCell nameCell = dataRow.createCell(2, CellType.STRING);
					nameCell.setCellValue(sign.getName());
					HSSFCell phoneCell = dataRow.createCell(3, CellType.STRING);
					phoneCell.setCellValue(sign.getPhone());
				}
			}
			
			String fileName = new StringBufferWrapper().append("传媒中心智慧巡查系统").append(".xls").toString();
			response.setHeader("Content-Disposition", "attchement;filename=" + fileName);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			workbook.write(response.getOutputStream());
			response.flushBuffer();
		}finally{
			if(workbook != null) workbook.close();
		}
		
	}
	
}
