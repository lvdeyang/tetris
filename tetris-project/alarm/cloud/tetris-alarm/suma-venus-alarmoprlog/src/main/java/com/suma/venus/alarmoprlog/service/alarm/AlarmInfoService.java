package com.suma.venus.alarmoprlog.service.alarm;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EAlarmLevel;

@Service
public class AlarmInfoService {

	@Autowired
	private IAlarmInfoDAO alarmInfoDAO;

	public Map<String, Object> importAlarmInfoExcel(InputStream is) {

		Map<String, Object> data = new HashMap<String, Object>();
		int importNum = 0;
		int errorNum = 0;
		int sameNum = 0;

		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(is);
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			data.put("errMsg", "Open excel file error");
		}

		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		// Row titleRow = sheet.getRow(0);

		for (int i = 1; i <= lastRowNum; i++) {

			AlarmInfoPO alarmInfoPO;
			try {
				Row row = sheet.getRow(i);

				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				if (StringUtils.isEmpty(row.getCell(0).getStringCellValue())
						|| StringUtils.isEmpty(row.getCell(1).getStringCellValue())
						|| transferAlarmLevel(row.getCell(3).getStringCellValue()) == null) {
					errorNum++;
					continue;
				}

				if (alarmInfoDAO.findByAlarmCode(row.getCell(0).getStringCellValue()) != null) {
					// TODO
					alarmInfoPO = alarmInfoDAO.findByAlarmCode(row.getCell(0).getStringCellValue());
				} else {
					alarmInfoPO = new AlarmInfoPO();

					// 0列 告警编码
					alarmInfoPO.setAlarmCode(row.getCell(0).getStringCellValue());
				}

				// 1列 告警名称
				alarmInfoPO.setAlarmName(row.getCell(1).getStringCellValue());

				// 2列 告警描述
				if (row.getCell(2) != null && row.getCell(2).getStringCellValue() != null) {
					alarmInfoPO.setAlarmBrief(row.getCell(2).getStringCellValue());
				}

				// 3列 告警级别
				alarmInfoPO.setAlarmLevel(transferAlarmLevel(row.getCell(3).getStringCellValue()));

				// 4列 告警解决方案
				// 2列 告警描述
				if (row.getCell(4) != null && row.getCell(4).getStringCellValue() != null) {
					alarmInfoPO.setAlarmSolution(row.getCell(4).getStringCellValue());
				}

			} catch (Exception e) {
				// TODO: handle exception
				errorNum++;
				continue;
			}

			alarmInfoDAO.save(alarmInfoPO);
			importNum++;
		}

		data.put("errMsg", "");
		data.put("importNum", importNum);
		data.put("sameNum", sameNum);
		data.put("errorNum", errorNum);
		return data;
	}

	private EAlarmLevel transferAlarmLevel(String str) {

		switch (str) {
		case "INFO":
			return EAlarmLevel.INFO;
		case "MINOR":                                                                                                                                                                                                                                                                                                                                                                                                     
			return EAlarmLevel.MINOR;
		case "MAJOR":
			return EAlarmLevel.MAJOR;
		case "CRITICAL":
			return EAlarmLevel.CRITICAL;
		case "提示":
			return EAlarmLevel.INFO;
		case "一般":
			return EAlarmLevel.MINOR;
		case "重要":
			return EAlarmLevel.MAJOR;
		case "严重":
			return EAlarmLevel.CRITICAL;
		default:
			return null;
		}

	}

}
