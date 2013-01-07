/**
 * 
 */
package org.cshou.zht4j.dht.entity;

import java.io.Serializable;

/**
 * @author cshou
 *
 */
public class StorePolicy implements Serializable {

	private static final long serialVersionUID = -5540786750659786206L;
	
	private int numOfReplica;

	public StorePolicy(int numOfReplica) {
		super();
		this.numOfReplica = numOfReplica;
	}

	public int getNumOfReplica() {
		return numOfReplica;
	}

	public void setNumOfReplica(int numOfReplica) {
		this.numOfReplica = numOfReplica;
	}

}
