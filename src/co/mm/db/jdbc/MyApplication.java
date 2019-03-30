/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.db.jdbc;

import co.mm.util.LogUtil;
import co.mm.util.PropertiesConfig;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

public class MyApplication {

    public static ObjectPool pool2;

    public static void initialResource() {
        System.setProperty("app.path.log", "C:\\Users\\User\\Desktop\\Batch Bot\\logs");
        PropertiesConfig.loadConfig("D:\\Work Public\\Batch\\SMSGSMModem\\src\\resources\\config.properties", "D:\\Work Public\\Batch\\SMSGSMModem\\src\\resources\\log4j.properties");
        LogUtil.getLogService().info("Start Process ..... GSMModem SMS Auto");
        LogUtil.getLogService().info("Config Load Success.");

    }

    public static ObjectPool initMySqlConnectionPool() {
        initialResource();
        /*
               If you are using Google's Guava:
               properties.load(Resources.getResource("config.properties");
         */

        String DB_DRIVER = PropertiesConfig.getConfig("db.driver");
        String DB_CONNECTION = PropertiesConfig.getConfig("db.url");
        String DB_USER = PropertiesConfig.getConfig("db.username");
        String DB_PASSWORD = PropertiesConfig.getConfig("db.password");
        String DB_CHARACTER_ENCODING = PropertiesConfig.getConfig("db.characterEncoding");

        String host = "103.102.46.145";
        String port = "3306";
        String schema = "database_initial";
        String user = DB_USER;
        String password = DB_PASSWORD;

        PoolableObjectFactory mySqlPoolableObjectFactory = new MySqlPoolableObjectFactory(host,
                Integer.parseInt(port), schema, user, password);
        Config config = new GenericObjectPool.Config();
        config.maxActive = 10;
        config.testOnBorrow = true;
        config.testWhileIdle = true;
        config.timeBetweenEvictionRunsMillis = 10000;
        config.minEvictableIdleTimeMillis = 60000;
        

        GenericObjectPoolFactory genericObjectPoolFactory = new GenericObjectPoolFactory(mySqlPoolableObjectFactory, config);
        pool2 = genericObjectPoolFactory.createPool();
        return pool2;
    }

    public static void main(String[] args) {

        pool2 = initMySqlConnectionPool();
        ExampleClassUsesMySQLConnectionPool MysqlPool = new ExampleClassUsesMySQLConnectionPool(pool2);
        MysqlPool.getRecords();

    }
}
