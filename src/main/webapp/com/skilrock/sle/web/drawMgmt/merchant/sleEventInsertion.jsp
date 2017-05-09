<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.inject.Context"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en-US">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="ThemeStarz">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
	
	<title>Sports Lottery | Event Insertion</title>

</head>

<body class="page-account" id="page-top">
<!-- Wrapper -->
<div class="wrapper">

    <!-- Page Content -->
    <div id="page-content">
        <!-- Breadcrumb -->
        <div class="container" style="margin-top:12px">
        </div>
   		<!-- end Breadcrumb -->
		
		
	<div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Events Insertion</h3>
					</div>
					<div class="box-body" >
							<div class="panel panel-default col-md-12 box-panel-style" id="gameDiv">
									<s:form cssClass="form-horizontal" method="post" action="fetchTeams" theme="simple" id ="f0">
										<div class="form-group">
                                            <div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> Select Any Game</label></div>
                                            <div class="col-md-7" align="left" >
												<s:select id="gameIds" headerKey="-1" headerValue="--Please Select--"
													name="gameIds" list="%{gameMap}" listKey="key" listValue="%{value.gameDispName}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
											</div>
											<div class="col-md-5"></div><div class="col-md-7"><small class="col-md-4 small-tag-style-for-error"></small></div>
											<div class="col-md-12" style="height:10px"></div>
											<div id="leagueDiv" ><div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> Select Any League</label></div>
                                            <div class="col-md-7" align="left" >
												<s:select id="leagueId" headerKey="-1" headerValue="--Please Select--"
													name="leagueId" list="%{leagueMap}" listKey="key" listValue="%{value.leagueDispName}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
											</div><div class="col-md-5"></div><div class="col-md-7"><small class="col-md-4 small-tag-style-for-error"></small></div></div>
                                        </div>									
									</s:form>	
							</div>							
							<div id="resultDiv"></div>
						</div>
					</div>
				</div>
		</div>
		
		
	</div>
    <!-- end Page Content -->
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sleEventInsertion.js"></script>
<script src="<%=request.getContextPath()%>/js/commonApp.js"></script>
<script>

$(document).ready(function(){
	$('#leagueDiv').hide();
	$('#resultDiv').empty();
	$('#gameIds').change(function(){
		if($(this).val()!=-1){
			$('#leagueDiv').show();
		}else{
			$('#resultDiv').empty();
			$('#leagueId').prop('selectedIndex', 0);
			$('#leagueDiv').val(-1);
			$('#leagueDiv').hide();
		}
	});
	
	$('#leagueId').change(function(){
		if($(this).val()!=-1){
			var selectedGameId = $('#gameIds').val();
			$('#gameId').val(selectedGameId);
			formSubmit('f0','resultDiv');
		}else{
			$('#resultDiv').empty();
		}
	});
		
	$(document).on('click','#addAnchor',function(e){
		e.preventDefault();
		formSubmit('f0','resultDiv');
	});
});

function makeTeamDropDown(teamData, teamdivId){
	$('#'+teamdivId).empty().append($('<option></option>').val("-1").html("--Please Select--"));
	if (teamData!=undefined && teamData.length>0) {
		$.each(teamData, function(key, value) {
			$('#'+teamdivId).append($('<option></option>').val(value.teamCode).html(value.teamName));
		});
	}else{
			$('#'+teamdivId).empty().append($('<option></option>').val("-1").html("--Please Select--"));
	}
}
</script>

<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>
