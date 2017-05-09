package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.controllerImpl.TeamMasterDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.VenueMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLEDrawEventInsertionAction extends BaseActionWeb {
	public SLEDrawEventInsertionAction() {
		super(SLEDrawEventInsertionAction.class.getName());
	}
	private static final long serialVersionUID = 1L;

	private Map<Integer, GameMasterBean> gameMap;
	private Map<Integer, LeagueMasterBean> leagueMap;
	private int gameId;
	private int gameIds;
	private int leagueId;
	private String eventStartDateTime;
	private String eventEndDateTime;
	private String[] optionSet;
	private int gameTypeId;
	private int drawId;
	List<TeamMasterBean> teamMasterList;
	List<EventMasterBean> eventMasterList;
	List<VenueMasterBean> venueMasterList;
	private int noOfEvents;
	private String eventSelected;
	private String homeTeam;
	private String awayTeam;
	private String homeTeamOdds;
	private String awayTeamOdds;
	private String drawOdds;
	private int venue;
	private String countryDeployed;
	private String venue_Name;

	public String getAwayTeam() {
		return awayTeam;
	}

	public List<TeamMasterBean> getTeamMasterList() {
		return teamMasterList;
	}

	public void setTeamMasterList(List<TeamMasterBean> teamMasterList) {
		this.teamMasterList = teamMasterList;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
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

	public String getHomeTeamOdds() {
		return homeTeamOdds;
	}

	public void setHomeTeamOdds(String homeTeamOdds) {
		this.homeTeamOdds = homeTeamOdds;
	}

	public String getAwayTeamOdds() {
		return awayTeamOdds;
	}

	public void setAwayTeamOdds(String awayTeamOdds) {
		this.awayTeamOdds = awayTeamOdds;
	}

	public String getDrawOdds() {
		return drawOdds;
	}

	public void setDrawOdds(String drawOdds) {
		this.drawOdds = drawOdds;
	}
	
	public void setLeagueMap(Map<Integer, LeagueMasterBean> leagueMap) {
		this.leagueMap = leagueMap;
	}

	public Map<Integer, LeagueMasterBean> getLeagueMap() {
		return leagueMap;
	}

	public void setEventStartDateTime(String eventStartDateTime) {
		this.eventStartDateTime = eventStartDateTime;
	}

	public String getEventStartDateTime() {
		return eventStartDateTime;
	}

	public void setEventEndDateTime(String eventEndDateTime) {
		this.eventEndDateTime = eventEndDateTime;
	}

	public String getEventEndDateTime() {
		return eventEndDateTime;
	}

	public void setOptionSet(String[] optionSet) {
		this.optionSet = optionSet;
	}

	public String[] getOptionSet() {
		return optionSet;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setGameIds(int gameIds) {
		this.gameIds = gameIds;
	}

	public int getGameIds() {
		return gameIds;
	}

	public List<VenueMasterBean> getVenueMasterList() {
		return venueMasterList;
	}

	public void setVenueMasterList(List<VenueMasterBean> venueMasterList) {
		this.venueMasterList = venueMasterList;
	}

	public void setVenue(int venue) {
		this.venue = venue;
	}

	public int getVenue() {
		return venue;
	}

	public String getCountryDeployed() {
		return countryDeployed;
	}

	public void setCountryDeployed(String countryDeployed) {
		this.countryDeployed = countryDeployed;
	}

	public String getVenue_Name() {
		return venue_Name;
	}

	public void setVenue_Name(String venue_Name) {
		this.venue_Name = venue_Name;
	}

	/* Menu Data*/
	public String drawEventInsertionMenu() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
	try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantInfoBean);
			leagueMap = CommonMethodsServiceImpl.getInstance().getLeagueMap();
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}
		return SUCCESS;
	}

	/*Fetch Teams*/
	public String fetchTeams() {
		TeamMasterDataControllerImpl controllerImpl = null;
		try {
				controllerImpl = new TeamMasterDataControllerImpl();
				teamMasterList = controllerImpl.getTeamMasterData(gameIds, leagueId);
				venueMasterList = controllerImpl.getVenueMasterData();
				countryDeployed=Util.getPropertyValue("COUNTRY_DEPLOYED");
		}catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	/*Event Insertion*/
	public String eventInsertionSubmit() {
		TeamMasterDataControllerImpl controllerImpl = null;
		SimpleDateFormat simpleDateFormat = null;
		try {
				controllerImpl = new TeamMasterDataControllerImpl();
				simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				long startTime = simpleDateFormat.parse(eventStartDateTime+":00").getTime();
				long endTime = simpleDateFormat.parse(eventEndDateTime+":00").getTime();
				//optionSet =  new String[] {"HOME_H","DRAW_D","AWAY_A","CANCEL_C"};
				controllerImpl.eventInsertionSubmit(gameId, homeTeam, awayTeam, homeTeamOdds, awayTeamOdds, drawOdds, optionSet, startTime, endTime, venue, leagueId, venue_Name);
		}catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}catch (Exception se) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
}