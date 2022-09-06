/**
 *
 */
package fr.rsquatre.meteor.service.data;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.service.data.schema.AbstractSchema;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class AbstractEntityManager implements IService {

	public abstract @NotNull <E extends AbstractSchema> Collection<E> findAll(@NotNull Class<E> type);

	public abstract @Nullable <E extends AbstractSchema> E find(@NotNull Class<E> type, @NotNull int id);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> find(@NotNull Class<E> type, @NotNull int... ids);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> find(@NotNull Class<E> type, @NotNull Collection<Integer> ids);

	public abstract @Nullable <E extends AbstractSchema> E findOneBy(@NotNull Class<E> type, @NotNull String field, @Nullable Object value);

	public abstract @Nullable <E extends AbstractSchema> E findOneBy(@NotNull Class<E> type, @NotNull String[] fields, @NotNull Object[] values);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @Nullable Object value);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @NotNull Object... values);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> findBy(@NotNull Class<E> type, @NotNull String field, @NotNull Collection<Object> values);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> findBy(@NotNull Class<E> type, @NotNull String[] fields, @NotNull Object[] values);

	public abstract AbstractEntityManager persist(@NotNull AbstractSchema entity);

	public abstract AbstractEntityManager persist(@NotNull AbstractSchema... entities);

	public abstract AbstractEntityManager persist(@NotNull Collection<AbstractSchema> entities);

	public abstract AbstractEntityManager remove(@NotNull AbstractSchema entity);

	public abstract AbstractEntityManager remove(@NotNull AbstractSchema... entities);

	public abstract AbstractEntityManager remove(@NotNull Collection<AbstractSchema> entities);

	public abstract AbstractEntityManager delete(@NotNull AbstractSchema entity);

	public abstract AbstractEntityManager delete(@NotNull AbstractSchema... entities);

	public abstract AbstractEntityManager delete(@NotNull Collection<AbstractSchema> entities);

	public abstract AbstractEntityManager flush();

	public abstract @NotNull AbstractEntityManager getInstance();

	public abstract @NotNull String getSaveType();

	protected abstract boolean isValid();

}
