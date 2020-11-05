package com.sumavision.bvc.device.group.service.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.sumavision.bvc.common.group.po.CommonCombineAudioPO;
import com.sumavision.bvc.common.group.po.CommonCombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @ClassName: 混音业务数据对象<br/> 
 * @author lvdeyang
 * @date 2018年8月12日 下午5:01:09 
 */
public class CombineAudioBO {

	/** 混音的uuid CombineAudioPO.uuid*/
	private String uuid;
	
	/** 混音源列表，DeviceGroupMemberPO.id */
	private List<Long> srcs;
	
	/** 设备组全部打开音频的成员：DeviceGroupMemberPO.id */
	private Collection<Long> totalVoiced;
	
	public String getUuid() {
		return uuid;
	}

	public CombineAudioBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public List<Long> getSrcs() {
		return srcs;
	}

	public CombineAudioBO setSrcs(List<Long> srcs) {
		this.srcs = srcs;
		return this;
	}

	public Collection<Long> getTotalVoiced() {
		return totalVoiced;
	}

	public CombineAudioBO setTotalVoiced(Collection<Long> totalVoiced) {
		this.totalVoiced = totalVoiced;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		CombineAudioBO other = (CombineAudioBO)obj;
		String thisSrcs = this.formatSrc();
		String otherSrcs = other.formatSrc();
		return thisSrcs.equals(otherSrcs);
	}
	
	/**
	 * @Title: 格式化源<br/> 
	 * @Description: 将源排序，返回id@@id格式的字符串
	 * @return String 格式化的源字符串
	 */
	public String formatSrc(){
		List<Long> srcs = this.getSrcs();
		if(srcs==null || srcs.size()<=0) return "";
		Collections.sort(srcs, new CombineAudioBO.srcAscComparator());
		StringBufferWrapper srcBuffer = new StringBufferWrapper();
		for(int i=0; i<srcs.size(); i++){
			srcBuffer.append(srcs.get(i)).append("@@");
		}
		return srcBuffer.toString();
	}
	
	/**
	 * @ClassName: 混音源排序器<br/> 
	 * @author lvdeyang
	 * @date 2018年8月12日 下午4:58:43 
	 */
	public static final class srcAscComparator implements Comparator<Long>{

		@Override
		public int compare(Long o1, Long o2) {
			if(o1.longValue() < o2.longValue()){
				return 1;
			}
			if(o1.longValue() == o2.longValue()){
				return 0;
			}
			return -1;
		}
		
	}
	
	/**
	 * @Title: 从PO中设置数据 
	 * @param entity
	 * @return CombineAudioBO 
	 */
	public CombineAudioBO set(CombineAudioPO entity){
		this.setUuid(entity.getUuid())
			.setSrcs(new ArrayList<Long>());
		Set<CombineAudioSrcPO> srcs = entity.getSrcs();
		for(CombineAudioSrcPO src:srcs){
			this.getSrcs().add(src.getMemberId());
		}
		return this;
	}
	public CombineAudioBO set(CommonCombineAudioPO entity){
		this.setUuid(entity.getUuid())
			.setSrcs(new ArrayList<Long>());
		Set<CommonCombineAudioSrcPO> srcs = entity.getSrcs();
		for(CommonCombineAudioSrcPO src:srcs){
			this.getSrcs().add(src.getMemberId());
		}
		return this;
	}
	
	/**
	 * @Title: 转换已有的混音数据 <br/>
	 * @param entities
	 * @return List<CombineAudioBO>
	 */
	public static List<CombineAudioBO> generateFromCombineAudios(Collection<CombineAudioPO> entities){
		List<CombineAudioBO> audios = new ArrayList<CombineAudioBO>();
		if(entities!=null && entities.size()>0){
			for(CombineAudioPO entity:entities){
				CombineAudioBO audio = new CombineAudioBO().set(entity);
				audios.add(audio);
			}
		}
		return audios;
	}
	public static List<CombineAudioBO> generateFromCombineAudios_Common(Collection<CommonCombineAudioPO> entities){
		List<CombineAudioBO> audios = new ArrayList<CombineAudioBO>();
		if(entities!=null && entities.size()>0){
			for(CommonCombineAudioPO entity:entities){
				CombineAudioBO audio = new CombineAudioBO().set(entity);
				audios.add(audio);
			}
		}
		return audios;
	}
	
	/**
	 * @Title: 根据打开音频的会议成员生成 <br/>
	 * @Description: 1.默认生成清除自己的混音加一个全混音<br/>
	 * 				 2.当所有设备组成员的音频都打开的时候不需要生成全量混音<br/>
	 * @param memberIds 打开音频的会议成员
	 * @return List<CombineAudioBO> 生成的混音列表
	 */
	public static List<CombineAudioBO> generateFromMembers(Collection<Long> totalVoicedIds, List<Long> totalMemberIds){
		List<CombineAudioBO> audios = new ArrayList<CombineAudioBO>();
		CombineAudioBO totalAudios = null;
		if(totalVoicedIds.size() < totalMemberIds.size()){
			totalAudios = new CombineAudioBO().setSrcs(new ArrayList<Long>()).setTotalVoiced(totalVoicedIds);
		}
		for(Long memberId:totalVoicedIds){
			CombineAudioBO filterSelfAudio = new CombineAudioBO().setSrcs(new ArrayList<Long>()).setTotalVoiced(totalVoicedIds);
			for(Long filterMemberId:totalVoicedIds){ 
				if(filterMemberId.longValue() != memberId.longValue()){
					filterSelfAudio.getSrcs().add(filterMemberId);
				}
			}
			if(filterSelfAudio.getSrcs().size() > 0) audios.add(filterSelfAudio);
			if(totalAudios != null) totalAudios.getSrcs().add(memberId);
		}
		if(totalAudios!=null && totalAudios.getSrcs().size()>0) audios.add(totalAudios);
		return audios;
	}
	
	/**
	 * @Title: 根据打开音频的成员生成全量音频<br/> 
	 * @param totalVoicedIds 打开音频的设备组成员id列表
	 * @return CombineAudioBO 音频
	 */
	public static CombineAudioBO getnerateFullAudioFromMembers(Collection<Long> totalVoicedIds){
		if(totalVoicedIds==null || totalVoicedIds.size()<=0) return null;
		CombineAudioBO totalAudios = new CombineAudioBO().setSrcs(new ArrayList<Long>()).setTotalVoiced(totalVoicedIds);
		for(Long memberId:totalVoicedIds){
			totalAudios.getSrcs().add(memberId);
		}
		return totalAudios;
	}
	
	/**
	 * @Title: 判断一个混音是否适合于一个设备组成员<br/> 
	 * @param totalVoicedIds 设备组中所有打开音频的成员
	 * @param memberId
	 * @return boolean
	 */
	public boolean isSuitableForMember(Long memberId){
		List<Long> srcs = this.getSrcs();
		Collection<Long> totalVoicedIds = this.getTotalVoiced();
		if(totalVoicedIds.contains(memberId)){
			//听去自己混音
			if(srcs.size()==(totalVoicedIds.size()-1) && !srcs.contains(memberId)) return true;
		}else{
			//听全量混音
			if(srcs.size() == totalVoicedIds.size()) return true;
		}
		return false;
	}
	
}
