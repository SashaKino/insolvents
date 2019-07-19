import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.sql.*;


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

                NodeList nlist, BankruptInfoNodeList, BankruptPersonNodeList, MessageInfoNodeList, MessageInfoNode1NodeList;
                Node md, nn, an, BankruptInfoNode1, BankruptPersonNode, MessageInfoNode1, MessageInfoNode1Node;
                NamedNodeMap BankruptPerson, MessageInfo;

                String BankruptInfo;

                // для банкрота
                String FirstName, MiddleName, LastName, Address, Birthdate, Birthplace, MessageType, CaseNumber;
                FirstName = MiddleName = LastName = Address = Birthdate = Birthplace = MessageType = CaseNumber = "NULL";
                long INN=0;

                // для кредитора
                String CreditorName, DemandSum, DemandDate, ReasonOccurence;
                CreditorName= DemandSum = DemandDate = ReasonOccurence = "NULL";


                String inserting, creditor_inserting;
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl =
                        "jdbc:sqlserver://FREE:1433;databaseName=DEV;integratedSecurity=true";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement st = con.createStatement();


                boolean ishuman = false;

                for
                    //(int i = 0; i < mdl.getLength(); i++)
                (int i = 0; i < mdl.getLength(); i++) {
                    md = mdl.item(i);
                    nlist = md.getChildNodes();
                    //System.out.println(i + ": ");

                    for (int ii = 0; ii < nlist.getLength(); ii++) {

                        nn = nlist.item(ii);
                        ishuman = false;

                        if (nn.getNodeName().equals("BankruptInfo")) {


                            BankruptInfo = nn.getTextContent();

                            if (BankruptInfo.length() > 10) {

                                ishuman = true;


                                if (nn.hasChildNodes()) {

                                    BankruptInfoNodeList = nn.getChildNodes();

                                    BankruptInfoNode1 = BankruptInfoNodeList.item(0);

                                    BankruptPerson = BankruptInfoNode1.getAttributes();
                                    FirstName = BankruptPerson.getNamedItem("FirstName").getTextContent();
                                    MiddleName = BankruptPerson.getNamedItem("MiddleName").getTextContent();
                                    LastName = BankruptPerson.getNamedItem("LastName").getTextContent();
                                    Address = BankruptPerson.getNamedItem("Address").getTextContent();


                                    //System.out.println(FirstName + " " + MiddleName + " " + LastName + " " + Address);

                                    BankruptPersonNodeList = BankruptInfoNode1.getChildNodes();

                                    for (int bpnl = 0; bpnl < BankruptPersonNodeList.getLength(); bpnl++) {
                                        BankruptPersonNode = BankruptPersonNodeList.item(bpnl);
                                        //System.out.println("BankruptPersonNode  " + BankruptPersonNode.getNodeName() + "  " + BankruptPersonNode.getTextContent());


                                        if (BankruptPersonNode.getNodeName().equals("Birthdate"))
                                            Birthdate = BankruptPersonNode.getTextContent();
                                        if (BankruptPersonNode.getNodeName().equals("Birthplace"))
                                            Birthplace = BankruptPersonNode.getTextContent();
                                        if (BankruptPersonNode.getNodeName().equals("INN"))
                                            INN = Long.parseLong(BankruptPersonNode.getTextContent());

                                    }
                                }


                                //System.out.println("человек?  "+nn.getNodeName() + " " + nn.getTextContent());


                                for (int a = 0; a < nlist.getLength(); a++) {
                                    // System.out.println("a "+ a);
                                    an = nlist.item(a);


                                    if (an.getNodeName().equals("CaseNumber"))
                                        CaseNumber = an.getTextContent();

                                    if (an.getNodeName().equals("MessageInfo")) {

                                        MessageInfo = an.getAttributes();


                                        MessageType = MessageInfo.getNamedItem("MessageType").getTextContent();

                                        System.out.println("MessageType: "+ MessageType);


                                       if (MessageType.equals("ReceivingCreditorDemand") & an.hasChildNodes()) {

                                            MessageInfoNodeList = an.getChildNodes();
                                            MessageInfoNode1 = MessageInfoNodeList.item(0);

                                            if (MessageInfoNode1.hasChildNodes()) {
                                                MessageInfoNode1NodeList=MessageInfoNode1.getChildNodes() ;

                                                for (int min1n = 0; min1n < MessageInfoNode1NodeList.getLength()  ; min1n++) {
                                                    MessageInfoNode1Node = MessageInfoNode1NodeList.item(min1n);

                                                    if (MessageInfoNode1Node.getNodeName().equals("DemandDate"))
                                                        DemandDate = MessageInfoNode1Node.getTextContent();
                                                    if (MessageInfoNode1Node.getNodeName().equals("DemandSum"))
                                                        DemandSum = MessageInfoNode1Node.getTextContent();
                                                    if (MessageInfoNode1Node.getNodeName().equals("CreditorName"))
                                                        CreditorName = MessageInfoNode1Node.getTextContent();
                                                    if ( MessageInfoNode1Node.getNodeName().equals("ReasonOccurence"));
                                                    ReasonOccurence = MessageInfoNode1Node.getTextContent();

                                                }
                                            }
                                        }

                                        /*if (MessageInfoNodeList.getLength() > 1) {
                                            System.out.println("MessageType: " + MessageType);
                                            System.out.println(MessageInfoNodeList.getLength());
                                                                                    }
                                                                                    */

                                            /*
                                            for (int min = 0; min < MessageInfoNodeList.getLength(); min++) {
                                                MessageInfoNode = MessageInfoNodeList.item(min);
                                                System.out.println(MessageInfoNode.getNodeName());
                                                }
                                                */
                                       // }


                                        //(nn.getTextContent().contains("банкротом"))
                                            //System.out.println(an.getTextContent());
                                    }
                                }



                                    inserting = "Insert into [DEV].[dbo].[bankrots]  VALUES ('" + FirstName + "', '" + MiddleName + "', '" + LastName + "', '" + Address + "', " + INN + ",'" + Birthdate + "', '" + Birthplace + "', '" + CaseNumber + "', '" + MessageType + "')";
                                    creditor_inserting = "Insert into [DEV].[dbo].[creditors]  VALUES (" + INN + ", '" + CreditorName + "', '" + DemandSum + "', '" + DemandDate + "', '" + ReasonOccurence + "')";

                                    System.out.println(inserting);

                                    if (MessageType.equals("ReceivingCreditorDemand"))
                                    System.out.println(creditor_inserting);

                               // try {
                                //   st.executeUpdate(inserting);
                               // } catch ( SQLException e) {
                               //     System.out.println(inserting);
                                //    System.out.println( e);

                               // }


                                System.out.println();
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


