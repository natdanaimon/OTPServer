/*
 * To change this license header, choose License Headers in Project PropertiesConfig.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author 
 */
public class PropertiesConfig {

	public static Properties config = null;
	public static InputStream inputStream;

	public static void loadConfig(String pathConfig, String pathConfigLog4j) {
		try {
			config = new Properties();

			inputStream = new FileInputStream(new File(pathConfig));

			if (inputStream != null) {
				config.load(inputStream);
				LogUtil.loadConfigLog4j(pathConfigLog4j);
			}

		} catch (Exception e) {
			System.out.println("Exception : " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException ex) {
				System.out.println("IOException : " + ex);
			}
		}
	}

	public static String getConfig(String key) {
		String result = "";
		try {
			result = config.getProperty(key);
		} catch (Exception e) {

		}
		return result;
	}

	public void AppLogsLoadConfig(String pathLogConfig) {
		PropertyConfigurator.configure(pathLogConfig);
	}
}
