package com.skilrock.sle.tp.rest.dataMgmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrorProperty;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.AuditTrailBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.javaBeans.FetchDrawEventsRequest;
import com.skilrock.sle.dataMgmt.javaBeans.FetchDrawEventsResponse;
import com.skilrock.sle.dataMgmt.javaBeans.TicketTxnStatusBean;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.gameDataMgmt.controllerImpl.GameDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawEventResultBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.FreezeDrawBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.mobile.common.SportsLotteryResponseData;
import com.skilrock.sle.tp.rest.common.javaBeans.GameResponseBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TpResultSubmissionResponseBean;

import net.sf.json.JSONObject;

@Path("/dataMgmt")
public class TPDataMgmtServiceImpl {
	private static final SLELogger logger = SLELogger.getLogger(TPDataMgmtServiceImpl.class.getName());
	private static final int SUCCESS_CODE = 100;
	private static final String SUCCESS_MESSAGE = "SUCCESS";
	
	private ResultSubmissionControllerImpl resultSubmissionControllerImpl;
	
	public TPDataMgmtServiceImpl(){
		this.resultSubmissionControllerImpl = new ResultSubmissionControllerImpl();
	}
	
	public TPDataMgmtServiceImpl(ResultSubmissionControllerImpl resltSubmissionControllerImpl){
		this.resultSubmissionControllerImpl = resltSubmissionControllerImpl;
	}

