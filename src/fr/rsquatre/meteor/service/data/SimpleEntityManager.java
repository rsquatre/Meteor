/**
 *
 */
package fr.rsquatre.meteor.service.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonSyntaxException;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.service.data.schema.AbstractSchema;
import fr.rsquatre.meteor.service.data.schema.Schema;
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

	private HashMap<Class<? extends AbstractSchema>, HashSet<AbstractSchema>> create = new HashMap<>();
	private HashMap<Class<? extends AbstractSchema>, HashSet<AbstractSchema>> persist = new HashMap<>();
	private HashMap<Class<? extends AbstractSchema>, HashSet<AbstractSchema>> delete = new HashMap<>();

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> find(@NotNull Class<E> type, @NotNull Collection<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @Nullable E findOneBy(@NotNull Class<E> type, @NotNull String field, @Nullable Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @Nullable E findOneBy(@NotNull Class<E> type, @NotNull String[] fields, @NotNull Object[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @Nullable Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @NotNull Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @NotNull Collection<Object> values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends AbstractSchema> @NotNull Collection<E> findBy(@NotNull Class<E> type, @NotNull String[] fields, @NotNull Object[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager persist(@NotNull AbstractSchema entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager persist(@NotNull AbstractSchema... entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager persist(@NotNull Collection<AbstractSchema> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager remove(@NotNull AbstractSchema entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager remove(@NotNull AbstractSchema... entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager remove(@NotNull Collection<AbstractSchema> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager delete(@NotNull AbstractSchema entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager delete(@NotNull AbstractSchema... entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager delete(@NotNull Collection<AbstractSchema> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityManager flush() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull AbstractEntityManager getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull String getSaveType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	private File getDataFolder(Class<? extends AbstractSchema> type) {

		if (type.isAnnotationPresent(Schema.class) || new Constraints(type.getAnnotation(Schema.class).name()).notBlank().isValid())
			throw new IllegalStateException(
					"Class " + type.getName() + " is an entity but failed to declare a valid " + Schema.class.getName() + " annotation");

		return new File(files, type.getAnnotation(Schema.class).name());
	}

	private File getIndex(Class<? extends AbstractSchema> type) {

		return new File(getDataFolder(type), "index");
	}

}
