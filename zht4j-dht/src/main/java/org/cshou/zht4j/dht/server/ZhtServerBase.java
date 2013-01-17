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
import java.util.List;

import org.cshou.zht4j.dht.core.ZhtLocator;
import org.cshou.zht4j.dht.entity.DefaultContext;
import org.cshou.zht4j.dht.entity.StorePolicy;
import org.cshou.zht4j.dht.entity.ZhtEntity;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.InfoHandler;
import org.cshou.zht4j.dht.intl.Locator;
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
	
	protected Locator locator = null;
	
	public ZhtServerBase () throws Exception {
		this (new SimpleDB(30000L), InetAddress.getLocalHost().getHostAddress());
		// this (new SimpleDB(), InetAddress.getLocalHost().getHostName());
	}
	
	public ZhtServerBase (PersistentStorage storage) throws Exception {
		this (storage, InetAddress.getLocalHost().getHostAddress());
	}
	
	public ZhtServerBase (String serviceName) throws Exception {
		this (new SimpleDB(), serviceName);
	}
	
	public ZhtServerBase (PersistentStorage storage, String serviceName) throws Exception {
		
		this.storage = storage;
		this.serviceName = serviceName;
		this.locator = ZhtLocator.getZhtLocator();
		
		System.out.println(this.serviceName);
		
		Registry svcReg = LocateRegistry.createRegistry(REG_PORT);
		
		// InfoHandler infoHandler = new InfoHandlerBase ();
		
		DataHandler dataHandler = new DataHandlerBase (DATA_PORT, this);
		svcReg.rebind(this.serviceName + DATA_SERVICE_NAME, dataHandler);
	}
	
	public void run () {

	}
	
	public int put (String key, Object object, ObjectContext context) {
		
		if (context == null)
			context = new DefaultContext();
		
		ZhtEntity entity = new ZhtEntity(key, object, context);
		int res = storage.put(key, entity);
		
		return res;
	}

	public int put (String key, Object object, ObjectContext context, StorePolicy strategy) {
		
		int res = put (key, object, context);
		
		// TODO replicate object
		try {
			
			String current = InetAddress.getLocalHost().getHostAddress();
			
			List<String> follower = locator.getFollowers(current);
			
			int reps = strategy.getNumOfReplica();
			
			for (int i = 0; i < follower.size() && i <= reps; i++) {
				
				// TODO invoke a transfer task
				new ReplicateTask(follower.get(i), key, object, context).run();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}

	public ZhtEntity get (String key) {
		
		ZhtEntity entity = (ZhtEntity) storage.get(key);
		
		return entity;
	}

	public int remove (String key) {
		
		int res = storage.remove(key);
		
		return res;
	}

}
