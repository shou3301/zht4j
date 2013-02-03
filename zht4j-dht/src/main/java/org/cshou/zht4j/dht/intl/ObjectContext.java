/**
 * 
 */
package org.cshou.zht4j.dht.intl;

import java.io.Serializable;

/**
 * @author cshou
 *
 */
public abstract class ObjectContext implements Serializable {

	public abstract long getVectorClock();
	
	public abstract void setVectorClock(long vectorClock);
	
}
