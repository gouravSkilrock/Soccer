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
    
    <title>Update Draw  Data </title>
    
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
						<i class="fa fa-gear"></i><h3 class="box-title">Draw To Change Freeze Time</h3>
					</div>
					<div class="box-body" >
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv2" class="col-md-12 alert alert-danger"></div>
							<div class="panel panel-default col-md-12 box-panel-style" id="drawTableDiv">
							
							
								<s:form  theme="simple" cssClass="form-horizontal" method="post" action="updateDrawInfo"  name="drawDetailsForm" id="drawDetailsForm" onsubmit="return validateUpdateFreeze()" >
									<s:hidden name="gameId"  value="%{gameId}" />
									<s:hidden name="gameTypeId" value="%{gameTypeId}" />
									<s:hidden name="drawId" value="%{drawInfo.drawId}" />
									<s:hidden name="drawStatus" value="%{drawInfo.drawStatus}" />
									<s:hidden name="eventResult" id="eventResult" value="" />
									<s:set name="merchantCode" value="#session.MER_CODE" />
									<table id="tableData" class="table table-bordered table-striped panel-table-border-style" style="text-align:center">
												<tbody>
													<tr align="center">
														<th colspan="6" ><center>Draw List</center></th>
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
											<br>
											<s:if test="%{#merchantCode =='RMS'}">
											<div class="col-md-12" align="left">
												<s:checkbox name="logoutAllRet" id="logoutAllRet" fieldValue="true" value="this.value"></s:checkbox> I agree that all the retailers will be logged out 
											</div>
											</s:if>
											<br>
											<br>
											<div class="form-group col-md-6 margin-div-style">
											<div class="col-md-5" align="left">
													Change Freeze Time (In Seconds)
											</div>
											<div class="col-md-3" align="left">
												<s:textfield id="seconds" name="seconds" maxlength="6" cssClass="form-control" />
											</div>
											
											<div class="col-md-2 left-button-div-style"></div>
											<div class="col-md-2 right-button-div-style" align="left" ><button type="submit" class="btn btn-primary" id="updFrz">Update Freeze Time</button></div>
									
											</div>
											
											<div class="col-md-12">
											<div class="col-md-10">
											</div><div class="col-md-2">
											<a href="<%=request.getContextPath() %>/com/skilrock/sle/web/merchantUser/drawMgmt/Action/drawMgmtMenu.action" class="btn btn-link">Back to Draw Search</a>
											</div>
											</div>
											
										</s:form>									
							</div>
						</div>
						
						</div>
	
				</div>
			</div>
		</div>
	</div>
			
							
							
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"></script>
<script type="text/javascript">

</script>

</body>
</html>
