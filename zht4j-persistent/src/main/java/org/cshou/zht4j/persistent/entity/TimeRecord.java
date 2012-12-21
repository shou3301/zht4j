/**
 * 
 */
package org.cshou.zht4j.persistent.entity;

/**
 * @author cshou
 *
 */
public class TimeRecord implements Comparable {

	private long time;
	private String key;
	
	public TimeRecord (long time, String key) {
		this.time = time;
		this.key = key;
	}
	
	public int compareTo (Object o) {
		
		TimeRecord other = (TimeRecord) o;
		
		if (this.time > other.getTime())
			return 1;
		else if (this.time > other.getTime())
			return -1;
		
		return 0;
	}
	
	public long getTime () {
		return this.time;
	}
	
	public String getKey () {
		return this.key;
	}
	
}
