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

public class upload_0803  extends HttpServlet {

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
		   
		   request.setCharacterEncoding("UTF-8");
		   String vender = request.getParameter("vender");
		   String group_id = request.getSession().getAttribute("group_id").toString();
		   String user_id = request.getSession().getAttribute("user_id").toString();
		   user_id =(user_id==null||user_id.length()<3)?"UNKNOWN":user_id;
		   group_id=(group_id==null)?"UNKNOWN":group_id;
		   vender  =(vender==null)?"UNKNOWN":vender;
		   //String upload_root=getServletConfig().getServletContext().getInitParameter("uploadpath");
		   //############################################################
		    String _uid= UUID.randomUUID().toString();
			//_uid="454c9c52-cb76-46d3-bf4e-e3ae820c8064";
			String no_way = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/"+group_id+"/"+_uid;
			new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender).mkdir();
			new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/"+vender+"/"+group_id).mkdir();
		   
		   
		   
//		   String msg="Have Done:<br>";
//		   new File(upload_root+"/"+vender).mkdir();
//		   new File(upload_root+"/"+vender+"/"+group_id).mkdir();
//		   String filePath =upload_root+"/"+vender+"/"+group_id+"/";
//		   //if(bool)msg+="&nbsp&nbspmkdir "+upload_root+"/"+vender+"<br>";
		   //if(bool2)msg+="&nbsp&nbspmkdir "+upload_root+"/"+vender+"/"+group_id+"<br>";
		   //msg+=(bool0==true?"a":"X")+(bool00==true?"b":"X")+(bool000==true?"c":"X")+"@@@";
		   int maxFileSize = 5000 * 1024;
		   int maxMemSize = 5000 * 1024;
		   String contentType = request.getContentType();
		   //System.out.println("content: " + contentType+" "+bool+bool2);
		   if (contentType!=null && (contentType.indexOf("multipart/form-data") >= 0)) {
		      DiskFileItemFactory factory = new DiskFileItemFactory();
		      factory.setSizeThreshold(maxMemSize);
		      factory.setRepository(new File("c:\\temp2"));
		      ServletFileUpload upload = new ServletFileUpload(factory);
		      upload.setSizeMax( maxFileSize );
		      try{ 
		         List fileItems = upload.parseRequest(request);
		         Iterator i = fileItems.iterator();
		         //out.println("<html><body>");
		         while ( i.hasNext () ) 
		         {
		            FileItem fi = (FileItem)i.next();
		            if ( !fi.isFormField () ) {
		                String fieldName = fi.getFieldName();
		                String fileName = fi.getName();
		                String[] tmp = fileName.split("\\.");
		                int j=0;
		                while(j<tmp.length){j++;}
		                j=j>0?j-1:j;
		                boolean isInMemory = fi.isInMemory();
		                long sizeInBytes = fi.getSize();
		                String fullname= no_way+"."+tmp[j];
						String conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
								+"/upload/urls="
								+new String(Base64.encodeBase64String((fullname).getBytes()))
								+"&usid="
								+new String(Base64.encodeBase64String(user_id.getBytes()));
		                File file ;
		                file = new File(fullname) ;
		                fi.write( file ) ;
		                //out.println("Uploaded Filename: " + fileName +"<br>&nbsp--> "+filePath+tmp[j]+ "<br>");
		                //msg+="&nbsp&nbspupload "+fileName+"<br>&nbsp -->&nbsp"+filePath+tmp[j];
		                //System.out.println("upload: "+fileName+"\n    --> "+filePath+tmp[j]);
		                HttpClient client = new HttpClient();
						HttpMethod method=new GetMethod(conString); 
						try{
							client.executeMethod(method);
						}catch(Exception e){
							request.setAttribute("action","WebServiceError: "+e.toString());
							RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
							successView.forward(request, response);
							return;
						}
						if("success".compareTo(method.getResponseBodyAsString())!=0){
							request.setAttribute("action","Connection error: "+conString);
						}else{
							request.setAttribute("action","success");
						}
						method.releaseConnection();
		                RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
						successView.forward(request, response);
						return;
		            }
		         }
		         //out.println("</body></html>");
		      }catch(Exception ex) {
		         //System.out.println(ex);
		         request.setAttribute("action",ex.toString());
		         RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
				successView.forward(request, response);
		      }
		   }else{
			   request.setAttribute("action","No one found.");
			   RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
			   successView.forward(request, response);
		       //out.println("<html><body><p>No file uploaded</p></body></html>");
		   }
	}
}
