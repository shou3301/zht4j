/**
 * 
 */
package org.cshou.zht4j.dht.util;

/**
 * @author cshou
 *
 */
public class Naming {
	
	private static final String INFO_SVC_SUFFIX = "-info";
	private static final String DATA_SVC_SUFFIX = "-data";
	private static final int REG_PORT = 6668;
	private static final int INFO_PORT = 6666;
	private static final int DATA_PORT = 6667;
	
	public static String getDataService (String prefix) {
		return prefix + DATA_SVC_SUFFIX;
	}
	
	public static String getInfoService (String prefix) {
		return prefix + INFO_SVC_SUFFIX;
	}
	
	public static int getRegPort () {
		return REG_PORT;
	}
	
	public static int getDataPort () {
		return DATA_PORT;
	}
	
	public static int getInfoPort () {
		return INFO_PORT;
	}
	
}
