/**
 *
 */
package fr.rsquatre.meteor.service.core;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.service.core.command.MeteorCommand;
import fr.rsquatre.meteor.service.data.AbstractEntityManager;
import fr.rsquatre.meteor.service.data.SimpleEntityManager;
import fr.rsquatre.meteor.service.data.schema.CachedSchema;
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

	private HashMap<Class<? extends CachedSchema>, HashMap<Integer, CachedSchema>> entityCache = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <X extends CachedSchema> X getEntityFromMemory(Class<X> type, int id) {

		if (entityCache.containsKey(type))
			return (X) entityCache.get(type).get(id);
		return null;
	}

	public boolean isCachedInMemory(Class<? extends CachedSchema> type, int id) {

		if (entityCache.containsKey(type))
			return entityCache.get(type).containsKey(id);

		return false;
	}

	public void cacheInMemory(CachedSchema entity) {

		if (entityCache.containsKey(entity.getClass())) { entityCache.put(entity.getClass(), new HashMap<>()); }

		entityCache.get(entity.getClass()).put(entity.getId(), entity);
	}

	public static Configuration getConfig() {

		if (config == null)
			throw new IllegalStateException("Meteor's Core service is not enabled yet! You can listen to ServiceRegisteredEvent to know when it is available.");

		return config;
	}

	public void loadConfig() throws IOException {

		config = new Configuration();
		Logger.info("Done reading configuration");

	}

	// Overrides

	@Override
	public void load() throws Exception {

		loadConfig();

		Meteor.getInstance().register(new MeteorCommand());
	}

	@Override
	public void unload() {

	}

	@Override
	public @NotNull String getName() {
		return "Meteor:Core";
	}

	@Override
	public @NotNull Class<? extends JavaPlugin> getOwner() {
		return Meteor.class;
	}

	@Override
	public @NotNull boolean isSystem() {
		return true;
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

		public final Services SERVICES;

		private Configuration() throws IOException {

			FileConfiguration fc = Meteor.getInstance().getConfig();

			Meteor.getInstance().saveDefaultConfig();

			TRUST_OPERATORS = fc.getBoolean("trustOperators");
			INTEGRITY_SHUTDOWN = fc.getBoolean("integrityShutown");

			SERVICES = new Services(fc);

		}

		public class Services {

			public final Class<? extends AbstractEntityManager> MAIN_ENTITY_MANAGER;
			public final boolean DEV;

			private Services(FileConfiguration fc) {

				MAIN_ENTITY_MANAGER = switch (new NotNullOrDefault<>(fc.getString("services.entityManager"), "").value().toLowerCase()) {

				case "local" -> SimpleEntityManager.class;
				case "sql" -> SimpleEntityManager.class; // TODO FIXME change to SQL EM when it is available
				default -> throw new IllegalArgumentException("Unknown entity manager : " + fc.getString("services.entityManager"));

				};
				DEV = fc.getBoolean("services.dev");
			}

		}
	}

}
