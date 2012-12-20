/**
 * 
 */
package org.cshou.zht4j.persistent.entity;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author cshou
 * 
 */
public class DBDescriptor {

	private File dbFile;
	private ConcurrentMap<String, KeyPointer> dbPointers;
	private long endOfFile;

	public DBDescriptor (File dbFile) {
		super();
		this.dbFile = dbFile;
		dbPointers = new ConcurrentHashMap<String, KeyPointer>();
		endOfFile = 0L;
	}

	public File getDbFile () {
		return dbFile;
	}

	public void setDbFile (File dbFile) {
		this.dbFile = dbFile;
	}

	public ConcurrentMap<String, KeyPointer> getDbPointers () {
		return dbPointers;
	}

	public void setDbPointers (ConcurrentMap<String, KeyPointer> dbPointers) {
		this.dbPointers = dbPointers;
	}

	public long getEndOfFile () {
		return endOfFile;
	}

	public void setEndOfFile (long endOfFile) {
		this.endOfFile = endOfFile;
	}
	
	public long getKeyOffset (String key) {
		return dbPointers.get(key).getOffset();
	}
	
	public int getKeyLength (String key) {
		return dbPointers.get(key).getLength();
	}

}
