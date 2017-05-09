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
			
<div style="margin-top:60px; margin-left:350px; text-align:center" class="col-md-6 alert alert-success">
       <div class="col-md-12"><div><i class="fa fa-check-circle fa-2x"></i></div> <font size="4px"><b>Winning Claimed Successfully !!</b></font></div>
       <div class="col-md-12">
       		<div class="col-md-6" style="text-align:right;"> <font size="3px">Winning Amount:</font></div>
       		<div class="col-md-6" style="text-align:left;"><font size="3px"><b><s:property value="%{winAmt}"/></b></font></div>
       </div>
        <div class="col-md-12">
       		<div class="col-md-6" style="text-align:right;"><font size="3px">Tax Amount:</font></div>
       		<div class="col-md-6" style="text-align:left;"><font size="3px"><b><s:property value="%{taxOnPwt}"/></b></font></div>
       </div>
        <div class="col-md-12">
       		<div class="col-md-6" style="text-align:right;"><font size="3px">Amount to be paid to player:</font></div>
       		<div class="col-md-6" style="text-align:left;"><font size="3px"><b><s:property value="%{netWinAmount}"/> </b></font></div>
       </div>
</div>
</body>
</html>
