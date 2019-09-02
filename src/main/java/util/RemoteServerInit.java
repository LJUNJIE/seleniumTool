package util;

import common.RemoteElementAction;
import common.RemoteOpenBrowser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteServerInit {

    public final static Logger logger = LoggerFactory.getLogger(RemoteServerInit.class);
    //xml中远程服务器的地址
//    public static String remoteServer;
    //存储读取配置文档
    private static Document DOCUMENT;
    static File file = new File("cfg/RemoteServerInfo.xml");

    //driver，thread放到map中
    private static Map<WebDriver,Thread> threadMap = new HashMap<>();

    /**
     * 读取配置文件，返回文件
     * @param file
     * @return Document
     */
    public static Document loadXmlFile(File file) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(file);
        }catch (DocumentException e){
            logger.error("读取xml配置文件失败："+e.getMessage(),e);
        }
        return document;
    }

    /**
     * 获取远程服务器的所有信息，remote-server标签，status为on的
     * @param document
     * @return
     */
    public static List<Element> getAllInfo(Document document){
        List<Element> allInfo = new ArrayList<Element>();
        try {
            List<Element> list = document.getRootElement().elements("remote-server");
            Iterator<Element> it = list.iterator();
            while (it.hasNext()){
                Element element = (Element) it.next();
                if(element.attributeValue("status").equals("on")){
                    allInfo.add(element);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return allInfo;
    }

    /**
     * 多线程启动程序，同时将启动的driver，thread放到map中，用于线程监控
     * @param list getAllInfo获取的list
     */
    public static void launch(List<Element> list){
        try {
            Iterator<Element> it = list.iterator();
            CountDownLatch countDownLatch=new CountDownLatch(list.size());
            ExecutorService executor= Executors.newFixedThreadPool(list.size());
            while (it.hasNext()){
                Element e = it.next();
                //获取xml中远程服务器地址
                String remoteServer = e.getText();
                String xmlTestName = e.attributeValue("name");
                String script = e.attributeValue("script");
                RemoteElementAction remoteElementAction = new RemoteElementAction(remoteServer,countDownLatch,xmlTestName,script);
                executor.submit(remoteElementAction);
            }

            countDownLatch.await();

            executor.shutdown();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        System.exit(-1);
    }
}
