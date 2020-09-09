package com.sumavision.signal.bvc.service;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskSwitchQueue {

	private ConcurrentLinkedQueue<String> switchQueue;

	private TaskSwitchQueue() {
		this.switchQueue = new ConcurrentLinkedQueue<String>();
	}
	
	private static TaskSwitchQueue instance;
	
	public static TaskSwitchQueue getInstance(){
		synchronized (TaskSwitchQueue.class) {
			if(instance == null){
				instance = new TaskSwitchQueue();
			}
			return instance;
		}
	}
	
	public ConcurrentLinkedQueue<String> getQueue(){
		return switchQueue;
	}
	
	public synchronized void put(String str){
		ConcurrentLinkedQueue<String> queue = this.getQueue();
		queue.offer(str);
	}
	
	public synchronized void take(String str){
		ConcurrentLinkedQueue<String> queue = this.getQueue();
		queue.remove(str);
	}
	
	public synchronized Object[] get(){
		ConcurrentLinkedQueue<String> queue = this.getQueue();
		if(queue.size() > 0){
			return queue.toArray();
		}
		
		return null;
	}
	
}
