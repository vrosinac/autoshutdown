    import java.sql.*;                                                         
    import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

    
    
public class EzJava 
{
    
public  static  Statement stmt;
 public  static ResultSet rs;
 public static int waited_intervals;   
    
  public static void main(String[] args) 
  {
      
      
      /*
      
      C:\autoshutdown> java -jar autoshutdown.jar //C1WC1WC1229.misys.global.ad:50000/
TIPLUS2 TIZONE28 T!Z0N3123

      
      
      */
      
     String urlPrefix = "jdbc:db2:";
    //  String urlPrefix = "jdbc:oracle:";
    String url;
    String user;
    String password;
    String empNo;                                                              
    Connection con;
    
    System.out.println ("**** Enter class EzJava");
    
    // Check the that first argument has the correct form for the portion
    // of the URL that follows jdbc:db2:,
    // as described
    // in the Connecting to a data source using the DriverManager 
    // interface with the IBM Data Server Driver for JDBC and SQLJ topic.
    // For example, for IBM Data Server Driver for 
    // JDBC and SQLJ type 2 connectivity, 
    // args[0] might be MVS1DB2M. For 
    // type 4 connectivity, args[0] might
    // be //stlmvs1:10110/MVS1DB2M.

    if (args.length!=3)
    {
      System.err.println ("Invalid value. First argument appended to "+
       "jdbc:db2: must specify a valid URL.");
      System.err.println ("Second argument must be a valid user ID.");
      System.err.println ("Third argument must be the password for the user ID.");
      System.exit(1);
    }
    url = urlPrefix + args[0];
    user = args[1];
    password = args[2];
    try 
    {       
        
         waited_intervals = 0;
        
      // Load the driver
      Class.forName("com.ibm.db2.jcc.DB2Driver");                              
      System.out.println("**** Loaded the JDBC driver");

      // Create the connection using the IBM Data Server Driver for JDBC and SQLJ
      con = DriverManager.getConnection (url, user, password);                 
      // Commit changes manually
      con.setAutoCommit(false);
      System.out.println("**** Created a JDBC connection to the data source");

      // Create the Statement
      stmt = con.createStatement();                                            
      System.out.println("**** Created JDBC Statement object");

      
      
      
      
      
      
      final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleAtFixedRate(EzJava::myTask, 0, 60, TimeUnit.SECONDS);


      
      
      
    /*  
      // Execute a query and generate a ResultSet instance
     // rs = stmt.executeQuery("SELECT EMPNO FROM EMPLOYEE");                    
      
    // rs = stmt.executeQuery("select master_ref from master"); 
     rs = stmt.executeQuery("select count(*) from  TIGLOBAL28.local_Session_details  where ZONE_ID <> ''  and ENDED IS NULL"); 
    
    
     System.out.println("**** Created JDBC ResultSet object");

      // Print all of the employee numbers to standard output device
      while (rs.next()) {
        //empNo = rs.getI  
        //empNo = rs.getInt(1);
        System.out.println("count of uses logged on zone = " + rs.getString(1));
        
        int nUsers = Integer.parseInt(rs.getString(1));
        if (nUsers >= 0){
            System.out.println("someone is logged in ");
            //we wait 1 more minute
            wait = wait + 1;
            
            
            
            
        }
        
        
        
        
      }

*/
      System.out.println("**** Fetched all rows from JDBC ResultSet");
      // Close the ResultSet
      rs.close();
      System.out.println("**** Closed JDBC ResultSet");
      
      // Close the Statement
      stmt.close();
      System.out.println("**** Closed JDBC Statement");

      // Connection must be on a unit-of-work boundary to allow close
      con.commit();
      System.out.println ( "**** Transaction committed" );
      
      // Close the connection
      con.close();                                                             
      System.out.println("**** Disconnected from data source");

      System.out.println("**** JDBC Exit from class EzJava - no errors");

    }
    
    catch (ClassNotFoundException e)
    {
      System.err.println("Could not load JDBC driver");
      System.out.println("Exception: " + e);
      e.printStackTrace();
    }

    catch(SQLException ex)                                                      
    {
      System.err.println("SQLException information");
      while(ex!=null) {
        System.err.println ("Error msg: " + ex.getMessage());
        System.err.println ("SQLSTATE: " + ex.getSQLState());
        System.err.println ("Error code: " + ex.getErrorCode());
        ex.printStackTrace();
        ex = ex.getNextException(); // For drivers that support chained exceptions
      }
    }
  }  // End main
  
  
  
  private static void myTask() 
  {
    System.out.println("Running");

    try
    {
        rs = stmt.executeQuery("select count(*) from  TIGLOBAL28.local_Session_details  where ZONE_ID <> ''  and ENDED IS NULL"); 


         System.out.println("**** Created JDBC ResultSet object");

          // Print all of the employee numbers to standard output device
          while (rs.next()) {
            //empNo = rs.getI  
            //empNo = rs.getInt(1);
            System.out.println("count of uses logged on zone = " + rs.getString(1));

            int nUsers = Integer.parseInt(rs.getString(1));
            if (nUsers >= 0){
                System.out.println("someone is logged in ");
                //we wait 1 more minute
                waited_intervals = 0;




            }
            else
            {
                if (waited_intervals > 60)
                {
                //we shutdown
                    System.out.println("no users logged in for  " + waited_intervals + " minutes, we will shutdown.");
                    System.exit(0);
                }
                else
                {
                    int still_to_wait = 60 -waited_intervals;
                    System.out.println("no users logged in for  " + waited_intervals + " minutes, we wait another " + still_to_wait + " minutes.");

                    //we wait 1 mor minute
                    waited_intervals = waited_intervals +1;
                    
                    
                }
            
            
            }
        }
    }
    
  
    catch(SQLException ex)                                                      
    {
      System.err.println("SQLException information");
      while(ex!=null) {
        System.err.println ("Error msg: " + ex.getMessage());
        System.err.println ("SQLSTATE: " + ex.getSQLState());
        System.err.println ("Error code: " + ex.getErrorCode());
        ex.printStackTrace();
        ex = ex.getNextException(); // For drivers that support chained exceptions
      }

    }
  }
  
}    // End EzJava