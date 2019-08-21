package util;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


public class KeyBoard{

    //ENTER键
    public void pressEnter(WebDriver webDriver){
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ENTER).perform();
    }

    //DELETE键
    public void pressDelete(WebDriver webDriver){
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.DELETE).perform();
    }

    //快捷键Shift+D
    public void pressShiftD(WebDriver webDriver){
        Actions actions = new Actions(webDriver);
        //以下两种方法都可以
//		actions.sendKeys(Keys.SHIFT,"d").perform();
        //网上说使用keyDown时，模拟按下不释放，可利用后面的批量拖动功能验证
        actions.keyDown(Keys.SHIFT).sendKeys("d").keyUp(Keys.SHIFT).perform();
    }

    //ESC
    public void pressEsc(WebDriver webDriver){
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();
    }
}
