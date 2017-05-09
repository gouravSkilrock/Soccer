<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String nameSpacePath = path
			+ "/com/skilrock/sle/web/merchantUser/drawMgmt/Action/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>Sports Lottery Match List</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
</head>

<body>
	<div>
		<div class="row_div_team">
			<s:if test="%{matchListDataDrawWiseBean.size > 0}">
				<div class="box box-info box-style box-team">
					<div class="box-header box-header-style">
						<h4 class="box-title"
							style="background-color: #EDEDED; padding: 10px 12px;margin-top: 0;border-radius: 5px 5px 0px 0px;">
							<i class="fa fa-gear"></i>
							<s:property value="gameTypDispName" />
							&nbsp;Match List
						</h4>
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess"
								style="float: left; margin: 6px 12px 6px 0px;"> Select
								Draw </label>

							<div style="float: left; width: 277px;">
								<s:select id="drawMatchId" name="drawMatchId"
									list="%{matchListDataDrawWiseBean}" listKey="drawId"
									listValue="%{drawName+' - '+drawDateTime}"
									cssClass="btn dropdown-toggle selectpicker btn-default option col-md-8"
									cssStyle="width:105%;padding: 6px 5px;">
								</s:select>
								<s:hidden id="drawMatchDetailBeanList"
									name="drawMatchDetailBeanList"
									value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(matchListDataDrawWiseBean)}" />
								<s:hidden id="drawMatchDetailBeanListForApplet"
									name="drawMatchDetailBeanListForApplet"
									value="%{new com.skilrock.sle.web.merchantUser.common.SportsLotteryWebResponseData().generateMatchListDataDrawWiseWeb(matchListDataDrawWiseBean)}" />
							</div>
						</div>
						<div class="col-md-6 text-right">
							<span id="printMatchList" class="span_print_btn"
								style="
							    BORDER-TOP: rgb(211,211,211) 1px solid;
							    BORDER-RIGHT: rgb(211,211,211) 1px solid;
							    BORDER-BOTTOM: rgb(211,211,211) 1px solid;
							    FLOAT: right;
							    PADDING-BOTTOM: 5px;
							    PADDING-TOP: 5px;
							    PADDING-LEFT: 10px;
							    BORDER-LEFT: rgb(211,211,211) 1px solid;
							    PADDING-RIGHT: 10px;
							    BACKGROUND-COLOR: #efefef;
							">
							<i class="fa fa-print"></i>&nbsp;Print</span>
						</div>
					</div>
					<div class="table table-striped panel-table-border-style"
						style="float: left; margin-top: 20px;" id="tableData">
					</div>
				</div>
			</s:if>
			<s:else>
				<div class="col-md-12 alert alert-danger" style="text-align:center">Match
					not available !!</div>
			</s:else>
		</div>
	</div>
</body>
</html>
