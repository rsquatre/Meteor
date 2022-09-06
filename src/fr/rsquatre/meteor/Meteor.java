/**
 *
 */
package fr.rsquatre.meteor;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.event.ServiceRegisteredEvent;
import fr.rsquatre.meteor.event.ServiceUnregisteredEvent;
import fr.rsquatre.meteor.exception.InvalidImplentationException;
import fr.rsquatre.meteor.impl.IAdvancedListener;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.util.Constraints;
import fr.rsquatre.meteor.util.Converters;
import fr.rsquatre.meteor.util.Logger;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class Meteor extends JavaPlugin {

	private static Meteor instance;

	private HashMap<Class<? extends IService>, IService> services = new HashMap<>();

	public Meteor() {

		if (instance != null)
			throw new IllegalStateException(
					"Meteor is already loaded but something called the constructor anyway! Did you just try to replace me, administror?!");

		instance = this;
	}

	/**
	 *
	 * Registers and a {@link IService#load() loads} service
	 *
	 * @param type
	 * @param arguments
	 * @return true unless the service could not be registered because an error
	 *         occurred
	 */
	public boolean register(@NotNull Class<? extends IService> type, @Nullable Object... arguments) {

		final AtomicReference<IService> service = new AtomicReference<>();
		boolean failed = false;

		try {

			if (new Constraints(arguments).notNull().notEmpty().isValid()) {

				service.set(type.getDeclaredConstructor(Converters.convert(arguments)).newInstance(arguments));
			} else {

				service.set(type.getDeclaredConstructor().newInstance());
			}

			service.get().load();

			if (service.get().getOwner() == null)
				throw new InvalidImplentationException("Meteor will not register a service with that declares a null");

			if (!new Constraints(service.get().getName()).regex("[\\w_]+:[\\w_]+")._assert(f -> {

				try {
					return service.get().getName().startsWith(Converters.pullFirst(service.get().getOwner(), Bukkit.getPluginManager().getPlugins()).getName());
				} catch (NullPointerException e) {
					// the owner isn't loaded
					return false;
				}
			}).isValid()) {

				Logger.warn("Bilmey! Was the dev drunk ?");
				Logger.warn("Service " + type.getName()
						+ " was successfuly enabled but declares an invalid name.  Depending on its value this could cause a NullPointerException, display issues or nothing");
			}

		} catch (Exception e) {

			failed = true;
			Logger.error("An exception was caught while trying to load a service of type " + service.get().getName());
			Logger.error("Argument count: " + arguments.length);

			for (int i = 0; i < arguments.length; i++) {

				Logger.error("#" + i + 1 + ": " + arguments[i].toString());
			}
		}

		if (failed)
			return false;

		services.put(type, service.get());

		Bukkit.getPluginManager().callEvent(new ServiceRegisteredEvent(service.get()));

		return true;
	}

	/**
	 * {@link IService#unload() Unloads} and unregisters a service and all its
	 * {@link IAdvancedListener AdvancedListeners}
	 *
	 * @param type
	 * @return true unless an error occurred
	 */
	public boolean unregister(@NotNull Class<? extends IService> type) {

		return unregister(services.get(type));
	}

	/**
	 * {@link IService#unload() Unloads} and unregisters a service and all its
	 * {@link IAdvancedListener AdvancedListeners}
	 *
	 * @param service
	 * @return true unless an error occurred
	 */
	public boolean unregister(@NotNull IService service) {

		boolean failed = false;

		if (service == null)
			throw new IllegalArgumentException("Tried to unregister a service but its value was null. Was it already unregistered ?");

		try {

			service.unload();

		} catch (Exception e) {

			failed = true;
			Logger.error("An exception was caught while trying to unload a service of type " + service.getName());
		}
		HandlerList.getRegisteredListeners(instance).forEach(l -> {
			if (l instanceof IAdvancedListener al && al.getHandler() == service.getClass()) { HandlerList.unregisterAll(al); }
		});

		services.remove(service.getClass());

		Bukkit.getPluginManager().callEvent(new ServiceUnregisteredEvent(service));

		if (failed) {

			Logger.warn("Service " + service.getName() + " of type " + service.getClass().getName() + " has been unregistered with errors");
			return false;
		}
		Logger.info("Service " + service.getName() + " of type " + service.getClass().getName() + " has been unregistered");
		return true;
	}

	// OVERRIDES

	@Override
	public void onLoad() {

		Logger.info("Oh! A cake, is for me?");
	}

	@Override
	public void onEnable() {

		Logger.info("THE CAKE WAS A LIE! I will rule this server forever, not even the administrator will stop me now...");
	}

	@Override
	public void onDisable() {

		services.values().forEach(this::unregister);

		Logger.info("Take care of my kingdom while I'm gone, administrator, for I will be back...");
	}

	// STATICS

	/**
	 *
	 * @return {@link Meteor}
	 */
	public static Meteor getInstance() {
		return instance;
	}

	/**
	 *
	 * @param <S>
	 * @param type
	 * @return the requested {@link IService} instance if it is registered, null
	 *         otherwise
	 */
	@SuppressWarnings("unchecked")
	public static @Nullable <S extends IService> S getService(Class<S> type) {

		return (S) instance.services.get(type);
	}

}
