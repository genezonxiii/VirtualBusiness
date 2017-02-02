
package tw.com.aber.chart;

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

public class bestsale extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String group_id = request.getSession().getAttribute("group_id").toString();
		group_id=(group_id==null||group_id.length()<3)?"KNOWN":group_id;
		
		String time1 = request.getParameter("time1");
		time1=(time1==null || time1.length()<3)?"1999-12-31":time1;
		String time2 = request.getParameter("time2");
		time2=(time2==null || time2.length()<3)?"2300-12-31":time2;
		
		String action = request.getParameter("action");
		if("search_best_sale".equals(action)){
			try {
				List<BestsaleVO> list;
				BestsaleDAO bestsaledao = null;
				java.sql.Date from_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time1).getTime());
				java.sql.Date till_date= new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(time2).getTime());
				String order_source=request.getParameter("ordersource");
				bestsaledao = new BestsaleDAO();
				if(order_source.length()<1){
					list = bestsaledao.bestsale(group_id, from_date, till_date);
				}else{
					list = bestsaledao.bestsalebyordersource(group_id, order_source, from_date, till_date);
				}
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {System.out.println("Error with time parse. :"+e);}
		}
	}
	

	/************************* 對應資料庫表格格式 **************************************/

	public class BestsaleVO implements java.io.Serializable {
		int quantity;
		String product_name;
	}
	/*************************** 操作資料庫 ****************************************/
	class BestsaleDAO{
		// 會使用到的Stored procedure
		private static final String sp_saleproduct_top10 = "call sp_saleproduct_top10(?,?,?)";
		private static final String sp_saleproduct_channel  = "call sp_saleproduct_channel (?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public List<BestsaleVO> bestsalebyordersource(String group_id,String order_source, java.sql.Date from_date,java.sql.Date till_date) {
			List<BestsaleVO> list=new ArrayList<BestsaleVO>();
			BestsaleVO bestsale = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_saleproduct_channel);
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				pstmt.setString(4,order_source);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					bestsale= new BestsaleVO();
					bestsale.quantity=rs.getInt("quantity");
					bestsale.product_name=rs.getString("product_name");
					list.add(bestsale);
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}
		public List<BestsaleVO> bestsale(String group_id, java.sql.Date from_date,java.sql.Date till_date) {
			List<BestsaleVO> list=new ArrayList<BestsaleVO>();
			BestsaleVO bestsale = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_saleproduct_top10);
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					bestsale= new BestsaleVO();
					bestsale.quantity=rs.getInt("quantity");
					bestsale.product_name=rs.getString("product_name");
					list.add(bestsale);
				}
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}
	}
}
