package tw.com.aber.product.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.supply.controller.supply.SupplyBean;



public class product extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		ProductService productService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		//System.out.println("action: "+action);
		if ("find_barcode".equals(action)) {
			String barcode = request.getParameter("barcode");
			productService = new ProductService();
//			group_id="a604a6b1-4253-11e6-806e-000c29c1d067";
//			barcode="9787111463115";
			List<ProductBean> list = productService.getsearchBarcode(group_id,barcode);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			//System.out.println("jsonStrList: "+jsonStrList);
			response.getWriter().write(jsonStrList);
			return;
		}
//		if ("find_barcode2".equals(action)) {
//			String barcode = request.getParameter("barcode");
//			productService = new ProductService();
//			group_id="a604a6b1-4253-11e6-806e-000c29c1d067";
//			barcode="9787111463115";
//			List<ProductBean> list0 = productService.getsearchBarcode(group_id,barcode);
//			System.out.println("g: "+group_id+" c_p: "+list0.get(0).c_product_id);
//			List<ProductBean> list = productService.getsearchSupplyname(group_id,list0.get(0).c_product_id);
//			Gson gson = new Gson();
//			String jsonStrList = gson.toJson(list);
//			System.out.println("jsonStrList: "+jsonStrList);
//			response.getWriter().write(jsonStrList);
//			return;
//			
//		}
		if ("search".equals(action)) {
			try {
				/*************************** 1.接收請求參數-格式檢查 ****************************************/
				String supply_name = request.getParameter("supply_name");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (supply_name == null || (supply_name.trim()).length() == 0) {
					productService = new ProductService();
					List<ProductBean> list = productService.SearchAllDB(group_id);
					ProductBean productBean = new ProductBean();
					productBean.setMessage("驗證通過");
					list.add(productBean);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷	
				}
				// 查詢指定Name  假如廠商名稱輸入不適空白  或是有東西 進入下面
				if (supply_name != null || (supply_name.trim()).length() > 0) {
					productService = new ProductService();
					List<ProductBean> list = productService.getsearchSupplyname(group_id,supply_name);
					ProductBean productBean = new ProductBean();
					productBean.setMessage("驗證通過");
					list.add(productBean);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 //處理auto complete
		if ("search_product_data".equals(action)) {
			String term = request.getParameter("term");
			String identity = request.getParameter("identity");
			if ("ID".equals(identity)) {
				//System.out.println("hihi");
				productService = new ProductService();
				List<Product_idBean> list = productService.getSearchUnit_byname(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				//System.out.println("json: "+jsonStrList);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("ID2".equals(identity)) {
				productService = new ProductService();
				List<Product_idBean> list = productService.getSearchType_byname(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("NAME".equals(identity)) {
				productService = new ProductService();
				List<SupplyBean> list = productService.getSearchSupplyname(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
		}
		if ("insert".equals(action)) {
			try {
				/*************************** 1.接收請求參數 **************************************/
				String c_product_id = request.getParameter("c_product_id");
				String product_name = request.getParameter("product_name");
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String type_id = request.getParameter("type_id");
				String unit_id = request.getParameter("unit_id");
				float cost = Float.valueOf(request.getParameter("cost"));
				float price = Float.valueOf(request.getParameter("price"));
				int  keep_stock = Integer.valueOf(request.getParameter("keep_stock"));
				String photo = request.getParameter("photo");
				String photo1 = request.getParameter("photo1");
				String description = request.getParameter("description");
				String barcode = request.getParameter("barcode");
				System.out.println(supply_name);
				/*************************** 2.開始新增資料 ***************************************/
				productService = new ProductService();
				productService.addProduct(group_id, c_product_id, product_name, supply_id, supply_name, type_id, unit_id,
						cost, price, keep_stock, photo, photo1, description,barcode, user_id);
				/***************************
				 * 3.新增完成,準備轉交(Send the Success view)
				 ***********/
				productService = new ProductService();
				ProductBean productBean = new ProductBean();
				productBean.setMessage("驗證通過");
				List<ProductBean> list = productService.SearchAllDB(group_id);
				Gson gson = new Gson();
				list.add(productBean);
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
				String product_id = request.getParameter("product_id");	
				/*************************** 2.開始刪除資料 ***************************************/
				productService = new ProductService();
				productService.deleteProduct(product_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				productService = new ProductService();
				ProductBean productBean = new ProductBean();
				productBean.setMessage("驗證通過");
				List<ProductBean> list = productService.SearchAllDB(group_id);
				list.add(productBean);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String product_id = request.getParameter("product_id");
				String c_product_id = request.getParameter("c_product_id");
				String product_name = request.getParameter("product_name");
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String type_id = request.getParameter("type_id");
				String unit_id = request.getParameter("unit_id");
				float cost = Float.valueOf(request.getParameter("cost"));
				float price = Float.valueOf(request.getParameter("price"));
				int  keep_stock = Integer.valueOf(request.getParameter("keep_stock"));
				String photo = request.getParameter("photo");
				String photo1 = request.getParameter("photo1");
				String description = request.getParameter("description");
				String barcode = request.getParameter("barcode");
				/*************************** 2.開始修改資料 ***************************************/
				productService = new ProductService();
				productService.updateProduct(product_id,group_id, c_product_id, product_name, supply_id, supply_name, type_id, unit_id,
						cost, price, keep_stock, photo, photo1, description,barcode, user_id);
				/*************************** 3.修改完成,準備轉alert(json_obj)交(Send the Success view) ***********/
				productService = new ProductService();
				ProductBean productBean = new ProductBean();
				productBean.setMessage("驗證通過");
				List<ProductBean> list = productService.SearchAllDB(group_id);
				Gson gson = new Gson();
				list.add(productBean);
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		private int keep_stock;
		private String photo;
		private String photo1;
		private String description;
		private String barcode;
		private String user_id;
		private String message;
		
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
	}
		
		class SupplyBean implements java.io.Serializable {
			private String  supply_id;
			private String 	supply_name;
			
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
			
		}
		class  Product_idBean implements java.io.Serializable {
			private String type_id;
			private String type_name;
			private String unit_id;
			private String unit_name;
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
			public String getType_name() {
				return type_name;
			}
			public void setType_name(String type_name) {
				this.type_name = type_name;
			}
			public String getUnit_name() {
				return unit_name;
			}
			public void setUnit_name(String unit_name) {
				this.unit_name = unit_name;
			}
		
			
		}	

	/*************************** 制定規章方法 ****************************************/
	interface product_interface {

		public void insertDB(ProductBean productBean);

		public void updateDB(ProductBean productBean);

		public void deleteDB(String product_id, String user_id);

		public List<ProductBean> searchAllDB(String group_id);
	
		public List<SupplyBean> getSupplyname(String group_id, String supply_name);

		public List<Product_idBean> getType_byname(String group_id,String type_id);
		
		public List<Product_idBean> getUnit_byname(String group_id,String unit_id);
		
		public List<ProductBean> getsearchSupplyname(String group_id,String supply_name);
		
		public List<ProductBean> getsearchBarcode(String group_id,String barcode);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class ProductService {
		private product_interface dao;

		public ProductService() {
			dao = new ProductDAO();
		}

		public ProductBean addProduct( String group_id, String c_product_id, String product_name, String supply_id, String supply_name,
				String type_id, String unit_id, Float cost, Float price, int keep_stock, String photo, String photo1,
				String description, String barcode, String user_id) {
			ProductBean productBean = new ProductBean();

			productBean.setGroup_id(group_id);
			productBean.setC_product_id(c_product_id);
			productBean.setProduct_name(product_name);
			productBean.setSupply_id(supply_id);
			productBean.setSupply_name(supply_name);
			productBean.setType_id(type_id);
			productBean.setUnit_id(unit_id);
			productBean.setCost(cost);;
			productBean.setPrice(price);
			productBean.setKeep_stock(keep_stock);
			productBean.setPhoto(photo);
			productBean.setPhoto1(photo1);
			productBean.setDescription(description);
			productBean.setBarcode(barcode);
			productBean.setUser_id(user_id);
			dao.insertDB(productBean);
			return productBean;
		}

		public ProductBean updateProduct(String product_id, String group_id, String c_product_id, String product_name, String supply_id,
				String supply_name,String type_id, String unit_id, Float cost, Float price, int keep_stock, String photo, String photo1,
				String description, String barcode, String user_id) {
				ProductBean productBean = new ProductBean();

			productBean.setProduct_id(product_id);
			productBean.setGroup_id(group_id);
			productBean.setC_product_id(c_product_id);
			productBean.setProduct_name(product_name);
			productBean.setSupply_id(supply_id);
			productBean.setSupply_name(supply_name);
			productBean.setType_id(type_id);
			productBean.setUnit_id(unit_id);
			productBean.setCost(cost);;
			productBean.setPrice(price);
			productBean.setKeep_stock(keep_stock);
			productBean.setPhoto(photo);
			productBean.setPhoto1(photo1);
			productBean.setDescription(description);
			productBean.setBarcode(barcode);
			productBean.setUser_id(user_id);
			dao.updateDB(productBean);
			return productBean;
		}


		public void deleteProduct(String product_id, String user_id) {
			dao.deleteDB(product_id, user_id);
		}

		public List<ProductBean> SearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
		public List<SupplyBean> getSearchSupplyname(String group_id, String supply_name) {
			return dao.getSupplyname(group_id, supply_name);
		}
		
		public List<Product_idBean> getSearchType_byname(String group_id, String type_id) {
			return dao.getType_byname(group_id,type_id);
		}
		
		
		public List<Product_idBean> getSearchUnit_byname(String group_id, String unit_id) {
			return dao.getUnit_byname(group_id, unit_id);
		}

		public List<ProductBean> getsearchSupplyname(String group_id, String supply_name) {
			return dao.getsearchSupplyname(group_id, supply_name);
		}
		public List<ProductBean> getsearchBarcode(String group_id,String barcode){
			return dao.getsearchBarcode(group_id,barcode);
		}
		
	}

	/*************************** 操作資料庫 ****************************************/
	class ProductDAO implements product_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_product_bybarcode = "call sp_get_product_bybarcode(?,?)";
		private static final String sp_selectall_product = "call sp_selectall_product (?)";
		private static final String sp_insert_product = "call sp_insert_product(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 	?)";
		private static final String sp_del_product = "call sp_del_product (?,?)";
		private static final String sp_update_product = "call sp_update_product (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		private static final String sp_get_product_bysupplyname = "call sp_get_product_bysupplyname (?,?)";
		
		private static final String sp_get_supplyname = "call sp_get_supplyname (?,?)";
		private static final String	sp_get_type_byname =  "call sp_select_type_byname (?,?)";
		private static final String sp_get_unit_byname =  "call sp_select_unit_byname (?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(ProductBean productBean) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
				pstmt.setString(12, productBean.getPhoto());
				pstmt.setString(13, productBean.getPhoto1());
				pstmt.setString(14, productBean.getDescription());
				pstmt.setString(15, productBean.getBarcode());
				pstmt.setString(16, productBean.getUser_id());

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
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
					list.add(productBean); // Store the row in the list
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
	public List<SupplyBean> getSupplyname(String group_id, String supplyname) {
		// TODO Auto-generated method stub
		List<SupplyBean> list = new ArrayList<SupplyBean>();
		SupplyBean supplyBean = null;

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
				supplyBean =new SupplyBean();
				supplyBean.setSupply_id(rs.getString("supply_id"));
				supplyBean.setSupply_name(rs.getString("supply_name"));
				list.add(supplyBean);
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
	public List<Product_idBean>  getType_byname(String group_id, String type_id) {
		// TODO Auto-generated method stub
		List<Product_idBean> list = new ArrayList<Product_idBean>();
		Product_idBean product_idBean = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
			pstmt = con.prepareStatement(sp_get_type_byname);
			pstmt.setString(1, group_id);
			pstmt.setString(2,type_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				product_idBean =new Product_idBean();
				product_idBean.setType_id(rs.getString("type_id"));
				product_idBean.setType_name(rs.getString("type_name"));
				list.add(product_idBean);
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
	public List<Product_idBean> getUnit_byname(String group_id, String unit_id) {
		// TODO Auto-generated method stub
		List<Product_idBean> list = new ArrayList<Product_idBean>();
		Product_idBean product_idBean = null;

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
				product_idBean =new Product_idBean();
				product_idBean.setUnit_name(rs.getString("unit_name"));
				product_idBean.setUnit_id(rs.getString("unit_id"));
				list.add(product_idBean);
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
	public List<ProductBean>  getsearchSupplyname(String group_id, String supply_name) {
		// TODO Auto-generated method stub
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
			pstmt.setString(2,supply_name);
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
	public List<ProductBean>  getsearchBarcode(String group_id,String barcode) {
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
				//productBean.setGroup_id(rs.getString("group_id"));
				productBean.setC_product_id(rs.getString("c_product_id"));
				productBean.setProduct_name(rs.getString("product_name"));
				//productBean.setSupply_id(rs.getString("supply_id"));
				//productBean.setSupply_name(rs.getString("supply_name"));
				//productBean.setType_id(rs.getString("type_id"));
				//productBean.setUnit_id(rs.getString("unit_id"));
				productBean.setCost(rs.getFloat("cost"));
				//productBean.setPrice(rs.getFloat("price"));
				productBean.setKeep_stock(rs.getInt("quantity"));
				//productBean.setPhoto(rs.getString("photo"));
				//productBean.setPhoto1(rs.getString("photo1"));
				//productBean.setDescription(rs.getString("description"));
				//productBean.setBarcode(rs.getString("barcode"));
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
	}
}