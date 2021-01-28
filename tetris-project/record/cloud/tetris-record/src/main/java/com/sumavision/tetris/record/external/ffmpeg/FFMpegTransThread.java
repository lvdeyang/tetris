package com.sumavision.tetris.record.external.ffmpeg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.file.RecordFileService;
import com.sumavision.tetris.record.file.RecordFilePO.EFFMpegTransStatus;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.storage.StoragePO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;

public class FFMpegTransThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FFMpegTransThread.class);

	private RecordFileDAO recordFileDAO;

	private RecordFilePO recordFilePO;

	private StoragePO storagePO;

	private RecordStrategyPO recordStrategyPO;

	private RecordFileService recordFileService;

	private String ffMpegPath = "/opt/sumavision/ffmpeg-3.4.1-64bit-static/ffmpeg";

	public FFMpegTransThread(RecordFilePO recordFilePO, StoragePO storagePO, RecordStrategyPO recordStrategyPO,
			RecordFileService recordFileService, RecordFileDAO recordFileDAO) {
		this.recordFilePO = recordFilePO;
		this.storagePO = storagePO;
		this.recordStrategyPO = recordStrategyPO;
		this.recordFileService = recordFileService;
		this.recordFileDAO = recordFileDAO;
	}

	@Override
	public void run() {
		// TODO
		LOGGER.info("FFMpegTransThread, start");

		if (recordFilePO.getStatus().equals(ERecordFileStatus.RECORD_SUC)) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

			Process ffTransProcess = null;

			// TODO 待补充，如果在本地挂载的话怎么处理
			try {
				Map<String, String> recordFileUrlMap = recordFileService.getFileUrl(recordFilePO, storagePO);

				String httpUrl = recordFileUrlMap.get("httpVodUrl");

				String dstFilePath = storagePO.getLocalFFMpegOutputPath() + recordStrategyPO.getName() + "-"
						+ sdf.format(recordFilePO.getStartTime()) + "-" + sdf.format(recordFilePO.getStopTime())
						+ ".mov";

				List<String> params = new ArrayList<String>();
				params.add(ffMpegPath);
				// params.add("-y -threads 2");
				params.add("-i");
				params.add(httpUrl);
				params.add("-map");
				params.add("0");
				params.add("-c");
				params.add("copy");
				params.add(dstFilePath);
				params.add("-y");

				LOGGER.info("FFMpegTransThread, ffmpeg cmd exectute start, cmd==" + JSONObject.toJSONString(params));

				ffTransProcess = new ProcessBuilder(params).redirectErrorStream(true).start();

				new PrintStream(ffTransProcess.getErrorStream()).run();

				new PrintStream(ffTransProcess.getInputStream()).run();

				int c = ffTransProcess.waitFor();

				LOGGER.info("FFMpegTransThread, ffmpeg cmd exectute finish, cmd==" + JSONObject.toJSONString(params)
						+ ", result==" + c);

				if (c == 0) {
					// 成功
					recordFilePO.setFfMpegTransStatus(EFFMpegTransStatus.TRANS_SUCCESS);
				} else {
					recordFilePO.setFfMpegTransStatus(EFFMpegTransStatus.TRANS_ERROR);
				}

				recordFileDAO.save(recordFilePO);

				ffTransProcess.destroy();

			} catch (Exception e) {
				LOGGER.error(e.getMessage());

			} finally {
				if (ffTransProcess != null) {
					ffTransProcess.destroy();
				}
			}
		} else {
			LOGGER.error("record task not finish, cannot transcode");
		}
	}
}
