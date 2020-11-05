package com.sumavision.tetris.guide.BO;

import java.util.List;

public class GuideTaskArrayBO {
	
	/**编码任务ID */
	private String id ;
	
	/**编码类型 */
	private String type;
	
	/**编码数组，数组表示编码个数，即多码率个数  */
	private Object encode_array;

	public String getId() {
		return id;
	}

	public GuideTaskArrayBO setId(String id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public GuideTaskArrayBO setType(String type) {
		this.type = type;
		return this;
	}

	public Object getEncode_array() {
		return encode_array;
	}

	public GuideTaskArrayBO setEncode_array(Object encode_array) {
		this.encode_array = encode_array;
		return this;
	}
	
}
