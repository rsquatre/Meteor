/**
 *
 */
package fr.rsquatre.meteor.service;

import org.bukkit.plugin.java.JavaPlugin;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.impl.IService;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class EntityManager implements IService {

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "Meteor:EntityManager";
	}

	@Override
	public Class<? extends JavaPlugin> getOwner() {
		return Meteor.class;
	}

}
