package common;

import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteThreadMonitor implements Runnable {

    private WebDriver driver;
    private Thread t;

    public static Map monitorData = new ConcurrentHashMap(128);

    public RemoteThreadMonitor(WebDriver driver, Thread t) {
        this.driver = driver;
        this.t = t;
    }

    @Override
    public void run() {
        try {
            while (true) {

                if (!monitorData.isEmpty()) {
                    for (Object o : monitorData.entrySet()) {
                        //发送线程状态 （开始）
                        //判断线程是否还存活
                        if (!t.isAlive()) {
                            System.out.println(driver + "运行结束");
                        } else if (t.isAlive()) {
                            System.out.println(driver + "正在运行");
                        }

                        delData(o.getClass().getName());
                    }
                }else {
                    Thread.sleep(50L);
                }
            }
        } catch (Exception e) {
            // 发送线程状态 （有异常）
        } finally {
            // 发送结束状态（结束）
        }
    }

    public static void addData(String threadName, String data) {
        monitorData.put(threadName, data);
    }
    private static void delData(String threadName) {
        monitorData.remove(threadName);
    }
}
