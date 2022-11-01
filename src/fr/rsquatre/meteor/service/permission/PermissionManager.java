/**
 *
 */
package fr.rsquatre.meteor.service.permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.impl.IService;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class PermissionManager implements IService {

	// Statics

	/**
	 *
	 * This method is always available regardless of this service's status
	 *
	 * @return true if the player is granted the permission, false otherwise
	 */
	public static boolean isGranted(Player player) {

		// TODO

		return false;
	}

	// Overrides

	@Override
	public void load() throws Exception {

	}

	@Override
	public void unload() {

	}

	@Override
	public @NotNull String getName() {
		return "Meteor:PermissionManager";
	}

	@Override
	public @NotNull Class<? extends JavaPlugin> getOwner() {
		return Meteor.class;
	}

	@Override
	public @NotNull boolean isSystem() {
		return false;
	}

}
