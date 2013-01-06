/**
 * 
 */
package org.cshou.zht4j.dht.client;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.entity.DataWrapper;
import org.cshou.zht4j.dht.entity.StoreStrategy;
import org.cshou.zht4j.dht.intl.DataHandler;
import org.cshou.zht4j.dht.intl.ZhtClient;
import org.cshou.zht4j.persistent.entity.DBEntity;

/**
 * @author cshou
 * 
 */
public class ZhtClientBase implements ZhtClient {

	private static final int REG_PORT = 6668;
	private static final String INFO_SERVICE_NAME = "-info";
	private static final String DATA_SERVICE_NAME = "-data";

	protected ZhtConf conf = null;
	protected MembershipManager memberManager = null;

	public ZhtClientBase() {
		
		try {
			conf = ZhtConf.getZhtConf();
			memberManager = MembershipManager.getMemberManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int put(String key, Object object) {

		int res = 1;
		
		try {
			
			DataHandler dataHandler = (DataHandler) getHandler ("192.168.2.4", "192.168.2.4" + DATA_SERVICE_NAME);

			res = dataHandler.receiveObject(new DataWrapper(key, object),
					new StoreStrategy(0));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public Object get(String key) {

		Object object = null;
		
		try {

			DataHandler dataHandler = (DataHandler) getHandler ("192.168.2.4", "192.168.2.4" + DATA_SERVICE_NAME);

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

			DataHandler dataHandler = (DataHandler) getHandler ("192.168.2.4", "192.168.2.4" + DATA_SERVICE_NAME);

			res = dataHandler.removeObject(key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private Remote getHandler (String hostaddress, String service) throws Exception {
		Registry svcReg = LocateRegistry.getRegistry(hostaddress, REG_PORT);
		return svcReg.lookup(service);
	}

	public void run() {

	}

}
