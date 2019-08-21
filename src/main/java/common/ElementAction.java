package common;

import Bean.TestDetailReport;
import Bean.TestReport;
import org.testng.annotations.Test;
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

@Test
public class ElementAction {

    private static WebDriver driver;
    private final  static Logger logger = LoggerFactory.getLogger(ElementAction.class);

    public void run() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        LoadProperties loadProperties = new LoadProperties();
        String uploadFile = loadProperties.loadProperties("uploadFile");
        String useDataK = loadProperties.loadProperties("useDataK");

        DataBaseSource dataBaseSource = new DataBaseSource();

        ExtenseReport extenseReport = new ExtenseReport();
//        ExtentReports extent = extenseReport.createReport();
        ExtentReports extent = null;
        extent = extenseReport.createReport("testReportName");


        /*
        pass:测试用例通过数量；fail:测试用例失败数量；Excel中的sheet即测试用例；
        pass + fail = 一个Excel中sheet的数量;
        beginTime,endTime:一个测试用例的开始结束时间,
        testName:用例名称，sheetName
        testComment:测试结果
        succ:sheet的测试结果成功/失败
         */
        int pass = 0;
        int fail = 0;
        String beginTime = null;
        String endTime = null;
        String testName = null;
        String testComment = null;
        String reportTableName = null;
        boolean succ = true;

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
        int explorer;

        KeyBoard keyBoard = new KeyBoard();
        LoadExcUtil loadExcUtil = new LoadExcUtil();
        String[] exc = loadExcUtil.LoadExc();
        OpenBrowser openBrowser = new OpenBrowser();
        SnapShot snapShot = new SnapShot();

        //用于报表拖拽元素,dataS部分有注释
        int countCol = 0;
        int countRow = 0;
        String col = "ch_" + Integer.toString(countCol);
        String row = "rh_" + Integer.toString(countRow);
        int add = 3;

