package tw.com.aber.basicinfo;

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

//import tw.com.aber.supply.controller.supply.SupplyBeanService;
//import tw.com.aber.supply.controller.supply.SupplyDAO.Barcode_supply;

public class stock extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		StockService stockService = null;
		String action = request.getParameter("action");
		
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("bar_code_search".equals(action)) {
			String barcode=request.getParameter("barcode");
			stockService = new StockService();
//			stockService.barcode_search(group_id,barcode);
			response.getWriter().write(stockService.barcode_search(group_id,barcode));
			return ;
		}
		if ("searh".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String product_name = request.getParameter("product_name");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (product_name == null || (product_name.trim()).length() == 0) {
					stockService = new StockService();
					List<StockBean> list = stockService.searhAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if(product_name != null){
					stockService = new StockService();
					List<StockBean> list = stockService.searhstock_byname(group_id,product_name);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				int  quantity = Integer.valueOf(request.getParameter("quantity"));
				String memo = request.getParameter("memo");
				String stock_id = request.getParameter("stock_id");
				String product_id = request.getParameter("product_id");
				//System.out.println(stock_id);
				/*************************** 2.開始修改資料 ***************************************/
				stockService = new StockService();
				StockBean  bean =stockService.updateStock(stock_id, group_id, user_id, product_id, quantity, memo);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				stockService = new StockService();
				List<StockBean> list = stockService.searhAllDB(group_id);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/************************* 對應資料庫表格格式 **************************************/
	public class StockBean implements java.io.Serializable {
		private String stock_id;
		private String group_id;
		private String user_id;
		private String product_id;
		private int quantity;
		private String memo;
		private String product_name;
		private String keep_stock;
		
		public String getStock_id() {
			return stock_id;
		}
		public void setStock_id(String stock_id) {
			this.stock_id = stock_id;
		}
		public String getGroup_id() {
			return group_id;
		}
		public void setGroup_id(String group_id) {
			this.group_id = group_id;
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
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		public String getProduct_name() {
			return product_name;
		}
		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
		public String getKeep_stock() {
			return keep_stock;
		}
		public void setKeep_stock(String keep_stock) {
			this.keep_stock = keep_stock;
		}
		
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface  Stock_interface {

		public void updateDB(StockBean stockBean);
		public List<StockBean> searhAllDB(String group_id);
		public List<StockBean> searhstock_byname(String group_id, String product_name);
		public String barcode_search(String group_id,String bar_code);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class StockService {
		private Stock_interface dao;

		public StockService() {
			dao = new StockDAO();
		}

		public StockBean updateStock(String stock_id, String group_id, String user_id,String product_id ,int quantity,String memo) {
			StockBean stockBean = new StockBean();
			stockBean.setStock_id(stock_id);
			stockBean.setGroup_id(group_id);
			stockBean.setUser_id(user_id);
			stockBean.setProduct_id(product_id);
			stockBean.setQuantity (quantity);
			stockBean.setMemo(memo);
			dao.updateDB(stockBean);
			return stockBean;
		}

		public List<StockBean> searhAllDB(String group_id) {
			return dao.searhAllDB(group_id);
		}
		
		public List<StockBean> searhstock_byname(String group_id, String product_name) {
			return dao.searhstock_byname(group_id,product_name);
		}
		
		public String barcode_search(String group_id,String bar_code) {
			return dao.barcode_search(group_id, bar_code);
		}
		
	}

	/*************************** 操作資料庫 ****************************************/
	class StockDAO implements Stock_interface {
		// 會使用到的Stored procedure	
		private static final String sp_selectall_stock = "call sp_selectall_stock (?)";
		private static final String sp_update_stock = "call sp_update_stock (?,?,?,?,?,?)";
		private static final String sp_select_stock_bybarcode = "call sp_select_stock_bybarcode (?,?)";
		private static final String sp_select_stock_byname = "call sp_select_stock_byname (?,?)";
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		
		@Override
		public void updateDB(StockBean stockBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_stock);
				pstmt.setString(1, stockBean.getStock_id());
				pstmt.setString(2, stockBean.getGroup_id());
				pstmt.setString(3, stockBean.getUser_id());
				pstmt.setString(4, stockBean.getProduct_id());
				pstmt.setInt(5, stockBean.getQuantity());
				pstmt.setString(6, stockBean.getMemo());
				pstmt.executeUpdate();
//				System.out.println( stockBean.getStock_id()+stockBean.getQuantity()+stockBean.getMemo());
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public List<StockBean> searhAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<StockBean> list = new ArrayList<StockBean>();
			StockBean stockBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_stock);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stockBean  = new StockBean();
					stockBean.setStock_id(rs.getString("stock_id"));
					stockBean.setGroup_id(rs.getString("group_id"));
					stockBean.setUser_id(rs.getString("user_id"));
					stockBean.setProduct_id(rs.getString("product_id"));
					stockBean.setQuantity(rs.getInt("quantity"));
					stockBean.setMemo(rs.getString("memo"));
					stockBean.setProduct_name(rs.getString("product_name"));
					stockBean.setKeep_stock(rs.getString("keep_stock"));
					
					list.add(stockBean); // Store the row in the list
				}
				
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		
		public List<StockBean> searhstock_byname(String group_id, String product_name) {
			// TODO Auto-generated method stub
			List<StockBean> list = new ArrayList<StockBean>();
			StockBean stockBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_stock_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stockBean  = new StockBean();
					stockBean.setStock_id(rs.getString("stock_id"));
					stockBean.setGroup_id(rs.getString("group_id"));
					stockBean.setUser_id(rs.getString("user_id"));
					stockBean.setProduct_id(rs.getString("product_id"));
					stockBean.setQuantity(rs.getInt("quantity"));
					stockBean.setMemo(rs.getString("memo"));
					stockBean.setProduct_name(rs.getString("product_name"));
					stockBean.setKeep_stock(rs.getString("keep_stock"));
					list.add(stockBean); // Store the row in the list
				}
				
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		
		public class Barcode_supply{
			public String stock_id;
			public String group_id;
			public String user_id;
			public String product_id;
			public String quantity;
			public String memo;
			public String product_name;
		}
		
		public String barcode_search(String group_id,String bar_code) {
			//List<Barcode_supply> list = new ArrayList<Barcode_supply>();
			//Barcode_supply barcode_supply = null;
			List<StockBean> list = new ArrayList<StockBean>();
			StockBean stockBean = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_stock_bybarcode);
				pstmt.setString(1, group_id);
				pstmt.setString(2, bar_code);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stockBean  = new StockBean();
					stockBean.setStock_id(rs.getString("stock_id"));
					stockBean.setGroup_id(rs.getString("group_id"));
					stockBean.setUser_id(rs.getString("user_id"));
					stockBean.setProduct_id(rs.getString("product_id"));
					stockBean.setQuantity(rs.getInt("quantity"));
					stockBean.setMemo(rs.getString("memo"));
					stockBean.setProduct_name(rs.getString("product_name"));
					stockBean.setKeep_stock(rs.getString("keep_stock"));
					
					list.add(stockBean);
				}
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
//				System.out.println(jsonStrList);
				return jsonStrList;
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		}
		

	}

}
