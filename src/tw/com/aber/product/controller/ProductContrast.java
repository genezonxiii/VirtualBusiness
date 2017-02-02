package tw.com.aber.product.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

import tw.com.aber.vo.ProductContrastVO;

public class ProductContrast extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(ProductContrast.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		ProductContrastService productContrastService = null;
		String action = request.getParameter("action");
		
		if ("insert".equals(action)) {
			logger.debug("enter ProductContrast insert method");
			try {
				String groupId = request.getSession().getAttribute("group_id").toString();

				/*************************** 1.接收請求參數 **************************************/
				String productId = request.getParameter("product_id");
				String productName = request.getParameter("product_name");
				String platformId = request.getParameter("platform_id");
				String productNamePlatform = request.getParameter("product_name_platform");
				String amount = request.getParameter("amount");
				
				ProductContrastVO paramVO = new ProductContrastVO();
				paramVO.setGroupId(groupId);
				paramVO.setProductId(productId);
				paramVO.setProductName(productName);
				paramVO.setPlatformId(platformId);
				paramVO.setProductNamePlatform(productNamePlatform);
				paramVO.setAmount(amount);
				
				logger.debug("product_id:" + productId);
				logger.debug("product_name:" + productName);
				logger.debug("platform_id:" + platformId);
				logger.debug("product_name_platform:" + productNamePlatform);
				logger.debug("amount:" + amount);
				
				/*************************** 2.開始新增資料 ***************************************/
				productContrastService = new ProductContrastService();
				List<ProductContrastVO> list = productContrastService.addProductContrast(paramVO);

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				logger.debug("insert list: " + list.toString());
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("update".equals(action)) {
			logger.debug("enter ProductContrast update method");
			try {				
				/*************************** 1.接收請求參數 **************************************/
				String contrastId = request.getParameter("contrast_id");
				String productId = request.getParameter("product_id");
				String productName = request.getParameter("product_name");
				String platformId = request.getParameter("platform_id");
				String productNamePlatform = request.getParameter("product_name_platform");
				String amount = request.getParameter("amount");
				
				ProductContrastVO paramVO = new ProductContrastVO();
				paramVO.setContrastId(contrastId);
				paramVO.setProductId(productId);
				paramVO.setProductName(productName);
				paramVO.setPlatformId(platformId);
				paramVO.setProductNamePlatform(productNamePlatform);
				paramVO.setAmount(amount);
				
				productContrastService = new ProductContrastService();
				List<ProductContrastVO> list = productContrastService.updateProductContrast(paramVO);
				
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("delete".equals(action)) {
			try {
				/*************************** 1.接收請求參數 **************************************/
				String contrastId = request.getParameter("contrast_id");
				
				ProductContrastVO paramVO = new ProductContrastVO();
				paramVO.setContrastId(contrastId);
				
				productContrastService = new ProductContrastService();
				List<ProductContrastVO> list = productContrastService.deleteProductContrast(paramVO);
				
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*************************** 處理業務邏輯 ****************************************/
	class ProductContrastService {
		private productContrast_interface dao;

		public ProductContrastService() {
			dao = new ProductContrastDAO();
		}

		public List<ProductContrastVO> addProductContrast( ProductContrastVO paramVO ) {
			dao.insertDB(paramVO);
			List<ProductContrastVO> list = new ArrayList<ProductContrastVO>();
			list.add(paramVO);
			return list;
		}

		public List<ProductContrastVO> updateProductContrast( ProductContrastVO paramVO ) {
			dao.updateDB(paramVO);
			List<ProductContrastVO> list = new ArrayList<ProductContrastVO>();
			list.add(paramVO);
			return list;
		}

		public List<ProductContrastVO> deleteProductContrast( ProductContrastVO paramVO ) {
			dao.deleteDB(paramVO);
			List<ProductContrastVO> list = new ArrayList<ProductContrastVO>();
			list.add(paramVO);
			return list;
		}
	}
	
	/*************************** 制定規章方法 ****************************************/
	interface productContrast_interface {

		public void insertDB(ProductContrastVO paramVO);

		public void updateDB(ProductContrastVO paramVO);

		public void deleteDB(ProductContrastVO paramVO);
	}
	
	/*************************** 操作資料庫 ****************************************/
	class ProductContrastDAO implements productContrast_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		// 會使用到的Stored procedure
		private static final String sp_insert_product_contrast = "call sp_insert_product_contrast(?,?)";
		private static final String sp_update_product_contrast = "call sp_update_product_contrast(?,?)";
		private static final String sp_delete_product_contrast = "call sp_delete_product_contrast(?,?)";
		
		@Override
		public void insertDB(ProductContrastVO paramVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_contrast);
				
				pstmt.setString(1, paramVO.getGroupId());
				pstmt.setString(2, paramVO.getProductId());
				pstmt.setString(3, paramVO.getProductName());
				pstmt.setString(4, paramVO.getPlatformId());
				pstmt.setString(5, paramVO.getProductNamePlatform());
				pstmt.setString(6, paramVO.getAmount());
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
				pstmt.setString(2, paramVO.getGroupId());
				pstmt.setString(3, paramVO.getProductId());
				pstmt.setString(4, paramVO.getProductName());
				pstmt.setString(5, paramVO.getPlatformId());
				pstmt.setString(6, paramVO.getProductNamePlatform());
				pstmt.setString(7, paramVO.getAmount());
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
	}
}
