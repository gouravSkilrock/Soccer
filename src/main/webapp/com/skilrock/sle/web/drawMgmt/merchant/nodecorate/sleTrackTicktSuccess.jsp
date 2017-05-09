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
    <div class="container" style="margin-top:8px"></div>
    <div class="container">
		<div class="col-md-1"></div>
			<div class="col-md-11" id="approvedResultDiv">						
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Track Ticket Details</h3>
					</div>
					<div class="box-body">
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
							<div  class="panel panel-default col-md-12 box-panel-style" style="max-height: 700px;overflow: auto;">
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style">Game</th>
												<th class="table-header-style">Game Type</th>
												<s:if test="%{countryDeployed=='ZIMBABWE'}">
													<th class="table-header-style">Merchant Name</th>
												</s:if>
												<th class="table-header-style">Draw Name</th>
												<th class="table-header-style">Draw Date Time</th>	
												<th class="table-header-style">Draw Status</th>
												<th class="table-header-style">Purchase Date Time</th>
																								
											</tr>
											<tr>
												<td><s:property value="trackTicketBean.gameName"/></td>
												<td><s:property value="trackTicketBean.gameTypeName"/></td>
												<s:if test="%{countryDeployed=='ZIMBABWE'}">
													<td><s:property value="trackTicketBean.merchantName"/></td>
												</s:if>
												<td><s:property value="trackTicketBean.drawName"/></td>
												<td><s:property value="trackTicketBean.drawDate"/> <s:property value="trackTicketBean.drawTime"/></td>
												<td><s:property value="trackTicketBean.drawStatus"/></td>
												<td><s:property value="trackTicketBean.purchaseDate"/> <s:property value="trackTicketBean.purchaseTime"/></td>														
											</tr>
										</tbody>
									</table>									
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
										
											<s:if test="%{trackTicketBean.tktStatus=='CANCELLED'}">
												<tr> 
													 <td colspan="7">CANCELLED TICKET</td>	
												</tr>
											</s:if>
											<s:elseif test="%{trackTicketBean.rpcCount > 0}">
												 <td colspan="7">Ticket has been reprinted <s:property value="trackTicketBean.rpcCount "/> times.</td>
											</s:elseif>
											
											<tr>
												<th class="table-header-style" colspan="8">Ticket Details</th>
											</tr>
											<tr>
												<th class="table-header-style">Ticket No</th>
												<th class="table-header-style">No. of Events</th>
												<th class="table-header-style">No. of Lines</th>
												<th class="table-header-style">No. of Board</th>
												<th class="table-header-style">Unit Price</th>
												<th class="table-header-style">Total Purchase Amount</th>
												<th class="table-header-style">Total Winning Amount</th>														
												<s:if test="%{trackTicketBean.merchantName=='PMS' && trackTicketBean.winStatus=='SETTLEMENT PENDING'}">
													<th class="table-header-style">Winning Status</th>
												</s:if>				
											</tr>
											<tr>
												<td><s:property value="trackTicketBean.ticketNumber"/></td>
												<td><s:property value="trackTicketBean.noOfEvents"/></td>
												<td><s:property value="trackTicketBean.noOfLines"/></td>
												<td><s:property value="trackTicketBean.noOfBoard"/></td>
												<td><s:property value="getText('{0,number,#,##0.00}', {trackTicketBean.unitPrice})"/></td>
												<td><s:property value="getText('{0,number,#,##0.00}', {trackTicketBean.totalAmount})"/></td>
												<s:if test="%{trackTicketBean.drawStatus=='CLAIM ALLOW' || trackTicketBean.drawStatus=='CLAIM PENDING'}">
													<td><s:property value="getText('{0,number,#,##0.00}', {trackTicketBean.totalWinAmt})"/></td>
												</s:if>	
												<s:else>
													<td>NA</td>
												</s:else>		
												<s:if test="%{trackTicketBean.merchantName=='PMS' && trackTicketBean.winStatus=='SETTLEMENT PENDING'}">
													<td><s:property value="trackTicketBean.winStatus"/></td>	
												</s:if>									
											</tr>
										</tbody>
									</table>
									<s:set name="eventDetails" value="trackTicketBean.eventDetails"></s:set>
									
									<table id="tableData" class="table table-bordered table-striped panel-table-border-style" style="text-align:center">
										<tbody>
											<tr>
												<th colspan="7" class="table-header-style">Event Details</th>
											</tr>
											<tr>
												<th class="table-header-style">Event Name</th>
												<th class="table-header-style">Event Description</th>
												<th class="table-header-style">Option Name</th>														
											</tr>
											<s:iterator value="eventDetails">
											<tr>										
												<td><s:property value="eventDisplay"/></td>
												<td><s:property value="eventDescription"/></td>									
												<td><s:property value="optionName"/></td>
											</tr>
											</s:iterator>
										</tbody>
									</table>
								

									<hr class="panel-hr-style">
									<div class="col-md-5 left-button-div-style"></div>
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
