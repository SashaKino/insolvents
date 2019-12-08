import com.sun.xml.internal.bind.v2.model.core.ID;
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

                NodeList nlist, BankruptInfoNodeList, BankruptPersonNodeList, MessageInfoNodeList, MessageInfoNode1NodeList, CourtDecisionNodeList, CourtDecreeNodeNodeList;
                Node md, nn, an, BankruptInfoNode1, BankruptPersonNode, MessageInfoNode1, MessageInfoNode1Node, CourtDecisionNode, CourtDecreeNode;
                NamedNodeMap BankruptPerson, MessageInfo, DecisionType, BankruptInfoNodeMap;

                String Id, BankruptInfo;
                int Idint;

                Id = "NULL";
                Idint=0;

                // для банкрота
                String FirstName, MiddleName, LastName, Address, Birthdate, Birthplace, MessageType, CaseNumber, INN, SNILS;
                FirstName = MiddleName = LastName = Address = Birthdate = Birthplace = MessageType = CaseNumber = SNILS = INN = "NULL";
                //long INN=0;

                String BankruptType, BankruptCategory;
                BankruptType = BankruptCategory = "NULL";

                // для кредитора
                String CreditorName, DemandSum, DemandDate, ReasonOccurence;
                CreditorName= DemandSum = DemandDate = ReasonOccurence = "NULL";

                //для типа решения
                String DecisionTypeId="NULL";

                //для суда
                String CourtId,CourtName;
                CourtId = CourtName ="NULL";


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

                    Id="NULL";
                    FirstName = MiddleName = LastName = Address = Birthdate = Birthplace = MessageType = CaseNumber = DecisionTypeId =INN = SNILS = "NULL";
                    CreditorName= DemandSum = DemandDate = ReasonOccurence = "NULL";
                    BankruptType = BankruptCategory = "NULL";
                    CourtId = CourtName ="NULL";
                    //INN = 0;
                    Idint=0;

                    nlist = md.getChildNodes();
                    //System.out.println(i + ": ");

                    //



                    for (int ii = 0; ii < nlist.getLength(); ii++) {



                        ishuman = false;
                        nn = nlist.item(ii);

                        if (nn.getNodeName().equals("Id")) {

                            Id = nn.getTextContent();

                            try {
                                Idint = Integer.parseInt(nn.getTextContent());
                            } catch (Exception e) {
                                System.out.println("Неужели здесь исключение! Id" + Id);

                            }


                        }


                        if (nn.getNodeName().equals("MessageInfo")) {

                            MessageInfo = nn.getAttributes();

                            try {
                                MessageType =  MessageInfo.getNamedItem("MessageType").getTextContent() ;
                            } catch (Exception e) {
                                System.out.println(Id + "исключение при получении MessageType"+ e);

                            }

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
                                            DecisionTypeId="'"+DecisionType.getNamedItem("Id").getTextContent()+"'";
                                        }
                                        } catch (Exception e) {
                                            System.out.println("исключение при получении данных типа рещения:" + MessageInfoNode1Node.getNodeName() );
                                            System.out.println(e);
                                            System.out.println();
                                        }


                                        if (MessageInfoNode1Node.getNodeName().equals("CourtDecree")){
                                            if (MessageInfoNode1Node.hasChildNodes()) {
                                                CourtDecreeNodeNodeList = MessageInfoNode1Node.getChildNodes();
                                                for (int cdn = 0; cdn <CourtDecreeNodeNodeList.getLength();  cdn++) {
                                                   CourtDecisionNode = CourtDecreeNodeNodeList.item(cdn);

                                                    if (CourtDecisionNode.getNodeName().equals("CourtId"))
                                                        CourtId = "'"+CourtDecisionNode.getTextContent()+"'";

                                                    if (CourtDecisionNode.getNodeName().equals("CourtName"))
                                                        CourtName ="'"+ CourtDecisionNode.getTextContent()+"'";




                                                }





                                            }

                                        }




                                    }
                                }

                            }

                           // System.out.println("MessageType = " + MessageType);
                        }



                        if (nn.getNodeName().equals("BankruptInfo")) {

                            BankruptInfo = nn.getTextContent();

                            if (nn.hasAttributes()) {
                                BankruptInfoNodeMap = nn.getAttributes();

                                try {
                                    BankruptType = BankruptInfoNodeMap.getNamedItem("BankruptType").getTextContent();
                                } catch (Exception e) {
                                    System.out.println(Id+ " исключение при получении BankruptType  "+ e);
                                }

                                if (BankruptType.equals("Person")) {

                                try {
                                    BankruptCategory ="'"+ BankruptInfoNodeMap.getNamedItem("BankruptCategory").getTextContent()+"'";
                                } catch (Exception e) {
                                    System.out.println(Id + " исключение при получении  BankruptCategory " + e);

                                }
                                }

                            }




                            if (BankruptInfo.length() > 10) {

                                ishuman = true;



                                if (nn.hasChildNodes()) {

                                    BankruptInfoNodeList = nn.getChildNodes();

                                    BankruptInfoNode1 = BankruptInfoNodeList.item(0);

                                    try {
                                        BankruptPerson = BankruptInfoNode1.getAttributes();
                                        FirstName ="'" + BankruptPerson.getNamedItem("FirstName").getTextContent()+ "'";
                                        try {
                                            MiddleName ="'" + BankruptPerson.getNamedItem("MiddleName").getTextContent()+"'" ;
                                        } catch (java.lang.NullPointerException e){
                                            System.out.println("нет отчества у  "+BankruptInfo);
                                            MiddleName = "NULL";

                                        }
                                        LastName ="'"+ BankruptPerson.getNamedItem("LastName").getTextContent()+"'";

                                        Address = "'"+BankruptPerson.getNamedItem("Address").getTextContent()+"'";

                                        if (Address.length()>198) Address = "'"+ BankruptPerson.getNamedItem("Address").getTextContent().substring(0,198)+"'";


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


                                        if (BankruptPersonNode.getNodeName().equals("Birthdate")) {
                                            //System.out.println(BankruptPersonNode.getTextContent());


                                            if (BankruptPersonNode.getTextContent().length() > 8)
                                                Birthdate = "'"+ BankruptPersonNode.getTextContent().substring(6, 10) + BankruptPersonNode.getTextContent().substring(3, 5) + BankruptPersonNode.getTextContent().substring(0, 2)+"'";

                                            if (Birthdate.contains(" ")) {
                                                System.out.println(Id+"  дата рождения указана так: "+BankruptPersonNode.getTextContent() );
                                            }



                                            if ((BankruptPersonNode.getTextContent().length() < 5) & ( BankruptPersonNode.getTextContent().length() > 1)  ) {
                                                System.out.println(Id + " нестандартый Birthdate: " + BankruptPersonNode.getTextContent());
                                                Birthdate = "NULL";
                                            }

                                            //Birthdate = BankruptPersonNode.getTextContent().substring(6, 10)+"0101";
                                            if (BankruptPersonNode.getTextContent().length() < 1)
                                                //System.out.println(Id +" пустой Birthdate: ");
                                                Birthdate = "NULL";

                                        }
                                        if (BankruptPersonNode.getNodeName().equals("Birthplace")) {
                                            Birthplace = "'"+ BankruptPersonNode.getTextContent()+"'";
                                            if (Birthplace.length()>148) {
                                                Address ="'"+ BankruptPersonNode.getTextContent().substring(0, 148)+"'";
                                            }
                                        }

                                        if (BankruptPersonNode.getNodeName().equals("INN"))
                                            INN = "'" + BankruptPersonNode.getTextContent() + "'" ;


                                        if (BankruptPersonNode.getNodeName().equals("SNILS"))
                                            SNILS ="'"+ BankruptPersonNode.getTextContent()+ "'"  ;

                                    }
                                }


                                //System.out.println("человек?  "+nn.getNodeName() + " " + nn.getTextContent());


                                for (int a = 0; a < nlist.getLength(); a++) {
                                    // System.out.println("a "+ a);
                                    an = nlist.item(a);


                                    if (an.getNodeName().equals("CaseNumber"))
                                        CaseNumber = "'"+ an.getTextContent()+"'";


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







                         if ( MessageType.equals("ArbitralDecree") &  DecisionTypeId.equals("'19'") ) {
                             //проверка, почему повторяются INN
/*
                             if ((INN == "32500507306") | (INN == "440115172411") | (INN == "772578505500") | (INN == "27318258906") | (INN == "323300345309")) {
                                 System.out.println(Id + " " + INN + " " + FirstName + " " + MiddleName + " " + LastName + " " + MessageType + " " + DecisionTypeId);
                             }
*/

/*
                             if (Birthdate.equals("NULL"))
                            inserting = "Insert into [DEV].[dbo].[bankrots19-6]  VALUES ('"+BankruptCategory+ "', '" + FirstName + "', '" + MiddleName + "', '" + LastName + "', '" + Address + "', " + INN + ",NULL,'" + Birthplace + "', '" + CaseNumber + "', '" +CourtId + "', '"+ CourtName + "', '"+ DecisionTypeId + "')";
                            else
                            inserting = "Insert into [DEV].[dbo].[bankrots19-6]  VALUES ('"+BankruptCategory+ "', '" + FirstName + "', '" + MiddleName + "', '" + LastName + "', '" + Address + "', " + INN + ", ' "+ Birthdate + "', '" + Birthplace + "', '" + CaseNumber + "', '" +CourtId + "', '"+ CourtName + "', '"+ DecisionTypeId + "')";
*/

                            // if (Birthdate.equals("NULL"))
                            //     inserting = "Insert into [DEV].[dbo].[bankrots19-7]  VALUES (" + Idint + ",'" + BankruptCategory + "', '" + FirstName + "', '" + MiddleName + "', '" + LastName + "', " + Address + ", " + INN + ",NULL,'" + Birthplace + "', '" + CaseNumber + "', '" + CourtId + "', '" + CourtName + "', '" + DecisionTypeId + "')";
                            // else
                                 inserting = "Insert into [DEV].[dbo].[bankrots19-7]  VALUES (" + Idint + "," + BankruptCategory + ", " + FirstName + ", " + MiddleName + ", " + LastName + ", " + Address + ", " + INN + ", " +SNILS + ","  + Birthdate + ", " + Birthplace + ", " + CaseNumber + ", " + CourtId + ", " + CourtName + ", " + DecisionTypeId + ")";


                             System.out.println(inserting);


                             try {
                                 st.executeUpdate(inserting);
                             } catch (SQLException e) {
                                 System.out.println("Исключение " + e + " в сообщении " + Id);
                                 System.out.println(inserting);
                                 System.out.println("Bitrhdate "+ Birthdate);

                             }



                         }

                        }


                        /*



                        if (MessageType.equals("ReceivingCreditorDemand") ) {
                            creditor_inserting = "Insert into [DEV].[dbo].[creditors]  VALUES (" + INN + ", '" + CreditorName + "', '" + DemandSum + "', '" + DemandDate + "', '" + ReasonOccurence + "')";

                            //System.out.println(creditor_inserting);


                            try {
                                st.executeUpdate(creditor_inserting);
                            } catch ( SQLException e) {
                                System.out.println(creditor_inserting);

                                System.out.println("Исключение " +e + " в строке ");
                                System.out.println(creditor_inserting);
                            }


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
                            */







                        //здесь заканчивается тег message data





            } catch (IOException e) {

                System.out.println(file+" "+e);


            } catch (Exception e) {
                System.out.println(file+" "+e);
            }
        }

    }

}


