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
 * Fired after an {@link IService} is loaded and accessible from
 * {@link Meteor#getService(Class)}
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class ServiceRegisteredEvent extends Event {

	private HandlerList handlers = new HandlerList();

	private Class<? extends IService> type;
	private String owner;

	public ServiceRegisteredEvent(IService service) {

		type = service.getClass();
		owner = Converters.pullFirst(service.getOwner(), Bukkit.getPluginManager().getPlugins()).getName();
	}

	/**
	 * @return the type
	 */
	public @NotNull Class<? extends IService> getType() {
		return type;
	}

	public @NotNull IService getService() {

		return Meteor.getService(type);
	}

	public String getName() {

		return Meteor.getService(type).getName();
	}

	/**
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
