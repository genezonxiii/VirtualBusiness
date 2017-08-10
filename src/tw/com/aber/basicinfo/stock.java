package tw.com.aber.basicinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import tw.com.aber.vo.StockVO;

public class stock extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(stock.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		StockService stockService = null;
		String action = request.getParameter("action");
		
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		logger.debug("group_id: "+group_id);
		logger.debug("user_id: "+user_id);
		logger.debug("action: "+action);
		
		if ("bar_code_search".equals(action)) {
			String barcode = request.getParameter("barcode");
			stockService = new StockService();
			// stockService.barcode_search(group_id,barcode);
			response.getWriter().write(stockService.barcode_search(group_id, barcode));
			return;
		}
		
		if ("report".equals(action)){
			String kind = request.getParameter("kind");
			
			logger.debug("kind: "+kind);

			String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
					+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
			String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

			
			String reportSourcePath = ("" + this.getClass().getResource("/")).substring(5)
					.replace("VirtualBusiness/WEB-INF/classes/", "VirtualBusiness/WEB-INF/");
			
			String reportGeneratePath = getServletConfig().getServletContext().getInitParameter("uploadpath") + "/report";
			new File(reportGeneratePath).mkdir();
			
			
			String reportName = "rptStockNew";
			
			String jrxmlFileName = reportSourcePath + "/" + reportName + ".jrxml";
			String jasperFileName = reportGeneratePath + "/" + reportName + ".jasper";
			String pdfFileName = reportGeneratePath + "/" + reportName + ".pdf";
			String xlsFileName = reportGeneratePath + "/" + reportName + ".xls";

			try {
				JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
		
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				HashMap<String, Object> hm = null;
				
				hm = new HashMap<String, Object>();
				hm.put("p_group_id", request.getSession().getAttribute("group_id"));
				
				JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
				JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
				
				JRXlsExporter exporter = new JRXlsExporter();
		        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jprint);
		        exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, xlsFileName);

		        exporter.exportReport();

		        File file = null;
				if (kind.equals("pdf")) {
					response.setContentType("APPLICATION/PDF");
					String disHeader = "inline;Filename=\"" + reportName + ".pdf" + "\"";
					response.setHeader("Content-Disposition", disHeader);
					file = new File(pdfFileName);
				} else if (kind.equals("xls")) {
					response.setContentType("application/vns.ms-excel");
					String disHeader = "inline;Filename=\"" + reportName + ".xls" + "\"";
					response.setHeader("Content-Disposition", disHeader);
					file = new File(xlsFileName);
				}
				
				
				FileInputStream fileIn = new FileInputStream(file);
				ServletOutputStream out = response.getOutputStream();
				byte[] outputByte = new byte[4096];
				while (fileIn.read(outputByte, 0, 4096) != -1) {
					out.write(outputByte, 0, 4096);
				}
		
				fileIn.close();
				out.flush();
				out.close();
			
			} catch (JRException e) {
				logger.error("JRException:" + e.getMessage());
			} catch (SQLException e) {
				logger.error("SQLException:" + e.getMessage());
			} catch (ClassNotFoundException e) {
				logger.error("ClassNotFoundException:" + e.getMessage());
			}
		}
		if ("searh".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ****************************************/
				String product_name = request.getParameter("product_name");
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (product_name == null || (product_name.trim()).length() == 0) {
					stockService = new StockService();
					List<StockVO> list = stockService.searhAllDB(group_id);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (product_name != null) {
					stockService = new StockService();
					List<StockVO> list = stockService.searhstock_byname(group_id, product_name);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;
				}
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
				int quantity = Integer.valueOf(request.getParameter("quantity"));
				String memo = request.getParameter("memo");
				String stock_id = request.getParameter("stock_id");
				String product_id = request.getParameter("product_id");
				
				logger.debug("quantity: "+quantity);
				logger.debug("memo: "+memo);
				logger.debug("stock_id: "+stock_id);
				logger.debug("product_id: "+product_id);
				
				/***************************
				 * 2.開始修改資料
				 ***************************************/
				stockService = new StockService();
				StockVO bean = stockService.updateStock(stock_id, group_id, user_id, product_id, quantity, memo);
				/***************************
				 * 3.修改完成,準備轉交(Send the Success view)
				 ***********/
				stockService = new StockService();
				List<StockVO> list = stockService.searhAllDB(group_id);
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
	interface Stock_interface {

		public void updateDB(StockVO stockVO);

		public List<StockVO> searhAllDB(String group_id);

		public List<StockVO> searhstock_byname(String group_id, String product_name);

		public String barcode_search(String group_id, String bar_code);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class StockService {
		private Stock_interface dao;

		public StockService() {
			dao = new StockDAO();
		}

		public StockVO updateStock(String stock_id, String group_id, String user_id, String product_id, int quantity,
				String memo) {
			StockVO stockVO = new StockVO();
			stockVO.setStock_id(stock_id);
			stockVO.setGroup_id(group_id);
			stockVO.setUser_id(user_id);
			stockVO.setProduct_id(product_id);
			stockVO.setQuantity(quantity);
			stockVO.setMemo(memo);
			dao.updateDB(stockVO);
			return stockVO;
		}

		public List<StockVO> searhAllDB(String group_id) {
			return dao.searhAllDB(group_id);
		}

		public List<StockVO> searhstock_byname(String group_id, String product_name) {
			return dao.searhstock_byname(group_id, product_name);
		}

		public String barcode_search(String group_id, String bar_code) {
			return dao.barcode_search(group_id, bar_code);
		}

	}

	/*************************** 操作資料庫 ****************************************/
	class StockDAO implements Stock_interface {
		// 會使用到的Stored procedure
		private static final String sp_selectall_stock = "call sp_selectall_stock (?)";
		private static final String sp_update_stock = "call sp_update_stock (?,?,?,?,?,?)";
		private static final String sp_select_stock_bybarcode = "call sp_select_stock_bybarcode (?,?)";
		private static final String sp_select_stock_byname = "call sp_select_stock_byname (?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void updateDB(StockVO stockVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_stock);
				pstmt.setString(1, stockVO.getStock_id());
				pstmt.setString(2, stockVO.getGroup_id());
				pstmt.setString(3, stockVO.getUser_id());
				pstmt.setString(4, stockVO.getProduct_id());
				pstmt.setInt(5, stockVO.getQuantity());
				pstmt.setString(6, stockVO.getMemo());
				pstmt.executeUpdate();
				// System.out.println(
				// stockVO.getStock_id()+stockVO.getQuantity()+stockVO.getMemo());
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
		public List<StockVO> searhAllDB(String group_id) {

			List<StockVO> list = new ArrayList<StockVO>();
			StockVO stockVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_stock);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stockVO = new StockVO();
					stockVO.setStock_id(rs.getString("stock_id"));
					stockVO.setGroup_id(rs.getString("group_id"));
					stockVO.setUser_id(rs.getString("user_id"));
					stockVO.setProduct_id(rs.getString("product_id"));
					stockVO.setQuantity(rs.getInt("quantity"));
					stockVO.setMemo(rs.getString("memo"));
					stockVO.setProduct_name(rs.getString("product_name"));
					stockVO.setKeep_stock(rs.getString("keep_stock"));

					list.add(stockVO); // Store the row in the list
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

		public List<StockVO> searhstock_byname(String group_id, String product_name) {

			List<StockVO> list = new ArrayList<StockVO>();
			StockVO stockVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_stock_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stockVO = new StockVO();
					stockVO.setStock_id(rs.getString("stock_id"));
					stockVO.setGroup_id(rs.getString("group_id"));
					stockVO.setUser_id(rs.getString("user_id"));
					stockVO.setProduct_id(rs.getString("product_id"));
					stockVO.setQuantity(rs.getInt("quantity"));
					stockVO.setMemo(rs.getString("memo"));
					stockVO.setProduct_name(rs.getString("product_name"));
					stockVO.setKeep_stock(rs.getString("keep_stock"));
					list.add(stockVO); // Store the row in the list
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

		public class Barcode_supply {
			public String stock_id;
			public String group_id;
			public String user_id;
			public String product_id;
			public String quantity;
			public String memo;
			public String product_name;
		}

		public String barcode_search(String group_id, String bar_code) {
			// List<Barcode_supply> list = new ArrayList<Barcode_supply>();
			// Barcode_supply barcode_supply = null;
			List<StockVO> list = new ArrayList<StockVO>();
			StockVO stockVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_stock_bybarcode);
				pstmt.setString(1, group_id);
				pstmt.setString(2, bar_code);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stockVO = new StockVO();
					stockVO.setStock_id(rs.getString("stock_id"));
					stockVO.setGroup_id(rs.getString("group_id"));
					stockVO.setUser_id(rs.getString("user_id"));
					stockVO.setProduct_id(rs.getString("product_id"));
					stockVO.setQuantity(rs.getInt("quantity"));
					stockVO.setMemo(rs.getString("memo"));
					stockVO.setProduct_name(rs.getString("product_name"));
					stockVO.setKeep_stock(rs.getString("keep_stock"));

					list.add(stockVO);
				}
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				// System.out.println(jsonStrList);
				return jsonStrList;
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
		}

	}

}
