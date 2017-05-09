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
		<title>Sports Lottery | Event Management</title>
		<script>var path="<%=request.getContextPath()%>"</script>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/sleGamePlayAssets/dist/css/jquery-confirm.min.css" type="text/css">
		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/commonApp.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/sleGamePlayAssets/dist/js/jquery-confirm.min.js"></script>
		<script>
			$(document).ready(function() {
				commonApp.init($);
			});
		</script>
	</head>
	<body class=" page-account" id="page-top">
		<div id="page-content">
			<div class="container margin-top-for-first-container"></div>
			<div class="container">
				<div class="col-md-12">
					<div class="box box-info box-style">
						<div class="box-header box-header-style">
							<i class="fa fa-gear"></i>
							<h3 class="box-title">
								Event Management
							</h3>
						</div>
						<div class="box-body">
							<div class="panel panel-default col-md-12 box-panel-style" id="gameDiv">
								<s:form cssClass="form-horizontal" method="post" id="eventMgmtMainForm"
									name="eventMgmtMainForm" theme="simple">
									<div class="col-lg-12 col-md-12 padding-15">
										<div class="col-md-2" align="right">
											<label class="control-label" for="inputSuccess">
												Select Game
											</label>
										</div>
										<div class="col-md-4" align="left">
											<s:select id="event_gameId" headerKey="-1"
												headerValue="--Please Select--" name="gameId"
												list="%{gameMap}" listKey="key"
												listValue="%{value.gameDispName}"
												cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12">
											</s:select>
										</div>
										<div class="col-md-2" align="right">
											<label class="control-label" for="inputSuccess">
												Select Game Type
											</label>
										</div>
										<div class="col-md-4" align="left">
											<s:select id="gameTypeId" headerKey="-1"
												headerValue="--Please Select--" name="gameTypeId"
												list="%{gameTypeMap}" listKey="key"
												listValue="%{value.gameTypeDispName}"
												cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12">
											</s:select>
										</div>
										<div class="col-md-2" align="right"></div>
										<div align="left" class="col-md-4">
											<div class="small-tag-style-for-error"  style="display:none;text-align:center" id="game_error">
												Please Select Game
											</div>
										</div>
										<div class="col-md-2" align="right"></div>
										<div align="left" class="col-md-4">
											<div class="small-tag-style-for-error" style="display:none;text-align:center" id="game_type_error">
												Please Select Game Type
											</div>
										</div>
									</div>
									<div class="col-lg-12"></div>
									<div class="col-lg-12 col-md-12">
										<div class="col-md-5"></div>
										<div class="col-md-7 right-button-div-style" align="right">
											<input type="button" value="Search" id="searchDrawBtn"
												class="btn btn-primary pull-left margin_right_two" />
										</div>
									</div>
								</s:form>
							</div>

						</div>
						<div id="drawInfo"></div>
						<div align="center" id="errorDiv" style="display: none;margin-left:2px" class="col-md-12 alert alert-danger">
								No Draw Available !!
						</div> 
					</div>
				</div>
			</div>
		</div>
	</body>
</html>