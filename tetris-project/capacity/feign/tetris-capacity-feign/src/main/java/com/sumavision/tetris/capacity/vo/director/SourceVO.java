package com.sumavision.tetris.capacity.vo.director;

import java.util.Comparator;

/**
 * 源参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月14日 下午3:06:52
 */
public class SourceVO {
	
	/** 源bundleId */
	private String bundleId;
	
	/** 类型，区分srt,udp等源 */
	private String type;

	/** 源IP */
	private String ip;
	
	/** 源端口 */
	private String port;
	
	/** 源索引--标识顺序 */
	private Long index;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}
	
	/**
	 * 按照index从小到大排序<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月16日 下午3:30:51
	 */
	public static final class IndexComparator implements Comparator<SourceVO>{
		
		@Override
		public int compare(SourceVO o1, SourceVO o2) {
			
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
