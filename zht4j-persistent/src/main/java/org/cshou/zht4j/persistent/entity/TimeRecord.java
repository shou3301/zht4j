/**
 * 
 */
package org.cshou.zht4j.persistent.entity;

/**
 * @author cshou
 * 
 */
public class TimeRecord {

	private String key;
	private long timestamp;

	public TimeRecord(String key, long timestamp) {
		super();
		this.key = key;
		this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
