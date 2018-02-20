
package tw.com.aber.product.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.sf.vo.ResponseUtil;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.PackageVO;

public class productpackage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(productpackage.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Util util = new Util();
		util.ConfirmLoginAgain(request, response);
		
		String group_id =(String) request.getSession().getAttribute("group_id");
		String user_id = (String)request.getSession().getAttribute("user_id");
		
		group_id = (group_id == null || group_id.length() < 3) ? "UNKNOWN" : group_id;
		String action = request.getParameter("action");
		ProductPackageDAO dao = new ProductPackageDAO();
		List<ProductPackageVO> list = new ArrayList<ProductPackageVO>();

		logger.debug("Action: " + action);

		if ("search".equals(action)) {
			String word = request.getParameter("word");

			logger.debug("word: " + word);

			word = word == null ? "" : word;
			List<ProductVO> parents = dao.searchPackagesList(group_id, word);
			
			Map<String, Object> map = new HashMap<>();
			map.put("result", null);
			map.put("data", parents);
			JSONObject jsonObject = new JSONObject(map);
			logger.debug("After delete:" + jsonObject.toString());
			response.getWriter().write(jsonObject.toString());
			return;
		} else if ("insert".equals(action)) {
			String c_package_id = request.getParameter("c_package_id");
			String supply_name = request.getParameter("supply_name");
			String package_name = request.getParameter("package_name");
			String price = request.getParameter("price");
			String package_type = request.getParameter("package_type");
			String barcode = request.getParameter("barcode");
			String description = request.getParameter("description");

			logger.debug("c_package_id:" + c_package_id);
			logger.debug("supply_name:" + supply_name);
			logger.debug("package_name:" + package_name);
			logger.debug("price:" + price);
			logger.debug("package_type:" + package_type);
			logger.debug("barcode:" + barcode);
			logger.debug("description:" + description);

			String result = dao.insertpackages(group_id, c_package_id, package_name, supply_name, price, package_type, barcode,
					description, user_id);
			List<ProductVO> parents = dao.searchPackagesList(group_id, "");
			
			Map<String, Object> map = new HashMap<>();
			map.put("result", result);
			map.put("data", parents);
			JSONObject jsonObject = new JSONObject(map);
			response.getWriter().write(jsonObject.toString());
		} else if ("update".equals(action)) {
			String package_id = request.getParameter("package_id");
			String c_package_id = request.getParameter("c_package_id");
			String supply_name = request.getParameter("supply_name");
			String package_name = request.getParameter("package_name");
			String price = request.getParameter("price");
			String package_type = request.getParameter("package_type");
			String barcode = request.getParameter("barcode");
			String description = request.getParameter("description");

			logger.debug("package_id:" + package_id);
			logger.debug("c_package_id:" + c_package_id);
			logger.debug("supply_name:" + supply_name);
			logger.debug("package_name:" + package_name);
			logger.debug("price:" + price);
			logger.debug("package_type:" + package_type);
			logger.debug("barcode:" + barcode);
			logger.debug("description:" + description);

			String result = dao.updatepackages(package_id, group_id, c_package_id, package_name, supply_name, price, package_type,
					barcode, description, user_id);
			List<ProductVO> parents = dao.searchPackagesList(group_id, "");

			Map<String, Object> map = new HashMap<>();
			map.put("result", result);
			map.put("data", parents);
			JSONObject jsonObject = new JSONObject(map);
			response.getWriter().write(jsonObject.toString());
		} else if ("delete".equals(action)) {
			String package_id = request.getParameter("package_id");
			logger.debug("Package ID:" + package_id);
			logger.debug("User ID:" + user_id);
			String result = dao.deletepackages(package_id, user_id);
			List<ProductVO> parents = dao.searchPackagesList(group_id, "");
			
			Map<String, Object> map = new HashMap<>();
			map.put("result", result);
			map.put("data", parents);
			JSONObject jsonObject = new JSONObject(map);
			response.getWriter().write(jsonObject.toString());
		} else if ("search_detail".equals(action)) {
			String package_id = request.getParameter("package_id");
			ProductVO[] parents = dao.searchpackagesdetail(package_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		} else if ("insert_detail".equals(action)) {
			String package_id = request.getParameter("package_id");
			String product_id = request.getParameter("product_id");
			String quantity = request.getParameter("quantity");
			String package_desc = request.getParameter("package_desc");

			logger.debug("package_id:" + package_id);
			logger.debug("product_id:" + product_id);
			logger.debug("quantity:" + quantity);
			logger.debug("package_desc:" + package_desc);

			dao.insertpackagesdetail(package_id, product_id, quantity, package_desc);
			ProductVO[] parents = dao.searchpackagesdetail(package_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		} else if ("update_detail".equals(action)) {
			String package_id = request.getParameter("package_id");
			String parent_id = request.getParameter("package_id2");
			String product_id = request.getParameter("product_id");
			String quantity = request.getParameter("quantity");
			String package_desc = request.getParameter("package_desc");

			logger.debug("package_id2:" + parent_id);
			logger.debug("package_id:" + package_id);
			logger.debug("product_id:" + product_id);
			logger.debug("quantity:" + quantity);
			logger.debug("package_desc:" + package_desc);

			dao.updatepackagesdetail(package_id, parent_id, product_id, quantity, package_desc);
			ProductVO[] parents = dao.searchpackagesdetail(parent_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		} else if ("delete_detail".equals(action)) {
			String package_id = request.getParameter("package_id");
			String parent_id = request.getParameter("parent_id");
			dao.deletepackagesdetail(package_id);
			ProductVO[] parents = dao.searchpackagesdetail(parent_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
			
		} else if ("sendToTelegraph".equals(action)) {
			String packageIds = request.getParameter("package_ids");
			
			List<tw.com.aber.vo.PackageVO> packageVOList = dao.getAllPackageInfo(group_id, packageIds);
			
			ValueService valueService = util.getValueService(request, response);
			String env = valueService.getGroupSfVO().getEnv();
			SfApi sfApi = new SfApi();
			String reqXml = sfApi.genBomService(packageVOList, valueService);
			String resXml = sfApi.sendXML(env, reqXml);
			ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String gresult = gson.toJson(responseUtil);
			response.getWriter().write(gresult);
		} else if ("sendItemService".equals(action)) {
			String packageIds = request.getParameter("package_ids");
			List<tw.com.aber.vo.PackageVO> packageVOList = dao.getAllPackageInfo(group_id, packageIds);

			ValueService valueService = util.getValueService(request, response);
			String env = valueService.getGroupSfVO().getEnv();
			SfApi sfApi = new SfApi();		
			String reqXml = sfApi.genItemServiceForPackage(packageVOList, valueService);
			String resXml = sfApi.sendXML(env, reqXml);
			ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String gresult = gson.toJson(responseUtil);
			response.getWriter().write(gresult);
		}else if ("get_data_by_c_productc_id".equals(action)) {
			try {
				String c_product_ids = request.getParameter("c_product_ids");
				String arr_c_product_id[]=c_product_ids.split("~");
				
				SfApi sfApi = new SfApi();

				ValueService valueService = util.getValueService(request, response);
				String env = valueService.getGroupSfVO().getEnv();
				String reqXml = sfApi.genItemQueryService(arr_c_product_id, valueService);
				String resXml = sfApi.sendXML(env, reqXml);
				ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);				
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String gresult = gson.toJson(responseUtil);
				response.getWriter().write(gresult);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
			
			
		return;
	}

	/************************* 對應資料庫表格格式 **************************************/
	public class ProductVO {
		String product_id;
		String group_id;
		String c_product_id;
		String product_name;
		String supply_id;
		String supply_name;
		String type_id;
		String unit_id;
		String cost;
		String price;
		String current_stock;
		String keep_stock;
		String photo;
		String photo1;
		String description;
		String barcode;
		String user_id;
		String message;
		String ispackage;
		String content;
		String package_id;
		String parent_id;
		String quantity;
		String package_desc;
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
		public String getCost() {
			return cost;
		}
		public void setCost(String cost) {
			this.cost = cost;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getCurrent_stock() {
			return current_stock;
		}
		public void setCurrent_stock(String current_stock) {
			this.current_stock = current_stock;
		}
		public String getKeep_stock() {
			return keep_stock;
		}
		public void setKeep_stock(String keep_stock) {
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
		public String getIspackage() {
			return ispackage;
		}
		public void setIspackage(String ispackage) {
			this.ispackage = ispackage;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getPackage_id() {
			return package_id;
		}
		public void setPackage_id(String package_id) {
			this.package_id = package_id;
		}
		public String getParent_id() {
			return parent_id;
		}
		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}
		public String getQuantity() {
			return quantity;
		}
		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		public String getPackage_desc() {
			return package_desc;
		}
		public void setPackage_desc(String package_desc) {
			this.package_desc = package_desc;
		}
	}

	public class ProductPackageVO {
		public String package_id;
		public String parent_id;
		public String product_id;
		public String quantity;
		public String package_desc;
	}

	/*************************** 操作資料庫 ****************************************/
	class ProductPackageDAO {
		// 會使用到的Stored procedure
		private static final String sp_select_package = "select * from tb_product where (package = true and group_id = ?) and (product_name LIKE CONCAT('%', ?, '%') or c_product_id LIKE CONCAT('%', ?, '%'))  order by create_time desc";
		private static final String sp_select_package_detail = "select * from tb_product_package INNER JOIN tb_product ON tb_product_package.product_id = tb_product.product_id  where parent_id = ? ";
		private static final String sp_insert_package = "call sp_insert_product(?,?,?,'','',?,'',0,?,0,'','',?,?,?,0,1)";
		private static final String sp_insert_package_detail = "call sp_insert_package (?,?,?,?)";
		private static final String sp_update_package = "call sp_update_product(?,?,?,?,'','',?,'',0,?,0,'','',?,?,?,1)";
		private static final String sp_update_package_detail = "call sp_update_package (?,?,?,?,?)";
		private static final String sp_delete_package = "call sp_del_product (?,?)";
		private static final String sp_delete_package2 = "call sp_del_package (?)";
		private static final String sp_delete_package_detail = "call sp_del_package_detail (?)";

		private static final String sp_get_package_master = "call sp_get_package_master(?,?,?);";
		private static final String sp_insert_package_master = "call sp_insert_package_master(?,?,?,?,?,?,?,?);";
		private static final String sp_update_package_master = "call sp_update_package_master(?,?,?,?,?,?,?,?,?);";
		private static final String sp_delete_package_master = "call sp_delete_package_master(?,?);";

		private static final String sp_get_all_package_info = "call sp_get_all_package_info(?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		public List<tw.com.aber.vo.PackageVO> getAllPackageInfo(String groupId, String packageIds) {
			PackageVO packageVO = null;

			List<tw.com.aber.vo.PackageVO> packageVOList = new ArrayList<tw.com.aber.vo.PackageVO>();
			List<tw.com.aber.vo.ProductPackageVO> detailList = null;
			tw.com.aber.vo.ProductPackageVO productPackageVO = null;
			tw.com.aber.vo.ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_all_package_info);

				pstmt.setString(1, groupId);
				pstmt.setString(2, packageIds);

				rs = pstmt.executeQuery();
				String bomId = "", bomId_now = "";

				while (rs.next()) {
					productVO = new tw.com.aber.vo.ProductVO();
					productVO.setC_product_id(rs.getString("itemSkuNo"));

					productPackageVO = new tw.com.aber.vo.ProductPackageVO();
					productPackageVO.setProductVO(productVO);
					productPackageVO.setQuantity(rs.getString("itemQuantity"));

					bomId_now = rs.getString("bomId");

					if ((!bomId_now.equals(bomId))) {
						detailList = new ArrayList<tw.com.aber.vo.ProductPackageVO>();

						packageVO = new PackageVO();
						packageVO.setC_package_id(rs.getString("bomSkuNo"));
						packageVO.setProductPackageList(detailList);
						packageVO.setBarcode(rs.getString("barcode"));
						packageVO.setPackage_name(rs.getString("package_name"));
						packageVOList.add(packageVO);

						bomId = bomId_now;
					}

					detailList.add(productPackageVO);
				}

			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			return packageVOList;
		}

		public void deletepackagesdetail(String package_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_package_detail);
				pstmt.setString(1, package_id);
				pstmt.executeQuery();
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public void updatepackagesdetail(String package_id, String package_id2, String product_id, String quantity,
				String package_desc) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_package_detail);
				pstmt.setString(1, package_id);
				pstmt.setString(2, package_id2);
				pstmt.setString(3, product_id);
				pstmt.setString(4, quantity);
				pstmt.setString(5, package_desc);
				pstmt.executeQuery();
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public void insertpackagesdetail(String package_id, String product_id, String quantity, String package_desc) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_package_detail);
				pstmt.setString(1, package_id);
				pstmt.setString(2, product_id);
				pstmt.setString(3, quantity);
				pstmt.setString(4, package_desc);
				pstmt.executeQuery();
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public ProductVO[] searchpackagesdetail(String package_id) {
			ProductVO[] packages = null;
			int i = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_package_detail);
				pstmt.setString(1, package_id);
				rs = pstmt.executeQuery();
				int count;
				if (rs.last()) {
					count = rs.getRow();
				} else {
					count = 0;
				}
				rs.beforeFirst();
				packages = new ProductVO[count];
				while (rs.next()) {
					packages[i] = new ProductVO();
					packages[i].product_id = rs.getString("product_id");
					packages[i].c_product_id = rs.getString("c_product_id");
					packages[i].product_name = rs.getString("product_name");
					packages[i].price = rs.getString("price");
					packages[i].photo = rs.getString("photo");
					packages[i].photo1 = rs.getString("photo1");
					packages[i].package_id = rs.getString("package_id");
					packages[i].parent_id = rs.getString("parent_id");
					packages[i].quantity = rs.getString("quantity");
					packages[i].package_desc = rs.getString("package_desc");
					
					i++;
				}
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			}
			return packages;
		}

		// 以上是package_detail四指令
		// 以下是package四指令
		public String deletepackages(String package_id, String user_id) {
			String result = "";
			
			Connection con = null;
			CallableStatement cs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_delete_package_master);
				cs.setString(1, package_id);
				cs.registerOutParameter(2, Types.VARCHAR);

				cs.execute();
				
				result = cs.getString(2);
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			} finally {
				if (cs != null) {
					try {
						cs.close();
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
			return result;
		}

		public String updatepackages(String package_id, String group_id, String c_package_id, String package_name,
				String supply_name, String price, String package_type, String barcode, String description,
				String user_id) {
			Connection con = null;
			CallableStatement cs = null;
			String result = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_update_package_master);
				
				cs.setString(1, package_id);
				cs.setString(2, group_id);
				cs.setString(3, c_package_id);
				cs.setString(4, package_name);
				cs.setString(5, package_type);
				cs.setString(6, price);
				cs.setString(7, barcode);
				cs.setString(8, description);
				cs.registerOutParameter(9, Types.VARCHAR);
				cs.execute();
				
				result = cs.getString(9);
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			} finally {
				if (cs != null) {
					try {
						cs.close();
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
			return result;
		}

		public String insertpackages(String group_id, String c_package_id, String package_name, String supply_name,
				String price, String package_type, String barcode, String description, String user_id) {
			Connection con = null;
			CallableStatement cs = null;
			String result = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_package_master);
				
				cs.setString(1, group_id);
				cs.setString(2, c_package_id);
				cs.setString(3, package_name);
				cs.setString(4, package_type);
				cs.setString(5, price);
				cs.setString(6, barcode);
				cs.setString(7, description);
				cs.registerOutParameter(8, Types.VARCHAR);
				cs.execute();
				
				result = cs.getString(8);
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				logger.error(e.toString());
			} finally {
				if (cs != null) {
					try {
						cs.close();
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
			return result;
		}

		public ProductVO[] searchpackages(String group_id, String word) {
			ProductVO[] packages = null;
			int i = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_package_master);
				pstmt.setString(1, group_id);
				pstmt.setString(2, word);
				pstmt.setString(3, word);
				rs = pstmt.executeQuery();
				int count;
				if (rs.last()) {
					count = rs.getRow();
				} else {
					count = 0;
				}
				rs.beforeFirst();
				packages = new ProductVO[count];
				while (rs.next()) {
					packages[i] = new ProductVO();
					packages[i].product_id = rs.getString("package_id");
					packages[i].group_id = rs.getString("group_id");
					packages[i].c_product_id = rs.getString("c_package_id") == null ? "" : rs.getString("c_package_id");
					packages[i].product_name = rs.getString("package_name");
					packages[i].type_id = rs.getString("package_spec");
					packages[i].price = rs.getString("amount");
					packages[i].description = rs.getString("description") == null ? "" : rs.getString("description");
					packages[i].barcode = rs.getString("barcode") == null ? "" : rs.getString("barcode");
					i++;
				}
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
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
			return packages;
		}
		
		public List<ProductVO> searchPackagesList(String group_id, String word) {
			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productBean = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_package_master);
				pstmt.setString(1, group_id);
				pstmt.setString(2, word);
				pstmt.setString(3, word);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					productBean = new ProductVO();
					productBean.setProduct_id(rs.getString("package_id"));
					productBean.setGroup_id(rs.getString("group_id"));
					productBean.setC_product_id(rs.getString("c_package_id") == null ? "" : rs.getString("c_package_id"));
					productBean.setProduct_name(rs.getString("package_name"));
					productBean.setType_id(rs.getString("package_spec"));
					productBean.setPrice(rs.getString("amount"));
					productBean.setDescription(rs.getString("description") == null ? "" : rs.getString("description"));
					productBean.setBarcode(rs.getString("barcode") == null ? "" : rs.getString("barcode"));
					
					list.add(productBean);
				}
			} catch (SQLException se) {
				logger.error("ERROR WITH: " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				logger.error("A database error occured. " + cnfe.getMessage());
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
