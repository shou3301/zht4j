/**
 * 
 */
package org.cshou.zht4j.persistent.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cshou.zht4j.persistent.entity.KeyPointer;
import org.cshou.zht4j.persistent.intl.PersistentStorage;

/**
 * @author cshou
 *
 */
public class SimpleDB implements PersistentStorage {
	
	private static final String defaultDBFile = "simpledb/storage.db";
	private static final long defaultFreq = 60000L;
	
	protected ConcurrentMap<String, Object> memCache;
	protected ConcurrentMap<String, Long> lruRecord;
	protected Map<String, KeyPointer> dbDescriptor;
	
	protected String dbFileName;
	protected File dbFile;
	
	protected AtomicBoolean memlock;
	
	protected Timer timer;
	
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
		
		memCache = new ConcurrentHashMap<String, Object>();
		lruRecord = new ConcurrentHashMap<String, Long>();
		dbDescriptor = new HashMap<String, KeyPointer>();
		
		try {
			dbFile = new File(dbFileName);
			if (dbFile.exists())
				dbFile.delete();
			dbFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		timer = new Timer(true);
		timer.schedule(new PersistTask(this), 60000L, freq);
		
		memlock = new AtomicBoolean();
		
	}
	
	public int put (String key, Object value) {
		return 0;
	}

	public Object get (String key) {
		return null;
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
	
}
