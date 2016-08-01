
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

import tw.com.aber.productunit.controller.productunit.ProductunitVO;
import tw.com.aber.saleamountstaticchart.SalechartVO;

import java.util.Date; 
import java.text.SimpleDateFormat;
@SuppressWarnings("serial")

public class salechart extends HttpServlet {

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
		time1=(time1==null || time1.length()<3)?"1999/12/31":time1;
		String time2 = request.getParameter("time2");
		time2=(time2==null || time2.length()<3)?"2300/12/31":time2;
		//System.out.println("from "+time1+" to "+time2);
		
		//###########################################
		try {
			SalechartService salechartService = null;
			java.sql.Date from_date= new java.sql.Date(new SimpleDateFormat("yyyy/MM/dd").parse(time1).getTime());
			java.sql.Date till_date= new java.sql.Date(new SimpleDateFormat("yyyy/MM/dd").parse(time2).getTime());
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
		private int[] month;
		private String[] entrance;// = {"asap","gohappy","ibon","17life","linemart","momo","myfone","payeasy","pchome","udn","yahoo","91mai","treemall","gomaji","books","umall","yahoomall","amart","rakuten","etmall"};
		private int[] answer;
		public SalechartVO(){}
		public String[] getEntrance() {
			return entrance;
		}
		public void setEntrance(String[] entrance) {
			this.entrance = entrance;
		}
		public int[] getAnswer() {
			return answer;
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
		private static final String sp_get_sale_quantity_statistics = "call sp_get_sale_quantity_statistics(?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";;
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public SalechartVO searhDB(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			//SalechartVO salechartVO = null;
			//List<SalechartVO > list = new ArrayList<SalechartVO>();
			SalechartVO salechartVO = new SalechartVO();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_quantity_statistics);
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
			    int k=0;
			    String[] vender=new String[count];;
			    int[] answer=new int[count];
			    int[] month=new int[count];
			    rs.beforeFirst();
			    while (rs.next()) {
					month[k]=rs.getInt("month");
					answer[k]=rs.getInt("price");
					vender[k]=rs.getString("order_source");
					//System.out.println(entrance[k]);
					//System.out.println(k+"th month: "+rs.getInt("month"));
					//System.out.println(k+"th price: "+rs.getInt("price"));
					k++;
				}
				salechartVO.setEntrance(vender);
				salechartVO.setAnswer(answer);
				salechartVO.setMonth(month);
//				int i=0,j=0,tmp_month=0;
//				while (rs.next()) {
//					if(tmp_month!=rs.getInt("month")){
//						if(salechartVO!=null){
//							String[] tmp_entrance=new String[j];;
//						    int[] tmp_answer=new int[j];
//							for(i=0;i<j;i++){
//								tmp_entrance[i]=entrance[i];
//								tmp_answer[i]=answer[i];
//							}
//							salechartVO.setEntrance(tmp_entrance);
//							salechartVO.setAnswer(tmp_answer);
//							list.add(salechartVO);
//						}
//						salechartVO=new SalechartVO();
//						tmp_month=rs.getInt("month");
//						salechartVO.setMonth(tmp_month);
//						entrance=new String[25];
//						answer=new int[25];
//						j=0;
//					};
//					entrance[j]=rs.getString("order_source");
//					answer[j]=rs.getInt("price");
//					j++;
//					//System.out.println(rs.getInt("month")+rs.getString("order_source")+rs.getInt("price"));
//				}
//				if(salechartVO!=null){
//					String[] tmp_entrance=new String[j];;
//				    int[] tmp_answer=new int[j];
//					for(i=0;i<j;i++){
//						tmp_entrance[i]=entrance[i];
//						tmp_answer[i]=answer[i];
//					}
//					salechartVO.setEntrance(tmp_entrance);
//					salechartVO.setAnswer(tmp_answer);
//					list.add(salechartVO);
//				}
				//salechartVO.setEntrance(entrance);
				//salechartVO.setAnswer(answer);
				//System.out.println("total Data: "+k);
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return salechartVO;
		}
	}
}
