package tw.com.aber;

import java.io.FileNotFoundException;
import java.io.FileWriter;
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
  public void contextInitialized(ServletContextEvent event) {   
	  ServletContext context = event.getServletContext();
      path = context.getInitParameter("uploadpath")+"/log.txt";
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
              
              try{
              	TimeUnit.SECONDS.sleep(20);
              }catch(Exception e){
              	System.out.println(e.toString());
              }
              
              
              //System.out.println("Finish customer classify at: "+new Date());
              try{
              	String record_log = path;
      			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\n\tFinish customer classify.\r\n";
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
    if(date.after(new Date())){
    	calendar.set(year, month, day+1, 9, 00, 00);
    }
    
    timer = new Timer(true);   
    timer.schedule( task, 0, 86400*1000);
//    timer.schedule( task, 0, 86400*1000);//milliseconds
  }   
  
  public void contextDestroyed(ServletContextEvent event) {   
    timer.cancel();   
  }   
}   
  