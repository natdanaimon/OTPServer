package co.mm.logs;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class PortalLogger extends Logger {

	// It's enough to instantiate a factory once and for all.
	private static PortalLoggerFactory logFactory = new PortalLoggerFactory();

	protected PortalLogger(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method overrides {@link Logger#getInstance} by supplying its own
	 * factory type as a parameter.
	 */
	public static Category getInstance(String name) {
		return Logger.getLogger(name, logFactory);
	}

	/**
	 * This method overrides {@link Logger#getLogger} by supplying its own
	 * factory type as a parameter.
	 */
	public static Logger getLogger(String name) {
		return Logger.getLogger(name, logFactory);
	}
}
