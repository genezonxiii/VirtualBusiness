
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
		String action = request.getParameter("action");
		if("select".equals(action)){
			response.getWriter().write("Action = select 喔 ~");
		}
		if("insert".equals(action)){
			System.out.println("Action = insert 喔 ~");
			int[] array={0,1,2,3,4,5,6,7,8,9,10};
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(array);
			response.getWriter().write(jsonStrList);
		}
		if("update".equals(action)){
			response.getWriter().write("Action = update 喔 ~");
		}
		if("delete".equals(action)){
			response.getWriter().write("Action = delete 喔 ~");
		}
		
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class SalereportVO implements java.io.Serializable {
		private String sale_id;
		private String seq_no;
		private String group_id;
		private String order_no;
		private String user_id;
		private String product_id;
		private String product_name;
		private String c_product_id;
		private String customer_id;
		private String name;
		private int quantity;
		private float price;
		private String invoice;
		private java.sql.Date invoice_date;
		private java.sql.Date trans_list_date;
		private java.sql.Date dis_date;
		private String memo;
		private java.sql.Date sale_date;
		private String order_source;
		public String getSale_id() {
			return sale_id;
		}
		public void setSale_id(String sale_id) {
			this.sale_id = sale_id;
		}
		public String getSeq_no() {
			return seq_no;
		}
		public void setSeq_no(String seq_no) {
			this.seq_no = seq_no;
		}
		public String getGroup_id() {
			return group_id;
		}
		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}
		public String getOrder_no() {
			return order_no;
		}
		public void setOrder_no(String order_no) {
			this.order_no = order_no;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getProduct_id() {
			return product_id;
		}
		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}
		public String getProduct_name() {
			return product_name;
		}
		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
		public String getC_product_id() {
			return c_product_id;
		}
		public void setC_product_id(String c_product_id) {
			this.c_product_id = c_product_id;
		}
		public String getCustomer_id() {
			return customer_id;
		}
		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		public String getInvoice() {
			return invoice;
		}
		public void setInvoice(String invoice) {
			this.invoice = invoice;
		}
		public java.sql.Date getInvoice_date() {
			return invoice_date;
		}
		public void setInvoice_date(java.sql.Date invoice_date) {
			this.invoice_date = invoice_date;
		}
		public java.sql.Date getTrans_list_date() {
			return trans_list_date;
		}
		public void setTrans_list_date(java.sql.Date trans_list_date) {
			this.trans_list_date = trans_list_date;
		}
		public java.sql.Date getDis_date() {
			return dis_date;
		}
		public void setDis_date(java.sql.Date dis_date) {
			this.dis_date = dis_date;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		public java.sql.Date getSale_date() {
			return sale_date;
		}
		public void setSale_date(java.sql.Date sale_date) {
			this.sale_date = sale_date;
		}
		public String getOrder_source() {
			return order_source;
		}
		public void setOrder_source(String order_source) {
			this.order_source = order_source;
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface Salereport_interface {
		//public String select_date(String group_id,Date time1,Date time2);
		
		//public void insertDB(SalereportVO productunitVO);

		//public void updateDB(SalereportVO productunitVO);

		//public void deleteDB(String unit_id,String user_id);

		public List<SalereportVO> searhDB(String group_id,java.sql.Date from_date,java.sql.Date till_date);

		//public List<SalereportVO> searhAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class SalereportService {
		private Salereport_interface dao;

		public SalereportService() {
			dao = new SalereportDAO();
		}

		public List<SalereportVO> getSearhDB(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			return dao.searhDB(group_id, from_date,till_date);
		}

		//public List<SalereportVO> getSearAllDB(String group_id) {
		//	return dao.searhAllDB(group_id);
		//}
	}

	/*************************** 操作資料庫 ****************************************/
	class SalereportDAO implements Salereport_interface {
		// 會使用到的Stored procedure
		private static final String sp_select_sale_bydisdate  = "call sp_select_sale_bydisdate(?,?,?)";
		/*private static final String sp_insert_product_unit = "call sp_insert_product_unit(?,?,?)";
		private static final String sp_selectall_product_unit = "call sp_selectall_product_unit (?)";
		private static final String sp_select_product_unit = "call sp_select_product_unit (?,?)";
		private static final String sp_del_product_unit = "call sp_del_product_unit (?,?)";
		private static final String sp_update_product_unit = "call sp_update_product_unit (?,?,?,?)";
		*/
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public List<SalereportVO> searhDB(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			List<SalereportVO> list =new ArrayList<SalereportVO>();
			SalereportVO salereportVO = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_bydisdate);
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				rs = pstmt.executeQuery();
				int k=0;
				while (rs.next()) {
					k++;
					salereportVO = new SalereportVO();
					salereportVO.setSale_id(rs.getString("sale_id"));
					salereportVO.setSeq_no(rs.getString("seq_no"));
					salereportVO.setGroup_id(rs.getString("group_id"));
					salereportVO.setOrder_no(rs.getString("order_no"));
					salereportVO.setUser_id(rs.getString("user_id"));
					salereportVO.setProduct_id(rs.getString("product_id"));
					salereportVO.setProduct_name(rs.getString("product_name"));
					salereportVO.setC_product_id(rs.getString("c_product_id"));
					salereportVO.setCustomer_id(rs.getString("customer_id"));
					salereportVO.setName(rs.getString("name"));
					salereportVO.setQuantity(rs.getInt("quantity"));
					salereportVO.setPrice(rs.getFloat("price"));
					salereportVO.setInvoice(rs.getString("invoice"));
					salereportVO.setInvoice_date(rs.getDate("invoice_date"));
					salereportVO.setTrans_list_date(rs.getDate("trans_list_date"));
					salereportVO.setDis_date(rs.getDate("dis_date"));
					salereportVO.setMemo(rs.getString("memo"));
					salereportVO.setSale_date(rs.getDate("sale_date"));
					salereportVO.setOrder_source(rs.getString("order_source"));
					list.add(salereportVO);
				}
				//list 排序 by dis_date
				//System.out.println("total Data: "+k);
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
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
		public void insertDB(SalereportVO productunitVO) {
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
		public void updateDB(SalereportVO productunitVO) {
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
		public List<SalereportVO> searhDB(String group_id, String unit_name) {
			List<SalereportVO> list = new ArrayList<SalereportVO>();
			SalereportVO productunitVO = null;

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
					productunitVO = new SalereportVO();
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
		public List<SalereportVO> searhAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<SalereportVO> list = new ArrayList<SalereportVO>();
			SalereportVO productunitVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_product_unit);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productunitVO = new SalereportVO();
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
