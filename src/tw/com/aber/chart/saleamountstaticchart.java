
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
import java.util.Date; 
import java.text.SimpleDateFormat;
@SuppressWarnings("serial")

public class saleamountstaticchart extends HttpServlet {

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
		String user_id = request.getSession().getAttribute("user_id").toString();
		user_id=(user_id==null||user_id.length()<3)?"KNOWN":user_id;
		
		String time1 = request.getParameter("time1");
		
		time1=(time1==null || time1.length()<3)?"1999-12-31":time1;
		String time2 = request.getParameter("time2");
		time2=(time2==null || time2.length()<3)?"2300-12-31":time2;
		//System.out.println("from "+time1+" to "+time2);
		
		//###########################################
		try {
			SalechartService salechartService = null;
			java.sql.Date from_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time1).getTime());
			java.sql.Date till_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time2).getTime());
			salechartService = new SalechartService();
			SalechartVO chart_data = salechartService.getSearhDB(group_id, from_date, till_date);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(chart_data);
			//System.out.println("json: "+ jsonStrList);
			response.getWriter().write(jsonStrList);
			return;
		} catch (Exception e) {System.out.println("Error with time parse. :"+e);}
		
		}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class SalechartVO implements java.io.Serializable {
		private String[] entrance;
		private String[] answer;
		private String[] vender;

		public String[] getVender() {
			return vender;
		}
		public void setVender(String[] vender) {
			this.vender = vender;
		}
		public SalechartVO(){
		}
		public String[] getEntrance() {
			return entrance;
		}

		public String[] getAnswer() {
			return answer;
		}
		public void setEntrance(String[] entrance) {
			this.entrance = entrance;
		}
		public void setAnswer(String[] answer) {
			this.answer = answer;
		}

	}

	/*************************** 制定規章方法 ****************************************/
	interface Salereport_interface {
		public SalechartVO searhDB(String group_id,java.sql.Date from_date,java.sql.Date till_date);
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
	}

	/*************************** 操作資料庫 ****************************************/
	class SalereportDAO implements Salereport_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_sale_price_percent  = "call sp_get_sale_price_percent(?,?,?)";

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
				pstmt = con.prepareStatement(sp_get_sale_price_percent);
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
			    String[] entrance=new String[count];
			    String[] answer=new String[count];
			    String[] vender=new String[count];
			    rs.beforeFirst();
			    int k=0;
				while (rs.next()) {
					entrance[k]=rs.getString("month");
					answer[k]=rs.getString("price");
					vender[k]=rs.getString("order_source");
					//System.out.println(entrance[k]);
					//System.out.println(k+"th month: "+rs.getInt("month"));
					//System.out.println(k+"th price: "+rs.getInt("price"));
					k++;
				}
				salechartVO.setEntrance(entrance);
				salechartVO.setAnswer(answer);
				salechartVO.setVender(vender);
				//System.out.println("total Data: "+k);
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return salechartVO;
		}


	}
}