package com.sumavision.tetris.system.storage.thread;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.context.AsynchronizedSystemInitialization;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.storage.SystemStorageDAO;
import com.sumavision.tetris.system.storage.SystemStoragePO;
import com.sumavision.tetris.system.storage.SystemStoragePartitionDAO;
import com.sumavision.tetris.system.storage.SystemStoragePartitionPO;
import com.sumavision.tetris.system.storage.gadget.Directory;
import com.sumavision.tetris.system.storage.gadget.Gadget;

/**
 * 存储空间统计<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月7日 下午1:51:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsPartitionSizeThread implements AsynchronizedSystemInitialization{

	private static final long INTERVAL = 60l*60l*1000l;
	
	@Autowired
	private SystemStorageDAO systemStorageDao;
	
	@Autowired
	private SystemStoragePartitionDAO systemStoragePartitionDao;
	
	@Override
	public int index() {
		return 1;
	}

	@Override
	public void init() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(INTERVAL);
						List<SystemStoragePO> storages = systemStorageDao.findAll();
						if(storages==null || storages.size()<=0){
							continue;
						}
						List<SystemStoragePartitionPO> partitions = systemStoragePartitionDao.findAll();
						if(partitions==null || partitions.size()<=0){
							continue;
						}
						for(SystemStoragePO storage:storages){
							Set<String> folderPathes = new HashSet<String>();
							for(SystemStoragePartitionPO partition:partitions){
								if(partition.getStorageId().equals(storage.getId())){
									folderPathes.add(new StringBufferWrapper().append(storage.getRootPath()).append("/").append(partition.getName()).toString());
								}
							}
							Gadget gadget = SpringContext.getBean(storage.getServerGadgetType().getBeanName(), Gadget.class);
							try {
								List<Directory> directories = gadget.statisticsFolderSize(storage.getGadgetBasePath(), folderPathes);
								if(directories!=null && directories.size()>0){
									for(Directory directory:directories){
										for(SystemStoragePartitionPO partition:partitions){
											if(partition.getStorageId().equals(storage.getId()) && 
													directory.getPath().startsWith(storage.getRootPath()) && 
													directory.getPath().endsWith(partition.getName())){
												partition.setUsedSize(directory.getSize());
												partition.setFreeSize(partition.getTotalSize()-partition.getUsedSize());
												break;
											}
										}
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						systemStoragePartitionDao.save(partitions);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
			
		}).start();
		
	}

}
