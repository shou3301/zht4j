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

import org.cshou.zht4j.dht.entity.DefaultContext;
import org.cshou.zht4j.dht.entity.StorePolicy;
import org.cshou.zht4j.dht.entity.ZhtEntity;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.InfoHandler;
import org.cshou.zht4j.dht.intl.Locator;
import org.cshou.zht4j.dht.intl.ObjectContext;
import org.cshou.zht4j.dht.intl.ZhtServer;
import org.cshou.zht4j.dht.membership.ZhtLocator;
import org.cshou.zht4j.dht.service.ZhtService;
import org.cshou.zht4j.dht.util.Naming;
import org.cshou.zht4j.persistent.entity.DBEntity;
import org.cshou.zht4j.persistent.impl.SimpleDB;
import org.cshou.zht4j.persistent.intl.PersistentStorage;

/**
 * @author cshou
 *
 */
public class ZhtServerBase implements ZhtServer {

	protected PersistentStorage storage = null;
	protected String serviceName = null;
	
	protected Locator locator = null;
	
	public ZhtServerBase (Locator locator) throws Exception {
		// this (new SimpleDB(30000L), InetAddress.getLocalHost().getHostAddress());
		this (new SimpleDB(), InetAddress.getLocalHost().getHostName(), locator);
	}
	
	public ZhtServerBase (PersistentStorage storage, Locator locator) throws Exception {
		this (storage, InetAddress.getLocalHost().getHostAddress(), locator);
	}
	
	public ZhtServerBase (String serviceName, Locator locator) throws Exception {
		this (new SimpleDB(), serviceName, locator);
	}
	
	public ZhtServerBase (PersistentStorage storage, String serviceName, Locator locator) throws Exception {
		
		this.storage = storage;
		this.serviceName = serviceName;
		this.locator = locator;
		
		System.out.println("========= debug: Service Name: " + serviceName);
		
		Registry svcReg = LocateRegistry.createRegistry(Naming.getRegPort());
		
		// InfoHandler infoHandler = new InfoHandlerBase ();
		
		DataHandler dataHandler = new DataHandlerBase (Naming.getDataPort(), this);
		svcReg.rebind(Naming.getDataService(this.serviceName), dataHandler);
	}
	
	public void run () {
		while (!ZhtService.shutdown.get()) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int put (String key, Object object, ObjectContext context) {
		
		if (context == null)
			context = new DefaultContext();
		
		ZhtEntity entity = new ZhtEntity(key, object, context);
		
		// single node test
		System.out.println("========= debug: Inserting object on " + this.serviceName);
		
		int res = storage.put(key, entity);
		
		return res;
	}

	public int put (String key, Object object, ObjectContext context, StorePolicy strategy) {
		
		int res = put (key, object, context);
		
		// TODO replicate object
		try {
			
			// String current = InetAddress.getLocalHost().getHostAddress();
			
			// single node test
			String current = this.serviceName;
			
			List<String> follower = locator.getFollowers(current);
			
			// single node test
			System.out.println("========= debug: Followers: " + follower);
			
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
