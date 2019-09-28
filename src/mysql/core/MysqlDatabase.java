package mysql.core;


import slip.mysql.base.Execute;
import slip.mysql.base.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 管理在指定数据库连接下的对数据库的操作
 */
public class MysqlDatabase {
    private String databaseName = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    /**
     * 指定连接的数据库
     */
    public MysqlDatabase(Connection connection, String databaseName) {
        this.connection = connection;
        this.databaseName = databaseName;
        Execute.otherExecute(this.connection, SQL.use(databaseName));
    }

    /**
     * 对数据库进行操作
     */
    public MysqlDatabase(Connection connection) {
        this.connection = connection;
    }

    /**
     * 连接数据库下指定的表
     */
    public MysqlTable getTable(String tableName) {
        return new MysqlTable(this.connection, tableName);
    }

    /**
     * 根据实体类的class对象将表和实体类关联起来
     */
    public MysqlTable getTable(Class bean) {
        return new MysqlTable(this.connection, bean);
    }

    /**
     * 连接数据库下所有的表
     */
    public MysqlTable getTables() {
        return new MysqlTable(this.connection);
    }

    /**
     * 查看所有数据库
     */
    public List<String> showDatabases() {
        if (databaseName != null) {
            List<Map> databases = Execute.selectExecute(this.connection, SQL.showDatabases());
            List<String> results = new LinkedList<>();
            for (Map database : databases) {
                String db = (String) database.get("database");
                results.add(db);
            }
            return results;
        } else {
            System.out.println("正处于" + databaseName + "数据库中，无法查看所有数据库信息");
            return null;
        }
    }
}
