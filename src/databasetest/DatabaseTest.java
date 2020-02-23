package databasetest;
import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {

    public static void main(String[] args) {
        
        getJSONData();
    }
    
    public static JSONArray getJSONData(){
        
        Connection conn = null;
        PreparedStatement pstSelect = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String query, key, value;
        
        boolean hasresults;
        int resultCount, columnCount;
        
        //create new JSON array
        JSONArray jsonArray = new JSONArray();
        
        try{
            
            //Identify the Server 
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "Catherinej13*mysql";
            System.out.println("Connecting to " + server + "...");
            
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            // Open Connection 
            conn = DriverManager.getConnection(server, username, password);
            
            //Prepare Select Query
            query = "SELECT * FROM people";
            pstSelect = conn.prepareStatement(query);
            hasresults = pstSelect.execute();
            
            //iterate through the data
            while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                if ( hasresults ) {

                    //Get ResultSet Metadata
                    resultset = pstSelect.getResultSet();
                    metadata = resultset.getMetaData();
                    columnCount = metadata.getColumnCount();

                    //get data from table
                    while(resultset.next()) {
                        
                        //create new JSON Object
                        JSONObject jsonObject = new JSONObject();
                        
                        for (int i = 2; i <= columnCount; i++) {
                            
                            //get column names
                            key = metadata.getColumnLabel(i);
                            
                            //get data
                            value = resultset.getString(i);
                            
                            //put data into json object
                            jsonObject.put(key, value); 
                        }
                        
                    //add objects to array
                    jsonArray.add(jsonObject);
                    }
                }
                
                else {

                    resultCount = pstSelect.getUpdateCount();  

                    if ( resultCount == -1 ) {
                        break;
                    }
                }
                    
            //Check for More Data
            hasresults = pstSelect.getMoreResults();

            }
            
        //Close Database Connection
        conn.close();
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
         
        return jsonArray;      
    }       
}