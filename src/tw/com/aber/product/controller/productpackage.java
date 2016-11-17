
package tw.com.aber.product.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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

import com.google.gson.Gson;

import java.util.Date; 
import java.text.SimpleDateFormat;
//import org.json.simple.JSONObject;
@SuppressWarnings("serial")

public class productpackage extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		group_id=(group_id==null||group_id.length()<3)?"UNKNOWN":group_id;
		String action= request.getParameter("action");
		ProductPackageDAO dao= new ProductPackageDAO();
		List<ProductPackageVO> list = new ArrayList<ProductPackageVO>();
//		String uuid = UUID.randomUUID().toString();
//		response.getWriter().write(uuid);

		if("search".equals(action)){
			String word= request.getParameter("word");
			word=word==null?"":word;
			ProductVO[] parents = dao.searchpackages(group_id,word);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
			//int i ;
			//System.out.println("word: "+word);
			//List<ProductPackageVO> packages;// = new ArrayList<ProductPackageVO>(); 
			//Object tmp2[] = new Object[parents.length];
			//String tmp[] = new String[parents.length];
			//for(i=0;i<parents.length;i++){
				//packages = dao.searhDB(parents[i].product_id);
				//tmp2[i] = gson.toJson(packages);
				//tmp[i] = gson.toJson(packages);
				//System.out.println(tmp2[i]);
				//parents[i].content = tmp[i];
			//}
			//String jsonstr= request.getParameter("str");
			//ProductPackageVO[] productpackage = gson.fromJson(jsonstr, ProductPackageVO[].class);
			//for(i=0;i<productpackage.length;i++){
				//System.out.println(productpackage[i].quantity+" "+productpackage[i].package_desc);
			//}
			//list=dao.searhDB(group_id);
			return ;
		}else if("insert".equals(action)){
			String c_package_id= request.getParameter("c_package_id");
			String supply_name= request.getParameter("supply_name");
			String package_name= request.getParameter("package_name");
			String price= request.getParameter("price");
			String package_type= request.getParameter("package_type");
			String barcode= request.getParameter("barcode");
			String description= request.getParameter("description");
			dao.insertpackages(group_id,c_package_id,package_name,supply_name,price,package_type,barcode,description,user_id);
			ProductVO[] parents = dao.searchpackages(group_id,"");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}else if("update".equals(action)){
			String package_id= request.getParameter("package_id");
			String c_package_id= request.getParameter("c_package_id");
			String supply_name= request.getParameter("supply_name");
			String package_name= request.getParameter("package_name");
			String price= request.getParameter("price");
			String package_type= request.getParameter("package_type");
			String barcode= request.getParameter("barcode");
			String description= request.getParameter("description");
			dao.updatepackages(package_id,group_id,c_package_id,package_name,supply_name,price,package_type,barcode,description,user_id);
			ProductVO[] parents = dao.searchpackages(group_id,"");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}else if("delete".equals(action)){
			String package_id= request.getParameter("package_id");
			dao.deletepackages(package_id,user_id);
			ProductVO[] parents = dao.searchpackages(group_id,"");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}else if("search_detail".equals(action)){
			String package_id= request.getParameter("package_id");
			//System.out.println(package_id);
			ProductVO[] parents = dao.searchpackagesdetail(package_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}else if("insert_detail".equals(action)){
			String package_id= request.getParameter("package_id");
			String product_id= request.getParameter("product_id");
			String quantity= request.getParameter("quantity");
			String package_desc= request.getParameter("package_desc");
//			product_id="0a8b87bb-a256-11e6-922d-005056af760c";
			dao.insertpackagesdetail(package_id,product_id,quantity,package_desc);
			ProductVO[] parents = dao.searchpackagesdetail(package_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}else if("update_detail".equals(action)){
			String package_id= request.getParameter("package_id");
			String parent_id= request.getParameter("package_id2");
			String product_id= request.getParameter("product_id");
			String quantity= request.getParameter("quantity");
			String package_desc= request.getParameter("package_desc");
			//System.out.println(package_id+" "+parent_id+" "+product_id+" "+quantity+" "+package_desc);
			dao.updatepackagesdetail(package_id,parent_id,product_id,quantity,package_desc);
			ProductVO[] parents = dao.searchpackagesdetail(parent_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}else if("delete_detail".equals(action)){
			String package_id= request.getParameter("package_id");
			String parent_id= request.getParameter("parent_id");
			dao.deletepackagesdetail(package_id);
			ProductVO[] parents = dao.searchpackagesdetail(parent_id);
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(parents));
		}
//		Gson gson = new Gson();
//		String jsonStrList = gson.toJson(list);
		//System.out.println("json: "+ jsonStrList);
//		response.getWriter().write(jsonStrList);
		return;
		//###########################################
	}

	/************************* 對應資料庫表格格式 **************************************/
	public class ProductVO{
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
	public class ProductPackageVO  {
		public String package_id;
		public String parent_id;
		public String product_id;
		public String quantity;
		public String package_desc;
	}
	/*************************** 操作資料庫 ****************************************/
	class ProductPackageDAO  {
		// 會使用到的Stored procedure sp_selectall_product  LIKE CONCAT('%', p_c_product_id , '%')
		private static final String sp_select_package = "select * from tb_product where (package = true and group_id = ?) and (product_name LIKE CONCAT('%', ?, '%') or c_product_id LIKE CONCAT('%', ?, '%'))  order by create_time desc";
		private static final String sp_select_package_detail = "select * from tb_product_package INNER JOIN tb_product ON tb_product_package.product_id = tb_product.product_id  where parent_id = ? ";
		private static final String sp_insert_package = "call sp_insert_product(?,?,?,'','',?,'',0,?,0,'','',?,?,?,0,1)";
		private static final String sp_insert_package_detail = "call sp_insert_package (?,?,?,?)";
		private static final String sp_update_package = "call sp_update_product(?,?,?,?,'','',?,'',0,?,0,'','',?,?,?,1)";
		private static final String sp_update_package_detail = "call sp_update_package (?,?,?,?,?)";
		private static final String sp_delete_package = "call sp_del_product (?,?)";
		private static final String sp_delete_package2= "call sp_del_package (?)";
		private static final String sp_delete_package_detail = "call sp_del_package_detail (?)";
//		sp_del_package ()
//		sp_del_package_detail ()
//		sp_insert_package (,,,)
//		sp_update_package (,,,,)
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public void deletepackagesdetail(String package_id){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_package_detail);
				pstmt.setString(1,package_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		public void updatepackagesdetail(String package_id,String package_id2, String product_id, String quantity, String package_desc){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_package_detail);
				pstmt.setString(1,package_id);
				pstmt.setString(2,package_id2);
				pstmt.setString(3,product_id);
				pstmt.setString(4,quantity);
				pstmt.setString(5,package_desc);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		public void insertpackagesdetail(String package_id, String product_id, String quantity, String package_desc){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_package_detail);
				pstmt.setString(1,package_id);
				pstmt.setString(2,product_id);
				pstmt.setString(3,quantity);
				pstmt.setString(4,package_desc);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		public ProductVO[] searchpackagesdetail(String package_id) {
			ProductVO[] packages=null;
			int i=0;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_package_detail);
				pstmt.setString(1,package_id);
				rs = pstmt.executeQuery();
				int count;
			    if (rs.last()){
			       count = rs.getRow();
			    }else{
			       count = 0;
			    }
			    rs.beforeFirst();
			    packages= new ProductVO[count];
			    while (rs.next()) {
			    	packages[i]=new ProductVO();
			    	packages[i].product_id=rs.getString("product_id");
			    	packages[i].group_id=rs.getString("group_id");
			    	packages[i].c_product_id=rs.getString("c_product_id");
			    	packages[i].product_name=rs.getString("product_name");
			    	packages[i].supply_id=rs.getString("supply_id");
			    	packages[i].supply_name=rs.getString("supply_name");
			    	packages[i].type_id=rs.getString("type_id");
			    	packages[i].unit_id=rs.getString("unit_id");
			    	packages[i].cost=rs.getString("cost");
			    	packages[i].price=rs.getString("price");
			    	packages[i].keep_stock=rs.getString("keep_stock");
			    	packages[i].photo=rs.getString("photo");
			    	packages[i].photo1=rs.getString("photo1");
			    	packages[i].description=rs.getString("description");
			    	packages[i].barcode=rs.getString("barcode");
			    	packages[i].ispackage=rs.getString("package");
			    	
			    	packages[i].package_id=rs.getString("package_id");
			    	packages[i].parent_id=rs.getString("parent_id");
			    	packages[i].quantity=rs.getString("quantity");
			    	packages[i].package_desc=rs.getString("package_desc");
			    	i++;
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return packages;
		}
		//以上是package_detail四指令		
		//以下是package四指令
		public void deletepackages(String package_id, String user_id){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_package);
				pstmt.setString(1,package_id);
				pstmt.setString(2,user_id);
				pstmt.executeQuery();
				
				con=null;
				pstmt=null;
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_package2);
				pstmt.setString(1,package_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		public void updatepackages(String package_id,String group_id, String c_package_id, String package_name, String supply_name, String price, String package_type, String barcode, String description, String user_id){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_package);
				pstmt.setString(1,package_id);
				pstmt.setString(2,group_id);
				pstmt.setString(3,c_package_id);
				pstmt.setString(4,package_name);
//				pstmt.setString(5,supply_name);
				pstmt.setString(5,package_type);
				pstmt.setString(6,price);
				pstmt.setString(7,description);
				pstmt.setString(8,barcode);
				pstmt.setString(9,user_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return ;
		}
		
		public void insertpackages(String group_id, String c_package_id, String package_name, String supply_name, String price, String package_type, String barcode, String description, String user_id){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				//System.out.println(c_package_id+supply_name+package_name+price+package_type+barcode+description);
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_package);
				pstmt.setString(1,group_id);
				pstmt.setString(2,c_package_id);
				pstmt.setString(3,package_name);
//				pstmt.setString(4,supply_name);
				pstmt.setString(4,package_type);
				pstmt.setString(5,price);
				pstmt.setString(6,description);
				pstmt.setString(7,barcode);
				pstmt.setString(8,user_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return ;
		}
		public ProductVO[] searchpackages(String group_id, String word) {
			ProductVO[] packages=null;
			int i=0;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_package);
				pstmt.setString(1,group_id);
				pstmt.setString(2,word);
				pstmt.setString(3,word);
				rs = pstmt.executeQuery();
				int count;
			    if (rs.last()){
			       count = rs.getRow();
			    }else{
			       count = 0;
			    }
			    rs.beforeFirst();
			    packages= new ProductVO[count];
			    while (rs.next()) {
			    	packages[i]=new ProductVO();
			    	packages[i].product_id=rs.getString("product_id");
			    	packages[i].group_id=rs.getString("group_id");
			    	packages[i].c_product_id=rs.getString("c_product_id");
			    	packages[i].product_name=rs.getString("product_name");
			    	packages[i].supply_id=rs.getString("supply_id");
			    	packages[i].supply_name=rs.getString("supply_name");
			    	packages[i].type_id=rs.getString("type_id");
			    	packages[i].unit_id=rs.getString("unit_id");
			    	packages[i].cost=rs.getString("cost");
			    	packages[i].price=rs.getString("price");
			    	packages[i].keep_stock=rs.getString("keep_stock");
			    	packages[i].photo=rs.getString("photo");
			    	packages[i].photo1=rs.getString("photo1");
			    	packages[i].description=rs.getString("description");
			    	packages[i].barcode=rs.getString("barcode");
			    	packages[i].ispackage=rs.getString("package");
			    	i++;
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return packages;
		}
	}
}
