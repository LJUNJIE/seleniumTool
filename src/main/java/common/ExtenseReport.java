package common;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtenseReport {

    private final  static Logger logger = LoggerFactory.getLogger(ExtenseReport.class);

    String reportLocation = "report/";
    ExtentTest test;

    public ExtentReports createReport(String reportName){
        ExtentReports extent = null;
        reportLocation = reportLocation + reportName + ".html";
        try {
            extent = new ExtentReports(reportLocation, NetworkMode.OFFLINE);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return extent;
    }

    public void startTestPass(ExtentReports extent,String testName, String testComment){
        try {
            test = extent.startTest(testName);
            test.log(LogStatus.PASS, testName, testComment);
            extent.endTest(test);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    public void startTestFail(ExtentReports extent,String testName, String testComment){
        try {
            test = extent.startTest(testName);
            test.log(LogStatus.FAIL, testName, testComment);
            extent.endTest(test);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    public void extentFlush(ExtentReports extent){
        extent.flush();
    }
}
