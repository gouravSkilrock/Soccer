<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String nameSpacePath = path+"/com/skilrock/sle/web/merchantUser/drawMgmt/Action/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Track Ticket Details</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
  </head>
  
  <body>
  <div class="wrapper">
    <div id="page-content">
    <div class="container" style="margin-top:2px"></div>
    <div class="container">
		<div class="col-md-1"></div>
			<div class="col-md-11" id="approvedResultDiv">						
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Ticket Details</h3>
					</div>
					<div class="box-body">
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
							<div class="panel panel-default col-md-12 box-panel-style">
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style">Game</th>
												<th class="table-header-style">Game Type</th>
												<th class="table-header-style">Merchant Name</th>
												<th class="table-header-style">Ticket Number</th>
												<th class="table-header-style">Total Winning Amount</th>
											</tr>
											<tr>
												<td><s:property value="verifyTicketBean.gameName"/></td>
												<td><s:property value="verifyTicketBean.gameTypename"/></td>
												<td><s:property value="verifyTicketBean.merchantName"/></td>
												<td><s:property value="verifyTicketBean.ticketNumber"/></td>														
												<td><s:property value="getText('{0,number,#,##0.00}',{verifyTicketBean.totalWinAmt})"/></td>
											</tr>
										</tbody>
									</table>
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style" colspan="6">Draw Details</th>
											</tr>
											<tr>
												<th class="table-header-style">Draw Name</th>
												<th class="table-header-style">Draw Time</th>
												<th class="table-header-style">Draw Status</th>
												<th class="table-header-style">Winning Amount</th>
												<th class="table-header-style">Status</th>
											</tr>
											<s:iterator var="currentElement" value="verifyTicketBean.verifyTicketDrawDataBeanArray">
												<tr>
													<s:set name="drawStatus" value="%{#currentElement.drawStatus}" />
													<s:set name="ticketStatus" value="%{#currentElement.status}" />
													<s:set name="rankId" value="%{#currentElement.rankId}" />

													<td><s:property value="%{#currentElement.drawName}"/></td>
													<td><s:property value="%{#currentElement.drawDateTime}"/></td>
													<td><s:property value="%{#currentElement.drawStatus}"/></td>
													<td><s:property value="getText('{0,number,#,##0.00}',{#currentElement.drawWinAmt})"/></td>
													<td><s:property value="%{#currentElement.status}"/></td>
												</tr>
											</s:iterator>
										</tbody>
									</table>
									<hr class="panel-hr-style">
									<div class="col-md-5 left-button-div-style"></div>
									<div class="col-md-12 right-button-div-style" align="center">
									<s:if test="%{#drawStatus=='CLAIM ALLOW' and #ticketStatus=='UNCLAIMED' and #rankId != 0}">
										<s:form method="POST" theme="simple" action="payPwt" onsubmit="return isVerificationCodeValid();">
											<div class="col-md-12">
												<s:hidden name="ticketNumber" value="%{verifyTicketBean.ticketNumber}" />
												<s:hidden name="winningAmount" value="%{verifyTicketBean.totalWinAmt}" />
												<s:hidden name="saleMerCode" value="%{verifyTicketBean.merchantName}" />
											</div>
											<s:if test ="%{verifyTicketBean.merchantName=='Asoft'}">
												<div class="col-md-12">
													<div class="col-md-5" align="right">
														<label class="control-label" for="inputSuccess">
															Enter Verification Code
														</label>
													</div>
													<div class="col-md-3" align="left">
														<s:textfield name="verificationCode" id="verificationCode" maxlength="7" cssClass="form-control" placeholder="Enter Verification Code"/>
													</div>
													<div class="col-md-4" align="center">
														<s:submit value="Pay" theme="simple" cssClass="btn btn-primary"/>
													</div>
													<div class="col-md-5" align="right"></div>
													<div class="col-md-3" align="left">
														<small id="verficationCodeError" class="col-md-12 small-tag-style-for-error" style="display:none;"></small>
													</div>
													<div class="col-md-4" align="center"></div>
												</div>
											</s:if>
											<s:else>
												<div class="col-md-12">
													<div class="col-md-2" align="right"></div>
													<div class="col-md-8" align="center">
														<s:submit value="Pay" theme="simple" cssClass="btn btn-primary"/>
													</div>
													<div class="col-md-2" align="left"></div>
												</div>
											</s:else>
										</s:form>
									</s:if>
									
									</div>
							</div>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div>
					
							
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"></script>
</body>
</html>