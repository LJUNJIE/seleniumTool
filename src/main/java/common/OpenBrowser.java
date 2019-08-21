package common;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LoadProperties;

import java.net.URL;


public class OpenBrowser {

    WebDriver driver;

    private final  static Logger logger = LoggerFactory.getLogger(OpenBrowser.class);

    //判断操作系统
    private String os = System.getProperty("os.name").toLowerCase();

    LoadProperties loadProperties = new LoadProperties();
    String isHeadless = loadProperties.loadProperties("headless");

    public WebDriver openBrowser(int explorer) {
        try {

            switch (explorer) {
                case 1:
                    //启动火狐2
//                    System.out.println("启动火狐浏览器中。。。");
//                    ProfilesIni allProfiles = new ProfilesIni();
//                    FirefoxProfile profile = allProfiles.getProfile("default");
//                    driver = new FirefoxDriver(profile);
                    break;
                case 2:
                    //启动IE,IEDriverServer下载地址http://www.seleniumhq.org/download/
//                    logger.info("启动IE浏览器");
//                    Properties props = System.getProperties();
//                    if (props.getProperty("os.arch").equals("amd64")) {
//                        System.setProperty("webdriver.ie.driver", "lib/IEDriverServer_64.exe");//64bit-IE启动
//                    } else {
//                        System.setProperty("webdriver.ie.driver", "lib/IEDriverServer_32.exe");//32bit-IE启动
//                    }
//                    DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();//设置安全性功能
//                    ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
//                    driver = new InternetExplorerDriver(ieCapabilities);
                    break;
                case 3:
                    if (os.contains("mac")) {
                        System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
                        logger.info("启动chrome浏览器");
                        ChromeOptions chromeOptions = new ChromeOptions();
                        //headless静默模式
                        //--kiosk窗口最大化
                        //disable-infobars，非静默模式下，关闭浏览器提示"窗口被自动化软件运行"
                        //--window-size=1366,768
                        if(isHeadless.equals("true")){
                            chromeOptions.addArguments("--kiosk","headless","--disable-gpu");
                        }else {
                            chromeOptions.addArguments("disable-infobars", "--kiosk");
                        }
                        driver = new ChromeDriver(chromeOptions);
                        break;
                    } else if(os.contains("win")) {
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("disable-infobars","--start-maximized");//关闭Chrome 正受到自动测试软件的控制提示
                        if(isHeadless.equals("true")){
                            chromeOptions.addArguments("headless");
                        }
                        logger.info("启动chrome浏览器");
                        System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
                        driver = new ChromeDriver(chromeOptions);
                        break;
                    }else{
                        //linux
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("disable-infobars","--no-sandbox","--start-maximized","headless","--single-process","--disable-dev-shm-usage","--disable-gpu");
                        logger.info("启动chrome浏览器");
                        System.setProperty("webdriver.chrome.driver", "lib/chromedriverlinux");
                        driver = new ChromeDriver(chromeOptions);
                        break;
                    }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return driver;
    }
}