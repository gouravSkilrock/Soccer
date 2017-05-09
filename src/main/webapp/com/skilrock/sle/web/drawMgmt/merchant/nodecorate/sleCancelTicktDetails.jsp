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
    
    <title>Ticket Details</title>
    
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
			<div class="col-md-10" id="approvedResultDiv">						
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Ticket Details</h3>
					</div>
					<div class="box-body">
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
								<s:form cssClass="form-horizontal" method="post" action="boCancelTicketSuccess" theme="simple" id="f1" onsubmit="return formSubmit(this.id,'resultDiv');">
							<div  class="panel panel-default col-md-12 box-panel-style">
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style">Game</th>
												<th class="table-header-style">Game Type</th>
												<th class="table-header-style">Draw Name</th>
												<th class="table-header-style">Draw Date Time</th>	
												<th class="table-header-style">Purchase Date Time</th>
																								
											</tr>
											<tr>
												<td><s:property value="trackTicketBean.gameName"/></td>
												<td><s:property value="trackTicketBean.gameTypeName"/></td>
												<td><s:property value="trackTicketBean.drawName"/></td>
												<td><s:property value="trackTicketBean.drawDate"/> <s:property value="trackTicketBean.drawTime"/></td>
												<td><s:property value="trackTicketBean.purchaseDate"/> <s:property value="trackTicketBean.purchaseTime"/></td>														
											</tr>
										</tbody>
									</table>									
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style" colspan="6">Ticket Details</th>
											</tr>
											<tr>
												<th class="table-header-style" >Ticket No</th>
												<th class="table-header-style">No. of Events</th>
												<th class="table-header-style">No. of Lines</th>
												<th class="table-header-style">No. of Board</th>
												<th class="table-header-style">Unit Price</th>
												<th class="table-header-style">Total Amount</th>														
											</tr>
											<tr>
											<s:hidden name="ticketNumber"  id="ticketNumber"  value="%{trackTicketBean.ticketNumber}"></s:hidden>
											<s:hidden name="transId"  id="transId"  value="%{trackTicketBean.transId}"></s:hidden>
											<s:hidden name="merchantUserId"  id="merchantUserId"  value="%{trackTicketBean.merchantUserId}"></s:hidden>
											
												<td><s:property value="trackTicketBean.ticketNumber"/></td>
												<td><s:property value="trackTicketBean.noOfEvents"/></td>
												<td><s:property value="trackTicketBean.noOfLines"/></td>
												<td><s:property value="trackTicketBean.noOfBoard"/></td>
												<td><s:property value="trackTicketBean.unitPrice"/></td>
												<td><s:property value="trackTicketBean.totalAmount"/></td>														
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
										<div class="col-md-4"></div>
											<div class="col-md-12">
												<hr class="panel-hr-style">
											</div>
											<div class="col-md-5 left-button-div-style"></div>
											<div class="col-md-7 right-button-div-style" align="left">
												<s:submit type="submit" cssClass="btn btn-primary" value="Cancel Ticket" />
											</div>
							</div>
							</s:form>
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
