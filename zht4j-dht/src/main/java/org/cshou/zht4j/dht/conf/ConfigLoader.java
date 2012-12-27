/**
 * 
 */
package org.cshou.zht4j.dht.conf;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @author cshou
 *
 */
public class ConfigLoader {
	
	private static final String confPath = "conf/zht4j-conf.xml";
	
	private Map<String, String> params;
	
	public ConfigLoader (Map<String, String> params) {
		this.params = params;
	}
	
	public void run () throws Exception {
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(confPath);
        
        String xpathExpression = "//Configuration/Property";
        
        List<Node> properties = document.selectNodes(xpathExpression);
        
        for (Node node : properties) {
        	Node name = node.selectSingleNode("Name");
        	Node value = node.selectSingleNode("Value");
        	params.put(name.getText(), value.getText());
        }
        
	}

}
