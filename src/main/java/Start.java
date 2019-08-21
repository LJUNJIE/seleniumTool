import common.ElementAction;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openqa.selenium.WebDriver;
import util.RemoteServerInit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Start {

    private static Document document;

    public static void main(String[] args) throws Exception {

        ElementAction elementAction = new ElementAction();
        elementAction.run();

//        File file = new File("cfg/RemoteServerInfo.xml");
//        document = RemoteServerInit.loadXmlFile(file);
//
//        List<Element> list = RemoteServerInit.getAllInfo(document);
//        RemoteServerInit.launch(list);

    }
}