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
							<h3 class="box-title">SIMNET PRIZE DISTRIBUTION</h3>
						</div>
						<div class="box-body">
							<div class="panel panel-default col-md-12 box-panel-style">
	                      	    <centre>Error!! : Your Query For Simnet Prize Distribution Can Not Be Completed !!</centre>><br><br>			
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<!-- end Page Content -->
	</div>

	<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>