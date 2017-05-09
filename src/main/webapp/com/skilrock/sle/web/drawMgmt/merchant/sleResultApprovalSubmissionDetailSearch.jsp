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
    
    <title>Result Submission Data Search</title>
    
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
			<div class="col-md-12" id="approvedResultDiv">						
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Result Submission Details</h3>
					</div>
					<div class="box-body">
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
							<div  class="panel panel-default col-md-12 box-panel-style">
								<s:form cssClass="form-horizontal"  theme="simple" method="post" action="resultApprovalSubmit" name="approvalForm" id="approvalForm" onsubmit=" return validateApprovedResultSubmissionForm();">
									<s:hidden name="gameId" value="%{gameId}" />
									<s:hidden name="gameTypeId" value="%{gameTypeId}" />
									<s:hidden name="drawId" value="%{drawId}" />
									<s:hidden name="eventResult" id="eventResult" value="" />
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style">Game</th>
												<th class="table-header-style">Game Type</th>
												<th class="table-header-style">Draw Id</th>
												<th class="table-header-style">Draw Name</th>
												<th class="table-header-style">Draw Date Time</th>													
											</tr>
											<tr>
												<td><s:property value="approvalBean.gameDispName" /></td>
												<td><s:property value="approvalBean.gameTypeDispName" /></td>
												<td><s:property value="drawId" /></td>
												<td><s:property value="approvalBean.drawName" /></td>
												<td><s:property value="approvalBean.drawDate" /></td>														
											</tr>
										</tbody>
									</table>									
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style" colspan="3">Users Details</th>
											</tr>
											<tr>
												<th class="table-header-style">User Name</th>
												<th class="table-header-style">Result Submission Time</th>														
											</tr>
											<tr>
												<td><s:property value="approvalBean.userName1" /> [First User]</td>
												<td><s:property value="approvalBean.userUpdateTime1" /></td>														
											</tr>
											<tr>
												<td><s:property value="approvalBean.userName2" /> [Second User]</td>
												<td><s:property value="approvalBean.userUpdateTime2" /></td>														
											</tr>
										</tbody>
									</table>
									<table id="tableData" class="table table-bordered table-striped panel-table-border-style" style="text-align:center">
										<tbody>
										<s:set name="optionCodeMap" value="%{approvalBean.optionCodeMap}"></s:set>
										<s:set name="size" value="#optionCodeMap.size"></s:set>
										<s:set name="headSize" value="#size+3"></s:set>
										
											<tr>
												<th colspan="<s:property value="#headSize"/>" class="table-header-style">Result Submission Details</th>
											</tr>
											<tr>
												<th class="table-header-style">Event Name</th>
												<th class="table-header-style"><input type="radio" name="userResult" value="1"> First User Result</th>
												<th class="table-header-style"> <input type="radio" name="userResult" value="2"> Second User Result</th>
												<th class="table-header-style" colspan="<s:property value="#size"/>"> <input type="radio" name="userResult" value="3"> Your Result </th>														
											</tr>
											<s:iterator value="approvalBean.userResult">
												<tr>										
													<td><s:property value="value.eventDescription" /></td>
													<td><s:property value="value.optionSelected1" /></td>									
													<td><s:property value="value.optionSelected2" /></td>
													<s:set name="evtId" value="value.eventId"></s:set>
													<s:iterator value="approvalBean.optionCodeMap">
													<td><label><input type="radio" name='opt<s:property value="%{#evtId}" />' value="<s:property value="%{value}"/>"> <s:property value="%{value}"/></label></td>
													</s:iterator>
												</tr>
											</s:iterator>
										</tbody>
									</table>

									<hr class="panel-hr-style">
									<div class="col-md-5 left-button-div-style"></div>
									<div class="col-md-7 right-button-div-style" align="left" ><button type="submit" class="btn btn-primary">Submit</button></div>
									
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

</body>
</html>
