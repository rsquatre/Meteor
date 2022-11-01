/**
 *
 */
package fr.rsquatre.meteor.service.dev;

import java.util.Random;

import fr.rsquatre.meteor.service.data.schema.AbstractSchema;
import fr.rsquatre.meteor.service.data.schema.EM.Field;
import fr.rsquatre.meteor.service.data.schema.EM.Id;
import fr.rsquatre.meteor.service.data.schema.EM.Schema;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
@Schema(name = "tests")
public class TestSchema extends AbstractSchema {

	@Id
	@Field(name = "id")
	private int id;
	@Field(name = "rand")
	private int rand = new Random().nextInt(10);

	@Override
	public int getId() {

		return id;
	}

	@Override
	public void setId(int id) {

		this.id = id;
	}

	public int getRand() {
		return rand;
	}

}
