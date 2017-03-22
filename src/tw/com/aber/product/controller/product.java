package tw.com.aber.product.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.vo.ProductTypeVO;
import tw.com.aber.vo.SupplyVO;

public class product extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(product.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		ProductService productService = null;
		String action = request.getParameter("action");
		logger.debug("Action:" + action);
		
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("find_barcode".equals(action)) {
			String barcode = request.getParameter("barcode");
			productService = new ProductService();
			List<ProductBean> list = productService.getsearchBarcode(group_id, barcode);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;
		}
		if ("is_duplicate".equals(action)) {
			String product_name = request.getParameter("product_name");
			if (product_name == null || (product_name.trim()).length() == 0) {
			} else {
				productService = new ProductService();
				String result = productService.is_duplicate(group_id, product_name);
				response.getWriter().write(result);
				return;
			}
		}
		if ("search_name".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String product_name = request.getParameter("product_name");
				logger.debug("product_name:" + product_name);
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (product_name == null || (product_name.trim()).length() == 0) {
					productService = new ProductService();
					List<ProductBean> list = productService.SearchAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					logger.debug("jsonStrList:" + jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定Name 假如廠商名稱輸入不適空白 或是有東西 進入下面
				if (product_name != null || (product_name.trim()).length() > 0) {
					productService = new ProductService();
					List<ProductBean> list = productService.getsearch_byname(group_id, product_name);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					// System.out.println(jsonStrList);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("search".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String supply_name = request.getParameter("supply_name");
				logger.debug("supply_name:" + supply_name);
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (supply_name == null || (supply_name.trim()).length() == 0) {
					productService = new ProductService();
					List<ProductBean> list = productService.SearchAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					logger.debug("jsonStrList:" + jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定Name 假如廠商名稱輸入不是空白 或是有東西 進入下面
				if (supply_name != null || (supply_name.trim()).length() > 0) {
					productService = new ProductService();
					List<ProductBean> list = productService.getsearchSupplyname(group_id, supply_name);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					logger.debug("jsonStrList:" + jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 處理auto complete
		if ("search_product_data".equals(action)) {
			String term = request.getParameter("term");
			String identity = request.getParameter("identity");
			if ("ID".equals(identity)) {
				// System.out.println("hihi");
				productService = new ProductService();
				List<ProductTypeVO> list = productService.getSearchUnit_byname(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				// System.out.println("json: "+jsonStrList);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("ID2".equals(identity)) {
				productService = new ProductService();
				List<ProductTypeVO> list = productService.getSearchType_byname(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("NAME".equals(identity)) {
				productService = new ProductService();
				List<SupplyVO> list = productService.getSearchSupplyname(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
		}
		if ("insert".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 **************************************/
				String c_product_id = request.getParameter("c_product_id");
				String product_name = request.getParameter("product_name");
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String type_id = request.getParameter("type_id");
				String unit_id = request.getParameter("unit_id");
				float cost = Float
						.valueOf((request.getParameter("cost") + "").equals("") ? "0" : request.getParameter("cost"));
				float price = Float
						.valueOf((request.getParameter("price") + "").equals("") ? "0" : request.getParameter("price"));
				int current_stock = Integer.valueOf((request.getParameter("current_stock") + "").equals("") ? "0"
						: request.getParameter("current_stock"));
				int keep_stock = Integer.valueOf((request.getParameter("keep_stock") + "").equals("") ? "0"
						: request.getParameter("keep_stock"));
				String photo = request.getParameter("photo");
				String photo1 = request.getParameter("photo1");
				String description = request.getParameter("description");
				String barcode = request.getParameter("barcode");
				String ispackage = request.getParameter("ispackage");
				/***************************
				 * 2.開始新增資料
				 ***************************************/
				productService = new ProductService();
				// System.out.println(photo+" @@ "+photo1);
				productService.addProduct(group_id, c_product_id, product_name, supply_id, supply_name, type_id,
						unit_id, cost, price, current_stock, keep_stock, photo, photo1, description, barcode, ispackage,
						user_id);
				/***************************
				 * 3.新增完成,準備轉交(Send the Success view)
				 ***********/
				productService = new ProductService();
				List<ProductBean> list = productService.SearchAllDB(group_id);
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
				String product_id = request.getParameter("product_id");
				/***************************
				 * 2.開始刪除資料
				 ***************************************/
				productService = new ProductService();
				productService.deleteProduct(product_id, user_id);
				/***************************
				 * 3.刪除完成,準備轉交(Send the Success view)
				 ***********/
				productService = new ProductService();
				List<ProductBean> list = productService.SearchAllDB(group_id);
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
				String product_id = request.getParameter("product_id");
				String c_product_id = request.getParameter("c_product_id");
				String product_name = request.getParameter("product_name");
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String type_id = request.getParameter("type_id");
				String unit_id = request.getParameter("unit_id");
				float cost = Float.valueOf(request.getParameter("cost"));
				float price = Float.valueOf(request.getParameter("price"));
				int keep_stock = Integer.valueOf(request.getParameter("keep_stock"));
				String photo = request.getParameter("photo");
				String photo1 = request.getParameter("photo1");
				// System.out.println("photo: " + photo);
				String description = request.getParameter("description");
				String barcode = request.getParameter("barcode");
				String ispackage = request.getParameter("ispackage");
				/***************************
				 * 2.開始修改資料
				 ***************************************/
				productService = new ProductService();
				productService.updateProduct(product_id, group_id, c_product_id, product_name, supply_id, supply_name,
						type_id, unit_id, cost, price, keep_stock, photo, photo1, description, barcode, ispackage,
						user_id);
				/***************************
				 * 3.修改完成,準備轉alert(json_obj)交(Send the Success view)
				 ***********/
				productService = new ProductService();
				List<ProductBean> list = productService.SearchAllDB(group_id);
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
	public class ProductBean implements Serializable {

		private static final long serialVersionUID = 1L;
		private String product_id;
		private String group_id;
		private String c_product_id;
		private String product_name;
		private String supply_id;
		private String supply_name;
		private String type_id;
		private String unit_id;
		private float cost;
		private float price;
		private int current_stock;
		private int keep_stock;
		private String photo;
		private String photo1;
		private String description;
		private String barcode;
		private String user_id;
		private String message;
		private String ispackage;

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getC_product_id() {
			return c_product_id;
		}

		public void setC_product_id(String c_product_id) {
			this.c_product_id = c_product_id;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getSupply_id() {
			return supply_id;
		}

		public void setSupply_id(String supply_id) {
			this.supply_id = supply_id;
		}

		public String getSupply_name() {
			return supply_name;
		}

		public void setSupply_name(String supply_name) {
			this.supply_name = supply_name;
		}

		public String getType_id() {
			return type_id;
		}

		public void setType_id(String type_id) {
			this.type_id = type_id;
		}

		public String getUnit_id() {
			return unit_id;
		}

		public void setUnit_id(String unit_id) {
			this.unit_id = unit_id;
		}

		public float getCost() {
			return cost;
		}

		public void setCost(float cost) {
			this.cost = cost;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public int getKeep_stock() {
			return keep_stock;
		}

		public void setKeep_stock(int keep_stock) {
			this.keep_stock = keep_stock;
		}

		public int getCurrent_stock() {
			return current_stock;
		}

		public void setCurrent_stock(int current_stock) {
			this.current_stock = current_stock;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPhoto1() {
			return photo1;
		}

		public void setPhoto1(String photo1) {
			this.photo1 = photo1;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getIspackage() {
			return ispackage;
		}

		public void setIspackage(String ispackage) {
			this.ispackage = ispackage;
		}

	}

	/*************************** 制定規章方法 ****************************************/
	interface product_interface {

		public void insertDB(ProductBean productBean);

		public void updateDB(ProductBean productBean);

		public void deleteDB(String product_id, String user_id);

		public List<ProductBean> searchAllDB(String group_id);

		public List<SupplyVO> getSupplyname(String group_id, String supply_name);

		public List<ProductTypeVO> getType_byname(String group_id, String type_id);

		public List<ProductTypeVO> getUnit_byname(String group_id, String unit_id);

		public List<ProductBean> getsearchSupplyname(String group_id, String supply_name);

		public List<ProductBean> getsearch_byname(String group_id, String product_name);

		public List<ProductBean> getsearchBarcode(String group_id, String barcode);

		public String is_duplicate(String group_id, String product_name);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class ProductService {
		private product_interface dao;

		public ProductService() {
			dao = new ProductDAO();
		}

		public ProductBean addProduct(String group_id, String c_product_id, String product_name, String supply_id,
				String supply_name, String type_id, String unit_id, Float cost, Float price, int current_stock,
				int keep_stock, String photo, String photo1, String description, String barcode, String ispackage,
				String user_id) {
			ProductBean productBean = new ProductBean();
			productBean.setGroup_id(group_id);
			productBean.setC_product_id(c_product_id);
			productBean.setProduct_name(product_name);
			productBean.setSupply_id(supply_id);
			productBean.setSupply_name(supply_name);
			productBean.setType_id(type_id);
			productBean.setUnit_id(unit_id);
			productBean.setCost(cost);
			;
			productBean.setPrice(price);
			productBean.setCurrent_stock(current_stock);
			productBean.setKeep_stock(keep_stock);
			productBean.setPhoto(photo);
			productBean.setPhoto1(photo1);
			productBean.setDescription(description);
			productBean.setBarcode(barcode);
			productBean.setUser_id(user_id);
			productBean.setIspackage(ispackage);
			dao.insertDB(productBean);
			return productBean;
		}

		public ProductBean updateProduct(String product_id, String group_id, String c_product_id, String product_name,
				String supply_id, String supply_name, String type_id, String unit_id, Float cost, Float price,
				int keep_stock, String photo, String photo1, String description, String barcode, String ispackage,
				String user_id) {
			ProductBean productBean = new ProductBean();

			productBean.setProduct_id(product_id);
			productBean.setGroup_id(group_id);
			productBean.setC_product_id(c_product_id);
			productBean.setProduct_name(product_name);
			productBean.setSupply_id(supply_id);
			productBean.setSupply_name(supply_name);
			productBean.setType_id(type_id);
			productBean.setUnit_id(unit_id);
			productBean.setCost(cost);
			;
			productBean.setPrice(price);
			productBean.setKeep_stock(keep_stock);
			productBean.setPhoto(photo);
			productBean.setPhoto1(photo1);
			productBean.setDescription(description);
			productBean.setBarcode(barcode);
			productBean.setUser_id(user_id);
			productBean.setIspackage(ispackage);
			dao.updateDB(productBean);
			return productBean;
		}

		public void deleteProduct(String product_id, String user_id) {
			dao.deleteDB(product_id, user_id);
		}

		public List<ProductBean> SearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public List<SupplyVO> getSearchSupplyname(String group_id, String supply_name) {
			return dao.getSupplyname(group_id, supply_name);
		}

		public List<ProductTypeVO> getSearchType_byname(String group_id, String type_id) {
			return dao.getType_byname(group_id, type_id);
		}

		public List<ProductTypeVO> getSearchUnit_byname(String group_id, String unit_id) {
			return dao.getUnit_byname(group_id, unit_id);
		}

		public List<ProductBean> getsearchSupplyname(String group_id, String supply_name) {
			return dao.getsearchSupplyname(group_id, supply_name);
		}

		public List<ProductBean> getsearch_byname(String group_id, String product_name) {
			return dao.getsearch_byname(group_id, product_name);
		}

		public List<ProductBean> getsearchBarcode(String group_id, String barcode) {
			return dao.getsearchBarcode(group_id, barcode);
		}

		public String is_duplicate(String group_id, String product_name) {
			return dao.is_duplicate(group_id, product_name);
		}

	}

	/*************************** 操作資料庫 ****************************************/
	class ProductDAO implements product_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_product_bybarcode = "call sp_get_product_bybarcode(?,?)";
		private static final String sp_selectall_product = "call sp_selectall_product (?)";
		private static final String sp_insert_product = "call sp_insert_product(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
		private static final String sp_del_product = "call sp_del_product (?,?)";
		private static final String sp_update_product = "call sp_update_product (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
		private static final String sp_get_product_bysupplyname = "call sp_get_product_bysupplyname (?,?)";
		private static final String sp_get_product_byproductname = "call sp_get_product_byproductname (?,?)";
		private static final String sp_get_supplyname = "call sp_get_supplyname (?,?)";
		private static final String sp_get_type_byname = "call sp_select_type_byname (?,?)";
		private static final String sp_get_unit_byname = "call sp_select_unit_byname (?,?)";
		private static final String sp_check_ProductName = "call sp_check_ProductName (?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(ProductBean productBean) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product);

				pstmt.setString(1, productBean.getGroup_id());
				pstmt.setString(2, productBean.getC_product_id());
				pstmt.setString(3, productBean.getProduct_name());
				pstmt.setString(4, productBean.getSupply_id());
				pstmt.setString(5, productBean.getSupply_name());
				pstmt.setString(6, productBean.getType_id());
				pstmt.setString(7, productBean.getUnit_id());
				pstmt.setFloat(8, productBean.getCost());
				pstmt.setFloat(9, productBean.getPrice());
				pstmt.setInt(10, productBean.getKeep_stock());
				pstmt.setString(11, productBean.getPhoto());
				pstmt.setString(12, productBean.getPhoto1());
				pstmt.setString(13, productBean.getDescription());
				pstmt.setString(14, productBean.getBarcode());
				pstmt.setString(15, productBean.getUser_id());
				pstmt.setInt(16, productBean.getCurrent_stock());
				pstmt.setString(17, productBean.getIspackage());
				// System.out.println("in"+productBean.getPhoto()+"in"+productBean.getPhoto1());

				pstmt.executeUpdate();

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
		public void updateDB(ProductBean productBean) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_product);

				pstmt.setString(1, productBean.getProduct_id());
				pstmt.setString(2, productBean.getGroup_id());
				pstmt.setString(3, productBean.getC_product_id());
				pstmt.setString(4, productBean.getProduct_name());
				pstmt.setString(5, productBean.getSupply_id());
				pstmt.setString(6, productBean.getSupply_name());
				pstmt.setString(7, productBean.getType_id());
				pstmt.setString(8, productBean.getUnit_id());
				pstmt.setFloat(9, productBean.getCost());
				pstmt.setFloat(10, productBean.getPrice());
				pstmt.setInt(11, productBean.getKeep_stock());
				// System.out.println("have throw: "+productBean.getPhoto());
				pstmt.setString(12, productBean.getPhoto());
				pstmt.setString(13, productBean.getPhoto1());
				pstmt.setString(14, productBean.getDescription());
				pstmt.setString(15, productBean.getBarcode());
				pstmt.setString(16, productBean.getUser_id());
				pstmt.setString(17, productBean.getIspackage());
				pstmt.executeUpdate();

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
		public void deleteDB(String product_id, String user_id) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_product);
				pstmt.setString(1, product_id);
				pstmt.setString(2, user_id);

				pstmt.executeUpdate();

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
		public List<ProductBean> searchAllDB(String group_id) {

			List<ProductBean> list = new ArrayList<ProductBean>();
			ProductBean productBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_product);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productBean = new ProductBean();
					productBean.setProduct_id(rs.getString("product_id"));
					productBean.setGroup_id(rs.getString("group_id"));
					productBean.setC_product_id(rs.getString("c_product_id"));
					productBean.setProduct_name(rs.getString("product_name"));
					productBean.setSupply_id(rs.getString("supply_id"));
					productBean.setSupply_name(rs.getString("supply_name"));
					productBean.setType_id(rs.getString("type_id"));
					productBean.setUnit_id(rs.getString("unit_id"));
					productBean.setCost(rs.getFloat("cost"));
					productBean.setPrice(rs.getFloat("price"));
					productBean.setKeep_stock(rs.getInt("keep_stock"));
					productBean.setPhoto(rs.getString("photo"));
					productBean.setPhoto1(rs.getString("photo1"));
					productBean.setDescription(rs.getString("description"));
					productBean.setBarcode(rs.getString("barcode"));
					// if(rs.getInt("package")==0){
					// System.out.println(rs.getString("package"));
					productBean.setIspackage(rs.getString("package"));
					list.add(productBean); // Store the row in the list
					// }
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

		@Override
		public List<SupplyVO> getSupplyname(String group_id, String supplyname) {

			List<SupplyVO> list = new ArrayList<SupplyVO>();
			SupplyVO supplyVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_supplyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supplyname);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					supplyVO = new SupplyVO();
					supplyVO.setSupply_id(rs.getString("supply_id"));
					supplyVO.setSupply_name(rs.getString("supply_name"));
					list.add(supplyVO);
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

		@Override
		public List<ProductTypeVO> getType_byname(String group_id, String type_id) {

			List<ProductTypeVO> list = new ArrayList<ProductTypeVO>();
			ProductTypeVO productTypeVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_type_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, type_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productTypeVO = new ProductTypeVO();
					productTypeVO.setType_id(rs.getString("type_id"));
					productTypeVO.setType_name(rs.getString("type_name"));
					list.add(productTypeVO);
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

		@Override
		public List<ProductTypeVO> getUnit_byname(String group_id, String unit_id) {

			List<ProductTypeVO> list = new ArrayList<ProductTypeVO>();
			ProductTypeVO productTypeVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_unit_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, unit_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productTypeVO = new ProductTypeVO();
					productTypeVO.setUnit_name(rs.getString("unit_name"));
					productTypeVO.setUnit_id(rs.getString("unit_id"));
					list.add(productTypeVO);
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

		@Override
		public List<ProductBean> getsearchSupplyname(String group_id, String supply_name) {

			List<ProductBean> list = new ArrayList<ProductBean>();
			ProductBean productBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_bysupplyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productBean = new ProductBean();
					productBean.setProduct_id(rs.getString("product_id"));
					productBean.setGroup_id(rs.getString("group_id"));
					productBean.setC_product_id(rs.getString("c_product_id"));
					productBean.setProduct_name(rs.getString("product_name"));
					productBean.setSupply_id(rs.getString("supply_id"));
					productBean.setSupply_name(rs.getString("supply_name"));
					productBean.setType_id(rs.getString("type_id"));
					productBean.setUnit_id(rs.getString("unit_id"));
					productBean.setCost(rs.getFloat("cost"));
					productBean.setPrice(rs.getFloat("price"));
					productBean.setKeep_stock(rs.getInt("keep_stock"));
					productBean.setPhoto(rs.getString("photo"));
					productBean.setPhoto1(rs.getString("photo1"));
					productBean.setDescription(rs.getString("description"));
					productBean.setBarcode(rs.getString("barcode"));
					list.add(productBean);
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

		public List<ProductBean> getsearch_byname(String group_id, String product_name) {

			List<ProductBean> list = new ArrayList<ProductBean>();
			ProductBean productBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_byproductname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productBean = new ProductBean();
					productBean.setProduct_id(rs.getString("product_id"));
					productBean.setGroup_id(rs.getString("group_id"));
					productBean.setC_product_id(rs.getString("c_product_id"));
					productBean.setProduct_name(rs.getString("product_name"));
					productBean.setSupply_id(rs.getString("supply_id"));
					productBean.setSupply_name(rs.getString("supply_name"));
					productBean.setType_id(rs.getString("type_id"));
					productBean.setUnit_id(rs.getString("unit_id"));
					productBean.setCost(rs.getFloat("cost"));
					productBean.setPrice(rs.getFloat("price"));
					productBean.setKeep_stock(rs.getInt("keep_stock"));
					productBean.setPhoto(rs.getString("photo"));
					productBean.setPhoto1(rs.getString("photo1"));
					productBean.setDescription(rs.getString("description"));
					productBean.setBarcode(rs.getString("barcode"));
					list.add(productBean);
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

		public List<ProductBean> getsearchBarcode(String group_id, String barcode) {
			List<ProductBean> list = new ArrayList<ProductBean>();
			ProductBean productBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_bybarcode);
				pstmt.setString(1, group_id);
				pstmt.setString(2, barcode);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productBean = new ProductBean();
					productBean.setProduct_id(rs.getString("product_id"));
					// productBean.setGroup_id(rs.getString("group_id"));
					productBean.setC_product_id(rs.getString("c_product_id"));
					productBean.setProduct_name(rs.getString("product_name"));
					// productBean.setSupply_id(rs.getString("supply_id"));
					// productBean.setSupply_name(rs.getString("supply_name"));
					// productBean.setType_id(rs.getString("type_id"));
					// productBean.setUnit_id(rs.getString("unit_id"));
					productBean.setCost(rs.getFloat("cost"));
					// productBean.setPrice(rs.getFloat("price"));
					productBean.setKeep_stock(rs.getInt("quantity"));
					// productBean.setPhoto(rs.getString("photo"));
					// productBean.setPhoto1(rs.getString("photo1"));
					// productBean.setDescription(rs.getString("description"));
					// productBean.setBarcode(rs.getString("barcode"));
					list.add(productBean);
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

		public String is_duplicate(String group_id, String product_name) {
			// System.out.println("ERROR?? ");
			Connection con = null;
			CallableStatement cs = null;
			Boolean rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_check_ProductName);
				cs.registerOutParameter(3, Types.BOOLEAN);
				cs.setString(1, group_id);
				cs.setString(2, product_name);
				cs.execute();
				rs = cs.getBoolean(3);
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			if (rs) {
				return "true";
			} else {
				return "false";
			}
		}
	}
}