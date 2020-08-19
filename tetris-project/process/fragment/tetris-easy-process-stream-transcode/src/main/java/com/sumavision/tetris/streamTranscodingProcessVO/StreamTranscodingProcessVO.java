package com.sumavision.tetris.streamTranscodingProcessVO;

public class StreamTranscodingProcessVO {
	/** 文件转流参数 */
	private FileToStreamVO fileToStreamVO;
	/** 流转码参数 */
	private StreamTranscodingVO streamTranscodingVO;
	/** 收录参数 */
	private RecordVO recordVO;
	
	public FileToStreamVO getFileToStreamVO() {
		return fileToStreamVO;
	}
	
	public StreamTranscodingProcessVO setFileToStreamVO(FileToStreamVO fileToStreamVO) {
		this.fileToStreamVO = fileToStreamVO;
		return this;
	}
	
	public StreamTranscodingVO getStreamTranscodingVO() {
		return streamTranscodingVO;
	}
	
	public StreamTranscodingProcessVO setStreamTranscodingVO(StreamTranscodingVO streamTranscodingVO) {
		this.streamTranscodingVO = streamTranscodingVO;
		return this;
	}
	
	public RecordVO getRecordVO() {
		return recordVO;
	}
	
	public StreamTranscodingProcessVO setRecordVO(RecordVO recordVO) {
		this.recordVO = recordVO;
		return this;
	}
}
