/**
 * 
 */
package org.cshou.zht4j.persistent.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cshou.zht4j.persistent.entity.DBDescriptor;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.entity.EmptyEntity;
import org.cshou.zht4j.persistent.entity.KeyPointer;
import org.cshou.zht4j.persistent.entity.TimeRecord;
import org.cshou.zht4j.persistent.intl.PersistentStorage;

/**
 * @author cshou
 *
 */
public class SimpleDB implements PersistentStorage {
	
	private static final String defaultDBFile = "simpledb/storage.db";
	private static final long defaultFreq = 600000L;
	private static final int defaultCapacity = 12800;
	
	protected ConcurrentMap<String, DBEntity> memCache;
	protected ConcurrentMap<String, TimeRecord> lruRecord;
	protected DBDescriptor dbDescriptor;
	
	protected String dbFileName;
	protected File dbFile;
	
	protected int capacity;
	
	protected AtomicBoolean memlock;
	protected AtomicBoolean cleanlock;
	
	// protected Timer timer;
	
	public SimpleDB () {
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
		lruRecord = new ConcurrentHashMap<String, TimeRecord>();
		
		try {
			dbFile = new File(dbFileName);
			if (dbFile.exists())
				dbFile.delete();
			dbFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbDescriptor = new DBDescriptor(dbFile);
		
		// timer = new Timer(true);
		// timer.schedule(PersistTask.getPersistTask(this), freq, freq);
		
		memlock = new AtomicBoolean();
		cleanlock = new AtomicBoolean();
	}
	
	public int put (String key, DBEntity value) {
		
		// TODO lock when cleaning the cache
		while (memlock.get()) {
			// wait for cache lock
		}
		
		if (value != null)
			memCache.put(key, value);
		else
			memCache.put(key, new EmptyEntity());
		
		lruRecord.put(key, new TimeRecord(new Date().getTime(), key));
		
		/* there is a lock means a clean task is undergoing,
		 * don't double clean task
		 * */
		if (getSize() > capacity && !cleanlock.get()) {
			cleanlock.set(true);
			cleanCache();
			cleanlock.set(false);
		}
		
		return 0;
	}

	public DBEntity get (String key) {
		
		DBEntity entity = searchMem(key);
		
		if (entity == null)
			entity = searchDisk(key);
		
		return entity;
	}

	public int remove (String key) {
		
		// TODO lock, when cleaning the cache
		while (memlock.get()) {
			// wait for cache lock
		}
		
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
		
		// TODO have a record of starting time
		long start = new Date().getTime();
		
		// TODO begin persist task
		PersistTask.getPersistTask(this).run();
		
		// TODO lock and clean cache
		memlock.set(true);
		
		new CleanMemTask(this, start).run();
		
		// TODO unlock cache
		memlock.set(false);
		
		return 0;
	}
	
	private DBEntity searchMem (String key) {
		
		DBEntity entity = null;
		
		if (memCache.containsKey(key)) {
			entity = memCache.get(key);
			if (entity instanceof EmptyEntity)
				entity = null;
		}
		
		return entity;
	}
	
	private DBEntity searchDisk (String key) {
		
		DBEntity entity = null;
		
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
		
		return entity;
	}
	
	public int getCapacity () {
		return this.capacity;
	}

	public ConcurrentMap<String, DBEntity> getMemCache () {
		return this.memCache;
	}
	
	public ConcurrentMap<String, TimeRecord> getLruRecord () {
		return this.lruRecord;
	}
	
	public DBDescriptor getDbDescriptor () {
		return this.dbDescriptor;
	}
	
}
