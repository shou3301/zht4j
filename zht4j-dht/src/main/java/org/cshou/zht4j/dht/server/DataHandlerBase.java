/**
 * 
 */
package org.cshou.zht4j.dht.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StorePolicy;
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

	public int receiveObject(DataWrapper object, StorePolicy strategy)
			throws RemoteException, NotBoundException {
		
		// TODO invoke server
		server.put(object.getKey(), object.getObject(), object.getContext(), strategy);
		
		return 0;
	}

	public int receiveReplica(DataWrapper object) throws RemoteException,
			NotBoundException {
		
		// TODO invoke server
		server.put(object.getKey(), object.getObject(), object.getContext());
		
		return 0;
	}

	public Object getObject (String key) throws RemoteException,
			NotBoundException {
		
		ZhtEntity entity = server.get(key);
		
		if (entity == null)
			return null;
		
		return entity.getObject();
	}
	
	public Object getObject (String key, ObjectContext context) throws RemoteException,
			NotBoundException {

		ZhtEntity entity = server.get(key);

		if (entity == null) {
			context = null;
			return null;
		}
		
		context = entity.getContext();

		return entity.getObject();
	}

	public ZhtEntity getReplica (String key) throws RemoteException, NotBoundException {
		
		ZhtEntity entity = server.getReplica(key);
		
		if (entity == null)
			return null;
		
		return entity;

	}

	public int removeObject (String key) throws RemoteException,
			NotBoundException {
		
		return server.remove(key);

	}

	public int migrateObject(String key, String destination)
			throws RemoteException, NotBoundException {

		// not implemented
		
		return 0;
	}

}
