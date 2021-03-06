
package tw.com.aber.chart;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class saleamountchart extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(saleamountchart.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id=(group_id==null||group_id.length()<3)?"KNOWN":group_id;
		String user_id = request.getSession().getAttribute("user_id").toString();
		user_id=(user_id==null||user_id.length()<3)?"KNOWN":user_id;
		
		String time1 = request.getParameter("time1");
		time1=(time1==null || time1.length()<3)?"1999-12-31":time1;
		String time2 = request.getParameter("time2");
		time2=(time2==null || time2.length()<3)?"2300-12-31":time2;
		String action = request.getParameter("action");
		
		logger.debug("group_id: " +group_id);
		logger.debug("user_id: " +user_id);
		logger.debug("time1: "+time1);
		logger.debug("time2: "+time2);
		
		if("search_week".equals(action)){
			try {
				SalechartService salechartService = null;
				java.sql.Date from_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time1).getTime());
				java.sql.Date till_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time2).getTime());
				salechartService = new SalechartService();
				SalechartVO chart_data = salechartService.getSearhDB(group_id, from_date, till_date);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(chart_data);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {System.out.println("Error with time parse. :"+e);}
		}
		if("search_month".equals(action)){
			try {
				SalechartService salechartService = null;
				java.sql.Date from_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time1).getTime());
				java.sql.Date till_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time2).getTime());
				salechartService = new SalechartService();
				SalechartVO chart_data = salechartService.getSearhDB_month(group_id, from_date, till_date);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(chart_data);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {System.out.println("Error with time parse. :"+e);}
		}
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class SalechartVO implements java.io.Serializable {
		private int[] entrance;
		private int[] answer;
		private String[] vender;
		private int[] month;
		public String[] getVender() {
			return vender;
		}
		public void setVender(String[] vender) {
			this.vender = vender;
		}
		public SalechartVO(){
			this.entrance=new int[30];
			this.answer=new int[30];
		}
		public int[] getEntrance() {
			return entrance;
		}

		public int[] getAnswer() {
			return answer;
		}
		public void setEntrance(int[] entrance) {
			this.entrance = entrance;
		}
		public void setAnswer(int[] answer) {
			this.answer = answer;
		}
		public int[] getMonth() {
			return month;
		}
		public void setMonth(int[] month) {
			this.month = month;
		}
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface Salereport_interface {
		public SalechartVO searhDB(String group_id,java.sql.Date from_date,java.sql.Date till_date);
		public SalechartVO searhDB_month(String group_id,java.sql.Date from_date,java.sql.Date till_date);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class SalechartService {
		private Salereport_interface dao;
		public SalechartService() {
			dao = new SalereportDAO();
		}
		public SalechartVO getSearhDB(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			return dao.searhDB(group_id, from_date,till_date);
		}
		public SalechartVO getSearhDB_month(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			return dao.searhDB_month(group_id, from_date,till_date);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class SalereportDAO implements Salereport_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_sale_price_statistics_byweek = "call sp_get_sale_price_statistics_byweek(?,?,?)";
		private static final String sp_get_sale_price_statistics = "call sp_get_sale_price_statistics(?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public SalechartVO searhDB(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			SalechartVO salechartVO = new SalechartVO();
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_price_statistics_byweek);
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				rs = pstmt.executeQuery();
				int count;
			    if (rs.last()){
			       count = rs.getRow();
			    }else{
			       count = 0;
			    }
			    int[] entrance=new int[count];
			    int[] answer=new int[count];
			    String[] vender=new String[count];
			    int[] month=new int[count];
			    rs.beforeFirst();
			    int k=0;
				while (rs.next()) {
					entrance[k]=rs.getInt("week");
					answer[k]=rs.getInt("price");
					vender[k]=rs.getString("order_source");
					month[k]=rs.getInt("month");
					k++;
				}
				salechartVO.setEntrance(entrance);
				salechartVO.setAnswer(answer);
				salechartVO.setVender(vender);
				salechartVO.setMonth(month);
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return salechartVO;
		}
		public SalechartVO searhDB_month(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			SalechartVO salechartVO = new SalechartVO();
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_price_statistics);
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				rs = pstmt.executeQuery();
				int count;
			    if (rs.last()){
			       count = rs.getRow();
			    }else{
			       count = 0;
			    }
			    int[] entrance=new int[count];
			    int[] answer=new int[count];
			    String[] vender=new String[count];
			    int[] month=new int[count];
			    rs.beforeFirst();
			    int k=0;
				while (rs.next()) {
					entrance[k]=rs.getInt("month");
					answer[k]=rs.getInt("price");
					vender[k]=rs.getString("order_source");
					month[k]=rs.getInt("month");
					k++;
				}
				salechartVO.setEntrance(entrance);
				salechartVO.setAnswer(answer);
				salechartVO.setVender(vender);
				salechartVO.setMonth(month);
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return salechartVO;
		}

	}
}
