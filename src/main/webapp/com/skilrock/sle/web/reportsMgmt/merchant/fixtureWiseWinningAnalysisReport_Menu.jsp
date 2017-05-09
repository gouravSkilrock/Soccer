<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en-US">
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="author" content="ThemeStarz">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css">
		<title>Sports Lottery | Fixture Wise Winning Analysis Report</title>
	</head>
	<body class=" page-account" id="page-top">
		<!-- Wrapper -->
		<div class="wrapper">
			<!-- Page Content -->
			<div id="page-content">
				<!-- Breadcrumb -->
				<div class="container" style="margin-top: 8px"></div>
				<!-- end Breadcrumb -->
				<div class="container">
					<div class="col-md-12">
						<div class="box box-info box-style">
							<div class="box-header box-header-style">
								<i class="fa fa-gear"></i>
								<h3 class="box-title main-heading">Fixture Wise Winning Analysis Report</h3>
							</div>
							<div class="box-body">
								<div style="margin-left: 0px; text-align: left; display: none; margin-bottom: 5px; margin-top: -4px"
									id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
								<div class="col-md-3"></div>
								<div class="panel panel-default col-md-12 box-panel-style">
									<s:form theme="simple" cssClass="form-horizontal" method="post"
										action="fixtureWiseWinningAnalysisReportSearch" id="winReportForm"
										onsubmit="return (validateFixtureWiseWinningAnalysisReportRequestForm()&&formSubmit(this.id,'resultDiv'));">
										<div class="form-group">
										<div class="form-group col-md-6 margin-div-style">
											<div class="col-md-5" align="right">
												<label class="control-label" for="inputSuccess">
													Select Game
												</label>
											</div>
											<div class="col-md-7" align="left">
												<s:select id="gameId" headerKey="-1"
													headerValue="--Please Select--" name="gameId"
													list="%{gameMap}" listKey="key"
													listValue="%{value.gameDispName}"
													cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
												<s:hidden id="gameType" name="gameType"
													value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(gameMap)}" />
											</div>
											<div class="col-md-5"></div>
											<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
										<div class="form-group col-md-6 margin-div-style">
											<div class="col-md-5" align="right">
												<label class="control-label" for="inputSuccess">
													Select Game Type
												</label>
											</div>
											<div class="col-md-7" align="left">
												<s:select id="gameTypeId" headerKey="-1"
													headerValue="--Please Select--" name="gameTypeId" list="{}"
													cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
											</div>
											<div class="col-md-5"></div>
											<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
										<div class="form-group col-md-6 margin-div-style">
											<div class="col-md-5" align="right">
												<label class="control-label" for="inputSuccess">
													Select Date
												</label>
											</div>
											<div class="col-md-7" align="left">
												<div class="input-group">
													<input type="text" id="selectedDateTimePicker" readonly="readonly"
														name="selectedDate" class="form-control" />
													<div class="input-group-addon" id="selectedDateTimePickerDiv">
														<i class="fa fa-calendar"></i>
													</div>
												</div>
											</div>
											<div class="col-md-5"></div>
											<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
										<div class="form-group col-md-6 margin-div-style">
											<div class="col-md-5" align="right">
												<label class="control-label" for="inputSuccess">
													Select Draw
												</label>
											</div>
											<div class="col-md-7" align="left">
												<s:select id="drawId" headerKey="-1"
													headerValue="--Please Select--" name="drawId" list="{}"
													cssClass="btn dropdown-toggle drawId btn-default option col-md-12"></s:select>
											</div>
											<div class="col-md-5"></div>
											<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
										<div class="col-md-12"><hr class="panel-hr-style"></div>
										<div class="col-md-5 left-button-div-style"></div>
										<div class="col-md-7 right-button-div-style" align="left">
											<button type="submit" class="btn btn-primary">
												Search
											</button>
										</div>
										</div>
									</s:form>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="resultDiv"></div>
			</div>
		</div>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/table/jquery.dataTables.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
		<script>
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
					}
				});
				$('#example').dataTable();	
				resetDateTimePicker();		
			});
			
			$('#gameTypeId').change(function() {		
					$('#drawId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
					$('#drawId').parent().parent().find(".small-tag-style-for-error").css("display","none");
					$('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
					$('#selectedDateTimePicker').removeClass("box-error-style");
					$('#drawId').removeClass("box-error-style");
				    $('#selectedDateTimePicker').val('');
		            resetDateTimePicker();
			});

			function resetDateTimePicker() {
		  		 $('#selectedDateTimePicker').datetimepicker( {
					mask : '9999/19/39',
					format : 'Y/m/d',
					formatDate : 'Y/m/d',
					timepicker : false
				  });
			}

			$('#selectedDateTimePicker').click(function(){
				$('#selectedDateTimePicker').blur();
			});

			$('#selectedDateTimePickerDiv').click(function(){
				$('#selectedDateTimePicker').datetimepicker('show');
				$('#selectedDateTimePicker').blur();
			});

			$('#selectedDateTimePicker').change(function(){
				$('#jsErrorDiv').css("display","none");
				var requestParam = '<%=request.getContextPath()%>/com/skilrock/sle/web/merchantUser/reportsMgmt/Action/fixtureWiseWinningAnalysisReportDrawSearch.action?gameId='+$("#gameId").val()+'&gameTypeId='+$("#gameTypeId").val()+'&selectedDate='+$("#selectedDateTimePicker").val();
				var drawData = _ajaxCallJson(requestParam,'','');
				if (drawData.toString().split("#")[0] == "10030"){
						$('#jsErrorDiv').html(drawData.split("#")[1]);
						$('#jsErrorDiv').css("display","block");
				}else{
					$('#drawId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
					$.each(drawData, function(key, value) {
						$('#drawId').append($('<option></option>').val(key).html(value));
					});
				}
				
				
			});
		</script>
		<!--[if gt IE 8]>
		<script type="text/javascript" src="assets/js/ie.js"></script>
		<![endif]-->
	</body>
</html>