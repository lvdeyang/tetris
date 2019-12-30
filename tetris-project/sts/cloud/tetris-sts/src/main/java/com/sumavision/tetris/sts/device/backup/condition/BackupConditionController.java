package com.sumavision.tetris.sts.device.backup.condition;

import com.sumavision.tetris.sts.common.CommonController;
import com.sumavision.tetris.sts.device.backup.DeviceBackupService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Poemafar on 2019/5/13 12:00
 */
@Controller
@RequestMapping("/backupCondition")
public class BackupConditionController extends CommonController {

	@Autowired
	BackupConditionDao backupConditionDao;

	@Autowired
	private DeviceBackupService deviceBackupService;

	static Logger logger = LogManager.getLogger(BackupConditionController.class);


	@RequestMapping("/updateBackupCondition")
	@ResponseBody
	public Map<String, Object> updateBackupCondition(@RequestParam Boolean cpuOverride,
													 @RequestParam Boolean gpuOverride,
													 @RequestParam BackupConditionPO.NetCardErrorType inputNetCardError,
													 @RequestParam Boolean ctrlPortDisconnect,
													 @RequestParam Boolean outputNetCardError){
		Map<String, Object> data = makeAjaxData();
		try {
			deviceBackupService.modifyBackupCondition(cpuOverride,gpuOverride,inputNetCardError,outputNetCardError,ctrlPortDisconnect);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateBackupCondition err",e);
			data.put(ERRMSG, "自动备份条件修改失败");
		}
		return data;
	}



	@RequestMapping("/getBackupCondition")
	@ResponseBody
	public Map<String, Object> getBackupCondition(){
		Map<String, Object> data = makeAjaxData();
		try {
			BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
			data.put("backupCondition",backupConditionPO);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getBackupCondition err",e);
			data.put(ERRMSG, "获取自动备份条件失败");
		}
		return data;
	}

}
