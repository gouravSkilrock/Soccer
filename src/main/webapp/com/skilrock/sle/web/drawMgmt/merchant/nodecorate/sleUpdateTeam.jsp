<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
	<head>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/select2.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/select2.css"/>
		<script>
				$(document).ready(function() {
					commonApp.init($);
					$("#teamInfo").select2();
				});
		</script>
	</head>
	<body>
		<div id="updateTeam" class="panel panel-default col-md-12 box-panel-style">
			<div id="updateInfo">
				<s:form cssClass="form-horizontal" method="post" action="#" theme="simple" id="updateTeam" name="updateTeam">
					<div class="col-lg-12 padding-15">
						<div class="col-md-3" align="right">
							<label class="control-label">Select Any Team</label>
						</div>
						<div class="col-md-9" align="left">
							<s:select id="teamInfo" name="teamId" headerKey="-1" headerValue="--Please Select--"
								list="%{teamMap}" listKey="key" listValue="%{value.teamName}"
								cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
						</div>
					</div>
					<div id="updateTeamData" style="display: none;">
						<div class="col-lg-12 padding-15">
							<div class="col-md-3" align="right">
								<label class="control-label">Enter Team Name</label>
							</div>
							<div class="col-md-9" align="left">
								<s:textfield id="updateTeamName" name="teamName" cssClass="input_box col-md-6"></s:textfield>
								<span id="update_t_error1" class="err"></span>
								<span id="update_team_exists_error" class="err"></span>
							</div>
						</div>
						<div class="col-lg-12 padding-15">
							<div class="col-md-3" align="right">
								<label class="control-label">Enter Team Code</label>
							</div>
							<div class="col-md-9" align="left">
								<s:textfield id="updateTeamCode" name="teamCode" cssClass="input_box col-md-6" maxlength="4"></s:textfield>
								<span id="update_t_error2" class="err"></span>
								<span id="update_teamCode_exists_error" class="err"></span>
							</div>
						</div>
						<div class="col-lg-12">
							<div class="col-lg-3"></div>
							<div class="col-md-9 right-button-div-style" align="center">
								<s:submit value="Update" id="updateTeamInfoBtn" cssClass="btn btn-primary pull-left"></s:submit>
							</div>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</body>

</html>