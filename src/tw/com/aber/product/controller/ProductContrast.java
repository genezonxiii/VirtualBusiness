package tw.com.aber.product.controller;

import java.io.IOException;
import java.math.BigDecimal;
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

import tw.com.aber.vo.DataTableBean;
import tw.com.aber.vo.PlatformVO;
import tw.com.aber.vo.ProductContrastVO;
import tw.com.aber.vo.ProductVO;

public class ProductContrast extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(ProductContrast.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		ProductContrastService productContrastService = null;
		ProductContrastVO productContrastVO = null;

		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();

		logger.debug("action: " + action);
		logger.debug("group_id: " + group_id);

		if ("insert".equals(action)) {
			try {
				String product_id = request.getParameter("product_id");
				String platform_id = request.getParameter("platform_id");
				String product_name_platform = request.getParameter("product_name_platform");
				String amountStr = request.getParameter("amt");
				String product_spec_platform = request.getParameter("product_spec_platform");
				String contrast_type = request.getParameter("contrast_type");

				BigDecimal amount = new BigDecimal(amountStr);
				
				ProductContrastVO paramVO = new ProductContrastVO();
				paramVO.setGroupId(group_id);
				paramVO.setProductId(product_id);
				paramVO.setPlatformId(platform_id);
				paramVO.setContrast_type(contrast_type);
				paramVO.setProductNamePlatform(product_name_platform);
				paramVO.setProduct_spec_platform(product_spec_platform);
				paramVO.setAmount(amount);

				logger.debug("product_id:" + product_id);
				logger.debug("platform_id:" + platform_id);
				logger.debug("contrast_type:" + contrast_type);
				logger.debug("product_name_platform:" + product_name_platform);
				logger.debug("product_spec_platform:" + product_spec_platform);
				logger.debug("amount:" + amount);

				productContrastService = new ProductContrastService();
				productContrastService.addProductContrast(paramVO);
				
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("update".equals(action)) {
			try {
				
				String contrast_id = request.getParameter("contrast_id");
				String product_name_platform = request.getParameter("product_name_platform");
				String amountStr = request.getParameter("amt");
				String product_spec_platform = request.getParameter("product_spec_platform");
				String contrast_type = request.getParameter("contrast_type");		
				String product_id = request.getParameter("product_id");	
				String platform_id = request.getParameter("platform_id");
				
				BigDecimal amount = new BigDecimal(amountStr);
				
				ProductContrastVO paramVO = new ProductContrastVO();
				paramVO.setContrastId(contrast_id);
				paramVO.setProductId(product_id);
				paramVO.setPlatformId(platform_id);
				paramVO.setContrast_type(contrast_type);
				paramVO.setProductNamePlatform(product_name_platform);
				paramVO.setProduct_spec_platform(product_spec_platform);
				paramVO.setAmount(amount);

				logger.debug("contrast_id:" + contrast_id);
				logger.debug("product_id:" + product_id);
				logger.debug("platform_id:" + platform_id);
				logger.debug("contrast_type:" + contrast_type);
				logger.debug("product_name_platform:" + product_name_platform);
				logger.debug("product_spec_platform:" + product_spec_platform);
				logger.debug("amount:" + amount);
				
				productContrastService = new ProductContrastService();
				productContrastService.updateProductContrast(paramVO);

				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("delete".equals(action)) {
			try {
				String contrastId = request.getParameter("contrast_id");
				logger.debug("contrast_id: " + contrastId);
				ProductContrastVO paramVO = new ProductContrastVO();
				paramVO.setContrastId(contrastId);

				productContrastService = new ProductContrastService();
				productContrastService.deleteProductContrast(paramVO);

				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("batchDelete".equals(action)) {
			try {
				String contrast_ids = request.getParameter("contrast_ids");
				String[] contrast_arr = contrast_ids.split(";");

				for (String id : contrast_arr) {
					
					logger.debug("batchDelete id: " + id);
					
					ProductContrastVO paramVO = new ProductContrastVO();
					paramVO.setContrastId(id);

					productContrastService = new ProductContrastService();
					productContrastService.deleteProductContrast(paramVO);
					
				}
				ProductContrastVO productContrast = new ProductContrastVO();
				productContrast.setMessage("success");
				
				String jsonStr = new Gson().toJson(productContrast);
				response.getWriter().write(jsonStr);
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("search-product-product-name".equals(action) | "dialog_product_product_name".equals(action)) {
			try {
				productContrastService = new ProductContrastService();
				List<ProductVO> list = productContrastService.getProductName(group_id);

				logger.debug("result getProductName list size: " + list.size());

				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("search-contrast-platform-name".equals(action) | "dialog_contrast_platform_name".equals(action)) {
			try {
				productContrastService = new ProductContrastService();
				List<ProductContrastVO> list = productContrastService.getProductContrastPlatformName(group_id);

				logger.debug("result getProductContrastPlatformName list size: " + list.size());

				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("search-platform-platform-name".equals(action) | "dialog_platform_platform_name".equals(action)) {
			try {
				productContrastService = new ProductContrastService();
				List<PlatformVO> list = productContrastService.getPlatformPlatformName(group_id);

				logger.debug("result getPlatformPlatformName list size: " + list.size());

				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("search-product-contrast".equals(action)) {
			try {

				String product_name = request.getParameter("product_name");
				String product_name_platform = request.getParameter("product_name_platform");
				String platform_name = request.getParameter("platform_name");
				String c_product_id_platform = request.getParameter("c_product_id_platform");

				logger.debug("product_name: " + product_name);
				logger.debug("product_name_platform: " + product_name_platform);
				logger.debug("platform_name: " + platform_name);
				logger.debug("c_product_id_platform: " + c_product_id_platform);


				productContrastService = new ProductContrastService();

				productContrastVO = new ProductContrastVO();
				productContrastVO.setProductName(product_name);
				productContrastVO.setProductNamePlatform(product_name_platform);
				productContrastVO.setPlatform_name(platform_name);
				productContrastVO.setC_product_id_platform(c_product_id_platform);

				List<ProductContrastVO> list = productContrastService.getProductContrast(productContrastVO, group_id);

				logger.debug("result getProductContrast list size: " + list.size());

				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("search-product-type-list".equals(action)) {
			String type = request.getParameter("type");
			productContrastService = new ProductContrastService();
			List<DataTableBean> list = productContrastService.getTypeList(type, group_id);

			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;
		}
	}

	/*************************** 處理業務邏輯 ****************************************/
	class ProductContrastService {
		private productContrast_interface dao;

		public ProductContrastService() {
			dao = new ProductContrastDAO();
		}

		public void addProductContrast(ProductContrastVO productContrastVO) {
			dao.insertDB(productContrastVO);
		}

		public void updateProductContrast(ProductContrastVO paramVO) {
			dao.updateDB(paramVO);
		}

		public void deleteProductContrast(ProductContrastVO paramVO) {
			dao.deleteDB(paramVO);
		}

		public List<ProductVO> getProductName(String group_id) {
			return dao.getProductNameDB(group_id);
		}

		public List<ProductContrastVO> getProductContrastPlatformName(String group_id) {
			return dao.getProductContrastPlatformNameDB(group_id);
		}

		public List<PlatformVO> getPlatformPlatformName(String group_id) {
			return dao.getPlatformPlatformNameDB(group_id);
		}

		public List<ProductContrastVO> getProductContrast(ProductContrastVO productContrastVO, String group_id) {
			return dao.getProductContrast(productContrastVO, group_id);
		}

		public List<DataTableBean> getTypeList(String type, String group_id) {

			return "PRD".equals(type) ? dao.getProductList(group_id) : dao.getPacgageList(group_id);
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface productContrast_interface {

		public void insertDB(ProductContrastVO paramVO);

		public void updateDB(ProductContrastVO paramVO);

		public void deleteDB(ProductContrastVO paramVO);

		public List<ProductVO> getProductNameDB(String group_id);

		public List<ProductContrastVO> getProductContrastPlatformNameDB(String group_id);

		public List<PlatformVO> getPlatformPlatformNameDB(String group_id);

		public List<ProductContrastVO> getProductContrast(ProductContrastVO productContrastVO, String group_id);

		public List<DataTableBean> getProductList(String group_id);

		public List<DataTableBean> getPacgageList(String group_id);
	}

	/*************************** 操作資料庫 ****************************************/
	class ProductContrastDAO implements productContrast_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		// 會使用到的Stored procedure
		private static final String sp_insert_product_contrast = "call sp_insert_product_contrast(?,?,?,?,?,?,?)";
		private static final String sp_update_product_contrast = "call sp_update_product_contrast(?,?,?,?,?,?,?)";
		private static final String sp_delete_product_contrast = "call sp_delete_product_contrast(?)";
		private static final String sp_get_product_product_name = "call sp_get_product_product_name(?)";
		private static final String sp_get_product_contrast_platform_name = "call sp_get_product_contrast_platform_name(?)";
		private static final String sp_get_platform_platform_name = "call sp_get_platform_platform_name(?)";
		private static final String sp_get_product_contrast = "call sp_get_product_contrast(?,?,?,?)";
		private static final String sp_get_product_product_list = "call sp_get_product_product_list(?)";
		private static final String sp_get_package_package_list = "call sp_get_package_package_list(?)";

		@Override
		public void insertDB(ProductContrastVO paramVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_contrast);

				pstmt.setString(1, paramVO.getGroupId());
				pstmt.setString(2, paramVO.getContrast_type());
				pstmt.setString(3, paramVO.getProductId());
				pstmt.setString(4, paramVO.getPlatformId());
				pstmt.setString(5, paramVO.getProductNamePlatform());
				pstmt.setString(6, paramVO.getProduct_spec_platform());
				pstmt.setBigDecimal(7, paramVO.getAmount());
				pstmt.executeUpdate();
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
		public void updateDB(ProductContrastVO paramVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_product_contrast);

				pstmt.setString(1, paramVO.getContrastId());
				pstmt.setString(2, paramVO.getContrast_type());
				pstmt.setString(3, paramVO.getProductId());
				pstmt.setString(4, paramVO.getPlatformId());
				pstmt.setString(5, paramVO.getProductNamePlatform());
				pstmt.setString(6, paramVO.getProduct_spec_platform());
				pstmt.setBigDecimal(7, paramVO.getAmount());
				pstmt.executeUpdate();
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
		public void deleteDB(ProductContrastVO paramVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_product_contrast);

				pstmt.setString(1, paramVO.getContrastId());
				pstmt.executeUpdate();
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
		public List<ProductVO> getProductNameDB(String group_id) {
			List<ProductVO> list = new ArrayList<ProductVO>();

			ProductVO productVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_product_name);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setProduct_name(null2str(rs.getString("product_name")));
					productVO.setProduct_id(null2str(rs.getString("product_id")));
					list.add(productVO); // Store the row in the list
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
		public List<ProductContrastVO> getProductContrastPlatformNameDB(String group_id) {
			List<ProductContrastVO> list = new ArrayList<ProductContrastVO>();

			ProductContrastVO productContrastVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_contrast_platform_name);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productContrastVO = new ProductContrastVO();
					productContrastVO.setProductNamePlatform(null2str(rs.getString("product_name_platform")));
					list.add(productContrastVO); // Store the row in the list
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
		public List<PlatformVO> getPlatformPlatformNameDB(String group_id) {
			List<PlatformVO> list = new ArrayList<PlatformVO>();

			PlatformVO platformVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_platform_platform_name);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					platformVO = new PlatformVO();
					platformVO.setPlatform_name(null2str(rs.getString("platform_name")));
					platformVO.setPlatform_id(null2str(rs.getString("platform_id")));
					list.add(platformVO); // Store the row in the list
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
		public List<ProductContrastVO> getProductContrast(ProductContrastVO productContrastVO, String group_id) {
			List<ProductContrastVO> list = new ArrayList<ProductContrastVO>();

			ProductContrastVO result = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_contrast);

				String product_name = null2str(productContrastVO.getProductName());
				String product_name_platform = null2str(productContrastVO.getProductNamePlatform());
				String platform_name = null2str(productContrastVO.getPlatform_name());
				
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				pstmt.setString(3, product_name_platform);
				pstmt.setString(4, platform_name);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					result = new ProductContrastVO();
					result.setProductName(null2str(rs.getString("product_name")));
					result.setProductNamePlatform(null2str(rs.getString("product_name_platform")));
					result.setPlatform_name(null2str(rs.getString("platform_name")));
					result.setAmount(rs.getBigDecimal("amount"));
					result.setContrastId(null2str(rs.getString("contrast_id")));
					result.setProduct_spec(null2str(rs.getString("product_spec")));
					result.setContrast_type(null2str(rs.getString("contrast_type")));
					result.setProduct_spec_platform(null2str(rs.getString("product_spec_platform")));
					result.setPlatformId(null2str(rs.getString("platform_id")));
					result.setProductId(null2str(rs.getString("product_id")));
					list.add(result); // Store the row in the list
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
		public List<DataTableBean> getProductList(String group_id) {
			List<DataTableBean> list = new ArrayList<DataTableBean>();

			DataTableBean result = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_product_list);

				pstmt.setString(1, group_id);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					result = new DataTableBean();
					result.setId(null2str(rs.getString("product_id")));
					result.setName(null2str(rs.getString("product_name")));
					result.setDesc(null2str(rs.getString("description")));
					list.add(result); // Store the row in the list
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
		public List<DataTableBean> getPacgageList(String group_id) {
			List<DataTableBean> list = new ArrayList<DataTableBean>();

			DataTableBean result = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_package_package_list);

				pstmt.setString(1, group_id);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					result = new DataTableBean();
					result.setId(null2str(rs.getString("package_id")));
					result.setName(null2str(rs.getString("package_name")));
					result.setDesc(null2str(rs.getString("package_spec")));
					list.add(result); // Store the row in the list
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

	public String null2str(Object object) {
		return object == null ? "" : object.toString();
	}

}
