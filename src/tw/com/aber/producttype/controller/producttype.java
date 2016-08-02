package tw.com.aber.producttype.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class producttype extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		producttypeService producttypeService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("searh".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String type_name = request.getParameter("type_name");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (type_name == null || (type_name.trim()).length() == 0) {
					producttypeService = new producttypeService();
					List<producttypeVO> list = producttypeService.getSearAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				producttypeService = new producttypeService();
				List<producttypeVO> list = producttypeService.getSearhDB(group_id, type_name);
				/*************************** 3.查詢完成,準備轉交(Send the Success view) ***********/
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("insert".equals(action)) {
			try {
				/*************************** 1.接收請求參數 **************************************/
				String type_name = request.getParameter("type_name");
				/*************************** 2.開始新增資料 ***************************************/
				producttypeService = new producttypeService();
				producttypeService.addproducttype(group_id, type_name, user_id);

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				producttypeService = new producttypeService();
				List<producttypeVO> list = producttypeService.getSearAllDB(group_id);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String type_id = request.getParameter("type_id");
				String type_name = request.getParameter("type_name");
				/*************************** 2.開始修改資料 ***************************************/
				producttypeService = new producttypeService();
				producttypeService.updateproducttype(type_id, group_id, type_name, user_id);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				producttypeService = new producttypeService();
				List<producttypeVO> list = producttypeService.getSearAllDB(group_id);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("delete".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String type_id = request.getParameter("type_id");
				/*************************** 2.開始刪除資料 ***************************************/
				producttypeService = new producttypeService();
				producttypeService.deleteproducttype(type_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				producttypeService = new producttypeService();
				List<producttypeVO> list = producttypeService.getSearAllDB(group_id);
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
	public class producttypeVO implements java.io.Serializable {
		private String type_id;
		private String group_id;
		private String type_name;
		private String user_id;

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String gettype_id() {
			return type_id;
		}

		public void settype_id(String type_id) {
			this.type_id = type_id;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String gettype_name() {
			return type_name;
		}

		public void settype_name(String type_name) {
			this.type_name = type_name;
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface producttype_interface {

		public void insertDB(producttypeVO producttypeVO);

		public void updateDB(producttypeVO producttypeVO);

		public void deleteDB(String type_id, String user_id);

		public List<producttypeVO> searhDB(String group_id, String type_name);

		public List<producttypeVO> searhAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class producttypeService {
		private producttype_interface dao;

		public producttypeService() {
			dao = new producttypeDAO();
		}

		public producttypeVO addproducttype(String group_id, String type_name, String user_id) {
			producttypeVO producttypeVO = new producttypeVO();
			producttypeVO.setGroup_id(group_id);
			producttypeVO.settype_name(type_name);
			producttypeVO.setUser_id(user_id);
			dao.insertDB(producttypeVO);
			return producttypeVO;
		}

		public producttypeVO updateproducttype(String type_id, String group_id, String type_name, String user_id) {
			producttypeVO producttypeVO = new producttypeVO();
			producttypeVO.settype_id(type_id);
			producttypeVO.setGroup_id(group_id);
			producttypeVO.settype_name(type_name);
			producttypeVO.setUser_id(user_id);
			dao.updateDB(producttypeVO);
			return producttypeVO;
		}

		public void deleteproducttype(String type_id, String user_id) {
			dao.deleteDB(type_id, user_id);
		}

		public List<producttypeVO> getSearhDB(String group_id, String type_name) {
			return dao.searhDB(group_id, type_name);
		}

		public List<producttypeVO> getSearAllDB(String group_id) {
			return dao.searhAllDB(group_id);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class producttypeDAO implements producttype_interface {
		// 會使用到的Stored procedure
		private static final String sp_insert_product_type = "call sp_insert_product_type(?,?,?)";
		private static final String sp_selectall_product_type = "call sp_selectall_product_type (?)";
		private static final String sp_select_product_type = "call sp_select_product_type (?,?)";
		private static final String sp_del_product_type = "call sp_del_product_type (?,?)";
		private static final String sp_update_product_type = "call sp_update_product_type (?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(producttypeVO producttypeVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_type);

				pstmt.setString(1, producttypeVO.getGroup_id());
				pstmt.setString(2, producttypeVO.gettype_name());
				pstmt.setString(3, producttypeVO.getUser_id());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public void updateDB(producttypeVO producttypeVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_product_type);

				pstmt.setString(1, producttypeVO.gettype_id());
				pstmt.setString(2, producttypeVO.getGroup_id());
				pstmt.setString(3, producttypeVO.gettype_name());
				pstmt.setString(4, producttypeVO.getUser_id());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public void deleteDB(String type_id, String user_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_product_type);
				pstmt.setString(1, type_id);
				pstmt.setString(2, user_id);

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public List<producttypeVO> searhDB(String group_id, String type_name) {
			List<producttypeVO> list = new ArrayList<producttypeVO>();
			producttypeVO producttypeVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_product_type);

				pstmt.setString(1, group_id);
				pstmt.setString(2, type_name);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					producttypeVO = new producttypeVO();
					producttypeVO.setGroup_id(rs.getString("group_id"));
					producttypeVO.settype_id(rs.getString("type_id"));
					producttypeVO.settype_name(rs.getString("type_name"));
					list.add(producttypeVO);
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public List<producttypeVO> searhAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<producttypeVO> list = new ArrayList<producttypeVO>();
			producttypeVO producttypeVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_product_type);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					producttypeVO = new producttypeVO();
					producttypeVO.setGroup_id(rs.getString("group_id"));
					producttypeVO.settype_id(rs.getString("type_id"));
					producttypeVO.settype_name(rs.getString("type_name"));
					list.add(producttypeVO); // Store the row in the list
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
	}
}