package fr.rsquatre.meteor.command;

import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import fr.rsquatre.meteor.impl.IService;

public abstract class AdvancedCommandExecutor extends BukkitCommand {

	public AdvancedCommandExecutor(String name) {
		super(name);
	}

	public AdvancedCommandExecutor(String name, String... aliases) {
		super(name);
		setAliases(Arrays.asList(aliases));
	}

	@Override
	public abstract boolean execute(CommandSender sender, String label, String[] args);

	/**
	 *
	 * @return true if args contains arg (case insensitive)
	 */
	public boolean isValid(String arg, String... args) {
		for (String s : args) {
			if (s.equalsIgnoreCase(arg))
				return true;
		}
		return false;
	}

	public abstract Class<? extends IService> getService();

	// Utils

	protected boolean isInCreative(CommandSender sender) {
		return sender instanceof Player p && p.getGameMode() == GameMode.CREATIVE;
	}

	protected boolean isConsole(CommandSender sender) {
		return !(sender instanceof Player);
	}

}
