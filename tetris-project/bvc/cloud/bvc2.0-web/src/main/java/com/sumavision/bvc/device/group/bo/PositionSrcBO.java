package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.common.group.po.CommonCombineVideoPositionPO;
import com.sumavision.bvc.common.group.po.CommonCombineVideoSrcPO;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;

/**
 * @ClassName: 协议层分屏布局
 * @author lvdeyang
 * @date 2018年8月7日 上午10:13:17 
 */
public class PositionSrcBO {
	
	private String uuid = "";
	
	private int pollingTime = 0;
	
	/** pause【|polling】 */
	private String pollingStatus;
	
	private int x = 0;
	
	private int y = 0;
	
	private int w = 0;
	
	private int h = 0;
	
	private int z_index = 0;
	
	/** 下一个轮询的序号，-1表示继续轮询当前或由合屏器决定 */
	private int polling_index = -1;
	
	/** 额外定义的操作，目前支持切换为下一个画面，字段值为"next_mixer_video_loop_index" */
	private String operation;
	
	private List<SourceBO> src = new ArrayList<SourceBO>();

	public String getUuid() {
		return uuid;
	}

	public PositionSrcBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public int getPollingTime() {
		return pollingTime;
	}

	public PositionSrcBO setPollingTime(String pollingTime) {
		this.pollingTime = Integer.parseInt(pollingTime);
		return this;
	}

	public String getPollingStatus() {
		return pollingStatus;
	}

	public PositionSrcBO setPollingStatus(String pollingStatus) {
		this.pollingStatus = pollingStatus;
		return this;
	}

	public int getX() {
		return x;
	}

	public PositionSrcBO setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public PositionSrcBO setY(int y) {
		this.y = y;
		return this;
	}

	public int getW() {
		return w;
	}

	public PositionSrcBO setW(int w) {
		this.w = w;
		return this;
	}

	public int getH() {
		return h;
	}

	public PositionSrcBO setH(int h) {
		this.h = h;
		return this;
	}

	public int getZ_index() {
		return z_index;
	}

	public PositionSrcBO setZ_index(String z_index) {
		this.z_index = Integer.parseInt(z_index);
		return this;
	}

	public int getPolling_index() {
		return polling_index;
	}

	public PositionSrcBO setPolling_index(int polling_index) {
		this.polling_index = polling_index;
		return this;
	}

	public String getOperation() {
		return operation;
	}

	public PositionSrcBO setOperation(String operation) {
		this.operation = operation;
		return this;
	}

	public List<SourceBO> getSrc() {
		return src;
	}

	public PositionSrcBO setSrc(List<SourceBO> src) {
		this.src = src;
		return this;
	}
	
	/**
	 * @Title: 生成协议层合屏布局数据 <br/>
	 * @param position 业务层布局数据
	 * @return PositionSrcBO 
	 */
	public PositionSrcBO set(CombineVideoPositionPO position, int width, int height){
		this.setUuid(position.getUuid())
			.setPollingTime(position.getPollingTime()==null?"0":position.getPollingTime())
			.setPollingStatus(PollingStatus.RUN.equals(position.getPollingStatus())?"polling":"pause")
			//.setX(transPixel(width, position.getX()))
			//.setY(transPixel(height, position.getY()))
			//.setW(transPixel(width, position.getW()))
			//.setH(transPixel(height, position.getH()))
			.setX(transTenThousandProportion(position.getX()))
			.setY(transTenThousandProportion(position.getY()))
			.setW(transTenThousandProportion(position.getW()))
			.setH(transTenThousandProportion(position.getH()))
			.setSrc(new ArrayList<SourceBO>());
		
		if(position.getPictureType().equals(PictureType.POLLING)){
			this.setPollingTime(position.getPollingTime());
			this.setPollingStatus(position.getPollingStatus().getProtocal());
		}
		
		List<CombineVideoSrcPO> srcs = position.getSrcs();
		if(srcs!=null && srcs.size()>0){
			for(CombineVideoSrcPO src:srcs){
				SourceBO protocalSource = new SourceBO().set(src);
				this.getSrc().add(protocalSource);
			}
		}	
		return this;
	}
	public PositionSrcBO set(CommonCombineVideoPositionPO position, int width, int height){
		this.setUuid(position.getUuid())
			.setPollingTime(position.getPollingTime()==null?"0":position.getPollingTime())
			.setPollingStatus(PollingStatus.RUN.equals(position.getPollingStatus())?"polling":"pause")
			//.setX(transPixel(width, position.getX()))
			//.setY(transPixel(height, position.getY()))
			//.setW(transPixel(width, position.getW()))
			//.setH(transPixel(height, position.getH()))
			.setX(transTenThousandProportion(position.getX()))
			.setY(transTenThousandProportion(position.getY()))
			.setW(transTenThousandProportion(position.getW()))
			.setH(transTenThousandProportion(position.getH()))
			.setSrc(new ArrayList<SourceBO>());
		
		if(position.getPictureType().equals(PictureType.POLLING)){
			this.setPollingTime(position.getPollingTime());
			this.setPollingStatus(position.getPollingStatus().getProtocal());
		}
		
		List<CommonCombineVideoSrcPO> srcs = position.getSrcs();
		if(srcs!=null && srcs.size()>0){
			for(CommonCombineVideoSrcPO src:srcs){
				SourceBO protocalSource = new SourceBO().set(src);
				this.getSrc().add(protocalSource);
			}
		}	
		return this;
	}
	
	/**
	 * @Title: 将分数转换成万分比<br/> 
	 * @param score 分数
	 * @return int 万分比分子
	 */
	private int transTenThousandProportion(String score){
		String[] structure = score.split("/");
		return Integer.parseInt(structure[0]) * 10000 / Integer.parseInt(structure[1]);
	}
	
	/**
	 * @Title: 按照比例计算像素，暂不使用<br/> 
	 * @param num 被乘数
	 * @param proportion 比例
	 * @return int 计算结果
	 */
	@Deprecated
	private int transPixel(int num, String proportion){
		String[] structure = proportion.split("/");
		return num*Integer.parseInt(structure[0])/Integer.parseInt(structure[1]);
	}
	
}
