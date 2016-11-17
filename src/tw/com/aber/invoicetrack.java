
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
import tw.com.aber.chart.saleamountstaticchart.SalechartVO;
import tw.com.aber.productunit.controller.productunit.ProductunitVO;

import java.util.Date; 
import java.text.SimpleDateFormat;
@SuppressWarnings("serial")

public class invoicetrack extends HttpServlet {

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
		//group_id ="cbcc3138-5603-11e6-a532-000d3a800878";
		String action= request.getParameter("action");
		InvoicetrackDAO dao= new InvoicetrackDAO();
		List<InvoicetrackVO> list = new ArrayList<InvoicetrackVO>();
		if("search".equals(action)){
			list=dao.searhDB(group_id);
		}else if("insert".equals(action)){
			String invoice_type = request.getParameter("invoice_type");
			String year_month = request.getParameter("year_month");
			String invoice_track = request.getParameter("invoice_track");
			String invoice_beginno = request.getParameter("invoice_beginno");
			String invoice_endno = request.getParameter("invoice_endno");
			String seq = request.getParameter("seq");
			dao.insert(group_id,invoice_type,year_month,invoice_track,invoice_beginno,invoice_endno,seq);
			list=dao.searhDB(group_id);
		}else if("update".equals(action)){
			String invoice_id = request.getParameter("invoice_id");
			String invoice_type = request.getParameter("invoice_type");
			String year_month = request.getParameter("year_month");
			String invoice_track = request.getParameter("invoice_track");
			String invoice_beginno = request.getParameter("invoice_beginno");
			String invoice_endno = request.getParameter("invoice_endno");
			String seq = request.getParameter("seq");
			dao.update(invoice_id,group_id,invoice_type,year_month,invoice_track,invoice_beginno,invoice_endno,seq);
			list=dao.searhDB(group_id);
		}else if("delete".equals(action)){
			String invoice_id = request.getParameter("invoice_id");
			dao.delete(invoice_id);
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

	public class InvoicetrackVO implements java.io.Serializable {
		public String invoice_id;
		public String group_id;
		public String invoice_type;
		public String year_month;
		public String invoice_track;
		public String invoice_beginno;
		public String invoice_endno;
		public String seq;
	}

	/*************************** 操作資料庫 ****************************************/
	class InvoicetrackDAO  {
		// 會使用到的Stored procedure
		private static final String sp_selectall_invoice_track = "call sp_selectall_invoice_track(?)";
		private static final String sp_insert_invoice_track = "call sp_insert_invoice_track(?,?,?,?,?,?,?)";
		private static final String sp_update_invoice_track = "call sp_update_invoice_track(?,?,?,?,?,?,?,?)";
		private static final String sp_del_invoice_track  = "call sp_del_invoice_track(?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public List<InvoicetrackVO> searhDB(String group_id) {
			InvoicetrackVO invoicetrackVO = null;
			List<InvoicetrackVO > list = new ArrayList<InvoicetrackVO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_invoice_track);
				pstmt.setString(1,group_id);
				rs = pstmt.executeQuery();

			    while (rs.next()) {
			    	invoicetrackVO =new InvoicetrackVO();
			    	invoicetrackVO.invoice_id  =rs.getString("invoice_id");
			    	invoicetrackVO.group_id    =rs.getString("group_id");
			    	invoicetrackVO.invoice_type=rs.getString("invoice_type");
					invoicetrackVO.year_month  =rs.getString("year_month");
					invoicetrackVO.invoice_track=rs.getString("invoice_track");
					invoicetrackVO.invoice_beginno=rs.getString("invoice_beginno");
					invoicetrackVO.invoice_endno=rs.getString("invoice_endno");
					invoicetrackVO.seq          =rs.getString("seq");
					list.add(invoicetrackVO);
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}
		
		public void insert(String group_id,String invoice_type, String year_month, String invoice_track, String invoice_beginno, String invoice_endno, String seq) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_invoice_track);
				pstmt.setString(1,group_id);
				pstmt.setString(2,invoice_type);
				pstmt.setString(3,year_month);
				pstmt.setString(4,invoice_track);
				pstmt.setString(5,invoice_beginno);
				pstmt.setString(6,invoice_endno);
				pstmt.setString(7,seq);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		
		public void update(String invoice_id,String group_id,String invoice_type, String year_month, String invoice_track, String invoice_beginno, String invoice_endno, String seq) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_invoice_track);
				pstmt.setString(1,invoice_id);
				pstmt.setString(2,group_id);
				pstmt.setString(3,invoice_type);
				pstmt.setString(4,year_month);
				pstmt.setString(5,invoice_track);
				pstmt.setString(6,invoice_beginno);
				pstmt.setString(7,invoice_endno);
				pstmt.setString(8,seq);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		
		public void delete(String track_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_invoice_track);
				pstmt.setString(1,track_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
	}
}
