<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.inject.Context"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en-US">
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="author" content="ThemeStarz">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css"
			type="text/css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/assets/css/ionicons.min.css"
			type="text/css">
		<title>Sports Lottery | Update Jackpot Amount/Message</title>
	</head>
	<body class="page-account" id="page-top">

		<!-- Wrapper -->
		<div class="wrapper">
			<!-- Page Content -->
			<div id="page-content">

				<div class="container">
					<div class="col-md-1"></div>
					<div class="col-md-11">
						<div class="box box-info box-style">
							<div class="box-header box-header-style">
								<i class="fa fa-gear"></i>
								<h3 class="box-title">
									Update Jackpot Amount/Message
								</h3>
							</div>
							<div class="box-body">
								<div class="panel panel-default col-md-12 box-panel-style"
									id="gameDiv">
									<s:form cssClass="form-horizontal" method="post" theme="simple"
										id="f0">
										<div class="form-group">
											<div class="col-md-10 left-button-div-style"></div>
											<div class="col-md-12" align="center">
												<div class="alert alert-success alert-dismissable"
													style="overflow: auto; margin-top: 30px">
													<div align="center" ><b>SUCCESS !!</b></div>
													<div align="center">
														<b> You have Successfully Updated your Jackpot Amount/Message.
														</b>
														<hr>
													</div>
												</div>
											</div>
										</div>
									</s:form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>

