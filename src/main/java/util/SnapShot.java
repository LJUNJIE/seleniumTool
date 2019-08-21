package util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class SnapShot {

    public final  static Logger logger = LoggerFactory.getLogger(SnapShot.class);

    static String os = System.getProperty("os.name").toLowerCase();

    /**
     *
     * @param drivername
     * @param filename 生成文件名称
     */
    public void snapShot(TakesScreenshot drivername, String filename){

        String currentPath = System.getProperty("user.dir")+"/pic";
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        try {
            logger.info("截图保存到:"+currentPath+"/"+filename);
            if(os.contains("win")){
                FileUtils.copyFile(scrFile, new File(currentPath+"\\"+filename));
            }else {
                FileUtils.copyFile(scrFile, new File(currentPath+"/"+filename));
            }
        } catch (IOException e) {
            logger.error("保存截图失败:"+e.getMessage());
        }
    }
}
