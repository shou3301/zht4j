/**
 * 
 */
package org.cshou.zht4j.dht.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StoreStrategy;
import org.cshou.zht4j.dht.entity.ZhtEntity;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.ObjectContext;
import org.cshou.zht4j.dht.intl.ZhtServer;

/**
 * @author cshou
 *
 */
public class DataHandlerBase extends UnicastRemoteObject implements DataHandler {

	private static final long serialVersionUID = -3490728284441110204L;
	
	private ZhtServer server;
	
	public DataHandlerBase(int port, ZhtServer server) throws RemoteException {
		super(port);
		this.server = server;
	}

	public int receiveObject(DataWrapper object, StoreStrategy strategy)
			throws RemoteException, NotBoundException {
		
		// TODO invoke server
		server.put(object.getKey(), object.getObject(), strategy);
		
		return 0;
	}

	public int receiveReplica(DataWrapper object) throws RemoteException,
			NotBoundException {
		
		// TODO invoke server
		
		return 0;
	}

	public Object getObject (String key) throws RemoteException,
			NotBoundException {
		
		ZhtEntity entity = server.get(key);
		
		if (entity == null)
			return null;
		
		return entity.getObject();
	}

	public int receiveObject (DataWrapper object, ObjectContext context,
			StoreStrategy strategy) throws RemoteException, NotBoundException {
		
		server.put(object.getKey(), object.getObject(), context, strategy);
		
		return 0;
	}

	public Object getObject (String key, ObjectContext context)
			throws RemoteException, NotBoundException {
		
		ZhtEntity entity = server.get(key);
		
		if (entity == null)
			return null;
		
		context = entity.getContext();
		
		return entity.getObject();

	}

}
