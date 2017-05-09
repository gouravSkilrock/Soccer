<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String nameSpacePath = path+"/com/skilrock/sle/web/merchantUser/drawMgmt/Action/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/sleGamePlayAssets/dist/css/jquery-confirm.min.css" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/sleGamePlayAssets/dist/js/jquery-confirm.min.js"></script>
	<script>
		commonApp.init($);
		var drawTypeData = $('#drawType').val();
		var drawTypeList = jQuery.parseJSON(drawTypeData); 
		var evtDataMap = new Object();
	</script>
  </head> 	
  
  <body>
		<div class="container  pageWrap smallContainer">
			<div class="col-md-11" id="drawDiv">
				<div class="box box-info box-style" id="mainDiv">
					<div class="box-body" >
							<div id="divtwo">
									<form class="form-horizontal" method="post" action="#">
										<div class="form-group">
                                            <div class="col-md-4" align="right">
                                             <label class="control-label" for="inputSuccess" style="margin-top: 40px;"> Select Draw</label></div>
                                            <div class="col-md-8" align="left">
												<s:select id="drawId" headerKey="-1" headerValue="-- Please Select Draw --" name="drawId" 
														list="%{drawMasterList}" listKey="drawId"
														listValue="%{drawName+' - '+drawDateTime}" 
														cssClass=" form_select form-control">
														
												</s:select>
												<s:hidden id="drawType" name="drawType"
													value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(drawMasterList)}" />
											</div>
                                        </div>
									</form>	
							</div>
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
					
								 <div align="center" id="errorDiv" style="display:none;margin-left:32px" class="col-md-12 alert alert-danger"></div>
					
							 
						</div>
						
								
					</div>
				</div>
				</div>	
				<div class="col-md-1"></div>
				<div id="resultDivEventData" class="resultDivEventData"></div>
				<div id="resSubForm" style="display: none;">
					<s:form  theme="simple" cssClass="form-horizontal" method="post" action="boResultSubmit" id="form3">
									<s:hidden name="gameId"  value="" />
									<s:hidden name="gameTypeId" value="" />
									<s:hidden name="drawId" value="" />
									<s:hidden name="eventResult" id="eventResult" value="" />
									<div class="col-md-12 form-group text-center" align="center" id="submitBtn" >
									  <button type="button" class="btn " onclick="submitResultSubmissionForm();">Submit</button>
									 </div>
									
				</s:form>	
		</div>
							
							

<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"></script>

</body>
</html>
