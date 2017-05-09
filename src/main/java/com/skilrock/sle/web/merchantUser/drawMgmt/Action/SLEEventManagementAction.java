package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.drawMgmt.controllerImpl.DrawMgmtControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.controllerImpl.DrawEventMappingControllerImpl;
import com.skilrock.sle.gameDataMgmt.controllerImpl.TeamMasterDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedEventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.VenueMasterBean;

public class SLEEventManagementAction extends BaseActionWeb {
	
	private static final long serialVersionUID = 1L;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private int eventId;
	private int leagueId;
	private int venueId;
	private int homeTeamId;
	private int awayTeamId;
	private String startTime;
	private String endTime;
	private String homeTeamOdds;
	private String awayTeamOdds;
	private String drawOdds;
	private String venue_Name_Entered;
	private Map<Integer, GameMasterBean> gameMap;
	private Map<Integer, GameTypeMasterBean> gameTypeMap;
	private Map<Integer, LeagueMasterBean> leagueMap;
	List<TeamMasterBean> teamMasterList;
	List<VenueMasterBean> venueMasterList;
	List<DrawMasterBean> drawMasterList;
	List<MappedEventMasterBean> mappedEventMasterList;
	List<DrawMasterBean> getDrawDetailsForParticularEvent;
	private String countryDeployed;
	
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

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getVenue_Name_Entered() {
		return venue_Name_Entered;
	}

	public void setVenue_Name_Entered(String venue_Name_Entered) {
		this.venue_Name_Entered = venue_Name_Entered;
	}

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}
	
	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}
	
	public Map<Integer, GameTypeMasterBean> getGameTypeMap() {
		return gameTypeMap;
	}
	
	public void setGameTypeMap(Map<Integer, GameTypeMasterBean> gameTypeMap) {
		this.gameTypeMap = gameTypeMap;
	}
	
	public Map<Integer, LeagueMasterBean> getLeagueMap() {
		return leagueMap;
	}

	public void setLeagueMap(Map<Integer, LeagueMasterBean> leagueMap) {
		this.leagueMap = leagueMap;
	}

	public List<TeamMasterBean> getTeamMasterList() {
		return teamMasterList;
	}

	public void setTeamMasterList(List<TeamMasterBean> teamMasterList) {
		this.teamMasterList = teamMasterList;
	}

	public List<VenueMasterBean> getVenueMasterList() {
		return venueMasterList;
	}

	public void setVenueMasterList(List<VenueMasterBean> venueMasterList) {
		this.venueMasterList = venueMasterList;
	}

	public List<DrawMasterBean> getDrawMasterList() {
		return drawMasterList;
	}
	
	public void setDrawMasterList(List<DrawMasterBean> drawMasterList) {
		this.drawMasterList = drawMasterList;
	}

	public List<MappedEventMasterBean> getMappedEventMasterList() {
		return mappedEventMasterList;
	}

	public void setMappedEventMasterList(
			List<MappedEventMasterBean> mappedEventMasterList) {
		this.mappedEventMasterList = mappedEventMasterList;
	}

	public List<DrawMasterBean> getGetDrawDetailsForParticularEvent() {
		return getDrawDetailsForParticularEvent;
	}

	public void setGetDrawDetailsForParticularEvent(
			List<DrawMasterBean> getDrawDetailsForParticularEvent) {
		this.getDrawDetailsForParticularEvent = getDrawDetailsForParticularEvent;
	}
	
	public String getCountryDeployed() {
		return countryDeployed;
	}

	public void setCountryDeployed(String countryDeployed) {
		this.countryDeployed = countryDeployed;
	}

	/**
	 * <p>This method creates two maps - gameMap & gameTypeMap by using SportsLotteryUtils class.</p>
	 * @return SUCCESS or applicationAjaxException
	 * @exception SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE on Internal System Error
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public String createEventManagementMenu() {
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			gameMap = SportsLotteryUtils.gameInfoMerchantMap.get(merCode);
			gameTypeMap = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merCode);
		} catch (Exception se) {
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	/**
	 * <p>This method creates drawMasterList arraylist by calling the getDrawMasterList function and passing gameId,
	 * gameTypeId and mercode as parameters that contains the information of all the active and inactive draws for a 
	 * particular game type.</p>
	 * @return SUCCESS or applicationAjaxException
	 * @exception SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE on Internal System Error
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public String getGameTypeDrawInformationForEventManagement(){
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			drawMasterList = new DrawEventMappingControllerImpl().getGameTypeDrawMasterList(gameId, gameTypeId, merCode);
		} catch (SLEException se) {
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
	
	/**
	 * <p>This methods creates mappedEventMasterList, teamMasterList, venueMasterList, leagueMap and fetches
	 * the sale time of the current draw based on gameId and drawId.</p>
	 * @return SUCCESS or applicationAjaxException
	 * @exception SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE on Internal System Error
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public String getMappedEventInformation(){
		DrawEventMappingControllerImpl controllerImpl = null;
		try {
				controllerImpl = new DrawEventMappingControllerImpl();
				mappedEventMasterList = new ArrayList<MappedEventMasterBean>();
				teamMasterList = new ArrayList<TeamMasterBean>();
				leagueMap = new HashMap<Integer, LeagueMasterBean>();
				venueMasterList = new ArrayList<VenueMasterBean>();
				countryDeployed = Util.getPropertyValue("COUNTRY_DEPLOYED");
				controllerImpl.getInformationForEventManagement(gameId,drawId,mappedEventMasterList,teamMasterList,leagueMap,venueMasterList);
			} catch (SLEException se) {
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
	
	/**
	 * <p>This methods fetches Team info mapped with particular league</p>
	 * @return SUCCESS or applicationAjaxException
	 * @exception SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE on Internal System Error
	 * @since 12-jan-2016
	 * @author Nikhil K. Bansal
	 */
	
	public String fetchTeams() throws IOException {
		TeamMasterDataControllerImpl controllerImpl = null;
		PrintWriter out=null;
		try {
				controllerImpl = new TeamMasterDataControllerImpl();
				teamMasterList = controllerImpl.getTeamMasterData(gameId, leagueId);
				out=response.getWriter();
		}catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}finally{
			out.print(new Gson().toJson(teamMasterList));
			out.flush();
			out.close();
		}
		return SUCCESS;
	}
	
	/**
	 * <p>This methods fetches draw information for a particular event</p>
	 * @return SUCCESS or applicationAjaxException
	 * @exception SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE on Internal System Error
	 * @since 16-Oct-2015
	 * @author Rishi
	 */
	
	public String fetchDrawInfoForParticularEvent(){
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			getDrawDetailsForParticularEvent = new DrawEventMappingControllerImpl().drawInfoForParticularEvent(gameId, eventId, merCode);
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
	
	/**
	 * <p>This method updates the event information changed by the user from the front end that are already mapped
	 * for a particular draw.The draw can either be active or inactive.</p> 
	 * @return SUCCESS or applicationAjaxException
	 * @exception SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE on Internal System Error
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public String upadteEventInformation(){
		DrawMgmtControllerImpl controllerImpl = null;
		UserInfoBean userInfoBean=null; 
		HttpSession session = null;
		try {
				session = request.getSession();
				controllerImpl = new DrawMgmtControllerImpl();
				mappedEventMasterList = new ArrayList<MappedEventMasterBean>();
				userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
				controllerImpl.updateMappedEvents(eventId,leagueId,venueId,homeTeamId,awayTeamId,startTime,endTime,homeTeamOdds,awayTeamOdds,drawOdds,venue_Name_Entered,userInfoBean);
				getMappedEventInformation();
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
