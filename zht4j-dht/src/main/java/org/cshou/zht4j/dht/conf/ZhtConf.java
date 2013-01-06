/**
 * 
 */
package org.cshou.zht4j.dht.conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cshou
 *
 */
public class ZhtConf {
	
	private static ZhtConf zhtConf = null;
	
	private static Map<String, String> params;
	
	private ZhtConf () throws Exception {
		params = new HashMap<String, String>();
		initParams();
		loadConf();
	}
	
	public static synchronized ZhtConf getZhtConf () throws Exception {
		if (zhtConf == null)
			zhtConf = new ZhtConf();
		return zhtConf;
	}
	
	private void loadConf () throws Exception {
		new ConfigLoader(params).run();
	}
	
	private void initParams () {
		params.put("zht4j.persist.frequence", "");
		params.put("zht4j.persist.storage", "");
		params.put("zht4j.persist.capacity", "");
		params.put("zht4j.dht.replica", "");
		params.put("zht4j.dht.size", "");
		params.put("zht4j.dht.capacity", "");
	}
	
	public String getValue (String key) {
		return params.get(key);
	}
	
	public long getPersistFreq () {
		String result = params.get("zht4j.persist.frequence");
		if (!result.equals(""))
			return Long.parseLong(result);
		return 0;
	}
	
	public int getZhtCapacity () {
		String result = params.get("zht4j.dht.capacity");
		if (!result.equals(""))
			return Integer.parseInt(result);
		return 0;
	}
	
	public String getStoragePath () {
		return params.get("zht4j.persist.storage");
	}
	
	public int getStorageCapacity () {
		String result = params.get("zht4j.persist.capacity");
		if (!result.equals(""))
			return Integer.parseInt(result);
		return 0;
	}
	
	public int getNumOfReplicas () {
		String result = params.get("zht4j.dht.replica");
		if (!result.equals(""))
			return Integer.parseInt(result);
		return -1;
	}
	
	public int getRingSize () {
		String result = params.get("zht4j.dht.size");
		if (!result.equals(""))
			return Integer.parseInt(result);
		return 0;
	}
	
	public Map<String, String> getParams () {
		return Collections.unmodifiableMap(params);
	}

}
