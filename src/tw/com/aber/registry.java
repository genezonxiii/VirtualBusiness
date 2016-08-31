package tw.com.aber;

import java.io.BufferedReader;
import java.io.File;
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

import tw.com.aber.salechart.SalechartVO;
import tw.com.aber.salechart.Salereport_interface;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("serial")

public class registry extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		if("send_mail".equals(action)){
			String to = request.getParameter("to");
			String user_id = new String(Base64.encodeBase64String(request.getParameter("user_id").getBytes()));
			String name = request.getParameter("name");
			String url = request.getRequestURL().toString().replace("registry.do","");
			
			response.getWriter().write( sendEmail(to,name,user_id,url));
		}
		if("registry_confirm".equals(action)){
			Register reg = new Register();
			String reg_id = new String(Base64.decodeBase64(request.getParameter("regid")));
			reg.registry_confirm(reg_id);
			response.getWriter().write("success");
		}
		if("registry".equals(action)){
			String uninumber = request.getParameter("uninumber");
			String corporation = request.getParameter("corporation");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String pwd = request.getParameter("pwd");
			Register reg = new Register();
			
			response.getWriter().write(reg.registry(uninumber,corporation,name,email,pwd));
		}
	}
	
	class Register {
		// 會使用到的Stored procedure
		private static final String sp_register = "call sp_register (?,?,?,?,?,?)";
		private static final String sp_register_confirm = "call sp_register_confirm (?)";
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		public Register(){}
		public void registry_confirm(String registry_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_register_confirm);
				pstmt.setString(1,registry_id);
				pstmt.executeQuery();
			} catch (SQLException se) {System.out.println("ERROR WITH: "+se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
		}
		public String registry(String unicode, String group_name, String name, String email, String pwd) {
			Connection con = null;
			CallableStatement cs = null;
			String rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_register);
				cs.registerOutParameter(6, Types.VARCHAR);
				cs.setString(1,unicode);
				cs.setString(2,group_name);
				cs.setString(3,name);
				cs.setString(4,email);
				cs.setString(5,pwd);
				cs.execute();
				rs = cs.getString(6);
				//rs = Base64.encodeBase64String(rs.getBytes());
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			} finally {
				if (cs != null) {
					try {
						cs.close();
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
			return rs;
		}
	}
	
	public static String sendEmail(String to,String name,String user_id,String url)
    {
		//還沒測試過在aber上行不行得通?
		//final String username = "pscaber@cloud.pershing.com.tw";
        //final String password = "==AMzgDMucmbph2cyVGU";
        final String username = "10506002@pershing.com.tw";
        final String password = "ykTNycTNjhnW";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", "mail.pershing.com.tw");
        //props.put("mail.smtp.host", "cloud-pershing-com-tw.mail.protection.outlook.com");
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
            message.setFrom(new InternetAddress("benchen@pershing.com.tw"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
            message.setSubject("智慧電商 - 驗證您的電子郵件");
            
          //##############################################################
            MimeBodyPart textPart = new MimeBodyPart();
            StringBuffer html = new StringBuffer();
            
            html.append("<table style='margin:0 auto;padding:0;width:600px' align='center' border='0' ><tr><td>"
            		+"<h2>親愛的"+name+"您好:</h2></td></tr><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;感謝您建立智慧電商平台帳戶。請登入以存取智慧電商平台線上應用軟體、服務、支援、報表、線上學院等項目。</td></tr>"
            		+"<tr><td style='padding:0 200px;'>&nbsp;<br><div style='background:#1f4f82;padding:13px 0px;width:200px; margin:0 auto; ' align='center'>"
            		//+"<a href='http://a-ber.com.tw/VirtualBusiness/registry.jsp?uid="+user_id+"'>"
            		//+"<a href='http://localhost:8081/VirtualBusiness/registry.jsp?uid="+user_id+"'>"
            		+"<a href='"+url+"registry.jsp?regid="+user_id+"'>"
            		+"<font color=white>驗證電子郵件地址 »</font></a></div></td></tr>"
            		+"<tr><td>&nbsp;<br>您收到這封電子郵件是因為最近有一個帳戶使用您的電子郵件地址建立。如果您並未建立此帳戶，請<a href='"+url+"msgboard.html'>提交協助要求</a>或發電子郵件至 <a href='mailto:pscaber@cloud.pershing.com.tw'>pscaber@cloud.pershing.com.tw</a>。</td><tr>"
            		+"<tr><td><h4><br>感謝您!<br>智慧電商團隊</h4></td><tr></table>");
            textPart.setContent(html.toString(), "text/html; charset=UTF-8");
            Multipart mmp = new MimeMultipart();
            mmp.addBodyPart(textPart);
            message.setContent(mmp);
          //##############################################################
//            message.setText("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>認證信</title></head><body>"
//            		+"<table style='margin:0 auto;padding:0;max-width:612px' align='center' border='0' width='100%'><tr><td>"
//            		+"親愛的"+name+"您好:</td></tr><tr><td>感謝您建立智慧電商平台帳戶。請登入以存取智慧電商平台線上應用軟體、服務、支援、報表、線上學院等項目。</td></tr>"
//            		+"<tr><td style='background:#1f4f82;padding:13px 0' align='center'><a href='http://a-ber.com.tw/VirtualBusiness/registry.jsp?uid="+user_id+"'>驗證電子郵件地址 »</a></td></tr>"
//            		+"<tr><td>您收到這封電子郵件是因為最近有一個帳戶使用您的電子郵件地址建立。如果您並未建立此帳戶，請<a href='#'>提交協助要求</a>或發電子郵件至 benchen@pershing.com.tw。</td><tr>"
//            		+"<tr><td>謝謝您!</td><tr>"
//            		+"<tr><td>智慧電商團隊</td><tr></table></body></html>");
//          message.setText(
//    		"親愛的"+name+"您好:\n感謝您建立智慧電商平台帳戶。請登入以存取智慧電商平台線上應用軟體、服務、支援、報表、線上學院等項目。\n"
//    		+"點擊以下連結以驗證電子郵件地址:\n"
////    		+"http://a-ber.com.tw/VirtualBusiness/registry.jsp?uid="+user_id+"\n"
//    		+"http://localhost:8081/VirtualBusiness/registry.jsp?uid="+user_id+"\n"
//    		+"您收到這封電子郵件是因為最近有一個帳戶使用您的電子郵件地址建立。\n"
//    		+"如果您並未建立此帳戶，請發電子郵件至 benchen@pershing.com.tw。\n"
//    		+"\n謝謝您!\n"
//    		+"智慧電商團隊");
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
}
