# Slip文档

## 软件包(Packages)

|               软件包                |                  摘要                  |
| :---------------------------------: | :------------------------------------: |
|             slip.mysql              |  包含Slip对于MySQL数据库实现的所有类   |
| [slip.mysql.base](#slip.mysql.base) |      提供实现Slip-MySQL的基础组件      |
| [slip.mysql.core](#slip.mysql.core) |     包含所有Slip-MySQL核心方法的类     |
|       [slip.util](#slip.util)       | 包含Slip本身使用和对外提供的所有工具类 |

---

## 类（Classes）

### slip.mysql.base

|                类名                 |            摘要             | 实现接口 | 直接子类 |
| :---------------------------------: | :-------------------------: | :------: | :------: |
| [Execute](#slip.mysql.base.Execute) | 负责管理SQL的提交和后续处理 |          |          |
|     [SQL](#slip.mysql.base.SQL)     |    负责管理和提供SQL语句    |          |          |

### slip.mysql.core

|                      类名                       |                             摘要                             | 实现接口 | 直接子类 |
| :---------------------------------------------: | :----------------------------------------------------------: | :------: | :------: |
|   [MysqlClient](#slip.mysql.core.MysqlClient)   |        负责对数据库连接的统一管理和对外提供数据库入口        |          |          |
| [MysqlDatabase](#slip.mysql.core.MysqlDatabase) | 从MysqlClient获取数据库入口，对数据库进行管理、操作并对外提供表入口 |          |          |
|    [MysqlTable](#slip.mysql.core.MysqlTable)    |        从MysqlDatabase获取表入口，对表进行管理、操作         |          |          |

### slip.util

|                    类名                    |                摘要                 | 实现接口 | 直接子类 |
| :----------------------------------------: | :---------------------------------: | :------: | :------: |
| [FormatData](#slip.mysql.util.FormateData) | 提供Map和实体类型的相互转换功能的类 |          |          |

---

## 方法（Fields）

### slip.mysql.base.Execute

|                             方法                             |                         摘要                          |                           注意事项                           |
| :----------------------------------------------------------: | :---------------------------------------------------: | :----------------------------------------------------------: |
| public static List<Map> **selectExecute**(Connection connection, String sql) |        执行查询相关的SQL语句，返回对应的结果集        | Connection参数一般应由MysqlClient类提供，sql参数一般应由SQL类提供 |
| public static int **otherExecute**(Connection connection, String sql) | 执行增、删、改相关的SQL语句，并返回执行成功的数据条数 |                              ~                               |

### slip.mysql.base.SQL

|                             方法                             |                摘要                |                           注意事项                           |
| :----------------------------------------------------------: | :--------------------------------: | :----------------------------------------------------------: |
| public static String **insert**(String tableName, Map data)  |    通过参数返回对应的insert语句    | tableName参数一般由MysqlTable类进行提供，info参数为要插入的新数据 |
| public static String **delete**(String tableName, Map conditions) |    通过参数返回对应的delete语句    |          conditions参数为被删除数据的一个或多个条件          |
| public static String **update**(String tableName, Map data, Map conditions) |    通过参数返回对应的update语句    | data参数为新数据，conditions参数为被修改数据的一个或多个条件 |
| public static String **select**(String tableName, Map conditions) |    通过参数返回对应的select语句    |           conditions参数为查询数据的一个或多个条件           |
|      public static String **select**(String tableName)       | 无参数返回查询所有数据的select语句 |                              ~                               |

### slip.mysql.core.MysqlClient

|                             方法                             |                             摘要                             | 注意事项 |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :------: |
| public **MysqlClient**(String ip, String port, String username, String password) | 获取完整的数据库信息，并通过getConnection()方法返回一条数据库连接 |          |
| public **MysqlClient**(String ip, String username, String password) | 设定默认端口号，并通过getConnection()方法返回获取一条数据库连接 |          |
|            private Connection **getConnection**()            | 私有方法，负责创建数据库连接且确保一个MysqlClient对象只负责一条连接 |          |
|  public MysqlDatabase **getDatabase**(String databaseName)   |    得到一个可以对指定的数据库进行操作的MysqlDatabase对象     |          |
|           public MysqlDatabase **getDatabases**()            |     得到一个可以对所有数据库进行操作的MysqlDatabase对象      |          |

### slip.mysql.core.MysqlDatabase

|                             方法                             |                     摘要                     | 注意事项 |
| :----------------------------------------------------------: | :------------------------------------------: | :------: |
| public **MysqlDatabase**(Connection connection, String databaseName) |               获取指定的数据库               |          |
|       public **MysqlDatabase**(Connection connection)        |                获取所有数据库                |          |
|       public MysqlTable **getTable**(String tableName)       | 得到一个可以对指定表进行操作的MysqlTable对象 |          |
|          public MysqlTable **getTable**(Class bean)          |  得到一个表和实体类进行关联的MysqlTable对象  |          |
|              public MysqlTable **getTables**()               |         得到一个能对所有表进行操作的         |          |

### slip.mysql.core.MysqlTable

|                             方法                             |            摘要            |                           注意事项                           |
| :----------------------------------------------------------: | :------------------------: | :----------------------------------------------------------: |
| public **MysqlTable**(Connection connection, String tableName) |        获取指定的表        |                                                              |
|   public **MysqlTable**(Connection connection, Class data)   |    将实体类和表进行关联    |                                                              |
|         public **MysqlTable**(Connection connection)         |      获取所有表的信息      |                                                              |
|                 public List **selectList**()                 |      得到表中所有数据      | 如果没有关联实体类，则返回一个List<Map>结果集，如果已关联实体类则返回对应的List<T>结果集 |
|              public <T> List **select**(T data)              |      根据对象查询数据      |                              ~                               |
|            public List **select**(Map conditions)            | 根据一个或多个条件查询数据 |                              ~                               |
|              public <T> int **insert**(T data)               |    将一个对象插入数据库    |                                                              |
|        public <T> int **insertMany**（List<T> datas)         |    将多个对象插入数据库    |                                                              |
|              public <T> int **delete**(T data)               |      删除一个对象数据      |                                                              |
|         public <T> int **deleteMany**(List<T> datas)         |      删除多个对象数据      |                                                              |
|       public <T> int **update**(T olddata, T newdata)        |      更新一个对象数据      |           olddata为原对象数据，newdata为新对象数据           |
|                                                              |                            |                                                              |
|                                                              |                            |                                                              |
|                                                              |                            |                                                              |

### slip.mysql.util.FormateData

|                         方法                         |              摘要               |          注意事项           |
| :--------------------------------------------------: | :-----------------------------: | :-------------------------: |
| public static Object **toBean**(Map map, Class bean) | 将一个Map类型对象转换成实体类型 | bean参数为实体类的Class对象 |
|       public static <T> Map **toMap**(T bean)        | 将一个实体类型对象转换成Map类型 |                             |



