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
    
    <title>Update Draw Data success</title>
    
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
    <div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Draw Detail</h3>
					</div>
					<div class="box-body" >
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv2" class="col-md-12 alert alert-danger"></div>
							<div class="panel panel-default col-md-12 box-panel-style" id="drawTableDiv">
							
									<s:hidden name="gameId"  value="%{gameId}" />
									<s:hidden name="gameTypeId" value="%{gameTypeId}" />
									<s:hidden name="drawId" value="%{drawInfo.drawId}" />
									<s:set name="merchantCode" value="#session.MER_CODE" />
									<table id="tableData" class="table table-bordered table-striped panel-table-border-style" style="text-align:center">
												<tbody>
													<tr align="center">
													<s:if test="%{drawInfo.eventTimeChangeReq == 'YES'}">
														<th colspan="6"  class="col-md-12 alert alert-danger"><center>Unsuccessful !! Freeze time can't be greater than event start time. <a href="<s:url action="eventMgmtMenu"/>">click here</a> to change event time.</center></th>
													</s:if>
													<s:else>
														<th colspan="6"  class="col-md-12 alert alert-danger"><center><s:property value="drawInfo.message"/></center></th>
													</s:else>
														
													</tr>
													<tr>
														<th class="table-header-style">Draw Id</th>
														<th class="table-header-style">Draw Name</th>
														<th class="table-header-style">Draw Status</th>
														<th class="table-header-style">Sale Start Time</th>
														<th class="table-header-style">Draw Freeze Time</th>
														<th class="table-header-style">Draw Date Time</th>														
													</tr>
													<tr>
														<td><s:property value="drawInfo.drawId"/></td>
														<!-- <s:hidden id="selectedDrawId" name="drawId" value="%{drawId}" /> --> 
														<td><s:property value="drawInfo.drawName"/></td>
														<td><s:property value="drawInfo.drawStatus"/></td>
														<td><s:property value="drawInfo.saleStartTime"/></td>
														<td><s:property value="drawInfo.drawFreezeTime"/></td>
														<td><s:property value="drawInfo.drawDateTime"/></td>														
													</tr>
												</tbody>
											</table>
											<hr class="panel-hr-style">
										
										<a href="<%=request.getContextPath() %>/com/skilrock/sle/web/merchantUser/drawMgmt/Action/drawMgmtMenu.action" class="btn btn-link">Back to Draw Search</a>		
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