	@Path("/fetchGameDataMerchantWise")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response fetchGameDataMerchantWise(String requestData) {
		Map<Integer, GameMasterBean> gameDataMap = new HashMap<Integer, GameMasterBean>();
		Entry<Integer, GameMasterBean> entrySet = null;
		List<GameMasterBean> gameMasterBeans = new ArrayList<GameMasterBean>();
		logger.info("***** Inside fetchActiveGameData Method with Request Data "+requestData);

		try {
			CommonMethodsServiceImpl.getInstance().fetchGameDataMerchantWise(gameDataMap, new JsonParser().parse(requestData).getAsJsonObject().get("merchantCode").getAsString());
			
			Iterator<Map.Entry<Integer, GameMasterBean>> itr = gameDataMap.entrySet().iterator();
			
			while(itr.hasNext()) {
				entrySet = itr.next();
				gameMasterBeans.add(entrySet.getValue());
			}
		}catch (Exception e) {
			e.printStackTrace();
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(gameMasterBeans).build();
	}
	
	@Path("/checkForAutoCancel")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkForAutoCancel(String reqData) {
		JsonObject reqObj = null;
		
		UserInfoBean userInfoBean = new UserInfoBean();
		MerchantInfoBean merchantInfoBean = null;
		
		logger.info("***** Inside checkForAutoCancel Method with Request Data "+reqData);
		
		try {
			reqObj = new JsonParser().parse(reqData).getAsJsonObject();
			
			merchantInfoBean = Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			
			userInfoBean.setMerchantDevName(merchantInfoBean.getMerchantDevName());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(reqObj.get("sessionId").getAsString());
			userInfoBean.setUserName(reqObj.get("userName").getAsString());
			CommonMethodsServiceImpl.getInstance().checkForAutoCancel(userInfoBean, String.valueOf(reqObj.get("slLastTxnId").getAsLong()));
		}catch (Exception e) {
			e.printStackTrace();
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity("OK").build();
	}
	
	@Path("/fetchAuditTrailData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchAuditTrailData(String reqData) {
		JsonObject reqObj = null;
		JSONObject resObj = new JSONObject();
		List<AuditTrailBean> auditTrailBeans = null;
		try {
			reqObj = new JsonParser().parse(reqData).getAsJsonObject();
			auditTrailBeans = CommonMethodsServiceImpl.getInstance().fetchAuditTrailData(reqObj.get("userId").getAsInt(), reqObj.get("merchantId").getAsInt(), reqObj.get("startTime").getAsString(), reqObj.get("endTime").getAsString());
			resObj.put("responseCode", 0);
			resObj.put("auditData", auditTrailBeans);
		} catch (SLEException e) {
			e.printStackTrace();
			resObj.put("responseCode", e.getErrorCode());
			resObj.put("responseMessage", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return Response.ok().entity(resObj).build();
	}
	
	@Path("/fetchSLEDrawData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchSLEDrawData(String requestData) {
		JSONObject resObj = new JSONObject();
		List<GameMasterBean> gameMasterList = null;
		try {
			gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(TransactionManager.getMerchantId());
			resObj = SportsLotteryResponseData.generateDrawGameData(gameMasterList,0,"SUCCESS");
			
		} catch (SLEException e) {
			e.printStackTrace();
			resObj.put("responseCode", e.getErrorCode());
			resObj.put("responseMsg", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		logger.debug("TP-Sports Lottery fetchSLEDrawData Response Data: "+resObj);
		return Response.ok().entity(resObj).build();
	}
	@Path("/fetchSLEDrawDataForSLEDrawScheduling")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchSLEDrawDataForSLEDrawScheduling(String RequestData) {
		JSONObject responseData = new JSONObject();
		List<GameMasterBean> gameMasterList = null;		
		try {
			gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(TransactionManager.getMerchantId());
			responseData = SportsLotteryResponseData.generateSLEDrawData(gameMasterList,0,"SUCCESS");
			
		} catch (SLEException e) {
			e.printStackTrace();
			responseData.put("responseCode", e.getErrorCode());
			responseData.put("responseMsg", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseData.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseData.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		logger.debug("FetchSLEDrawDataForSLEDrawScheduling Response Data: "+responseData);
		return Response.ok().entity(responseData).build();
	}
	
	@Path("/fetchSLEMatchListData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchSLEMatchListData(String requestData) {
		JSONObject resObj = new JSONObject();
		JsonObject reqObj = null;
		String fromDate=null;
		String toDate=null;
		List<GameMasterBean> gameMasterList = null;
		try {
			if (requestData == null || requestData.trim().length() < 1)
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			reqObj=new JsonParser().parse(requestData).getAsJsonObject();
			String listType=reqObj.get("listType").getAsString();
			if("drawWise".equalsIgnoreCase(listType)){
				gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(TransactionManager.getMerchantId(), fromDate, toDate,true);
				resObj = SportsLotteryResponseData.prepareSLEMatchListData(gameMasterList);
			}else if("dayWise".equalsIgnoreCase(listType)){
				fromDate = reqObj.get("fromDate").getAsString().concat(" 00:00:00");
				toDate = reqObj.get("toDate").getAsString().concat(" 23:59:59");
				if(Util.StringToDateConversion(toDate).before(Util.StringToDateConversion(fromDate))){
					throw new SLEException(SLEErrors.INVALID_DATE_RANGE_ERROR_CODE, SLEErrors.INVALID_DATE_RANGE_ERROR_MESSAGE);
				}
				gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(TransactionManager.getMerchantId(), fromDate, toDate,true);
				resObj = SportsLotteryResponseData.prepareSLEMatchListData(gameMasterList);
			}
			
		} catch (SLEException e) {
			e.printStackTrace();
			resObj.put("responseCode", e.getErrorCode());
			resObj.put("responseMsg", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return Response.ok().entity(resObj).build();
	}
	
	
	@Path("/fetchSLEResultData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchSLEResultData(String requestData) {
		JSONObject resObj = new JSONObject();
		List<GameMasterBean> gameMasterList = null;
		try {
			gameMasterList=GameDataControllerImpl.getInstance().getSportsLotteryWinningData(TransactionManager.getMerchantId());
			resObj = SportsLotteryResponseData.prepareSLEResultData(gameMasterList);
						
		} catch (SLEException e) {
			e.printStackTrace();
			resObj.put("responseCode", e.getErrorCode());
			resObj.put("responseMsg", e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return Response.ok().entity(resObj).build();
	}
	
	
	@Path("/fetchTxnStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fetchTxnStatus(String requestData) {
		JSONObject resObj = new JSONObject();
		JsonObject reqObj = null;
		List<TicketTxnStatusBean> txnStatusList = null;
		try {
			if (requestData == null || requestData.trim().isEmpty())
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			reqObj=new JsonParser().parse(requestData).getAsJsonObject();
			if( reqObj.get("playerName") == null || reqObj.get("merchantTxnIdList") == null || reqObj.get("sessionId") == null || reqObj.get("playerName").toString().trim().isEmpty() 
					|| reqObj.get("merchantTxnIdList").toString().trim().isEmpty() || reqObj.get("sessionId").getAsString().trim().isEmpty()){
				throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE,SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
			}
			txnStatusList=GameDataControllerImpl.getInstance().getSportsLotteryTxnStatusData(reqObj);
			resObj = SportsLotteryResponseData.prepareTxnStatusData(txnStatusList);				
		} catch (SLEException e) {
			e.printStackTrace();
			if(e.getErrorCode()==10012){
				resObj.put("responseCode", SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE);
				resObj.put("responseMsg", SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
			}else{
				resObj.put("responseCode", e.getErrorCode());
				resObj.put("responseMsg", e.getErrorMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return Response.ok().entity(resObj).build();
	}
	
	@Path("/getTicketPrice")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTicketPrice(String requestData) {
		JSONObject resObj = null;
		int gameTypeId = 0;
		int noOfBoard = 0;
		int noOfEvents = 0;
		double unitPrice = 0.0;
		double totalPurchaseAmt = 0.0;
		String merchantCode = null;
		JsonObject drawDataObject = null;
		JsonArray eventInfoArr = null;
		JsonArray drawInfoArr = null;
		JsonObject eventDataObject = null;
		try {
			resObj = new JSONObject();
			logger.debug("TP-Sports Lottery calculate ticket price Request Data: "+requestData);
			if (requestData == null || requestData.trim().length() < 1){
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			JsonObject sportsLotteryPlayData = new JsonParser().parse(requestData).getAsJsonObject();
			if(sportsLotteryPlayData.get("merchantCode") == null || sportsLotteryPlayData.get("merchantCode").getAsString().trim().isEmpty() || sportsLotteryPlayData.get("gameTypeId") == null || sportsLotteryPlayData.get("gameTypeId").getAsString().trim().isEmpty() || sportsLotteryPlayData.get("noOfBoard") == null || sportsLotteryPlayData.get("noOfBoard").getAsString().trim().isEmpty() || sportsLotteryPlayData.get("ticketInfo") == null || sportsLotteryPlayData.get("ticketInfo").getAsJsonArray().size() == 0){
				throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
			}else{
				merchantCode = sportsLotteryPlayData.get("merchantCode").getAsString();
				gameTypeId = sportsLotteryPlayData.get("gameTypeId").getAsInt();
				noOfBoard = sportsLotteryPlayData.get("noOfBoard").getAsInt();
				drawInfoArr = sportsLotteryPlayData.get("ticketInfo").getAsJsonArray();
			}
			
			if(!Util.merchantInfoMap.containsKey(merchantCode)){
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE,SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
			
			noOfEvents = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getNoOfEvents();
			unitPrice = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getUnitPrice();
			

			for (int i = 0; i < noOfBoard; i++) {
				drawDataObject = drawInfoArr.get(i).getAsJsonObject();
				int betAmtMultiple = drawDataObject.get("betAmtMul").getAsInt();
				eventInfoArr = drawDataObject.get("ticketData").getAsJsonArray();
				int noOfLines = 1;
				for (int j = 0; j < noOfEvents; j++) {
					eventDataObject = eventInfoArr.get(j).getAsJsonObject();
					String[] selectedOption = eventDataObject.get("eventSelected").getAsString().split(",");
					noOfLines *= selectedOption.length;
				}
				totalPurchaseAmt += noOfLines * unitPrice * betAmtMultiple;
			}
			resObj.put("responseCode", 0);
			resObj.put("responseMsg", "success");
			resObj.put("totalTicketPrice", totalPurchaseAmt);

			
		} catch (SLEException pe) {
			pe.printStackTrace();
			resObj.put("responseCode", pe.getErrorCode());
			resObj.put("responseMsg", pe.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			if (resObj.isEmpty()) {
				resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			logger.debug("TP-Sports Lottery calculate ticket price Response Data: "+resObj);
		}
		return Response.ok().entity(resObj).build();
	}
	
	
	
	@Path("/fetchGameAndGameType")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchGameAndGameType() {
		String responseData = null;
		GameResponseBean  responseSleBean = null;
		Map<Integer, GameMasterBean> gameMap = null;
		List<GameMasterBean> gameList = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			responseSleBean = new GameResponseBean();
			merchantInfoBean=Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			if(merchantInfoBean.getMerchantId()<1  ){
				responseSleBean.setResponseCode(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE);
				responseSleBean.setResponseMessage(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
				responseSleBean.setGameBeanList(null);
				responseData = new Gson().toJson(responseSleBean);
				return responseData;
			}
			gameList = new ArrayList<GameMasterBean>();
			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantInfoBean);
			for(Map.Entry<Integer, GameMasterBean> m : gameMap.entrySet()){
				gameList.add(m.getValue());
			}
			responseSleBean.setGameBeanList(gameList);
			responseSleBean.setResponseCode(SLEErrors.SUCCESS_CODE);
			responseSleBean.setResponseMessage(SLEErrors.SUCCESS_MESSAGE);
		} catch (Exception e) {
			responseSleBean.setResponseCode(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseSleBean.setResponseMessage(SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		responseData = new Gson().toJson(responseSleBean);
        return responseData;
	}
		
	
	@Path("/submitResultWithUserAuthentication")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String submitResultWithUserAuthentication(String requestData) throws SLEException{
		TpResultSubmissionResponseBean respBean = new TpResultSubmissionResponseBean();
		try{
			if (requestData == null || requestData.trim().length() < 1){
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			JsonObject resultSubmitData = parseSubmitResultReqData(requestData);
			validateForEventResult(resultSubmitData);
			DrawEventResultBean drawEventResultBean = setDrawEventResultBean(resultSubmitData);
			validateMandatoryParamsForResultSubmission(drawEventResultBean);
			boolean isValid = validateUserAuthenticationForResultSubmit(drawEventResultBean);
			if(isValid){
				String status = resultSubmissionControllerImpl.sportsLotteryResultSubmission(drawEventResultBean);
				resultSubmissionStatus(respBean, status);
			}
		} catch (SLEException sle) {
			respBean.setResponseCode(sle.getErrorCode());
			respBean.setResponseMsg(sle.getErrorMessage());
		}catch(Exception e){
			respBean.setResponseCode(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			respBean.setResponseMsg(SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return new Gson().toJson(respBean);
	}

	private boolean validateUserAuthenticationForResultSubmit(DrawEventResultBean drawEventResultBean) throws SLEException {
		boolean isValid = false;
		try {
			isValid = resultSubmissionControllerImpl.validateUserAuthenticationForResultSubmit(drawEventResultBean);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return isValid;
	}

	private void validateForEventResult(JsonObject resultSubmitData) throws SLEException {
		
		if(resultSubmitData.get("eventResult") == null || resultSubmitData.get("eventResult").getAsString().trim().isEmpty()){
			throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, "Event Result Cannot be Empty or Null");
		}
	}

	private void resultSubmissionStatus(TpResultSubmissionResponseBean respBean, String status) {
		logger.info("Result Status - "+status);
		if(status.equalsIgnoreCase("success")){
			respBean.setResponseCode(SLEErrors.SUCCESS_CODE);
			respBean.setResponseMsg(SLEErrors.SUCCESS_MESSAGE);
		}else if(status.equalsIgnoreCase("FIRST")){
			respBean.setResponseCode(SLEErrors.SUCCESS_CODE);
			respBean.setResponseMsg("First Result Submitted Successfully");
		}else if(status.equalsIgnoreCase("UNMATCHED")){
			respBean.setResponseCode(SLEErrors.SECOND_USER_RESULT_NOT_MATCHED_ERROR_CODE);
			respBean.setResponseMsg(SLEErrors.SECOND_USER_RESULT_NOT_MATCHED_ERROR_MESSAGE);
		}else if(status.equalsIgnoreCase("RESOLVED")){
			respBean.setResponseCode(SLEErrors.BOTH_USER_RESULT_NOT_MATCHED_ERROR_CODE);
			respBean.setResponseMsg(SLEErrors.BOTH_USER_RESULT_NOT_MATCHED_ERROR_MESSAGE);
		}
	}

	private void validateMandatoryParamsForResultSubmission(DrawEventResultBean drawEventResultBean) throws SLEException {
		Validator validator = prepareValidator();
		Set<ConstraintViolation<DrawEventResultBean>> violations = validator.validate(drawEventResultBean);
		if(!violations.isEmpty()){
			ConstraintViolation<DrawEventResultBean> firstViolation = violations.iterator().next();
			throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, firstViolation.getMessage());
		}
	}
	
	private Validator prepareValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator;
	}

	private DrawEventResultBean setDrawEventResultBean(JsonObject resultSubmitData) throws SLEException {
		DrawEventResultBean drawEventResultBean = new DrawEventResultBean();
		Map<Integer, String> eventOptionResult = new TreeMap<Integer, String>();
		try{
			drawEventResultBean = new Gson().fromJson(resultSubmitData, DrawEventResultBean.class);
			String eventResult = resultSubmitData.get("eventResult").getAsString();
			for(String result : eventResult.split(",")) {
				eventOptionResult.put(Integer.parseInt(result.split("_")[0]), result.split("_")[1]);
			}
			drawEventResultBean.setEventOptionResult(eventOptionResult);
		}catch(Exception e){
			throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
		}
		return drawEventResultBean;
	}

	private JsonObject parseSubmitResultReqData(String requestData) throws SLEException{
		JsonObject resultSubmitData = new JsonObject();
		try{
			resultSubmitData = new JsonParser().parse(requestData).getAsJsonObject();
		}catch(Exception e){
			throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
		}
		return resultSubmitData;
	}
	
	@Path("/fetchDrawsAndEvents")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String fetchDrawsAndEvents(String requestData ){
		List<DrawMasterBean> drawMasterList = null;
		JSONObject response = null;
		try {
			MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			response = new JSONObject();
			if (requestData == null  || requestData.trim().isEmpty()){
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			JsonObject request = (JsonObject) new JsonParser().parse(requestData);
			if(validateRequest(request,merchantInfoBean)){
				drawMasterList = resultSubmissionControllerImpl.resultSubmissionDrawData(request.get("gameId").getAsInt(),
						request.get("gameTypeId").getAsInt(), merchantInfoBean.getMerchantId(),
						request.get("userId").getAsInt());
				response.put("responseCode", 100);
				response.put("responseMessage", "SUCCESS");
				response.put("drawAndEventData", drawMasterList);
			}
			else{
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
				
		} catch (SLEException e) {
			response.put("responseCode",e.getErrorCode());
			response.put("responseMessage",e.getErrorMessage() );
		}catch (Exception e) {
			response.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			response.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return new Gson().toJson(response);
	}
	

 /** @author Vatsal Valecha
 * 	
 */
	
  @Path("/fetchfreezedDraws")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String fetchfreezedDraws(String request){
	  JSONObject response=null;
	  List<FreezeDrawBean>freezeDrawList=null;
          try{
    	    MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			response = new JSONObject();
			if(merchantInfoBean.getMerchantId()<1){
    	    	response.put("responseCode",SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE);
    	    	response.put("responseMessage",SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
    	  	    return new Gson().toJson(response);
    	  	}       
    	    if (request == null  || request.trim().isEmpty()){
    	    	response.put("responseCode",SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE);
    	    	response.put("responseMessage",SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
    	  	    return new Gson().toJson(response);
			}
    	    JsonObject requestData = (JsonObject) new JsonParser().parse(request);
    	    if(validateRequest(requestData)==0){
    	    	response.put("responseCode",SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE);
    	    	response.put("responseMessage",SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
    	  	    return new Gson().toJson(response);
    	    }else{  
    	    	freezeDrawList=resultSubmissionControllerImpl.freezedDrawResult(requestData.get("gameId").getAsInt(), requestData.get("gameTypeId").getAsInt(), requestData.get("userId").getAsInt(),merchantInfoBean.getMerchantId());
    	    	response.put("responseCode",SLEErrors.SUCCESS_CODE);
    	    	response.put("responseMessage",SLEErrors.SUCCESS_MESSAGE);
    	    	response.put("freezeDrawList",freezeDrawList);
    	    }
          }catch (SLEException e){
        	  response.put("responseCode",e.getErrorCode());
    			response.put("responseMessage",e.getErrorMessage());
          }catch (Exception e) {
  			response.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
  			response.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
  		}
	  
	  return new Gson().toJson(response);
  }
  
  
  
  
	private int validateRequest(JsonObject requestData) {

		if (requestData.get("gameId") == null || requestData.get("gameId").getAsInt() < 1
				|| requestData.get("gameTypeId") == null || requestData.get("gameTypeId").getAsInt() < 1 || requestData.get("userId") == null || requestData.get("userId").getAsInt() < 1) {
			return 0;
		}

		return 1;
	}
  
  
  
  
  
  
  
  
	public Boolean validateRequest(JsonObject request, MerchantInfoBean merchantInfoBean){
		Boolean flag = true;
		if(request.get("gameId") == null || request.get("gameId").getAsInt() < 1){
			flag = false;
		}
		if(request.get("gameTypeId") == null || request.get("gameTypeId").getAsInt() < 1 ){
			flag = false;
		}
		if( merchantInfoBean.getMerchantId() < 1){
			flag = false;
		}
		if( request.get("userId") == null || request.get("userId").getAsInt() < 1){
			flag = false;
		}
		return flag;
	}
	
	@Path("/fetchDrawEvents")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String fetchDrawEvents(String requestData){
		try {
			MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			if(merchantInfoBean == null || merchantInfoBean.getMerchantId() < 1){
				return prepareFetchDrawEventsFailureResponse(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
			if (requestData == null  || requestData.trim().isEmpty()){
				return prepareFetchDrawEventsFailureResponse(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}else{
				return fetchEventsIfRequestDataNotNull(requestData, merchantInfoBean);
			}
		} catch (Exception e) {
			return prepareFetchDrawEventsFailureResponse(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	private String fetchEventsIfRequestDataNotNull(String requestData, MerchantInfoBean merchantInfoBean) {
		String validateRequestResponse;
		FetchDrawEventsRequest fetchDrawEventsRequest;
		JsonObject request = (JsonObject) new JsonParser().parse(requestData);
		fetchDrawEventsRequest = new Gson().fromJson(request, FetchDrawEventsRequest.class);
		fetchDrawEventsRequest.setMerchantId(merchantInfoBean.getMerchantId());
		validateRequestResponse = validateRequestDataForFetchDrawEvents(fetchDrawEventsRequest);
		if(SUCCESS_MESSAGE.equals(validateRequestResponse)){
			return fetchDataIfRequestDataIsValid(fetchDrawEventsRequest);
		}
		else{
			return validateRequestResponse;
		}
	}

	private String fetchDataIfRequestDataIsValid(FetchDrawEventsRequest fetchDrawEventsRequest) {
		List<EventMasterBean> eventMasterList = resultSubmissionControllerImpl.getEventMasterDetails(fetchDrawEventsRequest);
		if(eventMasterList != null && eventMasterList.size() > 0){
			Map<String, String> eventOptionMap = CommonMethodsServiceImpl.getInstance().fetchGameTypeOptionMap(fetchDrawEventsRequest);
			if(eventOptionMap == null){
				return prepareFetchDrawEventsFailureResponse(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			return prepareFetchDrawEventsSuccessResponse(eventMasterList, fetchDrawEventsRequest, eventOptionMap);
		}else{
			return prepareFetchDrawEventsFailureResponse(SLEErrors.DRAW_EXPIRED_ERROR_CODE, SLEErrors.DRAW_EXPIRED_ERROR_MESSAGE);
		}
	}
	
	private String validateRequestDataForFetchDrawEvents(FetchDrawEventsRequest fetchDrawEventsRequestBean){
		if(fetchDrawEventsRequestBean.getGameId() < 1){
			return prepareFetchDrawEventsFailureResponse(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, SLEErrors.GAME_ID_NOT_PROVIDED_MESSAGE);
		}
		if(fetchDrawEventsRequestBean.getGameTypeId() < 1){
			return prepareFetchDrawEventsFailureResponse(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, SLEErrors.GAME_TYPE_ID_NOT_PROVIDED_MESSAGE);
		}
		if(fetchDrawEventsRequestBean.getDrawId() < 1){
			return prepareFetchDrawEventsFailureResponse(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, SLEErrors.DRAW_ID_NOT_PROVIDED_MESSAGE);
		}
		Validator validator = prepareValidator();
		Set<ConstraintViolation<FetchDrawEventsRequest>> violations = validator.validate(fetchDrawEventsRequestBean);
		if (!violations.isEmpty()) {
			ConstraintViolation<FetchDrawEventsRequest> constraintViolation = violations.iterator().next();
			return prepareFetchDrawEventsFailureResponse(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, constraintViolation.getMessage());
		}
		return SUCCESS_MESSAGE;
	}
	
	private String prepareFetchDrawEventsFailureResponse(int responseCode, String responseMessage){
		JSONObject response = new JSONObject();
		response.put("responseCode",responseCode);
		response.put("responseMessage",responseMessage);
		return new Gson().toJson(response);
	}
	
	private String prepareFetchDrawEventsSuccessResponse(List<EventMasterBean> eventMasterList, FetchDrawEventsRequest fetchDrawEventsRequest, Map<String, String> eventOptionMap){
		FetchDrawEventsResponse fetchDrawEventsResponse = new  FetchDrawEventsResponse();
		fetchDrawEventsResponse.setResponseCode(SUCCESS_CODE);
		fetchDrawEventsResponse.setResponseMessage(SUCCESS_MESSAGE);
		fetchDrawEventsResponse.setGameId(fetchDrawEventsRequest.getGameId());
		fetchDrawEventsResponse.setGameName(fetchDrawEventsRequest.getGameName());
		fetchDrawEventsResponse.setGameTypeId(fetchDrawEventsRequest.getGameTypeId());
		fetchDrawEventsResponse.setDrawId(fetchDrawEventsRequest.getDrawId());
		fetchDrawEventsResponse.setDrawName(fetchDrawEventsRequest.getDrawName());
		fetchDrawEventsResponse.setDrawDateTime(fetchDrawEventsRequest.getDrawDateTime());
		fetchDrawEventsResponse.setEventsInfo(eventMasterList);
		fetchDrawEventsResponse.setEventOptionMap(eventOptionMap);
		return new Gson().toJson(fetchDrawEventsResponse);
	}
	
}
