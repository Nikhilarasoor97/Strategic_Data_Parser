import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.oreilly.servlet.MultipartRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*; 
import com.sun.rowset.WebRowSetImpl;  
import java.io.FileOutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.sun.rowset.WebRowSetImpl;
import java.awt.Desktop;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
@MultipartConfig
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
   @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) 
            throws ServletException, IOException {
                response.setContentType("text/html");
                OutputStream out = null;
                InputStream filecontent = null;
                PrintWriter writer = response.getWriter();
                Part filePart = request.getPart("fname");
                String path="";
                String fileName = filePart.getSubmittedFileName();
                int index= fileName.indexOf(".");
                String contenttype=fileName.substring(index);
                if(fileName.contains("."))
                {
                 if(contenttype.equals(".txt")){
                       path="D:\\sdp\\textfile";
                       mkdir(path);
                }else if(contenttype.equals(".pdf")){
                       path="D:\\sdp\\pdffile";
                       mkdir(path);
                }else if(contenttype.equals(".docx")){
                       path="D:\\sdp\\docxfile";
                       mkdir(path);
                }else if(contenttype.equals(".xlsx")){
                       path="D:\\sdp\\Excelfile";
                       mkdir(path);  
                }else if(contenttype.equals(".xml")){
                       path="D:\\sdp\\xmlfile";
                       mkdir(path);  
                }else{
                       writer.println("File name cannot be accepted");
                    }
                }
                FileOutputStream r = new FileOutputStream(new File(path + File.separator+ fileName));
                filecontent = filePart.getInputStream();
                try {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = filecontent.read(buffer)) > 0) {
                        r.write(buffer, 0, length);
                        }
                    try{
                        if(contenttype.equals(".txt")){
                        path="D:\\sdp\\textfile";
                        mkdir(path);
                        String read=null;
                        BufferedReader in = new BufferedReader(new FileReader(new File(path + File.separator+ fileName))); 
                        while ((read = in.readLine()) != null) {
                           String[] splited = read.split(":");
                           String name = splited[0];
                           String b_name= splited[1];
                           Class.forName("com.mysql.jdbc.Driver");
                           Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bank details","root","1234");
                           Statement stmt = (Statement) con.createStatement();
                           int rs = stmt.executeUpdate("insert into basic_details values('" +name+ "','"+b_name+ "')");
                           if(rs == 1)
                           {
                            System.out.println("Uploaded");
                           }
                           else
                           {
                            System.out.println("Not uploaded");
                           }
                           System.out.println("name is"+name);
                          } 
                        }else if(contenttype.equals(".pdf")){
                            File file1=new File("D:\\sdp\\txtfile\\sample1.txt");
                            convertPDFToText(path + File.separator+ fileName,file1);
                            try{
                                BufferedReader in = new BufferedReader(new FileReader(file1)); 
                                String read;
                                while ((read = in.readLine()) != null) {
                                    String[] splited = read.split(":");
                                    String name = splited[0];
                                    String b_name= splited[1];
                                    Class.forName("com.mysql.jdbc.Driver");
                                    Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bank details","root","1234");
                                    Statement stmt = (Statement) con.createStatement();
                                    int rs = stmt.executeUpdate("insert into basic_details values('" +name+ "','"+b_name+"')");
                                    if(rs == 1) {
                                        System.out.println("Uploaded");
                                    }else{
                                        System.out.println("Not uploaded");
                                    }
                                        System.out.println("name is"+name);
                                    }
                                }catch(Exception e){
                                    System.out.println(e);
                            }
                        System.out.println("Conversion complete");
                        }else if(contenttype.equals(".xml")){
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                                java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank details", "root", "1234");
                                java.sql.Statement st=con.createStatement();
                                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                                org.w3c.dom.Document doc = docBuilder.parse (new File(path + File.separator+ fileName));
                                doc.getDocumentElement().normalize();
                                System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                                NodeList listOfPersons = doc.getElementsByTagName("employee");
                                for(int s=0; s<listOfPersons.getLength(); s++){
                                    Node firstPersonNode = listOfPersons.item(s);
                                    if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){
                                        Element firstPersonElement = (Element)firstPersonNode;
                                        NodeList nameList = firstPersonElement.getElementsByTagName("name");
                                        Element nameElement =(Element)nameList.item(0);
                                        NodeList textFNList = nameElement.getChildNodes();
                                        String name=((Node)textFNList.item(0)).getNodeValue().trim();
                                        NodeList addressList = firstPersonElement.getElementsByTagName("address");
                                        Element addressElement =(Element)addressList.item(0);
                                        NodeList textLNList = addressElement.getChildNodes();
                                        String address= ((Node)textLNList.item(0)).getNodeValue().trim();
                                        int i=st.executeUpdate("insert into basic_details values('"+name+"','"+address+"')");
                                    }
                                }
                                System.out.println("Data is successfully inserted!");
                            }catch (Exception err) {
                                System.out.println(" " + err.getMessage ());
                            }
                        }
                        Class.forName ("com.mysql.jdbc.Driver"); 
                        java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank details", "root", "1234");
                        try {
                            java.sql.Statement stmt = conn.createStatement();
                            try {
                                ResultSet query_set = stmt.executeQuery("select * from basic_details");
                   try {
                        WebRowSetImpl my_xml_data = new WebRowSetImpl();
                        my_xml_data.populate(query_set);
                        my_xml_data.writeXml(new FileOutputStream("D://output1.xml"));
                    } 
                    finally {
                            try { query_set.close(); 
                            } 
                            catch (Exception ignore) {
                        }
                    }
                } 
                finally {
                        try { 
                            stmt.close();
                        } catch (Exception ignore) {
                    }
                }
            } 
            finally {
                try { conn.close(); } catch (Exception ignore) {}
        }
                    }   
                    catch (Exception ex) {
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }                
                    }
                    finally {
                        out.write(1);
                    }
    }

    private void mkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public static void convertPDFToText(String src,File file){
     try{
                    //create file writer
        FileWriter fw=new FileWriter(file);
                    //create buffered writer
        BufferedWriter bw=new BufferedWriter(fw);
                    //create pdf reader
        PdfReader pr=new PdfReader(src);
                    //get the number of pages in the document
        int pNum=pr.getNumberOfPages();
                    //extract text from each page and write it to the output text file
        for(int page=1;page<=pNum;page++){
        String text=PdfTextExtractor.getTextFromPage(pr, page);
        bw.write(text);
        bw.newLine();
        }
        bw.flush();
        bw.close();
        }catch(Exception e){e.printStackTrace();}
  
    }
}