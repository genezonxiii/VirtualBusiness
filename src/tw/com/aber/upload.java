package tw.com.aber;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.config.Lookup;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.HttpClientBuilder;

import java.util.UUID;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*; 
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.TimeUnit;
public class upload  extends HttpServlet {

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
//		   int temp=0;
//		   String _uid= UUID.randomUUID().toString();
//		   String _group_id = new String(Base64.encodeBase64String("group_id".getBytes()));
//		   //#######測試叫webservice################
//		   HttpClient client = new HttpClient(); 
//		   //client.getHostConfiguration().setProxy("192.168.112.171",80); 
//		   HttpMethod method=new GetMethod("http://192.168.112.171:8099"); 
//		   client.executeMethod(method); 
//		   
//		   //System.out.println(method.getStatusLine()); 
//		   System.out.println(method.getResponseBodyAsString()); 
//		   method.releaseConnection(); 
		   //int tepp=0;
		   //if(tepp==0)return;
//		   String url = "http://192.168.112.171:8099/";
//		   HttpGet httpRequest = new HttpGet(url);
//		   HttpClient client = HttpClientBuilder.create().build();
//		   HttpResponse httpResponse;
//		   try {
//			   StringBuffer result = new StringBuffer();
//			   httpResponse = client.execute(httpRequest);
//			   String responseString = new BasicResponseHandler().handleResponse(httpResponse);
//			   System.out.println(responseString);
//			   
//			   
//			   int responseCode = httpResponse.getStatusLine().getStatusCode();
//			   System.out.println("hello"+responseString);
//		   } catch (Exception e) {
//				e.printStackTrace();
//			}	
//		   
//		   
		   //########測試叫webservice###############
		   //System.out.println("隨便產生一個uuid: "+_uid+" \n group_id的base64: "+_group_id+"\n");
		   
		   //if(temp==0)return;
		String conString="",ret="";
		conString=putFile(request, response);
		try{
			TimeUnit.SECONDS.sleep(2);
		}catch(Exception e){
			ret="Sleep error";
		}
		if(conString.charAt(0)!='E'){
			ret=webService(request, response,conString);
		}else{
			ret=conString;
		}

		request.setAttribute("action",ret);
		RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
		successView.forward(request, response);
		//############################################################
		return ;
	}
	
	protected String putFile(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String conString="",ret="";
		request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    String vender = request.getParameter("vender");
	    String group_id = request.getSession().getAttribute("group_id").toString();
	    String user_id = request.getSession().getAttribute("user_id").toString();
	    user_id =(user_id==null||user_id.length()<3)?"UNKNOWN":user_id;
	    group_id=(group_id==null)?"UNKNOWN":group_id;
	    vender  =(vender==null)?"UNKNOWN":vender;
	    String _uid= UUID.randomUUID().toString();
		//_uid="454c9c52-cb76-46d3-bf4e-e3ae820c8064";
		String no_way = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/"+group_id+"/"+_uid;
		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender).mkdir();
		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/"+group_id).mkdir();
		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/fail").mkdir();
		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		if (contentType!=null && (contentType.indexOf("multipart/form-data") >= 0)) {
		      DiskFileItemFactory factory = new DiskFileItemFactory();
		      factory.setSizeThreshold(maxMemSize);
		      String file_over=getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/fail";
		      factory.setRepository(new File(file_over));
		      ServletFileUpload upload = new ServletFileUpload(factory);
		      upload.setSizeMax( maxFileSize );
		      try{ 
		         List fileItems = upload.parseRequest(request);
		         Iterator i = fileItems.iterator();
		         while ( i.hasNext () ) 
		         {
		            FileItem fi = (FileItem)i.next();
		            if ( !fi.isFormField () ) {
		                String fileName = fi.getName();
		                String[] tmp = fileName.split("\\.");
		                int j=0;
		                while(j<tmp.length){j++;}
		                j=j>0?j-1:j;
		                String fullname= no_way+"."+tmp[j];
		                //System.out.println("fullname: "+fullname);
						conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
								+"/upload/urls="
								+new String(Base64.encodeBase64String((fullname).getBytes()))
								+"&usid="
								+new String(Base64.encodeBase64String(user_id.getBytes()));
		                File file ;
		                file = new File(fullname) ;
		                fi.write( file ) ;
		                //System.out.println("success");
		            }
		         }
		      }catch(Exception ex) {
		    	  //System.out.println("ERROR: "+ ex.toString());
		          ret="E_write_File:"+ex.toString();
		          return ret;
		      }
		   }else{
			   ret="E_No one found.";
			   return ret;
		   }
		if(ret.length()>3){return ret;}
		return conString;
	}
	protected String webService(HttpServletRequest request,HttpServletResponse response,String conString) throws ServletException, IOException {
		String ret="";
		HttpClient client = new HttpClient();
		HttpMethod method=new GetMethod(conString); 
		try{
			client.executeMethod(method);
		}catch(Exception e){
			ret=e.toString();
			ret="Error of call webservice:"+ret; 
		}
		if("success".compareTo(method.getResponseBodyAsString())!=0){
			ret="Error_Connection: "+conString;
		}else{
			ret="success";
		}
		method.releaseConnection();
		return ret;
	}
}
