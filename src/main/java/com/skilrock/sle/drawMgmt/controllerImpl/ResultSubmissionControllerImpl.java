package com.skilrock.sle.drawMgmt.controllerImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.dataMgmt.javaBeans.FetchDrawEventsRequest;
import com.skilrock.sle.dataMgmt.javaBeans.SimnetResultDrawsBean;
import com.skilrock.sle.drawMgmt.daoImpl.ResultSubmissionDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawEventResultBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.FreezeDrawBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultApprovalBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultUserAssignBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class ResultSubmissionControllerImpl {
	
	private static final SLELogger logger = SLELogger.getLogger(ResultSubmissionControllerImpl.class.getName());
	private ResultSubmissionDaoImpl resultSubmissionDaoImpl;
	
	public ResultSubmissionControllerImpl(ResultSubmissionDaoImpl resultSubmissionDaoImpl) {
		this.resultSubmissionDaoImpl = resultSubmissionDaoImpl;
	}
	
	public ResultSubmissionControllerImpl() {
		resultSubmissionDaoImpl = new ResultSubmissionDaoImpl();
	}	
	public Map<Integer, String> userAssignMenu(int merchantId) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		Map<Integer, String> userMap = null;
		try {
			connection = DBConnect.getConnection();
			userMap = daoImpl.userAssignMenu(merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return userMap;
	}

	public List<Integer> getAssignedUser(int merchantId, int gameId, int gameTypeId) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		List<Integer> userList = null;
		try {
			connection = DBConnect.getConnection();
			userList = daoImpl.getAssignedUser(merchantId, gameId, gameTypeId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return userList;
	}

	public void resultSubmissionUserAssign(ResultUserAssignBean assignBean) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			daoImpl.resultSubmissionUserAssign(assignBean, connection);
			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
	}

	public List<DrawMasterBean> resultSubmissionDrawData(int gameId, int gameTypeId, int merchantId, int userId) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		List<DrawMasterBean>drawMasterList = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			boolean isValid = daoImpl.getUserResultAuthorization(gameId, gameTypeId, merchantId, userId, connection);
			if(isValid) {
				drawMasterList = daoImpl.getDrawMasterDetails(gameId, gameTypeId,  userId,merchantId ,connection);
			}
			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return drawMasterList;
	}

	
	public List<FreezeDrawBean> freezedDrawResult(int gameId, int gameTypeId, int userId, int merchantId)
			throws Exception {
		Connection connection = null;
		List<FreezeDrawBean> freezedDrawList = null;
		try {
			connection = DBConnect.getConnection();
			boolean isValid = resultSubmissionDaoImpl.getUserResultAuthorization(gameId, gameTypeId, merchantId, userId, connection);
			if (isValid) {
				freezedDrawList = resultSubmissionDaoImpl.getFreezedDrawsList(gameId, gameTypeId,userId, merchantId,
						connection);
			}
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return freezedDrawList;
	}
	
	public String sportsLotteryResultSubmission(DrawEventResultBean drawEventResultBean) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		String status = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			status = daoImpl.insertUpdateResultStatus(drawEventResultBean, connection);
			if("MATCHED".equals(status)) {
				status = daoImpl.sportsLotteryResultSubmission(drawEventResultBean, connection);
			} else{
				connection.commit();
			}
			
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return status;
	}

	public List<ResultApprovalBean> getUnmatchedDraws(ResultApprovalBean requestBean) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		List<ResultApprovalBean> approvalList = null;
		try {
			connection = DBConnect.getConnection();
			approvalList = daoImpl.getUnmatchedDraws(requestBean, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return approvalList;
	}

	public ResultApprovalBean getUnmatchedDrawDetails(ResultApprovalBean requestBean) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		ResultApprovalBean approvalBean = null;
		try {
			connection = DBConnect.getConnection();
			approvalBean = daoImpl.getUnmatchedDrawDetails(requestBean, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return approvalBean;
	}

	public String resultApproval(DrawEventResultBean drawEventResultBean) throws SLEException {
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		String status = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			daoImpl.resolveResultStatus(drawEventResultBean, connection);
			status = daoImpl.sportsLotteryResultSubmission(drawEventResultBean, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return status;
	}
	
	public List<EventMasterBean> getEventMasterDetails(FetchDrawEventsRequest fetchDrawEventsRequestBean){
		List<EventMasterBean> eventMasterList = null;
		Connection connection = null;
		try{
			connection = DBConnect.getConnection();
			eventMasterList = resultSubmissionDaoImpl.getEventMasterDetails(fetchDrawEventsRequestBean.getGameId(), fetchDrawEventsRequestBean.getGameTypeId(), fetchDrawEventsRequestBean.getDrawId(), fetchDrawEventsRequestBean.getMerchantId(), connection);
		}catch(SLEException s){
			logger.error("Exception Occurred:"+s);
		}catch(Exception e){
			logger.error("Exception Occurred:"+e);
		}finally {
			DBConnect.closeConnection(connection);
		}
		return eventMasterList;
	}
	
	public boolean validateUserAuthenticationForResultSubmit(DrawEventResultBean drawEventResultBean) throws SLEException {
		boolean isValid = false;
		Connection connection = null;
		ResultSubmissionDaoImpl daoImpl = new ResultSubmissionDaoImpl();
		try {
			connection = DBConnect.getConnection();
			MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			isValid = daoImpl.getUserResultAuthorization(drawEventResultBean.getGameId(),drawEventResultBean.getGameTypeId(),merchantInfoBean.getMerchantId(),drawEventResultBean.getUserId(),connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return isValid;
	}
	
	public Map<String,Double> simnetPrizeDistribution(int gameId,int gameTypeId,int drawId,int simnetTotalSale,int userId,int noOfWinnersFor12,int noOfWinnersFor11,int noOfWinnersFor10) throws SLEException{
		 Connection connection=null;
		 Map<String,Double> SuccessJspSimnetPrizeDistMap=null;
		try{
			connection=DBConnect.getConnection();
		    SuccessJspSimnetPrizeDistMap=resultSubmissionDaoImpl.simnetPrizeDistributionDao(gameId,gameTypeId,drawId,simnetTotalSale,userId,noOfWinnersFor12,noOfWinnersFor11,noOfWinnersFor10,connection);
		  }catch (SLEException se) {
			throw se;
		  }catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		  } finally {
				DBConnect.closeConnection(connection);
		  }
		return SuccessJspSimnetPrizeDistMap;
	}
	
	
	
}
