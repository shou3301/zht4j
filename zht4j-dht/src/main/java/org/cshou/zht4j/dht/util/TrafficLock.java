/**
 * 
 */
package org.cshou.zht4j.dht.util;

/**
 * @author cshou
 *
 */
public class TrafficLock {

	private int count1;
	private int count2;
	
	public TrafficLock () {
		this.count1 = 0;
		this.count2 = 0;
	}
	
	public void lockFirst () throws InterruptedException {
		synchronized (this) {
			while (this.count2 > 0) {
				this.wait();
			}
			this.count1++;
		}
	}
	
	public void lockSecond () throws InterruptedException {
		synchronized (this) {
			while (this.count1 > 0) {
				this.wait();
			}
			this.count2++;
		}
	}
	
	public void lockBoth () throws InterruptedException {
		synchronized (this) {
			while (this.count1 > 0 || this.count2 > 0) {
				this.wait();
			}
			this.count1++;
			this.count2++;
		}
	}
	
	public void unlockFirst () throws InterruptedException {
		synchronized (this) {
			this.count1--;
			this.notifyAll();
		}
	}
	
	public void unlockSecond () throws InterruptedException {
		synchronized (this) {
			this.count2--;
			this.notifyAll();
		}
	}
	
	public void unlockBoth () throws InterruptedException {
		synchronized (this) {
			this.count1--;
			this.count2--;
			this.notifyAll();
		}
	}
	
}
