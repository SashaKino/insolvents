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


        File dir = new File("C:\\Source\\debtorlist\\");
        File[] list = dir.listFiles();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String FirstName, MiddleName, LastName, Address, Birthdate, Region, LastMessageDate, CategoryCode,SNILS, INN, month = "NULL";
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

                        FirstName = MiddleName = LastName = Address = Birthdate = Region = LastMessageDate = CategoryCode = SNILS = INN = "NULL";


                        try {
                            DebtorPersonNM = DebtorPerson.getAttributes();

                            FirstName ="'"+ DebtorPersonNM.getNamedItem("FirstName").getTextContent()+"'";

                            try {
                                MiddleName ="'"+ DebtorPersonNM.getNamedItem("MiddleName").getTextContent()+"'";
                            } catch (Exception e) {
                                System.out.println(e+ " -MiddleName"  );
                            }


                            LastName ="'"+  DebtorPersonNM.getNamedItem("LastName").getTextContent()+"'";




                            try {
                                INN = DebtorPersonNM.getNamedItem("INN").getTextContent();
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
                                System.out.println(e+ " Birthdate "  );
                            }


                            if (Address.length() > 198)
                                Address = DebtorPersonNM.getNamedItem("Adress").getTextContent().substring(0, 198);
                                Address ="'"+ Address + "'";


                            inserting = "INSERT into [debtorlist2] values " + " " + "(" + FirstName + ", " + MiddleName + ", " + LastName + ", " + INN + ", " + SNILS + ", " + Region + ", " + Address + ", " + Birthdate + ", " + CategoryCode + ", " + month + ")";
                            //System.out.println(inserting);


                            try {
                                st.executeUpdate(inserting);
                            } catch (SQLException e) {
                                System.out.println("Исключение " + e);
                                System.out.println(inserting);
                            }


                        } catch (Exception o) {
                            System.out.println(FirstName + MiddleName + LastName + Address + Birthdate + Region + LastMessageDate + CategoryCode + SNILS);
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
