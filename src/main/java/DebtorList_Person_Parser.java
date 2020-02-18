import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

import java.sql.*;


public class DebtorList_Person_Parser {

    public static void main(String[] args) {

        Node DebtorPerson;
        NamedNodeMap DebtorPersonNM;

        Node second;
        NodeList nlist,  EGRIPDifferenceslist;

        File dir = new File("C:\\Source\\debtorlist\\");
        File dir1 = new File("C:\\Source\\debtorlist2\\");
        File[] list = dir.listFiles();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String FirstName, MiddleName, LastName, Address, Birthdate, Region, LastMessageDate, CategoryCode, BankruptId, SNILS, INN, EGRIPOGRNIP, month = "NULL";
        //long INN = 0;
        String inserting;


        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl =
                    "jdbc:sqlserver://FREE:1433;databaseName=DEV;integratedSecurity=true";
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement st = con.createStatement();


            for (File file : list) {

                try {
                    month = file.getName().substring(0, 6);

                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(file);
                    System.out.println("парсим " + file.getName());

                    Node root = doc.getDocumentElement();
                    NodeList nl = root.getChildNodes();
                    System.out.println("длина: " + nl.getLength());


                    for (int i = 0; i < nl.getLength(); i++) {
                        DebtorPerson = nl.item(i);

                        FirstName = MiddleName = LastName = Address = Birthdate = Region = LastMessageDate = CategoryCode = BankruptId = SNILS = INN = EGRIPOGRNIP ="NULL";


                        try {

                            DebtorPersonNM = DebtorPerson.getAttributes();

                            FirstName ="'"+ DebtorPersonNM.getNamedItem("FirstName").getTextContent()+"'";

                            try {
                                MiddleName ="'"+ DebtorPersonNM.getNamedItem("MiddleName").getTextContent()+"'";
                            } catch (Exception e) {
                                System.out.println(e+ " -MiddleName"  );
                            }


                            LastName ="'"+  DebtorPersonNM.getNamedItem("LastName").getTextContent()+"'";
                           // System.out.println(i+" "+LastName);




                            try {
                                INN = DebtorPersonNM.getNamedItem("INN").getTextContent();
                            } catch (Exception e) {

                            }


                            try {
                                BankruptId = DebtorPersonNM.getNamedItem("BankruptId").getTextContent();
                            } catch (Exception e) {

                            }


                            try {
                                SNILS = "'"+DebtorPersonNM.getNamedItem("SNILS").getTextContent()+"'";
                            } catch (Exception e) {
                              //  System.out.println(e+ " Snils  ");
                            }


                            try {
                                Region = "'"+DebtorPersonNM.getNamedItem("Region").getTextContent()+"'";
                            } catch (Exception e) {
                                System.out.println(e+ " Region  ");
                            }


                            CategoryCode = "'"+ DebtorPersonNM.getNamedItem("CategoryCode").getTextContent()+"'";

                            try {
                                Address = DebtorPersonNM.getNamedItem("Address").getTextContent();
                             } catch (Exception e) {
                            System.out.println(e+ "Address");
                        }



                            try {
                                Birthdate ="'"+DebtorPersonNM.getNamedItem("Birthdate").getTextContent()+"'";
                            } catch (Exception e) {
                                Birthdate = "NULL";
                               // System.out.println(e+ " Birthdate "  );
                            }


                            try {
                                if (Address.length() > 198)
                                    Address = Address.substring(0, 198);
                                Address = "'" + Address + "'";
                            } catch (Exception e) {
                                System.out.println("у  "+LastName+" в адресе " +e);
                                Address = "NULL";
                            }




                            //Вот здесь получим EGRIP
                            nlist = DebtorPerson.getChildNodes();
                            if (nlist.getLength() > 1 ) {
                                for (int ii = 0; ii < nlist.getLength(); ii++) {

                                    if (nlist.item(ii).getNodeName().equals("EGRIPDifferences")) {

                                        //System.out.println(ii);
                                        EGRIPDifferenceslist = nlist.item(ii).getChildNodes();

                                        for (int iii = 0; iii < EGRIPDifferenceslist.getLength(); iii++) {
                                            if (EGRIPDifferenceslist.item(iii).getNodeName().equals("EGRIPOGRNIP")) {
                                                EGRIPOGRNIP = "'" + EGRIPDifferenceslist.item(iii).getTextContent() + "'";
                                                // System.out.println(LastName+" EGRIPOGRNIP "+ EGRIPDifferenceslist.item(iii).getTextContent() );
                                            }

                                        }
                                    }
                                }
                            }



                                   // inserting = "INSERT into [debtorlist3] values " + " " + "(" + FirstName + ", " + MiddleName + ", " + LastName + ", " + INN + ", " + SNILS + ", " + EGRIPOGRNIP + ", "+ Region + ", " + Address + ", " + Birthdate + ", " + CategoryCode + ", " + month + ")";
                                    inserting = "INSERT into [debtorlist4] values " + " " + "(" + FirstName + ", " + MiddleName + ", " + LastName + ", " +BankruptId+ ", " + INN + ", " + SNILS + ", " + EGRIPOGRNIP + ", "+ Region + ", " + Address + ", " + Birthdate + ", " + CategoryCode + ", " + month + ")";
                                   // System.out.println(inserting);


                            try {
                                st.executeUpdate(inserting);
                            } catch (SQLException e) {
                                System.out.println("Исключение " + e);
                                System.out.println(inserting);
                            }





                        } catch (Exception o) {
                            System.out.println(o + " в файле "+ file+ ": " +FirstName + MiddleName + LastName + Address + Birthdate + Region + LastMessageDate + CategoryCode + SNILS);
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
