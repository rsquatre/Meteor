/**
 *
 */
package fr.rsquatre.meteor.service.dev;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Supplier;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import fr.rsquatre.meteor.Meteor;
import fr.rsquatre.meteor.command.DevTestCommand;
import fr.rsquatre.meteor.impl.IService;
import fr.rsquatre.meteor.service.data.SimpleEntityManager;
import fr.rsquatre.meteor.service.dev.DevTests.TestResult.TestStatus;
import fr.rsquatre.meteor.util.Logger;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class DevTests implements IService {

	public static final HashMap<String, Supplier<TestResult>> tests = new HashMap<>();

	public Supplier<TestResult> findTest(String name) {

		return tests.get(name);
	}

	//

	@Override
	public void load() throws Exception {

		tests.put("localSaveSystem", () -> {

			TestResult tr = new TestResult();

			if (Meteor.getEntityManager() instanceof SimpleEntityManager sem) {

				Logger.debug("Testing local file based save system");

				for (int i = 0; i < 100; i++) {

					sem.persist(new TestSchema());
				}

				sem.flush();

				return tr.end(TestStatus.SUCCESS);
			}
			return tr.end(TestStatus.FAILLURE, "Local file based save system is not enabled or is not the main Entity Manager");
		});

		tests.put("clearLocalSaveSystem", () -> {

			TestResult tr = new TestResult();

			if (Meteor.getEntityManager() instanceof SimpleEntityManager sem) {

				Collection<TestSchema> c = sem.findAll(TestSchema.class);

				sem.delete(c).flush();
				return tr.end(TestStatus.SUCCESS);
			}
			return tr.end(TestStatus.FAILLURE, "Local file based save system is not enabled or is not the main Entity Manager");

		});

		Meteor.getInstance().register(new DevTestCommand());

	}

	@Override
	public void unload() {

		tests.clear();
	}

	@Override
	public @NotNull String getName() {
		return "Meteor:dev_tests";
	}

	@Override
	public @NotNull Class<? extends JavaPlugin> getOwner() {
		return Meteor.class;
	}

	public final class TestResult {

		private TestStatus status = TestStatus.SUCCESS;
		private String message = "";
		private long execTime = new Date().getTime();

		/**
		 *
		 *
		 * @param status
		 * @return this
		 */
		public TestResult end(TestStatus status) {
			return end(status, "");
		}

		/**
		 *
		 *
		 * @param status
		 * @param message
		 * @return this
		 */
		public TestResult end(TestStatus status, String message) {

			this.status = status;
			this.message = message;
			execTime = new Date().getTime() - execTime;

			return this;
		}

		/**
		 * @return the status
		 */
		public TestStatus getStatus() {
			return status;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @return the execTime
		 */
		public long getExecutionTime() {
			return execTime;
		}

		public enum TestStatus {

			SUCCESS, UNKNOWN, FAILLURE;
		}

	}

}
