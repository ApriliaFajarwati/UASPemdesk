/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

/**
 *
 * @author Asus
 */
public class DBConnect {


    private Connection con;     
    private Statement st;     
    private ResultSet rs;
     
     public DBConnect() {
         String url = "jdbc:mysql://localhost:3306/";         
         String dbName = "datanilai";        
         String driver = "com.mysql.jdbc.Driver";         
         String userName = "root";         
         String password = ""; 
         
         try {
             Class.forName(driver).newInstance();             
             con = DriverManager.getConnection(url + dbName, userName, password);             
             st = (Statement) con.createStatement();             
             System.out.println("Koneksi Sukses");         
         } catch (Exception ex) {             
            System.out.println("Error: " + ex);         
         }
     }
     public int getCountMahasiswa(){
         int rowCount = 0;         
         try {             
             String q = "select count(*) as jum from nilai";             
             rs = st.executeQuery(q);             
             rs.next();             
             rowCount = rs.getInt("jum");                      
         } catch (Exception ex) {             
             System.out.println("Error: " + ex);         
         }         
         return rowCount;     
     } 
     public Object[][] getDataMahasiswa() {
         Object[][]row = new Object[1000][3];
         try {
             String query = "select * from nilai";             
             rs = st.executeQuery(query);             
             int i = 0;             
             while (rs.next()) {                 
                 row[i][0] = rs.getString("nama");
                 row[i][1] = rs.getString("harian1"); 
                 row[i][2] = rs.getString("harian2"); 
                 row[i][3] = rs.getString("harian3");
                 row[i][4] = rs.getString("UTS");
                 row[i][5] = rs.getString("UAS");
                 i++;
             }
         } catch (Exception ex) {
             System.out.println("Error:" +ex);
         }
         return row;
     }
     public void setDataMahasiswa(String nama, String harian1, String harian2, String harian3, String UTS, String UAS) {         
        try {             
            String q = "select max(id) as maks from nilai";             
            rs = st.executeQuery(q);             
            rs.next();             
            int rowCount = rs.getInt("maks");             
            rowCount = rowCount+1;                          
            
            Calendar calendar = Calendar.getInstance();             
            java.sql.Timestamp lastUpdate =                   
                    new java.sql.Timestamp(calendar.getTime().getTime());  
            
            String query = " insert into nilai(nama, harian1, harian2, harian3, UTS, UAS, lastUpdate, userUpdate)"                     
                    + " values (?, ?, ?, ?, ?, ?)";  
            
            PreparedStatement preparedStmt =                  
                        (PreparedStatement) con.prepareStatement(query);                          
            preparedStmt.setInt(1, rowCount);             
            preparedStmt.setString(2, nama);             
            preparedStmt.setString(3, harian1);             
            preparedStmt.setString(4, harian2);
            preparedStmt.setString(5, harian3); 
            preparedStmt.setString(6, UTS); 
            preparedStmt.setString(7, UAS); 
            preparedStmt.setTimestamp(8, lastUpdate);             
            preparedStmt.setInt(9, 1);  
            
            preparedStmt.execute();  
        } catch (Exception ex) {             
            System.out.println("Error: " + ex);         
        }     
    }
     public void deleteMahasiswa(int kd) {         
         try {             
             String query = " delete from nilai where nama = ?";
         PreparedStatement preparedStmt =                  
                 (PreparedStatement) con.prepareStatement(query);                         
         preparedStmt.setInt(1, kd);             
         preparedStmt.execute();             
         preparedStmt.close();         
         } catch (Exception ex) {             
             System.out.println("Error: " + ex);
         }
     }
     public void updateMahasiswa(String harian1, String harian2, String harian3, String UTS, String UAS, String nama) {         
         try {             
             Calendar calendar = Calendar.getInstance();             
             java.sql.Timestamp lastUpdate =                  
                     new java.sql.Timestamp(calendar.getTime().getTime());                                     
             String query = " update nilai set  nama = ?, harian1 = ? , harian2 = ?, harian3 = ?, UTS = ?, UAS = ?              "
                     + "lastUpdate = ?  where nama = ?";             
             PreparedStatement preparedStmt =                  
                     (PreparedStatement) con.prepareStatement(query);                             
             preparedStmt.setString(1, harian1);             
             preparedStmt.setString(2, harian2);
             preparedStmt.setString(3, harian3); 
             preparedStmt.setString(4, UTS);
             preparedStmt.setString(5, UAS);
             preparedStmt.setTimestamp(6, lastUpdate);             
             preparedStmt.setString(7, nama);             
             preparedStmt.execute();             
             preparedStmt.close();                      
         } catch (Exception ex) {             
             System.out.println("Error: " + ex);         
         }     
     }
}
