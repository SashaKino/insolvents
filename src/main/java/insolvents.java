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

                NodeList nlist, BankruptInfoNodeList, BankruptPersonNodeList, MessageInfoNodeList, MessageInfoNode1NodeList, CourtDecisionNodeList;
                Node md, nn, an, BankruptInfoNode1, BankruptPersonNode, MessageInfoNode1, MessageInfoNode1Node, CourtDecisionNode;
                NamedNodeMap BankruptPerson, MessageInfo, DecisionType;

                String BankruptInfo;

                // для банкрота
                String FirstName, MiddleName, LastName, Address, Birthdate, Birthplace, MessageType, CaseNumber;
                FirstName = MiddleName = LastName = Address = Birthdate = Birthplace = MessageType = CaseNumber = "NULL";
                long INN=0;





                // для кредитора
                String CreditorName, DemandSum, DemandDate, ReasonOccurence;
                CreditorName= DemandSum = DemandDate = ReasonOccurence = "NULL";

                //для типа решения
                String DecisionTypeId="NULL";


                String inserting, creditor_inserting, mfo_demand_inserting;
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

                    FirstName = MiddleName = LastName = Address = Birthdate = Birthplace = MessageType = CaseNumber = "NULL";
                    CreditorName= DemandSum = DemandDate = ReasonOccurence = "NULL";
                    nlist = md.getChildNodes();
                    //System.out.println(i + ": ");



                    for (int ii = 0; ii < nlist.getLength(); ii++) {

                        ishuman = false;
                        nn = nlist.item(ii);

                        if (nn.getNodeName().equals("MessageInfo")) {

                            MessageInfo = nn.getAttributes();


                            MessageType = MessageInfo.getNamedItem("MessageType").getTextContent();

                            //System.out.println("MessageType: "+ MessageType);


                            if ((MessageType.equals("ReceivingCreditorDemand") | MessageType.equals("ArbitralDecree")) & nn.hasChildNodes()) {

                                MessageInfoNodeList = nn.getChildNodes();
                                MessageInfoNode1 = MessageInfoNodeList.item(0);

                                if (MessageInfoNode1.hasChildNodes()) {
                                    MessageInfoNode1NodeList=MessageInfoNode1.getChildNodes() ;

                                    for (int min1n = 0; min1n < MessageInfoNode1NodeList.getLength()  ; min1n++) {
                                        MessageInfoNode1Node = MessageInfoNode1NodeList.item(min1n);

                                        try {
                                            //если MessageInfoNode1 - ReceivingCreditorDemand
                                            if (MessageInfoNode1Node.getNodeName().equals("DemandDate"))
                                                DemandDate = MessageInfoNode1Node.getTextContent();
                                            if (MessageInfoNode1Node.getNodeName().equals("DemandSum"))
                                                DemandSum = MessageInfoNode1Node.getTextContent();
                                            if (MessageInfoNode1Node.getNodeName().equals("CreditorName")) {
                                                CreditorName = MessageInfoNode1Node.getTextContent();
                                               if (CreditorName.length()>199) CreditorName =MessageInfoNode1Node.getTextContent().substring(0,198);;
                                            }
                                            if (MessageInfoNode1Node.getNodeName().equals("ReasonOccurence")) {
                                                ReasonOccurence = MessageInfoNode1Node.getTextContent();
                                               if (ReasonOccurence.length()>148) ReasonOccurence = MessageInfoNode1Node.getTextContent().substring(0,149);;

                                            }
                                        } catch (Exception e) {
                                            System.out.println("исключение при получении данных кредитора:" + MessageInfoNode1Node.getNodeName() );
                                            System.out.println(e);
                                            System.out.println();
                                        }

                                      //  еслn MessageInfoNode1 - MessageInfo
                                        try {
                                        if (MessageInfoNode1Node.getNodeName().equals("DecisionType")){
                                            DecisionType = MessageInfoNode1Node.getAttributes();
                                            DecisionTypeId=DecisionType.getNamedItem("Id").getTextContent();
                                        }  } catch (Exception e) {
                                            System.out.println("исключение при получении данных типа рещения:" + MessageInfoNode1Node.getNodeName() );
                                            System.out.println(e);
                                            System.out.println();
                                        }







                                    }
                                }

                            }

                           // System.out.println("MessageType = " + MessageType);
                        }



                        if (nn.getNodeName().equals("BankruptInfo")) {

                            BankruptInfo = nn.getTextContent();

                            if (BankruptInfo.length() > 10) {

                                ishuman = true;


                                if (nn.hasChildNodes()) {

                                    BankruptInfoNodeList = nn.getChildNodes();

                                    BankruptInfoNode1 = BankruptInfoNodeList.item(0);

                                    try {
                                        BankruptPerson = BankruptInfoNode1.getAttributes();
                                        FirstName = BankruptPerson.getNamedItem("FirstName").getTextContent();
                                        try {
                                            MiddleName = BankruptPerson.getNamedItem("MiddleName").getTextContent();
                                        } catch (java.lang.NullPointerException e){
                                            System.out.println("нет отчества у  "+BankruptInfo);
                                            MiddleName = "";

                                        }
                                        LastName = BankruptPerson.getNamedItem("LastName").getTextContent();
                                        Address = BankruptPerson.getNamedItem("Address").getTextContent();

                                        if (Address.length()>198) Address = BankruptPerson.getNamedItem("Address").getTextContent().substring(0,199);;


                                    } catch (Exception e) {

                                        System.out.println("исключение при получении данных банкротв:" + BankruptInfoNode1.getNodeName() );
                                        System.out.println(e + ": ");
                                        System.out.println(BankruptInfo);
                                        System.out.println();


                                    }


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


                                }

                            }
                            //System.out.println(ishuman);
                        }

                        /* Можно удалять

                        if (nn.getNodeName().equals("CourtDecision"))
                        {
                            if (nn.hasChildNodes()) {
                                System.out.println("CourtDecision: nn.hasChildNodes " + nn.hasChildNodes() );
                                CourtDecisionNodeList = nn.getChildNodes();

                                for (int c = 0; c < CourtDecisionNodeList.getLength(); i++) {
                                    CourtDecisionNode = CourtDecisionNodeList.item(c);
                                    System.out.println(CourtDecisionNode.getNodeName());
                                    if (CourtDecisionNode.getNodeName().equals("DesisionType")){
                                        System.out.println( "has attributes: " + CourtDecisionNode.hasChildNodes() );
                                        DecisionType = CourtDecisionNode.getAttributes();
                                        DecisionTypeId=DecisionType.getNamedItem("Id").getTextContent();
                                        System.out.println("DecisionTypeId: "+DecisionTypeId);

                                    }
                               }

                               }
                            }
                            */



                        }

                        if ( MessageType.equals("ArbitralDecree") & ( DecisionTypeId.equals("7") | DecisionTypeId.equals("19")) )  {
                            inserting = "Insert into [DEV].[dbo].[bankrots]  VALUES ('" + FirstName + "', '" + MiddleName + "', '" + LastName + "', '" + Address + "', " + INN + ",'" + Birthdate + "', '" + Birthplace + "', '" + CaseNumber + "', '" + DecisionTypeId + "')";

                          //  System.out.println(inserting);

                            /*
                            try {
                                st.executeUpdate(inserting);
                            } catch ( SQLException e) {
                                System.out.println("Исключение " +e + " в строке ");
                                System.out.println(inserting);
                            }
                            */


                        }



                        if (MessageType.equals("ReceivingCreditorDemand") ) {
                            creditor_inserting = "Insert into [DEV].[dbo].[creditors]  VALUES (" + INN + ", '" + CreditorName + "', '" + DemandSum + "', '" + DemandDate + "', '" + ReasonOccurence + "')";

                            //System.out.println(creditor_inserting);

                            /*
                            try {
                                st.executeUpdate(creditor_inserting);
                            } catch ( SQLException e) {
                                System.out.println(creditor_inserting);

                                System.out.println("Исключение " +e + " в строке ");
                                System.out.println(creditor_inserting);
                            }
                            */

                        }


                            if  ( (MessageType.equals("ReceivingCreditorDemand")) &
                            ( CreditorName.contains("фонд содействия") |
                                  CreditorName.contains("РУСИНТЕРФИНАНС") |  CreditorName.contains("5408292849") |
                                 CreditorName.contains("ЗАЙМЕР") | CreditorName.contains("4205271785") |
                                 CreditorName.contains("ЦЕНТР ФИНАНСОВОЙ") | CreditorName.contains("6234006961") |

                                CreditorName.contains("ПРОСТОДЕНЬГИ") | CreditorName.contains("4205219217") |
                                CreditorName.contains("элемент") |  CreditorName.contains("ПЯТЫЙ") | CreditorName.contains("4025443121") |


                                CreditorName.contains("4ФИНАНС") |  CreditorName.contains("7724351447") |
                                 CreditorName.contains("ВЛ-ФИНАНС") | CreditorName.contains("2538081836") |
                                CreditorName.contains("смс") | CreditorName.contains("7729677643") |
                                CreditorName.contains("АРИФМЕТИКА")   | CreditorName.contains("5410059568") |
                                 CreditorName.contains("микро") | CreditorName.contains("МИКРО")

                            ) )

                            {
                                mfo_demand_inserting = "Insert into [DEV].[dbo].[mfo_demands]  VALUES (" + INN  + ",'" + FirstName + "', '" + MiddleName + "', '" + LastName + "', '" + Address + "', '" +  Birthdate + "', '" + Birthplace + "', '"+ CreditorName + "', '" + DemandSum + "', '" + DemandDate + "', '" + ReasonOccurence + "')";
                                try {
                                    st.executeUpdate(mfo_demand_inserting);

                                } catch ( SQLException e) {

                                    System.out.println("Исключение " +e + " в строке: ");
                                    System.out.println(mfo_demand_inserting);
                                }
                                //System.out.println(mfo_demand_inserting);

                            }







                        //здесь заканчивается тег message data

                    }



            } catch (IOException e) {
                System.out.println(e);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

}


