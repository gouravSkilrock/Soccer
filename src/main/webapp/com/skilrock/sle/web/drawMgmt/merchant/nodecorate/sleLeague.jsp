<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<s:form cssClass="form-horizontal" method="post" action="fetchTeamsDetail" theme="simple" id="mapLeagueTeamForm" name="mapLeagueTeamForm">
	<div id="leagueDiv">
		<div class="col-lg-12 padding-15">
			<div class="col-md-3" align="right">
				<label class="control-label">Select Any League</label>
			</div>
			<div class="col-md-9" align="left">
				<s:select id="leagueId" headerKey="-1" headerValue="--Please Select--" name="leagueId" 
					list="%{leagueMap}" listKey="key" listValue="%{value.leagueDispName}"
					cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
			</div>
		</div>
	</div>
</s:form>
<div id="mapping"></div>
<script type="text/javascript">
commonApp.init($);
</script>