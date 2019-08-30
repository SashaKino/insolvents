import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class DebtorList_Person_Parser {

    public static void main(String[] args) {

        Node DebtorPerson;
        NamedNodeMap DebtorPersonNM;


        File dir = new File("C:\\Source\\debtorlist\\");
        File[] list = dir.listFiles();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        for (File file : list) {
            try {

                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                System.out.println("парсим " + file.getName());

                Node root = doc.getDocumentElement();
                NodeList nl = root.getChildNodes();
                System.out.println("длина: " + nl.getLength());


                for (int i = 0; i < nl.getLength(); i++) {
                    DebtorPerson = nl.item(i);

                    try {
                        DebtorPersonNM = DebtorPerson.getAttributes();
                        System.out.println( i + " " + DebtorPersonNM.getNamedItem("INN") +DebtorPersonNM.getNamedItem("LastName") );


                    } catch (Exception o) {
                        System.out.println(o);
                    }



                }
            }catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
