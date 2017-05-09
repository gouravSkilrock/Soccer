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

	
	<title>Sports Lottery | Success</title>

</head>

<body class="page-account" id="page-top">

       <div class="col-md-3"></div> 
       <div style="margin-top:12px; margin-left:0px; text-align:center" class="col-md-6 alert alert-success">
       	<div><i class="fa fa-check-circle fa-2x"></i></div><font size="4px"><b>Event Inserted Successfully !!</b></font>
       	
       <!-- 	<s:url var="url" action="fetchTeams.action">
            <s:param name="gameIds"><s:property value="%{gameId}"/></s:param>
            <s:param name="leagueId"><s:property value="%{leagueId}"/></s:param>
        </s:url> -->
        
       <div><s:a id="addAnchor" href="#"><i class="fa fa-plus-circle" style="margin-left:-10px; margin-top:15px"></i> Add Another Event</s:a></div></div>	
</body>
</html>
