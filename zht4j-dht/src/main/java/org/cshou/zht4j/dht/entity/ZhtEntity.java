/**
 * 
 */
package org.cshou.zht4j.dht.entity;

import org.cshou.zht4j.dht.intl.ObjectContext;
import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 *
 */
public class ZhtEntity extends DBEntity {

	private static final long serialVersionUID = -328948444839357214L;

	protected ObjectContext context;
	private String key;
	protected Object object;
	
	public ZhtEntity(String key, Object object, ObjectContext context) {
		super();
		this.context = context;
		this.object = object;
		this.key = key;
	}

	public ObjectContext getContext() {
		return context;
	}

	public void setContext(ObjectContext context) {
		this.context = context;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
