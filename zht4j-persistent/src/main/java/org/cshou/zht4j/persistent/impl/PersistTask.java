/**
 * TODO this class should be used as a singleton
 * this is because that before clean the memcache,
 * it should invoke a persist task, otherwise some data mighe be lost;
 * however, the persist task should not be executed at the same time.
 */
package org.cshou.zht4j.persistent.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cshou.zht4j.persistent.entity.DBDescriptor;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.entity.KeyPointer;

/**
 * @author cshou
 *
 */
public class PersistTask extends TimerTask {
	
	private static final long MAX_LENGTH = 16384L;
	
	private SimpleDB simpleDB;
	
	public PersistTask (SimpleDB simpleDB) {
		this.simpleDB = simpleDB;
	}
	
	@Override
	public synchronized void run() {
		
		Map<String, DBEntity> copyOfMem = new HashMap<String, DBEntity>(simpleDB.getMemCache());
		ConcurrentMap<String, KeyPointer> dbPointers = simpleDB.getDbDescriptor().getDbPointers();
		long eof = simpleDB.getDbDescriptor().getEndOfFile();
		File dbFile = simpleDB.getDbDescriptor().getDbFile();
		
		try {
			
			RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
			
			for (String key : copyOfMem.keySet()) {
				
				// convert object to bytes
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = null;
				out = new ObjectOutputStream(bos);   
				out.writeObject(copyOfMem.get(key));
				byte[] valBytes = bos.toByteArray();
				
				byte[] finalBytes = new byte[(int) MAX_LENGTH];
				
				// check val length
				if (valBytes.length < MAX_LENGTH) {
					byte[] padding = new byte[(int)(MAX_LENGTH - valBytes.length)];
					System.arraycopy(valBytes, 0, finalBytes, 0, valBytes.length);
					System.arraycopy(padding, 0, finalBytes, valBytes.length, padding.length);
				}
				
				if (dbPointers.containsKey(key)) {
					// TODO update file at certain offset
					file.seek(dbPointers.get(key).getOffset());
					file.write(finalBytes);
					KeyPointer kp = new KeyPointer(dbPointers.get(key).getOffset(), valBytes.length);
					dbPointers.put(key, kp);
					
					// for test
					// System.out.println("Key: " + key + " offset: " + dbPointers.get(key).getOffset());
				}
				else {
					// TODO append to the end of the file
					file.seek(eof);
					file.write(finalBytes);
					KeyPointer kp = new KeyPointer(eof, valBytes.length);
					dbPointers.put(key, kp);
					eof = eof + MAX_LENGTH;
					
					// for test
					// System.out.println("Key: " + key + " offset: " + eof);
				}
				
			}
			
			file.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
