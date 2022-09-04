/**
 *
 */
package fr.rsquatre.meteor.impl;

import org.bukkit.event.Listener;

/**
 *
 * A {@link Listener} with an {@link IService} handler<br>
 * Advanced Listeners are unregistered when their handler is unloaded
 *
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public interface IAdvancedListener extends Listener {

	public Class<? extends IService> getHandler();

}
