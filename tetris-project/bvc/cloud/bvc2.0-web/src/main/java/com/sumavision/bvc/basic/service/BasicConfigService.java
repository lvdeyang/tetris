package com.sumavision.bvc.basic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.basic.bo.BasicConfigForwardBO;
import com.sumavision.bvc.basic.bo.BasicConfigForwardDstBO;
import com.sumavision.bvc.basic.bo.BasicConfigForwardSrcBO;
import com.sumavision.bvc.basic.bo.BusinessBundleBO;
import com.sumavision.bvc.basic.bo.BusinessForwardBO;
import com.sumavision.bvc.basic.bo.BusinessRoleBO;
import com.sumavision.bvc.basic.dao.BasicConfigDAO;
import com.sumavision.bvc.basic.enumeration.ConfigForwardType;
import com.sumavision.bvc.basic.exception.ConfigNameAlreadyExsitedException;
import com.sumavision.bvc.basic.po.BasicConfigForwardDstRolePO;
import com.sumavision.bvc.basic.po.BasicConfigForwardPO;
import com.sumavision.bvc.basic.po.BasicConfigForwardSrcRolePO;
import com.sumavision.bvc.basic.po.BasicConfigPO;
import com.sumavision.bvc.basic.util.BasicQueryUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ForwardSetBO;
import com.sumavision.bvc.device.group.bo.ForwardSetDstBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.ScreenBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;

@Service
@Transactional(rollbackFor = Exception.class)
public class BasicConfigService {
	
	@Autowired
	private BasicConfigDAO basicConfigDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private BasicQueryUtil basicQueryUtil;

