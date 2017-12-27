package tw.com.aber.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.util.Database;
import tw.com.aber.vo.MenuVO;
import tw.com.aber.vo.UserVO;

public class LoginDao {
	private static final Logger logger = LogManager.getLogger(LoginDao.class);

	private static final String sp_login = "call sp_login(?,?,?)";
	private static final String sp_checkuser = "call sp_checkuser(?,?)";
	private static final String sp_check_unicode = "call sp_check_unicode (?,?)";
	private static final String sp_get_main_menu = "call sp_get_main_menu()";
	private static final String sp_get_submenu_by_parent_id = "call sp_get_submenu_by_parent_id(?)";

	private Connection connection;

	public LoginDao() {
		connection = Database.getConnection();
	}

	public List<UserVO> loginDB(String p_email, String p_password, String p_unicode) {
		List<UserVO> list = new ArrayList<UserVO>();
		UserVO UserVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_login);
			pstmt.setString(1, p_email);
			pstmt.setString(2, p_password);
			pstmt.setString(3, p_unicode);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				UserVO = new UserVO();
				UserVO.setUser_id(rs.getString("uid"));
				UserVO.setGroup_id(rs.getString("gid"));
				UserVO.setUser_name(rs.getString("user"));
				UserVO.setRole(rs.getString("role"));
				UserVO.setPrivilege(rs.getString("privilege"));
				list.add(UserVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public Boolean checkuser(String p_email) {
		CallableStatement cs = null;
		Boolean rs = null;
		try {
			cs = connection.prepareCall(sp_checkuser);
			cs.registerOutParameter(2, Types.BOOLEAN);
			cs.setString(1, p_email);
			cs.execute();
			rs = cs.getBoolean(2);
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
		return rs;
	}

	public Boolean checkunicode(String unicode) {
		CallableStatement cs = null;
		Boolean rs = null;
		try {
			cs = connection.prepareCall(sp_check_unicode);
			cs.registerOutParameter(2, Types.BOOLEAN);
			cs.setString(1, unicode);
			cs.execute();
			rs = cs.getBoolean(2);
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
		return rs;
	}

	public List<MenuVO> getMainMenuDB() {
		List<MenuVO> list = new ArrayList<MenuVO>();

		MenuVO menuVO = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_main_menu);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				menuVO = new MenuVO();

				menuVO.setId(rs.getString("id"));
				menuVO.setMenuName(rs.getString("menu_name"));
				menuVO.setUrl(rs.getString("url"));
				menuVO.setSeqNo(rs.getString("seq_no"));
				menuVO.setParentId(rs.getString("parent_id"));
				menuVO.setPhotoPath(rs.getString("photo_path"));

				list.add(menuVO); // Store the row in the list
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		}
		return list;
	}

	public List<MenuVO> getSubMenuDB(String parent_id) {
		List<MenuVO> list = new ArrayList<MenuVO>();

		MenuVO menuVO = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_submenu_by_parent_id);
			pstmt.setString(1, parent_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				menuVO = new MenuVO();

				menuVO.setId(rs.getString("id"));
				menuVO.setMenuName(rs.getString("menu_name"));
				menuVO.setUrl(rs.getString("url"));
				menuVO.setSeqNo(rs.getString("seq_no"));
				menuVO.setParentId(rs.getString("parent_id"));
				menuVO.setPhotoPath(rs.getString("photo_path"));

				list.add(menuVO); // Store the row in the list
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		}
		return list;
	}

//	public static void main(String[] args) {
//		LoginDao dao = new LoginDao();
//		List<UserVO> list = dao.loginDB("", "", "");
//		logger.debug(list);
//	}
}
