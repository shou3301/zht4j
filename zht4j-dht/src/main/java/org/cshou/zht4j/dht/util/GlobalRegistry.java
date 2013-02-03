/**
 * 
 */
package org.cshou.zht4j.dht.util;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author cshou
 *
 */
public class GlobalRegistry {

	private static Registry registry = null;
	
	public static synchronized Registry getRegistry () throws Exception {
		
		if (registry == null) {
			registry = LocateRegistry.createRegistry(Naming.getRegPort());
		}
		
		return registry;
	}
	
}
