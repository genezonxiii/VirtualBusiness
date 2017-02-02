package tw.com.aber.basicinfo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.CustomerVO;


public class customer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String err="";
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				//System.out.println(group_id);
				List<CustomerVO> list = customerService.getAllCustomer(group_id);
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
	        	
				customerService = new CustomerService();
				CustomerVO encodeCustomerData = customerService.getEncodeData(name, address, phone, mobile);
				customerService.addCustomer(group_id, encodeCustomerData.getName(), encodeCustomerData.getAddress(), 
						encodeCustomerData.getPhone(), encodeCustomerData.getMobile(), email, post, customerClass, memo, 
						user_id);
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
				CustomerVO encodeCustomerData = customerService.getEncodeData(name, address, phone, mobile);
				customerService.updateCustomer(customer_id, group_id, encodeCustomerData.getName(), 
						encodeCustomerData.getAddress(), encodeCustomerData.getPhone(), 
						encodeCustomerData.getMobile(), email, post, customerClass, memo, user_id);
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
				/*************************** 1.接收請求參數 ***************************************/
				String customer_id = request.getParameter("customer_id");
				/*************************** 2.開始刪除資料 ***************************************/
				customerService = new CustomerService();
				customerService.deleteCustomer(customer_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
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
	}

	interface Customer_interface {
		public List<CustomerVO> searchAllDB(String group_id);
		
		public void deleteCustomer(String customer_id, String user_id);
		
		public void insertDB(CustomerVO customerVO, String user_id);
		
		public void updateDB(CustomerVO customerVO, String user_id);
		
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

		public void addCustomer(String group_id, String name, String address, String phone, String mobile, 
				String email, String post, String customerClass, String memo, String user_id) {
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
			dao.insertDB(customerVO, user_id);
		}
		
		public void updateCustomer(String customer_id, String group_id, String name, String address, String phone, String mobile, 
				String email, String post, String customerClass, String memo, String user_id) {
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
			dao.updateDB(customerVO, user_id);
		}
		
		public List<CustomerVO> getAllCustomer(String group_id) {
			return dao.searchAllDB(group_id);
		}
		
		public CustomerVO getEncodeData(String name, String address, String phone, String mobile){
			CustomerVO customerVO = new CustomerVO();
			customerVO.setName(name);
			customerVO.setAddress(address);
			customerVO.setPhone(phone);
			customerVO.setMobile(mobile);
			return dao.getEncodeData(customerVO);
		}
	}

	class CustomerDAO implements Customer_interface {
		private static final String sp_del_customer = "call sp_del_customer(?,?)";
		private static final String sp_insert_customer = "call sp_insert_customer(?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_update_customer = "call sp_update_customer(?,?,?,?,?,?,?,?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String wsPath = getServletConfig().getServletContext().getInitParameter("pythonwebservice");
//		private final String wsPath = "http://abers1.eastasia.cloudapp.azure.com:8090";

		@Override
		public void deleteCustomer(String customer_id, String user_id) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_customer);
				pstmt.setString(1, customer_id);
				pstmt.setString(2, user_id);
				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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
		public void insertDB(CustomerVO customerVO, String user_id) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
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

				pstmt.executeUpdate();
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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
		public void updateDB(CustomerVO customerVO, String user_id) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
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
				
				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			}catch(Exception ex){
				System.out.println("Error: "+ ex );
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

			List<CustomerVO> list = new ArrayList<CustomerVO>();
	
        	String gidInBase64 = new String(Base64.encodeBase64String(group_id.getBytes()));
//        	gidInBase64 = new String(Base64.encodeBase64String("cbcc3138-5603-11e6-a532-000d3a800878".getBytes()));
			String url = wsPath + "/query/group=" + gidInBase64;
        	HttpGet httpRequest = new HttpGet(url);
        	HttpClient client = HttpClientBuilder.create().build();
//        	System.out.println("gid: "+group_id);
//        	System.out.println("wspath: "+wsPath);
//        	System.out.println("url: "+url);
        	
        	HttpResponse httpResponse;
        	try {
        		StringBuffer result = new StringBuffer();
        		httpResponse = client.execute(httpRequest);
    			int responseCode = httpResponse.getStatusLine().getStatusCode();
    
    	    	if(responseCode==200){
    	    		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        	    	String line = "";
        	    	while ((line = rd.readLine()) != null) {
        	    		result.append(line);
        	    	}	
    	    		Gson gson = new Gson();
    	    		list = gson.fromJson(result.toString(), List.class);
    	    	}
    	    	else{
    	    		System.out.println("responseCode: " + responseCode);
    	    		System.out.println("fail to get data");
    	    	}    	    	
    		} catch (ClientProtocolException e) {
    			
    			e.printStackTrace();
    		} catch (UnsupportedOperationException e) {
    			
    			e.printStackTrace();
    		} catch (IOException e) {
    			
//    			e.printStackTrace();
    			err="ConnectIOE Error: "+e.toString();
    		}catch (Exception e){
    			err="Connect Error: "+e.toString();
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
			
        	String url = wsPath + "/aes/name="+nameInBase64+"&addr="+addressInBase64+"&phon="+phoneInBase64+"&mobi="+mobileInBase64;
        	
        	HttpGet httpRequest = new HttpGet(url);
        	HttpClient client = HttpClientBuilder.create().build();
        	HttpResponse httpResponse;
        	try {
        		StringBuffer result = new StringBuffer();
        		httpResponse = client.execute(httpRequest);
    			int responseCode = httpResponse.getStatusLine().getStatusCode();
    
    	    	if(responseCode==200){
    	    		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        	    	String line = "";
        	    	while ((line = rd.readLine()) != null) {
        	    		result.append(line);
        	    	}	
    	    		Gson gson = new Gson();
    	    		voFromJson = gson.fromJson(result.toString(), CustomerVO.class);
    	    	}
    	    	else{
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
	}
}