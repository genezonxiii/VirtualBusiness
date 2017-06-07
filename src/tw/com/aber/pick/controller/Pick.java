package tw.com.aber.pick.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import tw.com.aber.ship.controller.ship.ShipService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.PickDetailVO;
import tw.com.aber.vo.PickVO;

public class Pick extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Pick.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Util util =new Util();
		util.ConfirmLoginAgain(request, response);

		String groupId = (String)request.getSession().getAttribute("group_id");
		String userId = (String)request.getSession().getAttribute("user_id");
		String action = request.getParameter("action");
		String result = null;
		Gson gson = null;
		PickService pickService = new PickService();
		
		logger.debug("Action:".concat(action));
		try {
			if ("searchPickByPickTimeDate".equals(action)) {
				List<PickVO> pickVOList = new ArrayList<PickVO>();
				String startStr = request.getParameter("startDate");
				String endStr = request.getParameter("endDate");

				java.util.Date date = null;
				Date startDate = null, endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf.parse(startStr);
					startDate = new java.sql.Date(date.getTime());
					date = sdf.parse(endStr);
					endDate = new java.sql.Date(date.getTime());
				} catch (ParseException e) {
					logger.error("search date convert :".concat(e.getMessage()));
				}
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			

				pickVOList = pickService.searchPickByPickTimeDate(groupId, startDate, endDate);
				result = gson.toJson(pickVOList);
				
				logger.debug("result:" +result);
				
				response.getWriter().write(result);

			}
			
			if("searchPickByOrderNo".equals(action)){

				List<PickVO> pickVOList = new ArrayList<PickVO>();
				String orderNo = request.getParameter("orderNo");

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				pickVOList = pickService.searchPickByOrderNo(groupId, orderNo);
				result = gson.toJson(pickVOList);
				
				logger.debug("result:" +result);
				
				response.getWriter().write(result);
			}
			
			if("getDetail".equals(action)){
				List<PickDetailVO> pickDetailVOList = new ArrayList<PickDetailVO>();
				String pick_id = request.getParameter("pick_id");
				
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				pickDetailVOList = pickService.searchPickDetailByPickId(groupId, pick_id);
				result = gson.toJson(pickDetailVOList);
				
				logger.debug("result:" +result);
				
				response.getWriter().write(result);
				
			}

		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}	
	}
	public class PickService{
		private pick_interface dao;

		public PickService() {
			dao = new PickDAO();
		}
		
		public List<PickVO> searchPickByPickTimeDate(String groupId, Date startDate, Date endDate) {
			return dao.searchPickByPickTimeDate(groupId, startDate, endDate);
		}

		public List<PickVO> searchPickByOrderNo(String groupId,String OrderNo) {
			return dao.searchPickByOrderNo(groupId, OrderNo);
		}
		
		public List<PickDetailVO> searchPickDetailByPickId(String groupId,String pickId) {
			return dao.searchPickDetailByPickId(groupId, pickId);
		}


	}
	
	class PickDAO implements pick_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		private static final String sp_select_pick_by_pick_Time = "call sp_select_pick_by_pick_Time (?,?,?)";
		private static final String sp_select_pick_by_order_no = "call sp_select_pick_by_order_no (?,?)";
		private static final String sp_select_pick_detail_by_pick_id = "call sp_select_pick_detail_by_pick_id (?,?)";


		@Override
		public List<PickVO> searchPickByPickTimeDate(String groupId, Date startDate, Date endDate) {
			List<PickVO> pickVOList = new ArrayList<PickVO>();
			PickVO pickVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_pick_by_pick_Time);

				pstmt.setString(1, groupId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					pickVO = new PickVO();
					pickVO.setPick_id(rs.getString("pick_id"));
					pickVO.setPick_no(rs.getString("pick_no"));
					pickVO.setGroup_id(rs.getString("group_id"));
					pickVO.setPick_time(rs.getDate("pick_time"));
					pickVO.setPick_user_id(rs.getString("pick_user_id"));
					pickVOList.add(pickVO);
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			return pickVOList;
		}

		@Override
		public List<PickVO> searchPickByOrderNo(String groupId, String orderNo) {
			List<PickVO> pickVOList = new ArrayList<PickVO>();
			PickVO pickVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_pick_by_order_no);

				pstmt.setString(1, groupId);
				pstmt.setString(2, orderNo);
			

				rs = pstmt.executeQuery();
				while (rs.next()) {
					pickVO = new PickVO();
					pickVO.setPick_id(rs.getString("pick_id"));
					pickVO.setPick_no(rs.getString("pick_no"));
					pickVO.setGroup_id(rs.getString("group_id"));
					pickVO.setPick_time(rs.getDate("pick_time"));
					pickVO.setPick_user_id(rs.getString("pick_user_id"));
					pickVOList.add(pickVO);
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			return pickVOList;
		}

		@Override
		public List<PickDetailVO> searchPickDetailByPickId(String groupId, String pickId) {
			List<PickDetailVO> pickDetailVOList = new ArrayList<PickDetailVO>();
			PickDetailVO pickDetailVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_pick_detail_by_pick_id);

				pstmt.setString(1, groupId);
				pstmt.setString(2, pickId);
			

				rs = pstmt.executeQuery();
				while (rs.next()) {
					pickDetailVO = new PickDetailVO();
					pickDetailVO.setC_product_id(rs.getString("c_product_id"));
					pickDetailVO.setGroup_id(rs.getString("group_id"));
					pickDetailVO.setLocation_id(rs.getString("location_id"));
					pickDetailVO.setOrder_no(rs.getString("order_no"));
					pickDetailVO.setPick_id(rs.getString("pick_id"));
					pickDetailVO.setPickDetail_id(rs.getString("pickDetail_id"));
					pickDetailVO.setProduct_id(rs.getString("product_id"));
					pickDetailVO.setQuantity(rs.getInt("quantity"));
					pickDetailVO.setRealsaleDetail_id(rs.getString("realsaleDetail_id"));
					pickDetailVO.setV_product_name(rs.getString("product_name"));
					pickDetailVO.setV_location_code(rs.getString("location_code"));
					pickDetailVOList.add(pickDetailVO);
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			return pickDetailVOList;
		}

	}
	
	interface pick_interface {
		public List<PickVO> searchPickByPickTimeDate(String groupId, Date startDate, Date endDate);

		public List<PickDetailVO> searchPickDetailByPickId(String groupId, String pickId);

		public List<PickVO> searchPickByOrderNo(String groupId, String OrderNo);


	}

}
