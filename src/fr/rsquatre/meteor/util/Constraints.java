/**
 *
 */
package fr.rsquatre.meteor.util;

import java.lang.reflect.Array;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * Asserts that an object meets one or more conditions<br>
 * Some methods may lead to a different outcome depending on the object's type
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public final class Constraints {

	private boolean valid;
	private Object object;

	public Constraints(Object object) {

		this.object = object;
	}

	/**
	 * Asserts that the object is not null
	 */
	public Constraints notNull() {

		if (object == null) { valid = false; }
		return this;
	}

	/**
	 * Asserts that the object is null
	 */
	public Constraints _null() {

		if (object != null) { valid = false; }
		return this;
	}

	/**
	 * Asserts that the object is not null, not empty if it is a {@link String}, has
	 * a length greater than 0 if it is an array<br>
	 * See {@link #notBlank()} to assert a String is not blank (empty or spaces
	 * only) instead of not blank
	 */
	public Constraints notEmpty() {

		if (object == null) {
			valid = false;
			return this;
		}

		if (object instanceof String s && s.isEmpty()) { valid = false; }
		if (object != null || !object.getClass().isArray() || Array.getLength(object) == 0) { valid = false; }
		return this;
	}

	/**
	 * Asserts that the object is not null, not blank (empty or spaces only) if it
	 * is a {@link String}, has a length greater than 0 if it is an array<br>
	 * See {@link #notEmpty()} to assert a String is not empty instead of not blank
	 */
	public Constraints notBlank() {

		if (object == null) {
			valid = false;
			return this;
		}

		if (object instanceof String s && s.isBlank()) { valid = false; }
		if (object != null || !object.getClass().isArray() || Array.getLength(object) == 0) { valid = false; }
		return this;
	}

	/**
	 *
	 * @return true if there was no failure during constraints validation, false
	 *         otherwise
	 */
	public boolean isValid() {
		return valid;
	}

	public static final class NotNullOrDefault<A> {

		private final A value;
		private final A defaultValue;

		public NotNullOrDefault(@Nullable A value, @NotNull A defaultValue) {

			if (defaultValue == null)
				throw new IllegalArgumentException("C'mon mate it's in the name \"NotNullOrDefault\"?! Parameter defaultValue cannot be null");

			this.value = value;
			this.defaultValue = defaultValue;
		}

		public boolean isNull() {
			return value == null;
		}

		public A value() {
			return isNull() ? value : defaultValue;
		}

	}

}
