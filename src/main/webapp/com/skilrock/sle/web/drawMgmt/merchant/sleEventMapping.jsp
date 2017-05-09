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
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="ThemeStarz">
	
	<title>Sports Lottery | Events Mapping</title>

</head>

<body class=" page-account" id="page-top">
<!-- Wrapper -->
<div class="wrapper">

    <!-- Page Content -->
    <div id="page-content">
        <!-- Breadcrumb -->
        	<div class="container" style="margin-top:8px"></div>
        <!-- end Breadcrumb -->		
		
	<div class="container">
		<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Draw Events Mapping</h3>
					</div>
					<div class="box-body" >
							<div class="panel panel-default col-md-12 box-panel-style">
									<form class="form-horizontal" method="post" action="#" id="form1">
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
										<hr class="panel-hr-style">
										<div class="col-md-4 left-button-div-style"></div>
										<div class="col-md-6 right-button-div-style" align="left" >
											<button id="submitGameData"  type="button" class="btn btn-primary">Search</button></div>
									</form>	
							</div>
							<div class="col-md-12"></div>
							
							<div class="panel panel-default col-md-12 box-panel-style" align="center" style="display:none" id="drawDiv">
									<form class="form-horizontal" method="post" action="#" id="form2">
										<div class="form-group">
                                            <div class="col-md-4" align="right"><label class="control-label" for="inputSuccess"> Current Draw List</label></div>
                                            <div class="col-md-6" align="left" >
												<s:select id="drawId" headerKey="-1" headerValue="--Please Select--"
													name="drawId" list="{}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
											</div>
											<div class="col-md-4"></div><div class="col-md-6">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
											</div>
                                        </div>
										<hr class="panel-hr-style">
										<div class="col-md-4 left-button-div-style"></div>
										<div class="col-md-6 right-button-div-style" align="left" >
											<button id="submitEventData" type="button" class="btn btn-primary">Search</button>
										</div>
									</form>
							</div>
							
							<div align="center" id="errorDiv" style="display: none;margin-left:2px" class="col-md-12 alert alert-danger">
								No Current Active Draw Available !!
							</div> 
						</div>
					</div>
				</div>
		</div>
	
	<div class="container" id = "eventDataDiv" style="display:none">
		<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Map Events</h3>
					</div>
					<div class="box-body" >
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
							<div class="panel panel-default col-md-12 box-panel-style">
								<s:form  theme="simple" cssClass="form-horizontal" method="post" action="boEventMappingEventSubmit.action" onsubmit="return func();" id="form3">
								<s:token name="token"/>
									<s:hidden name="gameId" value="" />
									<s:hidden name="gameTypeId" value="" />
									<s:hidden name="drawId" value="" />
									<s:hidden name="drawName" value="" />
									<s:hidden name="noOfEvents" id="noOfEvents" value="" />
									<s:hidden name="eventSelected" id="eventSelected" value="" />	
									<table class="table table-bordered table-striped panel-table-border-style" style="text-align:center" id="tableData">
                                       
                                       
                                    </table>
									<hr class="panel-hr-style">
									<div class="col-md-12 right-button-div-style" align="center" ><button type="submit" class="btn btn-primary">Submit</button></div>
								</s:form>
							</div>
							
						</div>
					</div>
				</div>
		</div>
		
			<div class="col-md-2"></div>
			<div class="col-md-8 alert alert-danger" style="display:none;text-align:center" id="eventMsgDiv">Events are not scheduled for this draw (Please insert the events then map them) !! Thanks !!</div>
	</div>
    <!-- end Page Content -->
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>

