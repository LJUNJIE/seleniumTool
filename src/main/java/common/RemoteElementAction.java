package common;

import util.*;
import com.relevantcodes.extentreports.ExtentReports;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class RemoteElementAction implements Runnable{
    private final static Logger logger = LoggerFactory.getLogger(ElementAction.class);

    private CountDownLatch countDownLatch;
    //远程机器URL
    private String remoteServer;
    //xml文件中的name
    private String xmlTestName;

    public RemoteElementAction(String remoteServer,CountDownLatch countDownLatch, String xmlTestName) {
        this.remoteServer = remoteServer;
        this.countDownLatch = countDownLatch;
        this.xmlTestName = xmlTestName;
    }

    private WebDriver driver;

    @Override
    public void run() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        LoadProperties loadProperties = new LoadProperties();
        String uploadFile = loadProperties.loadProperties("uploadFile");

        ExtenseReport extenseReport = new ExtenseReport();
        ExtentReports extent = extenseReport.createReport(xmlTestName);

        /*
        testName:用例名称，sheetName
        testComment:测试结果
         */
        String testName = null;
        String testComment = null;

        int i;//sheet的第i行，从0开始
        String step = null;//步骤
        /*
        type：元素获取方法
        ename：元素
        action：操作方法
        evalue：元素值
        snap：截图
         */
        String sheetName;
        String type, ename = null, action, evalue, snap;
        String snapFile;//截图的文件名
        WebElement we;

        KeyBoard keyBoard = new KeyBoard();
        LoadExcUtil loadExcUtil = new LoadExcUtil();
        String[] exc = loadExcUtil.LoadExc();
        RemoteOpenBrowser remoteOpenBrowser = new RemoteOpenBrowser();
        SnapShot snapShot = new SnapShot();

        logger.info("=========脚本启动=========");
        //循环执行脚本
        for (int s = 0; s < exc.length; s++) {

            logger.info("开始执行脚本：" + exc[s]);

            int countSheet = loadExcUtil.countSheet(exc[s]);
            //循环执行脚本中的sheet
            for (int k = 0; k < countSheet; k++) {
                try {
                    sheetName = loadExcUtil.getSheetName(exc[s],k);
                    //用例名称
                    testName = sheetName;
                    driver = remoteOpenBrowser.openBrowser(remoteServer);
                    //打开地址
                    WebDriverWait wait = new WebDriverWait(driver, 30);//新建等待对象
                    FindElement fe = new FindElement(driver, wait);
                    String url = loadExcUtil.readValue(1, 1, exc[s], k);
                    driver.get(url);

                    //鼠标事件初始//////////////////////////////////////////////////
                    Actions mouse = new Actions(driver);
                    Select slt;//select选择器

                    i = 4;//从第5行开始填写执行内容

                    while (true) {
                        //读取每行的值
                        step = loadExcUtil.readValue(0, i, exc[s], k);
                        type = loadExcUtil.readValue(1, i, exc[s], k);
                        ename = loadExcUtil.readValue(2, i, exc[s], k);
                        action = loadExcUtil.readValue(3, i, exc[s], k);
                        evalue = loadExcUtil.readValue(4, i, exc[s], k);
                        snap = loadExcUtil.readValue(5, i, exc[s], k).toLowerCase();
                        we = null;//清除上次循环的元素
                        Thread.sleep(500);
                        logger.info(xmlTestName+" : "+step + "步开始");

                        if ("end".equals(step)) {
                            //如果读取到end则结束脚本
                            logger.info(xmlTestName+" : "+sheetName + "执行完成");
                            break;
                        }
                        if ("".equals(step)) {
                            //如果读取到空值则结束脚本，防止死循环
                            logger.info(xmlTestName+" : "+sheetName + "脚本完成");
                            break;
                        }

                        //查找元素****************************************

                        switch (type) {
                            case "id":
                                we = fe.findE(ename, 1);
                                break;

                            case "xpath":
                                we = fe.findE(ename, 2);
                                break;

                            case "linkText":
                                we = fe.findE(ename, 3);
                                break;

                            case "className":
                                we = fe.findE(ename, 4);
                                break;

                            case "name":
                                we = fe.findE(ename, 5);
                                break;
                        }

                        switch (action) {
                            case "sleep":
                                int slp = Integer.valueOf(evalue);
                                Thread.sleep(slp);
                                logger.info(xmlTestName+" : "+"sleep:" + evalue + "毫秒");
                                i++;
                                continue;

                            case "get":
                                driver.get(evalue);
                                break;

                            case "clear":
                                driver.get(evalue);
                                break;

                            case "input":
                                we.clear();
                                we.sendKeys(evalue);
                                break;

                            case "click":
                                mouse.click(we).perform();//点击元素
                                break;

                            case "doubleClick":
                                mouse.doubleClick(we).perform();//双击元素
                                break;

                            case "frame":
                                if ("defaultContent".equals(ename)) {
                                    driver.switchTo().defaultContent();//跳转到主窗口

                                } else {
                                    if ("xpath".equals(type)) {
                                        driver.switchTo().frame(we);//跳转到该窗口
                                    } else {
                                        driver.switchTo().frame(ename);//跳转到该窗口
                                    }
                                }
                                break;

                            case "equals":
                                if (we.getText().equals(evalue)) {
                                    logger.info(xmlTestName+" : "+"步骤： " + step + " ,字符串比较一致");
                                } else {
                                    logger.info(xmlTestName+" : "+"步骤： " + step + " ,字符串比较不一致");
                                }
                                break;

                            case "mouseover":
                                mouse.moveToElement(we).perform();//鼠标移动到元素上方
                                break;

                            /*
				            以鼠标当前位置或者 (0,0) 为中心开始移动到 (x,y) 坐标轴，E列填值 “x,y”，
				            x向右填正数，向左填负数，y向下填正数，向上填负数，
				            如果这两个值大于当前屏幕的大小，鼠标只能移到屏幕最边界的位置同时抛出MoveTargetOutOfBoundsExecption 的异常；
				            */
                            case "moveBy":
                                String[] moveBy = evalue.split(",");
                                mouse.moveByOffset(Integer.valueOf(moveBy[0]), Integer.valueOf(moveBy[1]));
                                break;

                            case "select":
                                slt = new Select(we);
                                slt.selectByVisibleText(evalue);
                                break;

                            case "newblank":
                                for (String winHandle : driver.getWindowHandles()) {
                                    //System.out.println(winHandle);
                                    driver.switchTo().window(winHandle);
                                }
                                break;

				            /*
				            模拟键盘事件
				            在mac上调用keyBoardUtil的方法会切换屏幕启动java程序，win未知
				            */
                            case "enter":
                                keyBoard.pressEnter(driver);
                                break;

				            /*
				            有些输入框input，输入报错Element must be user-editable in order to clear it
				            解决办法，先执行we.sendKeys(Keys.DELETE)，再input
				            */
                            case "delete":
                                we.sendKeys(Keys.DELETE);
                                break;

                            case "esc":
                                keyBoard.pressEsc(driver);
                                break;

                            case "scrollToTop"://移动滚动条直到该元素与当前窗口的“顶部”对齐
                                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", we);
                                break;

                            case "scrollToBottom"://移动滚动条直到该元素与当前窗口的“底部”对齐
                                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", we);
                                break;

                            case "uploadBy"://文件上传，脚本中元素值填文件名，文件放在upload文件夹中
                                String uploadEvalue = System.getProperty("user.dir") + "/upload/" + evalue;
                                we.sendKeys(uploadEvalue);
                                break;

                            case "upload"://文件上传，脚本中元素值填文件的绝对路径
                                we.sendKeys(evalue);
                                break;

                            case "uploadFile"://文件上传，文件绝对路径写在配置项的uploadFile，脚本中元素值不填
                                we.sendKeys(uploadFile);
                                break;

                            case "alertAccept"://浏览器弹窗点击确定
                                driver.switchTo().alert().accept();
                                break;

                            case "alertDismiss"://浏览器弹窗点击取消
                                driver.switchTo().alert().dismiss();
                                break;

                            case "dragDown"://纵向拖拽元素，正数：向下，负数：向上
                                int yOffSet = Integer.valueOf(evalue);
                                new Actions(driver).dragAndDropBy(we, 0, yOffSet).build().perform();
                                break;

                            case "dragHor"://横向拖拽元素，正数：向右，负数：向左
                                int xOffSet = Integer.valueOf(evalue);
                                new Actions(driver).dragAndDropBy(we, xOffSet, 0).build().perform();
                                break;

                            case "dragBy"://自由拖拽元素，以坐标为目的,以要拖动的元素的左上角为准(0,0)，x向右填正数，向左填负数，y向下填正数，向上填负数
                                String[] dragBy = evalue.split(",");
                                new Actions(driver).dragAndDropBy(we, Integer.valueOf(dragBy[0]), Integer.valueOf(dragBy[1])).build().perform();
                                break;

                            case "drag"://拖动元素到另一元素上
                                WebElement web = fe.findE(evalue, 2);
                                new Actions(driver).dragAndDrop(we, web).build().perform();
                                break;

                            case "executeJs"://执行js代码
                                ((JavascriptExecutor) driver).executeScript(evalue);
                                break;

                        }

                        i++;//读取下一步

                        //该步骤截图
                        if ("y".equals(snap)) {
                            String date = df.format(new Date());
                            Thread.sleep(2000);//等待页面加载完再截图
                            snapFile = xmlTestName+" : "+"snap_" + exc[s] + "_step" + step + "_" + date + ".png";
                            snapShot.snapShot((TakesScreenshot) driver, snapFile);
                        }
                    }

                    driver.quit();
                    logger.info(xmlTestName+" : "+"表格:"+ sheetName +"完成，关闭浏览器");
                    testComment = "PASS";
                        //测试报告中写入sheet的测试结果
                        extenseReport.startTestPass(extent,testName,testComment);
                }catch (Exception e) {
                    logger.error(xmlTestName+" : "+"第" + step + "步发生异常");
                    logger.error(e.getMessage(),e);
                    driver.quit();
                    logger.info(xmlTestName+" : "+"执行报错，关闭浏览器");

                    testComment = "第 " + step + " 步：元素 ["+ename.replaceAll("\'","\\\\'")+"] 出现异常";
                        //测试报告中写入sheet的测试结果
                        extenseReport.startTestFail(extent,testName,testComment);
                }
            }
            logger.info(xmlTestName+" : "+exc[s] + "执行完成");
        }
        logger.info(xmlTestName+" : "+"=========脚本完成=========");

        //更新统计测试结果数量
        //更新模板中sql的表名
            //输出最终的html格式测试报告
            extenseReport.extentFlush(extent);
        logger.info(xmlTestName+" 输出报告完成" );

        countDownLatch.countDown();
        long count=countDownLatch.getCount();
        System.out.println("当前任务数："+count);
//        System.exit(0);
    }

}