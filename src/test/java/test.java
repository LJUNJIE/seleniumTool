import Bean.TestReport;
import util.DataBaseSource;
import util.DownloadReport;
import common.*;
import org.testng.annotations.Test;

public class test{

    @Test
    public void test() throws Exception{
        ElementAction elementAction = new ElementAction();
        elementAction.run();
    }

    @Test
    public void openTest(){
        OpenBrowser openBrowser = new OpenBrowser();
        openBrowser.openBrowser(3);
    }

//    @Test
//    public void extent(){
//        TestRep testRep = new TestRep();
//        testRep.;
//    }

    @Test
    public void testReport(){
        TestReport testReport = new TestReport(1,1);
        DataBaseSource dataBaseSource = new DataBaseSource();
        dataBaseSource.update(testReport);
    }

    @Test
    public void testDownload(){
        DownloadReport downloadReport = new DownloadReport();
        downloadReport.downLoad();
    }

    @Test
    public void getUrl(){
        ExportProject exportProject = new ExportProject();
        String str = exportProject.getUrl();
        exportProject.downloadProject(str);
    }

}