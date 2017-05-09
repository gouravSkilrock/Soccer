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
	
	<script type="text/javascript">
		
			$(document).ready(function(){
					
		      	var scrollPos = $('.menu-div-style').offset().top;
				$(window).scrollTop(scrollPos);

			});
	</script>

</head>

<body class="page-account" id="page-top">
			
<div style="margin-top:60px; margin-left:250px; text-align:center" class="col-md-6 alert alert-success">
       <div><i class="fa fa-check-circle fa-2x"></i></div> <font size="4px"><b>Event Mapped Successfully !!</b></font>
		<hr style="margin-top:8px">
		
		<div class="col-md-12"><font size="3px">Event Details</font>   <i class="fa fa-arrow-circle-down"></i>
			<div class="col-md-12">
					<table id="tableData" style="text-align:center; box-shadow:3px 5px 10px #006600" class="table table-striped panel-table-border-style">
						<tbody>
							<tr>
								<th class="table-header-style" colspan="1" style="background-color:#FFFFFF !important; text-align:left !important; color:#000000; border-left:0px !important;">Mapped Events</th>
								<th style="background-color:#FFFFFF !important; text-align:right !important; color:#000000; border-left:0px !important;" class="table-header-style" colspan="2">Draw Selected: <s:property value="%{drawName}"/></th>
							</tr>
							<tr>
								<th class="table-header-style">Event Name</th>
								<th class="table-header-style">Event Start Time</th>
								<th class="table-header-style">Event End Time</th>
							</tr>
							<s:iterator value="eventMasterList">
							<tr>
								<td class="table-event-style"><s:property value="eventDisplay"/></td>
								<td><s:property value="startTime"/></td>
								<td><s:property value="endTime"/></td>
							</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
		</div>
</div>
</body>
</html>
