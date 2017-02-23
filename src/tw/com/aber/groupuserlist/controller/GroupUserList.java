package tw.com.aber.groupuserlist.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.vo.UserVO;

public class GroupUserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(GroupUserList.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		GroupUserListService service = null;
		UserVO userVO = null;

		String action = request.getParameter("action");

		logger.debug("action: " + action);

		String[] actions = { "search", "insert", "update", "delete", "isEmailExist" };

		int key = Arrays.asList(actions).indexOf(action);

		logger.debug("key: " + key);

		switch (key) {

		case 0: {
			String group_id = request.getParameter("group_id");

			service = new GroupUserListService();
			userVO = new UserVO();
			userVO.setGroup_id(group_id);
			List<UserVO> list = service.search(userVO);

			String jsonStr = new Gson().toJson(list);

			String format = "\n" + "group_id : {}\n" + "jsonStr : {}\n";

			logger.debug(format, group_id, jsonStr);

			logger.debug(jsonStr);
			response.getWriter().write(jsonStr);
			break;
		}
		case 1: {
			String group_id = request.getParameter("group_id");
			String user_name = request.getParameter("user_name");
			String role = request.getParameter("role");
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			userVO = new UserVO();
			userVO.setGroup_id(null2str(group_id));
			userVO.setUser_name(null2str(user_name));
			userVO.setRole(null2str(role));
			userVO.setEmail(null2str(email));
			userVO.setPassword(null2str(password));

			String format = "\n" + "group_id : {}\n" + "user_name : {}\n" + "role : {}\n" + "email : {}\n"
					+ "password : {}\n";

			logger.debug(format, group_id, user_name, role, email, password);

			service = new GroupUserListService();
			service.insert(userVO);

			break;
		}
		case 2: {
			String group_id = request.getParameter("group_id");
			String user_id = request.getParameter("user_id");
			String user_name = request.getParameter("user_name");
			String role = request.getParameter("role");
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			userVO = new UserVO();
			userVO.setGroup_id(null2str(group_id));
			userVO.setUser_id(null2str(user_id));
			userVO.setUser_name(null2str(user_name));
			userVO.setRole(null2str(role));
			userVO.setEmail(null2str(email));
			userVO.setPassword(null2str(password));

			String format = "\n" + "group_id : {}\n" + "user_id : {}\n" + "user_name : {}\n" + "role : {}\n"
					+ "email : {}\n" + "password : {}\n";

			logger.debug(format, group_id, user_id, user_name, role, email, password);

			service = new GroupUserListService();
			service.update(userVO);

			if (null2str(password).length() > 0) {
				service.updatePwd(userVO);
			}
			break;
		}
		case 3: {
			String[] user_ids = request.getParameter("user_id").split(";");

			logger.debug("user_ids: " + user_ids);

			for (String user_id : user_ids) {

				logger.debug("excute delete user_id: " + user_id);

				userVO = new UserVO();
				userVO.setUser_id(null2str(user_id));
				userVO.setOperation(null2str(user_id));

				service = new GroupUserListService();
				service.delete(userVO);
			}
			break;
		}
		case 4: {
			String group_id = request.getParameter("group_id");
			String email = request.getParameter("email");

			String format = "\n" + "group_id : {}\n" + "email : {}\n";

			logger.debug(format, group_id, email);

			userVO = new UserVO();
			userVO.setGroup_id(null2str(group_id));
			userVO.setEmail(null2str(email));

			service = new GroupUserListService();
			Boolean exist = service.isEmailExist(userVO);

			String result = exist ? "true" : "false";
			response.getWriter().write(result);
			break;
		}
		default: {
			String user_id = request.getSession().getAttribute("user_id").toString();
			logger.debug("user_id: " + user_id);
			break;
		}
		}
	}

	class GroupUserListDao implements groupUserList_interface {
		private static final String sp_insert_user = "call sp_insert_user(?,?,?,?,?)";
		private static final String sp_selectall_user = "call sp_selectall_user(?)";
		private static final String sp_del_user = "call sp_del_user(?,?)";
		private static final String sp_update_user = "call sp_update_user(?,?,?,?,?)";
		private static final String sp_check_email = "call sp_check_email(?,?,?)";
		private static final String sp_update_password = "call sp_update_password (?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(UserVO userVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_user);

				pstmt.setString(1, userVO.getGroup_id());
				pstmt.setString(2, userVO.getUser_name());
				pstmt.setString(3, userVO.getRole());
				pstmt.setString(4, userVO.getEmail());
				pstmt.setString(5, userVO.getPassword());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public void updateDB(UserVO userVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				pstmt = con.prepareStatement(sp_update_user);
				pstmt.setString(1, userVO.getUser_id());
				pstmt.setString(2, userVO.getGroup_id());
				pstmt.setString(3, userVO.getUser_name());
				pstmt.setString(4, userVO.getRole());
				pstmt.setString(5, userVO.getEmail());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public void deleteDB(UserVO userVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_user);
				pstmt.setString(1, userVO.getUser_id());
				pstmt.setString(2, userVO.getOperation());

				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		public List<UserVO> searchDB(UserVO userVO) {
			List<UserVO> list = new ArrayList<UserVO>();

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_user);
				pstmt.setString(1, userVO.getGroup_id());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					userVO = new UserVO();
					userVO.setUser_id(rs.getString("user_id"));
					userVO.setGroup_id(rs.getString("group_id"));
					userVO.setUser_name(rs.getString("user_name"));
					userVO.setRole(rs.getString("role"));
					userVO.setEmail(rs.getString("email"));
					userVO.setPassword(rs.getString("password"));
					list.add(userVO);
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
		public Boolean isEmailExist(UserVO userVO) {
			Connection con = null;
			CallableStatement cs = null;
			Boolean rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_check_email);
				cs.registerOutParameter(3, Types.BOOLEAN);
				cs.setString(1, userVO.getGroup_id());
				cs.setString(2, userVO.getEmail());
				cs.execute();
				rs = cs.getBoolean(3);
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

		@Override
		public void updatePwd(UserVO userVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_password);
				pstmt.setString(1, userVO.getUser_id());
				pstmt.setString(2, userVO.getPassword());
				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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

	}

	class GroupUserListService {
		private groupUserList_interface dao;

		public GroupUserListService() {
			dao = new GroupUserListDao();
		}

		public List<UserVO> search(UserVO userVO) {
			return dao.searchDB(userVO);
		}

		public void insert(UserVO userVO) {
			dao.insertDB(userVO);
		}

		public void update(UserVO userVO) {
			dao.updateDB(userVO);
		}

		public void delete(UserVO userVO) {
			dao.deleteDB(userVO);
		}

		public void updatePwd(UserVO userVO) {
			dao.updatePwd(userVO);
		}

		public Boolean isEmailExist(UserVO userVO) {
			return dao.isEmailExist(userVO);
		}
	}

	public String null2str(Object object) {
		return object == null ? "" : object.toString().trim();
	}
}

interface groupUserList_interface {

	public void insertDB(UserVO userVO);

	public void updateDB(UserVO userVO);

	public void deleteDB(UserVO userVO);

	public void updatePwd(UserVO userVO);

	public Boolean isEmailExist(UserVO userVO);

	public List<UserVO> searchDB(UserVO userVO);
}