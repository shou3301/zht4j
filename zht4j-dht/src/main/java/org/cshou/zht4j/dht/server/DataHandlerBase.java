/**
 * 
 */
package org.cshou.zht4j.dht.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StoreStrategy;
import org.cshou.zht4j.intl.DataHandler;
import org.cshou.zht4j.intl.ZhtServer;

/**
 * @author cshou
 *
 */
public class DataHandlerBase extends UnicastRemoteObject implements DataHandler {

	private ZhtServer server;
	
	public DataHandlerBase(int port, ZhtServer server) throws RemoteException {
		super(port);
		this.server = server;
	}

	public int receiveObject(DataWrapper object, StoreStrategy strategy)
			throws RemoteException, NotBoundException {
		
		// TODO invoke server
		
		return 0;
	}

	public int receiveReplica(DataWrapper object) throws RemoteException,
			NotBoundException {
		
		// TODO invoke server
		
		return 0;
	}

}
