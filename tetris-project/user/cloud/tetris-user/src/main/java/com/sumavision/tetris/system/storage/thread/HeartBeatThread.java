package com.sumavision.tetris.system.storage.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.context.AsynchronizedSystemInitialization;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.system.storage.SystemStorageDAO;
import com.sumavision.tetris.system.storage.SystemStoragePO;
import com.sumavision.tetris.system.storage.gadget.Gadget;

/**
 * 存储心跳<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月7日 下午1:49:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HeartBeatThread implements AsynchronizedSystemInitialization{

	private static final long INTERVAL = 60l*1000l;
	
	@Autowired
	private SystemStorageDAO systemStorageDao;
	
	@Override
	public int index() {
		return 0;
	}

	@Override
	public void init() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run(){
				while(true){
					try{
						Thread.sleep(INTERVAL);
						List<SystemStoragePO> entities = systemStorageDao.findAll();
						if(entities==null || entities.size()<=0){
							continue;
						}
						for(SystemStoragePO entity:entities){
							Gadget gadget = SpringContext.getBean(entity.getServerGadgetType().getBeanName(), Gadget.class);
							boolean online = gadget.heartbeat(entity.getGadgetBasePath());
							entity.setStatus(online?1:0);
						}
						systemStorageDao.save(entities);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
}
