
package tw.com.aber.chat;

import java.io.IOException;
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

import com.google.gson.Gson;

public class chatsubject extends HttpServlet {

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
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		group_id = (group_id == null || group_id.length() < 3) ? "KNOWN" : group_id;
		String action = request.getParameter("action");
		ExchangeDAO dao = new ExchangeDAO();
		List<DisscussionVO> list = new ArrayList<DisscussionVO>();
		if ("search".equals(action)) {
			String subject_id = request.getParameter("subject_id");
			System.out.println(subject_id);
			list = dao.searhDB(subject_id);
		} else if ("insert".equals(action)) {
			String subject_id = request.getParameter("subject_id");
			String subject_Name = request.getParameter("subject_Name");
			String content = request.getParameter("content");
			dao.insert(subject_id, subject_Name, content, user_id);
			list = dao.searhDB(subject_id);
		} else if ("update".equals(action)) {
			String disscussion_id = request.getParameter("disscussion_id");
			String subject_id = request.getParameter("subject_id");
			String subject_Name = request.getParameter("subject_Name");
			String content = request.getParameter("content");
			dao.update(disscussion_id, subject_id, subject_Name, content, user_id);
			list = dao.searhDB(subject_id);
		} else if ("delete".equals(action)) {
			String disscussion_id = request.getParameter("disscussion_id");
			String subject_id = request.getParameter("subject_id");
			dao.delete(subject_id);
			list = dao.searhDB(disscussion_id);
		} else if ("topic_list".equals(action)) {
			List<TopicVO> topic_list = dao.select_topic();
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(topic_list);
			response.getWriter().write(jsonStrList);
			return;
		}
		Gson gson = new Gson();
		String jsonStrList = gson.toJson(list);
		response.getWriter().write(jsonStrList);
		return;
	}

	/************************* 對應資料庫表格格式 **************************************/
	public class TopicVO implements java.io.Serializable {
		public String topic_id;
		public String topic;
		public String start_time;
		public String end_time;
		public String teacher;
		public String memo;
	}

	public class DisscussionVO implements java.io.Serializable {
		public String disscussion_id;
		public String subject_id;
		public String subject_Name;
		public String content;
		public String create_time;
		public String user_id;
	}

	/*************************** 操作資料庫 ****************************************/
	class ExchangeDAO {
		// 會使用到的Stored procedure
		private static final String sp_selectall_topic = "call sp_selectall_topic()";
		private static final String sp_insert_discussion = "call sp_insert_discussion (?,?,?,?)";
		private static final String sp_update_discussion = "call sp_update_discussion (?,?,?,?,?)";
		private static final String sp_del_discussion = "call sp_del_discussion (?)";
		private static final String sp_selectall_discussion = "call sp_selectall_discussion (?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		public List<TopicVO> select_topic() {
			TopicVO topicVO = null;
			List<TopicVO> list = new ArrayList<TopicVO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_topic);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					topicVO = new TopicVO();
					topicVO.topic_id = rs.getString("topic_id");
					topicVO.topic = rs.getString("topic");
					topicVO.start_time = rs.getString("start_time");
					topicVO.end_time = rs.getString("end_time");
					topicVO.teacher = rs.getString("teacher");
					topicVO.memo = rs.getString("memo");

					list.add(topicVO);
				}
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}

		public List<DisscussionVO> searhDB(String group_id) {
			DisscussionVO disscussionVO = null;
			List<DisscussionVO> list = new ArrayList<DisscussionVO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_discussion);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					disscussionVO = new DisscussionVO();
					disscussionVO.disscussion_id = rs.getString("disscussion_id");
					disscussionVO.subject_id = rs.getString("subject_id");
					disscussionVO.subject_Name = rs.getString("subject_Name");
					disscussionVO.content = rs.getString("content");
					disscussionVO.create_time = rs.getString("create_time");
					disscussionVO.user_id = rs.getString("user_id");
					list.add(disscussionVO);
				}
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return list;
		}

		public void insert(String subject_id, String subject_Name, String content, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_discussion);
				pstmt.setString(1, subject_id);
				pstmt.setString(2, subject_Name);
				pstmt.setString(3, content);
				pstmt.setString(4, user_id);
				pstmt.executeQuery();
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public void update(String disscussion_id, String subject_id, String subject_Name, String content,
				String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_discussion);
				pstmt.setString(1, disscussion_id);
				pstmt.setString(2, subject_id);
				pstmt.setString(3, subject_Name);
				pstmt.setString(4, content);
				pstmt.setString(5, user_id);
				pstmt.executeQuery();
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return;
		}

		public void delete(String subject_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_discussion);
				pstmt.setString(1, subject_id);
				pstmt.executeQuery();
			} catch (SQLException se) {
				System.out.println("ERROR WITH: " + se);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
			return;
		}
	}
}
