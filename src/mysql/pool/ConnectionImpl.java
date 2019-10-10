package slip.mysql.pool;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 描述: 自定义连接池的连接对象类型
 */
public class ConnectionImpl extends Connection {

    // 连接数据库用的数据源信息
    private static DataSource dataSource;

    // 和数据库保持的连接
    private java.sql.Connection connection;

    // 全局标识连接的序号  未使用的序号
    private static ConcurrentSkipListSet<Integer> unuseConnectionNoSet;

    // 全局标识连接的序号  已使用的序号
    private static ConcurrentSkipListSet<Integer> usedConnectionNoSet;

    // 连接的序号
    private int connectionNo;

    // 连接是否空闲
    private boolean idle;

    // 连接开始空闲的时间起点
    private Long endUseTime;

    // 初始化连接信息
    public ConnectionImpl() {
        // 该连接还未使用，置为null
        this.endUseTime = null;

        // 创建新的数据库连接，并给连接分配全局的序号
        try {
            this.connection = DriverManager.getConnection(dataSource.getUrl(),
                    dataSource.getUsername(),
                    dataSource.getUserpwd());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 给当前连接分配一个全局的序号
        int no = unuseConnectionNoSet.pollFirst();
        usedConnectionNoSet.add(no);
        this.connectionNo = no;

        // 新创建的连接设置空闲状态
        this.idle = true;

        //System.out.println("连接池创建了新的连接 No:" + this.connectionNo);
    }

    public static void setDataSource(DataSource dataSource) {
        ConnectionImpl.dataSource = dataSource;

        // 加载数据库驱动
        try {
            Class.forName(dataSource.getDriverName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 根据最大连接量，创建下标序号集合
        if (unuseConnectionNoSet == null) {
            unuseConnectionNoSet = new ConcurrentSkipListSet<>();
            for (int i = 0; i < dataSource.getMaxPoolSize(); i++) {
                unuseConnectionNoSet.add(i + 1);
            }
        }

        if (usedConnectionNoSet == null) {
            usedConnectionNoSet = new ConcurrentSkipListSet<>();
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * 把连接归还到连接池
     *
     * @throws SQLException
     */
    @Override
    public void close() throws SQLException {
        // 把连接归还到连接池，重置连接的初始状态
        idle = true;
        endUseTime = new Date().getTime();
        System.out.println("连接 No:" + connectionNo + " 已归还到连接池");
    }

    /**
     * 释放连接资源
     */
    public void releaseConnection() {
        // 被释放的连接，需要把连接的序号归还给set集合
        usedConnectionNoSet.remove(connectionNo);
        unuseConnectionNoSet.add(connectionNo);
        try {
            // 释放数据库连接资源
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public void setConnection(java.sql.Connection connection) {
        this.connection = connection;
    }

    public Long getEndUseTime() {
        return endUseTime;
    }

    public void setEndUseTime(Long endUseTime) {
        this.endUseTime = endUseTime;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public int getConnectionNo() {
        return connectionNo;
    }
}
