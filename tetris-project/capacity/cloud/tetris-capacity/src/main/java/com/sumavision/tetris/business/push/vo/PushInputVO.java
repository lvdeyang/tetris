package com.sumavision.tetris.business.push.vo;

import java.util.Comparator;
import java.util.List;

public class PushInputVO {

	/** 源类型 file、stream*/
	private String type;
	
	/** 索引顺序值 */
	private Long index;
	
	private List<PushFileVO> files;
	
	private PushStreamVO stream;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public List<PushFileVO> getFiles() {
		return files;
	}

	public void setFiles(List<PushFileVO> files) {
		this.files = files;
	}

	public PushStreamVO getStream() {
		return stream;
	}

	public void setStream(PushStreamVO stream) {
		this.stream = stream;
	}

	public static final class indexComparator implements Comparator<PushInputVO>{

		@Override
		public int compare(PushInputVO o1, PushInputVO o2) {

			long id1 = o1.getIndex().longValue();
			long id2 = o2.getIndex().longValue();
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
			
		}
		
	}

}
