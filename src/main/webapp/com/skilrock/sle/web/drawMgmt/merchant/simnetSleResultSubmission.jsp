<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="ThemeStarz">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/css/ionicons.min.css"
	type="text/css">

<title>Sports Lottery | Result Submission</title>
</head>
<body class=" page-account" id="page-top" onload=display_ct();>
	<!-- Wrapper -->
	<div class="wrapper">

		<!-- Page Content -->
		<div id="page-content">
			<!-- Breadcrumb -->
			<div class="container" style="margin-top: 8px"></div>
			<!-- end Breadcrumb -->


			<div class="container">
				<div class="col-md-1"></div>
				<div class="col-md-10">
					<div class="box box-info box-style">
						<div class="box-header box-header-style">
							<i class="fa fa-gear"></i>
							<h3 class="box-title">SIMNET RESULT DISTRIBUTION</h3>
						</div>
						<div class="box-body">

							<div class="panel panel-default col-md-12 box-panel-style">
								<s:form cssClass="form-horizontal" theme="simple" method="post"
									action="SimnetPrizeDistribution" id="form1"
									onsubmit="return validateSimnetGameDataForm();">
									<div class="form-group">
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
												Select Game Name</label>
										</div>
										<div class="col-md-6" align="left">
											<s:select id="gameId" headerKey="-1"
												headerValue="--Please Select--" name="gameId"
												list="%{gameMap}" listKey="key"
												listValue="%{value.gameDispName}"
												cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
											<s:hidden id="gameType" name="gameType"
												value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(gameMap)}" />
										</div>
										<div class="col-md-4"></div>
										<div class="col-md-6">
											<small class="col-md-12 small-tag-style-for-error"></small>
											<!--<small class="col-md-12 small-tag-style-for-success"></small>-->
										</div>
									</div>
									<div class="form-group">
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
												Select Game Type</label>
										</div>
										<div class="col-md-6" align="left">
											<s:select id="gameTypeId" headerKey="-1"
												headerValue="--Please Select--" name="gameTypeId" list="{}"
												cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
										</div>
										<div class="col-md-4"></div>
										<div class="col-md-6">
											<small class="col-md-12 small-tag-style-for-error"></small>
											<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
										</div>
									</div>
									<div class="form-group">
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
												Select Draw</label>
										</div>
										<div class="col-md-6" align="left">
											<s:select id="simnetDrawsId" headerKey="-1"
												headerValue="--Please Select--" name="simnetDrawsId"
												list="{}"
												cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
										</div>
										<div class="col-md-4"></div>
										<div class="col-md-6">
											<small class="col-md-12 small-tag-style-for-error"></small>
											<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
										</div>
									</div>
									
									<div class="form-group">
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
												Total Sale of Sports lottery(on SIMNET)</label>
										</div>
										<div class="col-md-6" align="left">
										    <s:textfield id="saleOnSimnet" name="saleOnSimnet" cssClass="form-control" maxlength="20" />
										</div>
										<div class="col-md-4"></div>
										<div class="col-md-6">
											<small class="col-md-12 small-tag-style-for-error"></small>
											<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
										</div>
									</div>	
									<div class="form-group">
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
					                            Number of winners for 12 match</label>
										</div>
										<div class="col-md-6" align="left">
										    <s:textfield id="noOfWinnersfor12" name="noOfWinnersfor12" cssClass="form-control" maxlength="20" />
										</div>
							        </div>
							        <div class="form-group">
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
					                            Number of winners for 11 match</label>
										</div>
										<div class="col-md-6" align="left">
										    <s:textfield id="noOfWinnersfor11" name="noOfWinnersfor11" cssClass="form-control" maxlength="20" />
										</div>
									</div>	
									<div class="form-group">	
										<div class="col-md-4" align="right">
											<label class="control-label" for="inputSuccess">
					                            Number of winners for 10 match</label>
										</div>
										<div class="col-md-6" align="left">
										    <s:textfield id="noOfWinnersfor10" name="noOfWinnersfor10" cssClass="form-control" maxlength="20" />
										</div>
									</div>
									<div class="form-group">	
										<div class="col-md-4"></div>
										<div class="col-md-6">
											<small class="col-md-12 small-tag-style-for-error" id="errorforwinners"></small>
											<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
										</div>
									</div>


									<hr class="panel-hr-style">
									<div class="col-md-4 left-button-div-style"></div>
									<div class="col-md-6 right-button-div-style" align="center">
										<button id="submitGameData" type="submit"
											class="btn btn-primary">Prize Distribution</button>
									</div>
								</s:form>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<!-- end Page Content -->
	</div>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/assets/js/app.js"></script>
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
						$('#simnetDrawsId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
					}
				})
				
});

$('#gameTypeId').change(function(){	
	var gameId = $("#gameId").val();
	var gameTypeId = $("#gameTypeId").val();
	$.ajax({
		type : "POST",
		url: projectName+"/com/skilrock/sle/web/merchantUser/drawMgmt/Action/SimnetResultDraws.action",
		data : {
			gameId: gameId,
			gameTypeId: gameTypeId
		},
		success : function(data) {
			if($('#gameTypeId').val()!=-1){
			var drawsList=jQuery.parseJSON(data);
			console.log(drawsList);	
			if (drawsList!=undefined && drawsList.length>0) {
				$('#simnetDrawsId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
				$.each(drawsList, function(key, value) {
					 $('#simnetDrawsId').append($('<option></option>').val(value.drawId).html((value.drawName).concat((value.drawDateTime))));
					});
				} else {
					$('#simnetDrawsId').empty().append($('<option></option>').val("-2").html("--No Available Draws--"));
				}
			}
			else {
				$('#simnetDrawsId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
			}
		},
		error : function(e) {
			location.reload(); 
		},
		done : function(e) {
		}
	});
});


</script>
	<script type="text/javascript">
			var projectName = "<%=request.getContextPath()%>";
	</script>

	<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>