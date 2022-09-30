/**
 *
 */
package fr.rsquatre.meteor.service.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonSyntaxException;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.service.data.schema.AbstractSchema;
import fr.rsquatre.meteor.service.data.schema.EM;
import fr.rsquatre.meteor.util.Constraints;
import fr.rsquatre.meteor.util.Logger;
import fr.rsquatre.meteor.util.json.Json;

/**
 * Json file based entity persistence manager<br>
 * Some features may not be available and will throw an
 * {@link UnsupportedOperationException}
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public final class SimpleEntityManager extends AbstractEntityManager {

	private File files;

	private HashMap<Class<? extends AbstractSchema>, ArrayList<AbstractSchema>> persist = new HashMap<>();
	private HashMap<Class<? extends AbstractSchema>, ArrayList<AbstractSchema>> delete = new HashMap<>();

	@Override
	public void load() throws IOException {

		files = new File(Meteor.getInstance().getDataFolder(), "entities");

		if ((!files.exists() || !files.isDirectory()) && !files.mkdirs())
			throw new IOException("Directory " + files.getPath() + " cannot be created");

	}

	@Override
	public void unload() {

		flush();
	}

	@Override
	public @NotNull String getName() {
		return "Meteor:LocalEntityManager";
	}

	@Override
	public @NotNull Class<? extends JavaPlugin> getOwner() {
		return Meteor.class;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findAll(@NotNull Class<E> type) {

		HashSet<E> entities = new HashSet<>();

		File files = getDataFolder(type);

		File[] entityFiles = files.listFiles(name -> !"index".equalsIgnoreCase(name.getName()));

		for (File file : entityFiles) {

			try {
				entities.add(Json.get().fromJson(Files.readString(Path.of(file.getAbsolutePath())), type));
			} catch (JsonSyntaxException e) {

				Logger.warn("Invalid file " + file.getName() + " in the entity folder " + files.getName() + ". Is it an corrupted entity or a stowaway ?");
				e.printStackTrace();
			} catch (IOException e) {

				Logger.error("An error occurred while trying to read file " + file.getName() + " in entity folder " + files.getName());
				e.printStackTrace();
			}
		}

		return entities;
	}

	@Override
	public <E extends AbstractSchema> @Nullable E find(@NotNull Class<E> type, @NotNull int id) {

		File files = getDataFolder(type);

		File file = new File(files, id + ".json");

		if (file.exists()) {

			try {
				return Json.get().fromJson(Files.readString(Path.of(file.getAbsolutePath())), type);
			} catch (JsonSyntaxException e) {

				Logger.warn("Invalid file " + file.getName() + " in the entity folder " + files.getName() + ". Is it corrupred ?");
				e.printStackTrace();
			} catch (IOException e) {

				Logger.error("An error occurred while trying to read file " + file.getName() + " in entity folder " + files.getName());
				e.printStackTrace();
			}

		}

		return null;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> find(@NotNull Class<E> type, @NotNull int... ids) {

		return find(type, Arrays.stream(ids).boxed().toList());
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> find(@NotNull Class<E> type, @NotNull Collection<Integer> ids) {

		HashSet<E> entities = new HashSet<>();

		File files = getDataFolder(type);

		StringBuilder errors = new StringBuilder("Exptected to find " + ids.size() + " entities but an issue occurred for the following: ");
		boolean errored = false;

		for (int id : ids) {

			File file = new File(files, id + ".json");

			if (!file.exists()) {
				errors.append(id + ".json (Not Found)" + (errored ? ", " : ""));
				errored = true;
			}

			try {

				entities.add(Json.get().fromJson(Files.readString(Path.of(file.getAbsolutePath())), type));

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				errors.append(id + ".json (Json Syntax Exception)" + (errored ? ", " : ""));
				errored = true;
			} catch (IOException e) {
				e.printStackTrace();
				errors.append(id + ".json (IO Exception)" + (errored ? ", " : ""));
				errored = true;
			}

		}

		if (errored) { errors.append("Found only " + entities.size()); }

		return entities;
	}

	@Override
	public <E extends AbstractSchema> @Nullable E findOneBy(@NotNull Class<E> type, @NotNull String field, @Nullable Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends AbstractSchema> @Nullable E findOneBy(@NotNull Class<E> type, @NotNull String[] fields, @NotNull Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @Nullable Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @NotNull Object... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @NotNull Collection<Object> values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String[] fields, @NotNull Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AbstractEntityManager persist(@NotNull AbstractSchema entity) {

		if (!persist.containsKey(entity.getClass())) {

			persist.put(entity.getClass(), new ArrayList<>());
		}

		persist.get(entity.getClass()).add(entity);

		return this;

	}

	@Override
	public AbstractEntityManager persist(@NotNull AbstractSchema... entities) {

		if (entities == null || entities.length == 0)
			return this;

		if (!persist.containsKey(entities[0].getClass())) {

			persist.put(entities[0].getClass(), new ArrayList<>());
		}

		Collections.addAll(persist.get(entities.getClass()), entities);

		return this;
	}

	@Override
	public AbstractEntityManager persist(@NotNull Collection<AbstractSchema> entities) {

		if (entities == null || entities.size() == 0)
			return this;

		Class<? extends AbstractSchema> type = entities.iterator().next().getClass();

		if (!persist.containsKey(type)) {

			persist.put(type, new ArrayList<>());
		}

		persist.get(type).addAll(entities);

		return this;
	}

	@Override
	public AbstractEntityManager remove(@NotNull AbstractSchema entity) {

		if (persist.containsKey(entity.getClass())) { persist.get(entity.getClass()).remove(entity); }

		return this;
	}

	@Override
	public AbstractEntityManager remove(@NotNull AbstractSchema... entities) {

		if (entities == null || entities.length == 0)
			return this;

		if (persist.containsKey(entities[0].getClass())) {

			for (AbstractSchema entity : entities) { persist.get(entities[0].getClass()).remove(entity); }
		}

		return this;
	}

	@Override
	public AbstractEntityManager remove(@NotNull Collection<AbstractSchema> entities) {

		if (entities == null || entities.size() == 0)
			return this;

		Class<? extends AbstractSchema> type = entities.iterator().next().getClass();

		if (persist.containsKey(type)) {

			entities.removeAll(entities);
		}

		return this;
	}

	@Override
	public AbstractEntityManager delete(@NotNull AbstractSchema entity) {

		if (!delete.containsKey(entity.getClass())) { delete.put(entity.getClass(), new ArrayList<>()); }

		delete.get(entity.getClass()).add(entity);

		return this;
	}

	@Override
	public AbstractEntityManager delete(@NotNull AbstractSchema... entities) {

		if (entities == null || entities.length == 0)
			return this;

		for (AbstractSchema entity : entities) {

			// no custom loop because we need to check every time if the ArrayList is there
			// in case there is more than one type of entity
			delete(entity);
		}

		return this;
	}

	@Override
	public AbstractEntityManager delete(@NotNull Collection<AbstractSchema> entities) {

		if (entities == null || entities.size() == 0) {}

		for (AbstractSchema entity : entities) {

			// no custom loop because we need to check every time if the ArrayList is there
			// in case there is more than one type of entity
			delete(entity);
		}

		return this;
	}

	/**
	 * <b><u>Asynchronously</u></b> persists and deletes entities on the next tick
	 */
	@Override
	synchronized public AbstractEntityManager flush() {

		Bukkit.getScheduler().runTaskAsynchronously(Meteor.getInstance(), (Runnable) () -> {

			for (Class<? extends AbstractSchema> type : persist.keySet()) {

				for (AbstractSchema entity : persist.get(type)) {

					if (entity == null) { continue; }

					// CREATE & UPDATE

					try {

						File file = getEntityFile(type, entity.getId() > 0 ? entity.getId() : injectId(entity));

						if (!file.exists() || !file.isFile()) { file.createNewFile(); }

						PrintWriter pw = new PrintWriter(file);
						pw.print(Json.get().toJson(entity));
						pw.flush();
						pw.close();

					} catch (NumberFormatException e) {

						e.printStackTrace();
						Logger.fatal("The index file for entities of type " + type.getName() + " is corrupted. Expected number but found something else");
					} catch (IOException e) {

						e.printStackTrace();
						Logger.fatal("An error occured while trying to read the index file for entities of type " + type.getName());
					}

				}
			}

			// DELETE

			for (Class<? extends AbstractSchema> type : delete.keySet()) {

				for (AbstractSchema entity : delete.get(type)) {

					if (entity == null || entity.getId() < 1) { continue; }

					File file = getEntityFile(entity);

					if (!file.exists() || !file.isFile()) { Logger.warn("Cannot delete file of entity " + entity + ". File not found"); }

					if (!file.delete()) { Logger.error("Cannot delete file of entity " + entity + ""); }
				}
			}

		});

		return this;

	}

	@Override
	public @NotNull String getSaveType() {
		return "JSON, Local file system";
	}

	@Override
	protected boolean isValid() {

		// check for IO errors ?

		return true;
	}

	private File getDataFolder(Class<? extends AbstractSchema> type) {

		if (type.isAnnotationPresent(EM.Schema.class) || new Constraints(type.getAnnotation(EM.Schema.class).name()).notBlank().isValid())
			throw new IllegalStateException(
					"Class " + type.getName() + " is an entity but failed to declare a valid " + EM.Schema.class.getName() + " annotation");

		return new File(files, type.getAnnotation(EM.Schema.class).name());
	}

	private File getEntityFile(AbstractSchema entity) {
		return getEntityFile(entity.getClass(), entity.getId());
	}

	private File getEntityFile(Class<? extends AbstractSchema> type, int id) {
		return new File(getDataFolder(type), id + ".json");
	}

	private File getIndexFile(Class<? extends AbstractSchema> type) {

		File file = new File(getDataFolder(type), "index");

		if (!file.exists() || !file.isFile()) {
			try {

				file.createNewFile();
				PrintWriter pw = new PrintWriter(file);
				pw.print("1");
				pw.flush();
				pw.close();

			} catch (IOException e) {

				e.printStackTrace();
				Logger.fatal("Cannot create index file for entity " + type.getName());
			}
		}

		return file;
	}

	private int getIndex(AbstractSchema entity) throws NumberFormatException, IOException {
		return getIndex(entity.getClass());
	}

	private int getIndex(Class<? extends AbstractSchema> type) throws NumberFormatException, IOException {
		return Integer.parseInt(Files.readString(Path.of(getIndexFile(type).getPath())));
	}

	/**
	 *
	 * Inject the next available id into this entity and increments the index
	 *
	 * @param entity
	 * @return the id that was injected into this entity
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private int injectId(AbstractSchema entity) throws NumberFormatException, IOException {

		entity.setId(getIndex(entity));

		File file = getIndexFile(entity.getClass());

		PrintWriter pw = new PrintWriter(file);
		pw.print(entity.getId() + 1);
		pw.flush();
		pw.close();

		return entity.getId();
	}

}
