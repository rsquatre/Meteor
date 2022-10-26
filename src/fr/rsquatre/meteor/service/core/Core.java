/**
 *
 */
package fr.rsquatre.meteor.service.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.service.core.command.MeteorCommand;
import fr.rsquatre.meteor.service.data.AbstractEntityManager;
import fr.rsquatre.meteor.service.data.SimpleEntityManager;
import fr.rsquatre.meteor.util.Constraints.NotNullOrDefault;
import fr.rsquatre.meteor.util.Logger;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class Core implements IService {

	private static Configuration config;

	public static Configuration getConfig() {

		if (config == null)
			throw new IllegalStateException("Meteor's Core service is not enabled yet! You can listen to ServiceRegisteredEvent to know when it is available.");

		return config;
	}

	public void loadConfig() {

		config = new Configuration();
		Logger.info("Done reading configuration");

	}

	// Overrides

	@Override
	public void load() throws Exception {

		Meteor.getInstance().saveDefaultConfig();

		loadConfig();

		Meteor.getInstance().register(new MeteorCommand());
	}

	@Override
	public void unload() {

	}

	@Override
	public @NotNull String getName() {
		return "Meteor:core";
	}

	@Override
	public @NotNull Class<? extends JavaPlugin> getOwner() {
		return Meteor.class;
	}

	/**
	 *
	 * EVERYTHING IN THIS CLASS IS READ ONLY.<br>
	 * If you need to change some values, use Bukkit's API and call
	 * {@link #loadConfig()} to generate a new {@link Configuration}
	 *
	 * @author <a href="https://github.com/rsquatre">rsquatre</a>
	 *
	 *         © All rights reserved, unless specified otherwise
	 *
	 */
	public class Configuration {

		public final boolean TRUST_OPERATORS;
		public final boolean INTEGRITY_SHUTDOWN;

		public final Class<? extends AbstractEntityManager> MAIN_ENTITY_MANAGER;

		public final Services SERVICES;

		private Configuration() {

			// TODO FIXME review the whole thing cause it's bad and half broken

			// Booleans

			Meteor.getInstance().getConfig().addDefault("truestedOperators", true);
			Meteor.getInstance().getConfig().addDefault("integrityShutown", true);

			TRUST_OPERATORS = Meteor.getInstance().getConfig().getBoolean("trustOperators");
			INTEGRITY_SHUTDOWN = Meteor.getInstance().getConfig().getBoolean("integrityShutown");

			// Strings

			Meteor.getInstance().getConfig().addDefault("em", "local");

			MAIN_ENTITY_MANAGER = switch (new NotNullOrDefault<>(Meteor.getInstance().getConfig().getString("em"), "").value().toLowerCase()) {
			case "local" -> SimpleEntityManager.class;
			case "sql" -> SimpleEntityManager.class; // TODO FIXME change to SQL EM when it is available
			default -> throw new IllegalArgumentException("Unknown entity manager : " + Meteor.getInstance().getConfig().getString("em"));

			};

			SERVICES = new Services();

			Meteor.getInstance().saveConfig();
		}

		public class Services {

			public final boolean DEV;

			private Services() {

				Meteor.getInstance().getConfig().addDefault("services.dev", false);

				DEV = Meteor.getInstance().getConfig().getBoolean("services.dev");
			}

		}
	}

}
