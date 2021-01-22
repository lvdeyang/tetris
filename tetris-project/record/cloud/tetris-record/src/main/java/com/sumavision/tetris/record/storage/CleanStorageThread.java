package com.sumavision.tetris.record.storage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.file.RecordFileService;

/**
 * @author chenmo
 */
@Component
public class CleanStorageThread implements Runnable {

	@Autowired
	private StorageDAO storageDAO;

	@Autowired
	private StorageService storageService;

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private RecordFileService recordFileService;

	@Override
	public void run() {

		List<StoragePO> storagePOList = storageDAO.findAll();

		if (CollectionUtils.isEmpty(storagePOList)) {
			return;
		}

		// 考虑可能有的多存储目录，按最复杂情况处理（其实也不复杂）
		for (StoragePO storagePO : storagePOList) {

			// 1.首先对超时保存的进行清理
			if (storagePO.getIsCheckTimeThreshold() != null && storagePO.getIsCheckTimeThreshold()) {
				Integer clean_timeThreshold = storagePO.getClean_timeThreshold();

				Date cleanTimeBefore = new Date(
						Calendar.getInstance().getTimeInMillis() - ((long) clean_timeThreshold * 24 * 60 * 60 * 1000));

				List<RecordFilePO> cleanRecordFileList = recordFileDAO
						.findByRecordStrategyIdAndStopTimeBefore(storagePO.getId(), cleanTimeBefore);

				if (!CollectionUtils.isEmpty(cleanRecordFileList)) {
					recordFileService.delRecordFile(cleanRecordFileList);
				}

			}

			// 2.检查磁盘剩余空间比率，进行清理
			if (storagePO.getIsCheckDiskUsedSpacePctThreshold() != null
					&& storagePO.getIsCheckDiskUsedSpacePctThreshold()
					&& storagePO.getClean_diskUsedSpaceThreshold() < storageService.updateDiskUsedPct(storagePO)) {
				// 清理
				storageService.cleanStorageForDiskUsedPct(storagePO);
			}

			// 3. 再开始处理空间占用百分比占用太多的情况
			if (storagePO.getIsCheckRecordMaxSpaceThreshold() != null && storagePO.getIsCheckRecordMaxSpaceThreshold()
					&& storagePO.getClean_recordMaxSpaceThreshold() < storageService.updateRecordSpace(storagePO)) {
				// 清理
				storageService.cleanStorageForReordMaxSpoace(storagePO);
			}
		}

	}

}
