/**
 *
 */
package fr.rsquatre.meteor.exception;

/**
 * An exception thrown when an implemented method doesn't behave as expected:
 * invalid return type, missing data etc.
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class InvalidImplentationException extends RuntimeException {

	private static final long serialVersionUID = -5379428482499060957L;

	public InvalidImplentationException() {
		super();
	}

	public InvalidImplentationException(String message) {
		super(message);
	}

}
