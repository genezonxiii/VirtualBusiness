package tw.com.aber.basicinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.CustomerVO;
import tw.com.aber.vo.SaleVO;

public class customer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(customer.class);

	String err = "";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		CustomerService customerService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("search".equals(action)) {
			try {
				/*************************** 開始查詢資料 ****************************************/
				customerService = new CustomerService();
				List<CustomerVO> list = customerService.getAllCustomer(group_id);

				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				
				if (err.length() > 3) {
					response.getWriter().write(err);
				} else {
					response.getWriter().write(jsonStrList);
				}
				return;// 程式中斷
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("getCustomerVOByName".equals(action)) {
			try {
				/*************************** 開始查詢資料 ****************************************/
				String custome_name = request.getParameter("custome_name");
				customerService = new CustomerService();
				CustomerVO customerVO = new CustomerVO();
				customerVO.setName(custome_name);
				List<CustomerVO> list = customerService.getCustomerVOByName(group_id, customerVO);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				if(err.length()>3){
					response.getWriter().write(err);
				}else{
					response.getWriter().write(jsonStrList);
				}
				return;// 程式中斷				
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("insert".equals(action)) {
			try {
				String name = request.getParameter("name");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String mobile = request.getParameter("mobile");
				String email = request.getParameter("email");
				String post = request.getParameter("post");
				String customerClass = request.getParameter("class");
				String memo = request.getParameter("memo");
				
				CustomerVO customerVO = new CustomerVO();
				customerVO.setName(name);
				customerVO.setAddress(address);
				customerVO.setPhone(phone);
				customerVO.setMobile(mobile);
				customerVO.setEmail(email);
				customerVO.setPost(post);
				customerVO.setCustomerClass(customerClass);
				customerVO.setMemo(memo);

				customerService = new CustomerService();
				CustomerVO encodeCustomerData = customerService.getEncodeData(name, address, phone, mobile);
				customerService.addCustomer(group_id, encodeCustomerData.getName(), encodeCustomerData.getAddress(),
						encodeCustomerData.getPhone(), encodeCustomerData.getMobile(), email, post, customerClass, memo,
						user_id,customerVO);
				List<CustomerVO> list = customerService.getAllCustomer(group_id);
				Gson gson = new Gson();
				String jsonList = gson.toJson(list);
				response.getWriter().write(jsonList);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}
		if ("update".equals(action)) {
			try {
				String customer_id = request.getParameter("customer_id");
				String name = request.getParameter("name");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String mobile = request.getParameter("mobile");
				String email = request.getParameter("email");
				String post = request.getParameter("post");
				String customerClass = request.getParameter("class");
				String memo = request.getParameter("memo");

				customerService = new CustomerService();
				CustomerVO customerVO = new CustomerVO();
				customerVO.setCustomer_id(customer_id);
				customerVO.setName(name);
				customerVO.setAddress(address);
				customerVO.setPhone(phone);
				customerVO.setMobile(mobile);
				customerVO.setEmail(email);
				customerVO.setPost(post);
				customerVO.setCustomerClass(customerClass);
				customerVO.setMemo(memo);
				
				CustomerVO encodeCustomerData = customerService.getEncodeData(name, address, phone, mobile);
				customerService.updateCustomer(customer_id, group_id, encodeCustomerData.getName(),
						encodeCustomerData.getAddress(), encodeCustomerData.getPhone(), encodeCustomerData.getMobile(),
						email, post, customerClass, memo, user_id,customerVO);
				List<CustomerVO> list = customerService.getAllCustomer(group_id);
				Gson gson = new Gson();
				String jsonList = gson.toJson(list);
				response.getWriter().write(jsonList);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}
		if ("delete".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String customer_id = request.getParameter("customer_id");
				/***************************
				 * 2.開始刪除資料
				 ***************************************/
				customerService = new CustomerService();
				customerService.deleteCustomer(customer_id, user_id);
				/***************************
				 * 3.刪除完成,準備轉交(Send the Success view)
				 ***********/
				customerService = new CustomerService();
				List<CustomerVO> salelist = customerService.getAllCustomer(group_id);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(salelist);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if("transactionRecord".equals(action)){
			String customer_id = request.getParameter("customer_id");
			customerService = new CustomerService();
			List<SaleVO> salelist = customerService.getTransactionRecord(group_id,customer_id);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String jsonStrList = gson.toJson(salelist);
			response.getWriter().write(jsonStrList);
			/*************************** 其他可能的錯誤處理 **********************************/
		}
	}

	interface Customer_interface {
		public List<CustomerVO> searchAllDB(String group_id);

		public void deleteCustomer(String customer_id, String user_id);

		public void insertDB(CustomerVO customerVO, String user_id,CustomerVO clearCustomerVO);

		public void updateDB(CustomerVO customerVO, String user_id,CustomerVO clearCustomerVO);
		
		public List<CustomerVO> getCustomerVOByName(String group_id,CustomerVO customerVO);
		
		public List<SaleVO> getTransactionRecord(String group_id,String customer_id);
		
		public CustomerVO getEncodeData(CustomerVO customerVO);
	}

	public class CustomerService {
		private Customer_interface dao;

		public CustomerService() {
			dao = new CustomerDAO();
		}

		public void deleteCustomer(String customer_id, String user_id) {
			dao.deleteCustomer(customer_id, user_id);
		}
		public List<SaleVO> getTransactionRecord(String group_id,String customer_id){
			return dao.getTransactionRecord(group_id, customer_id);
		}


		public void addCustomer(String group_id, String name, String address, String phone, String mobile, String email,
				String post, String customerClass, String memo, String user_id,CustomerVO clearCustomerVO) {
			CustomerVO customerVO = new CustomerVO();
			customerVO.setGroup_id(group_id);
			customerVO.setName(name);
			customerVO.setAddress(address);
			customerVO.setPhone(phone);
			customerVO.setMobile(mobile);
			customerVO.setEmail(email);
			customerVO.setPost(post);
			customerVO.setCustomerClass(customerClass);
			customerVO.setMemo(memo);
			dao.insertDB(customerVO, user_id, clearCustomerVO);
		}

		public void updateCustomer(String customer_id, String group_id, String name, String address, String phone,
				String mobile, String email, String post, String customerClass, String memo, String user_id,CustomerVO clearCustomerVO) {
			CustomerVO customerVO = new CustomerVO();
			customerVO.setCustomer_id(customer_id);
			customerVO.setGroup_id(group_id);
			customerVO.setName(name);
			customerVO.setAddress(address);
			customerVO.setPhone(phone);
			customerVO.setMobile(mobile);
			customerVO.setEmail(email);
			customerVO.setPost(post);
			customerVO.setCustomerClass(customerClass);
			customerVO.setMemo(memo);
			dao.updateDB(customerVO, user_id,clearCustomerVO);
		}

		public List<CustomerVO> getAllCustomer(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public CustomerVO getEncodeData(String name, String address, String phone, String mobile) {
			CustomerVO customerVO = new CustomerVO();
			customerVO.setName(name);
			customerVO.setAddress(address);
			customerVO.setPhone(phone);
			customerVO.setMobile(mobile);
			return dao.getEncodeData(customerVO);
		}
		
		public List<CustomerVO> getCustomerVOByName(String group_id,CustomerVO customerVO){
			return dao.getCustomerVOByName(group_id, customerVO);
		}
	}

	class CustomerDAO implements Customer_interface {
		private static final String sp_del_customer = "call sp_del_customer(?,?)";
		private static final String sp_insert_customer = "call sp_insert_customer(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_update_customer = "call sp_update_customer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_selectall_customer = "call sp_selectall_customer(?)";
		private static final String sp_get_customervo_by_name = "call sp_get_customervo_by_name(?,?)";
		private static final String sp_get_transaction_record_by_customer_id = "call sp_get_transaction_record_by_customer_id(?,?)";


		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String wsPath = getServletConfig().getServletContext().getInitParameter("pythonwebservice");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");


		@Override
		public void deleteCustomer(String customer_id, String user_id) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_customer);
				pstmt.setString(1, customer_id);
				pstmt.setString(2, user_id);
				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (Exception ex) {
				System.out.println("Error: " + ex);
				// Clean up JDBC resources
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
		public void insertDB(CustomerVO customerVO, String user_id,CustomerVO clearCustomerVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_customer);

				pstmt.setString(1, customerVO.getGroup_id());
				pstmt.setString(2, customerVO.getName());
				pstmt.setString(3, customerVO.getAddress());
				pstmt.setString(4, customerVO.getPhone());
				pstmt.setString(5, customerVO.getMobile());
				pstmt.setString(6, customerVO.getEmail());
				pstmt.setString(7, customerVO.getPost());
				pstmt.setString(8, customerVO.getCustomerClass());
				pstmt.setString(9, customerVO.getMemo());
				pstmt.setString(10, user_id);
				pstmt.setString(11, clearCustomerVO.getName());
				pstmt.setString(12, clearCustomerVO.getAddress());
				pstmt.setString(13, clearCustomerVO.getPhone());
				pstmt.setString(14, clearCustomerVO.getMobile());

				pstmt.executeUpdate();
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (Exception ex) {
				System.out.println("Error: " + ex);
				// Clean up JDBC resources
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
		public void updateDB(CustomerVO customerVO, String user_id,CustomerVO clearCustomerVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_customer);

				pstmt.setString(1, customerVO.getCustomer_id());
				pstmt.setString(2, customerVO.getGroup_id());
				pstmt.setString(3, customerVO.getName());
				pstmt.setString(4, customerVO.getAddress());
				pstmt.setString(5, customerVO.getPhone());
				pstmt.setString(6, customerVO.getMobile());
				pstmt.setString(7, customerVO.getEmail());
				pstmt.setString(8, customerVO.getPost());
				pstmt.setString(9, customerVO.getCustomerClass());
				pstmt.setString(10, customerVO.getMemo());
				pstmt.setString(11, user_id);
				pstmt.setString(12, clearCustomerVO.getName());
				pstmt.setString(13, clearCustomerVO.getAddress());
				pstmt.setString(14, clearCustomerVO.getPhone());
				pstmt.setString(15, clearCustomerVO.getMobile());
				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (Exception ex) {
				System.out.println("Error: " + ex);
				// Clean up JDBC resources
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
		public List<CustomerVO> searchAllDB(String group_id) {
			// use database: tmp

			List<CustomerVO> list = new ArrayList<CustomerVO>();
			CustomerVO customerVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_customer);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					customerVO = new CustomerVO();
					customerVO.setCustomer_id(rs.getString("customer_id"));
					customerVO.setGroup_id(rs.getString("customer_id"));
					customerVO.setName(rs.getString("name"));
					customerVO.setAddress(rs.getString("address"));
					customerVO.setPhone(rs.getString("phone"));
					customerVO.setMobile(rs.getString("mobile"));
					customerVO.setEmail(rs.getString("email"));
					customerVO.setPost(rs.getString("post"));
					customerVO.setMemo(rs.getString("memo"));

					list.add(customerVO); // Store the row in the list
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
		public CustomerVO getEncodeData(CustomerVO customerVO) {

			CustomerVO voFromJson = new CustomerVO();

			String nameInBase64 = new String(Base64.encodeBase64String(customerVO.getName().getBytes()));
			String addressInBase64 = new String(Base64.encodeBase64String(customerVO.getAddress().getBytes()));
			String phoneInBase64 = new String(Base64.encodeBase64String(customerVO.getPhone().getBytes()));
			String mobileInBase64 = new String(Base64.encodeBase64String(customerVO.getMobile().getBytes()));

			String url = wsPath + "/aes/name=" + nameInBase64 + "&addr=" + addressInBase64 + "&phon=" + phoneInBase64
					+ "&mobi=" + mobileInBase64;

			HttpGet httpRequest = new HttpGet(url);
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse httpResponse;
			try {
				StringBuffer result = new StringBuffer();
				httpResponse = client.execute(httpRequest);
				int responseCode = httpResponse.getStatusLine().getStatusCode();

				if (responseCode == 200) {
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity().getContent()));
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					Gson gson = new Gson();
					voFromJson = gson.fromJson(result.toString(), CustomerVO.class);
				} else {
					System.out.println("fail to get encode data");
				}

			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (UnsupportedOperationException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			return voFromJson;
		}
		@Override
		public List<CustomerVO> getCustomerVOByName(String group_id, CustomerVO customer) {
			//use database: tmp

			List<CustomerVO> list = new ArrayList<CustomerVO>();
			CustomerVO customerVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_customervo_by_name);
				pstmt.setString(1, group_id);
				pstmt.setString(2, customer.getName());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					customerVO = new CustomerVO();
					customerVO.setCustomer_id(rs.getString("customer_id"));
					customerVO.setGroup_id(rs.getString("customer_id"));
					customerVO.setName(rs.getString("name"));
					customerVO.setAddress(rs.getString("address"));
					customerVO.setPhone(rs.getString("phone"));
					customerVO.setMobile(rs.getString("mobile"));
					customerVO.setEmail(rs.getString("email"));
					customerVO.setPost(rs.getString("post"));
					customerVO.setMemo(rs.getString("memo"));
					
					list.add(customerVO); // Store the row in the list
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
		public List<SaleVO> getTransactionRecord(String group_id, String customer_id) {
			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_transaction_record_by_customer_id);

				pstmt.setString(1, group_id);
				pstmt.setString(2, customer_id);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setProduct_name(rs.getString("product_name"));
					saleVO.setC_product_id(rs.getString("c_product_id"));
					saleVO.setQuantity(rs.getInt("quantity"));
					saleVO.setPrice(rs.getFloat("price"));
					saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					list.add(saleVO);
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
			return list;
		}
	}
}