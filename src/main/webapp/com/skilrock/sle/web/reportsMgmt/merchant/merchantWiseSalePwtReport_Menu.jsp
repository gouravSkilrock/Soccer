<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>

<html lang="en-US">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="ThemeStarz">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css">
	<title>Sports Lottery | Merchant Wise Sale PWT Report</title>
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
						<i class="fa fa-gear"></i><h3 class="box-title main-heading">Merchant Wise Sale PWT Report</h3>
					</div>
					<div class="box-body" >
						<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>						
						<div class="col-md-3"></div>
						<div class="panel panel-default col-md-12 box-panel-style">
							<s:form theme="simple" cssClass="form-horizontal" method="post" action="merchantWiseSalePwtReportSearch" id="gameReportForm" onsubmit="return (validateFetchReportRequestForm()&&formSubmit(this.id,'resultDiv'));">
							<div class="form-group">
								<div class="form-group col-md-6 margin-div-style">
                                          <div class="col-md-5" align="right"><label class="control-label" for="inputSuccess"> Start Date</label></div>
                                          <div class="col-md-7" align="left" >
										<div class="input-group">													
											<input type="text" id="startDateTimePicker" name="startDate" class="form-control" readOnly/>
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
											<input type="text" id="endDateTimePicker" name="endDate" class="form-control" readOnly/>
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
								
								<div class="col-md-12"> <hr class="panel-hr-style"> </div>
								<div class="col-md-5 left-button-div-style"></div>
								<div class="col-md-7 right-button-div-style" align="left" ><button id="btn" type="submit" class="btn btn-primary">Search</button></div>
								</div>
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

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/table/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>

<script>
	$(document).ready(function() {
		$('#example').dataTable();
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
</script>

<!--[if gt IE 8]>
	<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>
