package tw.com.aber;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;   
import javax.servlet.ServletContextListener;   
  
public class bystartup implements ServletContextListener {   
  private Timer timer = null;
  public String path;
  public String sp_update_customer_class  = "call sp_update_customer_class()";
  public String dbURL ;
  public String dbUserName ;
  public String dbPassword ;
  public String ret="";
  public void contextInitialized(ServletContextEvent event) {   
	  ServletContext context = event.getServletContext();
      path = context.getInitParameter("uploadpath")+"/log.txt";
      sp_update_customer_class  = "call sp_update_customer_class()";
      dbURL = context.getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
      dbUserName = context.getInitParameter("dbUserName");
      dbPassword = context.getInitParameter("dbPassword");
	  TimerTask task = new TimerTask() {
          @Override
          public void run() {
//        	  String path="C:/log.txt";
              //System.out.println("Start customer classify at: "+new Date());
              try{
              	String record_log = path;
      			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\n\tStart customer classify---\r\n";
      			FileWriter fw;
      			try{
      				fw = new FileWriter(record_log,true);
      			}catch(FileNotFoundException e){
      				fw = new FileWriter(record_log,false);
      			}
      			fw.write(my_msg);
      			fw.close();
      		  }catch(Exception e){}//System.out.println("Error: "+e.toString());}
              try {
            	Connection con = null;
      			PreparedStatement pstmt = null;
  				Class.forName("com.mysql.jdbc.Driver");
  				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
  				pstmt = con.prepareStatement(sp_update_customer_class);
  				pstmt.executeUpdate();
              } catch (Exception se) {
            	  ret="\tUpdate customer class error with: "+se.toString()+"\r\n";
              }
              
//              try{
//              	TimeUnit.SECONDS.sleep(20);
//              }catch(Exception e){
//              	System.out.println(e.toString());
//              }
              
              
              //System.out.println("Finish customer classify at: "+new Date());
              try{
              	String record_log = path;
      			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\n\tFinish customer classify.\r\n"+ret;
      			FileWriter fw;
      			try{
      				fw = new FileWriter(record_log,true);
      			}catch(FileNotFoundException e){
      				fw = new FileWriter(record_log,false);
      			}
      			fw.write(my_msg);
      			fw.close();
      		}catch(Exception e){}//System.out.println("Error: "+e.toString());}
          }
      };
	  
    //System.out.println("Set Timer to classify customer.");
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.set(year, month, day, 9, 00, 00);
    Date date = calendar.getTime();
    if(date.before(new Date())){
    	calendar.set(year, month, day+1, 9, 00, 00);
    	date = calendar.getTime();
    }
    //System.out.println(day+1+" "+ date.after(new Date())+ "  " +date);
    
    timer = new Timer(true);   
    timer.schedule( task, date, 86400*1000);
//    timer.schedule( task, 0, 86400*1000);//milliseconds
  }   
  
  public void contextDestroyed(ServletContextEvent event) {   
    timer.cancel();   
  }   
}   
  