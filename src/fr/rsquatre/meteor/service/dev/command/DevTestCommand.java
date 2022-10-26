/**
 *
 */
package fr.rsquatre.meteor.service.dev.command;

import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.command.AdvancedCommandExecutor;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.service.dev.DevTests;
import fr.rsquatre.meteor.service.dev.DevTests.TestResult;
import fr.rsquatre.meteor.util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class DevTestCommand extends AdvancedCommandExecutor {

	public DevTestCommand() {
		super("dev", "test");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		if (sender.isOp() && "test".equalsIgnoreCase(label) && Meteor.isRegistered(DevTests.class) && args.length == 1) {

			boolean wasInDevMode = Logger.isInDevMode();
			Logger.setDevMode(true);

			Supplier<TestResult> s = Meteor.getService(DevTests.class).findTest(args[0]);
			if (s != null) {

				Logger.debug("Found a test for the name : " + args[0]);

				TestResult tr = s.get();

				TextColor tc = null;

				tc = switch (tr.getStatus()) {
				case UNKNOWN -> TextColor.fromHexString("cc7700");
				case SUCCESS -> TextColor.fromHexString("00aa00");
				default -> TextColor.fromHexString("aa0000");
				};

				sender.sendMessage(Component.text("[" + tr.getStatus().name() + "] ", tc).append(Component.text(tr.getMessage())));
				sender.sendMessage(Component.text("Processing time: " + tr.getExecutionTime() + "ms"));

				Logger.debug("[" + tr.getStatus().name() + "] " + tr.getMessage());
				Logger.debug("Processing time: " + tr.getExecutionTime() + "ms");

			} else {

				sender.sendMessage(Component.text("Could not find a test named : " + args[0], TextColor.fromHexString("cc0000")));
			}

			if (!wasInDevMode) { Logger.setDevMode(false); }
		}

		return true;
	}

	@Override
	public Class<? extends IService> getService() {

		return DevTests.class;
	}

}
