/**
 * 
 */
package org.cshou.zht4j.dht.entity;

import java.io.Serializable;

import org.cshou.zht4j.dht.intl.ObjectContext;
import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 *
 */
public class DataWrapper implements Serializable {

	private static final long serialVersionUID = 2651085440195857218L;
	
	private String key;
	private Object object;
	private ObjectContext context;
	
	public DataWrapper (String key, Object object) {
		this (key, object, null);
	}
	
	public DataWrapper (String key, Object object, ObjectContext context) {
		this.key = key;
		this.object = object;
		this.context = context;
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

	public ObjectContext getContext() {
		return context;
	}

	public void setContext(ObjectContext context) {
		this.context = context;
	}

}
