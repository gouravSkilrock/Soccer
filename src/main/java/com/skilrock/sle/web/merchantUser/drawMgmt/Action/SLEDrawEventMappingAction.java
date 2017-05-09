package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.controllerImpl.DrawEventMappingControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLEDrawEventMappingAction extends BaseActionWeb {
	public SLEDrawEventMappingAction() {
		super(SLEDrawEventMappingAction.class.getName());
	}
	private static final long serialVersionUID = 1L;

	private Map<Integer, GameMasterBean> gameMap;
	private Map<Integer, LeagueMasterBean> leagueMap;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	List<DrawMasterBean> drawMasterList;
	List<EventMasterBean> eventMasterList;
	private int noOfEvents;
	private String eventSelected;
	private String drawName;

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
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

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public List<DrawMasterBean> getDrawMasterList() {
		return drawMasterList;
	}

	public void setDrawMasterList(List<DrawMasterBean> drawMasterList) {
		this.drawMasterList = drawMasterList;
	}

	public List<EventMasterBean> getEventMasterList() {
		return eventMasterList;
	}

	public void setEventMasterList(List<EventMasterBean> eventMasterList) {
		this.eventMasterList = eventMasterList;
	}

	public int getNoOfEvents() {
		return noOfEvents;
	}

	public void setNoOfEvents(int noOfEvents) {
		this.noOfEvents = noOfEvents;
	}

	public String getEventSelected() {
		return eventSelected;
	}

	public void setEventSelected(String eventSelected) {
		this.eventSelected = eventSelected;
	}

	/* Menu Data*/
	public String drawEventMappingMenu() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
	try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantInfoBean);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}
		return SUCCESS;
	}

	/*Current Draw Search*/
	public void fetchCurrentDrawList() {
		response.setContentType("application/json");
		PrintWriter out = null;
		HttpSession session = null;
		DrawEventMappingControllerImpl controllerImpl = null;
		try {
				out = response.getWriter();
				session = request.getSession();
				String merCode = session.getAttribute("MER_CODE").toString();
				controllerImpl = new DrawEventMappingControllerImpl();
				drawMasterList = controllerImpl.getDrawMappingDrawMasterList(gameId, gameTypeId, merCode);
				String drawData = new Gson().toJson(drawMasterList);
				out.print(drawData);
				return;
		}catch (SLEException se) {
			logger.info("ErrorCode:"+se.getErrorCode()+" ErrorMessage:"+se.getErrorMessage());
			return;
		}catch (Exception se) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return;
		}finally {
			out.flush();
			out.close();
		}
	}

	/* Draw Event Search */
	public void drawEventMappingEventsSearch() {
		response.setContentType("application/json");
		PrintWriter out = null;
		DrawEventMappingControllerImpl controllerImpl = null;
		try {
				out = response.getWriter();
				controllerImpl = new DrawEventMappingControllerImpl();
				eventMasterList = controllerImpl.getDrawMappingEventMasterList(gameId, gameTypeId, drawId);
				if(eventMasterList.size()>0) {
					noOfEvents = eventMasterList.get(0).getNoOfEvents();
				}
				String eventData = new Gson().toJson(eventMasterList);
				out.print(eventData);
				return;
			} catch (SLEException se) {
				logger.info("ErrorCode:"+se.getErrorCode()+" ErrorMessage:"+se.getErrorMessage());
				return;
			}catch (Exception se) {
				logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				return;
			}finally {
				out.flush();
				out.close();
			}
	}

	/* Event Mapping Submit*/
	public String drawEventMappingEventsSubmit() {
		DrawEventMappingControllerImpl controllerImpl = new DrawEventMappingControllerImpl();
		try {
				String[] eventSelectionArray = eventSelected.split(",");
				eventMasterList = controllerImpl.fetchSelectedEventDetails(eventSelected);
				int[] eventSelected = new int[eventSelectionArray.length];
				for(int i=0; i<eventSelectionArray.length; i++) {
					eventSelected[i] = Integer.parseInt(eventSelectionArray[i]);
				}
				controllerImpl.drawEventMappingSubmit(gameId, gameTypeId, drawId, eventSelected);
				//SportsLotteryUtils.eventDataDrawWise.clear();
				//CommonMethodsServiceImpl.getInstance().setEventDataDrawWise();
		}catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}
		return SUCCESS;
	}

	public void setLeagueMap(Map<Integer, LeagueMasterBean> leagueMap) {
		this.leagueMap = leagueMap;
	}

	public Map<Integer, LeagueMasterBean> getLeagueMap() {
		return leagueMap;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawName() {
		return drawName;
	}
}