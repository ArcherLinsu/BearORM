package slip.mysql.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 描述: 实现数据源类
 */
public class PoolDataSource implements DataSource {

    private String driverName;
    private String url;
    private String username;
    private String userpwd;
    private Integer initPoolSize = 10;
    private Integer maxIdleTime = 0;
    private Integer maxPoolSize = 1024;
    /**
     * 连接池的对象
     */
    private ConnectionPoolImpl connectionPoolImpl;

    public PoolDataSource(String driverName, String url, String username, String userpwd, Integer initPoolSize, Integer maxIdleTime, Integer maxPoolSize) {
        this.driverName = driverName;
        this.url = url;
        this.username = username;
        this.userpwd = userpwd;
        this.initPoolSize = initPoolSize;
        this.maxIdleTime = maxIdleTime;
        this.maxPoolSize = maxPoolSize;

        // 给连接设置数据源，因为PoolConnectionImpl负责创建连接需要数据源信息
        PoolConnectionImpl.setPoolDataSource(this);

        // 启动连接池的实现
        connectionPoolImpl = new ConnectionPoolImpl(this);
        connectionPoolImpl.init();
    }

    public String getDriverName() {
        return driverName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public Integer getInitPoolSize() {
        return initPoolSize;
    }

    public Integer getMaxIdleTime() {
        return maxIdleTime;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * 从连接池获取有效连接
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        return connectionPoolImpl.getConnection();
    }

    /**
     * 暂不支持该方法调用，用户调用需要抛出异常
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        new UnsupportedOperationException("暂不支持该方法实现");
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
