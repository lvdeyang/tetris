package com.sumavision.tetris.oldCMS.api.server;

import com.sumavision.tetris.streamTranscodingProcessVO.FileToStreamVO;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingProcessVO;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingVO;

public class StreamProcessVO {
	/** 文件转流参数 */
	private FileToStreamVO fileToStreamVO;
	/** 流转码参数 */
	private StreamVO streamVO;
	/** 收录参数 */
	private RecordVO recordVO;
	
	public FileToStreamVO getFileToStreamVO() {
		return fileToStreamVO;
	}
	public StreamProcessVO setFileToStreamVO(FileToStreamVO fileToStreamVO) {
		this.fileToStreamVO = fileToStreamVO;
		return this;
	}
	public StreamVO getStreamVO() {
		return streamVO;
	}
	public StreamProcessVO setStreamVO(StreamVO streamVO) {
		this.streamVO = streamVO;
		return this;
	}
	public RecordVO getRecordVO() {
		return recordVO;
	}
	public StreamProcessVO setRecordVO(RecordVO recordVO) {
		this.recordVO = recordVO;
		return this;
	}
}
