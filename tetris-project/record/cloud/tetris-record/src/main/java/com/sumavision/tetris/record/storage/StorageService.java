package com.sumavision.tetris.record.storage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class StorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

	@Autowired
	private StorageDAO storageDAO;

	// 检测设备心跳动作的频率
	@Value("${localRecordBasePath}")
	private String localRecordBasePath;

	@Value("${ftpBasePath}")
	private String ftpBasePath;

	@Value("${httpBasePath}")
	private String httpBasePath;

	@Value("${storageSize}")
	private Integer size;

	@Value("${cleanSpaceThreshold}")
	private Integer cleanSpaceThreshold;

	@Value("${cleanTimeThreshold}")
	private Integer cleanTimeThreshold;

	public void init() {

		List<StoragePO> storagePOs = storageDAO.findAll();

		if (!CollectionUtils.isEmpty(storagePOs)) {
			LOGGER.error("InitStorageService init(), StoragePO already exists");
			return;
		}

		StoragePO storagePO = new StoragePO();
		storagePO.setName("默认仓库");
		storagePO.setLocalRecordPath(localRecordBasePath);
		storagePO.setFtpBasePath(ftpBasePath);
		storagePO.setHttpBasePath(httpBasePath);

		if (size == null || size == 0) {
			size = 80;
		}

		storagePO.setSize(size);

		if (cleanSpaceThreshold == null || cleanSpaceThreshold == 0) {
			cleanSpaceThreshold = 60;
		}

		storagePO.setCleanSpaceThreshold(cleanSpaceThreshold);

		if (cleanTimeThreshold == null || cleanTimeThreshold == 0) {
			cleanTimeThreshold = 30;
		}

		storagePO.setCleanTimeThreshold(cleanTimeThreshold);

		storageDAO.save(storagePO);

	}

}