<script type="text/javascript">
$(document).ready(function() {
				var gameTypeData = $('#gameType').val();
				var gameTypeMap = jQuery.parseJSON(gameTypeData);
				
				$('#gameId').change(function() {
					$("#drawId").empty();
					$("#drawDiv").css("display","none");					
					$("#errorDiv").css("display","none");
					$("#tableData").empty();
					$("#eventDataDiv").css("display","none");
					$("#eventMsgDiv").css("display","none");
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
					}
				});
				
				$('#gameTypeId').change(function() {
					$("#drawId").empty();
					$("#drawDiv").css("display","none");					
					$("#errorDiv").css("display","none");
					$("#tableData").empty();
					$("#eventDataDiv").css("display","none");
					$("#eventMsgDiv").css("display","none");			
				});
				
				$('#drawId').change(function() {
					$('#jsErrorDiv').html('');
					$('#jsErrorDiv').css("display","none");
					$("#tableData").empty();
					$("#eventDataDiv").css("display","none");
					$("#eventMsgDiv").css("display","none");			
				});
				
				$('#submitGameData').click(function(){
					var isValid = validateGameDataForm();
					if(isValid){
						var requestParam='<%=request.getContextPath()%>/com/skilrock/sle/web/merchantUser/drawMgmt/Action/boEventMappingCurrentDrawSearch.action?gameId='+$("#gameId").val()+'&gameTypeId='+$("#gameTypeId").val();
						var drawData = _ajaxCallJson(requestParam,'','');
						if (drawData!=undefined && drawData.length>0) {
								$('#drawId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
								$.each(drawData, function(key, value) {
									 $('#drawId').append($('<option></option>').val(value.drawId).html(value.drawName+'  '+value.drawFreezeTime));
								});
								$("#drawDiv").css("display","block");
						} else {
								$("#drawDiv").css("display","none");
								$("#errorDiv").css("display","block");
						}
					}
				});
				
				$('#submitEventData').click(function(){
					var isValid = validateEventDataForm();
					if(isValid){
						var requestParam='<%=request.getContextPath()%>/com/skilrock/sle/web/merchantUser/drawMgmt/Action/boEventMappingEventSearch.action?gameId='+$("#gameId").val()+'&gameTypeId='+$("#gameTypeId").val()+'&drawId='+$("#drawId").val();
						var eventData = _ajaxCallJson(requestParam,'','');
						if (eventData!=undefined && eventData.length>0) {
								$('#tableData').empty();								
								$('#tableData').append('<tr><th colspan="4" class="table-header-style">Map Events with Selected Current Draw</th></tr>'+
													   '<tr><th class="table-header-style">Event Name</th><th class="table-header-style">Event Start Time</th>'+
													   '<th class="table-header-style">Event End Time</th><th class="table-header-style">Event Selected</th></tr>');
								$.each(eventData, function(key, value) {
									 $("#noOfEvents").val(value.noOfEvents);
									 $('#tableData').append('<tr>'+'<td class="table-event-style">'+value.eventDisplay+'</td><td>'+value.startTime+'</td><td>'+value.endTime+'</td><td><input type="checkbox" name="eventList" class="" value="'+value.eventId+'"/></td></tr>');
								});
								$("#eventDataDiv").css("display","block");
								$("#eventMsgDiv").css("display","none");
						} else {
								$("#eventMsgDiv").css("display","block");
						}
					}
				});
});
			
			function func() {
				$("#form3_gameId").val($("#gameId").val());
				$("#form3_gameTypeId").val($("#gameTypeId").val());
				$("#form3_drawId").val($("#drawId").val());
				$("#form3_drawName").val($("#drawId option:selected").text());
				var count = 0;
				var eventSelected = "";
				$('input:checkbox[name=eventList]:checked').each(function() {
					count++;
					eventSelected += ($(this).val())+",";
				});
				eventSelected = eventSelected.substring(0, eventSelected.length-1);
				if(count != parseInt($('#noOfEvents').val())) {
					$('#jsErrorDiv').html('<li>Please Select '+$('#noOfEvents').val()+' Events.</li>');
					$('#jsErrorDiv').css("display","block");
					//$("html, body").animate({ scrollTop: $(document).height() }, "slow");
					var scrollPos = $('#eventDataDiv').offset().top;
					$(window).scrollTop(scrollPos);
					return false;
				}
				$('#eventSelected').val(eventSelected);
				return true;
			}
</script>

<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>

