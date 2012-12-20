/**
 * 
 */
package org.cshou.zht4j.persistent.impl;

import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 *
 */
public class FakeEntity extends DBEntity {
	
	private String data;
	
	public FakeEntity(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return this.data;
	}
	
}
