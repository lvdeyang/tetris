package com.sumavision.tetris.cs.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class ProgramQuery {
	@Autowired
	private ProgramDAO programDao;
	
	@Autowired
	private ScreenQuery screenQuery;

	/**
	 * 根据排期id获取排期分屏信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param scheduleId 排期id
	 * @return ProgramVO 分屏信息
	 */
	public ProgramVO getProgram(Long scheduleId) throws Exception {

		ProgramVO program = new ProgramVO();
		ProgramPO programPO = programDao.findByScheduleId(scheduleId);

		if(programPO == null)
			return null;
		
		program.set(programPO);
		program.setScreenInfo(screenQuery.getScreenInfo(programPO.getId()));
		return program;
	}
	
	/**
	 * 获取模板时获取已设定分屏信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月12日 上午11:39:45
	 * @param Long scheduleId 
	 * @param templates
	 */
	public TemplateVO getScreen2Template(Long scheduleId, JSONArray templates) throws Exception {
		ProgramVO programVO = getProgram(scheduleId);
		if (programVO == null) return null;
		List<TemplateVO> templateVOs = JSON.parseArray(templates.toJSONString(), TemplateVO.class);
		for (TemplateVO template : templateVOs) {
			if (template.getId() == programVO.getScreenId() && template.getScreenNum() == programVO.getScreenNum()) {
				for (TemplateScreenVO templateScreenVO : template.getScreen()) {
					for (ScreenVO screenVO : programVO.getScreenInfo()) {
						if (screenVO.getSerialNum() == templateScreenVO.getNo()) {
							if (templateScreenVO.getData() == null) templateScreenVO.setData(new ArrayList<ScreenVO>());
							templateScreenVO.getData().add(screenVO);
						}
					}
				}
				return template;
			}
		}
		return null;
	}

	/**
	 * 查询当前日期最近一个周<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年4月6日 下午1:16:55
	 * @param date 选择日期
	 * @return DateVO 星期表
	 */
	public List<DateVO> getAllWeek(String day) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd EEE");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date mdate = df.parse(day);
		int b = mdate.getDay();
		if (b==0) {
			b = 7;
		}
        Date fdate;
		List<Date> list = new ArrayList<Date>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a-1, fdate);
        }
        List<DateVO> dateVOs = new ArrayList<DateVO>();
        for (Date date : list) {
            DateVO dateVO = new DateVO();
            dateVO.setTitle(sdf.format(date));
            StringBufferWrapper title = new StringBufferWrapper().append(df.format(date)).append(System.getProperty("line.separator")).append(dateVO.getTitle().substring(10));
            dateVO.setName(df.format(date));;
            dateVO.setTitle(title.toString());
            System.out.println(dateVO.getTitle());
            dateVOs.add(dateVO);
        }
		return dateVOs;
	}
	
//	public static void main(String[] args) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd EEE");//EEE代表星期几
//        Date currentDate = new Date();
//        List<Date> days = getAllWeek(currentDate);
//        System.out.println("今天的日期: " + sdf.format(currentDate));
//        for (Date date : days) {
//            System.out.println(sdf.format(date));
//        }
//	}
	
}
