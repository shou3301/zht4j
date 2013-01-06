/**
 * 
 */
package org.cshou.zht4j.dht.entity;

import java.io.Serializable;

import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 *
 */
public class DataWrapper implements Serializable {

	private static final long serialVersionUID = 2651085440195857218L;
	
	private String key;
	private Object object;
	
	public DataWrapper (String key, Object object) {
		this.key = key;
		this.object = object;
	}

	public String getKey () {
		return key;
	}

	public void setKey (String key) {
		this.key = key;
	}

	public Object getObject () {
		return object;
	}

	public void setObject (Object object) {
		this.object = object;
	}

}
