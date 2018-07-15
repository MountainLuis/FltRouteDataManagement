package logtest;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;



public class UseLog4j {
    private static Logger LOGGER = LogManager.getLogger(UseLog4j.class);
    public  static void main(String[] args) {
//        BasicConfigurator.configure();
//        LOGGER.info("Hello, Log4j");
        UseLog4j ul = new UseLog4j();
//        ul.unLog();
        ul.withLog();
    }

    public void unLog(){
        //循环次数
        long CYCLE = 102;
        //程序入口——主函数
            long startTime = System.currentTimeMillis();
            for(int i=0;i<CYCLE;i++){
                if(i<100){
                    try{
                        System.out.println(new Person("godtrue",100/i,'M'));//打印对象的信息
                    }catch(Exception e){
                        System.out.println(i+"岁的小孩还不存在嘛！");//打印对象的信息
                    }finally{
                        System.out.println("现在大部分人的年龄都在0到100岁之间的!");//打印对象的信息
                    }
                }else{
                    System.out.println("我是一棵树，我今年活了"+i+"岁!哈哈，我厉害吧！");//打印对象的信息
                }
            }
            System.out.println("此程序的运行时间是："+(System.currentTimeMillis()-startTime));//打印程序运行的时间
    }

    public void withLog(){
        Logger LOGGER = LogManager.getLogger(UseLog4j.class);
        //循环次数
        long CYCLE = 102;
        //程序入口——主函数
        long startTime = System.currentTimeMillis();
            //自动快速地使用缺省Log4j环境
        BasicConfigurator.configure();
        for(int i=0;i<CYCLE;i++){
            if(i<100){
                try{
                        LOGGER.info(new Person("godtrue",100/i,'M'));//打印对象的信息
                    }catch(Exception e){
                        LOGGER.error(i+"岁的小孩还不存在嘛！");//打印对象的信息
                    }finally{
                        LOGGER.warn("现在大部分人的年龄都在0到100岁之间的!");//打印对象的信息
                    }
                }else{
                    LOGGER.info("我是一棵树，我今年活了"+i+"岁!哈哈，我厉害吧！");//打印对象的信息
                }
            }
            LOGGER.debug("此程序的运行时间是："+(System.currentTimeMillis()-startTime));//打印程序运行的时间
    }



}
class Person {
    //姓名
    private String name;
    //性别
    private char sex;
    //年龄
    private int age;

    //有参构造函数
    public Person(String name, int age, char sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    //无惨构造函数
    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char isSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }
}