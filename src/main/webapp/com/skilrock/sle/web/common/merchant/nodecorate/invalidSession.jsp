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
		<div class="col-md-12"><div class="col-md-3"></div><div class="col-md-6 alert alert-danger" style="margin-top:12px; margin-left:0px; text-align:center"><%=request.getAttribute("SLE_EXCEPTION")%></div>  		
    </body>
</html>