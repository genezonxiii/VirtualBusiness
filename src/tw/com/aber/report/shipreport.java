package tw.com.aber.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import java.util.Date; 
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("serial")

public class shipreport extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getSession().getAttribute("group_id")==null){
			response.getWriter().write("no_session!");
			return;
		}
//		System.out.println("2016/07/09".replaceAll("/", "-"));
//		int i=0;
//		if(i==0)return;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//SalereportService salereportService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id=(group_id==null||group_id.length()<3)?"UNKNOWN":group_id;

		String time1 = (request.getParameter("time1")==null)?"":request.getParameter("time1").replaceAll("/", "-");
		time1=(time1.length()<3)?"2016-06-01":time1;
		String time2 = (request.getParameter("time2")==null)?"":request.getParameter("time2").replaceAll("/", "-");
		time2=(time2.length()<3)?"2300-12-31":time2;
		//System.out.println("from "+time1+" to "+time2+"of groupid= "+group_id);
		
		String conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
				+ "/ship/groupid="
				+ new String(Base64.encodeBase64String(group_id.getBytes()))
				+ "&strdate="
				+ new String(Base64.encodeBase64String(time1.getBytes()))
				+ "&enddate="
				+ new String(Base64.encodeBase64String(time2.getBytes()));
		//System.out.println("conString: "+conString);
		if("today".equals(action)){
			conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
					+ "/ship/groupid="
					+ new String(Base64.encodeBase64String(group_id.getBytes()))
					+ "&strdate="
					+ new String(Base64.encodeBase64String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes()))
					+ "&enddate="
					+ new String(Base64.encodeBase64String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes()));
		}
		//System.out.println("conString "+conString);
		HttpClient client = new HttpClient();
		HttpMethod method;
		try{
			method=new GetMethod(conString);
			client.executeMethod(method);
		}catch(Exception e){
			response.getWriter().write("WebService Error for:"+e);
			return;
		}
		StringWriter writer = new StringWriter();
		IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
	    String ret=writer.toString().replaceAll("null", "\"\"");
		response.getWriter().write(ret);
		//System.out.println(method.getResponseBodyAsString());
		method.releaseConnection();
		return;
	}
}
