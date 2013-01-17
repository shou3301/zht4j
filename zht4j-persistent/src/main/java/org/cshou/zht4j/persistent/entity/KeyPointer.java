/**
 * 
 */
package org.cshou.zht4j.persistent.entity;

/**
 * @author cshou
 *
 */
public class KeyPointer {

	/**
	 * the offset where the object begins
	 */
	private long offset;
	
	/**
	 * the length of the object
	 */
	private int length;
	
	public KeyPointer () {
		super();
	}

	/**
	 * @param offset the offset where the object begins
	 * @param length the length of the object
	 */
	public KeyPointer (long offset, int length) {
		super();
		this.offset = offset;
		this.length = length;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
}
