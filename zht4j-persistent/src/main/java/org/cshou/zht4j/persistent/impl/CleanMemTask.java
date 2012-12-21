/**
 * Logic for clean cache:
 * (1) when putting a new object, if find cache is "full"
 * (2) add lock for cleaning process to prevent other threads execute cleaning
 * (3) record the current time
 * (4) persist cache to disk
 * (5) add lock to cache
 * (6) do things in this class:
 *     create a priority queue
 *     add all time record to this queue
 *     retrieve half capacity elements from the queue
 *     if the time record is earlier than the starting time
 *     and cache has a corresponding record
 *     then remove it from cache
 * (7) release cache lock
 * (8) release cleaning process lock
 */
package org.cshou.zht4j.persistent.impl;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.entity.TimeRecord;

/**
 * @author cshou
 *
 */
public class CleanMemTask extends Thread {
	
	private SimpleDB simpleDB;
	private long startTime;
	
	public CleanMemTask (SimpleDB simpleDB, long startTime) {
		this.simpleDB = simpleDB;
		this.startTime = startTime;
	}
	
	@Override
	public void run () {
		
		ConcurrentMap<String, DBEntity> memCache = simpleDB.getMemCache();
		ConcurrentMap<String, TimeRecord> lru = simpleDB.getLruRecord();
		
		Queue<TimeRecord> priority = new PriorityQueue<TimeRecord>();
		
		for (String key : lru.keySet()) {
			priority.add(lru.get(key));
		}
		
		for (int i = 0; i < simpleDB.getCapacity() / 2; i++) {
			
			if (priority.isEmpty())
				break;
			
			TimeRecord tr = priority.poll();
			if (memCache.containsKey(tr.getKey()) && tr.getTime() < startTime) {
				memCache.remove(tr.getKey());
				lru.remove(tr.getKey());
			} else if (!memCache.containsKey(tr.getKey()))
				lru.remove(tr.getKey());
		}
		
	}
	
}