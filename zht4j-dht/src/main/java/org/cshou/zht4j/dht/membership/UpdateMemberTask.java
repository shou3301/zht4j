/**
 * 
 */
package org.cshou.zht4j.dht.membership;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.cshou.zht4j.dht.intl.MembershipHandler;

/**
 * @author cshou
 *
 */
public class UpdateMemberTask extends Thread {

	protected static final int NUM_TRY = 2;
	
	protected MembershipHandler memberService = null;
	protected int index;
	protected String member;
	
	public UpdateMemberTask (MembershipHandler memberService, int index, String member) {
		this.memberService = memberService;
		this.index = index;
		this.member = member;
	}
	
	@Override
	public void run () {
		int i = 0;
		try {
			while (i < NUM_TRY && memberService.update(index, member) != 0) {
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
