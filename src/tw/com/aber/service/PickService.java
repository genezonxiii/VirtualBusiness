package tw.com.aber.service;

import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import tw.com.aber.dao.PickDao;
import tw.com.aber.vo.PickDetailVO;
import tw.com.aber.vo.PickVO;

public class PickService{
	private static final Logger logger = LogManager.getLogger(PickService.class);
	private PickDao dao;

	public PickService() {
		dao = new PickDao();
	}
	
	/**
	 * <p>依{@code group_id}及{@code pick_time}區間，撈取{@code tb_pick}
	 * 
	 * @param groupId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<PickVO> searchPickByPickTimeDate(String groupId, Date startDate, Date endDate) {
		return dao.searchPickByPickTimeDate(groupId, startDate, endDate);
	}

	/**
	 * <p>{@code group_id}及{@code order_no}撈取揀貨單明細{@code tb_pickDetail}
	 * 
	 * @param groupId
	 * @param OrderNo
	 * @return
	 */
	public List<PickVO> searchPickByOrderNo(String groupId,String OrderNo) {
		return dao.searchPickByOrderNo(groupId, OrderNo);
	}
	
	/**
	 * <p>依{@code group_id}和{@code pick_id}撈取揀貨單明細{@code tb_pickDetail}
	 * 
	 * @param groupId
	 * @param pickId
	 * @return
	 */
	public List<PickDetailVO> searchPickDetailByPickId(String groupId,String pickId) {
		return dao.searchPickDetailByPickId(groupId, pickId);
	}
	
	@Test
	public void testMethods() {
		
		PickService pickService = new PickService();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fromTime = new Date(sdf.parse("2016-01-01").getTime());
			Date tillTime = new Date(sdf.parse("2017-12-31").getTime());
			List<PickVO> pickVOs = pickService.searchPickByPickTimeDate("cbcc3138-5603-11e6-a532-000d3a800878",fromTime,tillTime );
			assertTrue(pickVOs!=null);
		} catch (ParseException e) {
			logger.error("jUnitTestErr",e);
		}
		
	}
}