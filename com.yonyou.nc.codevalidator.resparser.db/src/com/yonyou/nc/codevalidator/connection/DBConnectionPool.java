package com.yonyou.nc.codevalidator.connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 物理连接沲
 * 
 * @author linmin
 * 
 */
public abstract class DBConnectionPool implements ConnectionPool {

    protected int conMax = 100;

    protected int conMin = 2;  

    private List<PoolableConnection> pool = new ArrayList<PoolableConnection>();

    private final Object poolLock = new Object();
    /**
     * 等待建立连接的时间
     */
    protected int loginTimeout = 15 * 1000;

    public void initPool() {
        newConnection(conMin);
    }

    private void newConnection(int size) {
        try {
            for (int i = 0; i < conMin; i++) {
                synchronized (poolLock) {
                    PoolableConnection newConnection = createConnection();
                    newConnection.setClosed(true);
                    pool.add(newConnection);
                }
            }
        } catch (Exception e) {
            Logger.error("initializing the connection pool failed,can't open database connection", e);
        }
    }

    protected abstract PoolableConnection createConnection() throws SQLException;

    /* (non-Javadoc)
     * @see com.yonyou.nc.codevalidator.connection.ConnectionPool#getConnection()
     */
    public PoolableConnection getConnection()  {
    	//mazhqa cancel retry operation...
        PoolableConnection conn = null;
//        long time;
        SQLException cause = null;
//        for (time = 0L; conn == null && time < (long) loginTimeout;)
            synchronized (poolLock) {
                List<PoolableConnection> unAvailbleCon = new ArrayList<PoolableConnection>();
                try {
                    Iterator<PoolableConnection> iterator = pool.iterator();
                    while (iterator.hasNext()) {
                        PoolableConnection c = (PoolableConnection) iterator.next();
                        if (!c.isClosed())
                            continue;
                        if (testConnection(c)) {
                            conn = c;
                            conn.setClosed(false);
                            break;
                        }
                        try {
                            c.reallyClose();
                        } catch (Exception exception) {
                        }
                        if (unAvailbleCon == null)
                            unAvailbleCon = new ArrayList<PoolableConnection>();
                        unAvailbleCon.add(c);
                    }
                    if (unAvailbleCon.size() > 0)
                        pool.removeAll(unAvailbleCon);
                    if (conn == null) {
                        conn = createConnection();
                        if (conn != null)
                            pool.add(conn);
                    }
                } catch (SQLException e) {
                    cause = e;
                    Logger.debug((new StringBuilder("fetch connection failed:")).append(e.getMessage())
                            .append(", will try again..").toString());
//                    time += 500L;
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException interruptedexception) {
                    }
                }
            }

        if (conn == null) {
//            if (time >= (long) loginTimeout) {
//                throw new RuntimeException("Try to get database connection timeout! ");
//            } else {
                throw new RuntimeException("try to get database connection error", cause);
//            }
        } else {
            return conn;
        }
    }

    private boolean testConnection(PoolableConnection c) {
        boolean result = true;
        try {
            setTransactionIsolation_ReadComitted(c);
        } catch (Throwable t) {
            result = false;
        }
        return result;
    }

    public synchronized void setTransactionIsolation_ReadComitted(PoolableConnection c) throws Exception {
        // oracle
        if (c.getDbType() == PoolableConnection.ORACLE) {
            Statement stmt = c.createStatement();
            try {
                stmt.execute("ALTER SESSION SET ISOLATION_LEVEL = READ COMMITTED");
            } finally {
                stmt.close();
            }
        } else {
            c.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
        }
    }

    public void destroy() {
        synchronized (poolLock) {
            for (PoolableConnection conn : pool) {
                try {
                    conn.reallyClose();
                } catch (Exception e) {
                    Logger.error("destroy connection error", e);
                }
            }
        }
    }

    public void destroyConnection(PoolableConnection conn) throws Exception {
        conn.reallyClose();
    }

    public void removeOrStay(PoolableConnection conn) {
        synchronized (poolLock) {
            if (pool.size() > conMax) {
                try {
                    conn.reallyClose();
                } catch (SQLException e) {
                }
                pool.remove(conn);
            } else {
                conn.setClosed(true);
            }
        }
    }

    public int getPooledNum() {
        synchronized (poolLock) {
            return pool.size();
        }
    }

    public int getConMax() {
        return conMax;
    }

    public void setConMax(int conMax) {
        this.conMax = conMax;
    }

    public int getConMin() {
        return conMin;
    }

    public void setConMin(int conMin) {
        this.conMin = conMin;
    }

    public int getLoginTimeout() {
        return loginTimeout;
    }

    public void setLoginTimeout(int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }
}
