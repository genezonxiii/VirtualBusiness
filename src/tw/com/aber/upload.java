package tw.com.aber;

import org.apache.commons.codec.binary.Hex;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.IOUtils;
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
	public String file_name = "";
	public String ori_file_name = "";
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getSession().getAttribute("group_id")==null){request.setAttribute("action","no_session");RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");successView.forward(request, response);return;}
		if(request.getParameter("vender")==null){request.setAttribute("action","no_vender");RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");successView.forward(request, response);return;}
//		request.setCharacterEncoding("UTF-8");
//	    response.setCharacterEncoding("UTF-8");
//		request.setAttribute("action","succes2s");
//		RequestDispatcher successView2 = request.getRequestDispatcher("/upload.jsp");
//		successView2.forward(request, response);
//		int mm=0;
//		if(mm==0)return;
		//##########################test####################################
//		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		String action = request.getParameter("action");
//		if("test".equals(action)){
//			String name = request.getParameter("name");
//			System.out.println("go into:"+action);
//			try{TimeUnit.SECONDS.sleep(15);}catch(Exception e){}
//			System.out.println("alive to say hello to:"+name);
//			response.getWriter().write("I'm back!");
//			int ii=0;if(ii==0)return;
//		}
		//##########################test####################################
		
//		try{
//			String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
//			String processName =java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
//			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\n  I'm "+request.getSession().getAttribute("group_id")+" upload " +file_name+ " with PID = "+ Long.parseLong(processName.split("@")[0])+".\r\n";
//			FileWriter fw;
//			try{
//				fw = new FileWriter(record_log,true);
//			}catch(FileNotFoundException e){
//				fw = new FileWriter(record_log,false);
//			}
//			fw.write(my_msg);
//			fw.close();
//		}catch(Exception e){System.out.println("Error: "+e.toString());}
		
		
		
		//FileWriter fw = new FileWriter("C:/Users/10506002/Desktop/hello.txt",false);
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
		String conString="",ret="E";
		conString=putFile(request, response);
		
		try{
			String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
			String processName =java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
			String my_msg =(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+":\r\n  I'm "+request.getSession().getAttribute("user_name")+" upload "+ori_file_name+" as -> " +file_name+ " with PID = "+ Long.parseLong(processName.split("@")[0])+".\r\n";
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
			TimeUnit.SECONDS.sleep(2);
		}catch(Exception e){
			ret="Sleep error";
		}
		//System.out.println(conString);
		if(conString.charAt(0)!='E'){
			ret=webService(request, response,conString);
		}else{
			ret=conString;
		}
		ret=((ret==null)?"E":ret);
		request.setAttribute("action",ret);
		RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
		successView.forward(request, response);
		try{
			String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
			String my_msg ="  Result as \'"+ret+"\'.At ("+(new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss").format(new Date()))+")\r\n";
			FileWriter fw;
			try{
				fw = new FileWriter(record_log,true);
			}catch(FileNotFoundException e){
				fw = new FileWriter(record_log,false);
			}
			fw.write(my_msg);
			fw.close();
		}catch(Exception e){System.out.println("Error: "+e.toString());}
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
		new File(getServletConfig().getServletContext().getInitParameter("uploadpath")+"/fail").mkdir();
		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		if (contentType!=null && (contentType.indexOf("multipart/form-data") >= 0)) {
		      DiskFileItemFactory factory = new DiskFileItemFactory();
		      factory.setSizeThreshold(maxMemSize);
		      String file_over=getServletConfig().getServletContext().getInitParameter("uploadpath")+"/fail";
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
		                ori_file_name=fi.getName();
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
		                
		                InputStream is = fi.getInputStream();
		        		byte[] first = new byte[5] ;
		        		is.read(first, 0, 5);
		        		is.close();
		        		byte[] bytePDF = new byte[]{0x25, 0x50, 0x44, 0x46, 0x2D};
		        		byte[] byteXLS = new byte[]{(byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0, (byte) 0xA1};//, (byte) 0xB1, 0x1A, (byte) 0xEA};
//		        		byte[] byteCSV = new byte[]{0x5B, 0x75, 0x72, 0x6C};
		        		byte[] byteXLSX = new byte[]{0x50, 0x4B, 0x03, 0x04, 0x14};
//		        		System.out.println(Hex.encodeHexString(first));
//		        		System.out.println(Arrays.toString(first));
		        		if(Arrays.equals(first, bytePDF)){
		        			fullname = no_way+".pdf";
		        		}else if(Arrays.equals(first, byteXLS)){
		        			fullname = no_way+".xls";
		        		}else if(Arrays.equals(first, byteXLSX)){
		        			fullname = no_way+".xlsx";
		        		}else{
//		        			System.out.println(fi.getString("UTF-8"));
		        			String filecontent_ori= fi.getString("UTF-8");
			            	String[] filecontent = filecontent_ori.split(",");
			            	if(filecontent_ori.contains("content='text/html;")){
			            		fullname = no_way+".html";
			            	}else{
				            	j=0;
				            	while(j<filecontent.length){
//				            		System.out.println(filecontent[j].length()+" "+filecontent[j]);
				            		if(filecontent[j].length()>80){break;}
				            		j++;
				            	}
				            	if(j==filecontent.length){
				            		fullname = no_way+".csv";
				            	}else{
				            		fullname = no_way+".txt";
				            	}
			            	}
		        		}
		        		//fullname = no_way+"."+tmp[j];
		                file_name=fullname;
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
		try{
			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			String content=ret=writer.toString();
			//String content=method.getResponseBodyAsString();
			if("success".compareTo(content)!=0){
				if(content.length()>100){
					content=content.substring(0,90)+"....";
				}
				ret="Error_Connection: get "+content+" on: "+conString;
			}else{
				ret="success";
			}
		}catch(Exception e){
			ret=e.toString();
			ret="Error of call webservice content:"+ret; 
		}
		method.releaseConnection();
		return ret;
	}
}
