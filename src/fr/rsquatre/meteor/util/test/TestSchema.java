/**
 *
 */
package fr.rsquatre.meteor.util.test;

import fr.rsquatre.meteor.service.data.schema.AbstractSchema;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class TestSchema extends AbstractSchema {

	private int id;

	@Override
	public int getId() {

		return id;
	}

	@Override
	public void setId(int id) {

		this.id = id;
	}

}
