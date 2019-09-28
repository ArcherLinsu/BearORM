package mysql.base;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 一个专门用来提交SQL语句的类，包括查询语句的提交和其它语句的提交，
 * 所有的类方法都是静态方法，可以直接调用，但需要指定执行操作的数据库连接
 */
public class Execute {
    /**
     * @param connection 执行操作的数据库连接
     * @param sql        执行的SQL语句
     * @return 查询出的结果集
     */
    public static List<Map> selectExecute(Connection connection, String sql) {
        List<Map> listTemp = new LinkedList<Map>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Map mapTemp = new HashMap();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (resultSet.getMetaData().getColumnTypeName(i).equals("VARCHAR")) {
                        mapTemp.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getString(i));
                    } else if (resultSet.getMetaData().getColumnTypeName(i).equals("INT")) {
                        mapTemp.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getInt(i));
                    }else {
                        mapTemp.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getString(i));
                    }
                }
                listTemp.add(mapTemp);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return listTemp;
        }
    }

    /**
     * @param connection 执行操作的数据库连接
     * @param sql        执行的SQL语句
     * @return 查询出的结果集
     */
    public static int otherExecute(Connection connection, String sql) {
        int flag = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            flag = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return flag;
        }
    }
}
