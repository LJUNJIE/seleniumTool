package common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteOpenBrowser {

    private final  static Logger logger = LoggerFactory.getLogger(OpenBrowser.class);
    private static WebDriver driver;

    public  WebDriver openBrowser(String remoteServer) {
        try {
//            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//            desiredCapabilities.setBrowserName("chrome");
//
//            desiredCapabilities.chrome();
//            driver = new RemoteWebDriver(new URL(remoteServer),desiredCapabilities);

            ChromeOptions chromeOptions = new ChromeOptions();
            //headless静默模式
            //--kiosk窗口最大化
            //disable-infobars，非静默模式下，关闭浏览器提示"窗口被自动化软件运行"
            //--window-size=1366,768
            chromeOptions.addArguments("disable-infobars", "--kiosk", "--window-size=1366,768");
            System.out.println("远程机器"+remoteServer);
            driver = new RemoteWebDriver(new URL(remoteServer),chromeOptions);
        }catch (MalformedURLException e){
            logger.error("连接远程机器失败:"+e.getMessage(),e);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        return driver;
    }

    public static WebDriver getDriver(){
        return driver;
    }

}