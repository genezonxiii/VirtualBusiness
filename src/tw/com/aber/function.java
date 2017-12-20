
package tw.com.aber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
//import tw.com.aber.basicinfo.customer;
//import tw.com.aber.basicinfo.customer.CustomerService;
//import tw.com.aber.basicinfo.customer.CustomerVO;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import tw.com.aber.product.controller.product.ProductBean;

//import tw.com.aber.basicinfo.customer.CustomerDAO;
//import tw.com.aber.basicinfo.customer.CustomerVO;
//import tw.com.aber.basicinfo.customer.Customer_interface;

import java.util.Date; 
import java.text.SimpleDateFormat;
@SuppressWarnings("serial")

public class function extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action2= request.getParameter("action");
		if("get_controversial".equals(action2)){
			String askfor= request.getParameter("askfor");
			if("164".equals(askfor)){response.getWriter().write("192.168.112.164:8080");}
			if("aber".equals(askfor)){response.getWriter().write("abers1.eastasia.cloudapp.azure.com:8080");}
			if("Demouser_uni".equals(askfor)){response.getWriter().write("20939790");}
			if("Demouser_usr".equals(askfor)){response.getWriter().write("pershing@pershing.com.tw");}
			if("Demouser_pwd".equals(askfor)){response.getWriter().write("1234");}
			if("Myuser_uni".equals(askfor)){response.getWriter().write("1234");}
			if("Myuser_usr".equals(askfor)){response.getWriter().write("sett@archworld.com");}
			if("Myuser_pwd".equals(askfor)){response.getWriter().write("1234");}
			
			return ;
		}
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		int i=0,j;
		String action= request.getParameter("action");
		FunctionDAO dao= new FunctionDAO();
		List<FunctionVO> list = new ArrayList<FunctionVO>();
		if("customer_class_now".equals(action)){
			//叫sp_update_customer_class 
		}else if("default_primary_data".equals(action)){
			//type
			for(i=0;i<dao.default_type.length;i++){
				String type_name = dao.default_type[i];//[(int)(Math.random()*dao.supply_name.length)];
//				dao.insert_type(group_id,user_id,type_name);
				dao.insert_type(group_id,"sys",type_name);
				//System.out.println(type_name);
			}
			//unit
			for(i=0;i<dao.default_unit.length;i++){
				for(j=0;j<dao.default_unit2.length;j++){
					String unit_name =dao.default_unit[i]+ dao.default_unit2[j];//[(int)(Math.random()*dao.supply_name.length)];
//					dao.insert_unit(group_id,user_id,unit_name);
					dao.insert_unit(group_id,"sys",unit_name);
					//System.out.println(i+" "+j+" "+unit_name+" ");
				}
			}
			//supply
			for(i=0;i<dao.supply_name.length;i++){
				String supply_name = dao.supply_name[i];//[(int)(Math.random()*dao.supply_name.length)];
				String supply_unicode = String.format("%08d",(int)(Math.random()*100000000));
				String address = dao.city[(int)(Math.random()*dao.city.length)]+dao.road[(int)(Math.random()*dao.road.length)]+(Math.random()>0.6?((int)(Math.random()*300+1)+"巷"):"")+(Math.random()>0.8?(int)(Math.random()*100+1)+"弄":"")+((int)(Math.random()*300+1)+"號"+(int)(Math.random()*4+1)+"樓");
				String contact = dao.name[(int)(Math.random()*dao.name.length)];
				String phone = "0"+(int)(Math.random()*8+2)+"-"+String.format("%08d",(int)(Math.random()*100000000));
				String ext = (int)(Math.random()*10000+1)+"";
				String mobile = "09"+String.format("%02d",(int)(Math.random()*100))+"-"+String.format("%03d",(int)(Math.random()*1000))+"-"+String.format("%03d",(int)(Math.random()*1000));//String.format("%08d",(int)(Math.random()*100000000));
				String email = (char)((int)(Math.random()*26+97))+""+(char)((int)(Math.random()*26)+97)+""+(Math.random()>0.5?(char)((int)(Math.random()*10)+48)+"":(char)((int)(Math.random()*26)+97)+"")+(Math.random()>0.5?(char)((int)(Math.random()*10)+48)+"":(char)((int)(Math.random()*26)+97)+"")+(char)((int)(Math.random()*10)+48)+""+(char)((int)(Math.random()*10)+48)+"@gmail.com";
				//+(Math.random()>0.5?(char)((int)(Math.random()*48)+10)+"":(char)((int)(Math.random()*26)+97)+"")
				//+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+"@gmail.com";
				String contact1 = dao.name[(int)(Math.random()*dao.name.length)];
				String phone1 = "0"+(int)(Math.random()*8+2)+"-"+String.format("%08d",(int)(Math.random()*100000000));
				String ext1 = (int)(Math.random()*10000+1)+"";
				String email1 = (char)((int)(Math.random()*26+97))+""+(char)((int)(Math.random()*26)+97)+""+(Math.random()>0.5?(char)((int)(Math.random()*10)+48)+"":(char)((int)(Math.random()*26)+97)+"")+(Math.random()>0.5?(char)((int)(Math.random()*10)+48)+"":(char)((int)(Math.random()*26)+97)+"")+(char)((int)(Math.random()*10)+48)+""+(char)((int)(Math.random()*10)+48)+"@gmail.com";
				String mobile1 = "09"+String.format("%02d",(int)(Math.random()*100))+"-"+String.format("%03d",(int)(Math.random()*1000))+"-"+String.format("%03d",(int)(Math.random()*1000));//String.format("%08d",(int)(Math.random()*100000000));
				String memo = "sys";
				dao.insert_supply(group_id,user_id,supply_name,supply_unicode,address,contact,phone,ext,email,mobile,contact1,phone1,ext1,email1,mobile1,memo);
			}
			System.out.println("unitypesupply_success");
			response.getWriter().write("success");
		}else if("default_senior_data".equals(action)){
			//customer
			int number= Integer.parseInt(request.getParameter("n").length()<1?"0":request.getParameter("n"));
			for(i=0;i<number;i++){
				CustomerService customerService = null;
				try {
					String name = dao.name[(int)(Math.random()*dao.name.length)];
					String address =dao.city[(int)(Math.random()*dao.city.length)]+dao.road[(int)(Math.random()*dao.road.length)]+(Math.random()>0.6?((int)(Math.random()*300+1)+"巷"):"")+(Math.random()>0.8?(int)(Math.random()*100+1)+"弄":"")+((int)(Math.random()*300+1)+"號"+(int)(Math.random()*4+1)+"樓");
					String phone = "0"+(int)(Math.random()*8+2)+"-"+String.format("%08d",(int)(Math.random()*100000000));
					String mobile = "09"+String.format("%02d",(int)(Math.random()*100))+"-"+String.format("%03d",(int)(Math.random()*1000))+"-"+String.format("%03d",(int)(Math.random()*1000));//String.format("%08d",(int)(Math.random()*100000000));
					String email = (char)((int)(Math.random()*26+97))+""+(char)((int)(Math.random()*26)+97)+""+(Math.random()>0.5?(char)((int)(Math.random()*10)+48)+"":(char)((int)(Math.random()*26)+97)+"")+(Math.random()>0.5?(char)((int)(Math.random()*10)+48)+"":(char)((int)(Math.random()*26)+97)+"")+(char)((int)(Math.random()*10)+48)+""+(char)((int)(Math.random()*10)+48)+"@gmail.com";
					String post = "";
					String customerClass = "";
					String memo = "sys";
					//System.out.println(address);
					//System.out.println(number+" "+name+" "+address+" "+phone+" "+mobile+" ");
					//customer mc = new customer();
					customerService = new CustomerService();
					CustomerVO encodeCustomerData = customerService.getEncodeData(name, address, phone, mobile);
					customerService.addCustomer(group_id, encodeCustomerData.getName(), encodeCustomerData.getAddress(), 
							encodeCustomerData.getPhone(), encodeCustomerData.getMobile(), email, post, customerClass, memo,user_id);
					//List<CustomerVO> list = customerService.getAllCustomer(group_id);
					Gson gson = new Gson();
					String jsonList = gson.toJson(list);
					response.getWriter().write(jsonList);
				} catch (NumberFormatException e) {e.printStackTrace();System.out.println("customer_error");}
			}
			//#########################################################(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)+(char)(int)((Math.random()*26)+97)
			for(i=0;i<dao.default_product_name.length;i++){
				String c_product_id = (char)((int)(Math.random()*26+65))+""+(char)((int)(Math.random()*26+65))+""+String.format("%05d",(int)(Math.random()*100000));
				String product_name = dao.default_product_name[i];
				String supply_id = "";
				String supply_name = "";
				String type_id ="";
				String unit_id = "";
				String cost = (int)(Math.random()*3000+100)+"";
				String price =(Integer.valueOf(cost)+(int)(Math.random()*1000))+"";
				String current_stock =(int)(Math.random()*50+50)+"";
				String keep_stock = (int)(Math.random()*50)+"";
				String photo = "";
				String photo1 = "";
				String description = "sys";
				String barcode = "";
				dao.insert_product(group_id,user_id, c_product_id, product_name, supply_id, supply_name, type_id, unit_id,cost, price, current_stock, keep_stock, photo, photo1, description,barcode);
			}
			System.out.println("customerproduct_success");
			response.getWriter().write("success");
		}else if("random_product_value".equals(action)){
			//叫sp_update_product_randunittype('cbcc3138-5603-11e6-a532-000d3a800878')
			dao.randunittype(group_id);
			System.out.println("productval_success");
			response.getWriter().write("success");
		}else if("random_sale".equals(action)){
			int month= Integer.parseInt(request.getParameter("month").length()<1?"1":request.getParameter("month"));
			int number= Integer.parseInt(request.getParameter("n").length()<1?"0":request.getParameter("n"));
			String order_source= (request.getParameter("order_source").length()<1?"":request.getParameter("order_source"));
//			System.out.println(order_source);
			dao.insert_salerand(group_id, month, number, order_source);
			//CALL sp_insert_salerand('cbcc3138-5603-11e6-a532-000d3a800878',10,40,'momo');
			System.out.println("randsale_success");
			response.getWriter().write("success");
		}else if("random_purchase".equals(action)){
			response.getWriter().write("?");
		}else if("init".equals(action)){
			dao.delete_sys(group_id);
			System.out.println("delete_success");
			response.getWriter().write("success");
		}else if("example".equals(action)){
//			String exchange_id= request.getParameter("exchange_id");
//			dao.delete(group_id, exchange_id);
//			list=dao.searhDB(group_id);
		}
		//Gson gson = new Gson();
		//String jsonStrList = gson.toJson(list);
		//System.out.println("json: "+ jsonStrList);
		//response.getWriter().write(jsonStrList);
		return;
		//###########################################
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class FunctionVO implements java.io.Serializable {
		public String exchange_id;
		public String group_id;
		public String currency;
		public String exchange_rate;
	}

	/*************************** 操作資料庫 ****************************************/
	class FunctionDAO  {
		// 會使用到的Stored procedure		
		private static final String sp_insert_supply = "call sp_insert_supply(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_insert_product_unit = "call sp_insert_product_unit(?,?,?)";
		private static final String sp_insert_product_type = "call sp_insert_product_type(?,?,?)";
		private static final String sp_update_product_randunittype = "call sp_update_product_randunittype(?)";
		private static final String sp_insert_salerand = "call sp_insert_salerand(?,?,?,?)";
		private static final String sp_insert_product = "call sp_insert_product(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		
		
		public void delete_sys(String group_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("DELETE FROM tb_sale WHERE memo='sys' and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("DELETE FROM tb_customer WHERE memo='sys' and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("DELETE FROM tb_product WHERE description='sys' and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("DELETE FROM tb_supply WHERE memo='sys' and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("DELETE FROM tb_product WHERE description='sys' and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("Delete from tb_product_unit where (unit_name like '% XS' or unit_name like '% S' or unit_name like '% M' or unit_name like '% L' or unit_name like '% XL' or unit_name like '% XXL') and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("Delete from tb_product_type where (type_name like '%  ') and group_id = ?");
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		
		public void insert_product(String group_id, String user_id, String c_product_id, String product_name, String supply_id, String supply_name, String type_id, String unit_id, String cost,String price,String current_stock,String keep_stock,String photo,String photo1,String description,String barcode){
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product);
				pstmt.setString(1,group_id);
				pstmt.setString(2,c_product_id);
				pstmt.setString(3,product_name);
				pstmt.setString(4,supply_id);
				pstmt.setString(5,supply_name);
				pstmt.setString(6,type_id);
				pstmt.setString(7,unit_id);
				pstmt.setString(8,cost);
				pstmt.setString(9,price);
				pstmt.setString(10,keep_stock);
				pstmt.setString(11,photo);
				pstmt.setString(12,photo1);
				pstmt.setString(13,description);
				pstmt.setString(14,barcode);
				pstmt.setString(15,user_id);
				pstmt.setString(16,current_stock);
				//System.out.println("in"+productBean.getPhoto()+"in"+productBean.getPhoto1());
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		public void insert_salerand(String group_id,int month,int number,String order_source) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_salerand);
				pstmt.setString(1, group_id);
				pstmt.setInt(2, month);
				pstmt.setInt(3, number);
				pstmt.setString(4, order_source);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		public void randunittype(String group_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_product_randunittype);
				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		public void insert_type(String group_id, String user_id, String type_name) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_type);
				pstmt.setString(1, group_id);
				pstmt.setString(2, type_name);
				pstmt.setString(3, user_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		public void insert_unit(String group_id, String user_id, String unit_name) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_product_unit);

				pstmt.setString(1, group_id);
				pstmt.setString(2, unit_name);
				pstmt.setString(3, user_id);
				pstmt.executeUpdate();
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		public void insert_supply(String group_id, String user_id, String supply_name, String supply_unicode, String address, String contact, String phone, String ext, String email, String mobile, String contact1, String phone1, String ext1, String email1, String mobile1, String memo) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_supply);

				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_name);
				pstmt.setString(3, supply_unicode);
				pstmt.setString(4, address);
				pstmt.setString(5, contact);
				pstmt.setString(6, phone);
				pstmt.setString(7, ext);
				pstmt.setString(8, mobile);
				pstmt.setString(9, contact1);
				pstmt.setString(10, phone1);
				pstmt.setString(11, ext1);
				pstmt.setString(12, mobile1);
				pstmt.setString(13, email);
				pstmt.setString(14, email1);
				pstmt.setString(15, memo);
				pstmt.setString(16, user_id);
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
//		public List<FunctionVO> searhDB(String group_id) {
//			FunctionVO functionVO = null;
//			List<FunctionVO > list = new ArrayList<FunctionVO>();
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_selectall_exchange);
//				pstmt.setString(1,group_id);
//				rs = pstmt.executeQuery();
//
//			    while (rs.next()) {
//			    	functionVO =new FunctionVO();
//			    	functionVO.exchange_id   =rs.getString("exchange_id");
//			    	functionVO.group_id      =rs.getString("group_id");
//			    	functionVO.currency      =rs.getString("currency");
//					functionVO.exchange_rate =rs.getString("exchange_rate");
//					list.add(functionVO);
//				}
//			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			}
//			return list;
//		}
//		
//		public void insert(String group_id,String currency, String exchange_rate) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_insert_exchange);
//				pstmt.setString(1,group_id);
//				pstmt.setString(2,currency);
//				pstmt.setString(3,exchange_rate);
//				pstmt.executeQuery();
//			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			}
//			return ;
//		}
//		
//		public void update(String exchange_id, String group_id,  String currency,  String exchange_rate) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_update_exchange);
////				System.out.println(exchange_id+" "+group_id+" "+currency+" "+exchange_rate);
//				pstmt.setString(1,exchange_id);
//				pstmt.setString(2,group_id);
//				pstmt.setString(3,currency);
//				pstmt.setString(4,exchange_rate);
//				pstmt.executeQuery();
//			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			}
//			return ;
//		}
//		
//		public void delete(String group_id, String exchange_id) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_del_exchange);
//				pstmt.setString(1,group_id);
//				pstmt.setString(2,exchange_id);
//				pstmt.executeQuery();
//			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			}
//			return ;
//		}
		public String[] supply_name = new String[]{"時尚頂端 ","衣絲坦堡 ","禮拜四","一坪大","獨衣無二","初米","溫慶珠","小舞服飾","貓咪曬月亮 ","彼兔betwo","蘋果星沙","A-So-Bi","Rainbow Shop","LuLu’s","Catworld","STARMIMI","Miu-Star","衣服日記","Imagine girl","Two Do","衣衣不捨","五分埔NET","五分埔UNIQLO","五分埔GIORDANO","五分埔ESPRIT","五分埔HANG TEN","五分埔bossini","五分埔G2000","五分埔crocodile"};
		public String[] name = new String[]{"劉淑惠","張珮瑜","許銘谷","李仁利","謝曉慧","詹怡婷","陳光勇","楊偉銘","何建宏","劉文香","戴香君","胡鈺雯","蔡欣怡","賁孟哲","施惠雯","蔣桂發","洪宜臻","呂珊軍","陳豪甫","鄭惠茹","張雲凱","沈陽吟","張雯志","鄭宗穎","鍾明吟","杜喬州","周辛亮","謝宜真","吳麗君","陳凡瑜","丁行妏","楊亦芬","黃允蓁","羅書新","張怡君","謝皇函","吳宇隆","林名雨","賴威任","奚家豪","黃玄光","王宗憲","張昱玟","李淑玲","李威福","陳怡如","陳友汝","鄭俊彥","魏建中","蔡石侑","林美坤","林昇怡","張佩君","黃莉南","余右亦","簡琦堅","黃胤一","劉夢佩","林奎隆","吳石全","林以明","陳瑋心","黃美侑","白弘明","曹伯榮","李韋成","賴秀娟","陳佳怡","李俊一","蔡健豪","陳大姍","韓雅馨","韓雅如","賴雅婷","周明憲","盧名堅","何辛恬","楊怡萍","劉佳達","林瑋甫","郭聖凱","陳培倫","許姵君","劉志瑋","黃信松","林威廷","張心怡","鞠家銘","王智翔","林柏恩","陳柏伯","李君豪","林宜芳","林詩妹","陳玉婷","李芸喬","楊建志","葉惠雯","李偉婷","黃盈如","林雅玲","陳左啟","林雲宏","楊貞儀","謝坤翰","吳玉婷","陳俞其","倪淑惠","蔡建臻","陳佳蓉","吳志劭","林馨慧","陳郁婷","林靜凱","傅肇寧","陳信麟","王玟君","黃慧珊","郭盛育","王典瑞","方淑芳","陳冠誠","丁婉菁","林鈺芃","李俊廷","林雅雯","葉語嬌","林育意","涂宣姍","錢守祥","潘和元","王敬尹","蔡雅民","游奕廷","簡欣瑜","張邦榮","劉志峰","鮑子雲","張美茵","鄧佩玲","陳立依","趙人豪","林孟南","楊寶昀","黃信宏","林君安","劉燦雲","宋佩穎","周俊達","胡浩禾","葉南樺","黃玉書","陳正蘭","黃綺萱","王乃哲","黃仁怡","黃慧敏","溫裕財","陳俊善","陳禎財","林得娥","郭健勇","蒙欣瑜","黃冠城","姜家榮","陳香恬","張仕鈺","李怡婷","張鳳珠","藍佑軍","林郁昇","潘廷全","陳宛真","林文誠","李姿蓉","趙胤翰","涂建文","林育為","陳宜修","李欣誠","柳新花","黃凱翔","郭俊祥","狄皇旭","李政郁","溫維雨","楊慧君","周仁傑","曾崇其","黃志筠","許淑玲","陳喜翰","李雯聖","吳凱倫","姜雅雯","葉俊任","孫亭君","許夢睿","郭豪貞","王秀生","黃明珠","林冠良","趙怡潔","賴政季","謝恩東","潘健豪","方紹恭","賴方蘭","李佳芳","童維映","林怡廷","彭冠宇","吳星旭","林正名","王淑芳","吳仁杰","盧以","岑嘉玲","余佳穎","常豐宏","許佳慧","溫軍江","張淑安","陸建志","李哲瑋","吳政峰","徐家瑋","陳辛佩","陳湖德","吳俊士","劉書茜","阮智翔","陳玟綺","王育堅","王承翰","高寧玉","陳淑雯","方浩易","陳舜卉","宋志強","陳淑君","王欣怡","陳信珊","劉家榮","陳玉屏","許堯菱","許寧順","施亭一","蘇妙名","陳若林","林琬婷","王美娟","吳欣珊","陳子軒","吳思全","家依萍","林夢齊","彭佩璇","陳文賢","鄭喜航","朱雅雯","敖文彬","靳維謙","陳國美","劉惠茹","劉淑娟","王博幸","林思恬","蘇奕廷","蔡新隆","張月妃","黃家蓮","鄭雅惠","姜雅鈴","徐坤迪","陳淑華","蔡珮瑜","林育柏","王俊宇","周俊豪","蒙書瑋","賴俊毅","洪峻如","周怡伶","張善珊","李峻谷","黃皇恆","杜昕洋","陳惠玲","陳雅雯","林政玉","江家瑋","楊喜泉","曲靜如","林裕英","林治佳","張志凡","李琦峰","盧彥博","陳銘苓","陳筠佩","黃淑伸","楊怡雯","胡翠法","陳百淳","吳于慈","蔡志民","蔡宗妃","許家俐","張上谷","鄭凱合","楊登蓉","徐筱涵","鄭立冰","盧哲榮","許佩珊","張詩婷","林書瑋","曹怡文","葉岱麟","朱山如","陳怡男","黃奕侑","賴琬婷","金淑傑","王雅剛","吳登紋","沈映禮","魏萱來","楊孝寶","陳佳蓉","張祐琪","陳駿廷","江柏宏","顏皇然","黃文行","毛則瑄","李偉意","蔡麗卿","賴凱婷","陳麗祥","羅怡如","傅冠伶","李則音","鄧奕君","郭凱德","徐宛真","蘇長意","趙俊毅","方人蘭","袁胤學","王光岑","吳建中","張文華","楊志來","涂陽松","宋明宏","林敬意","李雅君","謝美珠","歐勇方","李建中","謝盈君","鄭奎傑","鄭義新","吳傑紋","林彥宏","江翊瑄","孫怡禎","江怡婷","張亭汝","葉美惠","徐萱男","苗明軒","賴俊成","曾茂菁","許家榮","吳先亞","賴靜儒","許兆欣","林志珠","趙吉樺","於大鈞","黎珊佐","陳俊瑋","姚靖延","何延珠","駱胤昆","張凱念","謝怡靜","彭海和","吳石琇","張凱希","陳水發","林俊成","葉筱涵","黃彥芷","謝皓惠","陳文欣","陳宥冰","錢惠紋","李淑琇","陳珈山","葉宛真","馬子傑","潘卓侑","陳家嘉","蔡玉華","林羽蘋","葉佩璇","張凱梅","林彥亨","陳瑞婷","蔡志虹","陳雅琪","郭惠凌","詹國榮","陳琦坤","羅任方","喻靖康","李喜卉","方俊良","吳俊堅","李彥盈","劉建琴","林韋廷","郭吉純","陳嘉娟","盧柏鈞","周家祥","侯玉婷","陳雅仰","張明輝","陳育政","林哲銘","米舒人","周振瑋","莊惠山","陳呈禮","陳淑貞","高怡君","連思婷","孫玉華","吳姵竹","張宗穎","林怡彬","黃淑卿","林佳辛","林佳綸","翁羽平","錢姿伶","劉冠惟","郭雅茹","方明儒","陳民勇","黃哲彬","張瑋婷","張惠富","陸其達","廖偉義","陳姿穎","黃思妤","王怡帆","游鈺婷","林靜青","徐嘉定","黃于均","周碧伯","潘燕音","黃得珮","袁雅萍","陳麗華","曲義裕","趙劭謙","林昀良","吳子翔","童惠雯","李淑華","侯筱婷","陳依彥","鄧亦真","馬人清","李怡禎","賴家瑜","陳珮瑜","謝偉萱","林育希","黃建淑","蘇家瑋","張皓香","林劭喜","李柏瑜","黃智傑","陳建諭","袁浩泉","吳浩雨","胡皓卿","謝良軒","袁冠中","陳君凌"};
		public String[] city = new String[]{"基隆市仁愛區","基隆市中正區","基隆市信義區","基隆市中山區","基隆市安樂區","基隆市暖暖區","基隆市七堵區","臺北市中正區","臺北市大同區","臺北市中山區","臺北市松山區","臺北市大安區","臺北市萬華區","臺北市信義區","臺北市士林區","臺北市北投區","臺北市內湖區","臺北市南港區","臺北市文山區","新北市板橋區","新北市新莊區","新北市中和區","新北市永和區","新北市土城區","新北市樹林區","新北市三峽區","新北市鶯歌區","新北市三重區","新北市蘆洲區","新北市五股區","新北市泰山區","新北市林口區","新北市八里區","新北市淡水區","新北市三芝區","新北市石門區","新北市金山區","新北市萬里區","新北市汐止區","新北市瑞芳區","新北市平溪區","新北市貢寮區","新北市雙溪區","新北市深坑區","新北市石碇區","新北市新店區","新北市坪林區","新北市烏來區","桃園市桃園區","桃園市中壢區","桃園市平鎮區","桃園市八德區","桃園市楊梅區","桃園市蘆竹區","桃園市大溪區","桃園市龍潭區","桃園市龜山區","桃園市大園區","桃園市觀音區","桃園市新屋區","桃園市復興區","新竹市東區","新竹市北區","新竹市香山區","新竹縣竹北市","新竹縣竹東鎮","新竹縣新埔鎮","新竹縣關西鎮","新竹縣湖口鄉","新竹縣新豐鄉","新竹縣峨眉鄉","新竹縣寶山鄉","新竹縣北埔鄉","新竹縣芎林鄉","新竹縣橫山鄉","新竹縣尖石鄉","新竹縣五峰鄉","宜蘭縣宜蘭市","宜蘭縣頭城鎮","宜蘭縣礁溪鄉","宜蘭縣壯圍鄉","宜蘭縣員山鄉","宜蘭縣羅東鎮","宜蘭縣蘇澳鎮","宜蘭縣五結鄉","宜蘭縣三星鄉","宜蘭縣冬山鄉","宜蘭縣大同鄉","宜蘭縣南澳鄉","苗栗縣苗栗市","苗栗縣造橋鄉","苗栗縣西湖鄉","苗栗縣頭屋鄉","苗栗縣公館鄉","苗栗縣銅鑼鄉","苗栗縣三義鄉","苗栗縣大湖鄉","苗栗縣獅潭鄉","苗栗縣卓蘭鎮","苗栗縣竹南鎮","苗栗縣頭份市","苗栗縣三灣鄉","苗栗縣南庄鄉","苗栗縣後龍鎮","苗栗縣通霄鎮","苗栗縣苑裡鎮","苗栗縣泰安鄉","臺中市中區","臺中市東區","臺中市南區","臺中市西區","臺中市北區","臺中市北屯區","臺中市西屯區","臺中市南屯區","臺中市太平區","臺中市大里區","臺中市霧峰區","臺中市烏日區","臺中市豐原區","臺中市后里區","臺中市石岡區","臺中市東勢區","臺中市和平區","臺中市新社區","臺中市潭子區","臺中市大雅區","臺中市神岡區","臺中市大肚區","臺中市沙鹿區","臺中市龍井區","臺中市梧棲區","臺中市清水區","臺中市大甲區","臺中市外埔區","臺中市大安區","彰化縣彰化市","彰化縣員林市","彰化縣和美鎮","彰化縣鹿港鎮","彰化縣溪湖鎮","彰化縣二林鎮","彰化縣田中鎮","彰化縣北斗鎮","彰化縣花壇鄉","彰化縣芬園鄉","彰化縣大村鄉","彰化縣永靖鄉","彰化縣伸港鄉","彰化縣線西鄉","彰化縣福興鄉","彰化縣秀水鄉","彰化縣埔心鄉","彰化縣埔鹽鄉","彰化縣大城鄉","彰化縣芳苑鄉","彰化縣竹塘鄉","彰化縣社頭鄉","彰化縣二水鄉","彰化縣田尾鄉","彰化縣埤頭鄉","彰化縣溪州鄉","南投縣南投市","南投縣埔里鎮","南投縣草屯鎮","南投縣竹山鎮","南投縣集集鎮","南投縣名間鄉","南投縣鹿谷鄉","南投縣中寮鄉","南投縣魚池鄉","南投縣國姓鄉","南投縣水里鄉","南投縣信義鄉","南投縣仁愛鄉","雲林縣斗六市","雲林縣斗南鎮","雲林縣林內鄉","雲林縣古坑鄉","雲林縣大埤鄉","雲林縣莿桐鄉","雲林縣虎尾鎮","雲林縣西螺鎮","雲林縣土庫鎮","雲林縣褒忠鄉","雲林縣二崙鄉","雲林縣崙背鄉","雲林縣麥寮鄉","雲林縣臺西鄉","雲林縣東勢鄉","雲林縣北港鎮","雲林縣元長鄉","雲林縣四湖鄉","雲林縣口湖鄉","雲林縣水林鄉","嘉義市東區","嘉義市西區","嘉義縣太保市","嘉義縣朴子市","嘉義縣布袋鎮","嘉義縣大林鎮","嘉義縣民雄鄉","嘉義縣溪口鄉","嘉義縣新港鄉","嘉義縣六腳鄉","嘉義縣東石鄉","嘉義縣義竹鄉","嘉義縣鹿草鄉","嘉義縣水上鄉","嘉義縣中埔鄉","嘉義縣竹崎鄉","嘉義縣梅山鄉","嘉義縣番路鄉","嘉義縣大埔鄉","嘉義縣阿里山鄉","臺南市中西區","臺南市東區","臺南市南區","臺南市北區","臺南市安平區","臺南市安南區","臺南市永康區","臺南市歸仁區","臺南市新化區","臺南市左鎮區","臺南市玉井區","臺南市楠西區","臺南市南化區","臺南市仁德區","臺南市關廟區","臺南市龍崎區","臺南市官田區","臺南市麻豆區","臺南市佳里區","臺南市西港區","臺南市七股區","臺南市將軍區","臺南市學甲區","臺南市北門區","臺南市新營區","臺南市後壁區","臺南市白河區","臺南市東山區","臺南市六甲區","臺南市下營區","臺南市柳營區","臺南市鹽水區","臺南市善化區","臺南市大內區","臺南市山上區","臺南市新市區","臺南市安定區","高雄市楠梓區","高雄市左營區","高雄市鼓山區","高雄市三民區","高雄市鹽埕區","高雄市前金區","高雄市新興區","高雄市苓雅區","高雄市前鎮區","高雄市旗津區","高雄市小港區","高雄市鳳山區","高雄市大寮區","高雄市鳥松區","高雄市林園區","高雄市仁武區","高雄市大樹區","高雄市大社區","高雄市岡山區","高雄市路竹區","高雄市橋頭區","高雄市梓官區","高雄市彌陀區","高雄市永安區","高雄市燕巢區","高雄市田寮區","高雄市阿蓮區","高雄市茄萣區","高雄市湖內區","高雄市旗山區","高雄市美濃區","高雄市內門區","高雄市杉林區","高雄市甲仙區","高雄市六龜區","高雄市茂林區","高雄市桃源區","高雄市那瑪夏區","屏東縣屏東市","屏東縣潮州鎮","屏東縣東港鎮","屏東縣恆春鎮","屏東縣萬丹鄉","屏東縣崁頂鄉","屏東縣新園鄉","屏東縣林邊鄉","屏東縣南州鄉","屏東縣琉球鄉","屏東縣枋寮鄉","屏東縣枋山鄉","屏東縣車城鄉","屏東縣滿州鄉","屏東縣高樹鄉","屏東縣九如鄉","屏東縣鹽埔鄉","屏東縣里港鄉","屏東縣內埔鄉","屏東縣竹田鄉","屏東縣長治鄉","屏東縣麟洛鄉","屏東縣萬巒鄉","屏東縣新埤鄉","屏東縣佳冬鄉","屏東縣霧臺鄉","屏東縣泰武鄉","屏東縣瑪家鄉","屏東縣來義鄉","屏東縣春日鄉","屏東縣獅子鄉","屏東縣牡丹鄉","屏東縣三地門鄉","澎湖縣馬公市","澎湖縣湖西鄉","澎湖縣白沙鄉","澎湖縣西嶼鄉","澎湖縣望安鄉","澎湖縣七美鄉","花蓮縣花蓮市","花蓮縣鳳林鎮","花蓮縣玉里鎮","花蓮縣新城鄉","花蓮縣吉安鄉","花蓮縣壽豐鄉","花蓮縣光復鄉","花蓮縣豐濱鄉","花蓮縣瑞穗鄉","花蓮縣富里鄉","花蓮縣秀林鄉","花蓮縣萬榮鄉","花蓮縣卓溪鄉","臺東縣臺東市","臺東縣成功鎮","臺東縣關山鎮","臺東縣長濱鄉","臺東縣池上鄉","臺東縣東河鄉","臺東縣鹿野鄉","臺東縣卑南鄉","臺東縣大武鄉","臺東縣綠島鄉","臺東縣太麻里鄉","臺東縣海端鄉","臺東縣延平鄉","臺東縣金峰鄉","臺東縣達仁鄉","臺東縣蘭嶼鄉","金門縣金城鎮","金門縣金湖鎮","金門縣金沙鎮","金門縣金寧鄉","金門縣烈嶼鄉","金門縣烏坵鄉","連江縣南竿鄉","連江縣北竿鄉","連江縣莒光鄉","連江縣東引鄉"};
		public String[] road = new String[]{"中正路","中山路","中華路","林森路","民族路","民權路","民生路","忠孝路","仁愛路","信義路","和平路","新生路","公園路","建國路","長沙路","三民路","四維路","中興路","文化路","博愛路","經國路"};
		public String[] default_type = new String[]{"印花T恤  ","短袖・五分袖  ","七分袖・長袖  ","長版上衣  ","厚棉系列  ","POLO衫  ","媽媽裝  ","法蘭絨襯衫  "
				,"休閒襯衫  ","商務襯衫  ","雪紡・輕柔系列  ","針織衫・毛衣  ","美麗諾羊毛  ","喀什米爾羊毛  ","保暖FLEECE系列  ","外套・風衣・大衣  "
				,"極輕羽絨外套  ","保暖羽絨外套  ","牛仔褲  ","長褲  ","短褲．七分褲  ","裙裝・洋裝  ","無鋼圈內衣  ","Bra_附罩杯內衣  "
				,"細肩帶・背心  ","HEATUP_保暖衣  ","COOL_輕涼系列  ","內褲  ","褲襪・內搭褲  ","襪子  ","輕便褲  ","家居服  "
				,"圍巾．帽子．手套  ","皮帶  ","鞋類  ","其他  "};
		
		public String[] default_type_2 = new String[]{"DVD類  ","上衣類  ","保養彩妝類  ","健身戶外類  ","各式鞋類  ","女裝類  ","婦幼童裝類  ","家具類  ","家電類  ","寵物類  ","居家收納類  ","廚具類  ","手機類  ","手錶類  ","文具類  ","書籍類  ","服裝配件類  ","棉被寢具類  ","汽機車用品  ","牛仔服飾類  ","玩具類  ","珠寶飾品類  ","男女皮包類  ","男裝類  ","童裝類  ","精品類  ","美體保健類  ","行李箱類  ","衛浴類  ","視聽電玩類  ","運動服飾類  ","運動用品類  ","醫療類  ","量販類  ","電腦資訊  ","食品類  ","高價品類  "};
		public String[] default_unit = new String[]{" 藏青"," 桃紅"," 白色"," 淺麻灰","亮灰色","玫瑰褐","柿子橙","霧玫瑰色"
						,"蕃茄紅 ","暗鮭紅","珊瑚紅","橙紅","赭黃","椰褐","陽橙","赭色"
						,"古董白","杏仁白","卡其色","小麥色","桃色","金菊色","琥珀色","茉莉黃"
						,"奶油色","象牙色","橄欖色","暗橄欖綠","草坪綠","暗海綠","孔雀石綠","薄荷綠"
						,"孔雀石綠","蒼色","碧藍色","綠松石色","水藍","淺藍","青色","薩克斯藍"
						,"天藍","愛麗絲藍","普魯士藍","道奇藍","鼠尾草藍","蔚藍","薰衣草紫","長春花色"
						,"藏青","藍色","紫丁香色 ","纈草紫","紫羅蘭色","三色堇紫","錦葵紫","淡紫丁香色"
						,"梅紅色","暗洋紅","玫瑰紅","淺珊瑚紅","淺粉紅","緋紅","茜紅","黑"};
		public String[] default_unit2 = new String[]{" XS"," S"," M"," L"," XL"," XXL"};
		public String[] default_product_name = new String[]{"東京著衣-花蕾絲七分蓬袖縮口上衣(3017268)  ","韓貨-長版大素T(6色)-貝思奇  ","韓貨-純棉.雙邊開衩大棉T(多色)  ","時尚編織懶人鞋  ","防水外套(紫色)  ","風采迷人印花雪紡寬袖洋裝  ","都會時尚拼接側拉鍊高跟短靴-兩色  ","粉色豹紋摺疊兩用購物袋  ","優雅蕾絲A-line洋裝喜酒宴會服(洋裝)  ","自信.條紋歐根紗拼接洋裝-黑  ","尾戒指尖戒-菱形黑鑽白鑽  ","男款PUFY吸濕快乾短袖T恤/亮白  ","輕量超撥水風衣(寶藍色)  ","紅唇化妝包  ","男款Polartec Grid吸濕排汗抗UV抗菌POLO衫-藍  ","豹紋帆布隨身包  ","男款超輕量冷黑抗UV超潑水外套/綠  ","VAPOURLT抗風外套-黃  ","SK-II  青春亮采尊榮組 -  ","英式搖滾艾薇兒風繫帶馬丁靴-兩色  ","女款DIR-RELEASE吸濕快乾T恤-粉紅  ","仿舊素色懶人鞋  ","凡爾賽炫鑽奪目內真皮魚口高跟鞋-三色  ","古典鏤空編織蕾絲內真皮平底樂福包鞋-三色  ","七分袖翻領拉鍊外套(外套)  ","金色平衡骨戒指  ","雪紡鑲鑽大尺碼超舒適熟齡媽媽套裝(上衣+褲子 、共四色)  ","可愛小貓懶人鞋  ","白麝香絲柔身體潤膚乳(400ml) /  ","Palmer's 全效修護精華油/玫瑰(150ml) /  ","海軍橫條化妝包(紅)  ","男款抗UV吸濕排汗抗皺彈性POLO衫-綠  ","V領金絲絨鑲鑽洋裝連衣裙(洋裝、共三色)  ","典雅氣質‧完美修身格A字連衣裙/白  ","柔美芬芳‧波希米亞小碎花短裙  ","Palmer's 全效修護精華油-玫瑰(150ml) -  ","多功能格層拉鍊手提肩背兩用 平板筆電包  ","英倫學院翻領繫帶高跟短靴‧白鳥麗子  ","歐系簡約商務電腦兩用包(沉穩藍)  ","女款抗UV外套-紫丁香  ","絨面材質背心連身裙  ","女款抗UV口袋衣/淺綠印花  ","時尚簡約網格懶人鞋  ","☆double c.華麗冒險☆動物紋舒莉便鞋-斑馬 41  ","男款抗UV超潑水外套-蔚藍  ","日系簡約防水尼龍配皮立體格紋雙層後背包  ","男款PolartecGrid 吸濕排汗抗UV抗菌上衣-藍  ","MAKE UP FOR EVER 專業美肌粉餅(10g)多色可選 #115  ","男款超輕量冷黑抗UV超潑水外套-綠  ","天然綠東陵石耳環  ","繡花鑲鑽假兩件針織上衣(上衣、共五色)  ","浪漫女神.圓領碎花雪紡裙-紫  ","花樣亮片假兩件式上衣熟齡媽媽  ","唯美蕾絲潑墨套裝  ","神秘女人.歐根紗蕾絲透膚洋裝-紅  ","神秘女人.歐根紗蕾絲透膚洋裝/紅  ","FOR BELOVED ONE寵愛之名 三分子玻尿酸藍銅精華(101ml) /  ","時尚女性菱格肩背包  ","務型輕薄電腦後背包  ","凡爾賽皇宮彩色瑪瑙珠貝耳環/神秘咖  ","男款抗UV超潑水外套/蔚藍  ","LANEIGE 蘭芝 牛奶光潤色CC霜SPF36/PA++#2自然色Pure Beige(40ml)...  ","通勤族最愛變裝秀套裝  ","Naturehike TPU超輕量 護頸U型充氣枕 超值2入  ","名人愛犬系列購物袋  ","ESTEE LAUDER雅詩蘭黛 經典明星年度超值組 -  ","女款吸濕快乾短袖T恤/薄荷綠  ","Crabtree & Evelyn 瑰珀翠 經典護手霜限量禮盒(25gx6)-軟管新包裝 -  ","男款吸濕快乾短袖POLO衫-牛仔藍  ","阿財長頸鹿連帽長袖T  ","L.A.-LOGO購物袋  ","美式簡約休閒洋裝 (共1色)  ","Unisysh 丹寧素色懶人鞋  ","V領修身長袖上衣(上衣)  ","女款SUPPLEX 吸濕快乾長袖長版襯衫-水藍  ","凡爾賽炫鑽奪目  ","小香風花朵洋裝  ","A.D.M.J 鞣製牛革L型中夾 (ORANGE)  ","吉普賽花園耳環項鍊套組~柔情藍  ","LANCOME 蘭蔻 時光凍齡調理組 /  ","迷你蜂巢金手鍊   ","輕量反光防水外套 Thor2.3(琉璃藍)  ","時尚巴黎風格 直立多層置物斜/側背包 輕量防潑水-黑  ","輕柔毛巾布化妝包  ","V領修身長袖上衣(上衣)  ","double c.緋聞女王 鉚釘羅馬高跟涼鞋-粉-EU39  ","古典鏤空編織蕾絲內真皮平底樂福包鞋/三色  ","Metropolis 防水外套(藍色)  ","簡約綁帶懶人鞋  ","優雅女伶內真皮質感高跟包鞋-兩色  ","圓領針織長襟蕾絲上衣(上衣、共3色)  ","ESTEE LAUDER雅詩蘭黛 白金級極緻珍璽甦活眼霜(5ml)x2 -  ","荷葉袖T恤上衣  ","FOR BELOVED ONE寵愛之名 杏仁酸亮白煥膚乳液(70ml)X2入-期效201511 -  ","數位相機包-PINK  ","ORIGINS 品木宣言 奇蹟+泥娃娃+一飲而盡保濕面膜(75mlX3入) -  ","THE BODY SHOP 茶樹淨膚除點隨身棒(2.5ml) -  ","正韓麻編圈狀精品高跟鞋  ","GEAR 街頭潮流SHIMANO 21速700C彎把公路車_GR2  ","都會時尚拼接側拉鍊高跟短靴/兩色  ","典雅氣質‧完美修身格A字連衣裙-白  ","清新簡約.顯瘦碎花洋裝-藍  ","女款抗UV口袋衣-淺綠印花  ","紐約簡約時尚素面亞麻套裝  ","double c.韓風精靈 韓版帆布綁帶休閒鞋-白-EU38  ","LANCOME 蘭蔻 超緊顏5D彈力霜(15ML)x2 -  ","南韓手感縫線設計一字厚底涼鞋-二色  ","日系簡約防水尼龍多口袋後背包  ","double c.森林精靈  擦色流蘇坡跟涼鞋-雲朵白-EU38  ","double c.微糖學院　擦皮綁帶莫卡辛-焦糖棕 39  ","優雅女伶內真皮質感高跟包鞋/兩色  ","男款PUFY吸濕快乾短袖T恤-亮白  ","男款奈米防污抗UV外套-深藍  ","超手感V型繡花上衣(上衣、共五色)  ","男款PolartecGrid 吸濕排汗抗UV抗菌上衣/藍  ","星星搖滾派對化妝包  ","美式個性格調厚底馬汀短靴-三色  ","經典簡約超輕薄風衣外套(風衣)  ","南韓手感縫線設計一字厚底涼鞋/二色  ","典雅氣質•完美修身格A字連衣裙/白  ","LANCOME 蘭蔻 時光凍齡調理組 -  ","手工波浪紋-單環戒指  ","南洋風印花帆布手拿包  ","男款SUPPLEX 吸濕快乾長袖襯衫-藍  ","男款吸濕快乾短袖POLO衫/牛仔藍  ","男款PUFY吸濕快乾短袖POLO衫-白色  ","襯衫領假兩件針織上衣(上衣、共五色)  ","星星搖滾派對化妝包   ","清新簡約.顯瘦碎花洋裝/藍  ","英式搖滾艾薇兒風繫帶馬丁靴/兩色  ","philosophy 微導煥膚組 [微導煥膚凝露60ml+微導煥膚胜?C去角質60g] -  ","經典中性側拉鍊多扣環設計高跟短靴-黑色  ","假兩件雪紡短袖中大尺碼熟齡婆婆媽媽喜宴褲套裝(上衣+褲子 、共四色)  ","女款抗UV外套淺卡其  ","蕾絲花朵懶人鞋  ","男款抗UV吸濕快乾長袖襯衫  ","FOR BELOVED ONE寵愛之名 杏仁酸亮白煥膚乳液(70ml)X2入/期效201511 /  ","個性拼接懶人鞋  ","MAKE UP FOR EVER 第一步奇肌對策(30ml)/多款可選 #7/完美潤色  ","凡爾賽皇宮珠貝耳環  ","金屬皮帶飾扣綁帶造型馬丁中筒靴-兩色  ","【牡丹紅服飾】棉麻舒適中國民族風修身上衣(青花/c114)  ","FOR BELOVED ONE寵愛之名 三分子玻尿酸藍銅精華(101ml) -  ","針織鑲鑽假兩件上衣(上衣、共三色)  ","凡爾賽皇宮彩色瑪瑙珠貝耳環-神秘咖  ","紳士輕旅專用可掛式摺疊收納尼龍手提行李包/紳士黑  ","牛仔小磨白圓領洋裝  ","double c.緋聞女王　羅馬繞帶及膝靴型涼鞋 -天后黑 38  ","紳士輕旅專用可掛式摺疊收納尼龍手提行李包-紳士黑  ","LANEIGE 蘭芝 水柔光持色粉餅SPF32/PA+++(9g) 多色可選 #2  ","日韓潮流亮彩雙磁扣口袋後背包  ","自信.條紋歐根紗拼接洋裝/黑  ","男孩率性風仿麂絨內真皮懶人鞋/藍色  ","螢光滾邊條紋化妝包  ","FELLOW DUO 浸泡式咖啡壺(灰)  ","partysu質感棉麻寬鬆V領泡泡袖上衣  ","經典中性 側拉鍊多扣環設計高跟短靴/黑色  ","濃縮洗衣精2250mlX4瓶(玫瑰甜心香氛)  ","double c.經典復刻　全真皮莫卡辛豆豆鞋-個性灰-EU40  ","單環戒指-925純銀  ","美式個性格調厚底馬汀短靴/三色  ","Scotchlitr反光抗UV超潑水輕量外套-綠格紋  ","金色尾戒/指尖戒  ","THE BODY SHOP 茶樹淨膚除點隨身棒(2.5ml) /  ","男款PUFY吸濕快乾短袖POLO衫/白色  ","Crabtree & Evelyn 瑰珀翠 薰衣草護手霜(100g)/軟管新包裝 /  ","古典玫瑰花懶人鞋  ","double c.韓風精靈 韓版內增高綁帶休閒鞋-白-36  ","philosophy 微導煥膚組 [微導煥膚凝露60ml+微導煥膚胜肽C去角質60g] -  ","男款COLDBLACK冷黑抗UV外套/淺灰  ","男款奈米防污抗UV外套/深藍  ","Albion 艾倫比亞 健康化妝水(12ml)X5入 -  ","輕量透氣防水外套 Ranger3.4(陽光橘)  ","趣味星空懶人鞋  ","【即期品】FOR BELOVED ONE寵愛之名 杏仁酸亮白煥膚乳液(70ml)X2入  ","Albion 艾倫比亞 健康化妝水(12ml)X5入 /  ","男款SUPPLEX 吸濕快乾長袖襯衫/藍  ","Crabtree & Evelyn 瑰珀翠 經典護手霜限量禮盒(25gx6)/軟管新包裝 /  ","凡爾賽炫鑽奪目內真皮魚口高跟鞋/三色  ","LANEIGE 蘭芝 牛奶光潤色CC霜SPF36/PA++#2自然色Pure Beige(40ml)  ","《THE BODY SHOP》白麝香絲柔身體潤膚乳(400ml) -  ","Crabtree & Evelyn 瑰珀翠 薰衣草護手霜(100g)-軟管新包裝 -  ","double c.甜點派對 馬卡龍系繽紛娃娃鞋-薄荷綠-EU36  ","男款COLDBLACK冷黑抗UV外套-淺灰  ","SKII R.N.A. 超肌能緊緻活膚霜 100g+青春露2ml*50  ","紳士輕旅專用可掛式摺疊收納尼龍後背包  ","女款DIR/RELEASE吸濕快乾T恤/粉紅  ","double c.微糖學院　雙繞帶尖頭低跟鞋 - 紫芋 38  ","MAKE UP FOR EVER 第一步奇肌對策(30ml)-多款可選 #7-完美潤色  ","男款抗UV吸濕排汗抗皺彈性POLO衫/綠  ","ORIGINS 品木宣言 奇蹟+泥娃娃+一飲而盡保濕面膜(75mlX3入) /  ","L.A./LOGO購物袋  ","男孩率性風仿麂絨內真皮懶人鞋-藍色  ","騎士風側拉鍊復古扣環高跟短靴-兩色  ","多功能格層拉鍊手提肩背兩用?平板筆電包  ","philosophy 微導煥膚組 [微導煥膚凝露60ml+微導煥膚胜肽C去角質60g] /  ","手工波浪紋/單環戒指  ","浪漫女神.圓領碎花雪紡裙/紫  ","單環戒指/925純銀  ","優雅蕾絲A/line洋裝喜酒宴會服(洋裝)  ","白麝香絲柔身體潤膚乳(400ml) -  ","男款Polartec Grid吸濕排汗抗UV抗菌POLO衫/藍  ","騎士風側拉鍊復古扣環高跟短靴/兩色  ","VAPOURLT抗風外套/黃  ","OSIM uPhoria Warm 暖足樂(黑灰色)  ","Rock Gym八合一搖滾運動機  ","時尚巴黎風格 直立多層置物斜/側背包 輕量防潑水/黑  ","LANCOME 蘭蔻 超緊顏5D彈力霜(15ML)x2 /  ","女款吸濕快乾短袖T恤-薄荷綠  ","尾戒指尖戒/菱形黑鑽白鑽  ","ESTEE LAUDER雅詩蘭黛 經典明星年度超值組 /  ","女款SUPPLEX 吸濕快乾長袖長版襯衫/水藍  ","金屬皮帶飾扣綁帶造型馬丁中筒靴/兩色  ","女款抗UV外套/紫丁香  ","金色尾戒-指尖戒  ","ESTEE LAUDER雅詩蘭黛 白金級極緻珍璽甦活眼霜(5ml)x2 /  ","柔美芬芳•波希米亞小碎花短裙  ","SK/II  青春亮采尊榮組 /  ","數位相機包/PINK  ","英倫學院翻領繫帶高跟短靴•白鳥麗子  "};
	}
	///////////以下全部都是複製customer.java的//////////////
	String err="";
	@SuppressWarnings("serial")
	public class CustomerVO implements java.io.Serializable {
		private String customer_id;
		private String group_id;
		private String name;
		private String address;
		private String phone;
		private String mobile;
		private String email;
		private String post;
		private String customerClass;
		private String memo;
		private String message;// 此參數用來存放錯誤訊息

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getCustomer_id() {
			return customer_id;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPost() {
			return post;
		}

		public void setPost(String post) {
			this.post = post;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getCustomerClass() {
			return customerClass;
		}

		public void setCustomerClass(String customerClass) {
			this.customerClass = customerClass;
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

		@Override
		public void deleteCustomer(String customer_id, String user_id) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
        	//System.out.println("gid: "+group_id);
//        	System.out.println("wspath: "+wsPath);
        	//System.out.println("url: "+url);
        	
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
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (UnsupportedOperationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
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
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (UnsupportedOperationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
        	return voFromJson;
		}
	}
}
