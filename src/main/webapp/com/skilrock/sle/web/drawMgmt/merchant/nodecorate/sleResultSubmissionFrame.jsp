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
    
    <title>My JSP 'sleEventInsertionFrame.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
  </head>
  
  <body>
    <div class="container">
		<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Sports Game Result Submission</h3>
					</div>
					<div class="box-body" >
							<div class="panel panel-default col-md-12 box-panel-style" id="divtwo">
									<form class="form-horizontal" method="post" action="#">
										<div class="form-group">
                                            <div class="col-md-4" align="right"><label class="control-label" for="inputSuccess"> Select Draw</label></div>
                                            <div class="col-md-7" align="left" >
												<s:select id="drawId" headerKey="-1" headerValue="--Please Select--" name="drawId"
														list="%{drawMasterList}" listKey="drawId"
														listValue="%{drawName+' - '+drawDateTime}" 
														cssClass="btn dropdown-toggle selectpicker btn-default option col-md-8">
												</s:select>
												<s:hidden id="drawType" name="drawType"
													value="%{new com.skilrock.sle.common.UtilityFunctions().convertJSON(drawMasterList)}" />
											</div>
                                        </div>
									</form>	
							</div>
							<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>
							<div class="panel panel-default col-md-12 box-panel-style" id="tableDiv" style="display:none">
							
							
								<s:form  theme="simple" cssClass="form-horizontal" method="post" action="boResultSubmit" onsubmit="return frmSubmit();" id="form3">
									<s:hidden name="gameId"  value="" />
									<s:hidden name="gameTypeId" value="" />
									<s:hidden name="drawId" value="" />
									<s:hidden name="eventResult" id="eventResult" value="" />
									<table class="table table-bordered table-striped panel-table-border-style" id="tableData">
                                                                               
                                    </table>
										<hr class="panel-hr-style">
										<div class="col-md-12 right-button-div-style" align="center" ><button type="submit" class="btn btn-primary">Submit</button></div>
								</s:form>									
							</div>
							<div align="center" id="errorDiv" style="display:none;margin-left:140px" class="col-md-8 alert alert-danger"></div> 
						</div>
					</div>
				</div>
		</div>
							
							
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validation.js"></script>


<script type="text/javascript">

			$(document).ready(function() {
				var drawTypeData = $('#drawType').val();
				var drawTypeList = jQuery.parseJSON(drawTypeData);
				
				$('#drawId').change(function() {
					$('#errorDiv').html("");
					$('#errorDiv').css("display", "none");

					if($(this).val()!=-1) {
						$('#tableData').empty();
						var drawId = $(this).prop("selectedIndex");
						var eventList = drawTypeList[drawId-1].eventMasterList;
						if (eventList!=undefined && eventList.length>0 ) {
							var colsSize = eventList.length; 
							if(colsSize == 4 || colsSize == 6 ){
								var colsLength = 7;
							} else{
								var colsLength = 5;
							}
							$('#tableData').append('<tr><th colspan='+colsLength+' class="table-header-style">Insert Event Result</th></tr>'+
	                                        	   '<tr><th class="table-header-style">Event Occured</th>'+
													   '<th colspan='+(colsLength-1)+' class="table-header-style">Result</th>'+
												   '</tr>');
							$.each(eventList, function(key, eventBean) {
								var eventOptionsList = eventBean.eventOptionsList;
								if (eventOptionsList!=undefined && eventOptionsList.length>0 ) {
									var tdHtml = "";
									$.each(eventOptionsList, function(key, optionName) {
										tdHtml+= '<td><label><input type="radio" name="opt'+eventBean.eventId+'" value="'+optionName+'" id="opt'+eventBean.eventId+'"> '+optionName+'</label></td>';
									});
									$('#tableData').append('<tr><td class="table-event-style">'+eventBean.eventDisplay+'</td>'+tdHtml+'</tr>');									
								}
							});
							$('#tableDiv').css("display", "block");							
						} else {
							$('#tableData').empty();
							$('#tableDiv').css("display", "none");
							$('#jsErrorDiv').css("display", "none");
							$('#errorDiv').html("Sorry !! No events are mapped with this draw - Please try again later.");
							$('#errorDiv').css("display", "block");
						}
					} else {
						$('#tableData').empty();
						$('#tableDiv').css("display", "none");
						$('#errorDiv').css("display", "none");
						$('#jsErrorDiv').css("display", "none");
					}
				});
			});
			
			function frmSubmit(){
			 var isValid = validateResultSubmissionForm(document.getElementsByTagName('input'));
			 if(isValid){
				var resultVal="";
				var inputList = document.getElementsByTagName('input');
				for(var i=0; i<inputList.length; i++) {
					if (inputList[i].type == 'radio' && inputList[i].name.match('opt')) {
						var elem = inputList[i];
						if(elem.checked == true) {
							resultVal += elem.name.substring(3)+"_"+elem.value+",";
						}
					}
				}
				$('#form3_gameId').val($('#gameId').val());
				$('#form3_gameTypeId').val($('#gameTypeId').val());
				$('#form3_drawId').val($('#drawId').val());
				$('#eventResult').val(resultVal);
				return true;
			}else{
				$('#jsErrorDiv').html('<li>Result must be selected for each event.</li>');
				$('#jsErrorDiv').css("display","block");
				var scrollPos = $('#divtwo').offset().top;
				$(window).scrollTop(scrollPos);
				return false;
			}
		}
		</script>
</body>
</html>
