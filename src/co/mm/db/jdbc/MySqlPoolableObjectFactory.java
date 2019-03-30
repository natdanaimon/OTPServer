/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.db.jdbc;

import java.sql.DriverManager;
import org.apache.commons.pool.BasePoolableObjectFactory;
 
public class MySqlPoolableObjectFactory extends BasePoolableObjectFactory {
     private String host;
     private int port;
     private String schema;
     private String user;
     private String password;
 
     public MySqlPoolableObjectFactory(String host, int port, String schema, String user, String password) {
          this.host = host;
          this.port = port;
          this.schema = schema;
          this.user = user;
          this.password = password;
     }
 
    /**
     *
     * @return
     * @throws Exception
     */
    @Override
     public Object makeObject() throws Exception {
          Class.forName("com.mysql.jdbc.Driver").newInstance();
          String url = "jdbc:mysql://" + host + ":" + port + "/" + schema + "?characterEncoding=UTF-8&autoReconnectForPools=true";
          return DriverManager.getConnection(url, user, password);
      }
}