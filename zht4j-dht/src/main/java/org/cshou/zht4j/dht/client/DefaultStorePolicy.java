/**
 * 
 */
package org.cshou.zht4j.dht.client;

import org.cshou.zht4j.dht.conf.ZhtConf;
import org.cshou.zht4j.dht.entity.StorePolicy;
import org.cshou.zht4j.dht.intl.StorePolicyFactory;

/**
 * @author cshou
 *
 */
public class DefaultStorePolicy implements StorePolicyFactory {

	protected ZhtConf conf;
	
	public DefaultStorePolicy () throws Exception {
		conf = ZhtConf.getZhtConf();
	}
	
	public StorePolicy getStorePolicy() {
		return new StorePolicy(conf.getNumOfReplicas());
	}

}
