/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.db.jdbc;

import co.mm.logs.Log;
import co.mm.util.LogUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;

/**
 *
 * @author natdanaimon
 */
public class ExampleClassUsesMySQLConnectionPool {

    public static final String SQL_SELECT = "SELECT * FROM tb_transaction where d_create like '2018-11-03%' ";
    private final ObjectPool connPool;

    public ExampleClassUsesMySQLConnectionPool(ObjectPool connPool) {
        this.connPool = connPool;
    }

    public void getRecords() {
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = (Connection) connPool.borrowObject();
            st = conn.createStatement();
            rs = st.executeQuery(SQL_SELECT);
            while (rs.next()) {
                System.out.println(rs.getString("s_in"));
            }
        } catch (SQLException e) {
            LogUtil.getLogService().error("Failed to return the connection to the pool : " + e.getMessage());
        } catch (Exception e) {
            LogUtil.getLogService().error("Failed to return the connection to the pool : " + e.getMessage());
        } finally {
            safeClose(rs);
            safeClose(st);
            safeClose(conn);
        }
       
    }

    private void safeClose(Connection conn) {
        if (conn != null) {
            try {
                connPool.returnObject(conn);
            } catch (Exception e) {
                LogUtil.getLogService().error("Failed to return the connection to the pool : " + e.getMessage());
            }
        }
    }

    private void safeClose(ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                LogUtil.getLogService().error("Failed to close databse resultset : " + e.getMessage());
            }
        }
    }

    private void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LogUtil.getLogService().error("Failed to close databse statmen : " + e.getMessage());
            }
        }
    }
}
