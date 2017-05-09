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
		<meta charset="UTF-8">
		<title>NLA Soccer Cash</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.css"
			rel="stylesheet" type="text/css" />
		<link
			href="https://fonts.googleapis.com/css?family=Roboto:400,100,300,300italic,100italic,400italic,500,500italic,700,700italic,900,900italic"
			rel="stylesheet" type="text/css">
		<link href="<%=request.getContextPath()%>/css/result.submission.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/pace.min.js"></script>
		 <script src="<%=request.getContextPath()%>/js/custom.js"></script>
	</head>
	<body class="bg2">
		<div class="container container-fluid pageWrap smallContainer">
			<div class="pageTitleWrap">
				<div class="pageLogo">
					<img
						src="<%=request.getContextPath()%>/images/backOffice/merchant/resultSubmission/soccerCashLogo.png"
						alt="soccer cash">
				</div>
				<div class="mainTitle">
					Sports Game Result
					<span>submission</span>
				</div>
			</div>
			<div class="contentOuterWrap">
				<div class="contentInnerWrap soccerPlay">
					<div class="text-center" style="margin-bottom: 80px;">
						<div style="margin: 20px auto;">
							<img
								src="<%=request.getContextPath()%>/images/backOffice/merchant/resultSubmission/allOK.png" />
						</div>
						<s:if test="%{status eq 'UNMATCHED'}">
							<div class="textItalic textExtraLarge">
								Result Mismatch Please Contact to BO !!.
							</div>
						</s:if>
						<s:else>
							<div class="textItalic textLarge ">
								Result Submitted Successfully !!
							</div>
							<div class="textItalic textExtraLarge colorGreen">
								Congratulations to all the Winners!!
							</div>
						</s:else>

					</div>
				</div>
			</div>
		</div>
	<script type="text/javascript">
		
		$(document).ready(function() {
			//window.history.forward();
			window.onunload = function() {
			   window.location.reload();
			};
	
		});
	</script>


	</body>
</html>