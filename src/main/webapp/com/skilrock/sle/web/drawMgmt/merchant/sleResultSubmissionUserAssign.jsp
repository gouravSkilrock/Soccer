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
		<div class="col-md-1"></div>
			<div class="col-md-10" id="approvedResultDiv">						
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Result Submission User Assign</h3>
					</div>
					<div class="box-body">
							<div class="panel panel-default col-md-12 box-panel-style">
								<s:form cssClass="form-horizontal"  theme="simple" method="post" action="userAssignSubmit" name="assignForm" id="assignForm" onsubmit=" return validatedResultSubmissionUserAssignForm();">
									<s:hidden id="userListString" name="userListString" value="" />
									<div class="form-group">
                                            <div class="col-md-4" align="right"><label class="control-label" for="inputSuccess"> Select Game Name</label></div>
                                            <div class="col-md-6" align="left" >
												<s:select id="gameId" headerKey="-1" headerValue="--Please Select--"
													name="gameId" list="%{gameMap}" listKey="key" listValue="%{value.gameDispName}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
												<s:hidden id="gameType" name="gameType" value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(gameMap)}" />
											</div>
											<div class="col-md-4"></div><div class="col-md-6">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<!--<small class="col-md-12 small-tag-style-for-success"></small>-->
											</div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-4" align="right"><label class="control-label" for="inputSuccess"> Select Game Type</label></div>
                                            <div class="col-md-6" align="left">
												<s:select id="gameTypeId" headerKey="-1" headerValue="--Please Select--"
												name="gameTypeId" list="{}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
											</div>
											<div class="col-md-4"></div><div class="col-md-6">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
											</div>
                                        </div>
									<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
									<table style="text-align:center" class="table table-bordered table-striped panel-table-border-style" id="tableData">
										<tbody>
											<tr>
												<th class="table-header-style" colspan="2">User Assign</th>
											</tr>
											<tr>
												<td colspan="2">
													<div class="col-md-12">
														<div class="col-md-4">
															<s:select id="user1" headerKey="-1" headerValue="--Please Select User1--"
													name="user1" list="userMap" listKey="key" listValue="value" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
														</div>
														<div class="col-md-4">
															<s:select id="user2" headerKey="-1" headerValue="--Please Select User2--"
													name="user2" list="userMap" listKey="key" listValue="value" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
														</div>
														<div class="col-md-4">
															<s:select id="user3" headerKey="-1" headerValue="--Please Select User3--"
													name="user3" list="userMap" listKey="key" listValue="value" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
														</div>
														</div>
														<div class="col-md-12" style="margin:2px"></div>
														<div class="col-md-12">
														<div class="col-md-4">
															<s:select id="user4" headerKey="-1" headerValue="--Please Select User4--"
													name="user4" list="userMap" listKey="key" listValue="value" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
														</div>
														<div class="col-md-4">
															<s:select id="user5" headerKey="-1" headerValue="--Please Select User5--"
													name="user5" list="userMap" listKey="key" listValue="value" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
														</div>
													</div>																	
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<div class="col-md-5 left-button-div-style"></div>
													<div class="col-md-7 right-button-div-style" align="left" ><button type="submit" class="btn btn-primary">Save</button></div>
												</td>											
											</tr>
										</tbody>
									</table>
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
$(document).ready(function() {
	var gameTypeData = $('#gameType').val();
	var gameTypeMap = jQuery.parseJSON(gameTypeData);
	
	$('#gameId').change(function() {					
		if($(this).val()!=-1) {
			$('#gameTypeId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
			var gameId = $(this).val();
			var gameTypeList = gameTypeMap[gameId].gameTypeMasterList;

			if (gameTypeList!=undefined && gameTypeList.length>0) {
				$.each(gameTypeList, function(key, value) {
					 $('#gameTypeId').append($('<option></option>').val(value.gameTypeId).html(value.gameTypeDispName));
				});
			} else {
				$('#gameTypeId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
			}
		} else {
			$('#gameTypeId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
			$('#user1').val(-1);
			$('#user2').val(-1);
			$('#user3').val(-1);
			$('#user4').val(-1);
			$('#user5').val(-1);
		}
	});
	
	$('#gameTypeId').change(function() {		
		var requestParam='<%=request.getContextPath()%>/com/skilrock/sle/web/merchantUser/drawMgmt/Action/getAssignedUser.action?gameId='+$("#gameId").val()+'&gameTypeId='+$("#gameTypeId").val();
		var userData = _ajaxCallJson(requestParam,'','');
		$.each(userData, function(key, value) {
			//$('#user1').append($('<option></option>').val(value).html(key));
			$('#user'+(key+1)).val(value);
		});
	});
});
</script>
</body>
</html>
