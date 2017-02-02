
package tw.com.aber.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tw.com.aber.vo.SaleReportVO;

public class salereturnreport extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("group_id") == null) {
			System.out.println("no_session");
			return;
		}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		SalereportService salereportService = null;
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id = (group_id == null || group_id.length() < 3) ? "UNKNOWN" : group_id;
		String time1 = request.getParameter("time1");
		time1 = (time1.length() < 3) ? "1999-12-31" : time1;
		String time2 = request.getParameter("time2");
		time2 = (time2.length() < 3) ? "2300-12-31" : time2;
		try {
			java.sql.Date from_date = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time1).getTime());
			java.sql.Date till_date = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time2).getTime());
			salereportService = new SalereportService();
			List<SaleReportVO> list = salereportService.getSearhDB(group_id, from_date, till_date);

			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;
		} catch (Exception e) {
			System.out.println("Error with time parse. :" + e);
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface Salereport_interface {

		public List<SaleReportVO> searhDB(String group_id, java.sql.Date from_date, java.sql.Date till_date);

	}

	/*************************** 處理業務邏輯 ****************************************/
	class SalereportService {
		private Salereport_interface dao;

		public SalereportService() {
			dao = new SalereportDAO();
		}

		public List<SaleReportVO> getSearhDB(String group_id, java.sql.Date from_date, java.sql.Date till_date) {
			return dao.searhDB(group_id, from_date, till_date);
		}

	}

	/*************************** 操作資料庫 ****************************************/
	class SalereportDAO implements Salereport_interface {
		// 會使用到的Stored procedure
		private static final String sp_select_sale_return_byreturndate = "call sp_select_sale_return_byreturndate(?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		public List<SaleReportVO> searhDB(String group_id, java.sql.Date from_date, java.sql.Date till_date) {
			List<SaleReportVO> list = new ArrayList<SaleReportVO>();
			SaleReportVO salereportVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_return_byreturndate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, from_date);
				pstmt.setDate(3, till_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					salereportVO = new SaleReportVO();
					salereportVO.setSale_id(rs.getString("sale_id"));
					salereportVO.setSeq_no(rs.getString("seq_no"));
					salereportVO.setGroup_id(rs.getString("group_id"));
					salereportVO.setOrder_no(rs.getString("order_no"));
					salereportVO.setUser_id(rs.getString("user_id"));
					salereportVO.setProduct_id(rs.getString("product_id"));
					salereportVO.setProduct_name(rs.getString("product_name"));
					salereportVO.setC_product_id(rs.getString("c_product_id"));
					salereportVO.setCustomer_id(rs.getString("customer_id"));
					salereportVO.setName(rs.getString("name"));
					salereportVO.setQuantity(rs.getInt("quantity"));
					salereportVO.setPrice(rs.getFloat("price"));
					salereportVO.setInvoice(rs.getString("invoice"));
					salereportVO.setInvoice_date(rs.getDate("invoice_date"));
					salereportVO.setTrans_list_date(rs.getDate("trans_list_date"));
					salereportVO.setDis_date(rs.getDate("dis_date"));
					salereportVO.setMemo(rs.getString("memo"));
					salereportVO.setSale_date(rs.getDate("sale_date"));
					salereportVO.setOrder_source(rs.getString("order_source"));
					salereportVO.setReturn_date(rs.getDate("return_date"));
					list.add(salereportVO);
				}
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}
	}
}
