/**
 *
 */
package fr.rsquatre.meteor.util;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class Converter {

	/**
	 *
	 * Takes an array of objects and return their respective classes in the same
	 * order
	 *
	 * @param objects
	 * @return the classes
	 */
	public static Class<?>[] convert(Object... objects) {

		Class<?>[] classes = new Class[objects.length];

		for (int i = 0; i < objects.length; i++) { classes[i] = objects[i].getClass(); }

		return classes;
	}

}
