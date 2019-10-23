package co.mm.db.jdbc;




import co.mm.util.EncryptionUtil;
import co.mm.util.PropertiesConfig;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;

public class DBConnectionPoolManager {
    
    private static final String POOL_DB_CONNECTION = EncryptionUtil.decrypt(PropertiesConfig.getConfig("pool.db.url"));
    private static final String POOL_DB_PORT = PropertiesConfig.getConfig("pool.db.port");
    private static final String POOL_DB_USER = EncryptionUtil.decrypt(PropertiesConfig.getConfig("pool.db.username"));
    private static final String POOL_DB_PASSWORD = EncryptionUtil.decrypt(PropertiesConfig.getConfig("pool.db.password"));
    private static final String POOL_DB_SCHEMA = PropertiesConfig.getConfig("pool.db.schema");
    
    public ObjectPool connPool;
    
    public ObjectPool getConnectPoolDB() {
        
        MySqlPoolableObjectFactory mySqlPoolableObjectFactory = new MySqlPoolableObjectFactory(
                POOL_DB_CONNECTION,
                Integer.parseInt(POOL_DB_PORT),
                POOL_DB_SCHEMA, POOL_DB_USER,
                POOL_DB_PASSWORD);
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.maxActive = 30;
        config.testOnBorrow = true;
        config.testWhileIdle = true;
        config.timeBetweenEvictionRunsMillis = 10000;
        config.minEvictableIdleTimeMillis = 60000;
        
        GenericObjectPoolFactory genericObjectPoolFactory = new GenericObjectPoolFactory(mySqlPoolableObjectFactory, config);
        connPool = genericObjectPoolFactory.createPool();
        return connPool;
    }
    
    
    
}
