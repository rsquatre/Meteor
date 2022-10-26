/**
 *
 */
package fr.rsquatre.meteor.service.core.command;

import org.bukkit.command.CommandSender;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.command.AdvancedCommandExecutor;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.service.core.Core;
import fr.rsquatre.meteor.util.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class MeteorCommand extends AdvancedCommandExecutor {

	public MeteorCommand() {
		super("meteor", "status");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		Component separator = Component.text(ChatUtils.repeat("-", 30), Style.style(TextColor.fromHexString("aa0000"), TextDecoration.STRIKETHROUGH));

		sender.sendMessage(separator);

		sender.sendMessage(Component.text("This server is running Meteor version " + Meteor.getInstance().getDescription().getVersion()));

		sender.sendMessage(separator);

		return true;
	}

	@Override
	public Class<? extends IService> getService() {
		return Core.class;
	}

}
