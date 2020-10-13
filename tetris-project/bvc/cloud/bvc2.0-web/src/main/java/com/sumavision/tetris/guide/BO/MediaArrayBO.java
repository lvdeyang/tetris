package com.sumavision.tetris.guide.BO;

public class MediaArrayBO {

	/**与task_array中task_id对应，表示该路输出应该输出哪个任务 */
	private String task_id;
	
	/**与task_array中encode_id对应，表示该路输出应该输出哪个视频编码 */
	private String encode_id;
	
	/**表示该路输出的编码类型 */
	private String type;

	public String getTask_id() {
		return task_id;
	}

	public MediaArrayBO setTask_id(String task_id) {
		this.task_id = task_id;
		return this;
	}

	public String getEncode_id() {
		return encode_id;
	}

	public MediaArrayBO setEncode_id(String encode_id) {
		this.encode_id = encode_id;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaArrayBO setType(String type) {
		this.type = type;
		return this;
	}
}
