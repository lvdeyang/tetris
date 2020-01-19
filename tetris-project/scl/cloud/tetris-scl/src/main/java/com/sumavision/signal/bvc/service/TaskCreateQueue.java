package com.sumavision.signal.bvc.service;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskCreateQueue {

	private ConcurrentLinkedQueue<String> createQueue;

	private TaskCreateQueue() {
		this.createQueue = new ConcurrentLinkedQueue<String>();
	}
	
	private static TaskCreateQueue instance;
	
	public static TaskCreateQueue getInstance(){
		synchronized (TaskCreateQueue.class) {
			if(instance == null){
				instance = new TaskCreateQueue();
			}
			return instance;
		}
	}
	
	public ConcurrentLinkedQueue<String> getQueue(){
		return createQueue;
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
