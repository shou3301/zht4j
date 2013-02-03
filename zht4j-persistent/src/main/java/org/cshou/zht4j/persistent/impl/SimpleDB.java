/*
 * TODO
 * 
 * 1. to modify the interface to generic
 * 
 * 2. currently, there is no persist record for keys,
 * as a result, when the node breaks down, there is no way
 * to recover from failure and still keep the original state.
 * 
 * One way is to create a operation log,
 * then later, it can catch up with the process by applying
 * operation log again.
 * 
 * Still need more consideration!
 */
package org.cshou.zht4j.persistent.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.cshou.zht4j.persistent.entity.DBDescriptor;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.entity.EmptyEntity;
import org.cshou.zht4j.persistent.entity.TimeRecord;
import org.cshou.zht4j.persistent.intl.PersistentStorage;

/**
 * @author cshou
 * the current implementation of persistent storage
 */
public class SimpleDB implements PersistentStorage {
	
	/**
	 * the default path to save data
	 */
	private static final String defaultDBFile = "simpledb/storage.db";
	
	/**
	 * the default path to operation log
	 */
	private static final String defaultOpLog = "simpledb/opertaion.log";
	
	/**
	 * the default frequency to execute persist task
	 */
	private static final long defaultFreq = 600000L;
	
	/**
	 * the default capacity for the in-memory cache
	 */
	private static final int defaultCapacity = 12800;
	
	/**
	 * the in-memory cache
	 */
	protected ConcurrentMap<String, DBEntity> memCache;
	
	/**
	 * the list to make it a lru cache
	 */
	protected ConcurrentLinkedQueue<TimeRecord> lru;
	
	/**
	 * the database descriptor,
	 * which maps memory to file
	 */
	protected DBDescriptor dbDescriptor;
	
	/**
	 * database file name
	 */
	protected String dbFileName;
	protected File dbFile;
	
	/**
	 * operation log file
	 */
	protected File logFile;
	
	/**
	 * the capacity of cache
	 */
	protected final int capacity;
	
	/**
	 * the atomic lock to lock clean process
	 * this guarantees that if a clean task is undergoing,
	 * following attempts to do clean task will be skipped 
	 */
	protected AtomicBoolean cleanLock;
	
	/**
	 * the lock to make to operation to cache and lru list
	 * as an atomic operation
	 * (this might compromise some performance) 
	 */
	protected Lock evictionLock;
	
	/**
	 * the timer to invoke a persist task
	 */
	protected Timer timer;
	
	public SimpleDB () {
		
		// for test comment
		
		this(defaultDBFile, defaultFreq, defaultCapacity);
	}
	
	public SimpleDB (String dbFileName) {
		this(dbFileName, defaultFreq, defaultCapacity);
	}
	
	public SimpleDB (long freq) {
		this(defaultDBFile, freq, defaultCapacity);
	}
	
	public SimpleDB (int capacity) {
		this(defaultDBFile, defaultFreq, capacity);
	}
	
	public SimpleDB (String dbFileName, long freq, int capacity) {
		
		this.capacity = capacity;
		
		memCache = new ConcurrentHashMap<String, DBEntity>();
		lru = new ConcurrentLinkedQueue<TimeRecord>();
		
		try {
			
			dbFile = new File(dbFileName);
			if (dbFile.exists())
				dbFile.delete();
			dbFile.createNewFile();
			
			logFile = new File(defaultOpLog);
			if (logFile.exists())
				logFile.delete();
			logFile.createNewFile();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbDescriptor = new DBDescriptor(dbFile);
		
		timer = new Timer(true);
		timer.schedule(PersistTask.getPersistTask(this), freq, freq);
		// single node test
		// timer.schedule(new PersistTask(this), freq, freq);
		
		cleanLock = new AtomicBoolean();
		evictionLock = new ReentrantLock();
	}
	
	public int put (String key, DBEntity value) {
		
		evictionLock.lock();
		try {
		
			if (value != null)
				memCache.put(key, value);
			else
				memCache.put(key, new EmptyEntity());
			
			lru.add(new TimeRecord(key, new Date().getTime()));
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		finally {
			evictionLock.unlock();
		}
		
		// for test
		// System.out.println("Cache: " + memCache.keySet());
		// System.out.println("LRU: " + lruRecord);
		
		/* there is a lock means a clean task is undergoing,
		 * don't double clean task
		 * */
		if (getSize() > capacity && !cleanLock.get()) {
			cleanLock.set(true);
			cleanCache();
			// should not set lock back here
			// cleanlock.set(false);
		}
		
		return 0;
	}

	public DBEntity get (String key) {
		
		boolean inCache = true;
		boolean inDisk = true;
		
		DBEntity entity = searchMem(key);
		
		if (entity == null) {
			entity = searchDisk(key);
			inCache = false;
			if (entity == null)
				inDisk = false;
		}
		
		if (!inCache && inDisk) {
			
			evictionLock.lock();
			try {
				memCache.put(key, entity);
				lru.add(new TimeRecord(key, new Date().getTime()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				evictionLock.unlock();
			}
		}
		
		// for test
		// System.out.println(memCache.keySet());
		
		return entity;
	}

	public int remove (String key) {
		
		put(key, new EmptyEntity());
		
		return 0;
	}
	
	public int getSize() {
		return memCache.size();
	}
	
	/**
	 * @return 0 means success
	 * clean cache operation is very expensive
	 */
	private int cleanCache () {
		
		// TODO begin persist task
		long startTime = new Date().getTime();
		
		// TODO commented for test
		PersistTask.getPersistTask(this).run();
		// single node test
		// new PersistTask(this).run();
		
		new CleanMemTask(this, startTime).run();
		
		return 0;
	}
	
	private DBEntity searchMem (String key) {
		
		DBEntity entity = null;
		
		if (memCache.containsKey(key)) {
			entity = memCache.get(key);
			if (entity instanceof EmptyEntity)
				entity = null;
		}
		
		// for test
		// System.out.println("Get object from mem cache = " + entity);
		
		return entity;
	}
	
	private DBEntity searchDisk (String key) {
		
		DBEntity entity = null;
		
		if (!dbDescriptor.containsKey(key))
			return entity;
		
		try {
			
			RandomAccessFile file = new RandomAccessFile(dbFile, "r");
			
			long offset = dbDescriptor.getKeyOffset(key);
			int length = dbDescriptor.getKeyLength(key);
			
			file.seek(offset);
			
			byte[] buffer = new byte[length];
			
			file.readFully(buffer);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			ObjectInput in = null;
			
			in = new ObjectInputStream(bis);
			entity = (DBEntity) in.readObject();
			
			file.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// for test
		// System.out.println("Get object from disk = " + entity);
		
		return entity;
	}
	
	public int getCapacity () {
		return this.capacity;
	}

	public ConcurrentMap<String, DBEntity> getMemCache () {
		return this.memCache;
	}
	
	public ConcurrentLinkedQueue<TimeRecord> getLru () {
		return this.lru;
	}
	
	public DBDescriptor getDbDescriptor () {
		return this.dbDescriptor;
	}
	
	public synchronized void setCleanLock (boolean flag) {
		this.cleanLock.set(flag);
	}
	
	public Lock getEvictionLock () {
		return this.evictionLock;
	}
	
	public File getOpLog () {
		return this.logFile;
	}
	
}
