package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sumavision.bvc.common.group.po.CommonCombineVideoPO;
import com.sumavision.bvc.common.group.po.CommonCombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPositionPO;
/**
 * @ClassName: 协议层合屏 
 * @author lvdeyang
 * @date 2018年8月7日 上午11:14:25 
 */
public class CombineVideoBO {
	
	private String taskId = "";
	
	private String lock_type = "write";
	
	private CodecParamBO codec_param = new CodecParamBO();
	
	private String uuid = "";
	
	private List<PositionSrcBO> position = new ArrayList<PositionSrcBO>();

	public String getTaskId() {
		return taskId;
	}

	public CombineVideoBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getLock_type() {
		return lock_type;
	}

	public CombineVideoBO setLock_type(String lock_type) {
		this.lock_type = lock_type;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public CombineVideoBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public CombineVideoBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public List<PositionSrcBO> getPosition() {
		return position;
	}

	public CombineVideoBO setPosition(List<PositionSrcBO> position) {
		this.position = position;
		return this;
	}

	/**
	 * @Title: 生成协议层合屏数据<br/> 
	 * @param video 业务合屏数据
	 * @return CombineVideoBO 协议合屏数据
	 */
	public CombineVideoBO set(BusinessCombineVideoPO video, CodecParamBO codec){
		this.setCodec_param(codec)
			.setUuid(video.getUuid())
			.setPosition(new ArrayList<PositionSrcBO>());
		
//		String[] resolution = codec.getVideo_param().getResolution().split("x");
//		int width = Integer.parseInt(resolution[0]);
//		int height = Integer.parseInt(resolution[1]);
		
		List<BusinessCombineVideoPositionPO> positions = video.getPositions();
		for(BusinessCombineVideoPositionPO position:positions){
			PositionSrcBO protocalPosition = new PositionSrcBO().set(position, 0, 0);
			this.getPosition().add(protocalPosition);
		}
		return this;
	}
	public CombineVideoBO set(CombineVideoPO video, CodecParamBO codec){
		this.setCodec_param(codec)
			.setUuid(video.getUuid())
			.setPosition(new ArrayList<PositionSrcBO>());
		
//		String[] resolution = codec.getVideo_param().getResolution().split("x");
//		int width = Integer.parseInt(resolution[0]);
//		int height = Integer.parseInt(resolution[1]);
		
		Set<CombineVideoPositionPO> positions = video.getPositions();
		for(CombineVideoPositionPO position:positions){
			PositionSrcBO protocalPosition = new PositionSrcBO().set(position, 0, 0);
			this.getPosition().add(protocalPosition);
		}
		return this;
	}
	public CombineVideoBO set(CommonCombineVideoPO video, CodecParamBO codec){
		this.setCodec_param(codec)
			.setUuid(video.getUuid())
			.setPosition(new ArrayList<PositionSrcBO>());
		
		String[] resolution = codec.getVideo_param().getResolution().split("x");
		int width = Integer.parseInt(resolution[0]);
		int height = Integer.parseInt(resolution[1]);
		
		Set<CommonCombineVideoPositionPO> positions = video.getPositions();
		for(CommonCombineVideoPositionPO position:positions){
			PositionSrcBO protocalPosition = new PositionSrcBO().set(position, width, height);
			this.getPosition().add(protocalPosition);
		}
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CombineVideoBO other = (CombineVideoBO) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
}
