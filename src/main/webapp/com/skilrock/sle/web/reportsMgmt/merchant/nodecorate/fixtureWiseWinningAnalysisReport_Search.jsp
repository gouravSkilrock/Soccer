<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Result Submission Data Search</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css" />
		<script type="text/javascript"  src="<%=request.getContextPath() %>/js/Export_Excel.js"></script>
	</head>
	<body>
		<div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
  						<div class="col-md-11">
     						<h3 class="box-title"><i class="fa fa-gear"></i>
     							<span id="other_data"></span>
     						</h3>
  						</div>
  						<div class="col-md-1" style="text-align:right">
     						<img src="<%=request.getContextPath()%>/images/backOffice/reports/excel-icon.png" width="27" id="expExcel" height="27" alt="EXCEL Download" align="right" />
							<s:form id="excelForm" action="export_to_excel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/sle/web/common/Action">
								<s:hidden id="tableData" name="exportData"></s:hidden>
							</s:form>
  						</div>
 					</div>
					<div class="box-body">
						<div class="col-md-3"></div>
						<s:if test="reportMap.size>0">
							<div class="panel panel-default col-md-12 box-panel-style"
								style="max-height: 700px; overflow: auto;">
								<table id="example" class="display" cellspacing="0" width="100%">
								<s:set name="eventDataBean" value="%{reportMap.get(1)}"></s:set>
								<s:set name="optionCodeMap" value="#eventDataBean.optionCodeMap"></s:set>
								<s:set name="size" value="#optionCodeMap.size"></s:set>
								
									<thead>
										<tr>
											<th rowspan="2"><center>Event</center></th>
											<th rowspan="2"><center>Description</center></th>
											<th colspan=<s:property value="#size"/>><center>Stakes</center></th>
											<th rowspan="2"><center>Winning %</center></th>
											<th rowspan="2"><center>Winning Team</center></th>
										</tr>
										<tr>
											<s:iterator value="#optionCodeMap">
												<th><s:property value="%{value}"/></th>
											</s:iterator>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="reportMap">
										<tr>
											<td>Event <s:property value="key"/></td>
											<td><s:property value="value.eventDescription" /></td>
											<s:set name="eventSelectionCountMap" value="value.eventCountMap"></s:set>
											<s:iterator value="#optionCodeMap">									
												<td>
													<s:property value="%{#eventSelectionCountMap.get(value)}"/>
												</td>
											</s:iterator>
											<td><s:property value="getText('{0,number,#,##0.00}', {value.winPercentage})" /></td>
											<td><s:property value="value.winningOptionCode" /></td>
										</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>
						</s:if>
						<s:else>
							<div class="alert alert-warning col-md-6"
								style="margin-bottom: 13px">
								<center>
									<b>No Record Exist !!</b>
								</center>
							</div>
						</s:else>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				var otherData = "Selected Date - "+$("#selectedDateTimePicker").val()+" | Draw - "+$("#drawId option:selected").text();
				$("#other_data").html(otherData);

				$('#example').dataTable({
					"bJQueryUI" : false,
					"bSort" : false,
					"bInfo" : false,
					"bFilter" : false,
					"bPaginate": false
				});

				$('#expExcel').click(function(){
					//$("#other_data").html(otherData+" (Home, Draw and Away are the Stakes)");
					exportToExcel('example');
					//$("#other_data").html(otherData);
				});
			});
		</script>
	</body>
</html>