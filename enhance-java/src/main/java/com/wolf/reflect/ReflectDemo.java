package com.wolf.reflect;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectDemo {
    private static String className = null;
    private static Class personClass = null;

    /**
     * 获取公有的构造函数
     *
     * @throws Exception
     */
    private static void getPublicConstructor() throws Exception {
        Constructor constructor = personClass.getConstructor(Long.class, String.class);
        Person person = (Person) constructor.newInstance(100L, "zhangsan");
        System.out.println(person.getId());
        System.out.println(person.getName());
        System.out.println("--------------\n");
    }

    /**
     * 获取私有的构造函数
     *
     * @throws Exception
     */
    private static void getPrivateConstructor() throws Exception {
        Constructor privateCons = personClass.getDeclaredConstructor(String.class);
        privateCons.setAccessible(true); //强制取消Java权限检测
        Person person = (Person) privateCons.newInstance("zhangsan");
        System.out.println(person.getName());
        System.out.println("--------------\n");
    }

    /**
     * 获取公有的成员变量
     *
     * @throws Exception
     */
    private static void getPublicField() throws Exception {
        Constructor constructor = personClass.getConstructor(Long.class, String.class);
        Object obj = constructor.newInstance(100L, "zhangsan");

        Field field = personClass.getField("name");
        field.set(obj, "lisi");
        System.out.println(field.get(obj));
        System.out.println("--------------\n");
    }

    /**
     * 获取私有的成员变量
     *
     * @throws Exception
     */
    private static void getPrivateField() throws Exception {
        Constructor constructor = personClass.getConstructor(Long.class);
        Object obj = constructor.newInstance(100L);

        Field field = personClass.getDeclaredField("id");
        field.setAccessible(true); //强制取消Java权限检测
        field.set(obj, 10L);
        System.out.println(field.get(obj));
        System.out.println("--------------\n");
    }

    /**
     * 获取公有的成员函数
     *
     * @throws Exception
     */
    private static void getPublicMethod() throws Exception {
        System.out.println(personClass.getMethod("toString"));

        Object obj = personClass.newInstance();
        Object object = personClass.getMethod("toString").invoke(obj);
        System.out.println(object);
        System.out.println("--------------\n");
    }

    /**
     * 获取私有的成员函数
     *
     * @throws Exception
     */
    private static void getPrivateMethod() throws Exception {
        Object obj = personClass.newInstance();

        Method method = personClass.getDeclaredMethod("getSomeThing");
        method.setAccessible(true);
        Object value = method.invoke(obj);
        System.out.println(value);
        System.out.println("--------------\n");
    }

    private static void otherMethod() throws ClassNotFoundException {
        //获取加载当前Class文件的类加载器对象
        System.out.println(personClass.getClassLoader());

        //获取类实现的所有接口
        Class[] interfaces = personClass.getInterfaces();
        for (Class inter : interfaces) {
            System.out.println(inter);
        }

        //获取类的直接父类
        System.out.println(personClass.getGenericSuperclass());

        //getResourceAsStream()获取指定文件的输入流
        //若path以'/'开头，则从classpath下获取资源；若不以'/'开头，则默认从该类所在包路径下获取资源。
        //只是通过path构造绝对路径，最终还是由ClassLoader获取资源。
        System.out.println(personClass.getResourceAsStream("/test.properties"));
        System.out.println(personClass.getResourceAsStream("test.properties"));

        //判断当前的Class对象是否是数组
        System.out.println(personClass.isArray());
        System.out.println(new String[3].getClass().isArray());

        //判断当前的Class对象是否是枚举
        System.out.println(personClass.isEnum());
        System.out.println(Class.forName("com.wolf.reflect.Person").isEnum());

        //判断当前的Class对象是否是接口
        System.out.println(personClass.isInterface());
        System.out.println(Class.forName("java.util.List").isInterface());
    }

    public static void main(String[] args) throws Exception {
        className = "com.wolf.reflect.Person";
        personClass = Class.forName(className);

        //获取Class文件对象
        System.out.println(personClass);
        System.out.println(Person.class);
        System.out.println("--------------\n");

        //创建Class文件对象实例（底层调用无参构造）
        System.out.println(personClass.newInstance());
        System.out.println("--------------\n");

        //获取公有的构造函数
        getPublicConstructor();

        //获取私有的构造函数
        getPrivateConstructor();

        //获取公有的成员变量
        getPublicField();

        //获取私有的成员变量
        getPrivateField();

        //获取公有的成员函数
        getPublicMethod();

        //获取私有的成员函数
        getPrivateMethod();

        otherMethod();
    }
}
