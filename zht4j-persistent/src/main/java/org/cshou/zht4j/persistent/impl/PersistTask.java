/**
 * 
 */
package org.cshou.zht4j.persistent.impl;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cshou.zht4j.persistent.entity.KeyPointer;

/**
 * @author cshou
 *
 */
public class PersistTask extends TimerTask {
	
	private SimpleDB simpleDB;
	
	public PersistTask (SimpleDB simpleDB){
		this.simpleDB = simpleDB;
	}
	
	@Override
	public void run() {
		// TODO persist memory into disk
	}

}
