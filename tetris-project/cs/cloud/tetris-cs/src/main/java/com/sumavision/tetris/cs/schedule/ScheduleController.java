package com.sumavision.tetris.cs.schedule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.excel.EasyExcel;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoService;
import com.sumavision.tetris.cs.program.ProgramDAO;
import com.sumavision.tetris.cs.program.ProgramPO;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ScreenDAO;
import com.sumavision.tetris.cs.program.ScreenPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cs/schedule")
public class ScheduleController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private BroadTerminalBroadInfoService broadTerminalBroadInfoService;
	
	@Autowired
	private ScheduleDAO scheduleDao;
	
	@Autowired
	private ProgramDAO programDao;
	
	@Autowired
	private ScreenDAO screenDao;
	
	/**
	 * 添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(Long channelId, String broadDate, String endDate, String remark, boolean mono, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if(mono){
			broadDate = "default";
			SchedulePO schedule = scheduleDao.findByBroadDate(broadDate);
			if(schedule != null){
				throw new BaseException(StatusCode.FORBIDDEN, "已存在默认排期");
			}
		}
		
		return scheduleService.add(channelId, broadDate, endDate, remark);
	}
	
	/**
	 * 删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleService.remove(id);
	}
	
	/**
	 * 编辑排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(Long id, String broadDate, String endDate, String remark, boolean mono, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if(mono){
			broadDate = "default";
			endDate = "";
			SchedulePO schedule = scheduleDao.findByBroadDateAndIdNot(broadDate, id);
			if(schedule != null){
				throw new BaseException(StatusCode.FORBIDDEN, "已存在默认排期");
			}
		}
		
		return scheduleService.edit(id, broadDate, endDate, remark);
	}
	
	/**
	 * 获取排期列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param int currentPage 分页当前页
	 * @param int pageSize 分页大小
	 * @return total 列表总数
	 * @return List<ScheduleVO> 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object get(Long channelId, int currentPage, int pageSize, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleQuery.getByChannelId(channelId, currentPage, pageSize);
	}
	
	/**
	 * 设置排期单总停止时间(目前仅终端播发使用，用于周期播放)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 上午10:25:28
	 * @param Long channelId 频道id
	 * @param String endDate 停止时间
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/total/endTime")
	public Object setTotalEndTime(Long channelId, String endDate, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		return broadTerminalBroadInfoService.setEndDate(channelId, endDate);
	}
	
	
	/**
	 * 上传节目单功能<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月16日 下午4:48:19
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/import")
	public Object handleImport(HttpServletRequest request, HttpServletResponse response) throws Exception{
		MultipartHttpServletRequestWrapper multipartRequest = new MultipartHttpServletRequestWrapper(request);
		//获取channelId
		String channelId = multipartRequest.getString("channelId");
		//读取excel
		List<ScheduleExcelModel> list = EasyExcel.read(multipartRequest.getInputStream("file"),ScheduleExcelModel.class,null).sheet(0).doReadSync();
		
		scheduleService.importSchedule(list,Long.valueOf(channelId));
		return null;
	}
	
	@RequestMapping(value = "/export/{channelId}")
	public void handleExport(@PathVariable Long channelId,HttpServletResponse response) throws IOException{
		
		XSSFWorkbook workbook = new XSSFWorkbook();

        String []columnNames = {"排期日期","节目名称","节目开始时间","节目结束时间"};
        Sheet sheet = workbook.createSheet();
        Row titleRow = sheet.createRow(0);
        
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(columnNames[i]);
        }
        
        List<ScheduleExcelModel> excelList = new ArrayList<ScheduleExcelModel>();
        
        //根据频道查排期.按播发日期排序
        List<SchedulePO> schedules = scheduleDao.findByChannelId(channelId);
        //遍历排期，查询每个排期下的program，根据programId查询节目
 
        for (SchedulePO schedule : schedules) {
        	//添加排期的日期
        	ScheduleExcelModel scheduleExcelModel = new ScheduleExcelModel();
        	scheduleExcelModel.setScheduleDate(schedule.getBroadDate());
        	excelList.add(scheduleExcelModel);
        	//获取programId
    		ProgramPO programPO = programDao.findByScheduleId(schedule.getId());
    		Long programId =null;
    		if(programPO!=null){
    			programId = programPO.getId();
    		}else{
    			programPO = new ProgramPO();
    			programPO.setUpdateTime(new Date());
    			programPO.setScheduleId(schedule.getId());
    			programPO.setScreenNum(1l);
				programPO.setScreenId(1l);
				programPO.setOrient("horizontal");
    			programDao.save(programPO);
    			programId = programPO.getId();
    		}
    		
    		//查询节目信息
    		List<ScreenPO> screenPOList = screenDao.findByProgramId(programId);
    		for (ScreenPO screenPO : screenPOList) {
    			//解析节目时间
    			String fullStartTime = DateUtil.format(screenPO.getStartTime(),"yyyy-MM-dd HH:mm:ss");
    			String fullStartTimes[] = fullStartTime.split(" ");
    			String fullEndTime = DateUtil.format(screenPO.getEndTime(),"yyyy-MM-dd HH:mm:ss");
    			String fullEndTimes[] = fullEndTime.split(" ");
    			//添加节目
    			ScheduleExcelModel scheduleExcelModelp = new ScheduleExcelModel();
    			scheduleExcelModelp.setProgramName(screenPO.getName());
    			if(fullStartTimes.length>1){
    				scheduleExcelModelp.setpStartTime(fullStartTimes[1]);
    			}
    			if(fullEndTimes.length>1){
    				scheduleExcelModelp.setpEndTime(fullEndTimes[1]);
    			}
    			excelList.add(scheduleExcelModelp);
			}
		}

        //创建数据行并写入值
        for (int j = 0; j < excelList.size(); j++) {
            int lastRowNum = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(excelList.get(j).getScheduleDate());
            dataRow.createCell(1).setCellValue(excelList.get(j).getProgramName());
            dataRow.createCell(2).setCellValue(excelList.get(j).getpStartTime());
            dataRow.createCell(3).setCellValue(excelList.get(j).getpEndTime());
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode("schedule.xls", "utf-8"));
        response.setHeader("Access-Control-Expose-Headers", "content-Disposition");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
	}
	
	/**
	 * 查询选择日期的排期，如没有，则创建一个排期<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年4月6日 上午11:29:12
	 * @param channelId
	 * @param date
	 * @return ScheduleVO
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/or/add")
	public Object getOrAdd(Long channelId,String date) throws Exception{
		
		return scheduleService.getOrAdd(channelId, date);
	}
}
