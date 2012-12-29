/**
 * 
 */
package org.cshou.zht4j.dht.entity;

import org.cshou.zht4j.dht.intl.ObjectContext;

/**
 * @author cshou
 *
 */
public class DefaultContext extends ObjectContext {
	
	private long vectorClock;
	private String extraInfo;
	
	public DefaultContext () {
		this (0L, "");
	}
	
	public DefaultContext (long vectorClock, String extraInfo) {
		this.vectorClock = vectorClock;
		this.extraInfo = extraInfo;
	}

	public long getVectorClock() {
		return vectorClock;
	}

	public void setVectorClock(long vectorClock) {
		this.vectorClock = vectorClock;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	
}
