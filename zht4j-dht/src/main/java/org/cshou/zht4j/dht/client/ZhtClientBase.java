/**
 * 
 */
package org.cshou.zht4j.dht.client;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.core.DefaultStorePolicy;
import org.cshou.zht4j.dht.core.MembershipManager;
import org.cshou.zht4j.dht.core.ZhtLocator;
import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StorePolicy;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.Locator;
import org.cshou.zht4j.dht.intl.StorePolicyFactory;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.cshou.zht4j.dht.util.Naming;
import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 * 
 */
public class ZhtClientBase implements ZhtClient {

	protected Locator locator = null;
	protected StorePolicyFactory policyFactory = null;

	public ZhtClientBase() {
		
		try {
			
			locator = ZhtLocator.getZhtLocator();
			policyFactory = new DefaultStorePolicy();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int put(String key, Object object) {

		int res = 1;
		
		try {
			System.out.println(locator.getCoordinator(key));
			/*DataHandler dataHandler = (DataHandler) getHandler ("192.168.2.4", "192.168.2.4" + DATA_SERVICE_NAME);

			res = dataHandler.receiveObject(new DataWrapper(key, object),
					new StorePolicy(0));*/

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public Object get(String key) {

		Object object = null;
		
		try {

			DataHandler dataHandler = (DataHandler) getHandler ("192.168.2.4", Naming.getDataService("192.168.2.4"));

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

			DataHandler dataHandler = (DataHandler) getHandler ("192.168.2.4", Naming.getDataService("192.168.2.4"));

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

	}

}
