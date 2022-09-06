/**
 *
 */
package fr.rsquatre.meteor.service.data.schema;

import fr.rsquatre.meteor.util.json.Json;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class AbstractSchema {

	public abstract int getId();

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass() == getClass() && ((AbstractSchema) obj).getId() == getId();
	}

	@Override
	public String toString() {
		return Json.get().toJson(this);
	}

}
