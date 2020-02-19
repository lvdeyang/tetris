package com.sumavision.tetris.business.director.vo;

import java.util.Comparator;
import java.util.List;

/**
 * 导播任务参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月14日 下午2:43:58
 */
public class DirectorTaskVO {

	/** 任务id */
	private String taskId;
	
	/** 能力ip */
	private String capacityIp;
	
	/** 源索引--标识顺序 */
	private Long index;
	
	/** 输入源 */
	private List<SourceVO> sources;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public List<SourceVO> getSources() {
		return sources;
	}

	public void setSources(List<SourceVO> sources) {
		this.sources = sources;
	}
	
	/**
	 * 按照index从小到大排序<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月16日 下午3:30:51
	 */
	public static final class IndexComparator implements Comparator<DirectorTaskVO>{
		
		@Override
		public int compare(DirectorTaskVO o1, DirectorTaskVO o2) {
			
			long id1 = o1.getIndex();
			long id2 = o2.getIndex();
			
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
