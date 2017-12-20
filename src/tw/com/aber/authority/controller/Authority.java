package tw.com.aber.authority.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.ship.controller.ship;
import tw.com.aber.ship.controller.ship.ShipService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.MenuForJsTreeVO;
import tw.com.aber.vo.MenuVO;
import tw.com.aber.vo.ResponseVO;
import tw.com.aber.vo.ShipVO;
import tw.com.aber.vo.StateVO;

public class Authority extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Authority.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		Util util = new Util();

		util.ConfirmLoginAgain(request, response);

		String groupId = (String) request.getSession().getAttribute("group_id");
		String userId = (String) request.getSession().getAttribute("user_id");

		String action = request.getParameter("action");
		logger.debug("Action:".concat(action));

		Gson gson = null;

		try {
			if ("getAllMenu".equals(action)) {
				
				String userIdForPrivilege  = request.getParameter("user_id");
				
				logger.debug("userIdForPrivilege: "+userIdForPrivilege);
				logger.debug("update by userId: " + userId);
				
				AuthorityService authorityService = new AuthorityService();
				List<MenuForJsTreeVO> menuForJsTreeVOList = authorityService.getMenuForJsTreeVOList(userIdForPrivilege);
				gson = new Gson();
			
				String result = gson.toJson(menuForJsTreeVOList);
				
				logger.debug("result:");
				logger.debug(result);
				response.getWriter().write(result);
				
			}else if("upDateAuthority".equals(action)){
				
				AuthorityService authorityService = new AuthorityService();
				gson = new Gson();
				
				String privilege = request.getParameter("ids");
				String userIdForPrivilege  = request.getParameter("user_id");
				logger.debug("privilege: "+privilege);
				logger.debug("userIdForPrivilege: "+userIdForPrivilege);

				ResponseVO responseVO = authorityService.udpateAuthority(userIdForPrivilege, privilege);
				if(!responseVO.isSuccess()){
					responseVO.setErrorReason("修改權限失敗");
				}
				
				String result = gson.toJson(responseVO);

				logger.debug("result:");
				logger.debug(result);
				response.getWriter().write(result);
				
			}
		} catch (Exception e) {
			response.getWriter().write("失敗");
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	interface authority_interface {
		public List<MenuVO> getAllMenuVO();
		public String getMenuIdsByUserId(String UserId);
		public ResponseVO udpateAuthority(String user_id,String privilege);
	}

	public class AuthorityService {
		private authority_interface dao;

		public AuthorityService() {
			dao = new AuthorityDAO();
		}

		public List<MenuForJsTreeVO> getMenuForJsTreeVOList(String userId) {
			List<MenuVO> menuVOList = dao.getAllMenuVO();
			String menuIds = dao.getMenuIdsByUserId(userId);
			List<MenuForJsTreeVO> menuForJsTreeVOList = null;
			if (menuIds== null) {
				menuIds="";
			}
				String[] menuIdsArr = menuIds.split(",");
				menuForJsTreeVOList = toMenuForJsTreeVOList(menuVOList, menuIdsArr);
		
			return menuForJsTreeVOList;
		}
		
		public ResponseVO udpateAuthority(String user_id,String privilege){
			return dao.udpateAuthority(user_id,privilege);
		}
	}

	class AuthorityDAO implements authority_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		private static final String sp_get_all_menu = "call sp_get_all_menu()";
		private static final String sp_get_menu_ids_by_user_id = "call sp_get_menu_ids_by_user_id(?)";
		private static final String sp_udpate_authority_by_user_id = "call sp_udpate_authority_by_user_id(?,?)";
		
		@Override
		public List<MenuVO> getAllMenuVO() {
			List<MenuVO> list = new ArrayList<MenuVO>();

			MenuVO menuVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_all_menu);

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
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
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
		public String getMenuIdsByUserId(String userId) {
			List<MenuVO> list = new ArrayList<MenuVO>();

			String privilege = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_menu_ids_by_user_id);
				pstmt.setString(1, userId);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					privilege = rs.getString("privilege");
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
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
			return privilege;
		}

		@Override
		public ResponseVO udpateAuthority(String user_id, String privilege) {
			ResponseVO responseVO = new ResponseVO();
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_udpate_authority_by_user_id);
				pstmt.setString(1, user_id);
				pstmt.setString(2, privilege);
				pstmt.executeUpdate();
				
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
			responseVO.setSuccessObj("修改權限成功");
			return responseVO;
		}

	}
	
	List<MenuForJsTreeVO> toMenuForJsTreeVOList(List<MenuVO> menuVOList, String[] idArr) {
		List<MenuForJsTreeVO> menuForJsTreeVOList = new ArrayList<MenuForJsTreeVO>();

		for (int i = 0; i < menuVOList.size(); i++) {
			MenuVO menuVO = menuVOList.get(i);
			MenuForJsTreeVO menuForJsTreeVO = new MenuForJsTreeVO();
			StateVO state = new StateVO();
			state.setDisabled(false);
			state.setOpened(false);
			state.setSelected(false);
			
			for (int j = 0; j < idArr.length; j++) {
				if (idArr[j].equals(menuVO.getId())) {
					state.setSelected(true);
					state.setOpened(true);
					break;
				}
			}

			menuForJsTreeVO.setId(menuVO.getId());
			menuForJsTreeVO.setIcon(menuVO.getPhotoPath());

			menuForJsTreeVO.setState(state);
			String parentId=menuVO.getParentId();
			if("0".equals(parentId)){
				parentId="#";
			}
			menuForJsTreeVO.setParent(parentId);
			menuForJsTreeVO.setText(menuVO.getMenuName());

			menuForJsTreeVOList.add(menuForJsTreeVO);
		}
		return menuForJsTreeVOList;
	}
}
