<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en-US">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="ThemeStarz">

	
	<title>Sports Lottery | Error</title>

</head>

<body class="page-account" id="page-top">

       <div class="col-md-3"></div> 
       <div style="margin-top:50px; margin-left:0px; text-align:center" class="col-md-6 alert alert-danger">
       	This Request has been already Processed.
       </div>
</body>
</html>