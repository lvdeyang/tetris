package com.sumavision.tetris.sts.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.sts.annotation.TotalQueue;
import com.sumavision.tetris.sts.exeception.CommonException;
import com.sumavision.tetris.sts.exeception.ErrorCodes;
import com.sumavision.tetris.sts.exeception.ExceptionI18Message;

/**
 * 确保主要操作都为线性的总线程，并提供各类操作的方法
 * @author gaofeng
 *
 */
@Aspect
@Component
public class TotalOptControl implements Runnable{
	
	static Logger logger = LogManager.getLogger(TotalOptControl.class);

	private PriorityBlockingQueue<CommonOpt> optQueue = new PriorityBlockingQueue<CommonOpt>();
	
	//操作队列中任务id记录，key是tasklinkId，value是当前任务id对应的最新操作的时间
	private static volatile Map<Long, Long> operatingTaskIds = new ConcurrentHashMap<Long, Long>();
	
	@Autowired
	ExceptionI18Message exceptionI18Message;
	private Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				CommonOpt opt = optQueue.take();
				lock.lock();
				try {
					logger.info("opt begin :" + opt.getClass());
					opt.execute();
					logger.info("opt end :" + opt.getClass());
				} catch(CommonException e){
					logger.error("opt err, opt :" + opt.getClass() + ", err :" + e.getErrorCode());
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					logger.error("opt sys err, opt :" + opt.getClass(),e1);
				}
				switch(opt.getOptType()){
				case TASK:
					if(operatingTaskIds.get(opt.getOptId()) != null && operatingTaskIds.get(opt.getOptId()) <= opt.getCreateTime()){
						operatingTaskIds.remove(opt.getOptId());
					}
					break;
				case OTHER:
					break;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				lock.unlock();
			}
		}
	}
	
	/**
	 * 等待3毫秒是为了解决，同一个业务操作把两个操作在一个时间压入，出队列顺序就不能保证了
	 * 例如：任务重建，连续压入了任务删除和创建
	 * @param opt
	 * @return
	 */
	public boolean addOpt(CommonOpt opt){
		try {
			Thread.sleep(3l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch(opt.getOptType()){
		case TASK:
			operatingTaskIds.put(opt.getOptId(), opt.getCreateTime());
			break;
		case OTHER:
			break;
		}
		return optQueue.add(opt);
	}
	
	/**
	 * 外部锁队列方法，执行过程中不允许
	 * @return
	 * @throws Throwable 
	 */
	@Around("within(com.suma.xianrd.sirius.service..*) && @annotation(totalQueue)")
	public Object lock(ProceedingJoinPoint pjp,TotalQueue totalQueue) throws Throwable{
		boolean getLock = false;
		try {
			getLock = lock.tryLock();
			if(!getLock){
				throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.QUEUE_BLOCK));
			}
			logger.info("totalOptControl get lock by other!");
			Object result = pjp.proceed();
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			throw e;
		}finally{
			if(getLock){
				logger.info("totalOptControl unlock by other!");
				lock.unlock();
			}
		}
	}

	public Lock getLock() {
		return lock;
	}

	public static Map<Long, Long> getOperatingTaskIds() {
		return operatingTaskIds;
	}
	
	
}
