package logtest;

import org.apache.log4j.*;

public class Log4JExample01 {
    //日志记录器
    private static Logger LOGGER = LogManager.getLogger(UseLog4j.class);
    //循环次数
    private static long CYCLE = 102;
    //程序入口——主函数
    public static void main(String[]args){
        long startTime = System.currentTimeMillis();
        /**
         * 使用自定义的log4j的环境配置
         */

        /**
         * 一：定义日志信息的格式化方式，Log4J采用类似C语言中的printf函数的打印格式格式化日志信息，打印参数如下：
         * 1）%t 用来输出生成该日志事件的线程的名称
         * 2）%p 用于输出日志事件的优先级，即DEBUG，INFO，WARN，ERROR，FATAL
         * 3）%r 用于输出从layout（布局）的构建到日志事件创建所经过的毫秒数
         * 4）%c 用于输出日志事件的category（类别），通常就是所在类的全名
         * 5）%F 用于输出被发出日志记录请求，其中的文件名
         * 6）%d 用于输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，
         * 输出类似：20017年02月18日 22：10：28，921
         * 7）%L 用于输出日志事件的发生位置，即在代码中的行数。举例：10
         * 8）%l 用于输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)
         * 9）%% 用于输出％标志
         * 10）%M 用于输出打印该条日志的方法名
         * 11）%m 用于输出代码中指定的消息
         * 12）%n 用于输出一个回车换行符，Windows平台为“rn”，Unix平台为“n”
         *
         *
         * 当我们运行程序后发现，因为输出的日志信息长短不一，所以对不齐，看起来不好看，那么有什么法子呢？log4j也发现这个问题，并且为我们提供了格式修饰符
         * 格式修饰符：可以控制输出字段的最小字段宽度、最大字段宽度、字段对齐格式，如下所示：
         * 序号 格式修饰符    对齐方式       最小宽度            最大宽度            备注（对%c来使用格式修饰符，所以改变的是类别名称）
         *
         * 1）  %-20c         左对齐            20                none            用空格右垫，如果类别名称少于20个字符长
         * 2）  %20c         右对齐            20                none            用空格左垫，如果类别名称少于20个字符长
         * 3）  %.30c         左对齐            none            30                从开始截断，如果类别名称超过30个字符长
         * 4）  %-20.30c     左对齐            20                30                用空格右侧垫，如果类别名称短于20个字符。但是，如果类别名称长度超过30个字符，那么从开始截断。
         * 5）  %20.30c         右对齐            20                30                用空格左侧垫，如果类别名称短于20个字符。但是，如果类别名称长度超过30个字符，那么从开始截断。
         *
         */
        //试验，并查看日志格式化后的效果
        //String pattern = "[1]%t - [2]%p - [3]%r - [4]%c - [5]%F - [6]%d  - [7]%L - [8]%l - [9]%% - [10]%M  - [11]%m[12]%n"; //运行程序看看，是不是我们期待的日志输出样子
        //试验，并查看日志格式化后的效果
        String pattern = "[1]%-10p - [2]%10p - [3]%.3p  - [4]%-10.3p - [5]%10.3p 。 %n"; //运行程序看看，是不是我们期待的日志输出样子

        /**
         * 二：定义日志输出的风格样式，日志输出的风格主要有以下几种（通过官方文档我们会发现还有其他的）：
         * 1）org.apache.log4j.HTMLLayout（以HTML表格形式布局），
         * 2）org.apache.log4j.PatternLayout（可以灵活地指定布局模式，这个在我的实际工作中是最常用的），
         * 3）org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
         * 4）org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
         */
        Layout layout = new PatternLayout(pattern);

        /**
         * 三：定义日志输出的目的地，日志输出的目的地主要中以下几种（通过官方文档我们会发现还有好多种类的）：
         * 1）org.apache.log4j.ConsoleAppender（控制台），
         * 2）org.apache.log4j.FileAppender（文件），
         * 3）org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），
         * 4）org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件，这个在我的实际工作中也是最常用的），
         * 5）org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
         */
        Appender appender= new ConsoleAppender(layout);

        //配置日志输出的定义，主要有三点：1：输出什么级别的日志信息，2：将日志信息输出到那里，3：输出的日志以什么格式展示
        BasicConfigurator.configure(appender);

        /**
         * 日志输出的级别，主要有以下几种：
         * 1）ALL    各级包括自定义级别。
         * 2）TRACE    最详细的信息。一般这些信息只记录到日志文件中。自版本1.2.12[3]。
         * 3）DEBUG    流经系统的详细信息。一般这些信息只记录到日志文件中。
         * 4）INFO    令人感兴趣的运行时事件（启动/关闭）。一般这些信息将立即呈现在状态控制台上，因而要保守使用，并保持到最低限度。
         * 5）WARN    使用已过时的API，API的滥用，潜在错误，其他不良的或意外的运行时的状况（但不一定是错误的）。一般这些信息将立即呈现在状态控制台上。
         * 6）ERROR    其他运行时错误或意外情况。一般这些信息将立即呈现在状态控制台上。
         * 7）FATAL    导致应用程序提前终止的严重错误。一般这些信息将立即呈现在状态控制台上。
         * 8）OFF    最高级别，用于关闭日志记录。
         *
         * 日志的级别之间的大小关系如右所示：ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
         */
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
