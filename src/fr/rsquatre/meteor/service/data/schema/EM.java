/**
 *
 */
package fr.rsquatre.meteor.service.data.schema;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class EM {

	@Retention(RUNTIME)
	@Target(FIELD)
	public static @interface Alias {

	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public @interface CollectionField {

		/**
		 *
		 * @return the type stored in this field's collection
		 */
		Class<?> type();
	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public @interface Field {

		String name();
	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public static @interface Id {

		boolean autoGenerated() default true;
	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public @interface MapField {

		/**
		 *
		 * @return the type of key stored in this field's map
		 */
		Class<?> keyType();

		/**
		 *
		 * @return the type of value stored in this field's map
		 */
		Class<?> valueType();
	}

	@Retention(RUNTIME)
	@Target(TYPE)
	public static @interface Schema {

		String name();
	}

}
