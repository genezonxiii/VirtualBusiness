package tw.com.aber.productunit.controller;

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

@SuppressWarnings("serial")
public class productunit extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		ProductunitService productunitService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("searh".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String unit_name = request.getParameter("unit_name");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (unit_name == null || (unit_name.trim()).length() == 0) {
					productunitService = new ProductunitService();
					List<ProductunitVO> list = productunitService.getSearAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				productunitService = new ProductunitService();
				List<ProductunitVO> list = productunitService.getSearhDB(group_id, unit_name);
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
				String unit_name = request.getParameter("unit_name");
				/*************************** 2.開始新增資料 ***************************************/
				productunitService = new ProductunitService();
				productunitService.addProductunit(group_id, unit_name, user_id);

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				productunitService = new ProductunitService();
				List<ProductunitVO> list = productunitService.getSearAllDB(group_id);
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
				String unit_id = request.getParameter("unit_id");
				String unit_name = request.getParameter("unit_name");
				/*************************** 2.開始修改資料 ***************************************/
				productunitService = new ProductunitService();
				productunitService.updateProductunit(unit_id, group_id, unit_name, user_id);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				productunitService = new ProductunitService();
				List<ProductunitVO> list = productunitService.getSearAllDB(group_id);
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
				String unit_id = request.getParameter("unit_id");
				/*************************** 2.開始刪除資料 ***************************************/
				productunitService = new ProductunitService();
				productunitService.deleteProductunit(unit_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				productunitService = new ProductunitService();
				List<ProductunitVO> list = productunitService.getSearAllDB(group_id);
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
	public class ProductunitVO implements java.io.Serializable {
		private String unit_id;
		private String group_id;
		private String unit_name;
		private String user_id;

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUnit_id() {
			return unit_id;
		}

		public void setUnit_id(String unit_id) {
			this.unit_id = unit_id;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getUnit_name() {
			return unit_name;
		}

		public void setUnit_name(String unit_name) {
			this.unit_name = unit_name;
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface Productunit_interface {

		public void insertDB(ProductunitVO productunitVO);

		public void updateDB(ProductunitVO productunitVO);

		public void deleteDB(String unit_id, String user_id);

		public List<ProductunitVO> searhDB(String group_id, String unit_name);

		public List<ProductunitVO> searhAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class ProductunitService {
		private Productunit_interface dao;

		public ProductunitService() {
			dao = new ProductunitDAO();
		}

		public ProductunitVO addProductunit(String group_id, String unit_name, String user_id) {
			ProductunitVO productunitVO = new ProductunitVO();
			productunitVO.setGroup_id(group_id);
			productunitVO.setUnit_name(unit_name);
			productunitVO.setUser_id(user_id);
			dao.insertDB(productunitVO);
			return productunitVO;
		}

		public ProductunitVO updateProductunit(String unit_id, String group_id, String unit_name, String user_id) {
			ProductunitVO productunitVO = new ProductunitVO();
			productunitVO.setUnit_id(unit_id);
			productunitVO.setGroup_id(group_id);
			productunitVO.setUnit_name(unit_name);
			productunitVO.setUser_id(user_id);
			dao.updateDB(productunitVO);
			return productunitVO;
		}

		public void deleteProductunit(String unit_id, String user_id) {
			dao.deleteDB(unit_id, user_id);
		}

		public List<ProductunitVO> getSearhDB(String group_id, String unit_name) {
			return dao.searhDB(group_id, unit_name);
		}

		public List<ProductunitVO> getSearAllDB(String group_id) {
			return dao.searhAllDB(group_id);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class ProductunitDAO implements Productunit_interface {
		// 會使用到的Stored procedure
		private static final String sp_insert_product_unit = "call sp_insert_product_unit(?,?,?)";
		private static final String sp_selectall_product_unit = "call sp_selectall_product_unit (?)";
		private static final String sp_select_product_unit = "call sp_select_product_unit (?,?)";
		private static final String sp_del_product_unit = "call sp_del_product_unit (?,?)";
		private static final String sp_update_product_unit = "call sp_update_product_unit (?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL");
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(ProductunitVO productunitVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_unit);

				pstmt.setString(1, productunitVO.getGroup_id());
				pstmt.setString(2, productunitVO.getUnit_name());
				pstmt.setString(3, productunitVO.getUser_id());

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
		public void updateDB(ProductunitVO productunitVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
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
		public void deleteDB(String unit_id, String user_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_product_unit);
				pstmt.setString(1, unit_id);
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
		public List<ProductunitVO> searhDB(String group_id, String unit_name) {
			List<ProductunitVO> list = new ArrayList<ProductunitVO>();
			ProductunitVO productunitVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_product_unit);

				pstmt.setString(1, group_id);
				pstmt.setString(2, unit_name);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					productunitVO = new ProductunitVO();
					productunitVO.setGroup_id(rs.getString("group_id"));
					productunitVO.setUnit_id(rs.getString("unit_id"));
					productunitVO.setUnit_name(rs.getString("unit_name"));
					list.add(productunitVO);
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
		public List<ProductunitVO> searhAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<ProductunitVO> list = new ArrayList<ProductunitVO>();
			ProductunitVO productunitVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_product_unit);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productunitVO = new ProductunitVO();
					productunitVO.setGroup_id(rs.getString("group_id"));
					productunitVO.setUnit_id(rs.getString("unit_id"));
					productunitVO.setUnit_name(rs.getString("unit_name"));
					list.add(productunitVO); // Store the row in the list
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
