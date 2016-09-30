
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
import java.util.Date; 
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")

public class membercondition extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String group_id =request.getSession().getAttribute("group_id").toString();
		String action = request.getParameter("action");
		if("select".equals(action)){
			MemberconditionDAO memberDAO = new MemberconditionDAO();
			List<MemberconditionVO> answer = memberDAO.searhDB(group_id);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(answer);
			response.getWriter().write(jsonStrList);
		}
		if("insert".equals(action)){
			String classname = request.getParameter("classname");
			String total_period = request.getParameter("total_period");
			String total_consumption = request.getParameter("total_consumption");
			String expire_day = request.getParameter("expire_day");
			String continue_period = request.getParameter("continue_period");
			String continue_consumption = request.getParameter("continue_consumption");
			MemberconditionDAO memberDAO = new MemberconditionDAO();
			memberDAO.insertDB(group_id, classname, total_period, total_consumption, expire_day , continue_period, continue_consumption);
			List<MemberconditionVO> answer = memberDAO.searhDB(group_id);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(answer);
			response.getWriter().write(jsonStrList);
		}
		if("update".equals(action)){
			String condition_id = request.getParameter("condition_id");
			String classname = request.getParameter("classname");
			String total_period = request.getParameter("total_period");
			String total_consumption = request.getParameter("total_consumption");
			String expire_day = request.getParameter("expire_day");
			String continue_period = request.getParameter("continue_period");
			String continue_consumption = request.getParameter("continue_consumption");
			
			MemberconditionDAO memberDAO = new MemberconditionDAO();
			memberDAO.updateDB(group_id, condition_id, classname, total_period, total_consumption, expire_day , continue_period, continue_consumption);
			List<MemberconditionVO> answer = memberDAO.searhDB(group_id);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(answer);
			response.getWriter().write(jsonStrList);
		}
		if("delete".equals(action)){
			String condition_id = request.getParameter("condition_id");
			MemberconditionDAO memberDAO = new MemberconditionDAO();
			memberDAO.deleteDB(group_id, condition_id);
			List<MemberconditionVO> answer = memberDAO.searhDB(group_id);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(answer);
			response.getWriter().write(jsonStrList);
		}
		
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class MemberconditionVO implements java.io.Serializable {
		private String condition_id;
		private String group_id;
		private String classname;
		private String total_period;
		private String total_consumption;
		private String expire_day;
		private String continue_period;
		private String continue_consumption;
		public String getCondition_id() {
			return condition_id;
		}
		public void setCondition_id(String condition_id) {
			this.condition_id = condition_id;
		}
		public String getGroup_id() {
			return group_id;
		}
		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}
		public String getClassname() {
			return classname;
		}
		public void setClassname(String classname) {
			this.classname = classname;
		}
		public String getTotal_period() {
			return total_period;
		}
		public void setTotal_period(String total_period) {
			this.total_period = total_period;
		}
		public String getTotal_consumption() {
			return total_consumption;
		}
		public void setTotal_consumption(String total_consumption) {
			this.total_consumption = total_consumption;
		}
		public String getExpire_day() {
			return expire_day;
		}
		public void setExpire_day(String expire_day) {
			this.expire_day = expire_day;
		}
		public String getContinue_period() {
			return continue_period;
		}
		public void setContinue_period(String continue_period) {
			this.continue_period = continue_period;
		}
		public String getContinue_consumption() {
			return continue_consumption;
		}
		public void setContinue_consumption(String continue_consumption) {
			this.continue_consumption = continue_consumption;
		}
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface Membercondition_interface {
		//public String select_date(String group_id,Date time1,Date time2);
		
		public void insertDB(String group_id, String classname, String total_period, String total_consumption,String expire_day, String continue_period,String continue_consumption);

		public void updateDB(String group_id, String condition_id, String classname, String total_period, String total_consumption,String expire_day, String continue_period,String continue_consumption);

		public void deleteDB(String group_id,String condition_id);

		public List<MemberconditionVO> searhDB(String group_id);
	}

	/*************************** 操作資料庫 ****************************************/
	class MemberconditionDAO implements Membercondition_interface {
		// 會使用到的Stored procedure
		private static final String sp_selectall_member_condition  = "call sp_selectall_member_condition(?)";
		private static final String sp_insert_member_condition   = "call sp_insert_member_condition (?,?,?,?,?,?,?)";
		private static final String sp_update_member_condition  = "call sp_update_member_condition (?,?,?,?,?,?,?,?)";
		private static final String sp_del_member_condition  = "call sp_del_member_condition (?)";
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public List<MemberconditionVO> searhDB(String group_id) {
			List<MemberconditionVO> list =new ArrayList<MemberconditionVO>();
			MemberconditionVO MemberconditionVO = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_member_condition);
				pstmt.setString(1,group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					MemberconditionVO = new MemberconditionVO();
					MemberconditionVO.setCondition_id(rs.getString("condition_id"));
					MemberconditionVO.setGroup_id(rs.getString("group_id"));
					MemberconditionVO.setClassname(rs.getString("class"));
					MemberconditionVO.setTotal_period(rs.getString("total_period"));
					MemberconditionVO.setTotal_consumption(rs.getString("total_consumption"));
					MemberconditionVO.setExpire_day(rs.getString("expire_day"));
					MemberconditionVO.setContinue_period(rs.getString("continue_period"));
					MemberconditionVO.setContinue_consumption(rs.getString("continue_consumption"));
					list.add(MemberconditionVO);
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}

		@Override
		public void insertDB(String group_id, String classname, String total_period, String total_consumption,
				String expire_day, String continue_period, String continue_consumption) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_member_condition);
				pstmt.setString(1,group_id);
				pstmt.setString(2,classname);
				pstmt.setString(3,total_period);
				pstmt.setString(4,total_consumption);
				pstmt.setString(5,expire_day);
				pstmt.setString(6,continue_period);
				pstmt.setString(7,continue_consumption);
				rs = pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
		}

		@Override
		public void updateDB(String group_id, String condition_id, String classname, String total_period,
				String total_consumption,String expire_day, String continue_period, String continue_consumption) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_member_condition);
				pstmt.setString(1,condition_id);
				pstmt.setString(2,group_id);
				pstmt.setString(3,classname);
				pstmt.setString(4,total_period);
				pstmt.setString(5,total_consumption);
				pstmt.setString(6,expire_day);
				pstmt.setString(7,continue_period);
				pstmt.setString(8,continue_consumption);
				rs = pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
		}

		@Override
		public void deleteDB(String group_id, String condition_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_member_condition);
				//pstmt.setString(1,group_id);
				pstmt.setString(1,condition_id);
				rs = pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
		}
		
		/*	public String select_date(String group_id,Date time1,Date time2) {
			//System.out.println("2222");
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_bytranslistdate);
				
				//System.out.println(new java.sql.Date(time1.getTime()));
				//System.out.println(new java.sql.Date(time2.getTime()));
				pstmt.setString(1, group_id);
				pstmt.setDate(2, new java.sql.Date(time1.getTime()));
				pstmt.setDate(3, new java.sql.Date(time2.getTime()));
				rs = pstmt.executeQuery();
				int k=0;
				while (rs.next()) {
					k++;
					System.out.println("k=="+k);
;sale_id"));
System.out.println(rs.getString("seq_no"));
System.out.println(rs.getString("group_id"));
System.out.println(rs.getString("order_no"));
System.out.println(rs.getString("user_id"));
System.out.println(rs.getString("product_id"));
System.out.println(rs.getString("product_name"));
System.out.println(rs.getString("c_product_id"));
System.out.println(rs.getString("customer_id"));
System.out.println(rs.getString("name"));
System.out.println(rs.getString("quantity"));
System.out.println(rs.getString("price"));
System.out.println(rs.getString("invoice"));
System.out.println(rs.getString("invoice_date"));
System.out.println(rs.getString("trans_list_date"));
System.out.println(rs.getString("dis_date"));
System.out.println(rs.getString("memo"));
System.out.println(rs.getString("sale_date"));
System.out.println(rs.getString("order_source"));
System.out.println("##########"+k+"###########");
				}
				System.out.println("k=="+k);
				// Handle any driver errors
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);}
			return "";
		}*/
		/*
		@Override
		public void insertDB(MemberconditionVO productunitVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_unit);

				pstmt.setString(1, productunitVO.getGroup_id());
				pstmt.setString(2, productunitVO.getUnit_name());
				pstmt.setString(3, productunitVO.getUser_id());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}

		@Override
		public void updateDB(MemberconditionVO productunitVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_product_unit);

				pstmt.setString(1, productunitVO.getUnit_id());
				pstmt.setString(2, productunitVO.getGroup_id());
				pstmt.setString(3, productunitVO.getUnit_name());
				pstmt.setString(4, productunitVO.getUser_id());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}

		@Override
		public void deleteDB(String unit_id,String user_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_product_unit);
				pstmt.setString(1, unit_id);
				pstmt.setString(2, user_id);

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}

		@Override
		public List<MemberconditionVO> searhDB(String group_id, String unit_name) {
			List<MemberconditionVO> list = new ArrayList<MemberconditionVO>();
			MemberconditionVO productunitVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_product_unit);

				pstmt.setString(1, group_id);
				pstmt.setString(2, unit_name);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					productunitVO = new MemberconditionVO();
					productunitVO.setGroup_id(rs.getString("group_id"));
					productunitVO.setUnit_id(rs.getString("unit_id"));
					productunitVO.setUnit_name(rs.getString("unit_name"));
					list.add(productunitVO);
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}

		@Override
		public List<MemberconditionVO> searhAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<MemberconditionVO> list = new ArrayList<MemberconditionVO>();
			MemberconditionVO productunitVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_product_unit);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productunitVO = new MemberconditionVO();
					productunitVO.setGroup_id(rs.getString("group_id"));
					productunitVO.setUnit_id(rs.getString("unit_id"));
					productunitVO.setUnit_name(rs.getString("unit_name"));
					list.add(productunitVO); // Store the row in the list
				}

				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}*/

	}
}
