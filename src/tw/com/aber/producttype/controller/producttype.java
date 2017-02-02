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

import tw.com.aber.vo.ProductTypeVO;

public class producttype extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		producttypeService producttypeService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("searh".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ****************************************/
				String type_name = request.getParameter("type_name");
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (type_name == null || (type_name.trim()).length() == 0) {
					producttypeService = new producttypeService();
					List<ProductTypeVO> list = producttypeService.getSearAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				producttypeService = new producttypeService();
				List<ProductTypeVO> list = producttypeService.getSearhDB(group_id, type_name);
				/***************************
				 * 3.查詢完成,準備轉交(Send the Success view)
				 ***********/
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
				/***************************
				 * 1.接收請求參數
				 **************************************/
				String type_name = request.getParameter("type_name");
				/***************************
				 * 2.開始新增資料
				 ***************************************/
				producttypeService = new producttypeService();
				producttypeService.addproducttype(group_id, type_name, user_id);

				/***************************
				 * 3.新增完成,準備轉交(Send the Success view)
				 ***********/
				producttypeService = new producttypeService();
				List<ProductTypeVO> list = producttypeService.getSearAllDB(group_id);
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
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String type_id = request.getParameter("type_id");
				String type_name = request.getParameter("type_name");
				/***************************
				 * 2.開始修改資料
				 ***************************************/
				producttypeService = new producttypeService();
				producttypeService.updateproducttype(type_id, group_id, type_name, user_id);
				/***************************
				 * 3.修改完成,準備轉交(Send the Success view)
				 ***********/
				producttypeService = new producttypeService();
				List<ProductTypeVO> list = producttypeService.getSearAllDB(group_id);
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
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String type_id = request.getParameter("type_id");
				/***************************
				 * 2.開始刪除資料
				 ***************************************/
				producttypeService = new producttypeService();
				producttypeService.deleteproducttype(type_id, user_id);
				/***************************
				 * 3.刪除完成,準備轉交(Send the Success view)
				 ***********/
				producttypeService = new producttypeService();
				List<ProductTypeVO> list = producttypeService.getSearAllDB(group_id);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface producttype_interface {

		public void insertDB(ProductTypeVO productTypeVO);

		public void updateDB(ProductTypeVO productTypeVO);

		public void deleteDB(String type_id, String user_id);

		public List<ProductTypeVO> searhDB(String group_id, String type_name);

		public List<ProductTypeVO> searhAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class producttypeService {
		private producttype_interface dao;

		public producttypeService() {
			dao = new producttypeDAO();
		}

		public ProductTypeVO addproducttype(String group_id, String type_name, String user_id) {
			ProductTypeVO productTypeVO = new ProductTypeVO();
			productTypeVO.setGroup_id(group_id);
			productTypeVO.setType_name(type_name);
			productTypeVO.setUser_id(user_id);
			dao.insertDB(productTypeVO);
			return productTypeVO;
		}

		public ProductTypeVO updateproducttype(String type_id, String group_id, String type_name, String user_id) {
			ProductTypeVO productTypeVO = new ProductTypeVO();
			productTypeVO.setType_id(type_id);
			productTypeVO.setGroup_id(group_id);
			productTypeVO.setType_name(type_name);
			productTypeVO.setUser_id(user_id);
			dao.updateDB(productTypeVO);
			return productTypeVO;
		}

		public void deleteproducttype(String type_id, String user_id) {
			dao.deleteDB(type_id, user_id);
		}

		public List<ProductTypeVO> getSearhDB(String group_id, String type_name) {
			return dao.searhDB(group_id, type_name);
		}

		public List<ProductTypeVO> getSearAllDB(String group_id) {
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
		public void insertDB(ProductTypeVO productTypeVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_type);

				pstmt.setString(1, productTypeVO.getGroup_id());
				pstmt.setString(2, productTypeVO.getType_name());
				pstmt.setString(3, productTypeVO.getUser_id());

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
		public void updateDB(ProductTypeVO productTypeVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_product_type);

				pstmt.setString(1, productTypeVO.getType_id());
				pstmt.setString(2, productTypeVO.getGroup_id());
				pstmt.setString(3, productTypeVO.getType_name());
				pstmt.setString(4, productTypeVO.getUser_id());

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
		public List<ProductTypeVO> searhDB(String group_id, String type_name) {
			List<ProductTypeVO> list = new ArrayList<ProductTypeVO>();
			ProductTypeVO productTypeVO = null;

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
					productTypeVO = new ProductTypeVO();
					productTypeVO.setGroup_id(rs.getString("group_id"));
					productTypeVO.setType_id(rs.getString("type_id"));
					productTypeVO.setType_name(rs.getString("type_name"));
					list.add(productTypeVO);
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
		public List<ProductTypeVO> searhAllDB(String group_id) {

			List<ProductTypeVO> list = new ArrayList<ProductTypeVO>();
			ProductTypeVO productTypeVO = null;

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
					productTypeVO = new ProductTypeVO();
					productTypeVO.setGroup_id(rs.getString("group_id"));
					productTypeVO.setType_id(rs.getString("type_id"));
					productTypeVO.setType_name(rs.getString("type_name"));
					list.add(productTypeVO); // Store the row in the list
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