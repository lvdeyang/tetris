package com.sumavision.tetris.record.storage;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.file.RecordFileService;

@Service
public class StorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

	@Autowired
	private StorageDAO storageDAO;

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private RecordFileService recordFileService;

	@Value("${localRecordBasePath}")
	private String localRecordBasePath;

	@Value("${ftpBasePath}")
	private String ftpBasePath;

	@Value("${httpBasePath}")
	private String httpBasePath;

	@Value("${clean_timeThreshold}")
	private Integer clean_timeThreshold;

	@Value("${clean_diskUsedPercentThreshold}")
	private Integer clean_diskUsedPercentThreshold;

	@Value("${clean_recordMaxSpaceThreshold}")
	private Integer clean_recordMaxSpaceThreshold;

	@Value("${localFFMpegOutputPath}")
	private String localFFMpegOutputPath;

	public void init() {

		List<StoragePO> storagePOs = storageDAO.findAll();

		if (!CollectionUtils.isEmpty(storagePOs)) {
			LOGGER.error("InitStorageService init(), StoragePO already exists");
			return;
		}

		StoragePO storagePO = new StoragePO();
		storagePO.setName("默认仓库");
		storagePO.setLocalRecordPath(localRecordBasePath);
		storagePO.setLocalFFMpegOutputPath(localFFMpegOutputPath);
		storagePO.setFtpBasePath(ftpBasePath);
		storagePO.setHttpBasePath(httpBasePath);

		if (clean_timeThreshold == null || clean_timeThreshold == 0) {
			clean_timeThreshold = 80;
		}

		storagePO.setIsCheckTimeThreshold(true);
		storagePO.setClean_timeThreshold(clean_timeThreshold);

		if (clean_diskUsedPercentThreshold == null || clean_diskUsedPercentThreshold == 0) {
			clean_diskUsedPercentThreshold = 85;
		}

		storagePO.setIsCheckDiskUsedSpacePctThreshold(true);
		storagePO.setClean_diskUsedSpaceThreshold(clean_diskUsedPercentThreshold);

		if (clean_recordMaxSpaceThreshold == null || clean_recordMaxSpaceThreshold == 0) {
			clean_recordMaxSpaceThreshold = 60;
		}

		storagePO.setIsCheckRecordMaxSpaceThreshold(true);
		storagePO.setClean_recordMaxSpaceThreshold(clean_recordMaxSpaceThreshold);

		storagePO.setIsMounted(true);

		storageDAO.save(storagePO);

	}

	public Integer updateRecordSpace(StoragePO storagePO) {

		String localRecordPath = storagePO.getLocalRecordPath();

		File recordDir = new File(localRecordPath);

		long usedSpaceInBytes = FileUtils.sizeOfDirectory(recordDir);

		Integer usedSpaceInMB = (int) (usedSpaceInBytes / 1024 / 1024);

		return usedSpaceInMB;
	}

	public Integer updateDiskUsedPct(StoragePO storagePO) {

		String localRecordPath = storagePO.getLocalRecordPath();
		File recordDir = new File(localRecordPath);
		float diskFreePctFloat = (float) recordDir.getUsableSpace() / recordDir.getTotalSpace();
		Integer diskUsedPct = 100 - Math.round(diskFreePctFloat * 100);
		return diskUsedPct;

	}

	public void cleanStorageForDiskUsedPct(StoragePO storagePO) {

		LOGGER.info("cleanStorageForDiskUsedPct start");

		// TODO
		List<RecordFilePO> recordFilePOs = recordFileDAO.findByStorageIdOrderByStopTimeDesc(storagePO.getId());

		if (!CollectionUtils.isEmpty(recordFilePOs)) {
			int size = recordFilePOs.size();
			int i = 0;
			while (i < size) {
				LOGGER.info("cleanStorageForDiskUsedPct, del recordFile()", i);
				recordFileService.delRecordFile(recordFilePOs.get(i));
				if (updateDiskUsedPct(storagePO) < storagePO.getClean_diskUsedSpaceThreshold()) {
					break;
				}
			}
		}

	}

	public void cleanStorageForReordMaxSpoace(StoragePO storagePO) {

		LOGGER.info("cleanStorageForReordMaxSpoace start");

		// TODO
		List<RecordFilePO> recordFilePOs = recordFileDAO.findByStorageIdOrderByStopTimeDesc(storagePO.getId());

		if (!CollectionUtils.isEmpty(recordFilePOs)) {
			int size = recordFilePOs.size();
			int i = 0;
			while (i < size) {
				LOGGER.info("cleanStorageForReordMaxSpoace, del recordFile()", i);

				recordFileService.delRecordFile(recordFilePOs.get(i));

				if (updateRecordSpace(storagePO) < storagePO.getClean_recordMaxSpaceThreshold()) {
					break;
				}
			}
		}
	}

}
