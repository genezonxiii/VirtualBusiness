package tw.com.aber;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.config.Lookup;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.HttpClientBuilder;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.output.*;
import org.apache.commons.codec.binary.Base64;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*; 
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.TimeUnit;
public class test  extends HttpServlet {
	static String path="";
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("group_id")==null){request.setAttribute("action","no_session");RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");successView.forward(request, response);return;}
		
		//##########################test####################################
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String some = request.getParameter("some");
		//新增幾個功能
		//tbsale有很多無主訂單 主要是給 customer_id建檔
		//訂單物品沒有類別單位
		//
		//
		//
		
		
		
		
		if("test".equals(action)){
			path=getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
			showTimer();
			response.getWriter().write("Set timer for customer classify success.");
			return;
		}
		return;
	}
	public static void showTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Start customer classify at: "+new Date());
                try{
                	String record_log = path;
        			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\nStart customer classify---\r\n";
        			FileWriter fw;
        			try{
        				fw = new FileWriter(record_log,true);
        			}catch(FileNotFoundException e){
        				fw = new FileWriter(record_log,false);
        			}
        			fw.write(my_msg);
        			fw.close();
        		}catch(Exception e){System.out.println("Error: "+e.toString());}
                
                
                try{
                	TimeUnit.SECONDS.sleep(5);
                }catch(Exception e){
                	System.out.println(e.toString());
                }
                
                try{
                	String record_log = path;
        			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\nFinish customer classify.\r\n";
        			FileWriter fw;
        			try{
        				fw = new FileWriter(record_log,true);
        			}catch(FileNotFoundException e){
        				fw = new FileWriter(record_log,false);
        			}
        			fw.write(my_msg);
        			fw.close();
        		}catch(Exception e){System.out.println("Error: "+e.toString());}
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 9, 00, 00);
        Date date = calendar.getTime();
        if(date.after(new Date())){
        	calendar.set(year, month, day+1, 9, 00, 00);
        }
        Timer timer = new Timer();
        //timer.schedule(task, date, 86400 * 1000);
        timer.schedule(task, date, 3600 * 1000);
    }
//	protected String putFile(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
//		String conString="",ret="";
//		request.setCharacterEncoding("UTF-8");
//	    response.setCharacterEncoding("UTF-8");
//	    String vender = request.getParameter("vender");
//	    String group_id = request.getSession().getAttribute("group_id").toString();
//	    String user_id = request.getSession().getAttribute("user_id").toString();
//	    user_id =(user_id==null||user_id.length()<3)?"UNKNOWN":user_id;
//	    group_id=(group_id==null)?"UNKNOWN":group_id;
//	    vender  =(vender==null)?"UNKNOWN":vender;
//	    String _uid= UUID.randomUUID().toString();
//		//_uid="454c9c52-cb76-46d3-bf4e-e3ae820c8064";
//		String no_way = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/"+group_id+"/"+_uid;
//		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender).mkdir();
//		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/"+group_id).mkdir();
//		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/fail").mkdir();
//		int maxFileSize = 5000 * 1024;
//		int maxMemSize = 5000 * 1024;
//		String contentType = request.getContentType();
//		if (contentType!=null && (contentType.indexOf("multipart/form-data") >= 0)) {
//		      DiskFileItemFactory factory = new DiskFileItemFactory();
//		      factory.setSizeThreshold(maxMemSize);
//		      String file_over=getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/fail";
//		      factory.setRepository(new File(file_over));
//		      ServletFileUpload upload = new ServletFileUpload(factory);
//		      upload.setSizeMax( maxFileSize );
//		      try{ 
//		         List fileItems = upload.parseRequest(request);
//		         Iterator i = fileItems.iterator();
//		         while ( i.hasNext () ) 
//		         {
//		            FileItem fi = (FileItem)i.next();
//		            if ( !fi.isFormField () ) {
//		                String fileName = fi.getName();
//		                String[] tmp = fileName.split("\\.");
//		                int j=0;
//		                while(j<tmp.length){j++;}
//		                j=j>0?j-1:j;
//		                String fullname= no_way+"."+tmp[j];
//		                //System.out.println("fullname: "+fullname);
//						conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
//								+"/upload/urls="
//								+new String(Base64.encodeBase64String((fullname).getBytes()))
//								+"&usid="
//								+new String(Base64.encodeBase64String(user_id.getBytes()));
//		                File file ;
//		                file = new File(fullname) ;
//		                fi.write( file ) ;
//		                //System.out.println("success");
//		            }
//		         }
//		      }catch(Exception ex) {
//		    	  //System.out.println("ERROR: "+ ex.toString());
//		          ret="E_write_File:"+ex.toString();
//		          return ret;
//		      }
//		   }else{
//			   ret="E_No one found.";
//			   return ret;
//		   }
//		if(ret.length()>3){return ret;}
//		return conString;
//	}
//	protected String webService(HttpServletRequest request,HttpServletResponse response,String conString) throws ServletException, IOException {
//		String ret="";
//		HttpClient client = new HttpClient();
//		HttpMethod method=new GetMethod(conString); 
//		try{
//			client.executeMethod(method);
//		}catch(Exception e){
//			ret=e.toString();
//			ret="Error of call webservice:"+ret; 
//		}
//		if("success".compareTo(method.getResponseBodyAsString())!=0){
//			ret="Error_Connection: "+conString;
//		}else{
//			ret="success";
//		}
//		
//		method.releaseConnection();
//		return ret;
//	}
}
