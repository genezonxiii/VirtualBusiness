package tw.com.aber;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

//@SuppressWarnings("serial")
public class forget extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		ForgetDAO forgetDAO = new ForgetDAO();
		//寫3個action
		//user_exist O一個給email去找user_id 然後發信
		//一個check 有沒有這個user_id
		//一個不看session的更新密碼
		if ("user_exist".equals(action)) {
			String email = request.getParameter("email");
			String url = request.getRequestURL().toString().replace("forget.do","");
			String tmp=forgetDAO.user_exist(email,url);
			
			if("exist".equals(tmp)){
				response.getWriter().write("exist");
			}else{
				response.getWriter().write(tmp);
			}
		}
//		if ("send_mail".equals(action)) {
//			response.getWriter().write("寄信");
//		}
		if ("check_user".equals(action)) {
			String uid = new String(Base64.decodeBase64(request.getParameter("uid")));
			String tmp = forgetDAO.check_user(uid);
			if("exist_uid".equals(tmp)){
				response.getWriter().write("yes");//"檢查是否合法uid");
			}else{
				response.getWriter().write("no");
			}
		}
		if ("update_pwd".equals(action)) {
			String uid = new String(Base64.decodeBase64(request.getParameter("uid")));
			String pwd = request.getParameter("pwd");
			forgetDAO.change_pwd(uid,pwd);
			response.getWriter().write("更新");
		}
		int i=0;if(i==0)return;
	}

	/*************************** 操作資料庫 ****************************************/
	class ForgetDAO{

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String email= getServletConfig().getServletContext().getInitParameter("email");
		private final String emailpwd= getServletConfig().getServletContext().getInitParameter("emailpwd");
		private final String smtphost= getServletConfig().getServletContext().getInitParameter("smtphost");
		
		
		public String user_exist(String email,String url) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String ret="";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("SELECT user_name, user_id FROM tb_user where email= ?");
				pstmt.setString(1,email);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ret=this.sendEmail(email,rs.getString("user_name"),rs.getString("user_id"),url);
					if("success".equals(ret)){ret="exist";}
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			}
			return ret;
		}
		public String sendEmail(String to,String name,String user_id,String url)
	    {
			//還沒測試過在aber上行不行得通?
	        final String username = email;
	        final String password = emailpwd;
	        final String smtp = smtphost;
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "false");
	        props.put("mail.smtp.host",smtp); 
	        props.put("mail.smtp.port", "25");

	        Session session = Session.getInstance(props,//null);
	          new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	            	return new PasswordAuthentication(username,new String(Base64.decodeBase64((new StringBuffer(password).reverse().toString()).getBytes())));
	            	//return new PasswordAuthentication(username,password);
	            }
	          });

	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(username));
	            message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(to));
	            message.setSubject("智慧電商 - 更改密碼");
	            
	          //##############################################################
	            MimeBodyPart textPart = new MimeBodyPart();
	            StringBuffer html = new StringBuffer();
	            
	            html.append("<table style='margin:0 auto;padding:0;width:600px' align='center' border='0' ><tr><td>"
	            		+"<h2>親愛的"+name+"您好:</h2></td></tr>"
	            		+"<tr><td style='padding:0 200px;'><div style='background:#1f4f82;padding:13px 0px;width:200px; margin:0 auto; ' align='center'>"
	            		+"<a href='"+url+"forget.jsp?uid="+Base64.encodeBase64String(user_id.getBytes())+"'>"
	            		+"<font color=white>更改密碼 »</font></a></div></td></tr>"
	            		+"<tr><td>&nbsp;<br>您收到這封電子郵件是因為最近忘記密碼並請求更改。如果您並未請求此動作，請<a href='"+url+"msgboard.html'>提交協助要求</a>或發電子郵件至 <a href='mailto:pscaber@cloud.pershing.com.tw'>pscaber@cloud.pershing.com.tw</a>。</td><tr>"
	            		+"<tr><td><h4><br>感謝您!<br>智慧電商團隊</h4></td><tr></table>");
	            textPart.setContent(html.toString(), "text/html; charset=UTF-8");
	            //System.out.println(url+"forget.jsp?uid="+Base64.encodeBase64String(user_id.getBytes()));
	            Multipart mmp = new MimeMultipart();
	            mmp.addBodyPart(textPart);
	            message.setContent(mmp);
	          //##############################################################
	            Transport.send(message);
	            return "success";
	        } 
	        catch (MessagingException e) 
	        {
	            //throw new RuntimeException(e);
	            return "Send Message Error?";
	        }catch (Exception e) 
	        {
	            //throw new RuntimeException(e);
	            return "Connect error.";
	        }
	    }
		public String check_user(String uid) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String ret="";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("SELECT user_name, user_id FROM tb_user where user_id = ?");
				pstmt.setString(1,uid);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ret="exist_uid";
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			}
			return ret;
		}
		public void change_pwd(String uid,String pwd) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			//String ret="";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("call sp_update_password (?,?)");
				pstmt.setString(1,uid);
				pstmt.setString(2,pwd);
				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					ret="exist_uid";
//				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			}
			//return ret;
		}
	}

}
