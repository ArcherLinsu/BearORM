package mysql.core;

import slip.mysql.base.Execute;
import slip.mysql.base.SQL;
import slip.mysql.util.FormatData;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 管理指定数据库下对表的操作
 */
public class MysqlTable {
    private Class bean = null;
    private String tableName = null;
    private Connection connection = null;

    /**
     * 连接所有表
     */
    public MysqlTable(Connection connection) {
        this.connection = connection;
    }

    /**
     * 根据表名连接到指定表
     */
    public MysqlTable(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * 将实体类和表进行关联
     */
    public MysqlTable(Connection connection, Class bean) {
        this.bean = bean;
        this.connection = connection;
        this.tableName = bean.getSimpleName().toLowerCase();
    }

    public List<String> showTables() {
        if (tableName != null) {
            List<Map> tables = Execute.selectExecute(this.connection, SQL.showTables());
            List<String> results = new LinkedList<>();
            for (Map table : tables) {
                String tb = (String) table.get("table");
                results.add(tb);
            }
            return results;
        } else {
            System.out.println("正处于" + tableName + "表中，无法查看所有表信息");
            return null;
        }
    }


    /**
     * 获取指定表下的所有数据
     */
    public List selectList() {
        List<Map> resultsMap = Execute.selectExecute(this.connection, SQL.select(tableName));
        List results = new LinkedList();
        if (bean != null) {
            for (Map map : resultsMap) {
                results.add(FormatData.toBean(map, this.bean));
            }
            return results;
        } else {
            return resultsMap;
        }
    }

    /**
     * 根据实例查询数据
     */
    public <T> List select(T bean) {
        List<Map> resultsMap = Execute.selectExecute(this.connection, SQL.select(tableName, FormatData.toMap(bean)));
        List results = new LinkedList();
        if (resultsMap.size() != 0) {
            for (Map map : resultsMap) {
                results.add(FormatData.toBean(map, this.bean));
            }
        }
        return results;
    }

    /**
     * 根据一个或多个条件进行查询
     */
    public List select(Map conditions) {
        List<Map> resultsMap = Execute.selectExecute(this.connection, SQL.select(tableName, conditions));
        List results = new LinkedList();
        if (bean != null) {
            for (Map map : resultsMap) {
                results.add(FormatData.toBean(map, this.bean));
            }
            return results;
        } else {
            return resultsMap;
        }
    }

    /**
     * 将一个实例插入到数据库
     */
    public <T> int insert(T data) {
        return Execute.otherExecute(this.connection, SQL.insert(this.tableName, FormatData.toMap(data)));
    }

    public int insert(Map data) {
        return Execute.otherExecute(this.connection, SQL.insert(this.tableName, data));
    }

    /**
     * 将多个实例插入数据库
     */
    public <T> int insertMany(List<T> datas) {
        int flag = 0;
        for (T data : datas) {
            flag += Execute.otherExecute(this.connection, SQL.insert(this.tableName, FormatData.toMap(data)));
        }
        return flag;
    }

    public int insertManyByMap(List<Map> datas) {
        int flag = 0;
        for (Map data : datas) {
            flag += Execute.otherExecute(this.connection, SQL.insert(this.tableName, data));
        }
        return flag;
    }

    /**
     * 根据一个实例删除对应数据
     */
    public <T> int delete(T data) {
        return Execute.otherExecute(this.connection, SQL.delete(this.tableName, FormatData.toMap(data)));
    }

    public int delete(Map data) {
        return Execute.otherExecute(this.connection, SQL.delete(this.tableName, data));
    }

    /**
     * 根据多个实例删除对应数据
     */
    public <T> int deleteMany(List<T> datas) {
        int flag = 0;
        for (T data : datas) {
            flag += Execute.otherExecute(this.connection, SQL.delete(this.tableName, FormatData.toMap(data)));
        }
        return flag;
    }

    public int deleteManyByMap(List<Map> datas) {
        int flag = 0;
        for (Map data : datas) {
            flag += Execute.otherExecute(this.connection, SQL.delete(this.tableName, data));
        }
        return flag;
    }

    /**
     * 用一个新的实例替换旧的实例数据
     */
    public <T> int update(T oldBean, T newBean) {
        return Execute.otherExecute(this.connection, SQL.update(this.tableName, FormatData.toMap(newBean), FormatData.toMap(oldBean)));
    }

    public int update(Map oldData, Map newData) {
        return Execute.otherExecute(this.connection, SQL.update(this.tableName, newData, oldData));
    }
}
