/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsql;

import java.io.BufferedReader;
import java.sql.*;
import javax.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Nomi & Furqi
 */
public class TestSql {

    // JDBC driver name and database URL

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/city";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        try {

            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            FileInputStream fstream = new FileInputStream("E:/GeoLiteCity-Location.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            int x = 0;
            String strLine;
            while ((strLine = br.readLine()) != null ) {
                x++;
                List<String> items = Arrays.asList(strLine.split("\\s*,\\s*"));

                String locId = items.get(0);
                //System.out.print("    "+locId+"     ");
                String country = items.get(1);
                //System.out.print("    "+y+"     ");

                String reg = items.get(2);
                //System.out.print("    "+y+"     ");
                String city = items.get(3);
                //System.out.print("    "+y+"     ");

                String postalCode = items.get(4);
                //System.out.print("    "+y+"     ");
                String latitude = items.get(5);
                //System.out.print("    "+y+"     ");

                String longitude = items.get(6);
                //System.out.print("    "+y+"     ");
                //String metroCode=items.get(7);
                System.out.println(items.get(0));
                stmt = conn.createStatement();
                String qu = "INSERT INTO record (locId, country, region, city, postalCode, latitude,longitude,metroCode,areaCode) ";
                sql = qu;
                locId = locId.trim();
                //jhangSystem.out.print("Converting this   "+locId+"\n");
                int foo = Integer.parseInt(locId);
                String metroCode = null;
                sql = sql + "VALUES ('" + foo + "','" + country + "','" + reg + "','" + city + "','" + postalCode + "','" + latitude + "','" + longitude + "','" + metroCode + "','" + "code" + "')";

                 stmt.executeUpdate(sql);
            }
            
            // Tak 1 Search city Longitude and Latitude 
            System.out.print("Task 1 Search City and find Latitude and Longitude\nPlease Enter the name of city:  ");
            Scanner cin = new Scanner(System.in);  // Reading from System.i
            String city = cin.nextLine();

            sql = "SELECT * FROM record WHERE record.city LIKE \"%" + city + "%\"";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //Retrieve by column name
                String first = rs.getString("locId");
                String lat = rs.getString("latitude");
                String lng = rs.getString("longitude");

                //Display values
                System.out.println("Congrats, City Found\n LocalID: " + first);
                System.out.println("latitude is given by: " + lat);
                System.out.println("Longitutde is given by: " + lng);
            }

            //////  task 3   Search Near Bt Cities
            System.out.print("\nTak 2 Search Near By cities \nPlease Enter the name of city for nearby Cities :  ");
            city = cin.nextLine();

            sql = "SELECT * FROM record WHERE record.city LIKE \"%" + city + "%\"";
            rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            String lat1 = null;
            String lang1 = null;
            String lat2 = null;
            String lang2 = null;
            String nearCity;
            while (rs.next()) {
                //Retrieve by column name
                String first = rs.getString("locId");
                nearCity = rs.getString("city");
                lat1 = rs.getString("latitude");
                lang1 = rs.getString("longitude");
                break;
            }

            double L1 = 59.9;
            double G1 = 30.3;
            double L2 = 37.8;
            double G2 = 122.4;
            L1 = Double.parseDouble(lat1);
            G1 = Double.parseDouble(lang1);

            sql = "SELECT * FROM record ";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //Retrieve by column name
                String first = rs.getString("locId");
                nearCity = rs.getString("city");
                lat2 = rs.getString("latitude");
                lang2 = rs.getString("longitude");

                L2 = Double.parseDouble(lat2);
                G2 = Double.parseDouble(lang2);

                // convert to radians
                L1 = Math.toRadians(L1);
                L2 = Math.toRadians(L2);
                G1 = Math.toRadians(G1);
                G2 = Math.toRadians(G2);

                // do the spherical trig calculation
                double angle = Math.acos(Math.sin(L1) * Math.sin(L2)
                        + Math.cos(L1) * Math.cos(L2) * Math.cos(G1 - G2));

                // convert back to degrees
                angle = Math.toDegrees(angle);

                // each degree on a great circle of Earth is 69.1105 miles
                double distance = 69.1105 * angle;
                if (distance < 500) {
                    System.out.println(" City with the Name: " + nearCity + " And ID: " + first + " Found at Distance: " + distance + " miles");
                }

            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end FirstExample
