package com.skilrock.sle.tp.rest.drawMgmt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrorProperty;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.DrawMgmtControllerImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.TrackTicketControllerImpl;
import com.skilrock.sle.drawMgmt.javaBeans.DrawInfoMerchantWiseBean;
import com.skilrock.sle.drawMgmt.javaBeans.SlePwtBean;
import com.skilrock.sle.drawMgmt.javaBeans.TrackSLETicketBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

@Path("/drawMgmt")
public class TPDrawMgmtServiceImpl {
	private static final SLELogger logger = SLELogger.getLogger(TPDrawMgmtServiceImpl.class.getName());

	
	@Path("/getSportsLotteryGameData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSportsLotteryDrawData(@Context HttpHeaders httpheader,
			@Context HttpServletRequest request, String requestData) {
		logger.info("***** Inside getSportsLotteryDrawData Function");
		DrawMgmtControllerImpl controllerImpl = null;
		JSONObject js=new JSONObject();
		Type type = null;
		SlePwtBean slePwtBean=null;
		String respString=null;
		try {
			type = new TypeToken<SlePwtBean>() {
			}.getType();
			slePwtBean = new Gson().fromJson(requestData, type);
			controllerImpl = new DrawMgmtControllerImpl();
			slePwtBean=controllerImpl.getSportsLotteryDrawData(slePwtBean);
			respString=new Gson().toJson(slePwtBean);
			if(slePwtBean.getDrawResult()!=null && !"".equalsIgnoreCase(slePwtBean.getDrawResult())){
				slePwtBean.setRespnseCode(0);
				js.put("drawData", respString);
				js.put("responseCode", 0);
			}else{
				js.put("drawData", respString);
				js.put("responseCode", 1);
			}
			
			
		} catch (Exception e) {
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(js).build();
	}
	
	@Path("/fetchWinningAmountDateAndDrawWise")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchWinningAmountDateAndDrawWise(String requestData) {
		List<DrawInfoMerchantWiseBean> drawInfoMerchantWiseBeans = new ArrayList<DrawInfoMerchantWiseBean>();
		
		JsonObject requestObject = new JsonObject();
		logger.info("***** Inside fetchWinningAmountDateAndDrawWise Method");
		
		try {
			requestObject = new JsonParser().parse(requestData).getAsJsonObject();
			new DrawMgmtControllerImpl().fetchWinningAmountDateAndDrawWise(
					drawInfoMerchantWiseBeans,
					requestObject.get("startDate").getAsString(), 
					requestObject.get("endDate").getAsString(),
					requestObject.get("gameId").getAsInt(),
					requestObject.get("gameTypeId").getAsInt(),
					requestObject.get("merchantCode").getAsString());
		} catch (SLEException e) {
			return Response.status(e.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(e.getErrorCode())).build();
		} catch (Exception e) {
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(drawInfoMerchantWiseBeans).build();
	}
	@Path("/TpTrackSLETicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response TpTrackTicket(String requestData) {
		TrackTicketControllerImpl controllerImpl = null;
		MerchantInfoBean merchantInfoBean=null;
		TrackSLETicketBean trackTicketBean =null;
		String merCode=null;
		JsonObject requestObject = new JsonObject();
		logger.info("***** Inside TpTrackTicket Method");
		try {
			if(requestData==null||requestData.trim().isEmpty()){
				return Response.status(SLEErrors.INVALID_DATA_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.INVALID_DATA_ERROR_CODE)).build();
			}
			requestObject = new JsonParser().parse(requestData).getAsJsonObject();
			if(requestObject.get("ticketNumber")==null||requestObject.get("ticketNumber").getAsString().trim().isEmpty()){
				return Response.status(SLEErrors.INVALID_DATA_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.INVALID_DATA_ERROR_CODE)).build();
			}
			merchantInfoBean=CommonMethodsServiceImpl.getInstance().fetchMerchantDetailFromTicket(requestObject.get("ticketNumber").getAsString());
			if(merchantInfoBean!=null ){
				merCode=merchantInfoBean.getMerchantDevName();
			}
			controllerImpl = TrackTicketControllerImpl.getInstance();

			trackTicketBean = controllerImpl.trackSLETicket(merCode,requestObject.get("ticketNumber").getAsString());
		  	trackTicketBean.setMerchantName(Util.merchantInfoMap.get(merCode).getMerchantName());
			logger.info(new Gson().toJson(trackTicketBean));
		} catch (SLEException e) {
			return Response.status(e.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(e.getErrorCode())).build();
		} catch (Exception e) {
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(trackTicketBean).build();
	}
	
}
