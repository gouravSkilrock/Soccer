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
    	<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/formValidation.css" type="text/css"> -->	
    
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css">

	
	<title>Sports Lottery | Result Approval</title>
</head>

<body class=" page-account" id="page-top" >
<!-- Wrapper -->
<div class="wrapper">

    <!-- Page Content -->
    <div id="page-content">
        <!-- Breadcrumb -->
        	<div class="container" style="margin-top:8px"></div>
        <!-- end Breadcrumb -->
		
		
	<div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Result Approval</h3>
					</div>
					<div class="box-body" >
						<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>						
						<div class="col-md-3"></div>
							<div class="panel panel-default col-md-12 box-panel-style">
									<s:form theme="simple" cssClass="form-horizontal" method="post" action="getUnmatchedDraws" id="resultApprovalForm" onsubmit="return (validateResultApprovalForm()&&formSubmit(this.id,'resultDiv'));">
										<div class="form-group col-md-6 margin-div-style">
                                            <div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> Select Game</label></div>
                                            <div class="col-md-7" align="left" >
												<s:select id="gameId" headerKey="-1" headerValue="--Please Select--"
													name="gameId" list="%{gameMap}" listKey="key" listValue="%{value.gameDispName}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
												<s:hidden id="gameType" name="gameType" value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(gameMap)}" />
											</div>
											<div class="col-md-5"></div><div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
                                        </div>
										<div class="form-group col-md-6 margin-div-style" >
                                            <div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> Select Game Type</label></div>
                                            <div class="col-md-7" align="left" >
												<s:select id="gameTypeId" headerKey="-1" headerValue="--Please Select--"
												name="gameTypeId" list="{}" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
											</div>
											<div class="col-md-5"></div><div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
                                        </div>
                                        
                                        
										<div class="form-group col-md-6 margin-div-style">
                                            <div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> Start Date</label></div>
                                            <div class="col-md-7" align="left" >
												<div class="input-group">													
													<input type="text" id="startDateTimePicker" name="startDate" class="form-control"/>
													<div class="input-group-addon" id="startDateTimePickerDiv">
														<i class="fa fa-calendar"></i>
													</div>
												</div>												
											</div>
											<div class="col-md-5"></div><div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>										
                                        </div>
										<div class="form-group col-md-6 margin-div-style" >
                                            <div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> End Date</label></div>
                                            <div class="col-md-7" align="left" >
												<div class="input-group">													
													<input type="text" id="endDateTimePicker" name="endDate" class="form-control"/>
													<div class="input-group-addon" id="endDateTimePickerDiv">
														<i class="fa fa-calendar"></i>
													</div>
												</div>												
											</div>
											<div class="col-md-5"></div><div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
                                      
										
										<div class="form-group col-md-12" >
                                            <div class="col-md-2" align="right" style="margin-left: 38px;"><label class="control-label" for="inputSuccess"> Draw Id</label></div>
                                            <div class="col-md-9" align="left" >
												<div class="input-group" class="col-md-12">													
													<input type="text" id="drawId" name="drawId" class="form-control" maxlength="9" style="width:261px;"/>&nbsp;
													 <span id="errmsg" class="small-tag-style-for-error"></span>													
												</div>												
											</div>
											<div class="col-md-2"></div><div class="col-md-10">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
										
										<hr class="panel-hr-style">
										<div class="col-md-5 left-button-div-style"></div>
										<div class="col-md-7 right-button-div-style" align="left" ><button type="submit" class="btn btn-primary">Search</button></div>
									</s:form>	
							</div>
						</div>
					</div>
				</div>
		</div>
		
		<div id="resultDiv"></div>

		
	
	</div>
    <!-- end Page Content -->

</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jtable/dataTables.jqueryui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jtable/jquery.dataTables.js"></script>



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
				
				$('#gameTypeId').change(function() {		
					//to do	
				});
				//$('#example').dataTable();			
		});

	$('#startDateTimePicker').datetimepicker({
		mask:'9999/19/39',
		format:'Y/m/d',
		formatDate:'Y/m/d',
		timepicker:false
	});
	$('#endDateTimePicker').datetimepicker({
		mask:'9999/19/39',
		format:'Y/m/d',
		formatDate:'Y/m/d',
		timepicker:false
	});
	
	$('#startDateTimePicker').click(function(){
		$('#startDateTimePicker').blur();
	});

	$('#endDateTimePicker').click(function(){
		$('#endDateTimePicker').blur();
	});

	$('#startDateTimePickerDiv').click(function(){
		$('#startDateTimePicker').datetimepicker('show');
		$('#startDateTimePicker').blur();
	});

	$('#endDateTimePickerDiv').click(function(){
		$('#endDateTimePicker').datetimepicker('show');
		$('#endDateTimePicker').blur();
	});
	
	$("#drawId").keypress(function (e) {
     //if the letter is not digit then display error and don't type anything
     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        //display error message
        $("#errmsg").html("Digits Only").show().fadeOut("slow");
               return false;
    }
   });


 	

</script>


<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>
