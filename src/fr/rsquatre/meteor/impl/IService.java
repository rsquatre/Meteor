/**
 *
 */
package fr.rsquatre.meteor.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.rsquatre.meteor.Meteor;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public interface IService {

	/**
	 * Called when the service is enabled
	 */
	public void load();

	/**
	 * Called when the service is disabled
	 */
	public void unload();

	/**
	 *
	 * @return the service's name (should be user friendly)
	 */
	public String getName();

	/**
	 * Attaches a listener to Meteor<br>
	 * Your service should override this method to pass it's own {@link JavaPlugin}
	 * or use {@link #register(IAdvancedListener, JavaPlugin)} instead
	 *
	 * @param listener the listener
	 */
	public default void register(IAdvancedListener listener) {
		register(listener, Meteor.getInstance());
	}

	/**
	 * Attaches a listener to a plugin<br>
	 * Use this method unless you've overriden {@link #register(IAdvancedListener)}
	 * to pass your own {@link JavaPlugin}
	 *
	 * @param listener the listener
	 * @param plugin   the plugin
	 */
	public default void register(IAdvancedListener listener, JavaPlugin plugin) {

		if (listener.getHandler() != getClass())
			throw new IllegalArgumentException("Service " + getName() + " (" + getClass() + ") tried to register a listener " + listener.getClass().getName()
					+ " that belongs to another service (" + listener.getHandler().getName()
					+ "). This is not a misconfiguration, the dev was drunk or something.");

		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}

}
