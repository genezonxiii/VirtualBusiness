
package tw.com.aber.chart;

import java.io.BufferedReader;
import java.io.IOException;
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

import tw.com.aber.report.salereport.SalereportVO;

import java.util.Date; 
import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.UUID;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*; 
import org.apache.commons.codec.binary.Base64;
@SuppressWarnings("serial")

public class heavybuyer extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id=(group_id==null||group_id.length()<3)?"KNOWN":group_id;
		
		String time1 = request.getParameter("time1");
		time1=(time1==null || time1.length()<3)?"1999-12-31":time1;
		String time2 = request.getParameter("time2");
		time2=(time2==null || time2.length()<3)?"2300-12-31":time2;
		//System.out.println("from "+time1+" to "+time2);
		//###########################################
		String action = request.getParameter("action");
		if("search_best_sale".equals(action)){
			try {
//				List<BestsaleVO> list;
				BestsaleDAO bestsaledao = null;
				String order_source=request.getParameter("ordersource");
				bestsaledao = new BestsaleDAO();
				String ret;
				if(order_source.length()<1){
					ret = bestsaledao.heavybuyer(group_id, time1, time2);
				}else{
					ret = bestsaledao.heavybuyerbyordersource(group_id, order_source, time1, time2);
				}
				//System.out.println(ret);
//				Gson gson = new Gson();
//				String jsonStrList = gson.toJson(list);
				//System.out.println(ret);
				response.getWriter().write(ret);
				return;
			} catch (Exception e) {System.out.println("Error with time parse. :"+e);}
		}
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class BestsaleVO implements java.io.Serializable {
		int quantity;
		String product_name;
	}
	/*************************** 操作資料庫 ****************************************/
	class BestsaleDAO{
		// 會使用到的Stored procedure
//		private static final String sp_saleproduct_top10 = "call sp_saleproduct_top10(?,?,?)";
//		private static final String sp_saleproduct_channel  = "call sp_saleproduct_channel (?,?,?,?)";

//		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
//				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
//		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
//		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
//		
		public String heavybuyer(String group_id, String time1, String time2) {
			String conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
					+ "/analytics/type=VE9QMTA=&guid="
					//+ "Y2JjYzMxMzgtNTYwMy0xMWU2LWE1MzItMDAwZDNhODAwODc4"
					+ new String(Base64.encodeBase64String(group_id.getBytes()))
					+ "&star="
					+ new String(Base64.encodeBase64String(time1.getBytes()))
					+ "&endd="
					+ new String(Base64.encodeBase64String(time2.getBytes()));
			//System.out.println(conString);
			HttpClient client = new HttpClient();
			HttpMethod method;
			String ret="";
			try{
				method=new GetMethod(conString);
				client.executeMethod(method);
				ret=method.getResponseBodyAsString().replaceAll("null", "\"\"");
				//System.out.println(ret.charAt(0));
				if('<'==ret.charAt(0)){
					ret="WebService Error";
				}
			}catch(Exception e){
				return "WebService Error for:"+e.toString();
			}
			method.releaseConnection();
			return ret;
			
		}
		public String heavybuyerbyordersource(String group_id, String order_source, String time1, String time2) {
			String conString=getServletConfig().getServletContext().getInitParameter("pythonwebservice")
					+ "/analytics/type=b3RoZXI=&guid="
					+ new String(Base64.encodeBase64String(group_id.getBytes()))
					+ "&star="
					+ new String(Base64.encodeBase64String(time1.getBytes()))
					+ "&endd="
					+ new String(Base64.encodeBase64String(time2.getBytes()))
					+ "&from="
					+ new String(Base64.encodeBase64String(order_source.getBytes()));
			//System.out.println(conString);
			HttpClient client = new HttpClient();
			HttpMethod method;
			String ret="";
			try{
				method=new GetMethod(conString);
				client.executeMethod(method);
				ret=method.getResponseBodyAsString().replaceAll("null", "\"\"");
			}catch(Exception e){
				return "WebService Error for:"+e.toString();
			}
			method.releaseConnection();
			return ret;
		}
	}
}
