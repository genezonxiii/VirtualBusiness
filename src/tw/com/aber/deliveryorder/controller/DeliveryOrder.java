package tw.com.aber.deliveryorder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.vo.SaleVO;

public class DeliveryOrder extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(DeliveryOrder.class);
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		if ("search".equals(action)) {
			String group_id = request.getSession().getAttribute("group_id").toString();
			String start = request.getParameter("startTime");
			String end = request.getParameter("endTime");

			String serviceStr = getServletConfig().getServletContext().getInitParameter("pythonwebservice")
					+ "/ship/groupid=" + new String(Base64.encodeBase64String(group_id.getBytes())) + "&strdate="
					+ new String(Base64.encodeBase64String(start.getBytes())) + "&enddate="
					+ new String(Base64.encodeBase64String(end.getBytes()));
			logger.debug("ServiceStr: " + serviceStr);
			HttpClient client = new HttpClient();
			HttpMethod method = null;
			StringBuffer stringBuffer = null;
			try {
				method = new GetMethod(serviceStr);
				client.executeMethod(method);

				InputStream inputStream = method.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				stringBuffer = new StringBuffer();

				String str = "";
				while ((str = br.readLine()) != null) {
					str = str.replace("null", "\"\"");
					stringBuffer.append(str);
				}

			} catch (Exception e) {
				logger.error("WebService Error for:" + e);
			} finally {
				logger.debug(stringBuffer.toString());
				response.getWriter().write(stringBuffer.toString());
				method.releaseConnection();
			}
			return;
		}
	}

	class DeliveryOrderService {
		private DeliveryOrder_interface dao;

		public DeliveryOrderService() {
			dao = new DeliveryOrderDao();
		}

		public List<SaleVO> selectSaleByDisDate(String group_id, Date startTime, Date endTime) {
			return dao.selectSaleByDisDate(group_id, startTime, endTime);
		}
	}

	interface DeliveryOrder_interface {
		public List<SaleVO> selectSaleByDisDate(String group_id, Date startTime, Date endTime);
	}

	class DeliveryOrderDao implements DeliveryOrder_interface {
		// For Connection
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		// Stored Procedure
		private static final String sp_select_sale_bydisdate = "call sp_select_sale_bydisdate(?,?,?)";

		@Override
		public List<SaleVO> selectSaleByDisDate(String group_id, Date startTime, Date endTime) {
			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				pstmt = con.prepareStatement(sp_select_sale_bydisdate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, startTime);
				pstmt.setDate(3, endTime);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSale_id(rs.getString("sale_id"));
					saleVO.setSeq_no(rs.getString("seq_no"));
					saleVO.setGroup_id(rs.getString("group_id"));
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setUser_id(rs.getString("user_id"));
					saleVO.setProduct_id(rs.getString("product_id"));
					saleVO.setProduct_name(rs.getString("product_name"));
					saleVO.setC_product_id(rs.getString("c_product_id"));
					saleVO.setCustomer_id(rs.getString("customer_id"));
					saleVO.setName(rs.getString("name"));
					saleVO.setQuantity(rs.getInt("quantity"));
					saleVO.setPrice(rs.getFloat("price"));
					saleVO.setInvoice(rs.getString("invoice"));
					saleVO.setInvoice_date(rs.getDate("invoice_date"));
					saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleVO.setDis_date(rs.getDate("dis_date"));
					saleVO.setMemo(rs.getString("memo"));
					saleVO.setSale_date(rs.getDate("sale_date"));
					saleVO.setOrder_source(rs.getString("order_source"));
					saleVO.setReturn_date(rs.getDate("return_date"));
					saleVO.setIsreturn(rs.getBoolean("isreturn"));
					saleVO.setDeliveryway(rs.getString("deliveryway"));
					list.add(saleVO);
				}
			} catch (SQLException se) {
				se.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}

	}
}