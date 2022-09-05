/**
 *
 */
package fr.rsquatre.meteor;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.exception.InvalidImplentationException;
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
				Logger.warn("Service " + type.getName()
						+ " was successfuly enabled but declares an invalid name. This could cause a NullPointerException, display issues or nothing. The correct format is plugin.yml#name:service_name");
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

		Logger.info("Take care of my kingdom while I'm gone, administrator, for I will be back...");
	}

	// STATICS

	public static Meteor getInstance() {
		return instance;
	}

}
