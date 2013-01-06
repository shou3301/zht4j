/**
 * 
 */
package org.cshou.zht4j.dht.conf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cshou
 *
 */
public class MemberLoader {

	private final static String memberFile = "conf/member";
	
	public MemberLoader () {
		super();
	}
	
	public List<String> loadMember () throws Exception {
		
		List<String> members = new ArrayList<String>();
		
		String line;
		InputStream is = new FileInputStream(memberFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		while ((line = reader.readLine()) != null) {
			members.add(line);
		}
		
		return members;
	}
}
