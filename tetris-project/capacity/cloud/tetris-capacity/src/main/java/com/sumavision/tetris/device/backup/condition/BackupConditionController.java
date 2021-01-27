package com.sumavision.tetris.device.backup.condition;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Poemafar on 2019/5/13 12:00
 */

@RestController
@RequestMapping("/backupCondition")
public class BackupConditionController{

	private static Logger LOGGER = LoggerFactory.getLogger(BackupConditionController.class);

	@Autowired
	private BackupConditionDao backupConditionDao;

	@Autowired
	private BackupConditionService backupConditionService;



	@JsonBody
	@RequestMapping("/update")
	public Object updateBackupCondition(@RequestParam Boolean cpuOverride,
													 @RequestParam Boolean gpuOverride,
													 @RequestParam BackupConditionPO.NetCardErrorType inputNetCardError,
													 @RequestParam Boolean ctrlPortDisconnect,
													 @RequestParam Boolean outputNetCardError)throws Exception{
		return backupConditionService.modifyBackupCondition(cpuOverride,gpuOverride,inputNetCardError,outputNetCardError,ctrlPortDisconnect);
	}

	@JsonBody
	@RequestMapping("/get")
	public Object getBackupCondition() throws Exception{
		BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
		if (backupConditionPO == null) {
			throw new BaseException(StatusCode.ERROR,"自动备份条件获取失败");
		}
		return backupConditionPO;
	}

}
