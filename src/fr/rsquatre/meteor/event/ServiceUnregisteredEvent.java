/**
 *
 */
package fr.rsquatre.meteor.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.util.Converters;

/**
 * Fired after an {@link IService} is unloaded and no more available from
 * {@link Meteor#getService(Class)}
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 *
 * @param service
 */
public class ServiceUnregisteredEvent extends Event {

	private HandlerList handlers = new HandlerList();

	private Class<? extends IService> type;
	private String name;
	private String owner;

	public ServiceUnregisteredEvent(IService service) {

		type = service.getClass();
		name = service.getName();
		owner = Converters.pullFirst(service.getOwner(), Bukkit.getPluginManager().getPlugins()).getName();
	}

	/**
	 * @return the type
	 */
	public @NotNull Class<? extends IService> getType() {
		return type;
	}

	/**
	 *
	 * @return the service's name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return the name of the {@link JavaPlugin} owner
	 */
	public @NotNull String getOwner() {
		return owner;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
