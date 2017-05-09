package com.skilrock.sle.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class AuthorizationRequestFilter implements ContainerRequestFilter,
		ContainerResponseFilter {

	private static final SLELogger logger = SLELogger.getLogger(AuthorizationRequestFilter.class.getName());
	
	@Context
    private HttpServletRequest request;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public ContainerRequest filter(ContainerRequest req) {
//		int merchantId = 0;
		JsonObject responseObj = null;
		try {
			// System.out.println(Thread.currentThread().getId());
			/*
			 * String method = req.getMethod(); String path = req.getPath(true);
			 */
			TransactionManager.startTransaction();

			logger.info("AUDIT_REQUEST_ID="+TransactionManager.getAuditId()+" , Request Header="+req.getRequestHeaders());

			// Get the authentification passed in HTTP headers parameters

			String userName = req.getHeaderValue("userName");
			String password = req.getHeaderValue("password");

			// If the user does not have the right (does not provide any HTTP
			// Basic Auth)
			if (userName == null || password == null) {
				 responseObj = new JsonObject();
				 responseObj.addProperty("responseCode", 101);
				 responseObj.addProperty("responseMsg", "Please Enter UserName/Password");
				 throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity(new Gson().toJson(responseObj)).build());
			} else {
				for(Entry<String, MerchantInfoBean> entrySet : Util.merchantInfoMap.entrySet()) {
					if(userName.equals(entrySet.getValue().getUserName()) && password.equals(entrySet.getValue().getPassword())) {
						TransactionManager.setMerchantId(entrySet.getValue().getMerchantId());		
					}
				}
				
/*				try {
					merchantId = MerchantAuthorizationHelper.merchantAuthorization(userName, password);
					TransactionManager.setMerchantId(merchantId);
				} catch (SLEException e) {
					throw new WebApplicationException(Status.UNAUTHORIZED);
				}*/
			}

			if (TransactionManager.getMerchantId() == 0) {
				responseObj = new JsonObject();
				responseObj.addProperty("responseCode", 101);
				responseObj.addProperty("responseMsg", "Invalid Merchant");
				throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity(new Gson().toJson(responseObj)).build());
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(req.getEntityInputStream(), writer, "UTF-8");

			// This is your POST Body as String
			String body = writer.toString();
			logger.info("AUDIT_REQUEST_ID="+TransactionManager.getAuditId()+" , merchantId="+TransactionManager.getMerchantId()+" , requestTime="+Util.getCurrentTimeStamp()+" , requestUri="+req.getRequestUri()+" , requestData="+body);

			// System.out.println(req.getRequestHeader("principal"));
			InputStream in = new ByteArrayInputStream(body.getBytes());
			req.setEntityInputStream(in);
		} catch (IOException e) {
		}
		return req;
	}

	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {

		// System.out.println(Thread.currentThread().getId());
		logger.info("AUDIT_RESPONSE_ID="+TransactionManager.getAuditId()+" , TimeTaken="+(System.currentTimeMillis() - TransactionManager.getAuditTime()));
		logger.info("AUDIT_RESPONSE_ID="+TransactionManager.getAuditId()+" , responseTime="+Util.getCurrentTimeStamp()+" , responseStatus="+response.getStatus()+" , statusType="+response.getStatusType()+" , requestUri="+request.getRequestUri()+" , responseData="+response.getEntity());
		TransactionManager.endTransaction();
		return response;
	}

}