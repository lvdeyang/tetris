package com.sumavision.tetris.guide.BO;

import java.util.List;

public class GuideTaskArrayBO {
	
	/**编码任务ID */
	private Long id ;
	
	/**编码类型 */
	private String type;
	
	/**编码数组，数组表示编码个数，即多码率个数  */
	private List<GuideEncodeArrayBO> encode_array;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<GuideEncodeArrayBO> getEncode_array() {
		return encode_array;
	}

	public void setEncode_array(List<GuideEncodeArrayBO> encode_array) {
		this.encode_array = encode_array;
	}

	
}
