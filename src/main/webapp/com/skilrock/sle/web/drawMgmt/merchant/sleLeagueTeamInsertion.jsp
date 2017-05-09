<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>

<html lang="en-US">
	<head>
		<title>Sports Lottery | League Team Insertion</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-2.1.0.min.js"></script>
		<script src="<%=request.getContextPath()%>/js/commonApp.js"></script>
		<script>var path="<%=request.getContextPath()%>";</script>
		<script>
			var league = [];
			var team = [];
			var teamCode = [];
			$(document).ready(function() {
				$('#league option').each(function() {
						league.push($(this).attr('value'));
				});
		
				$('#team option').each(function() {
						team.push($(this).attr('value'));
				});
				$('#teamCode option').each(function() {
					teamCode.push($(this).attr('value'));
				});
				commonApp.init($);
			});
		</script>
	</head>
	<body class=" page-account" id="page-top">
		<div id="page-content">
			<div class="container margin-top-for-first-container"></div>
			<div class="container">
				<div class="col-md-12">
					<div class="box box-info box-style">
						<div class="box-header box-header-style">
							<i class="fa fa-gear"></i>
							<h3 class="box-title">League Team Management</h3>
						</div>
						<div class="box-body">
							<div class="panel panel-default col-md-12 box-panel-style" id="gameDiv">
								<s:form cssClass="form-horizontal" method="post" id="mainForm" name="mainform" theme="simple">
									<div class="col-lg-12 col-md-12 padding-15">
										<div class="col-md-3" align="right">
											<label class="control-label">Select Any Game</label>
										</div>
										<div class="col-md-9" align="left">
											<s:select id="gameId" headerKey="-1" headerValue="--Please Select--" name="gameId"
												list="%{gameMap}" listKey="key"	listValue="%{value.gameDispName}"
												cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
										</div>
									</div>
									<div class="col-lg-12 col-md-12">
										<div class="col-md-3"></div>
										<div class="col-md-9 right-button-div-style" style="display:none;" align="right" id="options">
											<input type="button" value="Add League" id="addLeague" class="btn btn-primary pull-left margin_right_two" />
											<input type="button" value="Team Management" id="teamMgmt" class="btn btn-primary pull-left margin_right_two" />
											<input type="button" value="Map League-Team" id="mapLeagueTeam" class="btn btn-primary pull-left" />
										</div>
									</div>
								</s:form>
							</div>
						</div>
						<div class="box-body">
							<div id="addLeagueDiv" class="panel panel-default col-md-12 box-panel-style display-none">
								<s:form cssClass="form-horizontal" method="post" action="sleAddLeague" theme="simple" id="addLeagueForm" name="addLeagueForm">
									<div class="col-lg-12 col-md-12 padding-15">
										<div class="col-md-3" align="right">
											<label class="control-label">Enter League Name</label>
										</div>
										<div class="col-md-9" align="left">
											<s:textfield id="leagueName" name="leagueName" cssClass="input_box col-md-6" maxlength="50"></s:textfield>
											<span id="league_exists_error" class="err"></span>
											<span id="l_error1" class="err"></span>
										</div>
									</div>
									<div class="col-lg-12 col-md-12">
										<div class="col-md-3" align="center">
											<s:select id="league" headerKey="-1" name="leagueId" list="%{leagueMap}" 
											listKey="%{value.leagueDispName}" listValue="%{value.leagueDispName}"	
											cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"
											cssStyle="display:none;"></s:select>
										</div>
										<div class="col-md-5 right-button-div-style" align="center">
											<s:submit value="Submit" id="addLeagueBtn" cssClass="btn btn-primary pull-left"></s:submit>
										</div>
										<div class="col-md-4" align="center">
										</div>
									</div>
								</s:form>
							</div>
							<div id="addTeamDiv" class="panel panel-default col-md-12 box-panel-style" style="display: none;">
								<s:form cssClass="form-horizontal" method="post" action="sleAddTeam" theme="simple" id="addTeamForm" name="addTeamForm">
									<div class="col-lg-12 col-md-12 padding-15">
									<div class="col-md-3"></div>
										<div class="col-md-3">
											<label><input type="radio" value="Add New Team" id="addNewTeamBtn"
												class="btn btn-primary pull-left margin_right_two margin_radio"
												checked="checked">&nbsp;
												Add New Team
											</label>
										</div>
										<div class="col-md-3">
										<label ><input type="radio" value="Update Team" id="updateTeamBtn"
											class="btn btn-primary pull-left margin_right_two margin_radio">&nbsp;
											Update Team Information
										</label>
										</div>
										<div class="col-md-3"></div>
									</div>
									<div id="addNewTeam" class="panel panel-default col-md-12 box-panel-style">
										<div class="col-lg-12 col-md-12 padding-15">
											<div class="col-md-3" align="right">
												<label class="control-label">Enter Team Name</label>
											</div>
											<div class="col-md-9" align="left">
												<s:textfield id="teamName" name="teamName" cssClass="input_box col-md-6"></s:textfield>
												<span id="t_error1" class="err"></span>
												<span id="team_exists_error" class="err"></span>
											</div>
										</div>
										<div class="col-lg-12 col-md-12 padding-15">
											<div class="col-md-3" align="right">
												<label class="control-label">Enter Team Code</label>
											</div>
											<div class="col-md-9" align="left">
												<s:textfield id="teamCode" name="teamCode"
													cssClass="input_box col-md-6" maxlength="4"></s:textfield>
												<span id="t_error2" class="err"></span>
												<span id="teamCode_exists_error" class="err"></span>
											</div>
										</div>
										<s:select id="team" name="teamId" list="%{teamMap}"
											listKey="%{value.teamName}" listValue="%{value.teamName}"
											cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"
											cssStyle="display:none;"></s:select>

										<s:select id="teamCode" name="teamCode" list="%{teamMap}"
											listKey="%{value.teamCode}" listValue="%{value.teamCode}"
											cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"
											cssStyle="display:none;"></s:select>

										<div class="col-lg-12 col-md-12">
											<div class="col-lg-3 col-md-3"></div>
											<div class="col-md-9 right-button-div-style" align="center">
												<s:submit value="Submit" id="addTeamBtn"
													cssClass="btn btn-primary pull-left"></s:submit>
											</div>
										</div>
									</div>
									<div id="updateTeamInfo" style="display: none;"></div>
								</s:form>
							</div>

							<div id="mapLeagueTeamDiv" class="panel panel-default col-md-12 box-panel-style"
								style="display: none;">

							</div>
						</div>
					</div>
					<div id="result"></div>
				</div>
			</div>
		</div>

	</body>
</html>
