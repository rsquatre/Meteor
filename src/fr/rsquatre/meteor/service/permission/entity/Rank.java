/**
 *
 */
package fr.rsquatre.meteor.service.permission.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.service.data.schema.CachedSchema;
import fr.rsquatre.meteor.service.data.schema.EM.CollectionField;
import fr.rsquatre.meteor.service.data.schema.EM.Field;
import fr.rsquatre.meteor.service.data.schema.EM.Id;
import fr.rsquatre.meteor.service.data.schema.EM.MapField;
import fr.rsquatre.meteor.service.data.schema.EM.Schema;
import fr.rsquatre.meteor.util.Constraints;
import fr.rsquatre.meteor.util.json.TransientPostProcessorFactory.ITransientPostProcessable;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
@Schema(name = "ranks")
public class Rank extends CachedSchema implements ITransientPostProcessable {

	@Id
	private int id;
	@Field(name = "name")
	private String name = "";
	@Field(name = "power")
	private int power = 0;
	@Field(name = "parents")
	@CollectionField(type = Integer.class)
	private HashSet<Integer> parents = new HashSet<>();
	@Field(name = "permissions")
	@MapField(keyType = String.class, valueType = Boolean.class)
	private HashMap<String, Boolean> permissions = new HashMap<>();
	@Field(name = "colour")
	private String colour = "ffffff";
	@Field(name = "display_name")
	private String displayName = "";
	@Field(name = "tab_display_name")
	private String tabDisplayName = "";

	private transient HashMap<String, Boolean> wildcards = null;

	public boolean isGranted(String permission) {

		if (new Constraints(permission).notBlank().isValid()) { // fails if blank or null

			// check if a wildcard allows it
			for (Entry<String, Boolean> wildcard : wildcards.entrySet()) {

				if (permission.startsWith(wildcard.getKey()))
					return wildcard.getValue();
			}

			// check if direct permission allows it
			Boolean hasPerm = permissions.get(permission);
			if (hasPerm != null)
				return hasPerm;

			// check if parents allows it
			for (Rank parent : getParents())
				return parent.isGranted(permission);

		}

		return false;
	}

	// Overrides, getters & setters

	@Override
	public CacheType getCacheType() {
		return CacheType.MEMORY;
	}

	@Override
	public void postProcess() {

		if (wildcards == null) {

			wildcards = new HashMap<>();
			permissions.keySet()
					.forEach(perm -> { if (perm.endsWith(".*")) { wildcards.put(perm.split("\\.\\*$")[0], permissions.get(perm.toLowerCase())); } });
		}
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	@SuppressWarnings("unchecked")
	public Collection<Rank> getParents() {

		if (!parents.isEmpty())
			return (Collection<Rank>) Meteor.getEntityManager().find(this.getClass(), parents);

		return Collections.emptyList();
	}

	public HashSet<Integer> getParentIds() {
		return parents;
	}

	public void setParentsIds(HashSet<Integer> parents) {
		this.parents = parents;
	}

	public void addParent(int parent) {
		parents.add(parent);
	}

	public void removeParent(int parent) {
		parents.remove(parent);
	}

	public HashMap<String, Boolean> getPermissions() {
		return permissions;
	}

	public void setPermissions(HashMap<String, Boolean> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(String permission, boolean allowed) {
		permissions.put(permission, allowed);
	}

	public void removePermission(String permission) {
		permissions.remove(permission);
	}

	public String getColour() {
		return colour;
	}

	/**
	 * Yes I thought about you Americans
	 */
	public String getColor() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * Yes I thought about you Americans
	 */
	public void setColor(String color) {
		colour = color;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTabDisplayName() {
		return tabDisplayName;
	}

	public void setTabDisplayName(String tabDisplayName) {
		this.tabDisplayName = tabDisplayName;
	}

}
