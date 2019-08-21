package util;

import common.FindElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DownloadReport {
    private static WebDriver driver;
    private final  static Logger logger = LoggerFactory.getLogger(DownloadReport.class);
    private String os = System.getProperty("os.name").toLowerCase();

    public void downLoad(){
        try {

            logger.info("启动浏览器");

            if (os.contains("mac")) {
                //mac
                System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
                ChromeOptions chromeOptions = new ChromeOptions();
                //headless静默模式
                //--kiosk窗口最大化
                //disable-infobars，非静默模式下，关闭浏览器提示"窗口被自动化软件运行"
                //--window-size=1366,768
                chromeOptions.addArguments("--kiosk","headless");

                String downloadFilepath = "/work";
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("download.default_directory",downloadFilepath);
                prefs.put("profile.default_content_settings.popups",0);
                chromeOptions.setExperimentalOption("prefs", prefs);
                driver = new ChromeDriver(chromeOptions);

                ((ChromeDriver) driver).getSessionStorage();

//                DesiredCapabilities cap = DesiredCapabilities.chrome();
//                cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//                cap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
//                driver = new ChromeDriver(cap);


//                driver = new ChromeDriver(chromeOptions);
            } else if (os.contains("win")) {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-infobars", "--start-maximized", "headless");//关闭Chrome 正受到自动测试软件的控制提示
                System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
                driver = new ChromeDriver(chromeOptions);
            } else {
                //Linux
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-infobars", "--no-sandbox", "--start-maximized", "headless", "--single-process", "--disable-dev-shm-usage", "--disable-gpu");//关闭Chrome 正受到自动测试软件的控制提示
                System.setProperty("webdriver.chrome.driver", "lib/chromedriverlinux");
                driver = new ChromeDriver(chromeOptions);
            }

            logger.info("打开地址");
            driver.get("http://192.168.0.140:8888/datak-web/publishShow?viewId=1231");

            WebDriverWait wait = new WebDriverWait(driver, 30);//新建等待对象
            FindElement fe = new FindElement(driver, wait);

            fe.findE("username",1).sendKeys("admin");
            fe.findE("password",1).sendKeys("654321");
            fe.findE("loginSubmit",1).click();

            fe.findE("//a[contains(@ng-click,'ExportWarZipDialog()')]", 2).click();
            fe.findE("//input[contains(@ng-model,'needCas')]",2).click();
            logger.info("点击下载");
            fe.findE("//button[contains(@ng-click,'ExportWarZip()')]",2).click();
            Thread.sleep(50000);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

    }
}
