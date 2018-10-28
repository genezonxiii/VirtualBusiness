package tw.com.aber.allocinv.controller;

import java.io.IOException;
import java.lang.reflect.Type;
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
import com.google.gson.reflect.TypeToken;

import tw.com.aber.service.AllocInvService;
import tw.com.aber.vo.AllocInvVo;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;

public class allocInv extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(allocInv.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String groupId = (String) request.getSession().getAttribute("group_id");
		String userId = (String) request.getSession().getAttribute("user_id");

		String action = request.getParameter("action");
		
		logger.debug("Action: ".concat(action));
		logger.debug("groupId: "+groupId);
		logger.debug("userId: "+userId);

		AllocInvService service = null;
		Gson gson = null;
		String jsonStr = null;
		List<AllocInvVo> list = null;

		if ("getAll".equals(action)) {
			service = new AllocInvService();
			list = service.getAllData(groupId);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			jsonStr = gson.toJson(list);
			logger.debug("jsonStr: "+jsonStr);

			response.getWriter().write(jsonStr);
		} else if ("getGroup".equals(action)) {
			service = new AllocInvService();
			list = service.getGroupData(groupId);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			jsonStr = gson.toJson(list);
			logger.debug(jsonStr);

			response.getWriter().write(jsonStr);
		} else if ("doPurchases".equals(action)) {
			try {
				service = new AllocInvService();
				String seqNo = service.getPurchaseSeqNo(groupId);
				String jsonList = request.getParameter("jsonList");
				
				logger.debug("seqNo: "+seqNo);
				logger.debug("jsonList: "+jsonList);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				Type type = new TypeToken<List<AllocInvVo>>() {
				}.getType();
				List<AllocInvVo> allocInvVos = gson.fromJson(jsonList, type);

				String supplyId = allocInvVos.get(0).getSupply_id();
				
				if (supplyId == null) {
					response.getWriter().write("供應商資料尚未建立");
					return;
				}

				PurchaseVO purchaseVO = new PurchaseVO();
				purchaseVO.setSeq_no(seqNo);
				purchaseVO.setGroup_id(groupId);
				purchaseVO.setUser_id(userId);
				purchaseVO.setSupply_id(supplyId);

				String purchaseId = service.addPurchase(purchaseVO);
				
				logger.debug("purchaseId: " + purchaseId);
				
				try {
					List<PurchaseDetailVO> purchaseDetailVOs = new ArrayList<PurchaseDetailVO>();
					for (AllocInvVo allocInvVo : allocInvVos) {
						PurchaseDetailVO purchaseDetailVO = new PurchaseDetailVO();
						purchaseDetailVO.setPurchase_id(purchaseId);
						purchaseDetailVO.setGroup_id(groupId);
						purchaseDetailVO.setUser_id(userId);
						purchaseDetailVO.setProduct_id(allocInvVo.getProduct_id());
						purchaseDetailVO.setC_product_id(allocInvVo.getC_product_id());
						purchaseDetailVO.setProduct_name(allocInvVo.getProduct_name());
						purchaseDetailVO.setQuantity((int) (allocInvVo.getQuantity() - allocInvVo.getAlloc_qty()));

						purchaseDetailVOs.add(purchaseDetailVO);
					}
					service.addPurchaseDetail(purchaseDetailVOs);
					response.getWriter().write("成功<br><br>採購單單號為:".concat(seqNo));
				} catch (Exception e) {
					//假如detail失敗，則刪除主單
					service.delPurchase(purchaseId);
					response.getWriter().write("失敗");
					return;
				}
			} catch (Exception e) {
				response.getWriter().write("失敗");
			}
		}
	}
	
}
