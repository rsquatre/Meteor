/**
 *
 */
package fr.rsquatre.meteor;

import java.util.HashMap;

import javax.annotation.Nullable;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.util.Constraints;
import fr.rsquatre.meteor.util.Converter;
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

		IService service = null;

		try {

			if (new Constraints(arguments).notNull().notEmpty().isValid()) {

				service = type.getDeclaredConstructor(Converter.convert(arguments)).newInstance(arguments);
			} else {

				service = type.getDeclaredConstructor().newInstance();
			}

		} catch (Exception e) {

			Logger.error("An exception was caught while trying to load a service of type " + service.getName());
			Logger.error("Argument count: " + arguments.length);

			for (int i = 0; i < arguments.length; i++) {

				Logger.error("#" + i + 1 + ": " + arguments[i].toString());
			}
		}

		if (service == null)
			return false;

		services.put(type, service);

		return true;
	}

	// OVERRIDES

	@Override
	public void onLoad() {

		Logger.info("Oh! A cake, is for me?");
	}

	@Override
	public void onEnable() {

		Logger.info("THE CAKE WAS A LIE! I will rule this server forever.");
	}

	@Override
	public void onDisable() {

		Logger.info("Take care of my kingdom while I'm gone, for I will be back...");
	}

	// STATICS

	public static Meteor getInstance() {
		return instance;
	}

}
