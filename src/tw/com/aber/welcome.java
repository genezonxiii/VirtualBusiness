
package tw.com.aber;

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
@SuppressWarnings("serial")

public class welcome extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if("current_login".equals( request.getParameter("action") )){
			String group_id = (String) request.getSession().getAttribute("group_id");
			if(group_id == null){
				response.getWriter().write("false");
			}else{
				response.getWriter().write("true");
			}
			return ;
		}
		
		if(request.getSession().getAttribute("group_id")==null){System.out.println("no_session");return;}
		int sale_data=0,ship_data=0;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		if("searh".equals(action)){
			String group_id = request.getSession().getAttribute("group_id").toString();
			group_id=(group_id==null||group_id.length()<3)?"UNKNOWN":group_id;
			//String user_id = request.getSession().getAttribute("user_id").toString();
	//		String time1 = request.getParameter("time1");
	//		time1=(time1.length()<3)?"1999/12/31":time1;
	//		String time2 = request.getParameter("time2");
	//		time2=(time2.length()<3)?"2300/12/31":time2;
	//		//System.out.println("from "+time1+" to "+time2);
			//###########################################
			String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
					+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
			String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
				//java.util.Date now = new java.util.Date();
				//java.sql.Date sqlDate = new java.sql.Date(now.getTime());
				java.sql.Date from_date= new java.sql.Date(new SimpleDateFormat("yyyy/MM/dd").parse(today).getTime());
				java.sql.Date till_date= new java.sql.Date(new SimpleDateFormat("yyyy/MM/dd").parse(today).getTime());
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("call sp_shipper_count(?,?,?)");
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ship_data=rs.getInt("COUNT(*)");
				}
				
				pstmt = con.prepareStatement("call sp_select_sale_bytranslistdate(?,?,?)");
				pstmt.setString(1,group_id);
				pstmt.setDate(2,from_date);
				pstmt.setDate(3,till_date);
				rs = pstmt.executeQuery();
				int count;
			    if (rs.last()){
			       count = rs.getRow();
			    }else{
			       count = 0;
			    }
			   //int k=0;
			    rs.beforeFirst();
//				while (rs.next()) {
//					sale_data=rs.getInt("COUNT(*)");
//				}
			    sale_data=count;
				String tmp= "{\"ship_data\": [" + ship_data + "] , \"sale_data\": ["+sale_data+"] }";
				response.getWriter().write(tmp);
				
			} catch (Exception se) {System.out.println("ERROR WITH: "+se);}
		}
		return ;
	}
}
