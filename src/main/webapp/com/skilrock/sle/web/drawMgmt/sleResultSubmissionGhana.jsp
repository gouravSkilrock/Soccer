<!doctype html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>NLA Soccer Cash</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.css" rel="stylesheet" type="text/css" />
		<link href="<%=request.getContextPath()%>/sleGamePlayAssets/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Roboto:400,100,300,300italic,100italic,400italic,500,500italic,700,700italic,900,900italic" rel="stylesheet" type="text/css">
		<link href="<%=request.getContextPath()%>/css/result.submission.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/pace.min.js"></script>
		<script src="<%=request.getContextPath()%>/js/custom.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/commonApp.js"></script>
		<script type="text/javascript">
			var projectName = "<%=request.getContextPath()%>";
		</script>
	</head>
	<body class="bg1" id="body">
		<div class="preloader">
			<div class="loadLogo">
				<img src="<%=request.getContextPath()%>/images/backOffice/merchant/resultSubmission/soccerCashLogo.png" alt="soccer cash">
			</div>
		</div>
     
		<!--	Page Start -->
		 <button id="backBtn" type="button" class="btn btn-primary glyphicon glyphicon-arrow-left back_btn" title="Go Back"></button>
		<div class="container container-fluid pageWrap smallContainer">
		  <div class="pageTitleWrap" id="oldDiv">
			  <div class="pageLogo">
					<img src="<%=request.getContextPath()%>/images/backOffice/merchant/resultSubmission/soccerCashLogo.png" alt="soccer cash">
				</div>
				<div class="mainTitle">
					Sports Game Result
					<span>submission</span>
				</div>
			</div>
			<div class="contentOuterWrap">
				<div class="contentInnerWrap soccerPlay" id="mainDiv">
					<s:form cssClass="form-horizontal" theme="simple" method="post"
						action="drawEventSearch" id="form1"
						onsubmit="return (validateGameDataForm()&&formSubmit(this.id,'resultDiv'));"
						target="_blank">
						<div class="form-row">
							<div class="form-group">
							
									<div class="col-md-4" align="right">
									<label class="control-label" for="inputSuccess">
											Select Game 
									</label>
										 
	
									</div>
									<div class="col-md-7" align="left">
										<s:select id="gameId" headerKey="-1"
											headerValue="--Please Select--" name="gameId"
											list="%{gameMap}" listKey="key"
											listValue="%{value.gameDispName}"
											cssClass=" form-control" 
											cssStyle="margin-left: 50px;" >
										</s:select>
										<s:hidden id="gameType" name="gameType"
											value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(gameMap)}" />
									</div>
							
						
								  <div class="col-md-5"></div>
								   <div class="col-md-6">
									<small class="col-md-9 small-tag-style-for-error" style="margin-left:52px;width:296px"></small>
									<!--<small class="col-md-12 small-tag-style-for-success"></small>-->
								 </div>
						
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-5" align="right">
								<label class="control-label" for="inputSuccess" >
									Select Game Type
								</label>
							</div>   
							<div class="col-md-7" align="left">
								<s:select id="gameTypeId" headerKey="-1" 
									headerValue="--Please Select--" name="gameTypeId" list="{}"
									cssClass=" form-control"
									cssStyle="width: 300px;">
								</s:select>
							</div>
							<div class="col-md-5"></div>
							<div class="col-md-6">
								<small class="col-md-10 small-tag-style-for-error" style="width:300px"></small>
								<!--<small class="col-md-6 small-tag-style-for-success"></small>-->
							</div>
						</div>
						<div class="col-md-5 left-button-div-style"></div>
						<div class="col-md-12 form-group text-center" align="center">
							<button id="submitGameData" type="submit" class="btn">
								Search
							</button>
						</div>
					</s:form>
				</div>
			</div>
		</div>
		<div id="resultDiv"></div>
		<!--	Page End -->


		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/ajaxCall.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"></script>
		<script type="text/javascript">
         $(document).ready(function() {
                $("#gameId").val('');
                $('#backBtn').hide();
				var gameTypeData = $('#gameType').val();
				var gameTypeMap = jQuery.parseJSON(gameTypeData);
				
				$('#gameId').change(function() {					
					if($(this).val()!=-1) {
					    $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","none");
                        $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
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
					if($(this).val()!=-1){
                    	$('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
                 	}			
				});
				$('#backBtn').click(function(){
				   window.location.reload();
				
				}); 
				

});
</script>
	</body>
</html>