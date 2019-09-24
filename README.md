# Slip

**说明**：Slip基于**JDK1.8**环境开发，还没有在其它版本下验证，**理论上**在不低于JDK1.8的版本中都可以使用。另外，使用时需要自行导入mysql的jdbc包。

**简介**：针对关系型数据库的轻量ORM框架，主要具有以下特点：

- 不需要映射、注释或XML配置，也不需要继承任何类或实现任何接口，只需要POJO对象就能够达到实体类、表相互映射的效果；
- 不需要编写任何SQL语句便能够实现对MySQL数据库、表的多数常用操作，也提供执行自定义SQL的操作；
- 支持批处理、事务特性；
- 内置连接池和SQL日志系统实现；
- 提供对象数据检验、分页、SQL生成等常用操作；

---

[更新日志](./Update_log.md)	[API文档](./API_CN.md)

---

### 常用操作

以下是演示如何使用Slip以及一些常用表操作的示例代码（v1.0版本），更多操作请查看API文档

```java
public class Main {
    public static void main(String[] args) {
        //填写完整信息获取数据库连接
        MysqlClient mysqlClient = new MysqlClient("192.168.0.1","3306","root","root");
       	//不填写端口信息，则使用默认端口3306获取数据库连接
        MysqlClient mysqlClient = new MysqlClient("192.168.0.1","root","root");
        //使用表名获取指定数据库
        MysqlDatabase mysqlDatabase = mysqlClient.getDatabase("test");
        //可以使用实体类的Class对象关联到对应的表，前提是实体类名要与表名相同（不区分大小写）
        MysqlTable mysqlTable = mysqlDatabase.getTable(User.class);
        //可以直接获取一个实体类型的List列表
        List<User> users = mysqlTable.selectList();
        //将一个对象存入数据库
        User user = new User("admin","admin");
        mysqlTable.insert(user);
        //将多个对象存入数据库
        List<User> newUsers = new LinkedList<>();
        User user_01 = new User("1","1");
        User user_02 = new User("2","2");
        newUsers.add(user_01);
        newUsers.add(user_02);
        mysqlTable.insertMany(newUsers);
        //根据查询条件查询数据
        Map map = new HashMap();
        List<User> userList = mysqlTable.select(map);
        //修改数据
        User oldUser = user;
        User newUser = new User("a","a");
        mysqlTable.update(oldUser,newUser);
        //删除一个数据
        mysqlTable.delete(user);
        //删除多个数据
        mysqlTable.deleteMany(newUsers);
        
        //也可以直接使用表名连接到指定表
        MysqlTable mysqlTable = mysqlDatabase.getTable("user");
        //如果没有关联实体类，也可以获取一个Map类型的List列表
        List<Map> maps = mysqlTable.selectList();
        //增删改查的操作相同，只要将实体类对象换成Map对象即可，但要和数据库中的表名对应，不然会报错
        //例如增加一条数据
        Map<String,String> newInfo = new HashMap<>();
        newInfo.put("username","b");
        newInfo.put("password","b");
        mysqlTable.insert(newInfo);
    }
}

```