	/**
	 * 新建议程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午1:59:56
	 * @param String name 议程名
	 * @param String tag 用途标签
	 * @param List<BasicConfigForwardBO> forwards 转发关系
	 * @param boolean doPersistence 是否持久化
	 * @return BasicConfigPO 议程信息
	 */
	public BasicConfigPO saveConfig(
				String name, 
				String tag, 
				List<BasicConfigForwardBO> forwards, 
				boolean doPersistence) throws Exception{
		
		//校验name
		BasicConfigPO config = basicConfigDao.findByName(name);
		if(config != null){
			throw new ConfigNameAlreadyExsitedException(name);
		}
		
		BasicConfigPO _config = new BasicConfigPO();
		_config.setName(name);
		_config.setTag(tag);
		_config.setForwards(new HashSet<BasicConfigForwardPO>());
		
		for(BasicConfigForwardBO forward: forwards){
			BasicConfigForwardPO _forward = new BasicConfigForwardPO();
			_forward.setType(ConfigForwardType.fromName(forward.getType()));
			_forward.setDsts(new HashSet<BasicConfigForwardDstRolePO>());
			
			//源
			if(ConfigForwardType.fromName(forward.getType()).equals(ConfigForwardType.FORWARD)){
				//转发
				BasicConfigForwardSrcBO srcRole = forward.getSrc();
				BasicConfigForwardSrcRolePO _srcRole = new BasicConfigForwardSrcRolePO();
				_srcRole.setRoleId(srcRole.getRoleId());
				_srcRole.setRoleName(srcRole.getRoleName());
				_srcRole.setForward(_forward);
				
				_forward.setSrc(_srcRole);
			}else{
				//TODO: 合屏
				
			}
			
			//目的
			for(BasicConfigForwardDstBO dstRole: forward.getDsts()){
				BasicConfigForwardDstRolePO _dstRole = new BasicConfigForwardDstRolePO();
				_dstRole.setRoleId(dstRole.getRoleId());
				_dstRole.setRoleName(dstRole.getRoleName());
				_dstRole.setForward(_forward);
				
				_forward.getDsts().add(_dstRole);
			}
			
			_forward.setConfig(_config);
			_config.getForwards().add(_forward);
		}
		
		if(doPersistence) basicConfigDao.save(_config);
		
		return _config;
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午2:02:56
	 * @param Long id 议程id
	 */
	public void removeConfig(Long id) throws Exception{
		
		basicConfigDao.delete(id);
		
	}
	
	/**
	 * 批量删除议程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午2:03:20
	 * @param Collection<Long> ids 议程id列表
	 */
	public void batchRemoveConfig(Collection<Long> ids) throws Exception{
		
		basicConfigDao.deleteByIdIn(ids);
		
	}
	
	/**
	 * 执行议程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午6:45:37
	 * @param String tag 议程标签
	 * @param List<BusinessRoleBO>  businessRoles 业务角色
	 * @param CodecParamBO codec 参数
	 * @param boolean doProtocal 是否下发协议
	 * @return List<BusinessForwardBO> 业务转发关系
	 */
	public List<BusinessForwardBO> runConfig(
				String tag, 
				List<BusinessRoleBO> businessRoles, 
				CodecParamBO codec, 
				boolean doProtocal) throws Exception{
		
		List<BusinessForwardBO> businessForwards = new ArrayList<BusinessForwardBO>();
		
		BasicConfigPO config = basicConfigDao.findByTag(tag);
		if(config == null){
			throw new Exception("标签为：" + tag + " 的议程不存在！");
		}
		
		Set<BasicConfigForwardPO> forwards =  config.getForwards();
		for(BasicConfigForwardPO forward: forwards){
			
			if(forward.getSrc() == null) continue;
			if(forward.getDsts() == null || forward.getDsts().size() <= 0) continue;
			
			BusinessRoleBO srcRole = basicQueryUtil.queryBusinessRole(businessRoles, forward.getSrc().getRoleId());
			if(srcRole == null) continue;
			
			Set<BasicConfigForwardDstRolePO> dsts = forward.getDsts();
			
			if(forward.getType().equals(ConfigForwardType.FORWARD)){
				for(BasicConfigForwardDstRolePO dst: dsts){
					
					BusinessRoleBO dstRole = basicQueryUtil.queryBusinessRole(businessRoles, dst.getRoleId());
					if(dstRole == null) continue; 
					
					if(dstRole.isVirtualDevice()){
						//TODO:当角色为虚拟设备，议程本身不关注角色属性，根据传进来的角色属性确定是设备转发还是角色转发
						
					}else{
						List<BusinessBundleBO> srcbundles = srcRole.getBundles();
						if(srcbundles != null && srcbundles.size() > 0){
							
							BusinessBundleBO srcBundle = srcbundles.get(0);
							
							List<BusinessBundleBO> dstbundles = dstRole.getBundles();
							if(dstbundles != null && dstbundles.size() > 0){
								for(BusinessBundleBO dstbundle: dstbundles){
									if(!dstbundle.getDecodeAudioChannelId().isEmpty() && !srcBundle.getEncodeAudioChannelId().isEmpty()){
										BusinessForwardBO businessForward = new BusinessForwardBO().setBundleId(dstbundle.getBundleId())
																								   .setBundleName(dstbundle.getBundleName())
																								   .setVenusBundleType(dstbundle.getBundleType())
																								   .setChannelId(dstbundle.getDecodeAudioChannelId())
																								   .setChannelType(dstbundle.getDecodeAudioChannelType())
																								   .setLayerId(dstbundle.getLayerId())
																								   .setSourceBundleId(srcBundle.getBundleId())
																								   .setSourceBundleName(srcBundle.getBundleName())
																								   .setSourceLayerId(srcBundle.getLayerId())
																								   .setSourceChannelId(srcBundle.getEncodeAudioChannelId());
										
										businessForwards.add(businessForward);
									}
									
									if(!dstbundle.getDecodeVideoChannelId().isEmpty() && !srcBundle.getEncodeVideoChannelId().isEmpty()){
										BusinessForwardBO businessForward = new BusinessForwardBO().setBundleId(dstbundle.getBundleId())
																								   .setBundleName(dstbundle.getBundleName())
																								   .setVenusBundleType(dstbundle.getBundleType())
																								   .setChannelId(dstbundle.getDecodeVideoChannelId())
																								   .setChannelType(dstbundle.getDecodeVideoChannelType())
																								   .setLayerId(dstbundle.getLayerId())
																								   .setSourceBundleId(srcBundle.getBundleId())
																								   .setSourceBundleName(srcBundle.getBundleName())
																								   .setSourceLayerId(srcBundle.getLayerId())
																								   .setSourceChannelId(srcBundle.getEncodeVideoChannelId());
										
										businessForwards.add(businessForward);
									}
								}
							}
						}
					}
				}
			}else if(forward.getType().equals(ConfigForwardType.CONBINEVIDEO)){
				//TODO: 合屏
				
			}
		}
		
		if(doProtocal){
			LogicBO logic = setForward(businessForwards, codec, false);
			executeBusiness.execute(logic, "执行议程：");
		}
		
		return businessForwards;
		
	}
	
	/**
	 * 业务转发生成协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:58:02
	 * @param List<BusinessForwardBO> forwards 业务转发BO
	 * @param CodecParamBO codec 参数
	 * @param boolean doProtocal 是否发送协议
	 * @return LogicBO 协议
	 */
	public LogicBO setForward(
				List<BusinessForwardBO> forwards, 
				CodecParamBO codec, 
				boolean doProtocal) throws Exception{
		
		LogicBO logic = new LogicBO();
		logic.setForwardSet(new ArrayList<ForwardSetBO>());
		
		for(BusinessForwardBO forward: forwards){
			ForwardSetBO forwardSet = new ForwardSetBO();
			forwardSet.setScreens(new ArrayList<ScreenBO>());
			
			ForwardSetSrcBO srcBO = new ForwardSetSrcBO();
			if(forward.getSourceCombineUuid() != null && !forward.getSourceCombineUuid().equals("")){
				//TODO：合屏、混音
				
			}else{
				srcBO.setType("channel")
				   	 .setLayerId(forward.getSourceLayerId())
				   	 .setBundleId(forward.getSourceBundleId())
				   	 .setChannelId(forward.getSourceChannelId());
			}
			
			ForwardSetDstBO dstBO = new ForwardSetDstBO();
			dstBO.setBase_type(forward.getChannelType())
				 .setLayerId(forward.getLayerId())
				 .setBundleId(forward.getBundleId())
				 .setChannelId(forward.getChannelId())
				 .setBundle_type(forward.getVenusBundleType())
				 .setCodec_param(codec);
			
			forwardSet.setSrc(srcBO);
			forwardSet.setDst(dstBO);
			
			logic.getForwardSet().add(forwardSet);
		}
		
		if(doProtocal) executeBusiness.execute(logic, "转发：");
		
		return logic;
		
	}
	
}
