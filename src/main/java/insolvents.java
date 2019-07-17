import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

public class insolvents {
    public static void main(String[] args) {


        File dir = new File("C:\\Source\\банкрот\\");
        File[] list = dir.listFiles();


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        for (File file : list) {
            // File x1 = new File("C:\\Source\\банкрот\\mes-2-2019_1.xml");

            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                System.out.println("парсим " + file.getName());

                Node root = doc.getDocumentElement();
                NodeList mdl = root.getChildNodes();
                System.out.println("длина: " + mdl.getLength());

                NodeList nlist, BankruptInfoNodeList, BankruptPersonNodeList;
                Node md, nn, an, BankruptInfoNode1, BankruptPersonNode;
                NamedNodeMap BankruptPerson, MessageInfo;

                String BankruptInfo;

                String FirstName, MiddleName, LastName, Address, Birthdate, Birthplace, MessageType, CaseNumber;
                FirstName = MiddleName = LastName = Address = Birthdate = Birthplace = MessageType = CaseNumber = "NULL";


                String inserting;


                int BankruptInfoNodeListlength;

                boolean ishuman = false;

                for
                    //(int i = 0; i < mdl.getLength(); i++)
                (int i = 0; i < 250; i++) {
                    md = mdl.item(i);
                    nlist = md.getChildNodes();
                    //System.out.println(i + ": ");

                    for (int ii = 0; ii < nlist.getLength(); ii++) {

                        nn = nlist.item(ii);
                        ishuman = false;

                        if (nn.getNodeName().equals("BankruptInfo")) {


                            BankruptInfo = nn.getTextContent();

                            if (BankruptInfo.length() > 10) {

                                System.out.println(i + ": ");
                                ishuman = true;


                                if (nn.hasChildNodes()) {
                                    //System.out.println(nn.getNodeName()+" "+nn.hasChildNodes() );

                                    BankruptInfoNodeList = nn.getChildNodes();
                                    //  BankruptInfoNodeListlength = BankruptInfoNodeList.getLength();
                                    BankruptInfoNode1 = BankruptInfoNodeList.item(0);
                                    //System.out.println(BankruptInfoNode1.getNodeName()+" "+BankruptInfoNode1.hasAttributes() );

                                    BankruptPerson = BankruptInfoNode1.getAttributes();
                                    FirstName = BankruptPerson.getNamedItem("FirstName").getTextContent();
                                    MiddleName = BankruptPerson.getNamedItem("MiddleName").getTextContent();
                                    LastName = BankruptPerson.getNamedItem("LastName").getTextContent();
                                    Address = BankruptPerson.getNamedItem("Address").getTextContent();


                                    System.out.println(FirstName + " " + MiddleName + " " + LastName + " " + Address);

                                    BankruptPersonNodeList = BankruptInfoNode1.getChildNodes();

                                    for (int bpnl = 0; bpnl < BankruptPersonNodeList.getLength(); bpnl++) {
                                        BankruptPersonNode = BankruptPersonNodeList.item(bpnl);
                                        System.out.println("BankruptPersonNode  " + BankruptPersonNode.getNodeName() + "  " + BankruptPersonNode.getTextContent());


                                        if (BankruptPersonNode.getNodeName().equals("Birthdate"))
                                            Birthdate = BankruptPersonNode.getTextContent();
                                        if (BankruptPersonNode.getNodeName().equals("Birthplace"))
                                            Birthplace = BankruptPersonNode.getTextContent();
                                    }
                                }


                                //System.out.println("человек?  "+nn.getNodeName() + " " + nn.getTextContent());


                                for (int a = 0; a < nlist.getLength(); a++) {
                                    // System.out.println("a "+ a);
                                    an = nlist.item(a);

                                    //System.out.println();


                                    //if (an.getNodeName().equals("Id"))
                                    // System.out.print(an.getNodeName() + " " + an.getTextContent() + " ");

                                    //if (an.getNodeName().equals("Number"))
                                    //   System.out.print(an.getNodeName() + " " + an.getTextContent());

                                    //if (an.getNodeName().equals("BankruptInfo"))
                                    //   System.out.println(an.getNodeName() + " " + an.getTextContent() + " ");

                                    if (an.getNodeName().equals("CaseNumber"))
                                        CaseNumber = an.getTextContent();
                                    //   System.out.println(an.getNodeName() + " " + an.getTextContent());
                                    //if ((!nn.getNodeName().equals("PublisherInfo")) & (!nn.getNodeName().equals("MessageInfo")))
                                    //    System.out.println(an.getNodeName() + " " + an.getTextContent());
                                    if (an.getNodeName().equals("MessageInfo")) {

                                        MessageInfo = an.getAttributes();

                                        MessageType = MessageInfo.getNamedItem("MessageType").getTextContent();
                                        System.out.println("MessageType: " + MessageType);


                                        if (nn.getTextContent().contains("банкротом"))
                                            System.out.println(nn.getTextContent());
                                    }
                                }

                                inserting = "Insert into [DEV].[dbo].[bankrots]  VALUES ('" + FirstName + "', '" + MiddleName + "', '" + LastName + "', '" + Address + "', NULL, NULL, '" + Birthplace + "', '" + CaseNumber + "', '" + MessageType + "')";
                                System.out.println(inserting);
                           }
                            //System.out.println(ishuman);
                        }
                    }
                }


            } catch (IOException e) {
                System.out.println(e);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

}


