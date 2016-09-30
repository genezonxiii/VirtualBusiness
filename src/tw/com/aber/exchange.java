
package tw.com.aber;

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

import tw.com.aber.accpay.controller.accpay.AccpayVO;
import tw.com.aber.productunit.controller.productunit.ProductunitVO;
import tw.com.aber.saleamountstaticchart.SalechartVO;

import java.util.Date; 
import java.text.SimpleDateFormat;
@SuppressWarnings("serial")

public class exchange extends HttpServlet {

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
		String action= request.getParameter("action");
		ExchangeDAO dao= new ExchangeDAO();
		List<ExchangeVO> list = new ArrayList<ExchangeVO>();
		if("search".equals(action)){
			list=dao.searhDB(group_id);
		}else if("insert".equals(action)){
			String currency= request.getParameter("currency");
			String exchange_rate= request.getParameter("exchange_rate");
			dao.insert(group_id, currency, exchange_rate);
			list=dao.searhDB(group_id);
		}else if("update".equals(action)){
			String exchange_id= request.getParameter("exchange_id");
			String currency= request.getParameter("currency");
			String exchange_rate= request.getParameter("exchange_rate");
			dao.update(exchange_id,group_id, currency, exchange_rate);
			list=dao.searhDB(group_id);
		}else if("delete".equals(action)){
			String exchange_id= request.getParameter("exchange_id");
			dao.delete(group_id, exchange_id);
			list=dao.searhDB(group_id);
		}
		Gson gson = new Gson();
		String jsonStrList = gson.toJson(list);
		//System.out.println("json: "+ jsonStrList);
		response.getWriter().write(jsonStrList);
		return;
		//###########################################
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class ExchangeVO implements java.io.Serializable {
		public String exchange_id;
		public String group_id;
		public String currency;
		public String exchange_rate;
	}

	/*************************** 操作資料庫 ****************************************/
	class ExchangeDAO  {
		// 會使用到的Stored procedure
		private static final String sp_selectall_exchange = "call sp_selectall_exchange(?)";
		private static final String sp_insert_exchange = "call sp_insert_exchange(?,?,?)";
		private static final String sp_update_exchange = "call sp_update_exchange(?,?,?,?)";
		private static final String sp_del_exchange  = "call sp_del_exchange (?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public List<ExchangeVO> searhDB(String group_id) {
			ExchangeVO exchangeVO = null;
			List<ExchangeVO > list = new ArrayList<ExchangeVO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_exchange);
				pstmt.setString(1,group_id);
				rs = pstmt.executeQuery();

			    while (rs.next()) {
			    	exchangeVO =new ExchangeVO();
			    	exchangeVO.exchange_id   =rs.getString("exchange_id");
			    	exchangeVO.group_id      =rs.getString("group_id");
			    	exchangeVO.currency      =rs.getString("currency");
					exchangeVO.exchange_rate =rs.getString("exchange_rate");
					list.add(exchangeVO);
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}
		
		public void insert(String group_id,String currency, String exchange_rate) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_exchange);
				pstmt.setString(1,group_id);
				pstmt.setString(2,currency);
				pstmt.setString(3,exchange_rate);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		
		public void update(String exchange_id, String group_id,  String currency,  String exchange_rate) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_exchange);
				pstmt.setString(1,exchange_id);
				pstmt.setString(2,group_id);
				pstmt.setString(3,currency);
				pstmt.setString(4,exchange_rate);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		
		public void delete(String group_id, String exchange_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_exchange);
				pstmt.setString(1,group_id);
				pstmt.setString(2,exchange_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
	}
}
