/**
 * 
 */
package org.jfugue.elements;

import org.jfugue.visitors.ElementVisitor;

/**
 * @author joshua
 *
 */
@SuppressWarnings("serial")
public class DictAdd implements JFugueElement {
	
	protected String key = null;
	protected String value = null;
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	
	/**
	 * @param key
	 * @param value
	 */
	public DictAdd(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.jfugue.elements.JFugueElement#getMusicString()
	 */
	public String getMusicString() {
		return String.format("$%s=%s", getKey(), getValue());
	}

	/* (non-Javadoc)
	 * @see org.jfugue.elements.JFugueElement#getVerifyString()
	 */
	public String getVerifyString() {
		return String.format("DictAdd: key=%s, value=%s", getKey(), getValue());
	}

	/* (non-Javadoc)
	 * @see org.jfugue.elements.JFugueElement#acceptVisitor(org.jfugue.visitors.ElementVisitor)
	 */
	public void acceptVisitor(ElementVisitor visitor) {
		visitor.visit(this);
	}

}
