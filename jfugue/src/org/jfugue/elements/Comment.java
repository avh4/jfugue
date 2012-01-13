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
public class Comment implements JFugueElement {

	
	/**
	 * @param comment
	 */
	public Comment(String comment) {
		this.comment = comment;
	}

	protected String comment;
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/* (non-Javadoc)
	 * @see org.jfugue.elements.JFugueElement#getMusicString()
	 */
	public String getMusicString() {
		return getComment();
	}

	/* (non-Javadoc)
	 * @see org.jfugue.elements.JFugueElement#getVerifyString()
	 */
	public String getVerifyString() {
		return "Comment; comment=" + getComment();
	}

	/* (non-Javadoc)
	 * @see org.jfugue.elements.JFugueElement#acceptVisitor(org.jfugue.visitors.ElementVisitor)
	 */
	public void acceptVisitor(ElementVisitor visitor) {
		// TODO Auto-generated method stub

	}

}
