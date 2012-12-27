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
	private DBEntity entity;
	
	public DataWrapper (String key, DBEntity entity) {
		this.key = key;
		this.entity = entity;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public DBEntity getEntity() {
		return entity;
	}

	public void setEntity(DBEntity entity) {
		this.entity = entity;
	}

}
