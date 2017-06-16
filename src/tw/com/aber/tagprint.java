package tw.com.aber;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


public class tagprint extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		TagPrintService tagprintService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
//		String user_id = request.getSession().getAttribute("user_id").toString();
		// 商品搜尋
		if ("search".equals(action)) {
			try {
				/*************************** 1.接收請求參數-格式檢查 ****************************************/
				String product_name = request.getParameter("product_name");
				String c_product_id = request.getParameter("c_product_id");				
				/*************************** 2.開始查詢資料 ****************************************/
				// 查詢所有商品
				if ("".equals(c_product_id) && "".equals(product_name)) {
					tagprintService = new TagPrintService();
					List<ProductVO> list = tagprintService.getTagInfoByName(group_id, product_name);
					ProductVO productVO = new ProductVO();
					productVO.setMessage("驗證通過");
					list.add(productVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 商品名稱查詢
				if ("".equals(c_product_id) && !"".equals(product_name)) {
					tagprintService = new TagPrintService();
					List<ProductVO> list = tagprintService.getTagInfoByName(group_id, product_name);
					ProductVO productVO = new ProductVO();
					productVO.setMessage("驗證通過");
					list.add(productVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 商品ID查詢
				if (!"".equals(c_product_id) && "".equals(product_name)) {
					tagprintService = new TagPrintService();
					List<ProductVO> list = tagprintService.getTagInfoById(group_id, c_product_id);
					ProductVO productVO = new ProductVO();
					productVO.setMessage("驗證通過");
					list.add(productVO);
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
		// 商品ID條件查詢的auto complete
		if ("autocomplete_id".equals(action)) {
			String term = request.getParameter("term");
			tagprintService = new TagPrintService();
			List<ProductVO> list = tagprintService.getProductById(group_id, term);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;// 程式中斷
		}
		// 商品名稱條件查詢的auto complete
		if ("autocomplete_name".equals(action)) {
			String term = request.getParameter("term");
			tagprintService = new TagPrintService();
			List<ProductVO> list = tagprintService.getProductByName(group_id, term);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;// 程式中斷
		}
	}

	/*************************** 自訂方法 ****************************************/
	// 處理傳過來的日期格式
	public int DateConversionToDigital(String Date) {

		StringBuffer str = new StringBuffer();
		String[] dateArray = Date.split("-");
		for (String i : dateArray) {
			str.append(i);
		}
		return Integer.parseInt(str.toString());
	}

	// 獲得格式過的今年金月今日
	public String getThisYearMonthDate() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String date = String.valueOf(cal.get(Calendar.DATE));
		return year + formatTime(month) + formatTime(date);
	}

	// 處理個位數月份以及日
	private String formatTime(String str) {
		int counter = 0;
		for (int i = str.length() - 1; i >= 0; i--) {
			counter++;
		}
		if (counter == 1) {
			str = "0" + str;
		}
		return str;
	}

	// 格式化單號，不足四位補位
	private String formatSeqNo(int str) {
		String seqNo = String.valueOf(str);
		StringBuffer buf = new StringBuffer();
		buf.append(seqNo);
		int counter = 0;
		for (int i = 0; i < seqNo.length(); i++) {
			counter++;
		}
		if (counter < 5) {
			for (int j = 0; j < (4 - counter); j++) {
				buf.insert(0, "0");
			}
		}
		return buf.toString();
	}

	public String getGenerateSeqNo(String str) {
		str = str.substring(str.length() - 4);
		return getThisYearMonthDate() + formatSeqNo((Integer.valueOf(str) + 1));
	}

	class ProductVO implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String barcode;
		private String c_product_id;
		private String product_name;
		@SuppressWarnings("unused")
		private String message;// 此參數用來存放錯誤訊息

		public void setMessage(String message) {
			this.message = message;
		}

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public String getProduct_id() {
			return c_product_id;
		}

		public void setProduct_id(String c_product_id) {
			this.c_product_id = c_product_id;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
	}

	/*************************** 處理業務邏輯 ****************************************/
	class TagPrintService {
		private tagprint_interface dao;

		public TagPrintService() {
			dao = new TagPrintDAO();
		}
		
		public List<ProductVO> getTagInfoById(String group_id, String c_product_id) {
			return dao.getTagInfoById(group_id, c_product_id);
		}

		public List<ProductVO> getTagInfoByName(String group_id, String product_name) {
			return dao.getTagInfoByName(group_id, product_name);
		}
		
		public List<ProductVO> getProductByName (String group_id, String product_name) {
			return dao.getProductByName(group_id, product_name);
		}
		
		public List<ProductVO> getProductById (String group_id, String c_product_id) {
			return dao.getProductById(group_id, c_product_id);
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface tagprint_interface {

		public List<ProductVO> getTagInfoById(String group_id, String c_product_id);
		
		public List<ProductVO> getTagInfoByName(String group_id, String product_name);

		public List<ProductVO> getProductByName(String group_id, String product_name);

		public List<ProductVO> getProductById(String group_id, String c_product_id);		
	}

	/*************************** 操作資料庫 ****************************************/
	class TagPrintDAO implements tagprint_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_barcodebycid = "call sp_get_barcodebycid (?,?)";
		private static final String sp_get_barcodebyname = "call sp_get_barcodebyname (?,?)";
		private static final String sp_get_product_byid = "call sp_get_product_byid (?,?)";
		private static final String sp_get_product_byname = "call sp_get_product_byname (?,?)";
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";;
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public List<ProductVO> getTagInfoById(String group_id, String c_product_id) {
			// TODO Auto-generated method stub
			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_barcodebycid);
				pstmt.setString(1, group_id);
				pstmt.setString(2, c_product_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setBarcode(rs.getString("barcode"));
					productVO.setProduct_name(rs.getString("product_name"));
					list.add(productVO);
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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
		public List<ProductVO> getTagInfoByName(String group_id, String product_name) {
			// TODO Auto-generated method stub
			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_barcodebyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setBarcode(rs.getString("barcode"));
					productVO.setProduct_name(rs.getString("product_name"));
					list.add(productVO);
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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
		public List<ProductVO> getProductById(String group_id, String c_product_id) {
			// TODO Auto-generated method stub
			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_byid);
				pstmt.setString(1, group_id);
				pstmt.setString(2, c_product_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setProduct_id(rs.getString("c_product_id"));
					list.add(productVO);
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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
		public List<ProductVO> getProductByName(String group_id, String product_name) {
			// TODO Auto-generated method stub
			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setProduct_name(rs.getString("product_name"));
					list.add(productVO);
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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