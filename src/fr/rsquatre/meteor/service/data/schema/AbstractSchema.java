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

	public abstract void setId(int id);

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass() == getClass() && ((AbstractSchema) obj).getId() == getId();
	}

	@Override
	public String toString() {
		return getClass().getName() + " :  ".concat(Json.get().toJson(this));
	}

}
