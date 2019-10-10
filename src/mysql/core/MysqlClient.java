package slip.mysql.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 管理数据库连接，一个对象对应一条连接
 */
public class MysqlClient {
    private String url = null;
    private String username = null;
    private String password = null;
    private Connection connection = null;

    /**
     * 传递完整的数据库连接信息并获取数据库连接
     */
    public MysqlClient(String ip, String port, String username, String password) {
        StringBuilder urlTemp = new StringBuilder("jdbc:mysql://");
        urlTemp.append(ip).append(":").append(port).append("/").append("mysql").append("?useUnicode=true&characterEncoding=UTF-8");
        this.url = new String(urlTemp);
        this.username = username;
        this.password = password;
        connection = getConnection();
    }

    /**
     * 通过默认端口获取数据库连接
     */
    public MysqlClient(String ip, String username, String password) {
        StringBuilder urlTemp = new StringBuilder("jdbc:mysql://");
        urlTemp.append(ip).append(":").append("3306").append("/").append("mysql").append("?useUnicode=true&characterEncoding=UTF-8");
        this.url = new String(urlTemp);
        this.username = username;
        this.password = password;
        connection = getConnection();
    }

    /**
     * 管理数据库连接
     */
    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return connection;
        }
    }

    /**
     * 通过数据库名称获取一个指定的数据库
     */
    public MysqlDatabase getDatabase(String databaseName) {
        return new MysqlDatabase(this.connection, databaseName);
    }

    /**
     * 获取所有数据库信息，对所有数据库进行操作
     */
    public MysqlDatabase getDatabases() {
        return new MysqlDatabase(this.connection);
    }
}
