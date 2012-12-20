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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cshou.zht4j.persistent.entity.DBDescriptor;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.entity.KeyPointer;
import org.cshou.zht4j.persistent.intl.PersistentStorage;

/**
 * @author cshou
 *
 */
public class SimpleDB implements PersistentStorage {
	
	private static final String defaultDBFile = "simpledb/storage.db";
	private static final long defaultFreq = 60000L;
	
	protected ConcurrentMap<String, DBEntity> memCache;
	protected ConcurrentMap<String, Long> lruRecord;
	protected DBDescriptor dbDescriptor;
	
	protected String dbFileName;
	protected File dbFile;
	
	protected AtomicBoolean memlock;
	
	// protected Timer timer;
	
	public SimpleDB () {
		this(defaultDBFile, defaultFreq);
	}
	
	public SimpleDB (String dbFileName) {
		this(dbFileName, defaultFreq);
	}
	
	public SimpleDB (long freq) {
		this(defaultDBFile, freq);
	}
	
	public SimpleDB (String dbFileName, long freq) {
		
		memCache = new ConcurrentHashMap<String, DBEntity>();
		lruRecord = new ConcurrentHashMap<String, Long>();
		
		try {
			dbFile = new File(dbFileName);
			if (dbFile.exists())
				dbFile.delete();
			dbFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbDescriptor = new DBDescriptor(dbFile);
		
		// Start timer after 1 min
		// timer = new Timer(true);
		// timer.schedule(new PersistTask(this), 60000L, freq);
		
		memlock = new AtomicBoolean();
		
	}
	
	public int put (String key, DBEntity value) {
		// TODO remember to check object size
		
		memCache.put(key, value);
		
		return 0;
	}

	public DBEntity get (String key) {
		
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
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entity;
	}

	public int remove (String key) {
		return 0;
	}
	
	public int getSize() {
		return 0;
	}

	private int cleanCache () {
		return 0;
	}
	
	private Object searchMen (String key) {
		return null;
	}
	
	private Object searchDisk (String key) {
		return null;
	}

	public ConcurrentMap<String, DBEntity> getMemCache () {
		return this.memCache;
	}
	
	public DBDescriptor getDbDescriptor () {
		return this.dbDescriptor;
	}
	
}
