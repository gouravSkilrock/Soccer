package com.skilrock.sle.web.merchantUser.drawMgmt.Action;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.DrawMgmtControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.controllerImpl.TeamMasterDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedTeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLELeagueTeamInsertionAction extends BaseActionWeb {
	
	private static final long serialVersionUID = 1L;

	public SLELeagueTeamInsertionAction() {
		super(SLELeagueTeamInsertionAction.class.getName());
	}

	private Map<Integer, GameMasterBean> gameMap;
	private Map<Integer, LeagueMasterBean> leagueMap;
	private Map<Integer, TeamMasterBean> teamMap;
	Map<Integer,TeamMasterBean> unMappedTeamMasterList;
	Map<Integer,MappedTeamMasterBean> mappedTeamMasterList;
	private String leagueName;
	private String teamName;
	private String teamCode;
	private int gameId;
	private int leagueId;
	private int teamId;
	private String mappedTeam;
	
	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}

	public Map<Integer, LeagueMasterBean> getLeagueMap() {
		return leagueMap;
	}

	public void setLeagueMap(Map<Integer, LeagueMasterBean> leagueMap) {
		this.leagueMap = leagueMap;
	}

	public Map<Integer, TeamMasterBean> getTeamMap() {
		return teamMap;
	}

	public void setTeamMap(Map<Integer, TeamMasterBean> teamMap) {
		this.teamMap = teamMap;
	}

	public Map<Integer, TeamMasterBean> getUnMappedTeamMasterList() {
		return unMappedTeamMasterList;
	}

	public void setUnMappedTeamMasterList(
			Map<Integer, TeamMasterBean> unMappedTeamMasterList) {
		this.unMappedTeamMasterList = unMappedTeamMasterList;
	}

	public Map<Integer, MappedTeamMasterBean> getMappedTeamMasterList() {
		return mappedTeamMasterList;
	}

	public void setMappedTeamMasterList(
			Map<Integer, MappedTeamMasterBean> mappedTeamMasterList) {
		this.mappedTeamMasterList = mappedTeamMasterList;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
	
	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getMappedTeam() {
		return mappedTeam;
	}

	public void setMappedTeam(String mappedTeam) {
		this.mappedTeam = mappedTeam;
	}

	public String drawLeagueTeamInsertionMenu() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantInfoBean);
			leagueMap = CommonMethodsServiceImpl.getInstance().getLeagueMap();
			teamMap = CommonMethodsServiceImpl.getInstance().getTeamMap();
		}  catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	public String addLeague() {
		try {
			DrawMgmtControllerImpl.getInstance().insertLeague(gameId,leagueName);
		} catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	public String addTeam() {
		try {
			DrawMgmtControllerImpl.getInstance().insertTeam(gameId,teamName,teamCode);
		} catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	public String getLeagues(){
		try{
			leagueMap = CommonMethodsServiceImpl.getInstance().getLeagueMap();
		} catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	public String fetchAllTeams() {
		TeamMasterDataControllerImpl controllerImpl = null;
		try {
			controllerImpl = new TeamMasterDataControllerImpl();
			unMappedTeamMasterList = controllerImpl.getUnMappedTeamData(gameId, leagueId);
			mappedTeamMasterList = controllerImpl.getMappedTeamData(gameId, leagueId);
		} catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	public String updateTeams(){
		try{
			DrawMgmtControllerImpl.getInstance().updateTeamInfo(teamId,teamName,teamCode);
		} catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	public String mapLeagueTeams(){
		try {
			DrawMgmtControllerImpl.getInstance().mapLeagueTeam(gameId,leagueId,mappedTeam);
		} catch (SLEException se) {
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	
}
