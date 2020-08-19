package com.sumavision.signal.bvc.service;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskDestoryQueue {

	private ConcurrentLinkedQueue<String> destoryQueue;

	private TaskDestoryQueue() {
		this.destoryQueue = new ConcurrentLinkedQueue<String>();
	}
	
	private static TaskDestoryQueue instance;
	
	public static TaskDestoryQueue getInstance(){
		synchronized (TaskDestoryQueue.class) {
			if(instance == null){
				instance = new TaskDestoryQueue();
			}
			return instance;
		}
	}
	
	public ConcurrentLinkedQueue<String> getQueue(){
		return destoryQueue;
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
