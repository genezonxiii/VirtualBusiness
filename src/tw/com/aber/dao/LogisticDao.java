package tw.com.aber.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.util.Database;
import tw.com.aber.vo.LogisticVO;

public class LogisticDao {
	
	private static final Logger logger = LogManager.getLogger(PickDao.class);
	
	private static final String sp_logistic_record = "call sp_logistic_record (?,?,?,?,?)";

	private Connection connection;

	public LogisticDao() {
		connection = Database.getConnection();
	}
	
	public void logisticRecord(LogisticVO logisticVO){

		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_logistic_record);

			pstmt.setString(1, logisticVO.getGroupId());
			pstmt.setString(2, logisticVO.getUserId());
			pstmt.setString(3, logisticVO.getOrderNo());
			pstmt.setString(4, logisticVO.getShippingCompany());
			pstmt.setString(5, logisticVO.getReturnLabel());

			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		return ;
	}

}