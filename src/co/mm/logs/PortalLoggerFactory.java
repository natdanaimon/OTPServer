package co.mm.logs;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class PortalLoggerFactory implements LoggerFactory {

	@Override
	public Logger makeNewLoggerInstance(String name) {
		// TODO Auto-generated method stub
		return new PortalLogger(name);
	}
}
