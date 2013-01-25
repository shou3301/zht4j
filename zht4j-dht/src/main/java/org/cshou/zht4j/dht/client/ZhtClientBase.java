/**
 * 
 */
package org.cshou.zht4j.dht.client;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StorePolicy;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.Locator;
import org.cshou.zht4j.dht.intl.StorePolicyFactory;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.cshou.zht4j.dht.membership.MembershipManager;
import org.cshou.zht4j.dht.membership.ZhtLocator;
import org.cshou.zht4j.dht.service.ZhtService;
import org.cshou.zht4j.dht.util.Naming;
import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 * 
 */
public class ZhtClientBase implements ZhtClient {

	protected Locator locator = null;
	protected StorePolicyFactory policyFactory = null;

	public ZhtClientBase(Locator locator) {
		
		try {
			
			this.locator = locator;
			policyFactory = new DefaultStorePolicy();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int put(String key, Object object) {

		int res = 1;
		
		try {
			
			String pos = locator.getCoordinator(key);
			
			// single node test
			System.out.println("========= debug: Insert location is " + pos);
			
			DataHandler dataHandler = (DataHandler) getHandler (pos, Naming.getDataService(pos));

			res = dataHandler.receiveObject(new DataWrapper(key, object),
					new StorePolicy(1));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public Object get(String key) {

		Object object = null;
		
		try {

			String pos = locator.getCoordinator(key);
			
			// single node test
			System.out.println("========= debug: Get location is " + pos);
			
			DataHandler dataHandler = (DataHandler) getHandler (pos, Naming.getDataService(pos));

			object = dataHandler.getObject(key);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return object;
	}

	public int remove(String key) {
		
		int res = 1;
		
		try {
			String pos = locator.getCoordinator(key);
			
			DataHandler dataHandler = (DataHandler) getHandler (pos, Naming.getDataService(pos));

			res = dataHandler.removeObject(key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private Remote getHandler (String hostaddress, String service) throws Exception {
		Registry svcReg = LocateRegistry.getRegistry(hostaddress, Naming.getRegPort());
		return svcReg.lookup(service);
	}

	public void run() {
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

}
