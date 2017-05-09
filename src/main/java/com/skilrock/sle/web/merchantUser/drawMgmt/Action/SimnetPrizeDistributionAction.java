package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;

public class SimnetPrizeDistributionAction extends BaseActionWeb {

	int gameId;
	int gameTypeId;
	int simnetDrawsId;
	int saleOnSimnet;
	int noOfWinnersfor12;
	int noOfWinnersfor11;
	int noOfWinnersfor10;
	Map<String, Double> prizeDistributionMap;
	private ResultSubmissionControllerImpl controllerImpl = null;
	int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int i) {
		this.status = i;
	}

	public Map<String, Double> getPrizeDistributionMap() {
		return prizeDistributionMap;
	}

	public void setPrizeDistributionMap(Map<String, Double> prizeDistributionMap) {
		this.prizeDistributionMap = prizeDistributionMap;
	}

	public SimnetPrizeDistributionAction() {
		// super();
		controllerImpl = new ResultSubmissionControllerImpl();
	}

	public SimnetPrizeDistributionAction(ResultSubmissionControllerImpl controllerImpl) {
		this.controllerImpl = controllerImpl;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getSimnetDrawsId() {
		return simnetDrawsId;
	}

	public void setSimnetDrawsId(int simnetDrawsId) {
		this.simnetDrawsId = simnetDrawsId;
	}

	public int getSaleOnSimnet() {
		return saleOnSimnet;
	}

	public void setSaleOnSimnet(int saleOnSimnet) {
		this.saleOnSimnet = saleOnSimnet;
	}

	public int getNoOfWinnersfor12() {
		return noOfWinnersfor12;
	}

	public void setNoOfWinnersfor12(int noOfWinnersfor12) {
		this.noOfWinnersfor12 = noOfWinnersfor12;
	}

	public int getNoOfWinnersfor11() {
		return noOfWinnersfor11;
	}

	public void setNoOfWinnersfor11(int noOfWinnersfor11) {
		this.noOfWinnersfor11 = noOfWinnersfor11;
	}

	public int getNoOfWinnersfor10() {
		return noOfWinnersfor10;
	}

	public void setNoOfWinnersfor10(int noOfWinnersfor10) {
		this.noOfWinnersfor10 = noOfWinnersfor10;
	}

	public String simnetResultDatafetch() {
		HttpSession session = null;
		Map<String, Double> SimnetPrizeDetailMap = null;
		try {
			session = request.getSession();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			int userId = userBean.getMerchantUserId();
			SimnetPrizeDetailMap = controllerImpl.simnetPrizeDistribution(gameId, gameTypeId, simnetDrawsId,
					saleOnSimnet, userId, noOfWinnersfor12, noOfWinnersfor11, noOfWinnersfor10);
			if (SimnetPrizeDetailMap != null) {
				setStatus(1);
				setPrizeDistributionMap(SimnetPrizeDetailMap);
			} else {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
						SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setStatus(0);
			return ERROR;
		}
		return SUCCESS;

	}

}
