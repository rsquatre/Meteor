/**
 *
 */
package fr.rsquatre.meteor.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
	public void load() throws Exception;

	/**
	 * Called when the service is disabled
	 */
	public void unload();

	/**
	 * Returns the service's name in the following format:
	 * plugin.yml#name:service_name<br>
	 * <br>
	 * Valid examples:<br>
	 * <br>
	 * Meteor:EntityManager<br>
	 * my_plugin:my_service
	 *
	 * @return the name
	 */
	public @NotNull String getName();

	/**
	 *
	 * @return the service unqualified name (without the plugin part) or its
	 *         qualified name if the latter doesn't match the required format
	 */

	public default @NotNull String getUnqualifiedName() {

		String[] s = getName().split(":");

		return s.length != 2 || s[1].isBlank() ? getName() : s[1];
	}

	/**
	 * Returns the owner plugin's main class
	 *
	 * @return the owner
	 */

	public @NotNull Class<? extends JavaPlugin> getOwner();

	/**
	 * Attaches a listener to Meteor<br>
	 * Your service should override this method to pass it's own {@link JavaPlugin}
	 * or use {@link #register(IAdvancedListener, JavaPlugin)} instead
	 *
	 * @param listener the listener
	 */
	public default void register(@NotNull IAdvancedListener listener) {

		if (listener == null)
			throw new IllegalArgumentException("The listener cannot be null");

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
	public default void register(@NotNull IAdvancedListener listener, @NotNull JavaPlugin plugin) {

		if (listener == null)
			throw new IllegalArgumentException("The listener cannot be null");
		if (plugin == null)
			throw new IllegalArgumentException("The plugin cannot be null");
		if (!plugin.isEnabled())
			throw new IllegalArgumentException("Tried to attach a listner to a disabled plugin " + plugin.getName());

		if (listener.getHandler() != getClass())
			throw new IllegalArgumentException("Service " + getName() + " (" + getClass() + ") tried to register a listener " + listener.getClass().getName()
					+ " that belongs to another service (" + listener.getHandler().getName()
					+ "). This is not a misconfiguration, the dev was drunk or something.");

		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}

}
