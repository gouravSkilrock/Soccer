package com.skilrock.sle.tp.rest.userMgmt;

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
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrorProperty;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.roleMgmt.controllerImpl.CreateRoleHeadControllerImpl;
import com.skilrock.sle.roleMgmt.controllerImpl.GetPriviledgeControllerImpl;
import com.skilrock.sle.roleMgmt.javaBeans.PriviledgeModificationMasterBean;
import com.skilrock.sle.roleMgmt.javaBeans.PrivilegeDataBean;
import com.skilrock.sle.roleMgmt.javaBeans.RoleHeadRegistrationBean;
import com.skilrock.sle.roleMgmt.javaBeans.RolePrivilegeBean;
import com.skilrock.sle.roleMgmt.javaBeans.SubUserRegistrationBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;
import com.skilrock.sle.userMgmt.controllerImpl.UserMgmtControllerImpl;

@Path("/userMgmt")
public class TPUserMgmtServiceImpl {
	private static final SLELogger logger = SLELogger.getLogger(TPUserMgmtServiceImpl.class.getName());

	@Path("/userRegistration")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response tpUserRegistration(TpUserRegistrationBean tpUserRegistrationRequestBean) {
		logger.info("***** Inside TPuserRegistration Function");
		UserMgmtControllerImpl controllerImpl = null;
		JSONObject js=new JSONObject();
		try {
			tpUserRegistrationRequestBean.setMerchantId(TransactionManager.getMerchantId());
			logger.info("Tp User Reg. Bean "+tpUserRegistrationRequestBean);
			controllerImpl = new UserMgmtControllerImpl();
			controllerImpl.registerMerchantUser(tpUserRegistrationRequestBean);
			js.put("responseCode", 0);
			js.put("responseMessage", "SLE notified about user registration successfully !!");
		} catch (GenericException e) {
			return Response.status(e.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(e.getErrorCode())).build();
		} catch (Exception e) {
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(js).build();
	}
	
	@Path("/userLoginNotify")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userLoginNotifyLMS(TpUserRegistrationBean tpUserRegistrationRequestBean) {
		logger.info("***** Inside userLoginNotify Function");
		UserMgmtControllerImpl controllerImpl = null;
		JSONObject js=new JSONObject();
		try {
			tpUserRegistrationRequestBean.setMerchantId(TransactionManager.getMerchantId());
			logger.info("User Login Notify Request Bean "+tpUserRegistrationRequestBean);
			controllerImpl = new UserMgmtControllerImpl();
			controllerImpl.userLoginNotify(tpUserRegistrationRequestBean);
			SportsLotteryUtils.loggedInUser(tpUserRegistrationRequestBean.getMerchantId()+"_"+tpUserRegistrationRequestBean.getUserName(), tpUserRegistrationRequestBean.getSessionId());
			js.put("responseCode", 0);
			js.put("responseMessage", "SLE notified about user login successfully !!");
		} catch (GenericException e) {
			return Response.status(e.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(e.getErrorCode())).build();
		} catch (Exception e) {
			return Response	.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(js).build();
	}
	
	@Path("/userLogoutNotify")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userLogoutNotify(TpUserRegistrationBean tpUserRegistrationRequestBean) {
		logger.info("***** Inside userLogoutNotify Function");
		UserMgmtControllerImpl controllerImpl = null;
		JSONObject js=new JSONObject();
		try {
			tpUserRegistrationRequestBean.setMerchantId(TransactionManager.getMerchantId());
			logger.info("User Logout Notify Request Bean "+tpUserRegistrationRequestBean);
			controllerImpl = new UserMgmtControllerImpl();
			controllerImpl.userLogoutNotify(tpUserRegistrationRequestBean);
			SportsLotteryUtils.updateLoggedInUserMap(tpUserRegistrationRequestBean.getMerchantId()+"_"+tpUserRegistrationRequestBean.getUserName(), tpUserRegistrationRequestBean.getSessionId());
			js.put("responseCode", 0);
			js.put("responseMessage", "SLE notified about user logout successfully !!");
		} catch (GenericException e) {
			return Response.status(e.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(e.getErrorCode())).build();
		} catch (Exception e) {
			return Response	.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}
		return Response.ok().entity(js).build();
	}
	
	@Path("/sessionOutNotify")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sessionOutNotify(String requestData) {
		logger.info("***** Inside sessionOutNotify Function");
		UserMgmtControllerImpl controllerImpl = null;
		JSONObject js=new JSONObject();
		int merchantId = 0;
		try {
				JsonParser parser = new JsonParser();
				JsonObject requestObj = (JsonObject) parser.parse(requestData);
			
				merchantId = TransactionManager.getMerchantId();
				controllerImpl = new UserMgmtControllerImpl();
				controllerImpl.serverDownNotify(merchantId, requestObj.get("sessionId").getAsString());
				js.put("responseCode", 0);
				js.put("responseMessage", "SLE notified about merchant Session Out successfully !!");
			} catch (GenericException e) {
				return Response.status(e.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(e.getErrorCode())).build();
			} catch (Exception e) {
				return Response	.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
			}
			return Response.ok().entity(js).build();
	}
	
	@Path("/getRolePriviledge")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRolePriviledge(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		JsonParser parser = new JsonParser();
		JsonObject requestObj = (JsonObject) parser.parse(requestData);
		List<PrivilegeDataBean> privilegeList;	
		privilegeList = new GetPriviledgeControllerImpl().getRolePrivilledge(TransactionManager.getMerchantId(), requestObj.get("roleId").getAsInt());
		
		js.put("responseCode", 0);
		js.put("privData", privilegeList);
		/*Gson gson = new Gson();
		String json = gson.toJson(privilegeList);*/
		System.out.println(js);
		} catch (GenericException e) {
			js.put("responseCode",e.getErrorCode());
			js.put("responseMessage",e.getErrorMessage());
		}
		
		return Response.ok().entity(js).build();
	}
	
	@Path("/getSubUserPriviledge")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubUserPriviledge(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		JsonParser parser = new JsonParser();
		JsonObject requestObj = (JsonObject) parser.parse(requestData);
		 List<PrivilegeDataBean> privilegeList = new GetPriviledgeControllerImpl().getSubUserPrivilledge(TransactionManager.getMerchantId(), requestObj.get("userId").getAsInt());
		
			js.put("responseCode", 0);
			js.put("privData", privilegeList);
			System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	
	@Path("/getRetailerPrivilege")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRetailerPrivilege(String requestData){
		JSONObject js=new JSONObject();
		try {
		JsonParser parser = new JsonParser();
		JsonObject requestObj = (JsonObject) parser.parse(requestData);
		List<PrivilegeDataBean> privilegeList = new GetPriviledgeControllerImpl().getRetailerPrivilege(requestObj.get("userId").getAsInt(),requestObj.get("merCode").getAsString());
		js.put("responseCode", 0);
		js.put("privData", privilegeList);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	
	@Path("/updateRetailerPrivilege")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRetailerPrivilege(@Context HttpServletRequest request,String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
			
		RolePrivilegeBean rolePrivBean = new Gson().fromJson(requestData, new TypeToken<RolePrivilegeBean>() {}.getType());
		rolePrivBean.setRequestIp(request.getRemoteAddr());
		boolean isSuccess=new GetPriviledgeControllerImpl().updateRetailerPrivilege(rolePrivBean);
		
		js.put("responseCode", 0);
		js.put("isSuccess", isSuccess);
		System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	
	
	@Path("/getCreateUserPriviledge")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreateUserPriviledge(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		JsonParser parser = new JsonParser();
		JsonObject requestObj = (JsonObject) parser.parse(requestData);
		 List<PrivilegeDataBean> privilegeList = new GetPriviledgeControllerImpl().getCreateUserPriviledge(TransactionManager.getMerchantId(), requestObj.get("userId").getAsInt(),false);
		 
			js.put("responseCode", 0);
			js.put("privData", privilegeList);
			System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	@Path("/getCreateRoleUserPriviledge")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreateRoleUserPriviledge(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		JsonParser parser = new JsonParser();
		JsonObject requestObj = (JsonObject) parser.parse(requestData);
		 List<PrivilegeDataBean> privilegeList = new GetPriviledgeControllerImpl().getCreateUserPriviledge(TransactionManager.getMerchantId(), requestObj.get("userId").getAsInt(),true);
		 
			js.put("responseCode", 0);
			js.put("privData", privilegeList);
			System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}

	/*
	 * http://127.0.0.1:8081/SportsLottery/rest/userMgmt/fetchUserPriviledgeHistory
	 * {"userId":11217, "startDate":"2015-06-01 00:00:00", "endDate":"2015-06-08 23:59:59"}
	 */
	@Path("/fetchUserPriviledgeHistory")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchUserPriviledgeHistory(String requestData) {
		System.out.println("requestData - "+requestData);
		JSONObject js = new JSONObject();
		try {
			JsonObject requestObject = new JsonParser().parse(requestData).getAsJsonObject();

			int merchantId = TransactionManager.getMerchantId();
			int userId = requestObject.get("userId").getAsInt();
			String startDate = requestObject.get("startDate").getAsString();
			String endDate = requestObject.get("endDate").getAsString();

			PriviledgeModificationMasterBean masterBean = new GetPriviledgeControllerImpl().fetchUserPriviledgeHistory(merchantId, userId, startDate, endDate);
			js.put("responseCode", 0);
			js.put("responseData", masterBean);
			System.out.println(js);
		} catch (GenericException e) {
			js.put("responseCode",e.getErrorCode());
			js.put("responseMessage",e.getErrorMessage());
		}

		return Response.ok().entity(js).build();
	}

	@Path("/createRole")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRole(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		RolePrivilegeBean rolePrivBean = new Gson().fromJson(requestData, new TypeToken<RolePrivilegeBean>() {}.getType());
		
		new CreateRoleHeadControllerImpl().createRoleSave(rolePrivBean,TransactionManager.getMerchantId());
		
		js.put("responseCode", 0);
		System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	@Path("/editRole")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editRole(String requestData){
		System.out.println(requestData);
		/*JsonParser parser = new JsonParser();
		JsonObject requestObj = (JsonObject) parser.parse(requestData);
	*/	 
		JSONObject js=new JSONObject();
		try {
		RolePrivilegeBean rolePrivBean = new Gson().fromJson(requestData, new TypeToken<RolePrivilegeBean>() {}.getType());
		
		new CreateRoleHeadControllerImpl().editRoleSave(rolePrivBean,TransactionManager.getMerchantId());
		
		js.put("responseCode", 0);
		System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	@Path("/createRoleHead")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRoleHead(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {

		RoleHeadRegistrationBean registrationBean = new Gson().fromJson(requestData, new TypeToken<RoleHeadRegistrationBean>() {}.getType());

		new CreateRoleHeadControllerImpl().createRoleHeadUserSave(registrationBean,TransactionManager.getMerchantId());
		
		js.put("responseCode", 0);
		System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	@Path("/createSubUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createSubUser(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		SubUserRegistrationBean registrationBean = new Gson().fromJson(requestData, new TypeToken<SubUserRegistrationBean>() {}.getType());
		
		new CreateRoleHeadControllerImpl().createSubUserSave(registrationBean,TransactionManager.getMerchantId());
		
		
		js.put("responseCode", 0);
		System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
	
	@Path("/editSubUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editSubUser(String requestData){
		System.out.println(requestData);
		JSONObject js=new JSONObject();
		try {
		SubUserRegistrationBean registrationBean = new Gson().fromJson(requestData, new TypeToken<SubUserRegistrationBean>() {}.getType());
		
		new CreateRoleHeadControllerImpl().editSubUser(registrationBean,TransactionManager.getMerchantId());
		
		js.put("responseCode", 0);
		System.out.println(js);
	} catch (GenericException e) {
		js.put("responseCode",e.getErrorCode());
		js.put("responseMessage",e.getErrorMessage());
	}
		return Response.ok().entity(js).build();
	}
}
