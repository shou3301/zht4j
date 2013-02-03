/**
 * This class should be used as a singleton
 * this is because that before clean the memcache,
 * it should invoke a persist task, otherwise some data mighe be lost;
 * however, the persist task should not be executed at the same time.
 * 
 * The reason why I use a lazy way to remove objects:
 * A process:
 * (1) a persist task begins, a memory cache copy is made
 * (2) during this process, a remove operation is invoked
 * If we remove directly, say remove from mem cache first and then the pointer in db descriptor
 * (3) the key has not been used yet in persist task (now it's removed from db descriptor)
 * (4) later, when this key is used, the program cannot find it, then it will add the object to 
 * the end of the file, and create a new entry in db descriptor
 * Now, the removed file appears again in db descriptor and available in the disk (with an old value).
 */
package org.cshou.zht4j.persistent.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;

import org.cshou.zht4j.persistent.entity.DBDescriptor;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.entity.EmptyEntity;
import org.cshou.zht4j.persistent.entity.KeyPointer;

/**
 * @author cshou
 *
 */
public class PersistTask extends TimerTask {
	
	/**
	 * the default size for each object
	 */
	private static final long MAX_LENGTH = 16384L;
	
	private static PersistTask persistTask = null;
	
	private SimpleDB simpleDB;
	
	private PersistTask (SimpleDB simpleDB) {
		this.simpleDB = simpleDB;
	}
	
	// single node test
	/*public PersistTask (SimpleDB simpleDB) {
		this.simpleDB = simpleDB;
	}*/
	
	public synchronized static PersistTask getPersistTask (SimpleDB simpleDB) {
		if (persistTask == null) {
			persistTask = new PersistTask(simpleDB);
		}
		return persistTask;
	}
	
	@Override
	public synchronized void run() {
		
		Map<String, DBEntity> copyOfMem = new HashMap<String, DBEntity>(simpleDB.getMemCache());
		
		DBDescriptor dbDescriptor = simpleDB.getDbDescriptor();
		
		ConcurrentMap<String, KeyPointer> dbPointers = simpleDB.getDbDescriptor().getDbPointers();
		
		long eof = simpleDB.getDbDescriptor().getEndOfFile();
		
		File dbFile = simpleDB.getDbDescriptor().getDbFile();
		
		File logFile = simpleDB.getOpLog();
		
		RandomAccessFile dbfile = null;
		
		BufferedWriter output = null;
		
		try {
			
			dbfile = new RandomAccessFile(dbFile, "rw");
			
			for (String key : copyOfMem.keySet()) {
				
				if (!(copyOfMem.get(key) instanceof EmptyEntity)) {
					
					byte[] finalBytes = new byte[(int) MAX_LENGTH];
					
					// convert object to bytes
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutput out = null;
					out = new ObjectOutputStream(bos);   
					out.writeObject(copyOfMem.get(key));
					byte[] valBytes = bos.toByteArray();
					
					// check val length
					if (valBytes.length < MAX_LENGTH) {
						// byte[] padding = new byte[(int)(MAX_LENGTH - valBytes.length)];
						System.arraycopy(valBytes, 0, finalBytes, 0, valBytes.length);
						// System.arraycopy(padding, 0, finalBytes, valBytes.length, padding.length);
					}
				
				
				
					if (dbPointers.containsKey(key)) {
						// TODO update file at certain offset
						dbfile.seek(dbDescriptor.getKeyOffset(key));
						dbfile.write(finalBytes);
						KeyPointer kp = new KeyPointer(dbDescriptor.getKeyOffset(key), valBytes.length);
						dbPointers.put(key, kp);
						
						// for test
						// System.out.println("Key: " + key + " offset: " + dbPointers.get(key).getOffset());
					}
					else {
						// TODO append to the end of the file
						long nextSlot = dbDescriptor.getNextSlot();
						long temp = 0;
						
						if (nextSlot == -1) {
							nextSlot = eof;
							temp = MAX_LENGTH;
						}
						
						dbfile.seek(nextSlot);
						dbfile.write(finalBytes);
						KeyPointer kp = new KeyPointer(nextSlot, valBytes.length);
						dbPointers.put(key, kp);
						
						eof = eof + temp;
						
						// eof = eof + MAX_LENGTH;
						
						// for test
						// System.out.println("Key: " + key + " offset: " + eof);
					}
				
				}
				else {
					
					if (dbPointers.containsKey(key)) {
						long tempOffset = dbDescriptor.getKeyOffset(key);
						dbDescriptor.removePointer(key);
						dbDescriptor.pushSlot(tempOffset);
						simpleDB.getMemCache().remove(key);
					}
					
				}
				
			}
			
			dbDescriptor.setEndOfFile(eof);
			
			// persist log file
			output = new BufferedWriter(new FileWriter(logFile, true));
			
			for (Map.Entry<String, KeyPointer> e : dbPointers.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(e.getValue().getOffset() + "\t");
				sb.append(e.getKey() + "\t");
				sb.append(e.getValue().getLength());
				output.append(sb);
				output.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			
			try {
				if (dbfile != null) dbfile.close();
				if (output != null) output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
