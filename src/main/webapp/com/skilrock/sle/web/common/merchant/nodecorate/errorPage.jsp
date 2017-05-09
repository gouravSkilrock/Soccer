<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  		<meta charset="UTF-8">
  		<title>Page Not Found | Something happened wrong here !!</title>
  		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/bootstrap/css/bootstrap.css" type="text/css">
  		
	</head>

	<body>
		<div class="col-md-1"></div>
		
		<div class="col-md-10" style="border: 1px solid rgb(211, 211, 211);border-radius:0.45em;box-shadow:0px 3px 10px #d3d3d3">
		
			<div class="col-md-2"></div>
			<div class="col-md-8 alert alert-danger" style="margin-top:12px; margin-left:0px; text-align:center">Page Not Found !! Something happened wrong here <font color="red">(<%=request.getAttribute("SLE_EXCEPTION")%>)</font>... Try Again !!</div>
			<div class="col-md-2"></div>
			
			<div class="col-md-3"></div>
			<div class="col-md-6"><img src="<%=request.getContextPath()%>/images/404.jpg" alt="Page Not Found (404)." style="position: relative; left: 50%; top: 50%; margin-top: -20px;margin-left:-290px"></div>  		
			<div class="col-md-3"></div>
		
		</div>
		
		<div class="col-md-1"></div>
    </body>
    
</html>