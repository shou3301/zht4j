/**
 * 
 */
package org.cshou.zht4j.intl;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StoreStrategy;

/**
 * @author cshou
 *
 */
public interface DataHandler extends Remote {
	
	public int receiveObject (DataWrapper object, StoreStrategy strategy) throws RemoteException, NotBoundException;
	public int receiveReplica (DataWrapper object) throws RemoteException, NotBoundException;
	
}
