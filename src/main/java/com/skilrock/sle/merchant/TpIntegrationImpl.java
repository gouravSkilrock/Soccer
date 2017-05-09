package com.skilrock.sle.merchant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.tonybet.TonyBetUtils;
import com.skilrock.sle.merchant.tonybet.TonyBetUtils.ServiceMethod;
import com.skilrock.sle.merchant.weaver.WeaverUtils;
import com.skilrock.sle.merchant.weaver.WeaverUtils.ServiceMethods;
import com.skilrock.sle.merchant.weaver.WeaverUtils.TxnTypes;

public class TpIntegrationImpl {
	private static final Logger logger = LoggerFactory.getLogger(TpIntegrationImpl.class);
	public static String getLMSResponseString(String serviceName, String serviceMethodName, JsonObject jsonObj) throws GenericException{
		StringBuilder reqJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
//		logger.debug("***** Inside getLMSResponseString Function");
		StringBuilder urlString = new StringBuilder();
		MerchantInfoBean merchantInfoBean = null;
		try {
			merchantInfoBean = Util.merchantInfoMap.get("RMS");
			urlString.append(merchantInfoBean.getProtocol()).append("://").append(merchantInfoBean.getMerchantIp()).append(":")
				.append(merchantInfoBean.getPort()).append("/").append(merchantInfoBean.getProjectName())
				.append("/rest/").append(serviceName).append("/").append(serviceMethodName).append("?requestData=");
//			logger.debug("***** URL for LMS Calling "+urlString.toString());
			URL url = new URL(urlString.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName", "E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
			connection.setRequestProperty("password", "p@55w0rd");
			/*
			 * connection.setRequestProperty( "Cookie","JSESSIONID=" +
			 * "12ECA807E0C31320DE59FD6E355369A");
			 */
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

			Gson gson = new Gson();
			String json = gson.toJson(jsonObj);

			out.write(json);
			out.close();

//			logger.debug("Time Taken for LMS Request - "+(System.currentTimeMillis() - t1));

			int responseCode = connection.getResponseCode();
			reqJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
				return null;
			}
//			logger.debug("Response String From LMS "+reqJson.toString());
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqJson.toString();
	}

	public static String getPMSResponseString(String serviceName, String serviceMethodName, JsonObject jsonObj) throws GenericException {
		StringBuilder reqJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
//		logger.debug("***** Inside getPMSResponseString Function");
		StringBuilder urlString = new StringBuilder();
		MerchantInfoBean merchantInfoBean = null;
		try {
			merchantInfoBean = Util.merchantInfoMap.get("PMS");
			urlString.append(merchantInfoBean.getProtocol()).append("://").append(merchantInfoBean.getMerchantIp()).append(":")
				.append(merchantInfoBean.getPort()).append("/").append(merchantInfoBean.getProjectName())
				.append("/rest/").append(serviceName).append("/").append(serviceMethodName);
//			urlString.append(Util.serverPMSURL).append("rest/").append(serviceName).append("/").append(serviceMethodName);
//			logger.debug("***** URL for PMS Calling "+urlString.toString());
			URL url = new URL(urlString.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName", "E49B4EF3-1511-4B8B-86D2-CE78DA0F74D7");
			connection.setRequestProperty("password", "p@55w0rd");
			/*
			 * connection.setRequestProperty( "Cookie","JSESSIONID=" +
			 * "12ECA807E0C31320DE59FD6E355369A");
			 */
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

			Gson gson = new Gson();
			String json = gson.toJson(jsonObj);

			out.write(json);
			out.close();

//			logger.debug("Time Taken for PMS Request - "+(System.currentTimeMillis() - t1));

			int responseCode = connection.getResponseCode();
			reqJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
				return null;
			}
//			logger.debug("Response String From PMS "+reqJson.toString());
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqJson.toString();
	}
	
	public static String getWeaverResponseString(String serviceName, ServiceMethods serviceMethod, TxnTypes txnType, JsonObject jsonObj) throws GenericException {
		StringBuilder respJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		logger.info("***** Inside getWeaverResponseString Function"+jsonObj);
		StringBuilder urlString = new StringBuilder();
		MerchantInfoBean merchantInfoBean = null;
		try {
			merchantInfoBean = Util.merchantInfoMap.get("Weaver");
			urlString.append(merchantInfoBean.getProtocol()).append("://").append(merchantInfoBean.getMerchantIp()).append(":")
				.append(merchantInfoBean.getPort()).append("/").append(merchantInfoBean.getProjectName()).append(serviceName).append(serviceMethod);
			logger.info("***** URL for Weaver Calling {}", urlString.toString());
			URL url = new URL(urlString.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			long t1 = System.currentTimeMillis();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			
			// Set Authentication 
			JsonObject   vendorInfo = new JsonObject();
			vendorInfo.addProperty("userName", "sleuser");
			vendorInfo.addProperty("password", "12345678");
			jsonObj.add("vendorAuthenticationInfo", vendorInfo);
			
			//Set common parameters. 			
			jsonObj.addProperty("serviceCode", WeaverUtils.SERVICE_NAME);
			if(txnType!=null){
				jsonObj.addProperty("txnType", txnType.toString());	
			}
			
			
			Gson gson = new Gson();
			String json = gson.toJson(jsonObj);
			out.write(json);
			out.close();
			logger.info("request JSON for weaver :" + json);
			logger.debug("Time Taken for Weaver Request - {}", (System.currentTimeMillis() - t1));

			int responseCode = connection.getResponseCode();
			respJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					respJson.append(responseString);
				}
				logger.info("Response String From Weaver {}", respJson.toString());
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					respJson.append(responseString);
				}
				logger.info("Response String From Weaver {}", respJson.toString());
				return null;
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return respJson.toString();
	}

	public static String getTonyBetResponse(String serviceName,ServiceMethod serviceMethod,JsonObject jsonObject) throws GenericException{
		StringBuilder responseJson = null;
		MerchantInfoBean merchantInfoBean = null;
		HttpURLConnection connection = null;
		StringBuilder urlString = new StringBuilder();
		BufferedReader in = null;
		try {
			merchantInfoBean = Util.merchantInfoMap.get("TonyBet");
			urlString.append(merchantInfoBean.getProtocol()).append("://").append(merchantInfoBean.getMerchantIp()).append("/")
				.append(merchantInfoBean.getProjectName()).append(serviceName).append(serviceMethod);
			logger.info("***** URL for TonyBet Calling {}", urlString.toString());
			URL url = new URL(urlString.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			Gson gson = new Gson();
			String json = gson.toJson(jsonObject);
			out.write(json);
			out.close();
			
			int responseCode = connection.getResponseCode();
			responseJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					responseJson.append(responseString);
				}
				logger.info("Response String From TonyBet {}", responseJson.toString());
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					responseJson.append(responseString);
				}
				logger.info("Response String From TonyBet {}", responseJson.toString());
				return null;
			}
			in.close();
		}catch (MalformedURLException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE, SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseJson.toString();
	}

}
