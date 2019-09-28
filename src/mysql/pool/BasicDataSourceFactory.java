package mysql.pool;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 描述: 通过加载配置文件生成数据源
 */
public class BasicDataSourceFactory {

    /**
     * 从属性文件中加载配置信息，生成数据源对象并返回
     * @param pro
     * @return
     */
    public DataSource getDataSource(Properties pro){
        String driver = pro.getProperty("driverName");
        if(driver == null){
            throw new IllegalArgumentException("连接池-驱动的配置项名称不正确，请检查!");
        }

        String url = pro.getProperty("url");
        if(url == null){
            throw new IllegalArgumentException("连接池-连接字符串的配置项名称不正确，请检查!");
        }

        String username = pro.getProperty("username");
        if(username == null){
            throw new IllegalArgumentException("连接池-用户名的配置项名称不正确，请检查!");
        }

        String userpwd = pro.getProperty("userpwd");
        if(userpwd == null){
            throw new IllegalArgumentException("连接池-密码的配置项名称不正确，请检查!");
        }

        String initPoolSize = pro.getProperty("initPoolSize");
        if(initPoolSize == null){
            throw new IllegalArgumentException("连接池-初始连接量的配置项名称不正确，请检查!");
        }

        String maxIdleTime = pro.getProperty("maxIdleTime");
        if(maxIdleTime == null){
            throw new IllegalArgumentException("连接池-最大空闲时间的配置项名称不正确，请检查!");
        }

        String maxPoolSize = pro.getProperty("maxPoolSize");
        if(maxPoolSize == null){
            throw new IllegalArgumentException("连接池-最大连接量的配置项名称不正确，请检查!");
        }

        // 数据源对象，存储了连接数据库的详细信息
        PoolDataSource poolDataSource = new PoolDataSource(driver, url, username, userpwd,
                Integer.parseInt(initPoolSize),
                Integer.parseInt(maxIdleTime),
                Integer.parseInt(maxPoolSize));

        return poolDataSource;
    }
}
