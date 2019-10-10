package slip.mysql.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个用来将实体类和Map类型相互转换的工具类，
 * 类方法包括toBean()、toMap()两个转换方法，都是静态方法可以直接调用
 */
public class FormatData {
    /**
     * 将一个Map对象转换成一个实体类对象
     * @param map 储存有数据的Map对象
     * @param bean 实体类的Class对象
     * @return 一个实体类对象
     */
    public static Object toBean(Map map, Class bean) {
        Object object = null;
        try {
            object = bean.newInstance();
            Field[] fields = bean.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                for (Object key : map.keySet()) {
                    if (fields[i].getName().equals(key)) {
                        fields[i].set(object, map.get(key));
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 将一个实体类对象转换成一个Map对象
     * @param bean 实体类的c对象
     * @return 储存实体对象数据的Map对象
     */
    public static <T> Map toMap(T bean) {
        Map map = new HashMap();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                map.put(fields[i].getName(), fields[i].get(bean));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
