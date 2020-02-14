package com.suma.venus.alarmoprlog.service.alarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitAlarmInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitAlarmInfoService.class);

	public static final String separator = File.separator;

	@Autowired
	private AlarmInfoService alarmInfoService;

	public void initAlarmInfo() {
		// 初始化，导入告警基本信息excel
		String dir = "alarmFile" + separator;

		File[] files = new File(dir).listFiles();

		if (files == null || files.length == 0) {
			return;
		}

		for (File file : files) {
			if (file.isFile() && file.exists() && file.getName().startsWith("alarm")) {

				try {
					InputStream is = new FileInputStream(file);

					alarmInfoService.importAlarmInfoExcel(is);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
}
