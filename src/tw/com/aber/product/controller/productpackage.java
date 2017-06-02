
package tw.com.aber.product.controller;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.sf.vo.Response;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.PackageVO;
import tw.com.aber.vo.ShipDetail;
import tw.com.aber.vo.ShipVO;

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
			ProductVO[] parents = dao.searchpackages(group_id, word);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
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

			dao.insertpackages(group_id, c_package_id, package_name, supply_name, price, package_type, barcode,
					description, user_id);
			ProductVO[] parents = dao.searchpackages(group_id, "");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
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

			dao.updatepackages(package_id, group_id, c_package_id, package_name, supply_name, price, package_type,
					barcode, description, user_id);
			ProductVO[] parents = dao.searchpackages(group_id, "");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		} else if ("delete".equals(action)) {
			String package_id = request.getParameter("package_id");
			logger.debug("Package ID:" + package_id);
			logger.debug("User ID:" + user_id);
			dao.deletepackages(package_id, user_id);
			ProductVO[] parents = dao.searchpackages(group_id, "");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
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
			//測試假資料
//			packageIds = "'f45d98e3-3ef3-4ff5-8b1d-d71f8864327d','c550aeda-84c4-421a-b41d-1a5c32c99835'";
			List<tw.com.aber.vo.PackageVO> packageVOList = dao.getAllPackageInfo(group_id, packageIds);
			//logger.debug(new Gson().toJson(packageVOList));
			//response.getWriter().write(new Gson().toJson(packageVOList));
			
			ValueService valueService = util.getValueService(request, response);
			SfApi sfApi = new SfApi();
			String reqXml = sfApi.genBomService(packageVOList, valueService);
			String resXml = sfApi.sendXML(reqXml);
			logger.debug(resXml);
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
		private static final String sp_insert_package_master = "call sp_insert_package_master(?,?,?,?,?,?,?);";
		private static final String sp_update_package_master = "call sp_update_package_master(?,?,?,?,?,?,?);";
		private static final String sp_delete_package_master = "call sp_delete_package_master(?);";

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
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
					// packages[i].group_id = rs.getString("group_id");
					packages[i].c_product_id = rs.getString("c_product_id");
					packages[i].product_name = rs.getString("product_name");
					// packages[i].supply_id = rs.getString("supply_id");
					// packages[i].supply_name = rs.getString("supply_name");
					// packages[i].type_id = rs.getString("type_id");
					// packages[i].unit_id = rs.getString("unit_id");
					// packages[i].cost = rs.getString("cost");
					packages[i].price = rs.getString("price");
					// packages[i].keep_stock = rs.getString("keep_stock");
					packages[i].photo = rs.getString("photo");
					packages[i].photo1 = rs.getString("photo1");
					// packages[i].description = rs.getString("description");
					// packages[i].barcode = rs.getString("barcode");
					// packages[i].ispackage = rs.getString("package");

					packages[i].package_id = rs.getString("package_id");
					packages[i].parent_id = rs.getString("parent_id");
					packages[i].quantity = rs.getString("quantity");
					packages[i].package_desc = rs.getString("package_desc");
					i++;
				}
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return packages;
		}

		// 以上是package_detail四指令
		// 以下是package四指令
		public void deletepackages(String package_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_package_master);
				pstmt.setString(1, package_id);

				pstmt.executeQuery();
				//
				// con = null;
				// pstmt = null;
				// con = DriverManager.getConnection(dbURL, dbUserName,
				// dbPassword);
				// pstmt = con.prepareStatement(sp_delete_package2);
				// pstmt.setString(1, package_id);
				// pstmt.executeQuery();
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public void updatepackages(String package_id, String group_id, String c_package_id, String package_name,
				String supply_name, String price, String package_type, String barcode, String description,
				String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_package_master);
				pstmt.setString(1, package_id);
				pstmt.setString(2, c_package_id);
				pstmt.setString(3, package_name);
				pstmt.setString(4, package_type);
				pstmt.setString(5, price);
				pstmt.setString(6, barcode);
				pstmt.setString(7, description);
				pstmt.executeQuery();
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public void insertpackages(String group_id, String c_package_id, String package_name, String supply_name,
				String price, String package_type, String barcode, String description, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_package_master);
				pstmt.setString(1, group_id);
				pstmt.setString(2, c_package_id);
				pstmt.setString(3, package_name);
				pstmt.setString(4, package_type);
				pstmt.setString(5, price);
				pstmt.setString(6, barcode);
				pstmt.setString(7, description);
				pstmt.executeQuery();
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return;
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
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return packages;
		}
	}
}
