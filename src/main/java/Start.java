import common.ElementAction;
import org.dom4j.Document;
import org.dom4j.Element;
import util.LoadProperties;
import util.RemoteServerInit;

import java.io.File;
import java.util.List;

public class Start {

    private static Document document;

    public static void main(String[] args) throws Exception {

        LoadProperties loadProperties = new LoadProperties();
        String useRemote = loadProperties.loadProperties("useRemote");

        if(useRemote.equals("false")){
            ElementAction elementAction = new ElementAction();
            elementAction.run();
        }else {
            File file = new File("cfg/RemoteServerInfo.xml");
            document = RemoteServerInit.loadXmlFile(file);

            List<Element> list = RemoteServerInit.getAllInfo(document);
            RemoteServerInit.launch(list);
        }

    }
}