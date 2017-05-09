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
    <div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Sports Game Result List</h3>
					</div>
					<div class="box-body" >
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv2" class="col-md-12 alert alert-danger"></div>
							<div class="panel panel-default col-md-12 box-panel-style" id="drawTableDiv">
							
							
								<s:form  theme="simple" cssClass="form-horizontal" method="post" action="getUnmatchedDrawDetails"  name="drawDetailsForm" id="drawDetailsForm" onsubmit="return validateDrawListForm();">
									<s:hidden name="gameId"  value="%{gameId}" />
									<s:hidden name="gameTypeId" value="%{gameTypeId}" />
									<s:hidden name="eventResult" id="eventResult" value="" />
									<table id="tableData" class="table table-bordered table-striped panel-table-border-style" style="text-align:center">
												<tbody>
													<tr>
														<th class="table-header-style">View Result</th>
														<th class="table-header-style">Draw Id</th>
														<th class="table-header-style">Draw Name</th>
														<th class="table-header-style">Draw Date Time</th>														
													</tr>
													<s:iterator value="approvalList">
														<tr>
															<td><label><input type="radio" value='<s:property value="drawId"/>' id="radioButton1" name="drawId"></label></td>
															<td><s:property value="drawId"/></td>
															<!-- <s:hidden id="selectedDrawId" name="drawId" value="%{drawId}" /> --> 
															<td><s:property value="drawName"/></td>
															<td><s:property value="drawDate"/></td>														
														</tr>
													</s:iterator>
												</tbody>
											</table>
											<hr class="panel-hr-style">
											<div class="col-md-5 left-button-div-style"></div>
											<div class="col-md-7 right-button-div-style" align="left" ><button type="submit" class="btn btn-primary">Search</button></div>
									
										</s:form>									
							</div>
						</div>
						
						</div>
	
				</div>
			</div>
							
							
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"></script>

</body>
</html>
