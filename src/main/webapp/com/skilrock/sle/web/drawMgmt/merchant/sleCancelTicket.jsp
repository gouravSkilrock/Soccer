<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.inject.Context"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en-US">
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="author" content="ThemeStarz">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
		<title>Sports Lottery | Ticket Cancellation</title>
	</head>
	<body class="page-account" id="page-top">
		<!-- Wrapper -->
		<div class="wrapper">
			<!-- Page Content -->
			<div id="page-content">
				<!-- Breadcrumb -->
				<div class="container" style="margin-top: 12px"></div>
				<!-- end Breadcrumb -->
				<div class="container">
					<div class="col-md-1"></div>
					<div class="col-md-10">
						<div class="box box-info box-style">
							<div class="box-header box-header-style">
								<i class="fa fa-gear"></i>
								<h3 class="box-title">
									Ticket Cancellation
								</h3>
							</div>
							<div class="box-body">
								<div class="panel panel-default col-md-12 box-panel-style"
									id="gameDiv">
									<s:form cssClass="form-horizontal" method="post" action="boCancelTicket" theme="simple" id="f0" onsubmit="return (validateTicketNumberWithReason()&&formSubmit(this.id,'resultDiv'));">
										<div class="form-group">
										<div class="form-group col-md-6 margin-div-style">
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Enter Ticket Number
												</label>
											</div>
											<div class="col-md-7" align="left">
												<s:textfield id="ticketNumber" name="ticketNumber" cssClass="form-control" maxlength="20" />
											</div>
											<div class="col-md-5" align="left"></div>
											<div class="col-md-7">
												<small id="ticketNumberError" class="col-md-12 small-tag-style-for-error"></small>
												<small id="ticketNumberSuccess" class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>

										<div class="form-group col-md-6 margin-div-style">										
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Reason for Cancellation
												</label>
											</div>
											<div class="col-md-7" align="left">
												<s:select name="reasonForCancel" id="reasonForCancel"
												label="Select Reason for Cancellation" cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"
												headerKey="-1" headerValue="-- Select Reason --"
													list="{'WRONG_TEAM_SELECTED','WRONG_AMOUNT_PLAYED','TICKET_NOT_PRINTED', 'OTHER'}" />
											</div>
											<div class="col-md-5" align="left"></div>
											<div class="col-md-7">
												<small id="reasonError" class="col-md-12 small-tag-style-for-error"></small>
												<small id="reasonSuccess" class="col-md-12 small-tag-style-for-success"></small>
											</div>
										</div>
										
										
											<div class="col-md-12">
												<hr class="panel-hr-style">
											</div>
											<div class="col-md-5 left-button-div-style"></div>
											<div class="col-md-7 right-button-div-style" align="left">
												<s:submit type="submit" cssClass="btn btn-primary" value="Search" />
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
		
		<!-- end Page Content -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/sleEventInsertion.js"></script>

		

		<!--[if gt IE 8]>
		<script type="text/javascript" src="assets/js/ie.js"></script>
		<![endif]-->
	</body>
</html>