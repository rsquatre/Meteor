package fr.rsquatre.meteor.util.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils {

	@Deprecated
	public static String format(String text) {

		text = text.replaceAll("&0", "§0");
		text = text.replaceAll("&1", "§1");
		text = text.replaceAll("&2", "§2");
		text = text.replaceAll("&3", "§3");
		text = text.replaceAll("&4", "§4");
		text = text.replaceAll("&5", "§5");
		text = text.replaceAll("&6", "§6");
		text = text.replaceAll("&7", "§7");
		text = text.replaceAll("&8", "§8");
		text = text.replaceAll("&9", "§9");
		text = text.replaceAll("&a", "§a");
		text = text.replaceAll("&b", "§b");
		text = text.replaceAll("&c", "§c");
		text = text.replaceAll("&d", "§d");
		text = text.replaceAll("&e", "§e");
		text = text.replaceAll("&f", "§f");
		text = text.replaceAll("&l", "§l");
		text = text.replaceAll("&m", "§m");
		text = text.replaceAll("&o", "§o");
		text = text.replaceAll("&k", "§k");
		text = text.replaceAll("&n", "§n");
		text = text.replaceAll("&u", "§u");
		text = text.replaceAll("&r", "§r");
		return text.replaceAll("<3", "♥");
	}

	public static String getCharForNumber(int i) {

		return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : null;
	}

	private final static int CENTER_PX = 154;

	public static void sendCenteredMessage(CommandSender player, String message) {
		if (message == null || "".equals(message)) { player.sendMessage(""); }
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			}
			if (previousCode) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				}
				isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		player.sendMessage(sb.toString() + message);
	}

	public static String centre(String message) {

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			}
			if (previousCode) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				}
				isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		return sb.toString() + message;
	}

	public static final String align(int chars, String message) {

		int length = 0;

		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			}
			if (previousCode) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				}
				isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				length += isBold ? dFI.getBoldLength() : dFI.getLength();
				length++;
			}
		}

		while (chars * 4.5 > length) {
			message = message.concat(" ");
			length += DefaultFontInfo.SPACE.getLength() + 1;
		}
		return message;
	}

	public static String formatNumber(int amount, boolean useComaAsSeparator) {
		return String.valueOf(amount).replaceAll("(?<=\\d)(?=((\\d{3})+)(?!\\d))", useComaAsSeparator ? "," : " ");
	}

	public static String repeat(String text, int amount) {

		if (text == null)
			throw new IllegalArgumentException("Variable string:text cannot be null");

		if (amount < 1)
			throw new IllegalArgumentException("Variable int:amount cannot be smaller than 1");

		StringBuilder sb = new StringBuilder(text);

		for (int i = 0; i < amount; i++) { sb.append(text); }

		return sb.toString();
	}

}
