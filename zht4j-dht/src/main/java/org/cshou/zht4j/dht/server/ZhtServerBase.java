/**
 * TODO
 * 1. should provide another put/get interface
 *    that user can customize Context
 */
package org.cshou.zht4j.dht.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.cshou.zht4j.dht.entity.DefaultContext;
import org.cshou.zht4j.dht.entity.StoreStrategy;
import org.cshou.zht4j.dht.entity.ZhtEntity;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.InfoHandler;
import org.cshou.zht4j.dht.intl.ObjectContext;
import org.cshou.zht4j.dht.intl.ZhtServer;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.impl.SimpleDB;
import org.cshou.zht4j.persistent.intl.PersistentStorage;

/**
 * @author cshou
 *
 */
public class ZhtServerBase implements ZhtServer {
	
	private static final int INFO_PORT = 6666;
	private static final int DATA_PORT = 6667;
	private static final int REG_PORT = 6668;
	private static final String INFO_SERVICE_NAME = "-info";
	private static final String DATA_SERVICE_NAME = "-data";

	protected PersistentStorage storage = null;
	protected String serviceName = null;
	
	public ZhtServerBase () throws Exception {
		this (new SimpleDB(30000L), InetAddress.getLocalHost().getHostName());
		// this (new SimpleDB(), InetAddress.getLocalHost().getHostName());
	}
	
	public ZhtServerBase (PersistentStorage storage) throws Exception {
		this (storage, InetAddress.getLocalHost().getHostName());
	}
	
	public ZhtServerBase (String serviceName) throws Exception {
		this (new SimpleDB(), serviceName);
	}
	
	public ZhtServerBase (PersistentStorage storage, String serviceName) throws RemoteException {
		
		this.storage = storage;
		this.serviceName = serviceName;
		
		System.out.println(this.serviceName);
		
		Registry svcReg = LocateRegistry.createRegistry(REG_PORT);
		
		// InfoHandler infoHandler = new InfoHandlerBase ();
		
		DataHandler dataHandler = new DataHandlerBase (DATA_PORT, this);
		svcReg.rebind(this.serviceName + DATA_SERVICE_NAME, dataHandler);
	}
	
	public void run () {

	}

	public int put (String key, Object object, StoreStrategy strategy) {
		
		return put (key, object, new DefaultContext(), strategy);
	}

	public int put (String key, Object object, ObjectContext context, StoreStrategy strategy) {
		
		ZhtEntity entity = new ZhtEntity(key, object, context);
		int res = storage.put(key, entity);
		
		// TODO replicate object
		
		return res;
	}

	public ZhtEntity get (String key) {
		
		ZhtEntity entity = (ZhtEntity) storage.get(key);
		
		return entity;
	}

	public int remove (String key) {
		return 0;
	}

}
