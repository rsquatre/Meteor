package fr.rsquatre.meteor.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.util.Constraints.NotNullOrDefault;

public class Logger {

	private static org.slf4j.Logger logger;

	private static boolean devMode = false;

	public Logger() {
		logger = Meteor.getInstance().getSLF4JLogger();
	}

	public static String info(@NotNull Object str) {
		logger.info(str.toString());
		return str.toString();
	}

	public static String warn(@NotNull Object str) {
		logger.warn(str.toString());
		return str.toString();
	}

	public static String error(@NotNull Object str) {
		logger.error(str.toString());
		return str.toString();
	}

	public static String fatal(@NotNull Object str) {
		setDevMode(true);

		logger.error("[FATAL ERROR]" + str.toString());

		// Bukkit.broadcast(Component.text("[FATAL ERROR] Please inform the
		// administrator!"), Permissions.STAFF);
		// Bukkit.broadcast(Component.text("[FATAL ERROR] " + str.toString()),
		// Permissions.STAFF);

		// SecurityHandler.integrityStop();

		return str.toString();
	}

	public static String debug(@Nullable Object str) {

		if (devMode) {
			if (logger.isDebugEnabled()) {
				logger.debug(new NotNullOrDefault<>(str.toString(), "null").value());
			} else {
				logger.info("[DEBUG] " + str.toString());
			}
		}
		return str.toString();

	}

	public static void setDevMode(boolean mode) {
		devMode = mode;
	}

	public static boolean isInDevMode() {
		return devMode;
	}

}
