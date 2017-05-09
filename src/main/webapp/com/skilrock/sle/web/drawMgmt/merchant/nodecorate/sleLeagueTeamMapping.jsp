<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<div class="panel panel-default col-md-12 box-panel-style" id="mapLeagueTeamDiv">
	<s:form cssClass="form-horizontal" method="post" action="#" theme="simple" id="mapLeagueTeamForm" name="mapLeagueTeamForm">
		<div class="col-lg-12 padding-15">
			<div class="form-group col-md-5 margin-div-style">
				<div class="col-md-12 margin-40" align="left">
					<h4 class="margin-105">Unmapped Teams</h4>
					<s:select id="allTeam" headerKey="-1" name="allTeam"
						list="%{unMappedTeamMasterList}" listKey="key" listValue="%{value.teamName}" 
						cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12 txt-align"
						size="10" multiple="true"></s:select>
				</div>
			</div>
			<div class="form-group col-md-2 margin-div-style margin-imp">
				<ul id="btn" Class="list_arrow_add">
					<li>
						<a href="" id="add">&gt;</a>
					</li>
					<li>
						<a href="" id="remove">&lt;</a>
					</li>
				</ul>
			</div>
			<div class="form-group col-md-5 margin-div-style">
				<div class="col-md-12 margin-40" align="left">
						<h4 class="margin-105">Mapped Teams</h4>
						<s:hidden id="gameId" name="gameId" value="%{gameId}"></s:hidden>
						<s:hidden id="leagueId" name="leagueId" value="%{leagueId}"></s:hidden>
						<s:select id="mappedTeam" headerKey="-1" name="mappedTeam"
								list="%{mappedTeamMasterList}" listKey="key" listValue="%{value.teamName}"
								cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12 txt-align"
								size="10" multiple="true">
						</s:select>
							
						<s:select id="alreadyMapped" headerKey="-1" name="alreadyMapped"
								list="%{mappedTeamMasterList}" listKey="key" listValue="%{value.teamName}"
								cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"
								cssStyle="display:none;"></s:select>
				</div>
			</div>
		</div>
		<div class="col-lg-12">
			<div class="col-lg-5"></div>
			<div class="col-md-7 right-button-div-style" align="left">
				<input type="button" value="Submit" id="mapLeagueTeamBtn" class="btn btn-primary pull-left margin-btn" />
			</div>
			<div class="col-lg-12"></div>
		</div>
	</s:form>
</div>

<script>
	$(document).ready(function() {
		commonApp.init($);
	});
</script>