        //使用dataK写报告
        if(useDataK.equals("true")){
            //获取测试报告表名，生成表
            reportTableName = dataBaseSource.getReportTableName();
            dataBaseSource.createReportTable(reportTableName);

            String lastReportTableName = dataBaseSource.getLastReportTableName();
            dataBaseSource.updateReport(lastReportTableName,reportTableName);
        }
        logger.info("=========脚本启动=========");
        //循环执行脚本
        for (int s = 0; s < exc.length; s++) {

            logger.info("开始执行脚本：" + exc[s]);

                int countSheet = loadExcUtil.countSheet(exc[s]);
                //循环执行脚本中的sheet
                for (int k = 0; k < countSheet; k++) {
                try {
                    beginTime = df.format(new Date());
                    sheetName = loadExcUtil.getSheetName(exc[s],k);
                    //用例名称
                    testName = sheetName;
                    explorer = Integer.valueOf(loadExcUtil.readValue(1, 0, exc[s], k));
                    driver = openBrowser.openBrowser(explorer);

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
                        logger.info(step + "步开始");

                        if ("end".equals(step)) {
                            //如果读取到end则结束脚本
                            logger.info(sheetName + "执行完成");
                            break;
                        }
                        if ("".equals(step)) {
                            //如果读取到空值则结束脚本，防止死循环
                            logger.info(sheetName + "脚本完成");
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
                                logger.info("sleep:" + evalue + "毫秒");
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
                                    logger.info("步骤： " + step + " ,字符串比较一致");
                                } else {
                                    logger.info("步骤： " + step + " ,字符串比较不一致");
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

                            //以下是用于dataS的元素操作********************************************

                            case "dragToCol"://拖拽元素到列，下一个列框id加1
                                WebElement web1 = fe.findE(col, 1);
                                new Actions(driver).dragAndDrop(we, web1).perform();
                                countCol++;
                                col = "ch_" + Integer.toString(countCol);
                                break;

                            case "dragToRow"://拖拽元素到行
                                WebElement web2 = fe.findE(row, 1);
                                new Actions(driver).dragAndDrop(we, web2).perform();
                                countRow++;
                                row = "rh_" + Integer.toString(countRow);
                                break;

                            case "dragToExp"://配置新表达式，拖到同一个框，新增一个表达式A+B后，下一个列框id加3
                                countCol--;
                                col = "ch_" + Integer.toString(countCol);
                                WebElement web3 = fe.findE(col, 1);
                                new Actions(driver).dragAndDrop(we, web3).perform();
                                countCol = countCol + 3;
                                col = "ch_" + Integer.toString(countCol);
                                break;

                            case "addToExp"://为表达式添加一个因子,每增加一个因子，下一个列框id加1
                                int tmp = countCol;
                                countCol = countCol - add;
                                col = "ch_" + Integer.toString(countCol);
                                WebElement web4 = fe.findE(col, 1);
                                new Actions(driver).dragAndDrop(we, web4).perform();
                                countCol = tmp + 1;
                                add++;
                                col = "ch_" + Integer.toString(countCol);
                                break;

                            case "dragToCon"://拖拽元素到固定条件
                                WebElement web5 = fe.findE("conditionBaseGroup", 1);
                                new Actions(driver).dragAndDrop(we, web5).perform();
                                break;

                            case "dragToSin"://拖拽元素到指标条件
                                WebElement web6 = fe.findE("zbEditSingleCondBase", 1);
                                new Actions(driver).dragAndDrop(we, web6).perform();
                                break;

                            //以下是用于dataL的元素操作*****************************************
                            case "dragToColumn":
                                WebElement web7 = fe.findE("column-Group", 1);
                                new Actions(driver).dragAndDrop(we, web7).perform();
                                break;

                            //以下用于dataK的元素操作*******************************************
                            case "dragChart"://拖拽图表，excel元素值填像素数量，等同于上面的dragDown
                                new Actions(driver).dragAndDropBy(we, 0, Integer.valueOf(evalue)).build().perform();
                                break;

                            //拖拽元数据到系列
                        /*选择数据表模式，自助分析实力模式，系列的id为collapseColumn，
				        选择sql模式，excel文件，API模式，系列的id为collapseColumn1，
				        理论上用xpath的contains,start-with方法可以都定位到，实际操作不行
				        虽然页面只显示一个框，但是会把两种情况的代码都写出来，导致定位到的有两个元素，因此分两种操作
                        */

                            case "dragToSeriesByTable"://数据库表，自助分析实例模式
                                WebElement web9 = fe.findE("//div[contains(@id,\"collapseColumn\")]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web9).perform();
                                break;

                            case "dragToSeriesSecByTable"://用于双轴图的次轴
                                WebElement web10 = fe.findE("//*[@id=\"collapseColumnSec\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web10).perform();
                                break;

                            case "dragToSeries"://SQL模式，EXCEL模式，API模式
                                WebElement web11 = fe.findE("//*[@id=\"collapseColumn1\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web11).perform();
                                break;

                            case "dragToSeriesSec"://用于双轴图的次轴
                                WebElement web12 = fe.findE("//*[@id=\"collapseColumn2\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web12).perform();
                                break;

                            //拖拽元数据到分类
                            /*选择数据表模式，自助分析实例模式下，分类的id为collapseRow，
				            选择sql模式，excel文件，API模式，系列的id为collapseRow1，
				            理论上用xpath的contains,start-with方法可以都定位，可实际操作不行，
				            虽然页面只显示一个框，但是会把两种情况的代码都写出来，导致定位到的有两个元素，因此分两种操作
				            */
                            case "dragToVerbByTable"://数据库表，自助分析实例
                                WebElement web13 = fe.findE("//*[contains(@id,\"collapseRow\")]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web13).perform();
                                break;

                            case "dragToVerb"://SQL模式，EXCEL模式，API模式
                                WebElement web14 = fe.findE("//*[contains(@id,\"collapseRow1\")]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web14).perform();
                                break;

                            case "dragToFilter"://拖拽元数据到条件字段
                                WebElement web15 = fe.findE("//*[@id=\"collapseFilter\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web15).perform();
                                break;

                            case "dragToTime"://拖拽元数据到时间过滤字段
                                WebElement web16 = fe.findE("//*[@id=\"collapseTime\"]/div/div[4]", 2);
                                new Actions(driver).dragAndDrop(we, web16).perform();
                                break;

                            case "dragToOrder"://拖拽元数据到排序字段
                                WebElement web17 = fe.findE("//*[@id=\"collapseOrder\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web17).perform();
                                break;

                            case "deleteChart"://删除图表
                                keyBoard.pressShiftD(driver);
                                break;

                            case "collapseColumnLine"://地图飞线系列
                                WebElement web18 = fe.findE("//*[@id=\"collapseColumn-line\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web18).perform();
                                break;

                            case "collapseRowLineStart"://地图飞线起始字段
                                WebElement web19 = fe.findE("collapseRow-start", 1);
                                new Actions(driver).dragAndDrop(we, web19).perform();
                                break;

                            case "collapseRowLineEnd"://地图飞线结束字段
                                WebElement web20 = fe.findE("collapseRow-end", 1);
                                new Actions(driver).dragAndDrop(we, web20).perform();
                                break;

                            case "collapseColumnScatter"://地图散点系列，数据库表模式，自助分析结果
                                WebElement web21 = fe.findE("collapseColumn-scatter", 1);
                                new Actions(driver).dragAndDrop(we, web21).perform();
                                break;

                            case "collapseRowScatter"://地图散点分类，数据库表模式，自助分析结果
                                WebElement web22 = fe.findE("collapseRow-scatter", 1);
                                new Actions(driver).dragAndDrop(we, web22).perform();
                                break;

                            case "collapseColumnScatter2"://地图散点系列，EXCEL模式下，API模式，SQL模式
                                WebElement web23 = fe.findE("collapseColumn1-scatter", 1);
                                new Actions(driver).dragAndDrop(we, web23).perform();
                                break;

                            case "collapseRowScatter2"://地图散点分类，EXCEL模式下，API模式，SQL模式
                                WebElement web24 = fe.findE("//*[@id=\"collapseColumn1-scatter\"]/../following-sibling::div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web24).perform();
                                break;

                            case "collapseColumnBar"://3D地图柱状层系列
                                WebElement web25 = fe.findE("collapseColumn-bar", 1);
                                new Actions(driver).dragAndDrop(we, web25).perform();
                                break;

                            case "collapseRowBar"://3D地图柱状层分类
                                WebElement web26 = fe.findE("collapseRow2-bar", 1);
                                new Actions(driver).dragAndDrop(we, web26).perform();
                                break;

                            //以上用于dataK的元素操作*******************************************

                            //以下用于dataR的元素操作*******************************************

                            case "dropDataRColumn"://拖拽元素到系列
                                WebElement web27 = fe.findE("//*[@id=\"collapseColumn\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web27).perform();
                                break;

                            case "dropDataROrder"://拖拽元素到排序
                                WebElement web28 = fe.findE("//*[@id=\"collapseOrder\"]/div/div[2]", 2);
                                new Actions(driver).dragAndDrop(we, web28).perform();
                                break;

                            case "dropDataRCondition"://拖拽元素到条件
                                WebElement web29 = fe.findE("//*[@id=\"main-canvas\"]/div[1]/div[4]/div/div[3]/div", 2);
                                new Actions(driver).dragAndDrop(we, web29).perform();
                                break;

                            case "drawLine"://画斜线，只做了向下画线长度60个像素
                                new Actions(driver).dragAndDropBy(we, 0, 60).build().perform();
                                mouse.click();
                                break;

                            //以上用于dataR的元素操作*******************************************
                        }

                        i++;//读取下一步

                        //该步骤截图
                        if ("y".equals(snap)) {
                            String date = df.format(new Date());
                            Thread.sleep(2000);//等待页面加载完再截图
                            snapFile = "snap_" + exc[s] + "_step" + step + "_" + date + ".png";
                            snapShot.snapShot((TakesScreenshot) driver, snapFile);
                        }
                    }

                    driver.quit();
                    logger.info("表格:"+ sheetName +"完成，关闭浏览器");
                    //成功次数+1
                    pass++;
                    endTime = df.format(new Date());
                    testComment = "PASS";
                    //写入该sheet的测试结果
                    if(useDataK.equals("true")){
                        TestDetailReport testDetailReport = new TestDetailReport(testName,beginTime,endTime,testComment);
                        dataBaseSource.insertDetail(testDetailReport,reportTableName);
                    }else {
                        //测试报告中写入sheet的测试结果
                        extenseReport.startTestPass(extent,testName,testComment);
                    }
                }catch (Exception e) {
                        logger.error("第" + step + "步发生异常");
                        logger.error(e.getMessage(),e);
                        driver.quit();
                        logger.info("执行报错，关闭浏览器");
                        //失败次数+1
                        fail++;
                        endTime = df.format(new Date());

                        testComment = "第 " + step + " 步：元素 ["+ename.replaceAll("\'","\\\\'")+"] 出现异常";
                        //写入该sheet的测试结果
                        if(useDataK.equals("true")) {
                            TestDetailReport testDetailReport = new TestDetailReport(testName, beginTime, endTime, testComment);
                            dataBaseSource.insertDetail(testDetailReport, reportTableName);
                        }else {
                            //测试报告中写入sheet的测试结果
                            extenseReport.startTestFail(extent,testName,testComment);
                        }
                    }
                }
                logger.info(exc[s] + "执行完成");
            }
        logger.info("=========脚本完成=========");

        //更新统计测试结果数量
        //更新模板中sql的表名
        if(useDataK.equals("true")){
            TestReport testReport = new TestReport(pass, fail);
            dataBaseSource.update(testReport);
        }else {
            //输出最终的html格式测试报告
            extenseReport.extentFlush(extent);
        }

        logger.info("输出报告完成");
        System.exit(0);
    }
}
