package tw.com.aber;

import org.apache.commons.codec.binary.Hex;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.config.Lookup;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.HttpClientBuilder;
import java.util.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.*;

import com.google.gson.Gson;

import tw.com.aber.upload.Throwfile;

import org.apache.commons.codec.binary.Base64;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*; 
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.TimeUnit;
public class groupbuying  extends HttpServlet {
	public String file_name = "";
	public String ori_file_name = "";
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    String action = request.getParameter("action");
	    if("select_platform_kind".equals(action)){
			final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
					+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
			final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
			Connection con = null;
			Statement statement = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				statement = con.createStatement();
				String cmd ="SELECT * FROM tb_throwfile WHERE throwfile_type = 'general' and group_buying = 'true' ORDER BY throwfile_id ASC ";
				rs = statement.executeQuery(cmd);
				int count=0;
			    if (rs.last()){count = rs.getRow();}else{count = 0;}
			    Throwfile[] result=new Throwfile[count];
			    
			    rs.beforeFirst();count=0;
			    while (rs.next()) {
			    	result[count]= new Throwfile();
			    	result[count].throwfile_name=rs.getString("throwfile_name");
			    	result[count].throwfile_id=rs.getString("throwfile_id");
			    	result[count].throwfile_platform=rs.getString("throwfile_platform");
			    	result[count].throwfile_type=rs.getString("throwfile_type");
			    	result[count].icon=rs.getString("icon");
			    	result[count].memo=rs.getString("memo");
			    	result[count].reversed=rs.getString("reversed");
			    	result[count].throwfile_fileextension=rs.getString("throwfile_fileextension");
			    	count++;
			    }
			    Gson gson = new Gson();
				String jsonStrList = gson.toJson(result);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				if (rs != null) {try {rs.close();} catch (SQLException se) {se.printStackTrace(System.err);}}if (statement != null) {try {statement.close();} catch (SQLException se) {se.printStackTrace(System.err);}}if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			response.getWriter().write("fail!!!!!");
			return;
		}else if("select_way_of_platform".equals(action)){
			final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
					+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
			final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
			Connection con = null;
			Statement statement = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				statement = con.createStatement();
				String cmd ="SELECT * FROM tb_throwfile WHERE throwfile_platform = '"+request.getParameter("platform")+"' and throwfile_type != 'general' and group_buying = 'true' ORDER BY throwfile_id ASC ";
				rs = statement.executeQuery(cmd);
				int count=0;
			    if (rs.last()){count = rs.getRow();}else{count = 0;}
			    Throwfile[] result=new Throwfile[count];
			    rs.beforeFirst();count=0;
			    while (rs.next()) {
			    	result[count]= new Throwfile();
			    	result[count].throwfile_name=rs.getString("throwfile_name");
			    	result[count].throwfile_id=rs.getString("throwfile_id");
			    	result[count].throwfile_platform=rs.getString("throwfile_platform");
			    	result[count].throwfile_type=rs.getString("throwfile_type");
			    	result[count].memo=rs.getString("memo");
			    	result[count].reversed=rs.getString("reversed");
			    	result[count].throwfile_fileextension=rs.getString("throwfile_fileextension");
			    	count++;
			    }
			    Gson gson = new Gson();
				String jsonStrList = gson.toJson(result);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				if (rs != null) {try {rs.close();} catch (SQLException se) {se.printStackTrace(System.err);}}if (statement != null) {try {statement.close();} catch (SQLException se) {se.printStackTrace(System.err);}}if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			response.getWriter().write("fail!!!!!");
			return;
		}else{
			
			if(request.getSession().getAttribute("group_id")==null){request.setAttribute("action","no_session");RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");successView.forward(request, response);return;}
			if(request.getParameter("vender")==null){request.setAttribute("action","no_vender");RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");successView.forward(request, response);return;}
			String conString="",ret="E";
			conString=putFile(request, response);
//			System.out.println(conString);
			try{
				String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
				String processName =java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
				String my_msg =(new SimpleDateFormat("yyyy-MM-dd(u) HH:mm:ss").format(new Date()))+":\r\n  I'm "+request.getSession().getAttribute("user_name")+" upload(G) "+ori_file_name+" as -> " +file_name+ " with PID = "+ Long.parseLong(processName.split("@")[0])+".\r\n";
				FileWriter fw;
				try{
					fw = new FileWriter(record_log,true);
				}catch(FileNotFoundException e){
					fw = new FileWriter(record_log,false);
				}
				fw.write(my_msg);
				fw.close();
			}catch(Exception e){System.out.println("Error: "+e.toString());}
			
//			if(conString.length()<1000000)return;
			
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
			response.getWriter().write(ret);
	//		request.setAttribute("action",ret);
	//		RequestDispatcher successView = request.getRequestDispatcher("/upload.jsp");
	//		successView.forward(request, response);
			try{
				String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
				String my_msg ="  Result as \'"+ret+"\'.At ("+(new SimpleDateFormat("yyyy-MM-dd(u) HH:mm:ss").format(new Date()))+")\r\n";
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
	}
	
	protected String putFile(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String conString="",ret="";
		request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    String vender = request.getParameter("vender");
	    String ordertype = request.getParameter("ordertype");
	    String delivertype = request.getParameter("delivertype");
	    String productcode = request.getParameter("productcode");
	    
	    String group_id = request.getSession().getAttribute("group_id").toString();
	    String user_id = request.getSession().getAttribute("user_id").toString();
	    user_id =(user_id==null||user_id.length()<3)?"UNKNOWN":user_id;
	    group_id=(group_id==null)?"UNKNOWN":group_id;
	    vender  =(vender==null)?"UNKNOWN":vender;
	    String _uid= UUID.randomUUID().toString();
		//_uid="454c9c52-cb76-46d3-bf4e-e3ae820c8064";
		String no_way = getServletConfig().getServletContext().getInitParameter("groupbuypath")+"/"+vender+"/"+ordertype+"/"+group_id+"/"+_uid;
		new File(getServletConfig().getServletContext().getInitParameter("groupbuypath")+"/"+vender).mkdir();
		new File(getServletConfig().getServletContext().getInitParameter("groupbuypath")+"/"+vender+"/"+ordertype).mkdir();
		new File(getServletConfig().getServletContext().getInitParameter("groupbuypath")+"/"+vender+"/"+ordertype+"/"+group_id).mkdir();
		new File(getServletConfig().getServletContext().getInitParameter("groupbuypath")+"/fail").mkdir();
		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		if (contentType!=null && (contentType.indexOf("multipart/form-data") >= 0)) {
		      DiskFileItemFactory factory = new DiskFileItemFactory();
		      factory.setSizeThreshold(maxMemSize);
		      String file_over=getServletConfig().getServletContext().getInitParameter("groupbuypath")+"/fail";
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
						//System.out.println(conString);
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
		        			//System.out.println(filecontent_ori);
			            	String[] filecontent = filecontent_ori.split(",");
			            	if(filecontent_ori.contains("text/html;")||filecontent_ori.contains("<html>")){//filecontent_ori.contains("content='text/html;")){
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
		        		conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
								+"/groupbuy/urls="
								+new String(Base64.encodeBase64String((fullname).getBytes()))
								+"&usid="
								+new String(Base64.encodeBase64String(user_id.getBytes()))
								+"&delivertype="
								+new String(Base64.encodeBase64String(delivertype.getBytes()))
								+"&productcode="
								+new String(Base64.encodeBase64String(productcode.getBytes()));
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
			if("success".compareTo(content)==0 || content.contains("\"success\": true") ){
				ret=content;
			}else{
				if(content.length()>100){
					content=content.substring(0,90)+"....";
				}
				ret="Error_Connection: get "+content+" on: "+conString;
			}
		}catch(Exception e){
			ret=e.toString();
			ret="Error of call webservice content:"+ret; 
		}
		method.releaseConnection();
		return ret;
	}
	class Throwfile {
		String throwfile_id;
		String throwfile_platform;
		String throwfile_type;
		String throwfile_name;
		String throwfile_fileextension;
		String icon;
		String memo;
		String reversed;
	}
}
