package common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FindElement {

    private final  static Logger logger = LoggerFactory.getLogger(FindElement.class);

    private WebDriver wd;
    //private WebDriverWait wdw;
    private WebElement foo ;

    public FindElement(WebDriver d, WebDriverWait w) throws Exception{
        this.wd=d;
        //this.wdw=w;
    }

    /**
     *
     * @param path 元素
     * @param type 元素获取方法
     * @return
     */
    public WebElement findE(String path,int type) {
        try{
            switch(type){
                case 1:
                    waitForLoad(By.id(path));
                    foo = wd.findElement(By.id(path));

                    break;
                case 2:
                    waitForLoad(By.xpath(path));
                    foo = wd.findElement(By.xpath(path));

                    break;
                case 3:
                    waitForLoad(By.linkText(path));
                    foo = wd.findElement(By.linkText(path));

                    break;
                case 4:
                    waitForLoad(By.className(path));
                    foo = wd.findElement(By.className(path));

                    break;
                case 5:
                    waitForLoad(By.name(path));
                    foo = wd.findElement(By.name(path));

                    break;
            }
        }
        catch(Exception e){
            if(type==1){
                logger.error("元素(ID)'"+path+"'找不到");
            }
            if(type==2){
                logger.error("元素(XPATH)'"+path+"'找不到");
            }
            if(type==3){
                logger.error("元素(linkText)'"+path+"'找不到");
            }
            if(type == 4) {
                logger.error("元素(className)\'" + path + "\'找不到");
            }
            if(type == 5) {
                logger.error("元素(name)\'" + path + "\'找不到");
            }
            this.wd.quit();

            return null;
        }
        return foo;
    }

    public void waitForLoad(final By elementBy) {
        WebDriverWait wait = (new WebDriverWait(wd, 30));//单位秒
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                boolean loadcomplete = d.findElement(elementBy).isDisplayed();
                return loadcomplete;
            }
        });
    }
}
