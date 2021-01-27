package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.base.bo.LayerBody;
import com.suma.venus.resource.base.bo.LayerHeartBeatRequest;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.entity.dao.HeartBeatBundleDAO;
import com.sumavision.signal.bvc.entity.enumeration.OnlineStatus;
import com.sumavision.signal.bvc.entity.po.HeartBeatBundlePO;
import com.sumavision.signal.bvc.feign.ResourceServiceClient;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.mq.ProcessReceivedMsg;
import com.sumavision.signal.bvc.mq.bo.BundleBO;
import com.sumavision.signal.bvc.resource.util.ResourceQueryUtil;
import com.sumavision.signal.bvc.terminal.JV220Param;
import com.sumavision.signal.bvc.terminal.TerminalParam;

/**
 * 心跳service<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年8月2日 上午9:09:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HeartBeatService {
	
	public static final int BUNDLE_INTERVAL = 20000;
	
	public static final int LAYER_INTERVAL = 20000;
	
	private static final Logger LOG = LoggerFactory.getLogger(HeartBeatService.class);
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private HeartBeatBundleDAO heartBeatBundleDao;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private ProcessReceivedMsg processReceivedMsg;
	
	@Autowired
	private HeartBeatService heartBeatService;
	
	@Autowired
	private JV220Param jv220Param;
	
	/**
	 * 初始化心跳设备和接入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月2日 上午9:22:12
	 * @throws Exception
	 */
	public void initHeart() throws Exception{
		
		//接入心跳资源上报
		Thread layerHeartBeaThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(LAYER_INTERVAL);
					}catch(Exception e){
						LOG.error("接入心跳线程被打断！", e);
					}
					try{
						String layerId = RegisterStatus.getNodeId();
						LayerHeartBeatRequest layerRequest = new LayerHeartBeatRequest();
						LayerBody layer_heartbeat_request = new LayerBody();
						layer_heartbeat_request.setLayer_id(layerId);
						layerRequest.setLayer_heartbeat_request(layer_heartbeat_request);
						
						resourceServiceClient.layerHeartBeat(layerRequest);
					}catch(Exception e){
						LOG.error("接入心跳线程执行异常！", e);
					}
				}
			}
		});
		
		//设备心跳检测
		Thread bundleHeartBeatThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(BUNDLE_INTERVAL);
					}catch(Exception e){
						LOG.error("设备心跳线程被打断！", e);
					}
					try{
						String layerId = RegisterStatus.getNodeId();
						List<BundlePO> bundles = new ArrayList<BundlePO>();
						bundles.addAll(resourceQueryUtil.queryBundlesByAccessNodeUid(layerId, "jv210"));
						bundles.addAll(resourceQueryUtil.queryBundlesByAccessNodeUid(layerId, "proxy"));
						bundles.addAll(resourceQueryUtil.queryBundlesByAccessNodeUid(layerId, "jv220"));
						List<HeartBeatBundlePO> heartBeatBundles = heartBeatBundleDao.findAll();
						
						List<HeartBeatBundlePO> needRemoveBundles = new ArrayList<HeartBeatBundlePO>();
						List<BundlePO> needAddBundles = new ArrayList<BundlePO>();
						List<HeartBeatBundlePO> needAddHeartBeatBundles = new ArrayList<HeartBeatBundlePO>();
						List<HeartBeatBundlePO> needUpdateBundles = new ArrayList<HeartBeatBundlePO>();
						
						//增
						for(BundlePO bundle: bundles){
							boolean needAdd = true;
							for(HeartBeatBundlePO heartBeatBundle: heartBeatBundles){
								if(heartBeatBundle.getBundleId().equals(bundle.getBundleId())){
									needAdd = false;
									break;
								}
							}
							
							if(needAdd){
								needAddBundles.add(bundle);
							}
						}
						
						//删
						for(HeartBeatBundlePO heartBeatBundle: heartBeatBundles){
							boolean needRemove = true;
							for(BundlePO bundle: bundles){
								if(bundle.getBundleId().equals(heartBeatBundle.getBundleId())){
									needRemove = false;
									break;
								}
							}
							
							if(needRemove){
								needRemoveBundles.add(heartBeatBundle);
							}
						}
						
						//改--ip变化
						for(HeartBeatBundlePO heartBeatBundle: heartBeatBundles){
							boolean needUpdate = false;
							String ip = heartBeatBundle.getDeviceIp();
							OnlineStatus status = heartBeatBundle.getOnlineStatus();
							for(BundlePO bundle: bundles){
								if(heartBeatBundle.getDeviceIp() == null || (bundle.getBundleId().equals(heartBeatBundle.getBundleId()) && bundle.getDeviceIp() != null && !bundle.getDeviceIp().equals(heartBeatBundle.getDeviceIp()))){
									needUpdate = true;
									ip = bundle.getDeviceIp();
								}
								if(heartBeatBundle.getOnlineStatus() == null || (bundle.getBundleId().equals(heartBeatBundle.getBundleId()) && bundle.getOnlineStatus() != null && !bundle.getOnlineStatus().equals(heartBeatBundle.getOnlineStatus()))){
									needUpdate = true;
									status = OnlineStatus.valueOf(bundle.getOnlineStatus().toString());
								}
								if(needUpdate){
									break;
								}
							}
							
							if(needUpdate){
								heartBeatBundle.setDeviceIp(ip);
								needUpdateBundles.add(heartBeatBundle);
							}
						}
						
						for(BundlePO bundle: needAddBundles){
							HeartBeatBundlePO heartBeatBundle = new HeartBeatBundlePO();
							heartBeatBundle.setBundleId(bundle.getBundleId());
							heartBeatBundle.setBundleName(bundle.getBundleName());
							heartBeatBundle.setBundleType(bundle.getBundleType());
							heartBeatBundle.setDeviceIp(bundle.getDeviceIp());
							heartBeatBundle.setDeviceModel(bundle.getDeviceModel());
							heartBeatBundle.setLayerId(bundle.getAccessNodeUid());
							heartBeatBundle.setUpdateTime(new Date());
							
							needAddHeartBeatBundles.add(heartBeatBundle);
						}
						
						if(needRemoveBundles != null && needRemoveBundles.size() > 0){
							heartBeatBundleDao.deleteInBatch(needRemoveBundles);
						}

						if(needAddHeartBeatBundles != null && needAddHeartBeatBundles.size() > 0){
							heartBeatBundleDao.saveAll(needAddHeartBeatBundles);
						}
						
						if(needUpdateBundles != null && needUpdateBundles.size() > 0){
							heartBeatBundleDao.saveAll(needUpdateBundles);
						}
						
						List<HeartBeatBundlePO> allBundles = heartBeatBundleDao.findAll();
						
						for(HeartBeatBundlePO bundle: allBundles){
							String deviceIp = bundle.getDeviceIp();
							if(deviceIp != null){
								//心跳http--jv210和proxy端口不一样
								if(bundle.getDeviceModel().equals("jv210")){
									HttpAsyncClient.getInstance().formGet("http://" + deviceIp + TerminalParam.GET_HEART_BEAT_SUFFIX, null, new HeartBeatCallBack(bundle.getId(), heartBeatBundleDao, resourceServiceClient, heartBeatService));
								}else if(bundle.getDeviceModel().equals("proxy")){
									HttpAsyncClient.getInstance().formGet("http://" + deviceIp + ":8981" + TerminalParam.GET_HEART_BEAT_SUFFIX, null, new HeartBeatCallBack(bundle.getId(), heartBeatBundleDao, resourceServiceClient, heartBeatService));
								}else if(bundle.getDeviceModel().equals("jv220")){
									HttpAsyncClient.getInstance().httpAsyncPost("http://" + deviceIp + TerminalParam.JV220_API_SUFFIX, jv220Param.test().toJSONString(), null, new JV220HeartBeatCallBack(bundle.getId(), heartBeatBundleDao, resourceServiceClient, heartBeatService));
								}
							}
						}
					}catch(Exception e){
						LOG.error("心跳线程执行异常！", e);
					}
				}
			}
		});
		
		bundleHeartBeatThread.start();
		layerHeartBeaThread.start();
		LOG.info("心跳线程启动！");
	}
	
	/**
	 * 更新心跳设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午2:03:11
	 */
	public void updateHeartbeat() throws Exception{
		
		String layerId = RegisterStatus.getNodeId();
		List<BundlePO> bundles = new ArrayList<BundlePO>();
		bundles.addAll(resourceQueryUtil.queryBundlesByAccessNodeUid(layerId, "jv210"));
		bundles.addAll(resourceQueryUtil.queryBundlesByAccessNodeUid(layerId, "proxy"));
		bundles.addAll(resourceQueryUtil.queryBundlesByAccessNodeUid(layerId, "jv220"));
		List<HeartBeatBundlePO> heartBeatBundles = heartBeatBundleDao.findAll();
		
		List<HeartBeatBundlePO> needRemoveBundles = new ArrayList<HeartBeatBundlePO>();
		List<BundlePO> needAddBundles = new ArrayList<BundlePO>();
		List<HeartBeatBundlePO> needAddHeartBeatBundles = new ArrayList<HeartBeatBundlePO>();
		List<HeartBeatBundlePO> needUpdateBundles = new ArrayList<HeartBeatBundlePO>();
		
		//增
		for(BundlePO bundle: bundles){
			boolean needAdd = true;
			for(HeartBeatBundlePO heartBeatBundle: heartBeatBundles){
				if(heartBeatBundle.getBundleId().equals(bundle.getBundleId())){
					needAdd = false;
					break;
				}
			}
			
			if(needAdd){
				needAddBundles.add(bundle);
			}
		}
		
		//删
		for(HeartBeatBundlePO heartBeatBundle: heartBeatBundles){
			boolean needRemove = true;
			for(BundlePO bundle: bundles){
				if(bundle.getBundleId().equals(heartBeatBundle.getBundleId())){
					needRemove = false;
					break;
				}
			}
			
			if(needRemove){
				needRemoveBundles.add(heartBeatBundle);
			}
		}
		
		//改--ip变化
		for(HeartBeatBundlePO heartBeatBundle: heartBeatBundles){
			boolean needUpdate = false;
			String ip = "";
			for(BundlePO bundle: bundles){
				if(heartBeatBundle.getDeviceIp() == null || (bundle.getBundleId().equals(heartBeatBundle.getBundleId()) && bundle.getDeviceIp() != null && !bundle.getDeviceIp().equals(heartBeatBundle.getDeviceIp()))){
					needUpdate = true;
					ip = bundle.getDeviceIp();
					break;
				}
			}
			
			if(needUpdate){
				heartBeatBundle.setDeviceIp(ip);
				needUpdateBundles.add(heartBeatBundle);
			}
		}
		
		for(BundlePO bundle: needAddBundles){
			HeartBeatBundlePO heartBeatBundle = new HeartBeatBundlePO();
			heartBeatBundle.setBundleId(bundle.getBundleId());
			heartBeatBundle.setBundleName(bundle.getBundleName());
			heartBeatBundle.setBundleType(bundle.getBundleType());
			heartBeatBundle.setDeviceIp(bundle.getDeviceIp());
			heartBeatBundle.setDeviceModel(bundle.getDeviceModel());
			heartBeatBundle.setLayerId(bundle.getAccessNodeUid());
			heartBeatBundle.setUpdateTime(new Date());
			
			needAddHeartBeatBundles.add(heartBeatBundle);
		}
		
		if(needRemoveBundles != null && needRemoveBundles.size() > 0){
			heartBeatBundleDao.deleteInBatch(needRemoveBundles);
		}

		if(needAddHeartBeatBundles != null && needAddHeartBeatBundles.size() > 0){
			heartBeatBundleDao.saveAll(needAddHeartBeatBundles);
		}
		
		if(needUpdateBundles != null && needUpdateBundles.size() > 0){
			heartBeatBundleDao.saveAll(needUpdateBundles);
		}
	}	
	
	/**
	 * jv210终端恢复流程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:04:04
	 */
	public void bundleJv210Resume(BundleBO bundle) throws Exception{

		processReceivedMsg.bundleJv210Resume(bundle);
	}
	/**
	 * proxy终端恢复流程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:04:04
	 */
	public void bundleProxyResume(BundleBO bundle) throws Exception{

		processReceivedMsg.bundleProxyResume(bundle);
	}
	
	/**
	 * jv220终端恢复流程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 上午8:48:49
	 */
	public void bundleJv220Resume(BundleBO bundle) throws Exception{
		
		processReceivedMsg.bundleJv220Resume(bundle);
		
	}
	
}
