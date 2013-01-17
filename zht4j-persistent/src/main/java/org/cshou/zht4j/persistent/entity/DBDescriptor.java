/**
 * 
 */
package org.cshou.zht4j.persistent.entity;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author cshou
 * the class to manage the db file
 */
public class DBDescriptor {

	private File dbFile;
	
	/**
	 * <key, pointer to the offset in the file>
	 */
	private ConcurrentMap<String, KeyPointer> dbPointers;
	
	/**
	 * some object might be removed,
	 * then the space should be made good used of
	 */
	private Queue<Long> availableTable;
	
	private long endOfFile;

	public DBDescriptor (File dbFile) {
		super();
		this.dbFile = dbFile;
		dbPointers = new ConcurrentHashMap<String, KeyPointer>();
		availableTable = new LinkedList<Long>();
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

	public long getEndOfFile () {
		return endOfFile;
	}

	public void setEndOfFile (long endOfFile) {
		this.endOfFile = endOfFile;
	}
	
	public void removePointer (String key) {
		this.dbPointers.remove(key);
	}
	
	public long getKeyOffset (String key) {
		return dbPointers.get(key).getOffset();
	}
	
	public int getKeyLength (String key) {
		return dbPointers.get(key).getLength();
	}
	
	public void pushSlot (long offset) {
		availableTable.add(offset);
	}
	
	public long getNextSlot () {
		if (!availableTable.isEmpty()) {
			return availableTable.poll();
		}
		else
			return -1;
	}

}
