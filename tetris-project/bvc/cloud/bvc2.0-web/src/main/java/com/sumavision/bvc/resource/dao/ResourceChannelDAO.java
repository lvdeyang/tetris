package com.sumavision.bvc.resource.dao;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: 资源层channel查询对象<br/> 
 * @author lvdeyang
 * @date 2018年8月25日 下午6:15:37 
 */
@Slf4j
@Repository
public class ResourceChannelDAO {
	
	/** 编码通道 */
	public static final int ENCODE = 0;
	
	/** 解码通道 */
	public static final int DECODE = 1;
	
	/** 视频编码通道 */
	public static final int ENCODE_VIDEO = 2;
	
	/** 音频编码通道 */
	public static final int ENCODE_AUDIO = 3;
	
	/** 视频解码通道 */
	public static final int DECODE_VIDEO = 4;
	
	/** 音频解码通道 */
	public static final int DECODE_AUDIO = 5;
	
	@Resource
	@Qualifier("resourceEntityManager")
	private EntityManager resourceEntityManager;
	
	@PersistenceUnit(unitName="resourceEntityManagerFactory")
    private EntityManagerFactory emf;
	
	/**
	 * @Title: 根据设备id批量查询通道 
	 * @param bundleIds 设备id集合
	 * @return List<ChannelSchemePO> 通道
	 */
	public List<ChannelSchemeDTO> findByBundleIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		StringBufferWrapper sqlBuffer = new StringBufferWrapper().append("select new com.sumavision.bvc.resource.dto.ChannelSchemeDTO(")
																 .append("channel.id, ")
																 .append("channel.bundleId, ")
																 .append("channel.channelName, ")
																 .append("channel.channelId, ")
																 .append("channel.channelStatus, ")
																 .append("channel.operateIndex, ")
																 .append("template.id, ")
																 .append("template.deviceModel, ")
																 .append("template.bundleType, ")
																 .append("template.channelName, ")
																 .append("template.maxChannelCnt, ")
																 .append("template.baseType, ")
																 .append("template.externType, ")
																 .append("template.paramScope")
																 .append(") ")
																 .append("from ChannelSchemePO channel, ChannelTemplatePO template ")
																 .append("where channel.channelTemplateID=template.id ")
																 .append("and channel.bundleId in ?1");
		TypedQuery<ChannelSchemeDTO> query = resourceEntityManager.createQuery(sqlBuffer.toString(), ChannelSchemeDTO.class);
		query.setParameter(1, bundleIds);
		List<ChannelSchemeDTO> channels = gainResultList(sqlBuffer.toString(), ChannelSchemeDTO.class, bundleIds, null, null, null);
		return channels;
	}
	
	/**
	 * 根据通道编解码类型查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 上午11:46:19
	 * @param Collection<String> bundleIds 设备id列表
	 * @param int channelType 通道类型, 0:编码  1:解码
	 * @return List<ChannelSchemeDTO> 通道列表
	 */
	public List<ChannelSchemeDTO> findByBundleIdsAndChannelType(Collection<String> bundleIds, int channelType){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		StringBufferWrapper sqlBuffer = new StringBufferWrapper().append("select new com.sumavision.bvc.resource.dto.ChannelSchemeDTO(")
																 .append("channel.id, ")
																 .append("channel.bundleId, ")
																 .append("channel.channelName, ")
																 .append("channel.channelId, ")
																 .append("channel.channelStatus, ")
																 .append("channel.operateIndex, ")
																 .append("template.id, ")
																 .append("template.deviceModel, ")
																 .append("template.bundleType, ")
																 .append("template.channelName, ")
																 .append("template.maxChannelCnt, ")
																 .append("template.baseType, ")
																 .append("template.externType, ")
																 .append("template.paramScope")
																 .append(") ")
																 .append("from ChannelSchemePO channel, ChannelTemplatePO template ")
																 .append("where channel.channelTemplateID=template.id ")
																 .append("and channel.bundleId in ?1 ");
		if(ENCODE == channelType){
			sqlBuffer.append("and (template.baseType='VenusAudioIn' or template.baseType='VenusVideoIn')");
		}else if(DECODE == channelType){
			sqlBuffer.append("and (template.baseType='VenusAudioOut' or template.baseType='VenusVideoOut')");
		}else if(ENCODE_VIDEO == channelType){
			sqlBuffer.append("and (template.baseType='VenusVideoIn')");
		}else if(ENCODE_AUDIO == channelType){
			sqlBuffer.append("and (template.baseType='VenusAudioIn')");
		}else if(DECODE_VIDEO == channelType){
			sqlBuffer.append("and (template.baseType='VenusVideoOut')");
		}else if(DECODE_AUDIO == channelType){
			sqlBuffer.append("and (template.baseType='VenusAudioOut')");
		}
		TypedQuery<ChannelSchemeDTO> query = resourceEntityManager.createQuery(sqlBuffer.toString(), ChannelSchemeDTO.class);
		query.setParameter(1, bundleIds);
		List<ChannelSchemeDTO> channels = gainResultList(sqlBuffer.toString(), ChannelSchemeDTO.class, bundleIds, null, null, null);
		return channels;
	}
	
	/**
	 * 根据通道id查询通道，用于批量查询“视频编码通道1”等场景<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param Collection<String> bundleIds 设备id列表
	 * @param String channelId 通道id
	 * @return List<ChannelSchemeDTO> 通道列表
	 */
	public List<ChannelSchemeDTO> findByBundleIdsAndChannelId(Collection<String> bundleIds, String channelId){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		StringBufferWrapper sqlBuffer = new StringBufferWrapper().append("select new com.sumavision.bvc.resource.dto.ChannelSchemeDTO(")
																 .append("channel.id, ")
																 .append("channel.bundleId, ")
																 .append("channel.channelName, ")
																 .append("channel.channelId, ")
																 .append("channel.channelStatus, ")
																 .append("channel.operateIndex, ")
																 .append("template.id, ")
																 .append("template.deviceModel, ")
																 .append("template.bundleType, ")
																 .append("template.channelName, ")
																 .append("template.maxChannelCnt, ")
																 .append("template.baseType, ")
																 .append("template.externType, ")
																 .append("template.paramScope")
																 .append(") ")
																 .append("from ChannelSchemePO channel, ChannelTemplatePO template ")
																 .append("where channel.channelTemplateID=template.id ")
																 .append("and channel.bundleId in ?1 ")
																 .append("and channel.channelId = '")
																 .append(channelId)
																 .append("'");
		TypedQuery<ChannelSchemeDTO> query = resourceEntityManager.createQuery(sqlBuffer.toString(), ChannelSchemeDTO.class);
		query.setParameter(1, bundleIds);
		List<ChannelSchemeDTO> channels = gainResultList(sqlBuffer.toString(), ChannelSchemeDTO.class, bundleIds, null, null, null);
		return channels;
	}
	
	/**
	 * 根据通道id查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月24日 下午5:58:11
	 * @param String channelId 通道id
	 * @return ChannelSchemeDTO 通道
	 */
	public ChannelSchemeDTO findByChannelId(String channelId){
		StringBufferWrapper sqlBuffer = new StringBufferWrapper().append("select new com.sumavision.bvc.resource.dto.ChannelSchemeDTO(")
																 .append("channel.id, ")
																 .append("channel.bundleId, ")
																 .append("channel.channelName, ")
																 .append("channel.channelId, ")
																 .append("channel.channelStatus, ")
																 .append("channel.operateIndex, ")
																 .append("template.id, ")
																 .append("template.deviceModel, ")
																 .append("template.bundleType, ")
																 .append("template.channelName, ")
																 .append("template.maxChannelCnt, ")
																 .append("template.baseType, ")
																 .append("template.externType, ")
																 .append("template.paramScope")
																 .append(") ")
																 .append("from ChannelSchemePO channel, ChannelTemplatePO template ")
																 .append("where channel.channelTemplateID=template.id ")
																 .append("and channel.channelId in ?1");
		TypedQuery<ChannelSchemeDTO> query = resourceEntityManager.createQuery(sqlBuffer.toString(), ChannelSchemeDTO.class);
		List<ChannelSchemeDTO> channels = gainResultList(sqlBuffer.toString(), ChannelSchemeDTO.class, channelId, null, null, null);
		if(channels!=null || channels.size()>0){
			return channels.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * @Title: 通用查询方法，最多支持4个参数；当数据库重启导致查询失败时，会重新创建EntityManager实体再查询<br/> 
	 * @param qlString 查询语句
	 * @param resultClass 返回类型
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> gainResultList(String qlString, Class<?> resultClass,
			Object param1,
			Object param2,
			Object param3,
			Object param4){
		TypedQuery<?> query = resourceEntityManager.createQuery(qlString, resultClass);
		if(param1 != null) query.setParameter(1, param1);
		if(param2 != null) query.setParameter(2, param2);
		if(param3 != null) query.setParameter(3, param3);
		if(param4 != null) query.setParameter(4, param4);
		List<T> resultList;
		try{
			resultList = (List<T>) query.getResultList();
		}catch (Exception e){
			log.warn("ResourceChannelDAO query.getResultList()抛错，重设resourceEntityManager");
			resultList = regainResultList(qlString, resultClass, param1, param2, param3, param4);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> regainResultList(String qlString, Class<?> resultClass,
			Object param1,
			Object param2,
			Object param3,
			Object param4){
//		resourceEntityManager.close();
		resourceEntityManager = emf.createEntityManager();
		TypedQuery<?> query = resourceEntityManager.createQuery(qlString, resultClass);
		if(param1 != null) query.setParameter(1, param1);
		if(param2 != null) query.setParameter(2, param2);
		if(param3 != null) query.setParameter(3, param3);
		if(param4 != null) query.setParameter(4, param4);
		List<T> resultList = (List<T>) query.getResultList();
		return resultList;
	}
	
}
